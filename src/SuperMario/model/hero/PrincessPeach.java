package SuperMario.model.hero;

public class PrincessPeach extends Hero {

    public PrincessPeach(double x, double y) {
        super(x, y);
        setType(HeroType.PRINCESS_PEACH);
    }

    public PrincessPeach(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.PRINCESS_PEACH);
    }
}
