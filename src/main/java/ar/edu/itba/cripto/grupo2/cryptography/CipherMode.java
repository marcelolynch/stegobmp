package ar.edu.itba.cripto.grupo2.cryptography;

public enum CipherMode {
    ECB ("ECB"),
    CFB ("CFB"),
    OFB ("OFB"),
    CBC ("CBC");

    private final String code;

    CipherMode(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
