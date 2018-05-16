package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.steganography.LSB1;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LSB1Test {
    private LSB1 lsb1 = new LSB1();


    @Test
    public void lsb1Test() {
        byte[] bytes = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        byte[] processed = lsb1.nextEncodedBytes((byte)0xFF, bb);

        byte[] expected = {0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01};

        assertArrayEquals(expected, processed);

    }

    @Test
    public void lsb1Test2() {
        byte[] bytes = {0x00,(byte)0xFF,0x00,0x00,0x00,0x00, (byte) 0x8E,0x00};
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        byte[] processed = lsb1.nextEncodedBytes((byte)0x43, bb);

        byte[] expected = {0x00,(byte)0xFF,0x00,0x00,
                            0x00,0x00, (byte) 0x8F,0x01};

        assertArrayEquals(expected, processed);

    }

    @Test
    public void readMessage() {
        byte[] written = {0x01,0x00,0x01,0x01,0x01,0x01,0x01,0x00};
        ByteBuffer bb = ByteBuffer.wrap(written);
        int decoded = lsb1.nextDecodedByte(bb) & 0xFF;
        assertEquals(0xBE, decoded);
    }

    @Test
    public void steganographablelsb1Test() throws IOException {
        byte[] file = IOUtils.toByteArray(new FileInputStream("pixels.bmp"));
        Bitmap bmp = new Bitmap(file);
        assertEquals(2, lsb1.maximumEncodingSize(bmp));
    }



}

