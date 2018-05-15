package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.steganography.LSB1;
import ar.edu.itba.cripto.grupo2.steganography.LSB4;
import ar.edu.itba.cripto.grupo2.steganography.Message;
import ar.edu.itba.cripto.grupo2.steganography.PlaintextSteganographer;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PlaintextSteganographerTest {
    private PlaintextSteganographer pts;

    @Test
    public void ptsTest() throws IOException {
        PlaintextSteganographer pts = new PlaintextSteganographer(new LSB4());

        byte[] file = IOUtils.toByteArray(new FileInputStream("4x4.bmp"));
        Bitmap bmp = new Bitmap(file);

        byte[] payload = "hola".getBytes();   // 0x68 0x6F 0x6C 0x61

        Message msg = new Message(".txt", payload); // 0x2E 0x74 0x78 0x74

        assert(pts.canWrite(bmp, msg));
        pts.write(bmp, msg);

        // Size = 0x04
        byte[] stegBody = {
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x06, 0x08, 0x06, 0x0F,
                0x06, 0x0C, 0x06, 0x01, 0x02, 0x0E, 0x07, 0x04, 0x07, 0x08, 0x07, 0x04,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        };

        assert(Arrays.equals(stegBody, bmp.getImageBytes()));
    }

}

