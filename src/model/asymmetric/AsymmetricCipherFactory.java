package model.asymmetric;

public class AsymmetricCipherFactory {
    public AsymmetricCipher createAsymmetricCipher(String cipher) {
        switch (cipher) {
            case "RSA": {
                return new RSA();
            }
            default: {
                return null;
            }
        }
    }
}
