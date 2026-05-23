package SuperMario.model.hero;

public class Toad extends Hero {

    public Toad(double x, double y) {
        super(x, y);
        setType(HeroType.TOAD);
    }

    public Toad(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.TOAD);
        setRemainingLives(5);
    }
}
