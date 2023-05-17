package SuperMario.model.obstacle;

import SuperMario.model.hero.Hero;

import java.awt.image.BufferedImage;

public class CrossoverTunnel extends Pipe{

    public CrossoverTunnel(double x, double y, BufferedImage style) {
        super(x, y, style);
    }

    public boolean onTouchHero(Hero hero){
        hero.setVelY(-5);
        return hero.getY() == 600;
    }
}
