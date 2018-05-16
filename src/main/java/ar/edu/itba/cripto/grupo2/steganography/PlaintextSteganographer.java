package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class PlaintextSteganographer implements Steganographer {

    private static final int FILE_SIZE_HEADER_BYTES = 4;

    private SteganographyStrategy strategy;

    public PlaintextSteganographer(SteganographyStrategy strategy) {
        this.strategy = strategy;
    }

    private int writeSize(Message p) {
        return FILE_SIZE_HEADER_BYTES + p.getPayload().length + p.getExtension().getBytes().length + 1;
    }

    @Override
    public boolean canWrite(Bitmap bitmap, Message p) {
        return strategy.canHold(bitmap, writeSize(p));
    }

    @Override
    public final void write(Bitmap bitmap, Message p) throws IllegalArgumentException {
        if (!canWrite(bitmap, p)) {
            throw new IllegalArgumentException();
        }
        byte[] payload = p.getPayload();

        ByteBuffer bb = ByteBuffer.allocate(FILE_SIZE_HEADER_BYTES + payload.length + p.getExtension().length() + 1)
                                  .order(ByteOrder.BIG_ENDIAN);
        bb.putInt(payload.length);
        bb.put(payload);
        bb.put(p.getExtension().getBytes());
        bb.put((byte)0);
        bb.flip();

        ByteBuffer pictureBytes = ByteBuffer.wrap(bitmap.getImageBytes());

        int i = 0;
        while (bb.hasRemaining() && i < bitmap.getImageByteSize()) { // TODO: Checks
            byte[] transformed = strategy.nextEncodedBytes(bb.get(), pictureBytes); // Consume pictureBytes
            for (byte transformedByte : transformed) {
                bitmap.setByte(i, transformedByte);
                i++;
            }
        }
        if (bb.hasRemaining()) {
            throw new IllegalStateException("Couldn't write all bytes");
        }

    }

    @Override
    public Message read(Bitmap bitmap) throws IllegalArgumentException { // a.k.a el metodo mas imperativo de tu vida
        // TODO: Es probable que sea bueno extraer esto para reusarlo en el que encripta
        if (!strategy.canHold(bitmap, FILE_SIZE_HEADER_BYTES)) {   // Imposible que sea una esteganografia valida
            throw new IllegalArgumentException();
        }

        // Recupero el tamaño del texto
        byte[] image = bitmap.getImageBytes();
        ByteBuffer picBuffer = ByteBuffer.wrap(bitmap.getImageBytes());
        int size = getEncodingSize(picBuffer);


        if(!strategy.canHold(bitmap, FILE_SIZE_HEADER_BYTES + size)){ // Malformado o no era una esteganografia valida
            throw new IllegalArgumentException();
        }

        byte[] decoded = new byte[size];
        for (int i = 0 ; i < size ; i++){
            decoded[i] = strategy.nextDecodedByte(picBuffer);
        }

        // Recupero la extensión
        List<Byte> extensionByteList = new ArrayList<>();
        byte next = strategy.nextDecodedByte(picBuffer);
        while (picBuffer.hasRemaining() && next != 0) {
            extensionByteList.add(next);
            next = strategy.nextDecodedByte(picBuffer);
        }


        if (next != 0) { // Malformado - no termina en \0
            throw new IllegalArgumentException();
        }

        byte[] bytes = new byte[extensionByteList.size()];  // TODO: Esto de mejor forma???
        for (int i = 0 ; i < extensionByteList.size() ; i++) {
            bytes[i] = extensionByteList.get(i);
        }

        String extension = new String(bytes);

        return new Message(extension, decoded);
    }


    private int getEncodingSize(ByteBuffer picBuffer) { // Consume el buffer!
        ByteBuffer buf = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);

        for (int i = 0; i < 4 ; i++) {
            buf.put(strategy.nextDecodedByte(picBuffer));
        }

        buf.flip();
        return buf.getInt();
    }

}
