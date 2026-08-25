package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.brick.BrickLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CheckPoint extends Brick {
    private Animation animation;
    private boolean checked;
    private boolean isRevealed;

    public CheckPoint(double x, double y, BufferedImage style) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(false);
        isRevealed = false;
    }

    public Point check(boolean checked) {
        return BrickLogic.check(this, checked);
    }

    @Override
    public void draw(Graphics g) {
        BrickLogic.draw(this, g);
    }

    public void animate() {
        BrickLogic.animate(this);
    }

    public void setFrames(BufferedImage[] frames) {
        setAnimation(new Animation(frames));
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Animation getAnimation() {
        return animation;
    }
}
