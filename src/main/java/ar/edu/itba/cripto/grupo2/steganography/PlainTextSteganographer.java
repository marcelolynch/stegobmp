package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;

public class PlainTextSteganographer implements Steganographer {

    private static final int FILE_SIZE_HEADER_BYTES = 4;

    private SteganographyStrategy strategy;

    private int writeSize(Message p) {
        return FILE_SIZE_HEADER_BYTES + p.getPayload().length + p.getExtension().getBytes().length + 1;
    }

    @Override
    public boolean canWrite(Bitmap bitmap, Message p) {
        return writeSize(p) <= strategy.steganographableBytes(bitmap);
    }

    @Override
    public final void write(Bitmap bitmap, Message p) throws IllegalArgumentException {
        if (!canWrite(bitmap, p)) {
            throw new IllegalArgumentException();
        }
        byte[] payload = p.getPayload();

        ByteBuffer bb = ByteBuffer.allocate(FILE_SIZE_HEADER_BYTES + payload.length + p.getExtension().length() + 1);

        bb.putInt(payload.length);
        bb.put(payload);
        bb.put(p.getExtension().getBytes());
        bb.put((byte)0);

        ByteBuffer pictureBytes = ByteBuffer.wrap(bitmap.getBytes());

        int i = 0;
        while (bb.hasRemaining() && i < bitmap.getImageByteSize()) { // TODO: Checks
            byte[] transformed = strategy.nextBytes(bb.get(), pictureBytes); // Consume pictureBytes
            for (byte transformedByte : transformed) {
                bitmap.setByte(i, transformedByte);
                i++;
            }
        }

        if (bb.hasRemaining()) {
            throw new IllegalStateException("Couldn't write all bytes");
        }

    }

    @Override
    public byte[] read(Bitmap bitmap) throws IllegalArgumentException {
        return new byte[0];
    }

}
