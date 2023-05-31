package SuperMario.model.obstacle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Slime extends Brick {
    private BufferedImage slimeOnTouch;
    private boolean onTouch;

    public Slime(double x, double y, BufferedImage style) {
        super(x + 4, y, style);
        setBreakable(true);
        setEmpty(true);
    }

    @Override
    public void draw(Graphics g) {
        if (onTouch) {
            g.drawImage(slimeOnTouch, (int) getX() - 4, (int) getY(), null);
        } else {
            g.drawImage(getStyle(), (int) getX(), (int) getY(), null);
        }
    }

    public void setOnTouch(boolean onTouch) {
        this.onTouch = onTouch;
        setTimerToReStyle();
    }

    public void slimeOnTouch(BufferedImage slimeOnTouch) {
        this.slimeOnTouch = slimeOnTouch;
    }

    public void setTimerToReStyle() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                onTouch = false;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 500);
    }
}
