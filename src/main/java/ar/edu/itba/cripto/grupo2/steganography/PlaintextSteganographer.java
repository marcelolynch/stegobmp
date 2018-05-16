package ar.edu.itba.cripto.grupo2.steganography;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PlaintextSteganographer implements Steganographer {

    private static final int FILE_SIZE_HEADER_BYTES = 4;

    private SteganographyStrategy strategy;

    public PlaintextSteganographer(SteganographyStrategy strategy) {
        this.strategy = strategy;
    }

    private int writeSize(Message p) {
        return FILE_SIZE_HEADER_BYTES + p.getPayload().length + p.getExtension().getBytes().length + 1;
    }

    @Override
    public boolean canWrite(Bitmap bitmap, Message p) {
        ByteBuffer bb = ByteBuffer.wrap(bitmap.getImageBytes());
        return strategy.canHold(bb, writeSize(p));
    }

    @Override
    public final void write(Bitmap bitmap, Message p) throws IllegalArgumentException {
        if (!canWrite(bitmap, p)) {
            throw new IllegalArgumentException();
        }
        byte[] payload = p.getPayload();

        ByteBuffer toWrite = MessageSerializer.serialize(p);
        ByteBuffer pictureBytes = ByteBuffer.wrap(bitmap.getImageBytes());

        int i = 0;
        while (toWrite.hasRemaining() && i < bitmap.getImageByteSize()) {
            byte[] transformed = strategy.nextEncodedBytes(toWrite.get(), pictureBytes); // Consume pictureBytes
            for (byte transformedByte : transformed) {
                bitmap.setByte(i, transformedByte);
                i++;
            }
        }
        if (toWrite.hasRemaining()) {
            throw new IllegalStateException("Couldn't write all bytes");
        }

    }

    @Override
    public Message read(Bitmap bitmap) throws IllegalArgumentException {
        ByteBuffer buffer = ByteBuffer.wrap(bitmap.getImageBytes()).order(ByteOrder.BIG_ENDIAN);
        return MessageSerializer.deserialize(buffer, strategy);
    }

}
