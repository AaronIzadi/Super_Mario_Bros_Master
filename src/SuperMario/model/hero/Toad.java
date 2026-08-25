package SuperMario.model.hero;

import SuperMario.logic.hero.HeroLogic;

public class Toad extends Hero {

    public Toad(double x, double y) {
        super(x, y);
        setType(HeroType.TOAD);
        HeroLogic.applyCharacterTraits(this);
    }

    public Toad(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.TOAD);
        HeroLogic.applyCharacterTraits(this);
    }
}
