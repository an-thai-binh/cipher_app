package model.symmetric;

import java.util.ArrayList;
import java.util.List;

public class TripleDES extends SymmetricCipher {
    public TripleDES() {
        this.name = "DESede";
        this.isFixedKeySize = true;
    }

    @Override
    public int getSupportedIvOrNonceSize() {
        return 8;
    }

    @Override
    public List<Integer> getSupportedKeySize() {
        List<Integer> result = new ArrayList<>();
        result.add(112);
        result.add(168);
        return result;
    }

    @Override
    public List<String> getSupportedMode() {
        List<String> result = new ArrayList<>();
        result.add("ECB");
        result.add("CBC");
        result.add("PCBC");
        result.add("CFB");
        result.add("OFB");
        result.add("CTR");
        return result;
    }

    @Override
    public List<String> getSupportedPadding() {
        List<String> result = new ArrayList<>();
        result.add("PKCS5Padding");
        result.add("ISO10126Padding");
        return result;
    }
}
