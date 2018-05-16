package ar.edu.itba.cripto.grupo2.cryptography;

public enum CipherPadding {

    PKCS5 ("PKCS5Padding");

    private String code;

    CipherPadding(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
