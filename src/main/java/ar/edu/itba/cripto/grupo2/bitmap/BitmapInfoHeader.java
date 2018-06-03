package ar.edu.itba.cripto.grupo2.bitmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BitmapInfoHeader {
    public static final int INFO_HEADER_SIZE = 40;

    /**
     * The number of bytes required by the structure.
    **/
    private int biSize;

    /**
     The width of the bitmap, in pixels.
     If biCompression is BI_JPEG or BI_PNG, the biWidth member specifies the width of the decompressed JPEG or PNG image file, respectively.
     **/
    private int biWidth;

    /**
        The height of the bitmap, in pixels. If biHeight is positive, the bitmap is a bottom-up DIB and its origin is the lower-left corner. If biHeight is negative, the bitmap is a top-down DIB and its origin is the upper-left corner.
        If biHeight is negative, indicating a top-down DIB, biCompression must be either BI_RGB or BI_BITFIELDS. Top-down DIBs cannot be compressed.
        If biCompression is BI_JPEG or BI_PNG, the biHeight member specifies the height of the decompressed JPEG or PNG image file, respectively.
     */
    private int biHeight;

    /*
        The number of planes for the target device. This value must be set to 1.
    */
    private short biPlanes;

    /**
     * The number of bits-per-pixel.
     * Determines the number of bits that define each pixel and the maximum number of colors in the bitmap.
     *      24: The bitmap has a maximum of 2^24 colors, and the bmiColors member of BITMAPINFO is NULL.
     *          Each 3-byte triplet in the bitmap array represents the relative intensities of blue, green, and red, respectively, for a pixel
     */
    private short biBitCount;

    /*
        The type of compression for a compressed bottom-up bitmap (top-down DIBs cannot be compressed).
        This member can be one of the following values (...)
            BI_RGB (0x0000)	: An uncompressed format.
     */
    private int biCompression;

    /* The size, in bytes, of the image. This may be set to zero for BI_RGB bitmaps. */
    private int biSizeImage;

    /* The horizontal resolution, in pixels-per-meter, of the target device for the bitmap.
       An application can use this value to select a bitmap from a resource group that best
       matches the characteristics of the current device. */
    private int biXPelsPerMeter;

    /* The vertical resolution, in pixels-per-meter, of the target device for the bitmap. */
    private int biYPelsPerMeter;

    /* The number of color indexes in the color table that are actually used by the bitmap.
        If this value is zero, the bitmap uses the maximum number of colors corresponding to the value of the biBitCount member
        for the compression mode specified by biCompression.
      */
    private int biClrUsed;


    /* The number of color indexes that are required for displaying the bitmap. If this value is zero, all colors are required. */
    private int biClrImportant;

    public BitmapInfoHeader(byte[] header) {
        if (header.length != INFO_HEADER_SIZE) {
            throw new IllegalArgumentException("Wrong header size: expecting " + INFO_HEADER_SIZE);
        }

        ByteBuffer bb = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);

        biSize = bb.getInt();
        biWidth = bb.getInt();
        biHeight = bb.getInt();
        biPlanes = bb.getShort();
        biBitCount = bb.getShort();
        biCompression = bb.getInt();
        biSizeImage = bb.getInt();
        biXPelsPerMeter = bb.getInt();
        biYPelsPerMeter = bb.getInt();
        biClrUsed = bb.getInt();
        biClrImportant = bb.getInt();
    }

    public int getBiSize() {
        return biSize;
    }

    public int getBiWidth() {
        return biWidth;
    }

    public int getBiHeight() {
        return biHeight;
    }

    public short getBiPlanes() {
        return biPlanes;
    }

    public short getBiBitCount() {
        return biBitCount;
    }

    public int getBiCompression() {
        return biCompression;
    }

    public int getBiSizeImage() {
        return biSizeImage;
    }

    public int getBiXPelsPerMeter() {
        return biXPelsPerMeter;
    }

    public int getBiYPelsPerMeter() {
        return biYPelsPerMeter;
    }

    public int getBiClrUsed() {
        return biClrUsed;
    }

    public int getBiClrImportant() {
        return biClrImportant;
    }
}
