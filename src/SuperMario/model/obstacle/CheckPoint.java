package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CheckPoint extends Brick {
    private Animation animation;
    private boolean checked = false;
    public CheckPoint(double x, double y, BufferedImage style) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(false);
    }
    public Point checked() {
        BufferedImage newStyle = ImageLoader.getInstance().loadImage("/check-point.png");
        setStyle(newStyle);
        checked = true;
        return new Point((int) getX(), (int) getY());
    }

    public void draw(Graphics g) {
        super.draw(g);
        if (!checked){
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

}
