package ar.edu.itba.cripto.grupo2.cryptography;

public enum CipherType {

    AES_128 ("AES128"),
    AES_192 ("AES192"),
    AES_258 ("AES258"),
    DES     ("DES");

    private final String code;
    CipherType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
