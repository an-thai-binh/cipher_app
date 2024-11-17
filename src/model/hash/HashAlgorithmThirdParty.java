package model.hash;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.Blake2sDigest;
import org.bouncycastle.crypto.digests.TigerDigest;
import org.bouncycastle.crypto.digests.WhirlpoolDigest;
import utils.CipherException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class HashAlgorithmThirdParty implements IHashAlgorithm {
    private String name;
    private Digest digest;
    public HashAlgorithmThirdParty(String name) {
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
        switch (instance) {
            case "Tiger": {
                this.digest = new TigerDigest();
                break;
            }
            case "Blake2s": {
                this.digest = new Blake2sDigest();
                break;
            }
            case "Whirlpool": {
                this.digest = new WhirlpoolDigest();
                break;
            }
            default: {
                throw new Exception(CipherException.NO_SUCH_ALGORITHM);
            }
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
        digest.update(data, 0, data.length);
        byte[] result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        BigInteger bigInt = new BigInteger(1, result);
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
        setInstance(this.name);
        File file = new File(src);
        if(!file.exists() || !file.isFile()) {
            throw new Exception(CipherException.FILE_NOT_FOUND);
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] b = new byte[10 * 1024];
        int bytesRead;
        while((bytesRead = bis.read(b)) != -1) {    // đọc dữ liệu từ file cập nhật vô digest
            digest.update(b, 0, bytesRead);
        }
        bis.close();
        byte[] result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        BigInteger bigInt = new BigInteger(1, result);
        return bigInt.toString(16);
    }

    public static void main(String[] args) throws Exception {
        HashAlgorithmThirdParty hashAlgorithm = new HashAlgorithmThirdParty("Whirlpool");
        String text = "Xin chào cả nhà của kem";
        hashAlgorithm.setInstance(hashAlgorithm.getName());
        System.out.println(hashAlgorithm.hash(text));
    }
}
