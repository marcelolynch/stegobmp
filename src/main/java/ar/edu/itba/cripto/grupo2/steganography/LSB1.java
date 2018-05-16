package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;

public class LSB1 implements SteganographyStrategy {

    private static final int WRITTEN_BYTES_PER_BYTE = 8;
    private static final int LAST_BYTE_MASK = 1;

    @Override
    public byte[] nextBytes(byte b, ByteBuffer buffer) {
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
