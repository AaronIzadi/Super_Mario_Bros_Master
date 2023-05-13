package SuperMario.input;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

public class FontLoader {

    private Font font;

    public FontLoader() {

        try {
            File input = new File("src/SuperMario/resources/font/mario-font.ttf");
            InputStream targetStream = Files.newInputStream(input.toPath());
            font = Font.createFont(Font.TRUETYPE_FONT, targetStream);
        } catch (FontFormatException | IOException e) {
            font = new Font("Verdana", Font.PLAIN, 12);
            e.printStackTrace();
        }
    }

    public Font getFont() {
        return font;
    }
}
