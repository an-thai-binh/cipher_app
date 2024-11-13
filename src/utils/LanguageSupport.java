package utils;

public class LanguageSupport {
    /**
     * EN	tieng Anh
     */
    public static final int EN = 1;
    /**
     * VI	tieng Viet
     */
    public static final int VI = 2;
    public static String setAlphabet(int languageCode) throws Exception {
        String alphabet = "";
        switch(languageCode) {
            case EN: {
                alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                break;
            }
            case VI: {
                alphabet = "AÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬBCDĐEÉÈẺẼẸÊẾỀỂỄỆGHIÍÌỈĨỊKLMNOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢPQRSTÚÙỦŨỤƯỨỪỬỮỰVXY";
                break;
            }
            default: {
                throw new Exception("Unsupported language code");
            }
        }
        return alphabet;
    }
}
