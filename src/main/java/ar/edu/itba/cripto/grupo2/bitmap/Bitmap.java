package ar.edu.itba.cripto.grupo2.bitmap;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static ar.edu.itba.cripto.grupo2.bitmap.BitmapFileHeader.FILE_HEADER_SIZE;
import static ar.edu.itba.cripto.grupo2.bitmap.BitmapInfoHeader.INFO_HEADER_SIZE;


public class Bitmap {
    private byte[] bytes;
    private BitmapFileHeader fileHeader;
    private BitmapInfoHeader infoHeader;

    public static final int HEADER_SIZE = FILE_HEADER_SIZE + INFO_HEADER_SIZE;
    public static final int BYTES_PER_PIXEL = 3;
    private static final int COMPRESSION_BI_RGB = 0x0000; // Creo que no amerita un enum

    public static Bitmap fromFile(String path) throws IOException {
        byte[] picture = IOUtils.toByteArray(new FileInputStream(path));
        return new Bitmap(picture);
    }

    public Bitmap(byte[] imageBytes) {
        this.fileHeader = new BitmapFileHeader(Arrays.copyOfRange(imageBytes, 0, FILE_HEADER_SIZE));
        this.infoHeader = new BitmapInfoHeader(Arrays.copyOfRange(imageBytes, FILE_HEADER_SIZE, FILE_HEADER_SIZE + INFO_HEADER_SIZE));
        this.bytes = Arrays.copyOf(imageBytes, imageBytes.length);
    }

    public static boolean isUncompressed(Bitmap bmp) {
        return bmp.getInfoHeader().getBiCompression() == COMPRESSION_BI_RGB;
    }

    public byte byteAt(int position) {
        return bytes[HEADER_SIZE + position];
    }

    public void setByte(int position, byte b) {
        bytes[HEADER_SIZE + position] = b;
    }

    public int getImageByteSize() {
        int rawSize = infoHeader.getBiSizeImage();
        if (rawSize == 0) { // Puede ser cero si no hay compresion: lo calculo a mano
            return fileHeader.getFileSize() - HEADER_SIZE;
        }
        return rawSize;
    }

    public BitmapFileHeader getFileHeader() {
        return fileHeader;
    }

    public BitmapInfoHeader getInfoHeader() {
        return infoHeader;
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    public int getWidth() {
        return getInfoHeader().getBiWidth();
    }

    public int getHeight() {
        return getInfoHeader().getBiHeight();
    }

    public int getPixelCount() {
        return getWidth() * getHeight();
    }

    public byte[] getImageBytes() {
        return Arrays.copyOfRange(bytes, HEADER_SIZE, bytes.length);
    }
}
