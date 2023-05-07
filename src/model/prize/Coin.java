package model.prize;

import graphic.manager.GameEngine;
import model.GameObject;
import model.hero.Hero;
import model.hero.HeroType;

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
            if (engine.getHero() != null) {
                engine.getHero().acquirePoints(point);
            }
            if (hero.getType() == HeroType.TOAD) {
                hero.acquireCoinForToad();
                engine.getHero().acquireCoinForToad();
            } else {
                hero.acquireCoin();
                if (engine.getHero() != null) {
                    engine.getHero().acquireCoin();
                }
            }
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
