package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.steganography.LSB4;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LSB4Test {

    private LSB4 lsb4 = new LSB4();

    @Test
    public void lsb4Test() {
        byte[] bytes = {0x00,0x00};
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        byte[] processed = lsb4.nextEncodedBytes((byte)0xFF, bb);

        byte[] expected = {0x0F,0x0F};

        assertArrayEquals(expected, processed);

    }

    @Test
    public void lsb4Test2() {
        byte[] bytes = {0x00,(byte)0xFF,0x7D,0x6A};
        ByteBuffer bb = ByteBuffer.wrap(bytes);

        byte[] processed = lsb4.nextEncodedBytes((byte)0x43, bb);
        byte[] expected = {0x04,(byte)0xF3};

        assertArrayEquals(expected, processed);

        processed = lsb4.nextEncodedBytes((byte)0xBB, bb);
        expected = new byte[]{0x7B, (byte)0x6B};

        assertArrayEquals(expected, processed);
    }


    @Test
    public void lsb4readTest(){
        byte[] encoded = {0x04,(byte)0xF3, 0x7B, (byte)0x6B};
        ByteBuffer bb = ByteBuffer.wrap(encoded);

        int decoded = lsb4.nextDecodedByte(bb) & 0xFF;
        assertEquals(0x43, decoded);

        decoded = lsb4.nextDecodedByte(bb) & 0xFF;
        assertEquals(0xBB, decoded);

    }

    @Test
    public void steganographablelsb1Test() throws IOException {
        byte[] file = IOUtils.toByteArray(new FileInputStream("resources/test/pixels.bmp"));
        Bitmap bmp = new Bitmap(file);
        assertEquals(8, lsb4.maximumEncodingSize(bmp));
    }



}
