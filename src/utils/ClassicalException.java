package utils;

public class ClassicalException {
    public static final String CEASAR_INVALID_KEY = "Key phải có kiểu dữ liệu Integer và nằm trong [1; n)\nvới n là chiều dài bảng chữ cái.";
    public static final String SUBSTITUTION_INVALID_KEY = "Key phải có kiểu dữ liệu String và có độ dài bằng với bảng chữ cái.";
    public static final String AFFINE_INVALID_KEY = "Key phải là đối tượng AffineKey gồm 2 số a, b. Trong đó a thuộc (0, n), b thuộc [0, n) và GCD(a, n) = 1 - n là chiều dài bảng chữ cái.";
    public static final String VIGENERE_INVALID_KEY = "Key phải có kiểu dữ liệu String, có độ dài từ 1 đến n (n là chiều dài bảng chữ cái) và không chứa khoảng trắng.";
    public static final String HILL_INVALID_ORDER = "Mã hóa Hill của chương trình chỉ hỗ trợ ma trận 2x2 hoặc 3x3.";
    public static final String HILL_INVALID_KEY = "Key phải có kiểu dữ liệu String hoặc int[n][n] với n = 2 hoặc 3 và phải là một ma trận khả nghịch.";
    public static final String HILL_INVALID_MATRIX_MULTIPLY = "Ma trận không đủ điều kiện để thực phép nhân hai ma trận.";
}
