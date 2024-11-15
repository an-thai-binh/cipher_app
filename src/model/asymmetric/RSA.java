package model.asymmetric;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class RSA extends AsymmetricCipher {
    public RSA() {
        this.name = "RSA";
    }

    @Override
    public List<String> getSupportedInstace() {
        List<String> result = new ArrayList<>();
        result.add("RSA/ECB/PKCS1Padding");
        result.add("RSA/ECB/OAEPPadding");
        return result;
    }

    @Override
    public List<Integer> getSupportedKeySize() {
        List<Integer> result = new ArrayList<>();
        result.add(2048);
        result.add(3072);
        result.add(4096);
        return result;
    }

//    public static void main(String[] args) throws Exception {
//        RSA rsa = new RSA();
//        KeyPair keyPair = rsa.genKey(rsa.getSupportedKeySize().getFirst());
//        rsa.loadPublicKey(keyPair.getPublic());
//        rsa.loadPrivateKey(keyPair.getPrivate());
//        rsa.setInstance(rsa.getSupportedInstace().getFirst());
//        String encrypted = rsa.encryptBase64("Hello World");
//        System.out.println(encrypted);
//        System.out.println(rsa.decryptBase64(encrypted));
//    }
}
