package ar.edu.itba.cripto.grupo2.steganography;

import java.nio.ByteBuffer;

public class LSB4 implements SteganographyStrategy {

    private static final int WRITTEN_BYTES_PER_BYTE = 2;
    private static final int LAST_FOUR_BITS_MASK = 0x0F;
    private static final int FIRST_FOUR_BITS_MASK = 0xF0;
    private static final int BYTE_MASK = 0xFF;


    private static LSB4 instance;

    public static LSB4 getInstance(){
        if (instance == null)
            instance = new LSB4();
        return instance;
    }

    private LSB4(){};


    @Override
    public byte[] nextEncodedBytes(byte b, ByteBuffer buffer) {
        int targetByte = b & BYTE_MASK; // Las mascaras funcionan en int
        byte[] bytes = new byte[WRITTEN_BYTES_PER_BYTE];

        int next = buffer.get() & BYTE_MASK;
        int selected = (targetByte & FIRST_FOUR_BITS_MASK) >>> 4;
        int masked = next & ~LAST_FOUR_BITS_MASK;
        bytes[0] = (byte) (masked | selected);

        next = buffer.get() & BYTE_MASK;
        selected = targetByte & LAST_FOUR_BITS_MASK;
        masked = next & ~LAST_FOUR_BITS_MASK;
        bytes[1] = (byte) (masked | selected);

        return bytes;
    }


    @Override
    public byte nextDecodedByte(ByteBuffer buffer) {
        int high = buffer.get() & LAST_FOUR_BITS_MASK;
        int low = buffer.get() & LAST_FOUR_BITS_MASK;
        return (byte)(high << 4 | low);
    }

    @Override
    public int maximumEncodingSize(ByteBuffer b) {
        return b.remaining() / WRITTEN_BYTES_PER_BYTE;
    }
}
