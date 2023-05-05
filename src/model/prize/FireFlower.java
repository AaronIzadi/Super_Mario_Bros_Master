package model.prize;

import graphic.manager.GameEngine;
import model.hero.Hero;
import model.hero.HeroForm;
import graphic.view.Animation;
import graphic.view.ImageLoader;

import java.awt.image.BufferedImage;

public class FireFlower extends PrizeItems {

    public FireFlower(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(150);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        hero.acquirePoints(getPoint());
        if (engine.getHero() != null) {
            engine.getHero().acquirePoints(getPoint());
        }

        ImageLoader imageLoader = new ImageLoader(hero.getType());

        if (!hero.getHeroForm().ifCanShootFire()) {
            BufferedImage[] leftFrames = imageLoader.getLeftFrames(HeroForm.FIRE);
            BufferedImage[] rightFrames = imageLoader.getRightFrames(HeroForm.FIRE);

            Animation animation = new Animation(leftFrames, rightFrames);
            HeroForm newForm = new HeroForm(animation, true, true, hero.getType());
            hero.setHeroForm(newForm);
            if (engine.getHero() != null) {
                engine.getHero().setHeroForm(newForm);
            }
            hero.setDimension(48, 96);
            if (engine.getHero() != null) {
                engine.getHero().setDimension(48, 96);
            }

            engine.playFireFlower();
        }
    }

    @Override
    public void updateLocation() {
    }

}
