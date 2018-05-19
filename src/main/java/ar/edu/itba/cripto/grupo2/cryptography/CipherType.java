package ar.edu.itba.cripto.grupo2.cryptography;

public enum CipherType {

    AES_128 ("AES128", 16),
    AES_192 ("AES192", 24),
    AES_256 ("AES256", 32),
    DES     ("DES", 8);

    private final String code;
    private final int keyLenght; //En bytes
    private final String shortCode;

    CipherType(String code, int keyLength){
        this.code = code;
        this.keyLenght = keyLength;
        this.shortCode = code.substring(0,3);
    }

    public String getCode() {
        return code;
    }

    public String getShortCode() {
        return shortCode;
    }

    public int getKeyLenght() { return keyLenght;}
}
