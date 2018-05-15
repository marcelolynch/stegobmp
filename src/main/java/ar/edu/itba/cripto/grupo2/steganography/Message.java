package ar.edu.itba.cripto.grupo2.steganography;

public class Message {
    private String extension;
    private byte[] payload;

    public Message(String extension, byte[] payload) {
        this.extension = extension;
        this.payload = payload;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getPayload() {
        return payload;
    }
}
