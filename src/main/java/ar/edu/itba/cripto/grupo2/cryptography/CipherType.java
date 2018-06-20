package ar.edu.itba.cripto.grupo2.cryptography;

public enum CipherType {

    AES_128 ("AES", 16, 16),
    AES_192 ("AES", 24, 16),
    AES_256 ("AES", 32, 16),
    DES     ("DES", 8, 8);

    private final String code;
    private final int keyLength; // En bytes
    private int blockSize; // En bytes

    CipherType(String code, int keyLength, int blockSize) {
        this.keyLength = keyLength;
        this.code = code;
        this.blockSize = blockSize;
    }

    public String getCode() {
        return code;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public int getBlockSize() {
        return blockSize;
    }
}
