package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class BitmapTest {
    private byte[] rawBitmap = {
            0x42, 0x4D,
            0x46, 0x00, 0x00, 0x00,
            0x00, 0x00,
            0x00, 0x00,
            0x36, 0x00, 0x00, 0x00,
            0x28, 0x00, 0x00, 0x00,
            0x02, 0x00, 0x00, 0x00,
            0x02, 0x00, 0x00, 0x00,
            0x01, 0x00,
            0x18, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x10, 0x00, 0x00, 0x00,
            0x13, 0x0B, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, (byte)0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            0x00, 0x00, // Padding
            (byte)0xFF, 0x00, 0x00,
            0x00, (byte)0xFF, 0x00,
            0x00, 0x00 // Padding
    };

    private static final int PADDING = 4;
    private static final int IMAGE_SIZE = 4*Bitmap.BYTES_PER_PIXEL + PADDING;

    @Test
    public void bitmapTest() {
      //  byte[] file = IOUtils.toByteArray(new FileInputStream("pixels.bmp"));
        Bitmap bmp = new Bitmap(rawBitmap);
        assertEquals(Bitmap.HEADER_SIZE + IMAGE_SIZE, bmp.getFileHeader().getFileSize());
        assertEquals(0x4D42, bmp.getFileHeader().getHeaderField()); // Header field == BM
        assertEquals(54, bmp.getFileHeader().getOffset());

        assertEquals(16, bmp.getImageByteSize());
        assertEquals(2, bmp.getHeight());
        assertEquals(2, bmp.getHeight());
        assertEquals(4, bmp.getPixelCount());

        assertTrue(Bitmap.isUncompressed(bmp));
    }


    @Test
    public void bitmapTestWhiteSquare() throws IOException{
       byte[] file = IOUtils.toByteArray(new FileInputStream("resources/test/whitesquare.bmp"));
        Bitmap bmp = new Bitmap(file);

        assertEquals(54, bmp.getFileHeader().getOffset());
        assertEquals(100, bmp.getHeight());
        assertEquals(100, bmp.getWidth());
        assertEquals(100*100, bmp.getPixelCount());
    }


}
