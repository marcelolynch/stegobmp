package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.cryptography.CipherMode;
import ar.edu.itba.cripto.grupo2.cryptography.CipherType;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;
import org.junit.Test;

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
    }


    @Test
    public void MD5andAES128Test() throws NoSuchAlgorithmException {
        String password = "PASSWORDAES128";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_128, CipherMode.ECB, password);
        assertEquals(16, settings.getKey().getEncoded().length);


    }

    @Test
    public void MD5andAES256Test() throws NoSuchAlgorithmException {
        String password = "PASSWORDAES256";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_256, CipherMode.ECB, password);
        assertEquals(32, settings.getKey().getEncoded().length);


    }

    @Test
    public void MD5andAES192Test() throws NoSuchAlgorithmException {
        String password = "PASSWORDAES128";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_192, CipherMode.ECB, password);
        assertEquals(24, settings.getKey().getEncoded().length);

    }
}