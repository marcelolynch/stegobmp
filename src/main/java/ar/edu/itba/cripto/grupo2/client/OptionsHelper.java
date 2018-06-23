package ar.edu.itba.cripto.grupo2.client;

import ar.edu.itba.cripto.grupo2.cryptography.CipherMode;
import ar.edu.itba.cripto.grupo2.cryptography.CipherType;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;
import ar.edu.itba.cripto.grupo2.steganography.*;
import org.apache.commons.cli.*;
import ar.edu.itba.cripto.grupo2.client.ProgramSettings.*;

public class OptionsHelper {

    private static final String EMBED = "embed";
    private static final String IN = "in";
    private static final String P = "p";
    private static final String OUT = "out";
    private static final String STEG = "steg";
    private static final String EXTRACT = "extract";
    private static final String ALGORITHM = "a";
    private static final String FEEDBACK_MODE = "m";
    private static final String PASSWORD = "pass";

    private static final Options options;

    static {
        options = new Options();
        options.addOption(EMBED, false, "Indica que se va a ocultar información")
                .addOption(EXTRACT, false, "Indica que se va a extraer información")
                .addRequiredOption(P, P, true, "Archivo bmp portador")
                .addOption(IN, true, "Archivo que se va a ocultar")
                .addRequiredOption(OUT, OUT, true, "Archivo de salida")
                .addRequiredOption(STEG, STEG, true, "Algoritmo de esteganografiado: LSB de 1bit (LSB1), LSB de 4 bits (LSB4), LSB Enhanced (LSBE)")
                .addOption(ALGORITHM, true, "Algoritmo de encripción")
                .addOption(FEEDBACK_MODE, true, "Feedback mode")
                .addOption(PASSWORD, true, "Password de encripcion");
    }

    private static Options getOptions() {
        return options;
    }

    public static ProgramSettings getProgramSettings(String[] args) throws ParseException, InvalidOptionsException, IllegalOptionException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(OptionsHelper.getOptions(), args);

        ProgramSettingsBuilder settingsBuilder = ProgramSettings.getBuilder();

        if (!cmd.hasOption(EMBED) && !cmd.hasOption(EXTRACT)) {
            throw new InvalidOptionsException("Debe especificarse -embed o -extract");
        }

        if (cmd.hasOption(EMBED) && cmd.hasOption(EXTRACT)) {
            throw new InvalidOptionsException("No puede especificarse -embed y -extract al mismo tiempo");
        }

        if (cmd.hasOption(EMBED)) {
            if(!cmd.hasOption(IN)) {
                throw new InvalidOptionsException("Debe especificarse un archivo de entrada con -in si se especificó -embed");
            }
            settingsBuilder.toEmbed(cmd.getOptionValue(IN));
        }

        settingsBuilder.carrierBitmap(cmd.getOptionValue(P))
                .outputPath(cmd.getOptionValue(OUT))
                .steganographer(getSteganographer(cmd));

        return settingsBuilder.build();
    }

    private static Steganographer getSteganographer(CommandLine cmd) throws IllegalOptionException, InvalidOptionsException {
        SteganographyStrategy strategy = getSteganographyStrategy(cmd);

        Steganographer steganographer;

        if (cmd.hasOption(PASSWORD)) {
            EncryptionSettings settings = getEncryptionSettings(cmd);
            steganographer = new CryptoSteganographer(strategy, settings);
        } else {
            steganographer = new PlaintextSteganographer(strategy);
        }

        return steganographer;
    }

    private static SteganographyStrategy getSteganographyStrategy(CommandLine cmd) throws IllegalOptionException {
        String steg = cmd.getOptionValue(STEG);
        switch (steg.toUpperCase()) {
            case "LSB1": return LSB1.getInstance();
            case "LSB4": return LSB4.getInstance();
            case "LSBE": return EnhancedLSB1.getInstance();
            default:     throw new IllegalOptionException("El parámetro -steg requiere alguna de las opciones: lsb1, lsb4, lsbe. Se obtuvo " + steg);
        }
    }

    private static EncryptionSettings getEncryptionSettings(CommandLine cmd) throws IllegalOptionException {
        String algorithm = cmd.hasOption(ALGORITHM) ? cmd.getOptionValue(ALGORITHM) : "aes128";
        String fm = cmd.hasOption(FEEDBACK_MODE) ? cmd.getOptionValue(FEEDBACK_MODE) : "cbc";
        CipherMode mode;
        CipherType type;

        switch (algorithm.toUpperCase()) {
            case "DES":
                type = CipherType.DES;
                break;
            case "AES128":
                type = CipherType.AES_128;
                break;
            case "AES192":
                type = CipherType.AES_192;
                break;
            case "AES256":
                type = CipherType.AES_256;
                break;
            default:
                throw new IllegalOptionException("El parametro -a solo acepta las opciones: des, aes128, aes192, aes256. Se obtuvo: " + algorithm);
        }

        switch (fm.toUpperCase()) {
            case "CBC":
                mode = CipherMode.CBC;
                break;
            case "ECB":
                mode = CipherMode.ECB;
                break;
            case "OFB":
                mode = CipherMode.OFB;
                break;
            case "CFB":
                mode = CipherMode.CFB;
                break;
            default:
                throw new IllegalOptionException("El parámetro -m solo acepta las opciones: cbc, ecb, ofb, cfb. Se obtuvo: " + fm);
        }

        return new EncryptionSettings(type, mode, cmd.getOptionValue(PASSWORD));
    }
}
