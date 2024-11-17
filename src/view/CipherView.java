package view;

import model.asymmetric.AsymmetricCipher;
import model.classical.IClassicalCipher;
import model.hash.IHashAlgorithm;
import model.symmetric.SymmetricCipher;
import model.symmetric.SymmetricCipherThirdParty;
import utils.IconUtils;
import view.asymmetric.AsymmetricCipherView;
import view.classical.ClassicalCipherView;
import view.hash.HashAlgorithmView;
import view.symmetric.SymmetricCipherThirdPartyView;
import view.symmetric.SymmetricCipherView;

import javax.swing.*;
import java.awt.event.ActionListener;

public class CipherView extends JFrame implements ICipherView {
	public CipherView() {
		setTitle("Cipher Application");
		setIconImage(IconUtils.LOGO_CIPHER.getImage());
		setSize(1216, 834);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setUIDefault();
	}

	/**
	 * setUIDefault	điều chỉnh một số thuộc tính mặc định của giao diện
	 */
	private void setUIDefault() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
			e.printStackTrace();
		}
        ToolTipManager.sharedInstance().setInitialDelay(100);	// tool tip text hiển thị nhanh hơn
	}

	/**
	 * display	hiển thị giao diện
	 */
	@Override
	public void display() {
		this.setVisible(true);
	}

	/**
	 * createMenuView	khởi tạo giao diện trang chủ
	 * @param listener	đối tượng xử lý sự kiện cho giao diện này
	 */
	@Override
	public void createMenuView(ActionListener listener) {
		JPanel menuView = new MenuView(listener);
		this.setContentPane(menuView);
	}

	/**
	 * createClassicalCipherView	khởi tạo giao diện trang mã hóa
	 * @param classicalCipher	đối tượng thực hiện
	 */
	@Override
	public void createClassicalCipherView(IClassicalCipher classicalCipher) {
		JFrame cipherView = new JFrame();
		cipherView.setIconImage(IconUtils.LOGO_CIPHER.getImage());
		cipherView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cipherView.setSize(571, 834);
		cipherView.setLocationRelativeTo(null);
		cipherView.setContentPane(new ClassicalCipherView(classicalCipher));
		cipherView.setTitle("Mã hóa " + classicalCipher.getName());
		cipherView.setVisible(true);
	}

	/**
	 * createSymmetricCipherView	khởi tạo giao diện trang mã hóa
	 * @param symmetricCipher	đối tượng thực hiện
	 */
	@Override
	public void createSymmetricCipherView(SymmetricCipher symmetricCipher) {
		JFrame cipherView = new JFrame();
		cipherView.setIconImage(IconUtils.LOGO_CIPHER.getImage());
		cipherView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cipherView.setSize(1000, 834);
		cipherView.setLocationRelativeTo(null);
		cipherView.setContentPane(new SymmetricCipherView(symmetricCipher));
		cipherView.setTitle("Mã hóa " + symmetricCipher.getName());
		cipherView.setVisible(true);
	}

	/**
	 * createSymmetricCipherThirdPartyView	khởi tạo giao diện trang mã hóa
	 * @param symmetricCipher	đối tượng thực hiện
	 */
	@Override
	public void createSymmetricCipherThirdPartyView(SymmetricCipherThirdParty symmetricCipher) {
		JFrame cipherView = new JFrame();
		cipherView.setIconImage(IconUtils.LOGO_CIPHER.getImage());
		cipherView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cipherView.setSize(1000, 834);
		cipherView.setLocationRelativeTo(null);
		cipherView.setContentPane(new SymmetricCipherThirdPartyView(symmetricCipher));
		cipherView.setTitle("Mã hóa " + symmetricCipher.getName());
		cipherView.setVisible(true);
	}

	/**
	 * createAsymmetricCipherView	khởi tạo giao diện trang mã hóa
	 * @param asymmetricCipher	đối tượng thực hiện
	 */
	@Override
	public void createAsymmetricCipherView(AsymmetricCipher asymmetricCipher) {
		JFrame cipherView = new JFrame();
		cipherView.setIconImage(IconUtils.LOGO_CIPHER.getImage());
		cipherView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cipherView.setSize(1216, 834);
		cipherView.setLocationRelativeTo(null);
		cipherView.setContentPane(new AsymmetricCipherView(asymmetricCipher));
		cipherView.setTitle("Mã hóa " + asymmetricCipher.getName());
		cipherView.setVisible(true);
	}

	/**
	 * createHashAlgorithmView	khởi tạo giao diện giải thuật hash
	 * @param algorithm	đối tượng thực hiện
	 */
	public void createHashAlgorithmView(IHashAlgorithm algorithm) {
		JFrame cipherView = new JFrame();
		cipherView.setIconImage(IconUtils.LOGO_CIPHER.getImage());
		cipherView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cipherView.setSize(571, 834);
		cipherView.setLocationRelativeTo(null);
		cipherView.setContentPane(new HashAlgorithmView(algorithm));
		cipherView.setTitle("Giải thuật " + algorithm.getName());
		cipherView.setVisible(true);
	}

	/**
	 * showErrorDialog	hiển thị cửa sổ thông báo lỗi
	 * @param message	thông báo
	 */
	@Override
	public void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Lỗi khởi tạo", JOptionPane.ERROR_MESSAGE);
	}
}
