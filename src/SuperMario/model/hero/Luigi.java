package SuperMario.model.hero;

public class Luigi extends Hero {

    public Luigi(double x, double y) {
        super(x, y);
        setType(HeroType.LUIGI);
    }

    public Luigi(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.LUIGI);
    }
}
