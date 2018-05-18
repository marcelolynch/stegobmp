package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.cryptography.CipherMode;
import ar.edu.itba.cripto.grupo2.cryptography.CipherType;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;
import ar.edu.itba.cripto.grupo2.cryptography.MessageCipher;
import ar.edu.itba.cripto.grupo2.steganography.Message;
import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CipherTest {

    @Test
    public void DesECBTest() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        String password = "password";
        EncryptionSettings settings = new EncryptionSettings(CipherType.DES, CipherMode.ECB);
        MessageCipher c = MessageCipher.getInstance();
        String message = "mensaje a encriptar";
        byte[] cipherText = c.cipher(message.getBytes(),password,settings);
        System.out.println(cipherText);


    }

    @Test
    public void AesCBCTest() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        String password = "password";
        EncryptionSettings settings1 = new EncryptionSettings(CipherType.AES_128, CipherMode.CBC);
        EncryptionSettings settings2 = new EncryptionSettings(CipherType.AES_192, CipherMode.CBC);
        EncryptionSettings settings3 = new EncryptionSettings(CipherType.AES_256, CipherMode.CBC);

        MessageCipher c = MessageCipher.getInstance();
        String message = "mensaje a encriptar";
        byte[] cipherText1 = c.cipher(message.getBytes(),password,settings1);
        byte[] cipherText2 = c.cipher(message.getBytes(),password,settings2);
        byte[] cipherText3 = c.cipher(message.getBytes(),password,settings3);
        System.out.println(cipherText1);
        System.out.println(cipherText2);
        System.out.println(cipherText3);

    }

}
