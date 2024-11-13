package model.asymmetric;

import utils.CipherException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

public abstract class AsymmetricCipher {
    protected String name;
    private Cipher cip;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * getName  lấy ra tên thuật toán
     * @return  String
     */
    public String getName() {
        return this.name;
    }
    /**
     * getSupportedInstance lấy ra các instance của thuật toán
     * @return List<String>
     */
    public abstract List<String> getSupportedInstace();

    /**
     * getSupportedKeySize  lấy ra các key size mà thuật toán hỗ trợ
     * @return List<Integer>
     */
    public abstract List<Integer> getSupportedKeySize();

    /**
     * setInstance  cài đặt instance cipher
     * @param instance  instance thuật toán
     * @throws Exception    thuật toán không được hỗ trợ
     */
    public void setInstance(String instance) throws Exception {
        try {
            cip = Cipher.getInstance(instance);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        } catch (NoSuchPaddingException e) {
            throw new Exception(CipherException.NO_SUCH_PADDING);
        }
    }

    /**
     * genKey   khởi tạo key
     * @param keySize   kích thước key
     * @return  KeyPair
     * @throws Exception    thuật toán không được hỗ trợ
     */
    public KeyPair genKey(int keySize) throws Exception {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(this.name);
            generator.initialize(keySize);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
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
            KeyFactory keyFactory = KeyFactory.getInstance(this.name);
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
            KeyFactory keyFactory = KeyFactory.getInstance(this.name);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            this.privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        } catch (InvalidKeySpecException e) {
            throw new Exception(CipherException.INVALID_KEY);
        }
    }

    /**
     * encrypt  mã hóa chuỗi thành một mảng byte
     * @param text  chuỗi đầu vào
     * @return bytes[]  mảng byte đã đụợc mã hóa
     * @throws Exception    key không hợp lệ
     */
    public byte[] encrypt(String text) throws Exception {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        try {
            cip.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        }
        return cip.doFinal(bytes);
    }

    /**
     * encryptBase64    mã hóa chuỗi thành một chuỗi dưới dạng base64
     * @param text  chuỗi đầu vào
     * @return String   chuỗi được mã hóa ở dạng base64
     * @throws Exception    key không hợp lệ
     */
    public String encryptBase64(String text) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(text));
    }

    /**
     * decrypt  giải mã chuỗi ở dạng mảng bytes
     * @param bytes mảng byte đầu vào
     * @return String   chuỗi được giải mã
     * @throws Exception    key không hợp lệ
     */
    public String decrypt(byte[] bytes) throws Exception {
        try {
            cip.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        }
        return new String(cip.doFinal(bytes), StandardCharsets.UTF_8);
    }

    /**
     * decryptBase64  giải mâ chuỗi ở dạng chuỗi base64
     * @param text  chuỗi base64 đầu vào
     * @return String chuỗi được giải mã
     * @throws Exception    key không hợp lệ
     */
    public String decryptBase64(String text) throws Exception {
        byte[] decodedBase64 = Base64.getDecoder().decode(text);
        return decrypt(decodedBase64);
    }
}