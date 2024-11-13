package view;

import model.asymmetric.AsymmetricCipher;
import model.classical.IClassicalCipher;
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
	 * createAsymmetricCipherView	khởi tạo giao diện trang mã hóa
	 * @param asymmetricCipher	đối tượng thực hiện
	 */
	public void createAsymmetricCipherView(AsymmetricCipher asymmetricCipher);

	/**
	 * showErrorDialog	hiển thị cửa sổ thông báo lỗi
	 * @param message	thông báo
	 */
	public void showErrorDialog(String message);
}
