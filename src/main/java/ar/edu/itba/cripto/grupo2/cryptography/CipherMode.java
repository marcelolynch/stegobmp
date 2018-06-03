package ar.edu.itba.cripto.grupo2.cryptography;

public enum CipherMode {
    ECB ("ECB", CipherPadding.PKCS5, false),
    CFB ("CFB8", CipherPadding.NO_PADDING, true),
    OFB ("OFB", CipherPadding.NO_PADDING, true),
    CBC ("CBC", CipherPadding.PKCS5, true);

    private final String code;
    private final CipherPadding padding;
    private final boolean ivRequired;

    CipherMode(String code, CipherPadding padding, boolean ivRequired){
        this.code = code;
        this.padding = padding;
        this.ivRequired = ivRequired;
    }

    public String getCode() {
        return code;
    }

    public CipherPadding getPadding() {
        return padding;
    }

    public boolean isIvRequired() {
        return ivRequired;
    }
}
