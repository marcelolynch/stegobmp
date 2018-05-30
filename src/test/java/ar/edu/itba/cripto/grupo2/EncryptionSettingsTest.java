package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.cryptography.CipherMode;
import ar.edu.itba.cripto.grupo2.cryptography.CipherType;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EncryptionSettingsTest {

    @Test
    public void MD5andDESTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String password = "PASSWORDDES";
        EncryptionSettings settings = new EncryptionSettings(CipherType.DES, CipherMode.ECB, password);
        assertEquals(8, settings.getKey().getEncoded().length);
        assertEquals("AD9B06C24AEFA3DB", decodePassword(settings.getKey().getEncoded()));
    }


    @Test
    public void MD5andAES128Test() throws NoSuchAlgorithmException {
        String password = "PASSWORDAES128";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_128, CipherMode.ECB, password);
        assertEquals(16, settings.getKey().getEncoded().length);
        assertEquals("f6b50eb662d4ffd07f0870e82fdf1b09".toUpperCase(), decodePassword(settings.getKey().getEncoded()));
    }

    @Test
    public void MD5andAES256Test() throws NoSuchAlgorithmException {
        String password = "PASSWORDAES256";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_256, CipherMode.ECB, password);
        assertEquals(32, settings.getKey().getEncoded().length);
        assertEquals("d887b32f8c4bb2fd3ac48492b6f5264a00000000000000000000000000000000".toUpperCase(), decodePassword(settings.getKey().getEncoded()));
    }

    @Test
    public void MD5andAES192Test() throws NoSuchAlgorithmException {
        String password = "PASSWORDAES192";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_192, CipherMode.ECB, password);
        assertEquals(24, settings.getKey().getEncoded().length);
        assertEquals("ea5d00b910ea6f95cf7875d1d99366620000000000000000".toUpperCase(), decodePassword(settings.getKey().getEncoded()));

    }

    public String decodePassword(byte[] key) {
        StringBuilder sb = new StringBuilder();
        for (byte b : key) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    @Test
    public void passwordTest() throws Exception {
        String password = "margarita";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_256, CipherMode.CBC, password);

        SecretKey key = settings.getKey();
        IvParameterSpec iv = settings.getIv();

        byte[] keyBytes = key.getEncoded();
        byte[] ivBytes = iv.getIV();
        for(byte b: keyBytes){
            System.out.printf("%02X ", b);
        }
        System.out.println();
        for(byte b: ivBytes){
            System.out.printf("%02X ", b);
        }

    }

}
