package model.symmetric;

import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import utils.CipherException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class StreamCipherThirdParty implements ISymmetricCipherThirdParty {
    private StreamCipher cip;
    private KeyParameter key;
    private byte[] iv;
    public StreamCipherThirdParty(String engine) throws Exception {
        switch (engine) {
            case "ChaCha20": {
                this.cip = new ChaChaEngine();
                break;
            }
            default: {
                throw new Exception(CipherException.NO_SUCH_ALGORITHM);
            }
        }
    }

    /**
     * getName  lấy ra tên giải thuật
     * @return  String
     */
    public String getName() {
        return cip.getAlgorithmName();
    }

    /**
     * getSupportedKeySize  lấy ra kích thước key thuật toán hỗ trợ
     * @return List<Integer>
     */
    public List<Integer> getSupportedKeySize() {
        List<Integer> result = new ArrayList<>();
        result.add(256);
        return result;
    }

    @Override
    public List<String> getSupportedMode() {
        List<String> result = new ArrayList<>();
        result.add("NONE");
        return result;
    }

    @Override
    public List<String> getSupportedPadding() {
        List<String> result = new ArrayList<>();
        result.add("NoPadding");
        return result;
    }

    /**
     * getSupportedIvOrNonceSize    lấy ra kích thước iv hoặc nonce thuật toán hỗ trợ
     * @return int
     */
    public int getSupportedIvOrNonceSize() {
        return 8;
    }

    /**
     * setCipher    cài đặt thuật toán thực hiện
     * @param mode  tên mode
     * @param padding   tên padding
     * @throws Exception
     */
    public void setCipher(String mode, String padding) throws Exception {
        return;
    }

    /**
     * genKey   tạo key ngẫu nhiên
     * @return KeyParameter
     */
    public KeyParameter genKey() {
        byte[] bytes = new byte[this.getSupportedKeySize().getFirst() / 8];
        SecureRandom rd = new SecureRandom();
        rd.nextBytes(bytes);
        return new KeyParameter(bytes);
    }

    /**
     * loadKey  gán key thủ công
     * @param key KeyParameter
     */
    public void loadKey(KeyParameter key) {
        this.key = key;
    }

    /**
     * loadKey  gán key thủ công từ chuỗi
     * @param text  chuỗi đầu vào
     * @throws Exception    key không hợp lệ
     */
    public void loadKey(String text) throws Exception {
        byte[] bytes = text.getBytes();
        if(bytes.length != this.getSupportedKeySize().getFirst() / 8) {
            throw new Exception(CipherException.INVALID_KEY);
        }
        loadKey(new KeyParameter(bytes));
    }

    /**
     * generateRandomIv tạo iv ngẫu nhiên
     * @return String
     */
    public String generateRandomIv() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        int blockSize = this.getSupportedIvOrNonceSize();
        StringBuilder result = new StringBuilder(blockSize);
        for (int i = 0; i < blockSize; i++) {
            int n = random.nextInt(charSet.length());
            result.append(charSet.charAt(n));
        }
        return result.toString();
    }

    /**
     * setIv    cài đặt iv
     * @param bytes mảng byte đầu vào
     * @throws Exception    iv có kích thước với block size
     */
    public void setIv(byte[] bytes) throws Exception {
        if(bytes.length != this.getSupportedIvOrNonceSize()) {
            throw new Exception(CipherException.ILLEGAL_BLOCK_SIZE);
        }
        this.iv = bytes;
    }

    /**
     * setIv    cài đặt iv từ chuỗi
     * @param text  chuỗi đầu vào
     * @throws Exception    iv có kích thước với block size
     */
    public void setIv(String text) throws Exception {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        int blockSize = this.getSupportedIvOrNonceSize();
        if(bytes.length == blockSize) {
            setIv(bytes);
        } else if(bytes.length < blockSize) {
            bytes = Arrays.copyOf(bytes, blockSize);    // tạo mảng byte mới với kích thước của block size (mặc định byte trống = 0)
            setIv(bytes);
        } else {
            bytes = Arrays.copyOfRange(bytes, 0, blockSize);
            setIv(bytes);
        }
    }

    /**
     * encrypt  mã hóa chuỗi thành một mảng byte
     * @param text  chuỗi đầu vào
     * @param useIv có sử dụng Iv không
     * @return bytes[]  mảng byte đã đụợc mã hóa
     * @throws Exception    key không hợp lệ
     */
    public byte[] encrypt(String text, boolean useIv) throws Exception {
        if(useIv) {
            cip.init(true, new ParametersWithIV(this.key, this.iv));
        } else {
            cip.init(true, this.key);
        }
        byte[] inputBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] outputBytes = new byte[inputBytes.length];
        cip.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
        return outputBytes;
    }

    /**
     * encryptBase64    mã hóa chuỗi thành một chuỗi dưới dạng base64
     * @param text  chuỗi đầu vào
     * @param useIv có sử dụng Iv không
     * @return String   chuỗi được mã hóa ở dạng base64
     * @throws Exception    key không hợp lệ
     */
    public String encryptBase64(String text, boolean useIv) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(text, useIv));
    }

    /**
     * decrypt  giải mã chuỗi ở dạng mảng bytes
     * @param inputBytes mảng byte đầu vào
     * @param useIv có sử dụng Iv không
     * @return String   chuỗi được giải mã
     * @throws Exception    key không hợp lệ
     */
    public String decrypt(byte[] inputBytes, boolean useIv) throws Exception {
        if(useIv) {
            cip.init(false, new ParametersWithIV(this.key, this.iv));
        } else {
            cip.init(false, this.key);
        }
        byte[] outputBytes = new byte[inputBytes.length];
        cip.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
        return new String(outputBytes, StandardCharsets.UTF_8);
    }

    /**
     * decryptBase64  giải mâ chuỗi ở dạng chuỗi base64
     * @param text  chuỗi base64 đầu vào
     * @param useIv có sử dụng Iv không
     * @return String chuỗi được giải mã
     * @throws Exception    key không hợp lệ
     */
    public String decryptBase64(String text, boolean useIv) throws Exception {
        byte[] decodedBase64 = Base64.getDecoder().decode(text);
        return decrypt(decodedBase64, useIv);
    }

    /**
     * encryptFile  mã hóa file
     * @param src   đường dẫn file nguồn
     * @param dest  đường dẫn file đích
     * @param useIv có sử dụng Iv không
     * @return boolean
     * @throws Exception
     */
    public boolean encryptFile(String src, String dest, boolean useIv) throws Exception {
        if(useIv) {
            cip.init(true, new ParametersWithIV(this.key, this.iv));
        } else {
            cip.init(true, this.key);
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
        byte[] inBuffer = new byte[10 * 1024];
        int bytesRead;
        while((bytesRead = bis.read(inBuffer)) != -1) {
            byte[] outBuffer = new byte[bytesRead];
            int processBytes = cip.processBytes(inBuffer, 0, bytesRead, outBuffer, 0);
            bos.write(outBuffer, 0, processBytes);
        }
        bos.close();
        bis.close();
        return true;
    }

    /**
     * decryptFile  giải mã file
     * @param src   đường dẫn file nguồn
     * @param dest  đường dẫn file đích
     * @param useIv có sử dụng Iv không
     * @return boolean
     * @throws Exception
     */
    public boolean decryptFile(String src, String dest, boolean useIv) throws Exception {
        if(useIv) {
            cip.init(false, new ParametersWithIV(this.key, this.iv));
        } else {
            cip.init(false, this.key);
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
        byte[] inBuffer = new byte[10 * 1024];
        int bytesRead;
        while((bytesRead = bis.read(inBuffer)) != -1) {
            byte[] outBuffer = new byte[bytesRead];
            int processBytes = cip.processBytes(inBuffer, 0, bytesRead, outBuffer, 0);
            bos.write(outBuffer, 0, processBytes);
        }
        bos.close();
        bis.close();
        return true;
    }
}
