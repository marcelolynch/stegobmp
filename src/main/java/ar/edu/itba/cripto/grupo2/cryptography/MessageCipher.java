package ar.edu.itba.cripto.grupo2.cryptography;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MessageCipher {

    private static String HASH_ALGORITHM = "MD5";

    public byte[] cipher(byte[] message, String password, EncryptionSettings settings) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        byte[] key = password.getBytes();
        MessageDigest md5 = MessageDigest.getInstance(HASH_ALGORITHM);
        key = md5.digest(key);

        key = Arrays.copyOf(key, settings.getCipherType().getKeyLenght());

        SecretKeySpec secretKey = new SecretKeySpec(key, settings.getCipherType().getShortCode());
        Cipher c = Cipher.getInstance(settings.getCode());

        c.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] cipherText = c.doFinal(message);

        return cipherText;

    }

}
