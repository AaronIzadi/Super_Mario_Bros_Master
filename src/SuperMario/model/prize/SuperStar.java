package SuperMario.model.prize;

import java.awt.image.BufferedImage;

public class SuperStar extends PrizeItems {

    private int jumpDelayTicks;

    public SuperStar(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(40);
        setVelX(2);
    }

    public int getJumpDelayTicks() {
        return jumpDelayTicks;
    }

    public void setJumpDelayTicks(int jumpDelayTicks) {
        this.jumpDelayTicks = jumpDelayTicks;
    }
}
