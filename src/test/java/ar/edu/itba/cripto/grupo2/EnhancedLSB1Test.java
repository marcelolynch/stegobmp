package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.steganography.EnhancedLSB1;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EnhancedLSB1Test {

    private EnhancedLSB1 elsb1 = EnhancedLSB1.getInstance();

    @Test
    public void elsb1Test() {
        byte[] bytes = {(byte)0xFF,
                        0x00, 0x01, 0x00, 0x01,
                        (byte)0xFE,(byte)0xFE,
                        0x00, 0x77, 0x68,
                        (byte)0xFE,(byte)0xFF,(byte)0xFF,(byte)0xFE,(byte)0xFF };

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        byte[] processed = elsb1.nextEncodedBytes((byte)0x49, bb); // 0100 1001

        byte[] expected = {(byte)0xFE,
                0x00, 0x01, 0x00, 0x01,
                (byte)0xFF,(byte)0xFE,
                0x00, 0x77, 0x68,
                (byte)0xFE,(byte)0xFF,(byte)0xFE,(byte)0xFE,(byte)0xFF };

        assertArrayEquals(expected, processed);

    }

    @Test
    public void elsb1ReadTest() {

        byte[] encoded = {(byte)0xFE,
                0x00, 0x01, 0x00, 0x01,
                (byte)0xFF,(byte)0xFE,
                0x00, 0x77, 0x68, 0x10,
                (byte)0xFE,(byte)0xFF,(byte)0xFE,(byte)0xFE,(byte)0xFF };


        ByteBuffer bb = ByteBuffer.wrap(encoded);
        byte decoded = elsb1.nextDecodedByte(bb); // 0100 1001
        assertEquals(0x49, decoded);
    }


    @Test
    public void steganographableELSB1Test() throws IOException {
        byte[] file = IOUtils.toByteArray(new FileInputStream("resources/test/whitesquare.bmp"));
        Bitmap bmp = new Bitmap(file);
        assertEquals(bmp.getImageByteSize()/8, elsb1.maximumEncodingSize(ByteBuffer.wrap(bmp.getImageBytes()))); // Todos los pixeles son blancos, puedo meter cosas en todos
    }


}
