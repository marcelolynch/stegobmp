package ar.edu.itba.cripto.grupo2.steganography;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

public class MessageSerializer {
    private static final int FILE_SIZE_HEADER_BYTES = 4;

    // Devuelve un ByteBuffer en modo lectura con el mensaje serializado
    public static ByteBuffer serialize(final Message message) {
        byte[] payload = message.getPayload();
        String extension = message.getExtension();
        ByteBuffer bb = ByteBuffer.allocate(FILE_SIZE_HEADER_BYTES + payload.length + extension.length() + 1)
                .order(ByteOrder.BIG_ENDIAN);
        bb.putInt(payload.length);
        bb.put(payload);
        bb.put(extension.getBytes());
        bb.put((byte)0);
        bb.flip();
        return bb;
    }

    /*
        Recibe un bytebuffer Big Endian donde se asume que fue esteganografiado un Message con la estrategia indicada,
        y lo transforma en un mensaje.
     */
    public static Message deserialize(final ByteBuffer buffer, SteganographyStrategy strategy){
        if(buffer.remaining() < FILE_SIZE_HEADER_BYTES){
            throw new IllegalArgumentException();
        }

        int length = getEncodingSize(buffer, strategy);

        if(!strategy.canHold(buffer, length)){
            throw new IllegalArgumentException();
        }

        byte[] payload = new byte[length];
        for (int i = 0 ; i < length ; i++){
            payload[i] = strategy.nextDecodedByte(buffer);
        }

        ByteArrayOutputStream extensionBytes = new ByteArrayOutputStream();

        byte next = strategy.nextDecodedByte(buffer); //buffer.get();
        while (buffer.hasRemaining() && next != 0) {
            extensionBytes.write(next);
            next = strategy.nextDecodedByte(buffer);
        }

        if (next != 0) { // Malformado - no termina en \0
            throw new IllegalArgumentException();
        }

        String extension = new String(extensionBytes.toByteArray());
        return new Message(extension, payload);
    }

    private static int getEncodingSize(ByteBuffer picBuffer, SteganographyStrategy strategy) { // Consume el buffer!
        ByteBuffer buf = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);

        for (int i = 0; i < 4 ; i++) {
            buf.put(strategy.nextDecodedByte(picBuffer));
        }

        buf.flip();
        return buf.getInt();
    }


}
