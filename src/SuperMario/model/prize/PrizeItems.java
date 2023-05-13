package SuperMario.model.prize;

import SuperMario.graphic.view.animation.Animation;
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
    private boolean isTouched;

    public PrizeItems(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    public void onTouch(Hero hero, GameEngine engine) {
        isTouched = true;
        hero.acquirePoints(getPoint());
        if (engine.getUserData().getHero() != null) {
            engine.getUserData().getHero().acquirePoints(getPoint());
        }

        ImageLoader imageLoader = ImageLoader.getInstance();

        BufferedImage[] leftFrames;
        BufferedImage[] rightFrames;
        if (!hero.isSuper()) {
            hero.setY(hero.getBottomBounds().getY() - hero.getDimension().getHeight());
            hero.setDimension(48, 96);
        }
        if (!hero.getHeroForm().isSuper()) {
            leftFrames = imageLoader.getLeftFrames(HeroForm.SUPER);
            rightFrames = imageLoader.getRightFrames(HeroForm.SUPER);

            Animation animation = new Animation(leftFrames, rightFrames);
            HeroForm newForm = new HeroForm(animation, true, false, hero.getType());
            hero.setHeroForm(newForm);
        } else {
            leftFrames = imageLoader.getLeftFrames(HeroForm.FIRE);
            rightFrames = imageLoader.getRightFrames(HeroForm.FIRE);

            Animation animation = new Animation(leftFrames, rightFrames);
            HeroForm newForm = new HeroForm(animation, true, true, hero.getType());
            hero.setHeroForm(newForm);
        }

        engine.playPowerUp();
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
