package model.prize;

import graphic.manager.GameEngine;
import graphic.view.Animation;
import graphic.view.ImageLoader;
import model.GameObject;
import model.hero.Hero;
import model.hero.HeroForm;

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

    public void onTouch(Hero hero, GameEngine engine){
        isTouched = true;
        hero.acquirePoints(getPoint());
        if (engine.getHero() != null) {
            engine.getHero().acquirePoints(getPoint());
        }

        ImageLoader imageLoader = new ImageLoader();

        BufferedImage[] leftFrames;
        BufferedImage[] rightFrames;
        if (!hero.getHeroForm().isSuper()) {
            leftFrames = imageLoader.getLeftFrames(HeroForm.SUPER);
            rightFrames = imageLoader.getRightFrames(HeroForm.SUPER);

            Animation animation = new Animation(leftFrames, rightFrames);
            HeroForm newForm = new HeroForm(animation, true, false, hero.getType());
            hero.setHeroForm(newForm);
            hero.setDimension(48, 96);
            if (engine.getHero() != null) {
                engine.getHero().setHeroForm(newForm);
                engine.getHero().setDimension(48, 96);
            }
        }
        else{
            leftFrames = imageLoader.getLeftFrames(HeroForm.FIRE);
            rightFrames = imageLoader.getRightFrames(HeroForm.FIRE);

            Animation animation = new Animation(leftFrames, rightFrames);
            HeroForm newForm = new HeroForm(animation, true, true, hero.getType());
            hero.setHeroForm(newForm);
            hero.setDimension(48, 96);
            if (engine.getHero() != null) {
                engine.getHero().setHeroForm(newForm);
                engine.getHero().setDimension(48, 96);
            }
        }
        engine.playSuperMushroom();
    };

    @Override
    public int getPoint() {
        return point;
    }

    @Override
    public void updateLocation(){
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


    public boolean isTouched() {
        return isTouched;
    }
}
