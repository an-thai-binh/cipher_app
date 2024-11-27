package model.symmetric;

import utils.CipherException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public abstract class SymmetricCipher {
    protected String name;
    private AlgorithmParameterSpec iv;
    private Cipher cip;
    private SecretKey key;
    protected boolean isFixedKeySize;

    protected SymmetricCipher() {
        this.iv = null;
    }

    /**
     * setCipher    cài đặt thuật toán thực hiện
     * @throws Exception    lỗi thuật toán hoặc padding
     */
    public void setCipher(String mode, String padding) throws Exception {
        try {
            cip = Cipher.getInstance(name + "/" + mode + "/" + padding);
            System.out.println(cip.getAlgorithm());
            System.out.println(cip.getBlockSize());
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        } catch (NoSuchPaddingException e) {
            throw new Exception(CipherException.NO_SUCH_PADDING);
        }
    }

    /**
     * getName  lấy ra tên thuật toán thực hiện
     * @return  String
     */
    public String getName() {
        return this.name;
    }

    /**
     * isFixedKeySize   kiểm tra thuật toán có cố định key size không
     * @return
     */
    public boolean isFixedKeySize() {
        return this.isFixedKeySize;
    }

    /**
     * getSupportedIvOrNonceSize    lấy ra kích thước IV mà thuật toán hỗ trợ
     * @return int  số byte
     */
    public abstract int getSupportedIvOrNonceSize();

    /**
     * getSupportedKeySize  lấy ra các key size mà thuật toán hỗ trợ
     * @return List<Integer>
     */
    public abstract List<Integer> getSupportedKeySize();

    /**
     * getSupportedMode lấy ra các mode mà thuật toán hỗ trợ
     * @return List<String>
     */
    public abstract List<String> getSupportedMode();

    /**
     * getSupportedPadding  lấy ra các padding mà thuật toán hỗ trợ
     * @return List<String>
     */
    public abstract List<String> getSupportedPadding();

    /**
     * setIvParameterSpec   cài đặt tham số IV
     * @param iv    AlgorithmParameterSpec
     */
    public void setIvParameterSpec(AlgorithmParameterSpec iv) {
        this.iv = iv;
    }

    /**
     * setIvParameterSpec   cài đặt tham số IV từ chuỗi
     * @param text  chuỗi đầu vào
     * @return boolean (true nếu text có kích thước byte bằng block size ngược lại thì false)
     */
    public boolean setIvParameterSpec(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        int blockSize = getSupportedIvOrNonceSize();
        if(bytes.length == blockSize) {
            setIvParameterSpec(bytes);
            return true;
        } else if(bytes.length < blockSize) {
            bytes = Arrays.copyOf(bytes, blockSize);    // tạo mảng byte mới với kích thước của block size (mặc định byte trống = 0)
            setIvParameterSpec(bytes);
            return false;
        } else {
            bytes = Arrays.copyOfRange(bytes, 0, blockSize);
            setIvParameterSpec(bytes);
            return false;
        }
    }

    /**
     * setIvParameterSpec   cài đặt tham số IV từ mảng byte
     * @param bytes mảng byte đầu vào
     */
    public void setIvParameterSpec(byte[] bytes) {
        this.iv = new IvParameterSpec(bytes);
    }

    /**
     * generateRandomIv tạo chuỗi Iv ngẫu nhiên
     * @param blockSize block size
     * @return  String
     */
    public String generateRandomIv(int blockSize) {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(blockSize);
        for (int i = 0; i < blockSize; i++) {
            int n = random.nextInt(charSet.length());
            result.append(charSet.charAt(n));
        }
        return result.toString();
    }

    /**
     * genKey   khởi tạo key
     * @param keySize   kích thước key
     * @return  SecretKey
     * @throws Exception    thuật toán không được hỗ trợ
     */
    public SecretKey genKey(int keySize) throws Exception {
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance(name);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        }
        generator.init(keySize);
        key = generator.generateKey();
        return key;
    }

    /**
     * loadKey  gán key thủ công
     * @param key   key
     */
    public void loadKey(SecretKey key) {
        this.key = key;
    }

    /**
     * encrypt  mã hóa chuỗi thành một mảng byte
     * @param text  chuỗi đầu vào
     * @return bytes[]  mảng byte đã đụợc mã hóa
     * @throws Exception    key không hợp lệ
     */
    public byte[] encrypt(String text) throws Exception {
        byte[] result = null;
        try {
            if(iv == null)
                cip.init(Cipher.ENCRYPT_MODE, key);
            else
                cip.init(Cipher.ENCRYPT_MODE, key, iv);
            result = cip.doFinal(text.getBytes(StandardCharsets.UTF_8));
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        }
        return result;
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
        String result = null;
        try {
            if(iv == null)
                cip.init(Cipher.DECRYPT_MODE, key);
            else
                cip.init(Cipher.DECRYPT_MODE, key, iv);
            result = new String(cip.doFinal(bytes), StandardCharsets.UTF_8);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new Exception(CipherException.INVALID_KEY);
        }
        return result;
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

    /**
     * encryptFile  mã hóa file
     * @param src   file nguồn
     * @param dest  file đích
     * @return boolean
     */
    public boolean encryptFile(String src, String dest) throws Exception {
        try {
            if(iv == null)
                cip.init(Cipher.ENCRYPT_MODE, key);
            else
                cip.init(Cipher.ENCRYPT_MODE, key, iv);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
            CipherInputStream cis = new CipherInputStream(bis, cip);	// mã hóa ở đầu vào
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] b = new byte[10 * 1024];
            int bytesRead;
            while((bytesRead = cis.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            bos.flush();
            bos.close();
            cis.close();
            return true;
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        } catch (FileNotFoundException e) {
            throw new Exception(CipherException.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new Exception(CipherException.IO_EXCEPTION);
        }
    }

    /**
     * decryptFile  giải mã file
     * @param src   file nguồn
     * @param dest  file đích
     * @return boolean
     */
    public boolean decryptFile(String src, String dest) throws Exception {
        try {
            if(iv == null)
                cip.init(Cipher.DECRYPT_MODE, key);
            else
                cip.init(Cipher.DECRYPT_MODE, key, iv);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
            CipherOutputStream cos = new CipherOutputStream(bos, cip);	// giải mã ở đầu ra
            byte[] b = new byte[10 * 1024];
            int bytesRead;
            while((bytesRead = bis.read(b)) != -1) {
                cos.write(b, 0, bytesRead);
            }
            cos.flush();
            cos.close();
            bis.close();
            return true;
        } catch (InvalidKeyException e) {
            throw new Exception(CipherException.INVALID_KEY);
        } catch (FileNotFoundException e) {
            throw new Exception(CipherException.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new Exception(CipherException.IO_EXCEPTION);
        }
    }
}