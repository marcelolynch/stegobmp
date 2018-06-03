package ar.edu.itba.cripto.grupo2.steganography;

import java.nio.ByteBuffer;

public interface SteganographyStrategy {

    default boolean canHold(ByteBuffer buffer, int size) {
        return maximumEncodingSize(buffer) >= size;
    }

    byte[] nextEncodedBytes(byte b, ByteBuffer buffer);
    byte nextDecodedByte(ByteBuffer buffer);
    int maximumEncodingSize(ByteBuffer b);

}
