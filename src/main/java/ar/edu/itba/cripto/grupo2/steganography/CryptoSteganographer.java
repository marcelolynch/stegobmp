package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoSteganographer implements Steganographer {

    private EncryptionSettings settings;
    private final Cipher encryptionCipher;
    private final Cipher decryptionCipher;
    private final SteganographyStrategy strategy;

    public CryptoSteganographer(SteganographyStrategy strategy, EncryptionSettings settings){
        this.settings = settings;
        SecretKey key = settings.getKey();
        IvParameterSpec ivSpec = settings.getIv();

        try {
            this.encryptionCipher = Cipher.getInstance(settings.getCode());
            this.encryptionCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

            this.decryptionCipher = Cipher.getInstance(settings.getCode());
            this.decryptionCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Incorrect settings");
        }

        this.strategy = strategy;
    }

    @Override
    public boolean canWrite(Bitmap bitmap, Message message) {
        final int writable = bitmap.getImageByteSize();
        final int blockSize = encryptionCipher.getBlockSize();
        return settings.getPadding().encryptionSize(writable, blockSize) >= message.getPayload().length;
    }

    @Override
    public void write(Bitmap bitmap, Message message) throws IllegalArgumentException{
        ByteBuffer serialized = MessageSerializer.serialize(message);
        ByteBuffer picBuffer = ByteBuffer.wrap(bitmap.getImageBytes());
        int blockSize = encryptionCipher.getBlockSize();        // Esto es siempre distinto de 0 porque esta asegurado que usamos un cifrado de bloque
                                                                // (por los valores posibles de CipherType)

        // Calcular el tamaño de la encripción, cambia segun el padding
        int encryptionSize = settings.getPadding().encryptionSize(serialized.remaining(), blockSize);

        // Esteganografiar el tamaño de la encripción
        byte[] encryptionSizeBytes = new byte[4];               // 4 bytes en un int
        ByteBuffer sizeBuffer = ByteBuffer.wrap(encryptionSizeBytes).order(ByteOrder.BIG_ENDIAN);
        sizeBuffer.putInt(encryptionSize);                      // Esto modifica el arreglo size

        int writeOffset = 0;
        writeOffset += writeToBitmap(bitmap, writeOffset, picBuffer, encryptionSizeBytes);  // Se esteganografia y se consumen los bytes necesarios de picBuffer


        byte[] blockBuffer = new byte[blockSize];               // Para acumular bloques - podria hacerse de una vez pero

        while (serialized.remaining() >= blockSize) {
            serialized.get(blockBuffer, 0, blockSize);
            byte[] encrypted = encryptionCipher.update(blockBuffer);
            writeOffset += writeToBitmap(bitmap, writeOffset, picBuffer, encrypted);
        }

        byte[] lastBlock = new byte[serialized.remaining()];
        for (int i = 0 ; serialized.hasRemaining() ; i++){
            lastBlock[i] = serialized.get();
        }

        try {
            byte[] encrypted = encryptionCipher.doFinal(lastBlock);
            writeToBitmap(bitmap, writeOffset, picBuffer, encrypted);
        } catch (Exception e){
            throw new IllegalStateException();
        }

    }

    private int writeToBitmap(Bitmap bitmap, int offset, ByteBuffer readBuffer, byte[] encrypted) {
        int written = 0;
        int i;

        for (i = 0 ; i < encrypted.length && readBuffer.hasRemaining() ; i++) {
            byte[] transformed = strategy.nextEncodedBytes(encrypted[i], readBuffer); // Consume readBuffer
            for (int j = 0; j < transformed.length ; j++) {
                bitmap.setByte(offset + written, transformed[j]);
                written++;
            }
        }

        if(i < encrypted.length){
            throw new IllegalStateException();
        }

        return written;
    }

    @Override
    public Message read(Bitmap bitmap) throws IllegalArgumentException {
        ByteBuffer buffer = ByteBuffer.wrap(bitmap.getImageBytes()).order(ByteOrder.BIG_ENDIAN);
        int size = MessageSerializer.getEncodingSize(buffer, strategy);
        if(!strategy.canHold(buffer, size)){
            throw new IllegalArgumentException();
        }

        ByteArrayOutputStream decryption = new ByteArrayOutputStream();
        for(int i = 0 ; i < size ; i++){
            decryption.write(strategy.nextDecodedByte(buffer));
        }

        try {
            byte[] serializedMessage = decryptionCipher.doFinal(decryption.toByteArray());
            ByteBuffer messageBuffer = ByteBuffer.wrap(serializedMessage).order(ByteOrder.BIG_ENDIAN);
            return MessageSerializer.deserialize(messageBuffer, new IdentitySteganography());

        } catch (IllegalBlockSizeException e) {
            throw new IllegalStateException();
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException();
        }


    }
}
