package model.prize;

import graphic.manager.GameEngine;
import model.GameObj;
import model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PrizeItems extends GameObj implements Prize {

    private boolean revealed = false;
    private int point;

    public PrizeItems(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    public abstract void onTouch(Hero hero, GameEngine engine);

    @Override
    public int getPoint() {
        return point;
    }

    @Override
    public void updateLocation() {
        if (revealed) {
            super.updateLocation();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (revealed) {
            g.drawImage(getStyle(), (int) getX(), (int) getY(), null);
        }
    }

    @Override
    public void reveal() {
        setY(getY() - 48);
        revealed = true;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
