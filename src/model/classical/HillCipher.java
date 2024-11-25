package model.classical;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import utils.Array2DUtils;
import utils.ClassicalException;
import utils.HillKey;
import utils.LanguageSupport;

public class HillCipher implements IClassicalCipher  {
	private int order;
	private int[][] key;
	private String alphabet;
	public HillCipher(int languageCode, int order) throws Exception {
		setOrder(order);
		this.key = new int[0][0];
		setAlphabet(languageCode);
	}

	/**
	 * getName	lấy ra tên của thuật toán mã hóa
	 * @return	String
	 */
	@Override
	public String getName() {
		return "Hill";
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
	 * setOrder	cài cấp của ma trận vuông
	 * @param order	cấp ma trận
	 * @throws Exception	cấp ma trận không hợp lệ
	 */
	@Override
	public void setOrder(int order) throws Exception {
		if(order != 2 && order != 3) {
			throw new Exception(ClassicalException.HILL_INVALID_ORDER);
		}
		this.order = order;
	}

	/**
	 * genKey	tạo key ngẫu nhiên
	 * key là một ma trận vuông cấp 2 hoặc cấp 2 và là ma trận khả nghịch
	 * @return	Object (HillKey - gồm 2 thuộc tính là key ở dạng ma trận và key ở dạng text)
	 */
	@Override
	public Object genKey() {
		key = new int[order][order];
		Random rd = new Random();
		String keyText = "";
		for(int i = 0; i < key.length; i++) {
			for(int j = 0; j < key[i].length; j++) {
				key[i][j] = rd.nextInt(alphabet.length());
				keyText += alphabet.charAt(key[i][j]);
			}
		}
		// kiểm tra xem ma trận có khả nghịch không
		int detK = determinant(key);
		if(detK == 0 || gcd(detK, alphabet.length()) != 1) {
			return genKey();
		}
		HillKey result = new HillKey(key, keyText);
		return result;
	}
	
	/**
	 * loadKey	gắn key thủ công
	 * @param o	String hoặc int[][]
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public void loadKey(Object o) throws Exception {
		int[][] key;
		if(o instanceof String) {	// key dạng String => chuyển sang dạng ma trận
			String keyText = (String) o;
			if(keyText.length() == 4) {
				key = new int[2][2];
			} else if(keyText.length() == 9) {
				key = new int[3][3];
			} else {
				throw new Exception(ClassicalException.HILL_INVALID_KEY);
			}
			int pos = 0;
			for(int i = 0; i < key.length; i++) {
				for(int j = 0; j < key[i].length; j++) {
					key[i][j] = alphabet.indexOf(Character.toUpperCase(keyText.charAt(pos)));
					pos++;
				}
			}
		} else if(o instanceof int[][]) {	//	key dạng ma trận
			key = (int[][]) o;
			// kiểm tra key có phải ma trận bậc 2 hoặc bậc 3 không
			if(!((key.length == 2 && key[0].length == 2) || (key.length == 3 && key[0].length == 3))) {
				throw new Exception(ClassicalException.HILL_INVALID_KEY);
			}
		} else {
			throw new Exception(ClassicalException.HILL_INVALID_KEY);
		}
		// kiểm tra key có phải ma trận khả nghịch không
		int detK = determinant(key);
		if(detK == 0 || gcd(detK, alphabet.length()) != 1) {
			throw new Exception(ClassicalException.HILL_INVALID_KEY);
		}
		this.key = key;
	}

	/**
	 * encrypt	mã hóa chuỗi với phương pháp mã hóa hill
	 * @param text	chuỗi đầu vào
	 * @return	chuỗi được mã hóa
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String encrypt(String text) throws Exception {
		if(!((key.length == 2 && key[0].length == 2) || (key.length == 3 && key[0].length == 3))) {
			throw new Exception(ClassicalException.HILL_INVALID_KEY);
		}
		String plainText = "";
		HashMap<Integer, Character> specialChars = new HashMap<>();
		// loai bo nhung ky tu dac biet, luu ky tu dac biet cung vi tri trong map
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(Character.isLetter(c)) {
				plainText += c;
			} else {
				specialChars.put(i, c);
			}
		}
		// them nhung ky tu gia (Y) vao plainText de dam bao chia het cho so bac cua ma tran vuong (2 hoac 3)
		while(plainText.length() % key.length != 0) {
			plainText += '\0';
		}
		// encrypt
		StringBuilder result = new StringBuilder();
		char[] plainChars = new char[key.length];	// chuoi n ky tu chuan bi ma hoa 
		int[][] plainMatrix = new int[key.length][1];	// chuoi n ky tu o dang ma tran so (nx1)
		int[][] encryptMatrix;	// chuoi ma hoa o dang ma tran so (nx1)
		for(int i = 0; i < plainText.length(); i += key.length) {
			// lay n ky tu chuyen sang dang ma tran so (nx1)
			for(int j = 0; j < plainMatrix.length; j++) {
				plainMatrix[j][0] = alphabet.indexOf(Character.toUpperCase(plainText.charAt(i + j)));
				plainChars[j] = plainText.charAt(i + j);
			}
			// nhan ma tran key voi ma tran tren tao thanh ma tran ma hoa
			encryptMatrix = multiplyMatrixHill(key, plainMatrix);
			// them cac ky tu trong ma tran ma hoa vao ket qua
			for(int n = 0; n < encryptMatrix.length; n++) {
				if(Character.isUpperCase(plainChars[n]))
					result.append(alphabet.charAt(encryptMatrix[n][0]));
				else
					result.append(Character.toLowerCase(alphabet.charAt(encryptMatrix[n][0])));
			}
		}
		// them lai cac ky tu dac biet vao chuoi ban dau
		for(Entry<Integer, Character> entry : specialChars.entrySet()) {
			result.insert(entry.getKey(), entry.getValue().toString());
		}
		return result.toString();	// substring dung de loai bo cac ky tu gia duoc them vao truoc do
	}

	/**
	 * decrypt	giải mã chuỗi được mã hóa bằng phương pháp hill
	 * @param text	chuỗi đầu vào
	 * @return	String	chuỗi được giải mã
	 * @throws Exception	key không hợp lệ
	 */
	@Override
	public String decrypt(String text) throws Exception {
		int[][] inverseKey = inverseMatrixHillCipher(this.key);	// tim ma tran nghich dao cua key ban dau
		if(!((inverseKey.length == 2 && inverseKey[0].length == 2) || (inverseKey.length == 3 && inverseKey[0].length == 3))) {
			throw new Exception(ClassicalException.HILL_INVALID_KEY);
		}
		String cypherText = "";
		HashMap<Integer, Character> specialChars = new HashMap<>();
		// loai bo nhung ky tu dac biet, luu ky tu dac biet cung vi tri trong map
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(Character.isLetter(c)) {
				cypherText += c;
			} else {
				specialChars.put(i, c);
			}
		}
		// them nhung ky tu gia (Y) vao plainText de dam bao chia het cho so bac cua ma tran vuong (2 hoac 3)
		while(cypherText.length() % inverseKey.length != 0) {
			cypherText += '\0';
		}
		// decrypt
		StringBuilder result = new StringBuilder();
		char[] plainChars = new char[inverseKey.length];	// chuoi n ky tu chuan bi giai ma
		int[][] plainMatrix = new int[inverseKey.length][1];	// chuoi n ky tu o dang ma tran so (nx1)
		int[][] decryptMatrix;	// chuoi giai ma o dang ma tran so (nx1)
		for(int i = 0; i < cypherText.length(); i += inverseKey.length) {
			// lay n ky tu chuyen sang dang ma tran so (nx1)
			for(int j = 0; j < plainMatrix.length; j++) {
				plainMatrix[j][0] = alphabet.indexOf(Character.toUpperCase(cypherText.charAt(i + j)));
				plainChars[j] = cypherText.charAt(i + j);
			}
			// nhan ma tran key voi ma tran tren tao thanh ma tran ma hoa
			decryptMatrix = multiplyMatrixHill(inverseKey, plainMatrix);
			// them cac ky tu trong ma tran ma hoa vao ket qua
			for(int n = 0; n < decryptMatrix.length; n++) {
				if(Character.isUpperCase(plainChars[n]))
					result.append(alphabet.charAt(decryptMatrix[n][0]));
				else
					result.append(Character.toLowerCase(alphabet.charAt(decryptMatrix[n][0])));
			}
		}
		// them lai cac ky tu dac biet vao chuoi ban dau
		for(Entry<Integer, Character> entry : specialChars.entrySet()) {
			result.insert(entry.getKey(), entry.getValue().toString());
		}
		return result.toString();	// substring dung de loai bo cac ky tu gia duoc them vao truoc do
	}
	
