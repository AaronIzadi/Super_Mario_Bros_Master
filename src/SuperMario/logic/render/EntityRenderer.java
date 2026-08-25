package SuperMario.logic.render;

import SuperMario.model.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class EntityRenderer {

    private EntityRenderer() {
    }

    public static void drawSprite(GameObject object, Graphics g) {
        BufferedImage style = object.getStyle();

        if (style != null) {
            g.drawImage(style, (int) object.getX(), (int) object.getY(), null);
        }
    }
}
