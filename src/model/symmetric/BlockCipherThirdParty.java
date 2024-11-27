package model.symmetric;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.CamelliaEngine;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.engines.SerpentEngine;
import org.bouncycastle.crypto.modes.*;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import utils.CipherException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class BlockCipherThirdParty implements ISymmetricCipherThirdParty {
    private BlockCipher engine;
    private BufferedBlockCipher cip;
    private KeyParameter key;
    private byte[] iv;

    public BlockCipherThirdParty(String engine) throws Exception {
        switch (engine) {
            case "Serpent": {
                this.engine = new SerpentEngine();
                break;
            }
            case "Camellia": {
                this.engine = new CamelliaEngine();
                break;
            }
            case "SEED": {
                this.engine = new SEEDEngine();
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
        return engine.getAlgorithmName();
    }

    /**
     * getSupportedKeySize  lấy ra kích thước key thuật toán hỗ trợ
     * @return List<Integer>
     */
    public List<Integer> getSupportedKeySize() {
        List<Integer> result = new ArrayList<>();
        result.add(engine.getBlockSize() * 8);
        return result;
    }

    @Override
    public List<String> getSupportedMode() {
        List<String> result = new ArrayList<>();
        result.add("ECB");
        result.add("CBC");
        result.add("SIC");
        return result;
    }

    @Override
    public List<String> getSupportedPadding() {
        List<String> result = new ArrayList<>();
        result.add("PKCS7Padding");
        result.add("ISO10126d2Padding");
        return result;
    }

    /**
     * getSupportedIvOrNonceSize    lấy ra kích thước iv hoặc nonce thuật toán hỗ trợ
     * @return int
     */
    public int getSupportedIvOrNonceSize() {
        return engine.getBlockSize();
    }

    /**
     * createMode   khởi tạo mode cùng thuật toán
     * @param name  tên mode
     * @param cipher    thuật toán thực hiện
     * @return BlockCipher
     * @throws Exception    thuật toán không hợp lệ
     */
    public BlockCipher createMode(String name, BlockCipher cipher) throws Exception {
        switch (name) {
            case "ECB": {
                return cipher;
            }
            case "CBC": {
                return new CBCBlockCipher(cipher);
            }
            case "SIC": {
                return new SICBlockCipher(cipher);
            }
            default: {
                throw new Exception(CipherException.NO_SUCH_ALGORITHM);
            }
        }
    }

    /**
     * createPadding    khởi tạo padding
     * @param name  tên padding
     * @return BlockCipherPadding
     * @throws Exception    padding không hợp lệ
     */
    public BlockCipherPadding createPadding(String name) throws Exception {
        switch (name) {
            case "PKCS7Padding": {
                return new PKCS7Padding();
            }
            case "ISO10126d2Padding": {
                return new ISO10126d2Padding();
            }
            default: {
                throw new Exception(CipherException.NO_SUCH_PADDING);
            }
        }
    }

    /**
     * setCipher    cài đặt thuật toán thực hiện
     * @param mode  tên mode
     * @param padding   tên padding
     * @throws Exception
     */
    public void setCipher(String mode, String padding) throws Exception {
        if(padding.equals("NoPadding")) {
            cip = new BufferedBlockCipher(createMode(mode, engine));
        } else {
            cip = new PaddedBufferedBlockCipher(createMode(mode, engine), createPadding(padding));
        }
    }

    /**
     * genKey   tạo key ngẫu nhiên
     * @return KeyParameter
     */
    public KeyParameter genKey() {
        byte[] bytes = new byte[engine.getBlockSize()];
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
        if(bytes.length != engine.getBlockSize()) {
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
        int blockSize = engine.getBlockSize();
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
        if(bytes.length != engine.getBlockSize()) {
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
        int blockSize = engine.getBlockSize();
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
        byte[] outputBytes = new byte[cip.getOutputSize(inputBytes.length)];
        int length = cip.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
        try {
            length += cip.doFinal(outputBytes, length);
        } catch (InvalidCipherTextException e) {
            throw new Exception(CipherException.EXECUTE_EXCEPTION);
        }
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
        byte[] outputBytes = new byte[cip.getOutputSize(inputBytes.length)];
        int length = cip.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
        try {
            length += cip.doFinal(outputBytes, length);
        } catch (InvalidCipherTextException e) {
            throw new Exception(CipherException.EXECUTE_EXCEPTION);
        }
        return new String(outputBytes, 0, length, StandardCharsets.UTF_8);
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
        byte[] outBuffer = new byte[cip.getOutputSize(inBuffer.length)];
        int bytesRead;
        while((bytesRead = bis.read(inBuffer)) != -1) {
            int processBytes = cip.processBytes(inBuffer, 0, bytesRead, outBuffer, 0);
            bos.write(outBuffer, 0, processBytes);
        }
        bytesRead = cip.doFinal(outBuffer, 0);
        bos.write(outBuffer, 0 , bytesRead);
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
        byte[] outBuffer = new byte[cip.getOutputSize(inBuffer.length)];
        int bytesRead;
        while((bytesRead = bis.read(inBuffer)) != -1) {
            int processBytes = cip.processBytes(inBuffer, 0, bytesRead, outBuffer, 0);
            bos.write(outBuffer, 0, processBytes);
        }
        bytesRead = cip.doFinal(outBuffer, 0);
        bos.write(outBuffer, 0 , bytesRead);
        bos.close();
        bis.close();
        return true;
    }

//    public static void main(String[] args) throws Exception {
//        SymmetricCipherThirdParty cipher = new SymmetricCipherThirdParty("Serpent");
//        KeyParameter key = cipher.genKey();
//        String iv = "0000000000000000";
//        cipher.loadKey(key);
//        cipher.setIv(iv);
//        cipher.setCipher("CBC", "PKCS7Padding");
////        String encrypted = cipher.encryptBase64("tôi là An Thái Bình", true);
////        System.out.println(encrypted);
////        System.out.println(cipher.decryptBase64(encrypted, true));
//        String src = "D:/Documents/TEMQA.pptx";
//        String desc = "D:/Documents/temqa-encrypt.pptx";
//        String desc2 = "D:/Documents/temqa-decrypt.pptx";
//        cipher.encryptFile(src, desc, true);
//        cipher.decryptFile(desc, desc2, true);
//    }
 }
