package view;

import model.asymmetric.AsymmetricCipher;
import model.classical.IClassicalCipher;
import model.hash.IHashAlgorithm;
import model.signature.DigitalSignature;
import model.symmetric.ISymmetricCipherThirdParty;
import model.symmetric.SymmetricCipher;

import java.awt.event.ActionListener;

public interface ICipherView {
	/**
	 * display	hiển thị giao diện
	 */
	public void display();

	/**
	 * createMenuView	khởi tạo giao diện trang chủ
	 * @param listener	đối tượng xử lý sự kiện cho giao diện này
	 */
	public void createMenuView(ActionListener listener);

	/**
	 * createClassicalCipherView	khởi tạo giao diện trang mã hóa
	 * @param classicalCipher	đối tượng thực hiện
	 */
	public void createClassicalCipherView(IClassicalCipher classicalCipher);

	/**
	 * createSymmetricCipherView	khởi tạo giao diện trang mã hóa
	 * @param symmetricCipher	đối tượng thực hiện
	 */
	public void createSymmetricCipherView(SymmetricCipher symmetricCipher);

	/**
	 * createSymmetricCipherThirdPartyView	khởi tạo giao diện trang mã hóa
	 * @param symmetricCipher	đối tượng thực hiện
	 */
	public void createSymmetricCipherThirdPartyView(ISymmetricCipherThirdParty symmetricCipher);

	/**
	 * createAsymmetricCipherView	khởi tạo giao diện trang mã hóa
	 * @param asymmetricCipher	đối tượng thực hiện
	 */
	public void createAsymmetricCipherView(AsymmetricCipher asymmetricCipher);

	/**
	 * createHashAlgorithmView	khởi tạo giao diện giải thuật hash
	 * @param algorithm	đối tượng thực hiện
	 */
	public void createHashAlgorithmView(IHashAlgorithm algorithm);

	/**
	 * createDigitalSignatureView	khởi tạo giao diện chữ ký điện tử
	 * @param ds	đối tượng thực hiện
	 */
	public void createDigitalSignatureView(DigitalSignature ds);

	/**
	 * showErrorDialog	hiển thị cửa sổ thông báo lỗi
	 * @param message	thông báo
	 */
	public void showErrorDialog(String message);


}
