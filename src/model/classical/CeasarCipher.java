package model.classical;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import utils.ClassicalException;
import utils.LanguageSupport;

public class CeasarCipher implements IClassicalCipher {
	private int key;
	private String alphabet;
	public CeasarCipher(int languageCode) throws Exception {
		key = 0;
		setAlphabet(languageCode);
	}

	/**
	 * getName	lấy ra tên của thuật toán mã hóa
	 * @return	String
	 */
	@Override
	public String getName() {
		return "Dịch chuyển";
	}

	/**
	 * setAlphabet	cài đặt bảng chữ cái theo ngôn ngữ
	 * @param languageCode	mã ngôn ngữ
	 * @throws Exception	mã ngôn ngữ không hỗ trợ
	 */
	@Override
	public void setAlphabet(int languageCode) throws Exception {
		this.alphabet = LanguageSupport.setAlphabet(languageCode);
	}

	/**
	 * phương thức không hỗ trợ trong phương pháp này
	 */
	@Override
	public void setOrder(int order) throws Exception {
		throw new NoSuchMethodException();
	}

	/**
	 * genKey	tạo key ngẫu nhiên
	 * 1 <= key < n (chiều dài alphabet)
	 * @return	Object (int)
	 */
	@Override
	public Object genKey() {
		Random rd = new Random();
		key = rd.nextInt(alphabet.length() - 1) + 1;	// [1; n)
		return key;
	}
	
	/**
	 * loadKey	gắn key thủ công
	 * @param	o	int
	 */
	@Override
	public void loadKey(Object o) throws Exception {
		try {
			this.key = Integer.parseInt(o.toString());
		} catch (Exception e) {
			throw new Exception(ClassicalException.CEASAR_INVALID_KEY);
		}

	}
	
	/**
	 * encrypt	mã hóa chuỗi với phương pháp mã hóa dịch chuyển
	 * (x + k + n) % n
	 * @param text	chuỗi đầu vào
	 * @return	String	chuỗi được mã hóa
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String encrypt(String text) throws Exception {
		if(key <= 0) throw new Exception(ClassicalException.CEASAR_INVALID_KEY);
		StringBuilder result = new StringBuilder();
		int curPos;
		int newPos;
		char curChar;
		char newChar;
		for(int i = 0; i < text.length(); i+=1) {
			curChar = text.charAt(i);
			if(Character.isLetter(curChar)) {
				curPos = alphabet.indexOf(Character.toUpperCase(curChar));
				newPos = (curPos + key + alphabet.length()) % alphabet.length();
				newChar = alphabet.charAt(newPos);
				result.append(Character.isUpperCase(curChar) ? newChar : Character.toLowerCase(newChar));
			} else {
				result.append(curChar);
			}
		}
		return result.toString();
	}
	
	/**
	 * decrypt	giải mã chuỗi được mã hóa bằng phương pháp dịch chuyển
	 * (x - k + n) % n
	 * @param text	chuỗi đầu vào
	 * @return	chuỗi được giải mã
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String decrypt(String text) throws Exception {
		if(key <= 0) throw new Exception(ClassicalException.CEASAR_INVALID_KEY);
		StringBuilder result = new StringBuilder();
		int curPos;
		int newPos;
		char curChar;
		char newChar;
		for(int i = 0; i < text.length(); i++) {
			curChar = text.charAt(i);
			if(Character.isLetter(curChar)) {
				curPos = alphabet.indexOf(Character.toUpperCase(curChar));
				newPos = (curPos - key + alphabet.length()) % alphabet.length();
				newChar = alphabet.charAt(newPos);
				result.append(Character.isUpperCase(curChar) ? newChar : Character.toLowerCase(newChar));
			} else {
				result.append(curChar);
			}
		}
		return result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		int key = 10;
		// english test
		CeasarCipher cip = new CeasarCipher(LanguageSupport.EN);
		cip.loadKey(key);
		String text = "It's a bad trip";
		System.out.println(cip.encrypt(text));
		// decrypt english
		System.out.println(cip.decrypt("Sd'c k lkn dbsz"));
		// vietnamese test
		cip = new CeasarCipher(LanguageSupport.VI);
		cip.loadKey(key);
		text = "Thắng lợi trong tay tạo";
		System.out.println(cip.encrypt(text));
		// decrypt vietnamese
		System.out.println(cip.decrypt("Ữnậổm ốụo ữừỗổm ữẵẳ ữẩỗ"));
	}
}
