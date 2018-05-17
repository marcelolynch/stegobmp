package ar.edu.itba.cripto.grupo2.cryptography;

public enum CipherPadding {

    PKCS5 ("PKCS5Padding") {

        @Override
        public int encryptionSize(int messageSize, int blockSize) {
            return (messageSize/blockSize + 1) * blockSize;
        }
    };
    private String code;

    CipherPadding(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public abstract int encryptionSize(int messageSize, int blockSize);
}
