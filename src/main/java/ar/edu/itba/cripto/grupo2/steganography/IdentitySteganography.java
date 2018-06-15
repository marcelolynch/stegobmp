package ar.edu.itba.cripto.grupo2.steganography;

import java.nio.ByteBuffer;

public class IdentitySteganography implements SteganographyStrategy {

    private static IdentitySteganography instance;

    public static IdentitySteganography getInstance(){
        if (instance == null)
            instance = new IdentitySteganography();
        return instance;
    }

    private IdentitySteganography(){};


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
