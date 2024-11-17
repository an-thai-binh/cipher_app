package model;

import model.asymmetric.AsymmetricCipher;
import model.asymmetric.RSA;
import model.classical.*;
import model.hash.HashAlgorithm;
import model.hash.HashAlgorithmThirdParty;
import model.hash.IHashAlgorithm;
import model.symmetric.*;
import utils.CipherSupport;

public class CipherFactory {
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
            default: {
                return null;
            }
        }
    }

    public SymmetricCipherThirdParty createSymmetricCipherThirdParty(String cipher) throws Exception {
        return new SymmetricCipherThirdParty(cipher);
    }

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

    public IHashAlgorithm createHashAlgorithm(String algorithm) {
        if(CipherSupport.isContains(CipherSupport.HASH_ALGORITHMS_THIRD_PARTY, algorithm)) {
            return new HashAlgorithmThirdParty(algorithm);
        }
        return new HashAlgorithm(algorithm);
    }
}
