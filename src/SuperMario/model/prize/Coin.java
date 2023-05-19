package SuperMario.model.prize;

import SuperMario.logic.GameEngine;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.HeroType;


import java.awt.*;
import java.awt.image.BufferedImage;


public class Coin extends GameObject implements Prize {

    private final int point;
    private boolean revealed, acquired = false;
    private final int revealBoundary;

    public Coin(double x, double y, BufferedImage style, int point) {
        super(x, y, style);
        this.point = point;
        revealed = false;
        setDimension(30, 42);
        revealBoundary = (int) getY() - getDimension().height;
    }

    @Override
    public int getPoint() {
        return point;
    }

    @Override
    public void reveal() {
        revealed = true;
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        if (!acquired) {
            acquired = true;
            hero.acquirePoints(point);
            hero.acquireCoin();
            engine.playCoin();
        }
    }

    @Override
    public void updateLocation() {
        if (revealed) {
            setY(getY() - 5);
        }
    }

    @Override
    public void draw(Graphics g) {
        if (revealed) {
            g.drawImage(getStyle(), (int) getX(), (int) getY(), null);
        }
    }

    public int getRevealBoundary() {
        return revealBoundary;
    }
}
