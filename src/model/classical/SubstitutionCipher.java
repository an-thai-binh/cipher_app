package model.classical;

import utils.ClassicalException;
import utils.LanguageSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SubstitutionCipher implements IClassicalCipher {
	private String key;
	private String alphabet;
	public SubstitutionCipher(int languageCode) throws Exception {
		key = "";
		setAlphabet(languageCode);
	}

	/**
	 * getName	lấy ra tên của thuật toán mã hóa
	 * @return	String
	 */
	@Override
	public String getName() {
		return "Thay thế";
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
	 * độ dài key = độ dài alphabet
	 * @return	Object	(String)
	 */
	@Override
	public Object genKey() {
		StringBuilder result = new StringBuilder();
		List<Character> selectedChars = new ArrayList<Character>();
		Random rd = new Random();
		char c;
		int index;
		while(result.length() < alphabet.length()) {
			index = rd.nextInt(alphabet.length());
			c = alphabet.charAt(index);
			if(!selectedChars.contains(c)) {
				result.append(c);
				selectedChars.add(c);
			}
		}
		key = result.toString();
		return key;
	}
	
	/**
	 * loadKey	gắn key thủ công
	 * @param o	String
	 */
	@Override
	public void loadKey(Object o) throws Exception {
		if(!(o instanceof String)) {
			throw new Exception(ClassicalException.SUBSTITUTION_INVALID_KEY);
		}
		String key = (String) o;
		if(key.length() != alphabet.length()) {
			throw new Exception(ClassicalException.SUBSTITUTION_INVALID_KEY);
		}
		this.key = key;
	}
	
	/**
	 * encrypt	mã hóa chuỗi với phương pháp mã hóa thay thế
	 * @param text	chuỗi đầu vào
	 * @return	chuỗi được mã hóa
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String encrypt(String text) throws Exception {
		if(key.length() != alphabet.length()) 
			throw new Exception(ClassicalException.SUBSTITUTION_INVALID_KEY);
		StringBuilder result = new StringBuilder();
		int pos;
		char curChar;
		char newChar;
		for(int i = 0; i < text.length(); i++) {
			curChar = text.charAt(i);
			if(Character.isLetter(curChar)) {
				pos = alphabet.indexOf(Character.toUpperCase(curChar));
				newChar = key.charAt(pos);
				result.append(Character.isUpperCase(curChar) ? newChar : Character.toLowerCase(newChar));
			} else {
				result.append(curChar);
			}
		}
		return result.toString();
	}
	
	/**
	 * decrypt	giải mã chuỗi được mã hóa bằng phương pháp thay thế
	 * @param text	chuỗi đầu vào
	 * @return	String	chuỗi được giải mã
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String decrypt(String text) throws Exception {
		if(key.length() != alphabet.length())
			throw new Exception(ClassicalException.SUBSTITUTION_INVALID_KEY);
		StringBuilder result = new StringBuilder();
		int pos;
		char curChar;
		char newChar;
		for(int i = 0; i < text.length(); i++) {
			curChar = text.charAt(i);
			if(Character.isLetter(curChar)) {
				pos = key.indexOf(Character.toUpperCase(curChar));
				newChar = alphabet.charAt(pos);
				result.append(Character.isUpperCase(curChar) ? newChar : Character.toLowerCase(newChar));
			} else {
				result.append(curChar);
			}
		}
		return result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		// english test
		SubstitutionCipher cip = new SubstitutionCipher(LanguageSupport.EN);
		String generatedKey = (String) cip.genKey();
		System.out.println("Alphabet: " + cip.alphabet);
		System.out.println("Generated key: " + generatedKey);
		String encryptedText = cip.encrypt("HAPPY NEW YEAR");
		System.out.println(encryptedText);
		System.out.println(cip.decrypt(encryptedText));
		// vietnamese test
		System.out.println("---");
		cip = new SubstitutionCipher(LanguageSupport.VI);
		generatedKey = (String) cip.genKey();
		System.out.println(cip.alphabet);
		System.out.println(generatedKey);
		encryptedText = cip.encrypt("Xin chào mọi người");
		System.out.println(encryptedText);
		System.out.println(cip.decrypt(encryptedText));
	}
}
