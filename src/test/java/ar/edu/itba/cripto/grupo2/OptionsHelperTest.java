package ar.edu.itba.cripto.grupo2;

import ar.edu.itba.cripto.grupo2.client.IllegalOptionException;
import ar.edu.itba.cripto.grupo2.client.InvalidOptionsException;
import ar.edu.itba.cripto.grupo2.client.OptionsHelper;
import ar.edu.itba.cripto.grupo2.client.ProgramSettings;
import ar.edu.itba.cripto.grupo2.cryptography.CipherMode;
import ar.edu.itba.cripto.grupo2.cryptography.CipherType;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;
import ar.edu.itba.cripto.grupo2.steganography.CryptoSteganographer;
import ar.edu.itba.cripto.grupo2.steganography.PlaintextSteganographer;
import ar.edu.itba.cripto.grupo2.steganography.Steganographer;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsHelperTest {

    @Test
    public void embedOptionsTest() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-embed", "-in", "mensaje1.txt", "-p", "imagen1.bmp", "-out", "imagenmas1.bmp", "-steg", "LSBE", "-a", "des", "-m", "cbc", "-pass", "oculto"};
        ProgramSettings settings = OptionsHelper.getProgramSettings(args);

        assertFalse(settings.mustExtract());
        assertEquals("mensaje1.txt", settings.getToEmbed().get());
        assertEquals("imagenmas1.bmp", settings.getOutputPath());
        assertEquals("imagen1.bmp", settings.getCarrierBitmap());

        EncryptionSettings encryptionSettings = ((CryptoSteganographer)settings.getSteganographer()).getSettings();

        assertEquals(new EncryptionSettings(CipherType.DES, CipherMode.CBC, "oculto"), encryptionSettings);
    }


    @Test
    public void embedOptionsTest2() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-embed", "-in", "mensaje1.txt", "-p", "imagen1.bmp", "-out", "imagenmas1.bmp", "-steg", "LSBE"};

        ProgramSettings settings = OptionsHelper.getProgramSettings(args);

        assertFalse(settings.mustExtract());
        assertEquals("mensaje1.txt", settings.getToEmbed().get());
        assertEquals("imagenmas1.bmp", settings.getOutputPath());
        assertEquals("imagen1.bmp", settings.getCarrierBitmap());

        assertEquals(PlaintextSteganographer.class, settings.getSteganographer().getClass());
    }



    @Test
    public void extractOptionsTest() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-extract", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBE", "-a", "aes256", "-m", "ecb", "-pass", "oculto"};
        ProgramSettings settings = OptionsHelper.getProgramSettings(args);

        assert(settings.mustExtract());
        assertEquals("mensaje1", settings.getOutputPath());
        assertEquals("imagenmas1.bmp", settings.getCarrierBitmap());

        EncryptionSettings encryptionSettings = ((CryptoSteganographer)settings.getSteganographer()).getSettings();
        assertEquals(new EncryptionSettings(CipherType.AES_256, CipherMode.ECB, "oculto"), encryptionSettings);
    }


    @Test(expected = InvalidOptionsException.class)
    public void missingModeArgument() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-extract", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBE", "-a", "aes256", "-pass", "oculto"};
        ProgramSettings settings = OptionsHelper.getProgramSettings(args);  // Missing -m argument
    }


    @Test(expected = InvalidOptionsException.class)
    public void missingPassArgument() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-extract", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBE", "-a", "aes256", "-m", "cfb"};
        ProgramSettings settings = OptionsHelper.getProgramSettings(args);  // Missing -pass argument
    }


    @Test(expected = InvalidOptionsException.class)
    public void missingInArgument() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-embed", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBE", "-a", "aes256", "-m", "cfb"};
        ProgramSettings settings = OptionsHelper.getProgramSettings(args);  // Missing -in argument
    }


    @Test(expected = InvalidOptionsException.class)
    public void missingAlgorithmArgument() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-extract", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBE", "-pass", "oculto", "-m", "cfb"};
        ProgramSettings settings = OptionsHelper.getProgramSettings(args);  // Missing -a argument
    }


    @Test(expected = IllegalOptionException.class)
    public void invalidAlgorithmArgument() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-extract", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBE", "-a", "CUALQUIERA", "-m", "ecb", "-pass", "oculto"};
        ProgramSettings settings = OptionsHelper.getProgramSettings(args);  //  Invalid -a argument
    }



    @Test(expected = IllegalOptionException.class)
    public void invalidModeArgument() throws ParseException, InvalidOptionsException, IllegalOptionException {
        String[] args = {"-extract", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBE", "-a", "aes", "-m", "CUALQUIERA", "-pass", "oculto"};
        ProgramSettings settings = OptionsHelper.getProgramSettings(args);  //  Invalid -m argument
    }


}
