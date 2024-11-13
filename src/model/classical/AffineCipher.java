package model.classical;

import java.util.Random;

import utils.AffineKey;
import utils.ClassicalException;
import utils.LanguageSupport;

public class AffineCipher implements IClassicalCipher {
	private int a;
	private int b;
	private String alphabet;
	public AffineCipher(int languageCode) throws Exception {
		a = 0;
		b = 0;
		setAlphabet(languageCode);
	}

	/**
	 * getName	lấy ra tên của thuật toán mã hóa
	 * @return	String
	 */
	@Override
	public String getName() {
		return "Affine";
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
	 * gcd	tính ước chung lớn nhất của 2 số nguyên
	 * @param x	số x
	 * @param y	số y
	 * @return	int
	 */
	private int gcd(int x, int y) {
		if(y == 0)
			return x;
		else {
			return gcd(y, x % y);
		}
	}

	/**
	 * extendedEuclidean	thuật toán Euclidean mở rộng giúp tìm nghịch đảo của a mod m
	 * công thức:
	 * 1. r(i) = r(i-2) % r(i-1)
	 * 2. q(i) = r(i-2) / r(i-1)
	 * 3. x(i) = x(i-2) - qi * x(i-1)
	 * @param a số a
	 * @param m	số m
	 * @return	int
	 * @throws Exception	GCD(a, m) != 1
	 */
	private int extendedEuclidean(int a, int m) throws Exception {
		if(gcd(a, m) != 1) {
			throw new Exception("GCD(a, m) must equal to 1");
		}
		int baseMod = m;
		int x1 = 0;	// khởi tạo x(i-2) = 0
		int x2 = 1;	// khởi tạo x(i-1) = 1
		int r, q;
		int temp;
		while(true) {
			r = m % a;	// tính r(i) = r(i-2) % r(i-1)
			q = m / a;	// tính q(i) = r(i-2) / r(i-1)
			if(r == 0) {	// nếu r = 0 kết thúc vòng lặp
				break;
			}
			temp = x2;
			x2 = x1 - q * x2;	// tính x(i) = x(i-2) - qi * x(i-1) xong gán x(i-1) = x(i)
			x1 = temp;	//	gán x(i-2) = x(i-1)
			m = a;	// gán r(i-2) = r(i-1)
			a = r;	// gán r(i-1) = r(i)
		}
		return (x2 < 0 ? x2 + baseMod : x2);	// nếu x2 < 0 thì + cho mod ban đầu
	}

	/**
	 * genKey	tạo key ngẫu nhiên
	 * 1 <= a < m (chiều dài alphabet)
	 * 0 <= b < m
	 * gcd(a, m) = 1
	 * @return	Object (AffineKey)
	 */
	@Override
	public Object genKey() {
		Random rd = new Random();
		do {
			a = rd.nextInt(alphabet.length() - 1) + 1;
		} while(gcd(a, alphabet.length()) !=  1);
		b = rd.nextInt(alphabet.length());
		AffineKey key = new AffineKey(a, b);
		return key;
	}
	
	/**
	 * loadKey	gắn key thủ công
	 * @param o	AffineKey
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public void loadKey(Object o) throws Exception {
		int a = 0;
		int b = 0;
		if(o instanceof AffineKey) {
			AffineKey key = (AffineKey) o;
			a = key.getA();
			b = key.getB();
		} else if(o instanceof String) {
			String[] num = o.toString().split("-");
			a = Integer.parseInt(num[0]);
			b = Integer.parseInt(num[1]);
		}
		if(a <= 0 || a >= alphabet.length() || b < 0 || b >= alphabet.length() || gcd(a, alphabet.length()) != 1) {
			throw new Exception(ClassicalException.AFFINE_INVALID_KEY);
		}
		this.a = a;
		this.b = b;
	}

	/**
	 * encrypt	mã hóa chuỗi với phương pháp mã hóa affine
	 * @param text	chuỗi đầu vào
	 * @return	chuỗi được mã hóa
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String encrypt(String text) throws Exception {
		if(a <= 0 || a >= alphabet.length() || b < 0 || b >= alphabet.length() || gcd(a, alphabet.length()) != 1) {
			throw new Exception(ClassicalException.AFFINE_INVALID_KEY);
		}
		StringBuilder result = new StringBuilder();
		int curPos;
		int newPos;
		char curChar;
		char newChar;
		for(int i = 0; i < text.length(); i++) {
			curChar = text.charAt(i);
			if(Character.isLetter(curChar)) {
				curPos = alphabet.indexOf(Character.toUpperCase(curChar));
				newPos = (a * curPos + b) % alphabet.length();	// (ax + b) % n
				newChar = alphabet.charAt(newPos);
				result.append(Character.isUpperCase(curChar) ? newChar : Character.toLowerCase(newChar));
			} else {
				result.append(curChar);
			}
		}
		return result.toString();
	}

	/**
	 * decrypt	giải mã chuỗi được mã hóa bằng phương pháp affine
	 * @param text	chuỗi đầu vào
	 * @return	String	chuỗi được giải mã
	 * @throws Exception	key không hợp lệ
	 */
	public String decrypt(String text) throws Exception {
		if(a <= 0 || a >= alphabet.length() || b < 0 || b >= alphabet.length() || gcd(a, alphabet.length()) != 1) {
			throw new Exception(ClassicalException.AFFINE_INVALID_KEY);
		}
		StringBuilder result = new StringBuilder();
		int curPos;
		int newPos;
		int aInverse;
		char curChar;
		char newChar;
		for(int i = 0; i < text.length(); i++) {
			curChar = text.charAt(i);
			if(Character.isLetter(curChar)) {
				curPos = alphabet.indexOf(Character.toUpperCase(curChar));
				aInverse = extendedEuclidean(a, alphabet.length());
				newPos = (aInverse * (curPos - b)) % alphabet.length();	// (a^-1(x - b)) % n
				if(newPos < 0) newPos += alphabet.length();
				newChar = alphabet.charAt(newPos);
				result.append(Character.isUpperCase(curChar) ? newChar : Character.toLowerCase(newChar));
			} else {
				result.append(curChar);
			}
		}
		return result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		AffineCipher cip = new AffineCipher(LanguageSupport.VI);
		cip.loadKey(new AffineKey(7, 3));
		System.out.println(cip.encrypt("KHOA CONG NGHE THONG TIN"));
		System.out.println(cip.decrypt("ÒYỪẢ ỐỪÚỨ ÚỨYƯ PYỪÚỨ PĂÚ"));
	}
}
