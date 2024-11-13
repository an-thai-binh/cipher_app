package model;

import model.asymmetric.AsymmetricCipher;
import model.classical.IClassicalCipher;
import model.symmetric.SymmetricCipher;

public interface ICipherModel {
	/**
	 * createClassicalCipher	khởi tạo đối tượng mã hóa cổ điển
	 * @param cipher	phương pháp mã hóa
	 * @param languageCode	mã ngôn ngữ
	 * @param order	bậc ma trận cho mã hóa Hill
	 * @return	IClassicalCipher
	 * @throws Exception	cấu hình không hợp lệ
	 */
	public IClassicalCipher createClassicalCipher(String cipher, int languageCode, int order) throws Exception;

	/**
	 * createSymmetricCipher	khởi tạo đối tượng mã hóa đối xứng hiện đại
	 * @param cipher	phương pháp mã hóa
	 * @return SymmetricCipher
	 */
	public SymmetricCipher createSymmetricCipher(String cipher);

	/**
	 * createAsymmetricCipher	khởi tạo đối tượng mã hóa bất đối xứng
	 * @param cipher	phương pháp mã hóa
	 * @return SymmetricCipher
	 */
    public AsymmetricCipher createAsymmetric(String cipher);
}
