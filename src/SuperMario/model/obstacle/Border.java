package SuperMario.model.obstacle;

import java.awt.image.BufferedImage;

public class Border extends Obstacle{

    public Border(double x, double y, BufferedImage style) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
        setDimension(48, 48);
    }
}
