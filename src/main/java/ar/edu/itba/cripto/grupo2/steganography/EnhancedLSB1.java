package ar.edu.itba.cripto.grupo2.steganography;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class EnhancedLSB1 implements SteganographyStrategy {

    private static final int BYTE_MASK = 0xFF;
    private static final int WRITTEN_BYTES_PER_BYTE = 8;
    private static final int THRESHOLD_VALUE = 253;



    @Override
    public byte[] nextEncodedBytes(byte b, ByteBuffer buffer) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        int selector = 7; // Elijo los bits de izquierda a derecha, empezando por el bit 7

        while (selector >= 0) {
            int next = buffer.get() & BYTE_MASK;
            if (next <= THRESHOLD_VALUE) {
                bytes.write(next);
            } else {
                next = next & ~1;
                next |= (b & (1 << selector)) >> selector;
                bytes.write(next);
                selector--;
            }
        }

        return bytes.toByteArray();
    }

    @Override
    public byte nextDecodedByte(ByteBuffer buffer) {
        int b = 0;

        int i = 0;
        while (i < WRITTEN_BYTES_PER_BYTE) {
            int next = buffer.get() & BYTE_MASK;
            if (next > THRESHOLD_VALUE) {
                next = next & 1;
                b = (b << 1) | next;
                i++;
            }
        }

        return (byte)b;
    }


    @Override
    public int maximumEncodingSize(ByteBuffer buffer) {
        int count = 0;
        buffer.mark();
        while(buffer.hasRemaining()) {
            byte b = buffer.get();
            if ((b & BYTE_MASK) >= 254) {   // Cast to int to make comparison
                count++;
            }
        }
        buffer.reset();
        return count / 8; // 8 bytes por bit
    }
}
