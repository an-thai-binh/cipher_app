package controller;

import model.ICipherModel;
import model.asymmetric.AsymmetricCipher;
import model.classical.IClassicalCipher;
import model.hash.IHashAlgorithm;
import model.signature.DigitalSignature;
import model.symmetric.SymmetricCipher;
import model.symmetric.SymmetricCipherThirdParty;
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
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (CipherSupport.isContains(CipherSupport.CLASSICAL_CIPHERS, cmd)) {    // cổ điển
				try {
					IClassicalCipher cipher = model.createClassicalCipher(cmd, LanguageSupport.EN, 2);
					if (cipher != null) {
						view.createClassicalCipherView(cipher);
					} else {
						view.showErrorDialog("Không khởi tạo được thuật toán");
					}
				} catch (Exception ex) {
					view.showErrorDialog("Cấu hình cho phương pháp không hợp lệ");
				}
			} else if (CipherSupport.isContains(CipherSupport.SYMMETRIC_CIPHERS, cmd)) {    // đối xứng hiện đại
				SymmetricCipher cipher = model.createSymmetricCipher(cmd);
				if (cipher != null) {
					view.createSymmetricCipherView(cipher);
				} else {
					view.showErrorDialog("Không khởi tạo được thuật toán");
				}
			} else if (CipherSupport.isContains(CipherSupport.SYMMETRIC_CIPHERS_THIRD_PARTY, cmd)) {    // đối xứng hiện đại thư viện thứ 3
				SymmetricCipherThirdParty cipher = null;
				try {
					cipher = model.createSymmetricCipherThirdParty(cmd);
				} catch (Exception ex) {
					view.showErrorDialog(ex.getMessage());
				}
				if (cipher != null) {
					view.createSymmetricCipherThirdPartyView(cipher);
				} else {
					view.showErrorDialog("Không khởi tạo được thuật toán");
				}
			} else if (CipherSupport.isContains(CipherSupport.ASYMMETRIC_CIPHERS, cmd)) {    // bất đối xứng
				AsymmetricCipher cipher = model.createAsymmetric(cmd);
				if (cipher != null) {
					view.createAsymmetricCipherView(cipher);
				} else {
					view.showErrorDialog("Không khởi tạo được thuật toán");
				}
			} else if (CipherSupport.isContains(CipherSupport.HASH_ALGORITHMS, cmd) || CipherSupport.isContains(CipherSupport.HASH_ALGORITHMS_THIRD_PARTY, cmd)) {
				IHashAlgorithm algorithm = model.createHashAlgorithm(cmd);
				if(algorithm != null) {
					view.createHashAlgorithmView(algorithm);
				} else {
					view.showErrorDialog("Không khởi tạo được thuật toán");
				}
			}  else if(cmd.equals("DigitalSignature")) {
				DigitalSignature ds = model.createDigitalSignature();
				if(ds != null) {
					view.createDigitalSignatureView(ds);
				} else {
					view.showErrorDialog("Không khởi tạo được thuật toán");
				}
			} else {
				view.showErrorDialog("Phương pháp không hỗ trợ");
			}
		}
	}
}