	/**
	 * multiplyMatrixHill	nhân ma trận text với ma trận key
	 * @param keyMatrix	ma trận key (nxn)
	 * @param plainMatrix	ma trận text (nx1)
	 * @return	int[][]	ma trận kết quả (nx1)
	 * @throws Exception	ma tran khong du dieu kien de thuc hien phep nhan
	 */
	private int[][] multiplyMatrixHill(int[][] keyMatrix, int[][] plainMatrix) throws Exception {
		if(keyMatrix[0].length != plainMatrix.length) {
			throw new Exception(ClassicalException.HILL_INVALID_MATRIX_MULTIPLY);
		}
		int[][] result = new int[keyMatrix.length][plainMatrix[0].length];
		for(int a = 0; a < keyMatrix.length; a++) {	// duyet dong ma tran key
			for(int b = 0; b < plainMatrix[0].length; b++) {	// duyet cot ma tran text
				for(int c = 0; c < keyMatrix[a].length; c++) {
					result[a][b] += keyMatrix[a][c] * plainMatrix[c][b];
				}
				result[a][b] %= alphabet.length();
				if(result[a][b] < 0) result[a][b] += alphabet.length();
			}
		}
		return result;
	}
	
	
	/**
	 * inverseMatrixHill	tính ma trận nghịch đảo trong mã hóa Hill
	 * @param keyMatrix	ma trận key
	 * @return	int[][]	ma trận nghịch đảo của ma trận key
	 * @throws Exception 
	 */
	private int[][] inverseMatrixHillCipher(int[][] keyMatrix) throws Exception {
		// K^-1 = det(K)^-1 * adj(K) % alphabet.length
		int detK = determinant(keyMatrix);	// tinh det(K)
		int inverseDetK = extendedEuclidean(detK, alphabet.length());	// tinh det(K) ^ -1
		int[][] adjK = adjugateMatrix(keyMatrix);	// tinh adj(K)
		int[][] inverseK = new int[keyMatrix.length][keyMatrix.length];	// tinh K^-1
		for(int i = 0; i < adjK.length; i++) {
			for (int j = 0; j < adjK[i].length; j++) {
				inverseK[i][j] = (inverseDetK * adjK[i][j]) % alphabet.length();
				if (inverseK[i][j] < 0) {
					inverseK[i][j] += alphabet.length();
				}
			}
		}
		return inverseK;
	}
	
