package model.hash;

import utils.CipherException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgorithm implements IHashAlgorithm {
    private String name;
    private MessageDigest md;
    public HashAlgorithm(String name) {
        this.name = name;
    }

    /**
     * getName  lấy ra tên thuật toán hash
     * @return
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * setInstance  cài đặt instance thực hiện
     * @param instance  instance
     * @throws Exception    thuật toán không hỗ trợ
     */
    @Override
    public void setInstance(String instance) throws Exception {
        try {
            md = MessageDigest.getInstance(instance);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(CipherException.NO_SUCH_ALGORITHM);
        }
    }

    /**
     * hash hash văn bản
     * @param text  văn bản đầu vào
     * @return String
     */
    @Override
    public String hash(String text) {
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        byte[] digest = md.digest(data);
        BigInteger bigInt = new BigInteger(1, digest);
        return bigInt.toString(16);
    }

    /**
     * hashFile hash file
     * @param src   đường dẫn file nguồn
     * @return String
     * @throws Exception    file không hợp lệ
     */
    @Override
    public String hashFile(String src) throws Exception {
        File file = new File(src);
        if(!file.exists() || !file.isFile()) {
            throw new Exception(CipherException.FILE_NOT_FOUND);
        }
        DigestInputStream dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), md);
        byte[] b = new byte[10 * 1024];
        int bytesRead;
        do {
            bytesRead = dis.read(b);
        } while (bytesRead != -1);
        byte[] digest = dis.getMessageDigest().digest();
        dis.close();
        BigInteger bigInt = new BigInteger(1, digest);
        return bigInt.toString(16);
    }
}