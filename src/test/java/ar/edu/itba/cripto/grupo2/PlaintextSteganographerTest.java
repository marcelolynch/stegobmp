package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.steganography.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PlaintextSteganographerTest {
    private PlaintextSteganographer pts;

    @Test
    public void ptsTest() throws IOException {
        pts = new PlaintextSteganographer(LSB4.getInstance());

        byte[] file = IOUtils.toByteArray(new FileInputStream("resources/test/4x4.bmp"));
        Bitmap bmp = new Bitmap(file);

        byte[] payload = "hola".getBytes();   // 0x68 0x6F 0x6C 0x61

        Message msg = new Message(payload, ".txt"); // 0x2E 0x74 0x78 0x74


        assert(pts.canWrite(bmp, msg));
        pts.write(bmp, msg);


        // Size = 0x04
        byte[] stegBody = {
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x06, 0x08, 0x06, 0x0F,
                0x06, 0x0C, 0x06, 0x01, 0x02, 0x0E, 0x07, 0x04, 0x07, 0x08, 0x07, 0x04,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        };

        assertArrayEquals(stegBody, bmp.getImageBytes());
    }

    @Test
    public void ptsReadTest() throws IOException {
        pts = new PlaintextSteganographer(LSB4.getInstance());
        byte[] file = IOUtils.toByteArray(new FileInputStream("resources/test/4x4encoded.bmp"));
        Bitmap bmp = new Bitmap(file);

        Message message = pts.read(bmp);

        assertArrayEquals("hola".getBytes(), message.getPayload());
        assertEquals(".txt", message.getExtension());
    }


    @Test
    public void ladoLSB4_WriteTest() throws IOException {
        Bitmap bmp = Bitmap.fromFile("resources/test/lado.bmp");
        byte[] payload = IOUtils.toByteArray(new FileInputStream("resources/test/doc.pdf"));

        Steganographer s = new PlaintextSteganographer(LSB4.getInstance());
        Message message = new Message(payload, ".pdf");
        s.write(bmp, message);

        byte[] expected = IOUtils.toByteArray(new FileInputStream("resources/test/ladoLSB4.bmp"));

        assertArrayEquals(expected, bmp.getBytes());
    }


    @Test
    public void ladoLSB1_WriteTest() throws IOException {
        Bitmap bmp = Bitmap.fromFile("resources/test/lado.bmp");
        byte[] payload = IOUtils.toByteArray(new FileInputStream("resources/test/doc.pdf"));

        Steganographer s = new PlaintextSteganographer(LSB1.getInstance());
        Message message = new Message(payload, ".pdf");
        s.write(bmp, message);

        byte[] expected = IOUtils.toByteArray(new FileInputStream("resources/test/ladoLSB1.bmp"));

        assertArrayEquals(expected, bmp.getBytes());
    }


    @Test
    public void ladoELSB_WriteTest() throws IOException {
        Bitmap bmp = Bitmap.fromFile("resources/test/lado.bmp");
        byte[] payload = IOUtils.toByteArray(new FileInputStream("resources/test/pic.png"));

        Steganographer s = new PlaintextSteganographer(EnhancedLSB1.getInstance());
        Message message = new Message(payload, ".png");
        s.write(bmp, message);

        byte[] expected = IOUtils.toByteArray(new FileInputStream("resources/test/ladoLSBE.bmp"));

        assertArrayEquals(expected, bmp.getBytes());
    }


}

