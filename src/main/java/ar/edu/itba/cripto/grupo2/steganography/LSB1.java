package ar.edu.itba.cripto.grupo2.steganography;

import java.nio.ByteBuffer;

public class LSB1 implements SteganographyStrategy {

    private static final int WRITTEN_BYTES_PER_BYTE = 8;
    private static final int LAST_BYTE_MASK = 1;

    private static LSB1 instance;

    public static LSB1 getInstance(){
        if (instance == null)
            instance = new LSB1();
        return instance;
    }

    private LSB1(){};

    @Override
    public byte[] nextEncodedBytes(byte b, ByteBuffer buffer) {
        byte[] bytes = new byte[WRITTEN_BYTES_PER_BYTE];
        int k = 0;
        for (int i = WRITTEN_BYTES_PER_BYTE - 1; i >= 0; i--) {
            byte next = buffer.get();
            next = (byte) (next & ~1); // borra bit menos significativo de next
            next |= (b & (1 << i)) >> i; // selecciona un bit en b, lo ubica en el menos significativo de next
            bytes[k++] = next;
        }

        return bytes;
    }

    @Override
    public byte nextDecodedByte(ByteBuffer buffer) {
        int b = 0;

        for (int i = 0 ; i < WRITTEN_BYTES_PER_BYTE ; i++) {
            int next = buffer.get() & LAST_BYTE_MASK;
            b = (b << 1) | next;
        }

        return (byte)b;
    }

    @Override
    public int maximumEncodingSize(ByteBuffer b) {
        return b.remaining() / WRITTEN_BYTES_PER_BYTE;
    }
}
