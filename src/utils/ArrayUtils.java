package utils;

public class ArrayUtils {
    public static String[] concatenate(String[] arr1, String[] arr2) {
        String[] result = new String[arr1.length + arr2.length];
        int count = 0;
        for(String s: arr1) {
            result[count] = s;
            count++;
        }
        for(String s: arr2) {
            result[count] = s;
            count++;
        }
        return result;
    }
}
