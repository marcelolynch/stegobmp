package ar.edu.itba.cripto.grupo2.cryptography;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class EncryptionSettings {

    static {
        //TODO: Esto ponerlo en el main
        Security.setProperty("crypto.policy", "unlimited");
    }

    private static String HASH_ALGORITHM = "MD5";
    private CipherType cipherType;
    private CipherMode cipherMode;
    private CipherPadding padding;
    private SecretKey key;

    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode, SecretKey secretKey) {
        this(cipherType, cipherMode, secretKey, CipherPadding.PKCS5);
    }

    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode, SecretKey key, CipherPadding padding) {
        this.setType(cipherType);
        this.setMode(cipherMode);
        this.setPadding(padding);
        this.setKey(key);
    }

    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode, String password) throws NoSuchAlgorithmException {
        this.setType(cipherType);
        this.setMode(cipherMode);
        this.setPadding(CipherPadding.PKCS5);
        this.setKey(generateKey(password));
    }

    private SecretKey generateKey(String password) throws NoSuchAlgorithmException {
        byte[] key = password.getBytes();
        MessageDigest md5 = MessageDigest.getInstance(HASH_ALGORITHM);
        key = md5.digest(key);
        key = Arrays.copyOf(key, getCipherType().getKeyLength());
        return new SecretKeySpec(key,getCipherType().getCode());
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

    private void setType(CipherType cipherType){
        Objects.requireNonNull(cipherType);
        this.cipherType = cipherType;
    }

    private void setMode(CipherMode cipherMode){
        Objects.requireNonNull(cipherMode);
        this.cipherMode = cipherMode;
    }

    private void setPadding(CipherPadding cipherPadding){
        Objects.requireNonNull(cipherPadding);
        this.padding = cipherPadding;
    }

    private void setKey(SecretKey key){
        Objects.requireNonNull(key);
        this.key = key;
    }
}
