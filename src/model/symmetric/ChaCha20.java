package model.symmetric;

import java.util.ArrayList;
import java.util.List;

public class ChaCha20 extends SymmetricCipher {
    public ChaCha20() {
        this.name = "ChaCha20";
        this.isFixedKeySize = true;
    }

    @Override
    public int getSupportedIvOrNonceSize() {
        return 12;
    }

    @Override
    public List<Integer> getSupportedKeySize() {
        List<Integer> result = new ArrayList<>();
        result.add(256);
        return result;
    }

    @Override
    public List<String> getSupportedMode() {
        List<String> result = new ArrayList<>();
        result.add("NONE");
        return result;
    }

    @Override
    public List<String> getSupportedPadding() {
        List<String> result = new ArrayList<>();
        result.add("NoPadding");
        return result;
    }
}
