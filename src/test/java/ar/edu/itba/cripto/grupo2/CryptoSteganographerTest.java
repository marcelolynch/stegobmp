package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.cryptography.CipherMode;
import ar.edu.itba.cripto.grupo2.cryptography.CipherType;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;
import ar.edu.itba.cripto.grupo2.steganography.*;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CryptoSteganographerTest {

    private CryptoSteganographer cs;

    public void encryptionHelper() throws Exception { // TODO: Borrar

        byte[] encKey =  {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        SecretKey desKey = new SecretKeySpec(encKey, CipherType.DES.getCode());

        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        EncryptionSettings settings = new EncryptionSettings(CipherType.DES, CipherMode.CBC, desKey, ivspec);

        Cipher cipher = Cipher.getInstance(settings.getCode());
        cipher.init(Cipher.ENCRYPT_MODE, desKey, ivspec);


        byte[] realToEncrypt = {
            0x00, 0x00, 0x00, 0x04,         // longitud: 4
            0x68, 0x6F, 0x6C, 0x61,         // mensaje: hola
            0x2E, 0x74, 0x78, 0x74, 0x00    // extension: .txt\0
        };


        byte[] encrypted = cipher.doFinal(realToEncrypt);

        cipher.init(Cipher.DECRYPT_MODE, desKey, ivspec);

        byte[] decr = cipher.doFinal(encrypted);

        for(byte b: encrypted){
            System.out.printf("%02X ", b);
        }

        // Resulta el cifrado
        // 94 F8 B9 B8 83 CB 48 C7 10 B3 A3 47 E8 5C 6C 68

        // De tamaño 16 bytes (codificado como 00 00 00 10)

        // O sea se debe esteganografiar
        // 00 00 00 10 94 F8 B9 B8 83 CB 48 C7 10 B3 A3 47 E8 5C 6C 68

    }


    @Before
    public void init(){
        byte[] encKey =  {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        byte[] iv =  {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        SecretKey desKey = new SecretKeySpec(encKey, CipherType.DES.getCode());
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        EncryptionSettings settings = new EncryptionSettings(CipherType.DES, CipherMode.CBC, desKey, ivSpec);
        cs = new CryptoSteganographer(LSB4.getInstance(), settings);
    }

    @Test
    public void cryptoStegoWriteTest() throws IOException {
        byte[] file = IOUtils.toByteArray(new FileInputStream("resources/test/4x4.bmp"));
        Bitmap bmp = new Bitmap(file);

        byte[] payload = "hola".getBytes();   // 0x68 0x6F 0x6C 0x61
        Message msg = new Message(payload, ".txt"); // 0x2E 0x74 0x78 0x74

        ByteBuffer bb = MessageSerializer.serialize(msg);
        cs.write(bmp, msg);

        // Size = 0x04
        byte[] stegBody = {
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x09, 0x04, 0x0F, 0x08,
                0x0B, 0x09, 0x0B, 0x08, 0x08, 0x03, 0x0C, 0x0B, 0x04, 0x08, 0x0C, 0x07,
                0x01, 0x00, 0x0B, 0x03, 0x0A, 0x03, 0x04, 0x07, 0x0E, 0x08, 0x05, 0x0C,
                0x06, 0x0C, 0x06, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        };

        assertArrayEquals(stegBody, bmp.getImageBytes());
    }


    @Test
    public void ptsReadTest() throws IOException {
        byte[] file = IOUtils.toByteArray(new FileInputStream("resources/test/4x4crypto.bmp"));
        Bitmap bmp = new Bitmap(file);

        Message message = cs.read(bmp);

        assertArrayEquals("hola".getBytes(), message.getPayload());
        assertEquals(".txt", message.getExtension());
    }


    @Test
    public void ladoLSB4aes256cbc_ReadTest() throws IOException {
        Bitmap bmp = Bitmap.fromFile("resources/test/ladoLSB4aes256cbc.bmp");
        byte[] expected = IOUtils.toByteArray(new FileInputStream("resources/test/doc.pdf"));

        EncryptionSettings es = new EncryptionSettings(CipherType.AES_256, CipherMode.CBC,"secreto");
        Steganographer s = new CryptoSteganographer(LSB4.getInstance(), es);

        Message message = s.read(bmp);

        assertArrayEquals(expected, message.getPayload());
        assertEquals(".pdf", message.getExtension());
    }


    @Test
    public void ladoLSB4aes256cbc_WriteTest() throws IOException {
        Bitmap bmp = Bitmap.fromFile("resources/test/lado.bmp");
        byte[] payload = IOUtils.toByteArray(new FileInputStream("resources/test/doc.pdf"));
        EncryptionSettings es = new EncryptionSettings(CipherType.AES_256, CipherMode.CBC,"secreto");
        Steganographer s = new CryptoSteganographer(LSB4.getInstance(), es);

        Message message = new Message(payload, ".pdf");

        s.write(bmp, message);

        byte[] expected = IOUtils.toByteArray(new FileInputStream("resources/test/ladoLSB4aes256cbc.bmp"));
        assertArrayEquals(expected, bmp.getBytes());
    }



    @Test
    public void ladoLSB4aes256ofb_ReadTest() throws IOException {
        Bitmap bmp = Bitmap.fromFile("resources/test/ladoLSB4aes256ofb.bmp");
        byte[] expected = IOUtils.toByteArray(new FileInputStream("resources/test/doc.pdf"));

        EncryptionSettings es = new EncryptionSettings(CipherType.AES_256, CipherMode.OFB,"secreto");
        Steganographer s = new CryptoSteganographer(LSB4.getInstance(), es);

        Message message = s.read(bmp);

        assertArrayEquals(expected, message.getPayload());
        assertEquals(".pdf", message.getExtension());
    }


    @Test
    public void ladoLSB4aes256ofb_WriteTest() throws IOException {
        Bitmap bmp = Bitmap.fromFile("resources/test/lado.bmp");
        byte[] payload = IOUtils.toByteArray(new FileInputStream("resources/test/doc.pdf"));
        EncryptionSettings es = new EncryptionSettings(CipherType.AES_256, CipherMode.OFB,"secreto");
        Steganographer s = new CryptoSteganographer(LSB4.getInstance(), es);
        Message message = new Message(payload, ".pdf");

        s.write(bmp, message);

        byte[] expected = IOUtils.toByteArray(new FileInputStream("resources/test/ladoLSB4aes256ofb.bmp"));
        assertArrayEquals(expected, bmp.getBytes());
    }






}
