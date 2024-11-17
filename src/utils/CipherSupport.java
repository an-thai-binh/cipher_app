package utils;

public class CipherSupport {
    public final static String[] CLASSICAL_CIPHERS = new String[]{"Dịch chuyển", "Thay thế", "Affine", "Vigenere", "Hill"};
    public final static String[] SYMMETRIC_CIPHERS = new String[]{"AES", "DES", "TripleDES", "Blowfish", "ChaCha20", "RC2", "RC4"};
    public final static String[] SYMMETRIC_CIPHERS_THIRD_PARTY = new String[]{"SEED", "Camellia", "Serpent"};
    public final static String[] ASYMMETRIC_CIPHERS = new String[]{"RSA"};
    public final static String[] HASH_ALGORITHMS = new String[]{"MD2", "MD5", "SHA"};
    public final static String[] HASH_ALGORITHMS_THIRD_PARTY = new String[]{"Tiger", "Blake2s", "Whirlpool"};

    /**
     * isContains	kiểm tra giải thuật này có hỗ trợ không
     * @param ciphers	danh sách giải thuật hỗ trợ
     * @param cipherName	giải thuật cần kiểm tra
     * @return boolean
     */
    public static boolean isContains(String[] ciphers, String cipherName) {
        for(String cipher: ciphers) {
            if(cipherName.equals(cipher)) {
                return true;
            }
        }
        return false;
    }
}
