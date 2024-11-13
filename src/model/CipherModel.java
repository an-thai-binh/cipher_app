package model;

import model.asymmetric.AsymmetricCipher;
import model.asymmetric.AsymmetricCipherFactory;
import model.classical.*;
import model.symmetric.SymmetricCipher;
import model.symmetric.SymmetricCipherFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class CipherModel implements ICipherModel {
	private final ClassicalCipherFactory classicalCipherFactory;
	private final SymmetricCipherFactory symmetricCipherFactory;
	private final AsymmetricCipherFactory asymmetricCipherFactory;
	public CipherModel() {
		addProviders();
		this.classicalCipherFactory = new ClassicalCipherFactory();
		this.symmetricCipherFactory = new SymmetricCipherFactory();
		this.asymmetricCipherFactory = new AsymmetricCipherFactory();
	}

	/**
	 * addProviders	thêm các provider từ thư viên thứ 3
	 */
	private void addProviders() {
		Security.addProvider(new BouncyCastleProvider());
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
		return classicalCipherFactory.createClassicalCipher(cipher, languageCode, order);
	}

	/**
	 * createSymmetricCipher	khởi tạo đối tượng mã hóa đối xứng hiện đại
	 * @param cipher	phương pháp mã hóa
	 * @return SymmetricCipher
	 */
	@Override
	public SymmetricCipher createSymmetricCipher(String cipher) {
		return symmetricCipherFactory.createSymmetricCipher(cipher);
	}

	/**
	 * createAsymmetricCipher	khởi tạo đối tượng mã hóa bất đối xứng
	 * @param cipher	phương pháp mã hóa
	 * @return SymmetricCipher
	 */
	@Override
	public AsymmetricCipher createAsymmetric(String cipher) {
		return asymmetricCipherFactory.createAsymmetricCipher(cipher);
	}
}
