package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;

public class LSB1 implements SteganographyStrategy {

    private static final int WRITTEN_BYTES_PER_BYTE = 8;
    private static final int LAST_BYTE_MASK = 1;

    @Override
    public byte[] nextBytes(byte b, ByteBuffer buffer) {
        byte[] bytes = new byte[WRITTEN_BYTES_PER_BYTE];

        int selector = 7; // Elijo los bits de izquierda a derecha, empezando por el bit 7

        for (int i = 0 ; i < WRITTEN_BYTES_PER_BYTE ; i++) {
            byte next = buffer.get();
            boolean isOne = (b&(1 << selector)) != 0;
            selector--;

            if (isOne) {
                bytes[i] = (byte) (next | 1);
            } else {
                bytes[i] = (byte) (next & ~1);
            }
        }

        return bytes;
    }

    @Override
    public byte nextByteDecode(ByteBuffer buffer) {
        int b = 0;

        for(int i = 0 ; i < WRITTEN_BYTES_PER_BYTE ; i++){
            int next = buffer.get() & LAST_BYTE_MASK;
            b = (b << 1) | next;
        }

        return (byte)b;
    }

    @Override
    public int steganographableBytes(Bitmap b) {
        return b.getImageByteSize() / WRITTEN_BYTES_PER_BYTE;
    }
}
