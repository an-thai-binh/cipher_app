package utils;

public class Array2DUtils {
    /**
     * toString chuyển mảng 2 chiều sang dạng chuỗi
     * @param arr   mảng đầu vào
     * @return  String
     */
    public static String toString(int[][] arr) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                result.append(arr[i][j]);
                if(j < arr[i].length - 1) {
                    result.append("\t");
                }
            }
            if(i < arr.length - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    /**
     * toInt2DArray chuyển chuỗi sang mảng số nguyên 2 chiều
     * @param text  chuỗi mảng đầu vào
     * VD:  1   2   3
     *      4   5   6
     *      10  11  12
     * @return  int[][]
     */
    public static int[][] toInt2DArray(String text) {
        String[] rows = text.split("\n");   // xác định số dòng
        int colCount = rows[1].split("\t").length;  // xác định số cột
        int[][] result = new int[rows.length][colCount];
        for(int i = 0; i < result.length; i++) {
            String[] cols = rows[i].split("\t");
            for(int j = 0; j < cols.length; j++) {
                result[i][j] = Integer.parseInt(cols[j]);
            }
        }
        return result;
    }
}
