package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnhancedLSB1 implements SteganographyStrategy {

    private static final int BYTE_MASK = 0xFF;
    private static final int WRITTEN_BYTES_PER_BYTE = 8;
    private static final int THRESHOLD_VALUE = 253;

    private List<Byte> byteList = new ArrayList<>();


    @Override
    public byte[] nextEncodedBytes(byte b, ByteBuffer buffer) {
        byteList.clear();
        int selector = 7; // Elijo los bits de izquierda a derecha, empezando por el bit 7

        while (selector >= 0) {
            int next = buffer.get() & BYTE_MASK;
            if (next <= THRESHOLD_VALUE) {
                byteList.add((byte) next);
            } else {
                next = (byte) (next & ~1);
                next |= (b & (1 << selector)) >> selector;
                byteList.add((byte) next);
                selector--;
            }
        }

        byte[] bytes = new byte[byteList.size()];

        for (int i = 0 ; i < byteList.size() ; i++) {
            bytes[i] = byteList.get(i);
        }

        return bytes;
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
    public int maximumEncodingSize(Bitmap bitmap) {
        int count = 0;
        for (byte b : bitmap.getBytes()) {
            if ((b & BYTE_MASK) >= 254) {   // Cast to int to make comparison
                count++;
            }
        }
        return count / 8; // 8 bytes por bit
    }
}
