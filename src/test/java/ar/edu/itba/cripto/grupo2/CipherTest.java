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
    public void cipherTest() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        String password = "password";
        EncryptionSettings settings = new EncryptionSettings(CipherType.AES_192, CipherMode.ECB);
        MessageCipher c = new MessageCipher();
        String message = "mensaje a encriptar";
        byte[] cipherText = c.cipher(message.getBytes(),password,settings);
        System.out.println(cipherText);


    }

}
