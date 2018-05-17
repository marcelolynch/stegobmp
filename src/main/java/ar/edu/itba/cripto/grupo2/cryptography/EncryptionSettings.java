package ar.edu.itba.cripto.grupo2.cryptography;


import sun.security.provider.MD5;

import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.KeySpec;

public class EncryptionSettings {

    private final CipherType cipherType;
    private final CipherMode cipherMode;
    private final CipherPadding padding;

    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode) {
        this.cipherType = cipherType;
        this.cipherMode = cipherMode;
        this.padding = CipherPadding.PKCS5; // Default
    }

    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode, CipherPadding padding) {
        this.cipherType = cipherType;
        this.cipherMode = cipherMode;
        this.padding = padding;

    }

    public String getCode(){
       return String.format("%s/%s/%s", cipherType.getShortCode(), cipherMode.getCode(), padding.getCode());
    }

    public CipherType getCipherType() {
        return cipherType;
    }

    public CipherMode getCipherMode() {
        return cipherMode;
    }

    public CipherPadding getPadding() {
        return padding;
    }
}
