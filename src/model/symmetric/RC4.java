package model.symmetric;

import java.util.ArrayList;
import java.util.List;

public class RC4 extends SymmetricCipher {
    public RC4() {
        this.name = "RC4";
        this.isFixedKeySize = false;
    }

    @Override
    public int getSupportedIvOrNonceSize() {
        return 0;
    }

    @Override
    public List<Integer> getSupportedKeySize() {
        List<Integer> result = new ArrayList<>();
        result.add(40);
        result.add(1024);
        return result;
    }

    @Override
    public List<String> getSupportedMode() {
        List<String> result = new ArrayList<>();
        result.add("");
        return result;
    }

    @Override
    public List<String> getSupportedPadding() {
        List<String> result = new ArrayList<>();
        result.add("");
        return result;
    }
}
