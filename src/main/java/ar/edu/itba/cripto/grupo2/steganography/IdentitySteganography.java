package ar.edu.itba.cripto.grupo2.steganography;

import java.nio.ByteBuffer;

public class IdentitySteganography implements SteganographyStrategy {

    @Override
    public byte[] nextEncodedBytes(byte b, ByteBuffer buffer) {
        buffer.get(); // Read 1 byte
        return new byte[]{b};
    }

    @Override
    public byte nextDecodedByte(ByteBuffer buffer) {
        return buffer.get();
    }

    @Override
    public int maximumEncodingSize(ByteBuffer b) {
        return b.remaining();
    }
}
