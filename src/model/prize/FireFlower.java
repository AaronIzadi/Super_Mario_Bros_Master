package model.prize;

import graphic.manager.GameEngine;
import model.hero.Hero;

import java.awt.image.BufferedImage;

public class FireFlower extends PrizeItems {

    public FireFlower(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(20);
    }

    @Override
    public void updateLocation() { }

}
