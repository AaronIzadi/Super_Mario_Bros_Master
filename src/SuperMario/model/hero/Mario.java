package SuperMario.model.hero;

public class Mario extends Hero {

    public Mario(double x, double y) {
        super(x, y);
        setType(HeroType.MARIO);
    }

    public Mario(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.MARIO);
    }
}
