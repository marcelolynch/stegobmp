package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;

public interface SteganographyStrategy {

    default boolean canHold(Bitmap bitmap, int size) {
        return maximumEncodingSize(bitmap) >= size;
    }

    byte[] nextEncodedBytes(byte b, ByteBuffer buffer);
    byte nextDecodedByte(ByteBuffer buffer);
    int maximumEncodingSize(Bitmap b);
}
