package SuperMario.model.obstacle;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Hole extends Obstacle{

    public Hole(double x, double y, BufferedImage style) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }
}
