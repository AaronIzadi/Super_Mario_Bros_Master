package SuperMario.model.prize;

import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.HeroForm;


import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PrizeItems extends GameObject implements Prize {
    private boolean revealed = false;
    private int point;

    public PrizeItems(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    public void onTouch(Hero hero, GameEngine engine) {

        hero.acquirePoints(getPoint());
        if (engine.getUserData().getHero() != null) {
            engine.getUserData().getHero().acquirePoints(getPoint());
        }

        if (!hero.isSuper()) {
            hero.setY(hero.getBottomBounds().getY() - hero.getDimension().getHeight());
            hero.setDimension(48, 96);
        }
        if (!hero.getHeroForm().isSuper()) {
            setHeroForm(hero, HeroForm.SUPER);
        } else {
            setHeroForm(hero, HeroForm.FIRE);
        }

        engine.playPowerUp();
    }

    private void setHeroForm(Hero hero, int heroFormType) {

        HeroForm newForm = null;
        ImageLoader imageLoader = ImageLoader.getInstance();
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames(heroFormType);
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames(heroFormType);

        if (heroFormType == HeroForm.SUPER) {
            newForm = new HeroForm(leftFrames, rightFrames, true, false, hero.getType());
        } else if (heroFormType == HeroForm.FIRE) {
            newForm = new HeroForm(leftFrames, rightFrames, true, true, hero.getType());
        }

        hero.setHeroForm(newForm);
    }

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
