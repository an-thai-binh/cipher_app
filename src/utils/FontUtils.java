package utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class FontUtils {
    public static Font createRobotoFont(String type, float size) {
        try {
            switch (type) {
                case "regular": {
                    return Font.createFont(Font.TRUETYPE_FONT, FontUtils.class.getClassLoader().getResourceAsStream("fonts/Roboto-Regular.ttf")).deriveFont(size);
                }
                case "medium": {
                    return Font.createFont(Font.TRUETYPE_FONT, FontUtils.class.getClassLoader().getResourceAsStream("fonts/Roboto-Medium.ttf")).deriveFont(size);
                }
                case "extraBold": {
                    return Font.createFont(Font.TRUETYPE_FONT, FontUtils.class.getClassLoader().getResourceAsStream("fonts/Roboto-Black.ttf")).deriveFont(size);
                }
                default:
                    return new Font("Arial", Font.PLAIN, (int) size);
                }
        } catch (FontFormatException e) {
            return new Font("Arial", Font.PLAIN, (int) size);
        } catch (IOException e) {
            return new Font("Arial", Font.PLAIN, (int) size);
        }

    }
}
