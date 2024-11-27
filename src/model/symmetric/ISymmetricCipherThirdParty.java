package model.symmetric;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.params.KeyParameter;

import java.util.List;

public interface ISymmetricCipherThirdParty {
    /**
     * getName  lấy ra tên giải thuật
     * @return  String
     */
    public String getName();

    /**
     * getSupportedKeySize  lấy ra kích thước key thuật toán hỗ trợ
     * @return List<Integer>
     */
    public List<Integer> getSupportedKeySize();

    /**
     * getSupportedMode lấy ra các mode mà thuật toán hỗ trợ
     * @return List<String>
     */
    public List<String> getSupportedMode();

    /**
     * getSupportedPadding  lấy ra các padding mà thuật toán hỗ trợ
     * @return List<String>
     */
    public List<String> getSupportedPadding();

    /**
     * getSupportedIvOrNonceSize    lấy ra kích thước iv hoặc nonce thuật toán hỗ trợ
     * @return int
     */
    public int getSupportedIvOrNonceSize();

    /**
     * createMode   khởi tạo mode cùng thuật toán
     * @param name  tên mode
     * @param cipher    thuật toán thực hiện
     * @return BlockCipher
     * @throws Exception    thuật toán không hợp lệ
     */
    public BlockCipher createMode(String name, BlockCipher cipher) throws Exception;

    /**
     * createPadding    khởi tạo padding
     * @param name  tên padding
     * @return BlockCipherPadding
     * @throws Exception    padding không hợp lệ
     */
    public BlockCipherPadding createPadding(String name) throws Exception;

    /**
     * setCipher    cài đặt thuật toán thực hiện
     * @param mode  tên mode
     * @param padding   tên padding
     * @throws Exception
     */
    public void setCipher(String mode, String padding) throws Exception;

    /**
     * genKey   tạo key ngẫu nhiên
     * @return KeyParameter
     */
    public KeyParameter genKey();

    /**
     * loadKey  gán key thủ công
     * @param key KeyParameter
     */
    public void loadKey(KeyParameter key);

    /**
     * loadKey  gán key thủ công từ chuỗi
     * @param text  chuỗi đầu vào
     * @throws Exception    key không hợp lệ
     */
    public void loadKey(String text) throws Exception;

    /**
     * generateRandomIv tạo iv ngẫu nhiên
     * @return String
     */
    public String generateRandomIv();

    /**
     * setIv    cài đặt iv
     * @param bytes mảng byte đầu vào
     * @throws Exception    iv có kích thước với block size
     */
    public void setIv(byte[] bytes) throws Exception;

    /**
     * setIv    cài đặt iv từ chuỗi
     * @param text  chuỗi đầu vào
     * @throws Exception    iv có kích thước với block size
     */
    public void setIv(String text) throws Exception;

    /**
     * encrypt  mã hóa chuỗi thành một mảng byte
     * @param text  chuỗi đầu vào
     * @param useIv có sử dụng Iv không
     * @return bytes[]  mảng byte đã đụợc mã hóa
     * @throws Exception    key không hợp lệ
     */
    public byte[] encrypt(String text, boolean useIv) throws Exception;

    /**
     * encryptBase64    mã hóa chuỗi thành một chuỗi dưới dạng base64
     * @param text  chuỗi đầu vào
     * @param useIv có sử dụng Iv không
     * @return String   chuỗi được mã hóa ở dạng base64
     * @throws Exception    key không hợp lệ
     */
    public String encryptBase64(String text, boolean useIv) throws Exception;

    /**
     * decrypt  giải mã chuỗi ở dạng mảng bytes
     * @param inputBytes mảng byte đầu vào
     * @param useIv có sử dụng Iv không
     * @return String   chuỗi được giải mã
     * @throws Exception    key không hợp lệ
     */
    public String decrypt(byte[] inputBytes, boolean useIv) throws Exception;

    /**
     * decryptBase64  giải mâ chuỗi ở dạng chuỗi base64
     * @param text  chuỗi base64 đầu vào
     * @param useIv có sử dụng Iv không
     * @return String chuỗi được giải mã
     * @throws Exception    key không hợp lệ
     */
    public String decryptBase64(String text, boolean useIv) throws Exception;

    /**
     * encryptFile  mã hóa file
     * @param src   đường dẫn file nguồn
     * @param dest  đường dẫn file đích
     * @param useIv có sử dụng Iv không
     * @return boolean
     * @throws Exception
     */
    public boolean encryptFile(String src, String dest, boolean useIv) throws Exception;

    /**
     * decryptFile  giải mã file
     * @param src   đường dẫn file nguồn
     * @param dest  đường dẫn file đích
     * @param useIv có sử dụng Iv không
     * @return boolean
     * @throws Exception
     */
    public boolean decryptFile(String src, String dest, boolean useIv) throws Exception;
}
