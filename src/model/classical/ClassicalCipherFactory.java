package model.classical;

public class ClassicalCipherFactory {
    public IClassicalCipher createClassicalCipher(String cipher, int languageCode, int order) throws Exception {
        switch (cipher) {
            case "Dịch chuyển": {
                return new CeasarCipher(languageCode);
            }
            case "Thay thế": {
                return new SubstitutionCipher(languageCode);
            }
            case "Affine": {
                return new AffineCipher(languageCode);
            }
            case "Vigenere": {
                return new VigenereCipher(languageCode);
            }
            case "Hill": {
                return new HillCipher(languageCode, order);
            }
            default: {
                return null;
            }
        }
    }
}
