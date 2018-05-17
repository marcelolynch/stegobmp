package ar.edu.itba.cripto.grupo2.cryptography;


import sun.security.provider.MD5;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Objects;

public class EncryptionSettings {

    private final CipherType cipherType;
    private final CipherMode cipherMode;
    private final CipherPadding padding;
    private final SecretKey key;

    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode, SecretKey secretKey) {
        this(cipherType, cipherMode, secretKey, CipherPadding.PKCS5);
    }

    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode, SecretKey key, CipherPadding padding) {
        Objects.requireNonNull(cipherType);
        Objects.requireNonNull(cipherMode);
        Objects.requireNonNull(key);
        Objects.requireNonNull(padding);

        this.cipherType = cipherType;
        this.cipherMode = cipherMode;
        this.padding = padding;
        this.key = key;
    }

    public String getCode(){
       return String.format("%s/%s/%s", cipherType.getCode(), cipherMode.getCode(), padding.getCode());
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

    public SecretKey getKey() {
        return key;
    }
}
