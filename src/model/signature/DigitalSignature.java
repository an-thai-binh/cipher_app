package model.signature;

import utils.CipherException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DigitalSignature {
    public static final String[] ALGORITHMS = new String[]{"RSA", "DSA"};
    public static final String[] HASH_ALGORITHMS = new String[]{"SHA1", "SHA224", "SHA256", "SHA384", "SHA512"};
    private String asymmetricCipher;
    private String hashAlgorithm;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Signature signature;
    public DigitalSignature() {
        this.hashAlgorithm = "SHA1";
    }

    /**
     * setAsymmetricCipher  cài đặt tên giải thuật bất đối xứng thực hiện
     * @param name  tên giải thuật
     */
    public void setAsymmetricCipher(String name) {
        this.asymmetricCipher = name;
    }

    /**
     * setHashAlgorithm cài đặt tên giải thuật hash
     * @param name  tên giải thuật
     */
    public void setHashAlgorithm(String name) {
        this.hashAlgorithm = name;
    }

    /**
     * setSignature cài đặt chữ ký
     * @throws Exception    thuật toán không hỗ trợ
     */
    public void setSignature() throws Exception {
        try {
            signature = Signature.getInstance(hashAlgorithm + "with" + asymmetricCipher);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        }
    }

    /**
     * genKey   tạo cặp key
     * @param keySize   kích thước key
     * @return KeyPair
     * @throws Exception    thuật toán không hỗ trợ
     */
    public KeyPair genKey(int keySize) throws Exception {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(asymmetricCipher);
            SecureRandom rd = new SecureRandom();
            generator.initialize(keySize, rd);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        }
    }

    /**
     * loadPublicKey    gán key public
     * @param key   public key
     */
    public void loadPublicKey(PublicKey key) {
        this.publicKey = key;
    }

    /**
     * loadPublicKey    gán key public bằng mảng byte
     * @param bytes mảng byte
     * @throws Exception    thuật toán không được hỗ trợ
     */
    public void loadPublicKey(byte[] bytes) throws Exception {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(this.asymmetricCipher);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            this.publicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        } catch (InvalidKeySpecException e) {
            throw new Exception(CipherException.INVALID_KEY);
        }
    }

    /**
     * loadPrivateKey    gán key private
     * @param key   private key
     */
    public void loadPrivateKey(PrivateKey key) {
        this.privateKey = key;
    }

    /**
     * loadPrivateKey    gán key private bằng mảng byte
     * @param bytes mảng byte
     * @throws Exception    thuật toán không được hỗ trợ
     */
    public void loadPrivateKey(byte[] bytes) throws Exception {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(this.asymmetricCipher);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            this.privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        } catch (InvalidKeySpecException e) {
            throw new Exception(CipherException.INVALID_KEY);
        }
    }

    /**
     * sign ký văn bản
     * @param text  văn bản đầu vào
     * @return  String  chữ ký dạng base64
     * @throws Exception
     */
    public String sign(String text) throws Exception {
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        try {
            signature.initSign(privateKey);
            signature.update(data);
            byte[] sign = signature.sign();
            return Base64.getEncoder().encodeToString(sign);
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        } catch (SignatureException e) {
            throw new Exception(CipherException.EXECUTE_EXCEPTION + ":\n" + e.getMessage());
        }
    }

    /**
     * verify   xác thực chữ ký với văn bản
     * @param text  văn bản đầu vào
     * @param signBase64    chữ ký dạng base64
     * @return boolean
     * @throws Exception
     */
    public boolean verify(String text, String signBase64) throws Exception {
        try {
            byte[] data = text.getBytes(StandardCharsets.UTF_8);
            byte[] signBytes = Base64.getDecoder().decode(signBase64);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signBytes);
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        } catch (SignatureException e) {
            throw new Exception(CipherException.EXECUTE_EXCEPTION + ":\n" + e.getMessage());
        }
    }

    /**
     * signFile ký file
     * @param src  đường dẫn file nguồn
     * @return  String  chữ ký dạng base64
     * @throws Exception
     */
    public String signFile(String src) throws Exception {
        try {
            signature.initSign(privateKey);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
            byte[] b = new byte[10 * 1024];
            int bytesRead;
            while ((bytesRead = bis.read(b)) != -1) {
                signature.update(b, 0, bytesRead);
            }
            byte[] sign = signature.sign();
            return Base64.getEncoder().encodeToString(sign);
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        } catch (FileNotFoundException e) {
            throw new Exception(CipherException.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new Exception(CipherException.IO_EXCEPTION);
        } catch (SignatureException e) {
            throw new Exception(CipherException.EXECUTE_EXCEPTION + ":\n" + e.getMessage());
        }
    }

    /**
     * verifyFile   xác thực chữ ký với file
     * @param src  đường dẫn file nguồn
     * @param signBase64    chữ ký dạng base64
     * @return boolean
     * @throws Exception
     */
    public boolean verifyFile(String src, String signBase64) throws Exception {
        try {
            signature.initVerify(publicKey);    // init trước update
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
            byte[] b = new byte[10 * 1024];
            int bytesRead;
            while((bytesRead = bis.read(b)) != -1) {
                signature.update(b, 0, bytesRead);
            }
            byte[] signBytes = Base64.getDecoder().decode(signBase64);
            return signature.verify(signBytes);
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        } catch (FileNotFoundException e) {
            throw new Exception(CipherException.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new Exception(CipherException.IO_EXCEPTION);
        } catch (SignatureException e) {
            throw new Exception(CipherException.EXECUTE_EXCEPTION + ":\n" + e.getMessage());
        }
    }

//    public static void main(String[] args) throws Exception {
//        // init
//        DigitalSignature dg = new DigitalSignature();
//        dg.setAsymmetricCipher("DSA");
////        dg.setHashAlgorithm("SHA1");
//        KeyPair keyPair = dg.genKey(1024);
//        dg.setSignature();
////        // sign
////        String text = "Hello World";
////        dg.loadPrivateKey(keyPair.getPrivate());
////        String signBase64 = dg.sign(text);
////        System.out.println(signBase64);
////        // verify
////        String text2 = "Hello World";
////        dg.loadPublicKey(keyPair.getPublic());
////        System.out.println(dg.verify(text2, signBase64));
//        // sign file
//        String src = "D:\\Documents\\TEMQA.pptx";
//        dg.loadPrivateKey(keyPair.getPrivate());
//        String signBase64 = dg.signFile(src);
//        System.out.println(signBase64);
//        // verify file
//        String src2 = "D:\\Documents\\TEMQA - Copy.pptx";
//        dg.loadPublicKey(keyPair.getPublic());
//        System.out.println(dg.verifyFile(src2, signBase64));
//    }
}
