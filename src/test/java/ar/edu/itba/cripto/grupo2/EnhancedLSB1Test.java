package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.steganography.EnhancedLSB1;
import ar.edu.itba.cripto.grupo2.steganography.LSB4;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EnhancedLSB1Test {

    private EnhancedLSB1 elsb1 = new EnhancedLSB1();

    @Test
    public void elsb1Test() {
        byte[] bytes = {(byte)0xFF,
                        0x00, 0x01, 0x00, 0x01,
                        (byte)0xFE,(byte)0xFE,
                        0x00, 0x77, 0x68,
                        (byte)0xFE,(byte)0xFF,(byte)0xFF,(byte)0xFE,(byte)0xFF };

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        byte[] processed = elsb1.nextBytes((byte)0x49, bb); // 0100 1001

        byte[] expected = {(byte)0xFE,
                0x00, 0x01, 0x00, 0x01,
                (byte)0xFF,(byte)0xFE,
                0x00, 0x77, 0x68,
                (byte)0xFE,(byte)0xFF,(byte)0xFE,(byte)0xFE,(byte)0xFF };

        assert(Arrays.equals(expected, processed));

    }

    @Test
    public void steganographableELSB1Test() throws IOException {
        byte[] file = IOUtils.toByteArray(new FileInputStream("whitesquare.bmp"));
        Bitmap bmp = new Bitmap(file);
        assertEquals(bmp.getImageByteSize()/8, elsb1.steganographableBytes(bmp)); // Todos los pixeles son blancos, puedo meter cosas en todos
    }


}
