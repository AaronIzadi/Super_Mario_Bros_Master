package SuperMario.model.prize;

import SuperMario.logic.GameEngine;
import SuperMario.logic.prize.PrizeLogic;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PrizeItems extends GameObject implements Prize {
    private boolean revealed = false;
    private int point;

    public PrizeItems(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        PrizeLogic.onTouch(this, hero, engine);
    }

    @Override
    public int getPoint() {
        return point;
    }

    @Override
    public void updateLocation() {
        PrizeLogic.update(this);
    }

    @Override
    public void draw(Graphics g) {
        PrizeLogic.draw(this, g);
    }

    @Override
    public void reveal() {
        PrizeLogic.reveal(this);
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
}
