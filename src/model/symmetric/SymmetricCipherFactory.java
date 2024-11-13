package model.symmetric;

public class SymmetricCipherFactory {
    public SymmetricCipher createSymmetricCipher(String cipher) {
        switch (cipher) {
            case "AES": {
                return new AES();
            }
            case "DES": {
                return new DES();
            }
            case "TripleDES": {
                return new TripleDES();
            }
            case "Blowfish": {
                return new Blowfish();
            }
            case "ChaCha20": {
                return new ChaCha20();
            }
            case "RC2": {
                return new RC2();
            }
            case "RC4": {
                return new RC4();
            }
            case "SEED": {
                return new SEED();
            }
            case "Camellia": {
                return new Camellia();
            }
            case "Serpent": {
                return new Serpent();
            }
            default: {
                return null;
            }
        }
    }
}
