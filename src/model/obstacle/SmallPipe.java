package model.obstacle;

import java.awt.image.BufferedImage;

public class SmallPipe extends Brick{

    public SmallPipe(double x, double y, BufferedImage style) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
        setDimension(96, 48);}
}
