package utils;

public class HillKey {
	private int[][]	key;
	private String keyText;
	public HillKey(int[][] key, String keyText) {
		this.key = key;
		this.keyText = keyText;
	}
	public int[][] getKey() {
		return key;
	}
	public void setKey(int[][] key) {
		this.key = key;
	}
	public String getKeyText() {
		return keyText;
	}
	public void setKeyText(String keyText) {
		this.keyText = keyText;
	}
}