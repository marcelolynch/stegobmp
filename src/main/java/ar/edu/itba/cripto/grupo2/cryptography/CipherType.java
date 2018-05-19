package ar.edu.itba.cripto.grupo2.cryptography;

public enum CipherType {

    AES_128 ("AES", 16),
    AES_192 ("AES", 24),
    AES_256 ("AES", 32),
    DES     ("DES", 8);

    private final int keyLength; //En bytes
    private final String code;

    CipherType(String code, int keyLength){
        this.keyLength = keyLength;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getKeyLength() { return keyLength;}
}
