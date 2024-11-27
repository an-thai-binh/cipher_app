package model;

import model.asymmetric.AsymmetricCipher;
import model.classical.IClassicalCipher;
import model.hash.IHashAlgorithm;
import model.signature.DigitalSignature;
import model.symmetric.ISymmetricCipherThirdParty;
import model.symmetric.SymmetricCipher;

public class CipherModel implements ICipherModel {
	private final CipherFactory cipherFactory;
	public CipherModel() {
		this.cipherFactory = new CipherFactory();
	}

	/**
	 * createClassicalCipher	khởi tạo đối tượng mã hóa cổ điển
	 * @param cipher	phương pháp mã hóa
	 * @param languageCode	mã ngôn ngữ
	 * @param order	bậc ma trận cho mã hóa Hill
	 * @return	IClassicalCipher
	 * @throws Exception	cấu hình không hợp lệ
	 */
	@Override
	public IClassicalCipher createClassicalCipher(String cipher, int languageCode, int order) throws Exception {
		return cipherFactory.createClassicalCipher(cipher, languageCode, order);
	}

	/**
	 * createSymmetricCipher	khởi tạo đối tượng mã hóa đối xứng hiện đại
	 * @param cipher	phương pháp mã hóa
	 * @return SymmetricCipher
	 */
	@Override
	public SymmetricCipher createSymmetricCipher(String cipher) {
		return cipherFactory.createSymmetricCipher(cipher);
	}

	/**
	 * createSymmetricCipherThirdParty	khởi tạo đối tượng mã hóa đối xứng hiện đại của thư viện thứ 3
	 * @param cipher	phương pháp mã hóa
	 * @return SymmetricCipher
	 */
	@Override
	public ISymmetricCipherThirdParty createSymmetricCipherThirdParty(String cipher) throws Exception {
		return cipherFactory.createSymmetricCipherThirdParty(cipher);
	}

	/**
	 * createAsymmetricCipher	khởi tạo đối tượng mã hóa bất đối xứng
	 * @param cipher	phương pháp mã hóa
	 * @return SymmetricCipher
	 */
	@Override
	public AsymmetricCipher createAsymmetric(String cipher) {
		return cipherFactory.createAsymmetricCipher(cipher);
	}

	/**
	 * createHashAlgorithm	khởi tạo giải thuật hash
	 * @param algorithm	giải thuật
	 * @return IHashAlgorithm
	 */
	@Override
	public IHashAlgorithm createHashAlgorithm(String algorithm) {
		return cipherFactory.createHashAlgorithm(algorithm);
	}

	/**
	 * createDigitalSignature	khởi tạo đối tượng chữ ký điện tử
	 * @return DigitalSignature
	 */
	@Override
	public DigitalSignature createDigitalSignature() {
		return new DigitalSignature();
	}
}
