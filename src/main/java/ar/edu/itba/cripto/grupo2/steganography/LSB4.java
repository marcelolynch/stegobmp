package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;

public class LSB4 implements SteganographyStrategy {

    private static final int WRITTEN_BYTES_PER_BYTE = 2;
    private static final int LAST_FOUR_BITS_MASK = 0x0F;
    private static final int FIRST_FOUR_BITS_MASK = 0xF0;
    private static final int BYTE_MASK = 0xFF;

    @Override
    public byte[] nextBytes(byte b, ByteBuffer buffer) {
        int targetByte = b & BYTE_MASK; // Las mascaras funcionan en int
        byte[] bytes = new byte[WRITTEN_BYTES_PER_BYTE];

        int next = buffer.get() & BYTE_MASK;
        int selected = (targetByte & FIRST_FOUR_BITS_MASK) >>> 4;
        bytes[0] = (byte) (next & ~FIRST_FOUR_BITS_MASK | selected);

        next = buffer.get() & BYTE_MASK;
        selected = targetByte & LAST_FOUR_BITS_MASK;
        int cleared = next & ~LAST_FOUR_BITS_MASK;
        bytes[1] = (byte) (cleared | selected);

        return bytes;
    }

    @Override
    public int steganographableBytes(Bitmap b) {
        return b.getImageByteSize() / WRITTEN_BYTES_PER_BYTE;
    }
}
