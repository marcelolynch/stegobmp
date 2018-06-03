package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.steganography.IdentitySteganography;
import ar.edu.itba.cripto.grupo2.steganography.Message;
import ar.edu.itba.cripto.grupo2.steganography.MessageSerializer;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MessageSerializerTest {

    @Test
    public void serializeTest(){
        Message message = new Message("hola".getBytes(), ".txt");

        byte[] expectedSerialization = {
                0x00, 0x00, 0x00, 0x04,         // longitud: 4
                0x68, 0x6F, 0x6C, 0x61,         // mensaje: hola
                0x2E, 0x74, 0x78, 0x74, 0x00    // extension: .txt\0
        };
        ByteBuffer buf = MessageSerializer.serialize(message);
        byte output[] = new byte[buf.remaining()];
        buf.get(output, 0, buf.remaining());
        assertArrayEquals(expectedSerialization, output);

    }


    @Test
    public void deserializeTest(){
        byte[] serialization = {
                0x00, 0x00, 0x00, 0x04,         // longitud: 4
                0x68, 0x6F, 0x6C, 0x61,         // mensaje: hola
                0x2E, 0x74, 0x78, 0x74, 0x00    // extension: .txt\0
        };

        ByteBuffer bb = ByteBuffer.wrap(serialization);

        Message m = MessageSerializer.deserialize(bb, IdentitySteganography.getInstance());

        assertArrayEquals("hola".getBytes(), m.getPayload());
        assertEquals(".txt", m.getExtension());
    }

}
