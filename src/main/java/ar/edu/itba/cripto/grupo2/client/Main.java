package ar.edu.itba.cripto.grupo2.client;

import ar.edu.itba.cripto.grupo2.bitmap.Bitmap;
import ar.edu.itba.cripto.grupo2.steganography.Message;
import ar.edu.itba.cripto.grupo2.steganography.Steganographer;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ProgramSettings programSettings = OptionsHelper.getProgramSettings(args);
            if (programSettings.mustExtract()) {
                extract(programSettings.getCarrierBitmap(), programSettings.getOutputPath(), programSettings.getSteganographer());
            } else {
                embed(programSettings.getToEmbed().get(), programSettings.getCarrierBitmap(), programSettings.getOutputPath(), programSettings.getSteganographer());
            }
        } catch (InvalidOptionsException | IllegalOptionException | ParseException | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void embed(String toEmbed, String carrierBitmap, String outputPath, Steganographer steganographer) throws IOException {
        Bitmap bitmap = Bitmap.fromFile(carrierBitmap);
        byte[] payload = IOUtils.toByteArray(new FileInputStream(toEmbed));
        String[] split = toEmbed.split(".");
        String extension;
        if (split.length < 2) {
            extension = ""; // TODO: Que hacer en este caso
        } else {
           extension = split[split.length - 1];
        }

        Message m = new Message(payload, extension);

        steganographer.write(bitmap, m);

        IOUtils.write(bitmap.getBytes(), new FileOutputStream(outputPath));
    }

    private static void extract(String carrierBitmap, String outputPath, Steganographer steganographer) throws IOException {
        Bitmap bitmap = Bitmap.fromFile(carrierBitmap);
        Message m = steganographer.read(bitmap);
        outputPath = outputPath + m.getExtension();  // Se guarda con la extension

        IOUtils.write(m.getPayload(), new FileOutputStream(outputPath));
    }
}