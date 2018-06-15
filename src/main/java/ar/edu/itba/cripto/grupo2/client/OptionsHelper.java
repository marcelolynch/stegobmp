package ar.edu.itba.cripto.grupo2.client;

import ar.edu.itba.cripto.grupo2.cryptography.CipherMode;
import ar.edu.itba.cripto.grupo2.cryptography.CipherType;
import ar.edu.itba.cripto.grupo2.cryptography.EncryptionSettings;
import ar.edu.itba.cripto.grupo2.steganography.*;
import org.apache.commons.cli.*;
import ar.edu.itba.cripto.grupo2.client.ProgramSettings.*;

public class OptionsHelper {

    private final static String EMBED = "embed";
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


    public static Options getOptions() {
        return options;
    }



    private static Steganographer getSteganographer(CommandLine cmd) throws IllegalOptionException, InvalidOptionsException {
        SteganographyStrategy strategy = getSteganographyStrategy(cmd);

        Steganographer steganographer;

        if (cmd.hasOption(ALGORITHM) && cmd.hasOption(PASSWORD) && cmd.hasOption(FEEDBACK_MODE)) {
            EncryptionSettings settings = getEncryptionSettings(cmd);
            steganographer = new CryptoSteganographer(strategy,settings);
        } else if (!cmd.hasOption(ALGORITHM) && !cmd.hasOption(PASSWORD) && !cmd.hasOption(FEEDBACK_MODE)){
            steganographer = new PlaintextSteganographer(strategy);
        } else {
            throw new InvalidOptionsException("Las opciones -m, -pass, -a deben especificarse todas (o ninguna para esteganografiar sin encripción)");
        }

        return steganographer;

    }


    private static EncryptionSettings getEncryptionSettings(CommandLine cmd) throws IllegalOptionException {
        String password;
        CipherMode mode;
        CipherType type;

        password = cmd.getOptionValue(PASSWORD);


        switch (cmd.getOptionValue(ALGORITHM).toUpperCase()) {
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
                throw new IllegalOptionException("El parametro -a requiere aes128, aes192 o aes256 como argumentos");
        }


        switch (cmd.getOptionValue(FEEDBACK_MODE).toUpperCase()) {
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
                throw new IllegalOptionException("El parámetro -m requiere alguna de las opciones: cbc, ecb, ofb, cfb");
        }

        return new EncryptionSettings(type, mode, password);
    }


    private static SteganographyStrategy getSteganographyStrategy(CommandLine cmd) throws InvalidOptionsException {
        switch(cmd.getOptionValue(STEG).toUpperCase()){
            case "LSB1": return LSB1.getInstance();
            case "LSB4": return LSB4.getInstance();
            case "LSBE": return EnhancedLSB1.getInstance();
            default:     throw new InvalidOptionsException("Los parametros de encripción (-m, -pass, -a) deben aparecer todos o ninguno");
        }
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
            if(!cmd.hasOption(IN)){
                throw new InvalidOptionsException("Debe especificarse un archivo de entrada con -in si se especificó -embed");
            }
            settingsBuilder.toEmbed(cmd.getOptionValue(IN));
        }


        settingsBuilder.carrierBitmap(cmd.getOptionValue(P))
                        .outputPath(cmd.getOptionValue(OUT))
                        .steganographer(getSteganographer(cmd));

        return settingsBuilder.build();

    }
}
