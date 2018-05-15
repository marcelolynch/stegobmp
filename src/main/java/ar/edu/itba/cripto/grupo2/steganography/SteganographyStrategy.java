package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;

public interface SteganographyStrategy {
    byte[] nextBytes(byte b, ByteBuffer buffer);
    byte nextByteDecode(ByteBuffer buffer);
    int steganographableBytes(Bitmap b);
}
