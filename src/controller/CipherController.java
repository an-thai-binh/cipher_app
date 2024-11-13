package controller;

import model.ICipherModel;
import model.asymmetric.AsymmetricCipher;
import model.classical.CeasarCipher;
import model.classical.IClassicalCipher;
import model.symmetric.SymmetricCipher;
import utils.CipherSupport;
import utils.LanguageSupport;
import view.ICipherView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CipherController implements ICipherController {
	private ICipherView view;
	private ICipherModel model;
	public CipherController(ICipherView view, ICipherModel model) {
		this.view = view;
		this.model = model;
	}

	/**
	 * start	khởi động giao diện
	 */
	@Override
	public void start() {
		view.createMenuView(new MenuListener());
		view.display();
	}

	class MenuListener implements ActionListener {

		/**
		 * isContains	kiểm tra giải thuật này có hỗ trợ không
		 * @param ciphers	danh sách giải thuật hỗ trợ
		 * @param cipherName	giải thuật cần kiểm tra
		 * @return boolean
		 */
		private boolean isContains(String[] ciphers, String cipherName) {
			for(String cipher: ciphers) {
				if(cipherName.equals(cipher)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if(isContains(CipherSupport.CLASSICAL_CIPHERS, cmd)) {
				try {
					IClassicalCipher cipher = model.createClassicalCipher(cmd, LanguageSupport.EN, 2);
					if(cipher != null) {
						view.createClassicalCipherView(cipher);
					} else {
						view.showErrorDialog("Không khởi tạo được thuật toán");
					}
				} catch (Exception ex) {
					view.showErrorDialog("Cấu hình cho phương pháp không hợp lệ");
				}
			} else if(isContains(CipherSupport.SYMMETRIC_CIPHERS, cmd)) {
				SymmetricCipher cipher = model.createSymmetricCipher(cmd);
				if (cipher != null) {
					view.createSymmetricCipherView(cipher);
				} else {
					view.showErrorDialog("Không khởi tạo được thuật toán");
				}
			} else if(isContains(CipherSupport.ASYMMETRIC_CIPHERS, cmd)) {
				AsymmetricCipher cipher = model.createAsymmetric(cmd);
				if(cipher != null) {
					view.createAsymmetricCipherView(cipher);
				} else {
					view.showErrorDialog("Không khởi tạo được thuật toán");
				}
			} else {
				view.showErrorDialog("Phương pháp không hỗ trợ");
			}
		}
	}
}
