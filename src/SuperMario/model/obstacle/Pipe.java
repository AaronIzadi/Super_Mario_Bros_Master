package SuperMario.model.obstacle;

import java.awt.image.BufferedImage;

public class Pipe extends Obstacle {

    public Pipe(double x, double y, BufferedImage style) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
        setDimension(96, 96);
    }
}
