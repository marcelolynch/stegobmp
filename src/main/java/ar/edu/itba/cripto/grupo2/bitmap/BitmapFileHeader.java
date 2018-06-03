package ar.edu.itba.cripto.grupo2.bitmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BitmapFileHeader {
    public static final int FILE_HEADER_SIZE = 14;

    /* The header field used to identify the BMP and DIB file is 0x42 0x4D in hexadecimal, same as BM in ASCII.
        The following entries are possible:
            BM – Windows 3.1x, 95, NT, ... etc.
         (...) */
    private short headerField;

    /* The size of the BMP file in bytes */
    private int fileSize;

    /* Two 16 bit reserved values */
    private int reserved;

    /* The offset, i.e. starting address, of the byte where the bitmap image data (pixel array) can be found.
    *  Nos interesa que sea 54
    */
    private int offset;

    public BitmapFileHeader(byte[] header) {
        if (header.length != FILE_HEADER_SIZE) {
            throw new IllegalArgumentException("Wrong header size: expecting " + FILE_HEADER_SIZE);
        }

        ByteBuffer bb = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);
        headerField  = bb.getShort();
        fileSize = bb.getInt();
        reserved = bb.getInt();
        offset = bb.getShort();
    }

    public short getHeaderField() {
        return headerField;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getOffset() {
        return offset;
    }
}
