package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnhancedLSB1 implements SteganographyStrategy {

    private static final int BYTE_MASK = 0xFF;
    private List<Byte> byteList = new ArrayList<>();

    @Override
    public byte[] nextBytes(byte b, ByteBuffer buffer) {
        byteList.clear();
        int selector = 7; // Elijo los bits de izquierda a derecha, empezando por el bit 7

        while (selector >= 0) {
            int next = buffer.get() & BYTE_MASK;
            if (next < 254) {
                byteList.add((byte)next);
            } else {
                boolean isOne = (b & (1 << selector)) != 0;
                selector--;

                if (isOne) {
                    byteList.add((byte) (next | 1));
                } else {
                    byteList.add((byte) (next & ~1));
                }
            }
        }

        byte[] bytes = new byte[byteList.size()];

        for (int i = 0 ; i < byteList.size() ; i++) {
            bytes[i] = byteList.get(i);
        }

        return bytes;
    }

    @Override
    public int steganographableBytes(Bitmap bitmap) {
        int count = 0;
        for (byte b : bitmap.getBytes()) {
            if ((b & BYTE_MASK) >= 254) {   // Cast to int to make comparison
                count++;
            }
        }
        return count / 8; // 8 bytes por bit
    }
}
