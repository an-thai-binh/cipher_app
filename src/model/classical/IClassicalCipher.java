package model.classical;

public interface IClassicalCipher {
	/**
	 * getName	lấy ra tên của thuật toán mã hóa
	 * @return	String
	 */
	public String getName();

	/**
	 * setAlphabet	cài đặt bảng chữ cái theo ngôn ngữ
	 * @param languageCode	mã ngôn ngữ	(trong lớp LanguageSupport)
	 * @throws Exception	mã ngôn ngữ không hỗ trợ
	 */
	public void setAlphabet(int languageCode) throws Exception;

	/**
	 * setOrder	cài đặt bậc cho ma trận key (chỉ áp dụng cho Hill)
	 * @param order	bậc ma trận
	 * @throws Exception	kích thước không được hỗ trợ
	 */
	public void setOrder(int order) throws Exception;

	/**
	 * genKey	tạo key ngẫu nhiên
	 * @return	Object
	 */
	public Object genKey();

	/**
	 * loadKey	gắn key thủ công
	 * @param o	key
	 */
	public void loadKey(Object o) throws Exception;

	/**
	 * encrypt	mã hóa
	 * @param text	chuỗi cần mã hóa
	 * @return	String	chuỗi mã hóa
	 * @throws Exception	lỗi mã hóa
	 */
	public String encrypt(String text) throws Exception;

	/**
	 * decrypt	giải mã
	 * @param text	chuỗi cần giải mã
	 * @return	String	chuỗi giải mã
	 * @throws Exception	lỗi giải mã
	 */
	public String decrypt(String text) throws Exception;
}
