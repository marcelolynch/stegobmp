package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.cryptography.CipherMode;
import ar.edu.itba.cripto.grupo2.cryptography.CipherType;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

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
        String password = "PASSWORDAES192";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_192, CipherMode.ECB, password);
        assertEquals(24, settings.getKey().getEncoded().length);
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

        String expectedKey = "F1BE6354B41828A0B8AA1201094A15CC00ADD2A52D5BDE5B582A1576FDFDFBD4";
        String expectedIv = "66ED36D9005C1D4F5A1C4055E7B14283";

        assertEquals(expectedKey, decodePassword(key.getEncoded()));
        assertEquals(expectedIv, decodePassword(iv.getIV()));

    }

}
