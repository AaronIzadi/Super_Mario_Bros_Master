package SuperMario.model.prize;

import SuperMario.logic.GameEngine;
import SuperMario.logic.prize.PrizeLogic;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;

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
        PrizeLogic.reveal(this);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        PrizeLogic.onTouch(this, hero, engine);
    }

    @Override
    public void updateLocation() {
        PrizeLogic.update(this);
    }

    @Override
    public void draw(Graphics g) {
        PrizeLogic.draw(this, g);
    }

    public int getRevealBoundary() {
        return revealBoundary;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isAcquired() {
        return acquired;
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
    }
}
