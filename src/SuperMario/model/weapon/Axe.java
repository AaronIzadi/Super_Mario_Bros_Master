package SuperMario.model.weapon;


import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;

import java.awt.image.BufferedImage;

public class Axe extends GameObject {

    private boolean coolDownFinished;
    private boolean isReleased;
    private BufferedImage[] axeStyle;

    public Axe(double x, double y, BufferedImage style, boolean toRight) {
        super(x, y, style);
        setDimension(96, 96);
        setFalling(true);
        setJumping(false);
        setVelX(10);

        if (!toRight) {
            setVelX(-5);
        }
    }

    public boolean isCoolDownFinished() {
        return coolDownFinished;
    }

    public void setCoolDownFinished(boolean coolDownFinished) {
        this.coolDownFinished = coolDownFinished;
    }

    public BufferedImage[] getAxeStyle() {
        return axeStyle;
    }

    public void setAxeStyle(BufferedImage[] axeStyle) {
        this.axeStyle = axeStyle;
    }

    public void setReleased(boolean released) {
        isReleased = released;
    }

    public boolean isReleased() {
        return isReleased;
    }
}
