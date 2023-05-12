package graphic.manager;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

    private Font font;

    public FontLoader(){
        try {
            InputStream input = getClass().getResourceAsStream("/graphic/media/font/mario-font.ttf");
            assert input != null;
            font = Font.createFont(Font.TRUETYPE_FONT, input);
        } catch (FontFormatException | IOException e) {
            font = new Font("Verdana", Font.PLAIN, 12);
            e.printStackTrace();
        }
    }

    public Font getFont() {
        return font;
    }
}
