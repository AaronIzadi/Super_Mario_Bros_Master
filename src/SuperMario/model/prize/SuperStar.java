package SuperMario.model.prize;

import java.awt.image.BufferedImage;

public class SuperStar extends PrizeItems {

    private boolean isJumpTimerActivated = false;

    public SuperStar(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(40);
        setVelX(2);
    }

    public boolean isJumpTimerActivated() {
        return isJumpTimerActivated;
    }

    public void setJumpTimerActivated(boolean jumpTimerActivated) {
        isJumpTimerActivated = jumpTimerActivated;
    }
}
