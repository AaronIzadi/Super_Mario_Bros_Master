package SuperMario.model.hero;

public class Rosalina extends Hero {

    public Rosalina(double x, double y) {
        super(x, y);
        setType(HeroType.ROSALINA);
    }

    public Rosalina(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.ROSALINA);
    }
}
