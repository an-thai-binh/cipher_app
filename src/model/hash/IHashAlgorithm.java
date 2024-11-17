package model.hash;

public interface IHashAlgorithm {
    /**
     * getName  lấy ra tên thuật toán hash
     * @return
     */
    public String getName();

    /**
     * setInstance  cài đặt instance thực hiện
     * @param instance  instance
     * @throws Exception    thuật toán không hỗ trợ
     */
    public void setInstance(String instance) throws Exception;

    /**
     * hash hash văn bản
     * @param text  văn bản đầu vào
     * @return String
     */
    public String hash(String text);

    /**
     * hashFile hash file
     * @param src   đường dẫn file nguồn
     * @return String
     * @throws Exception    file không hợp lệ
     */
    public String hashFile(String src) throws Exception;
}
