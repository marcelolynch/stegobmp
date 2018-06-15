package ar.edu.itba.cripto.grupo2.cryptography;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.Objects;

public class EncryptionSettings {

    static {
        //TODO: Esto ponerlo en el main
        Security.setProperty("crypto.policy", "unlimited");
    }

    private CipherType cipherType;
    private CipherMode cipherMode;
    private SecretKey key;
    private IvParameterSpec iv;


    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode, SecretKey key, IvParameterSpec iv) {
        this.setType(cipherType);
        this.setMode(cipherMode);
        this.setKey(key);
        this.setIv(iv);
    }


    public EncryptionSettings(CipherType cipherType, CipherMode cipherMode, String password) {
        this.setType(cipherType);
        this.setMode(cipherMode);
        generateKeyAndIv(password);
    }


    private void generateKeyAndIv(String password) {
        // Implementacion de EVP_BytesToKey: https://www.openssl.org/docs/man1.1.0/crypto/EVP_BytesToKey.html

        int keyLength = cipherType.getKeyLength();
        int ivLength = cipherType.getBlockSize();
        byte[] data = password.getBytes();

        MessageDigest digestor = null;

        try {
            final String HASH_ALGORITHM = "SHA-256";
            digestor = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException ignored) {}

        int requiredLength = keyLength + ivLength;

        // Me aseguro de generar suficientes bytes
        int iterations = (requiredLength / digestor.getDigestLength()) + (requiredLength % digestor.getDigestLength() == 0 ? 0 : 1);
        byte[] keyData = new byte[iterations * digestor.getDigestLength()];

        byte[] prev = {}; // D_i
        int offset = 0;   //
        for (int i = 0 ; i < iterations ; i++) {
            byte[] hashable = new byte[prev.length + data.length];
            // Concatenate D_(n-1) || data
            // (No salt)
            System.arraycopy(prev, 0, hashable, 0, prev.length);
            System.arraycopy(data, 0, hashable, prev.length, data.length);

            prev = digestor.digest(hashable); // D_n
            System.arraycopy(prev, 0, keyData, offset, prev.length);
            offset += prev.length;
        }

        byte[] key = Arrays.copyOfRange(keyData,0, keyLength);
        byte[] iv = Arrays.copyOfRange(keyData, keyLength, requiredLength);

        setKey(new SecretKeySpec(key, this.cipherType.getCode()));
        setIv(new IvParameterSpec(iv));
    }

    public String getCode(){
       return String.format("%s/%s/%s", cipherType.getCode(), cipherMode.getCode(), cipherMode.getPadding().getCode());
    }

    public CipherType getCipherType() {
        return cipherType;
    }

    public CipherMode getCipherMode() {
        return cipherMode;
    }

    public CipherPadding getPadding() {
        return cipherMode.getPadding();
    }

    public SecretKey getKey() {
        return key;
    }

    public IvParameterSpec getIv() {
        return iv;
    }

    private void setType(CipherType cipherType){
        Objects.requireNonNull(cipherType);
        this.cipherType = cipherType;
    }


    private void setIv(IvParameterSpec iv) {
        Objects.requireNonNull(iv);
        this.iv = iv;
    }

    private void setMode(CipherMode cipherMode){
        Objects.requireNonNull(cipherMode);
        this.cipherMode = cipherMode;
    }


    private void setKey(SecretKey key){
        Objects.requireNonNull(key);
        this.key = key;
    }


    private String decodePassword(byte[] key) {
        StringBuilder sb = new StringBuilder();
        for (byte b : key) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        return "{\nKey = " +
                decodePassword(key.getEncoded()) +
                "\n IV = " +
                decodePassword(iv.getIV()) +
                "\n}";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptionSettings that = (EncryptionSettings) o;
        return cipherType == that.cipherType &&
                cipherMode == that.cipherMode &&
                Objects.equals(key, that.key) &&
                Arrays.equals(iv.getIV(), that.iv.getIV());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cipherType, cipherMode, key, iv.getIV());
    }
}
