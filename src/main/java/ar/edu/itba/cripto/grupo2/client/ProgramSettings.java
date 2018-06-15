package ar.edu.itba.cripto.grupo2.client;

import ar.edu.itba.cripto.grupo2.steganography.Steganographer;

import java.util.Objects;
import java.util.Optional;

public class ProgramSettings {
    private Steganographer steganographer;
    private String toEmbed;
    private String carrierBitmap;
    private String outputPath;

    public static ProgramSettingsBuilder getBuilder() {
        return new ProgramSettingsBuilder();
    }

    public Steganographer getSteganographer() {
        return steganographer;
    }

    public Optional<String> getToEmbed() {
        return Optional.ofNullable(toEmbed);
    }

    public boolean mustExtract(){
        return toEmbed == null;
    }

    public String getCarrierBitmap() {
        return carrierBitmap;
    }

    public String getOutputPath() {
        return outputPath;
    }

    private ProgramSettings(Steganographer steganographer, String carrierBitmap, String outputPath, String toExtract) {
        Objects.requireNonNull(steganographer);
        Objects.requireNonNull(carrierBitmap);
        Objects.requireNonNull(outputPath);

        this.steganographer = steganographer;
        this.toEmbed = toExtract;
        this.carrierBitmap = carrierBitmap;
        this.outputPath = outputPath;
    }


    public static class ProgramSettingsBuilder {
        private Steganographer steganographer;
        private String toEmbed;
        private String carrierBitmap;
        private String outputPath;


        public ProgramSettingsBuilder steganographer(Steganographer steganographer) {
            this.steganographer = steganographer;
            return this;
        }

        public ProgramSettingsBuilder toEmbed(String toEmbed) {
            this.toEmbed = toEmbed;
            return this;
        }

        public ProgramSettingsBuilder carrierBitmap(String carrierBitmap) {
            this.carrierBitmap = carrierBitmap;
            return this;
        }

        public ProgramSettingsBuilder outputPath(String outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        public ProgramSettings build() {
            return new ProgramSettings(steganographer, carrierBitmap, outputPath, toEmbed);
        }
    }

}
