package model.brick;

import java.awt.image.BufferedImage;

public class Hole extends Brick{

    public Hole(double x, double y, BufferedImage style) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
    }
}
