package model.classical;

import utils.ClassicalException;
import utils.LanguageSupport;

import java.util.Random;

public class VigenereCipher implements IClassicalCipher {
	private String key;
	private String alphabet;
	public VigenereCipher(int languageCode) throws Exception {
		key = "";
		setAlphabet(languageCode);
	}

	/**
	 * getName	lấy ra tên của thuật toán mã hóa
	 * @return	String
	 */
	@Override
	public String getName() {
		return "Vigenere";
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
	 * key là 1 chuỗi có dài chiều m ngẫu nhiên (m thuộc [1; n]), không chứa khoảng trắng
	 * @return	Object	(String)
	 */
	@Override
	public Object genKey() {
		StringBuilder key = new StringBuilder();
		Random rd = new Random();
		int m = rd.nextInt(alphabet.length()) + 1;	// [1; n]
		int pos;
		for(int i = 0; i < m; i++) {
			pos = rd.nextInt(alphabet.length());
			key.append(alphabet.charAt(pos));
		}
		this.key = key.toString();
		return this.key;
	}
	
	/**
	 * loadKey	gắn key thủ công
	 * @param o	String
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public void loadKey(Object o) throws Exception {
		if(!(o instanceof String)) {
			throw new Exception(ClassicalException.VIGENERE_INVALID_KEY);
		}
		String key = (String) o;
		if(key.length() == 0 || key.contains(" ")) {
			throw new Exception(ClassicalException.VIGENERE_INVALID_KEY);
		}
		this.key = key;
	}

	/**
	 * encrypt	mã hóa chuỗi với phương pháp mã hóa vigenere
	 * @param text	chuỗi đầu vào
	 * @return	chuỗi được mã hóa
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String encrypt(String text) throws Exception {
		if(key.length() == 0 || key.contains(" ")) {
			throw new Exception(ClassicalException.VIGENERE_INVALID_KEY);
		}
		int curPos;
		int keyPos;
		int newPos;
		char curChar;
		char newChar;
		StringBuilder result = new StringBuilder();
		for(int i = 0, j = 0; i < text.length(); i++, j++) {	// i là vị trí trong chuỗi đầu vào, j là vị trí trong chuỗi key
			curChar = text.charAt(i);
			if(Character.isLetter(curChar)) {
				curPos = alphabet.indexOf(Character.toUpperCase(curChar));
				keyPos = alphabet.indexOf(Character.toUpperCase(key.charAt(j % key.length())));
				newPos = (curPos + keyPos) % alphabet.length();	// (x + k) % n
				newChar = alphabet.charAt(newPos);
				result.append(Character.isUpperCase(curChar) ? newChar : Character.toLowerCase(newChar));
			} else {
				result.append(curChar);
				j--;	// không tăng j nếu curChar không phải chữ cái
			}
		}
		return result.toString();
	}

	/**
	 * decrypt	giải mã chuỗi được mã hóa bằng phương pháp vigenere
	 * @param text	chuỗi đầu vào
	 * @return	String	chuỗi được giải mã
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String decrypt(String text) throws Exception {
		if(key.length() == 0 || key.contains(" ")) {
			throw new Exception(ClassicalException.VIGENERE_INVALID_KEY);
		}
		int curPos;
		int keyPos;
		int newPos;
		char curChar;
		char newChar;
		StringBuilder result = new StringBuilder();
		for(int i = 0, j = 0; i < text.length(); i++, j++) {	// i là vị trí trong chuỗi đầu vào, j là vị trí trong chuỗi key
			curChar = text.charAt(i);
			if(Character.isLetter(curChar)) {
				curPos = alphabet.indexOf(Character.toUpperCase(curChar));
				keyPos = alphabet.indexOf(Character.toUpperCase(key.charAt(j % key.length())));
				newPos = (curPos - keyPos + alphabet.length()) % alphabet.length();	// (x - k + n) % n
				newChar = alphabet.charAt(newPos);
				result.append(Character.isUpperCase(curChar) ? newChar : Character.toLowerCase(newChar));
			} else {
				result.append(curChar);
				j--;	// không tăng j nếu curChar không phải chữ cái
			}
		}
		return result.toString();
	}

	public static void main(String[] args) throws Exception {
		VigenereCipher cip = new VigenereCipher(LanguageSupport.VI);
		cip.loadKey("cipher");
//		System.out.println(cip.generateRepeatKey("ONEONEONE", cip.key));
		System.out.println(cip.encrypt("hello"));
		System.out.println(cip.decrypt("ồơèữt"));
	}
}
