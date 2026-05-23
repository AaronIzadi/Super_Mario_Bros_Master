package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;

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
        this.checked = checked;
        isRevealed = true;
        BufferedImage newStyle;

        if (checked) {
            newStyle = ImageLoader.getInstance().getRevealedCheckPoint();
        } else {
            newStyle = ImageLoader.getInstance().getRevealedPrizeBrick();

        }

        setStyle(newStyle);
        setEmpty(true);

        return new Point((int) getX(), (int) getY());
    }

    public void draw(Graphics g) {
        super.draw(g);
        if (!checked && !isEmpty()) {
            animate();
        }
    }

    public void animate() {
        boolean isAnimationTicked = animation.animate(5);
        if (isAnimationTicked) {
            setStyle(animation.getCurrentFrame());
        }
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