	/**
	 * derterminant	tính định thức của ma trận
	 * @param matrix	ma trận 2x2 hoặc 3x3
	 * @return	int
	 */
	private int determinant(int[][] matrix) {
		if(matrix.length == 2) {	// 2x2
			// det(K) = ad - bc
			return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];	// ad - bc
		} else {	// 3x3
			int result = 0;	
			int[][] subMatrix = new int[2][2];
			for(int n = 0; n < matrix[0].length; n++) {
				for(int i = 1; i < matrix.length; i++) {
					int subCol = 0;
					for(int j = 0; j < matrix[i].length; j++) {
						if(j != n) {
							subMatrix[i - 1][subCol] = matrix[i][j];
							subCol++;
						}
					}
				}
				result += (n != 1) ? matrix[0][n] * determinant(subMatrix) : -matrix[0][n] * determinant(subMatrix);
			}
			return result;
		}
	}
	
	/**
	 * adjugateMatrix	tính ma trận phụ hợp của ma trận cho trước
	 * @param matrix	ma trận 2x2 hoặc 3x3
	 * @return	int[][]
	 */
	private int[][] adjugateMatrix(int[][] matrix) {
		if(matrix.length == 2) {	// 2x2
			int[][] result = {{matrix[1][1], -matrix[0][1]}, {-matrix[1][0], matrix[0][0]}};
			return result; 
		} else {	// 3x3
			int[][]	cofMatrix = new int[3][3];
			int[][] subMatrix = new int[2][2];
			int subRow;
			int subCol;
			// tìm ma trận phần bù đại số
			for(int a = 0; a < matrix.length; a++) {
				for(int b = 0; b < matrix.length; b++) {
					subRow = 0;
					for(int i = 0; i < matrix.length; i++) {
						if(i != a) {
							subCol = 0;
							for(int j = 0; j < matrix.length; j++) {
								if(j != b) {
									subMatrix[subRow][subCol] = matrix[i][j];
									subCol++;
								}
							}
							subRow++;
						}
					}
					cofMatrix[a][b] = subMatrix[0][0] * subMatrix[1][1] - subMatrix[0][1] * subMatrix[1][0];
				}
			}
			// áp dụng quy tắc Laplace vào ma trận phần bù đại số
			cofMatrix[0][1] = -cofMatrix[0][1];
			cofMatrix[1][0] = -cofMatrix[1][0];
			cofMatrix[1][2] = -cofMatrix[1][2];
			cofMatrix[2][1] = -cofMatrix[2][1];
			// chuyển vị ma trận đại số thành ma trận phụ hợp
			int[][] adjMatrix = new int[3][3];
			for(int i = 0; i < cofMatrix.length; i++) {
				for(int j = 0; j < cofMatrix[i].length; j++) {
					adjMatrix[j][i] = cofMatrix[i][j];
				}
			}
			return adjMatrix;
		}
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
	
//	public static void main(String[] args) throws Exception {
//		HillCipher cip = new HillCipher(LanguageSupport.EN, 2);
////		cip.loadKey("lidh");
//		int[][]	 m = {{3, 3}, {2, 5}};
//		cip.loadKey(m);
//		System.out.println(cip.encrypt("dhnonglam"));
////		int[][] m = {{6, 24, 1}, {13, 16, 10}, {20, 17, 15}};
//
//
////		int[][]	m2 = cip.inverseMatrixHillCipher(m);
////		for(int i = 0; i < m2.length; i++) {
////			for(int j = 0; j < m2[i].length; j++) {
////				System.out.print(m2[i][j] + "\t");
////			}
////			System.out.println();
////		}
//		System.out.println(cip.decrypt("epdsfehwht"));
//	}
}
