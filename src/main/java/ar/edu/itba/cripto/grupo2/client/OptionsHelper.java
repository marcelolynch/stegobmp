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
        options.addOption(EMBED, false, "This flag indicates that information will be encoded into the carrier bitmap")
                .addOption(EXTRACT, false, "This flag indicates that information will be extracted from the carrier bitmap")
                .addRequiredOption(P, P, true, "Carrier bitmap")
                .addOption(IN, true, "File to be hidden")
                .addRequiredOption(OUT, OUT, true, "Output file")
                .addRequiredOption(STEG, STEG, true, "Stego algorithm: 1bit LSB  (LSB1),  4-bit LSB (LSB4), LSB Enhanced (LSBE). Case insensitive")
                .addOption(ALGORITHM, true, "Encryption algorithm")
                .addOption(FEEDBACK_MODE, true, "Feedback mode")
                .addOption(PASSWORD, true, "Encryption password");
    }

    private static Options getOptions() {
        return options;
    }

    public static ProgramSettings getProgramSettings(String[] args) throws ParseException, InvalidOptionsException, IllegalOptionException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(OptionsHelper.getOptions(), args);

        ProgramSettingsBuilder settingsBuilder = ProgramSettings.getBuilder();

        if (!cmd.hasOption(EMBED) && !cmd.hasOption(EXTRACT)) {
            throw new InvalidOptionsException("Either -embed or -extract must be specified");
        }

        if (cmd.hasOption(EMBED) && cmd.hasOption(EXTRACT)) {
            throw new InvalidOptionsException("-embed and -extract can't be specified at the same time");
        }

        if (cmd.hasOption(EMBED)) {
            if(!cmd.hasOption(IN)) {
                throw new InvalidOptionsException("An input file must be provided with -in while in embed mode");
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
            default:     throw new IllegalOptionException("The -steg parameter requires one of the following arguments (case insensitive) : lsb1, lsb4, lsbe. The argument provided was " + steg);
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
                throw new IllegalOptionException("The -a option only takes the following (case insensitive): des, aes128, aes192, aes256. The argument provided was: " + algorithm);
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
                throw new IllegalOptionException("The -m option only takes the following: cbc, ecb, ofb, cfb (case insensitive).  The argument provided was: " + fm);
        }

        return new EncryptionSettings(type, mode, cmd.getOptionValue(PASSWORD));
    }
}
