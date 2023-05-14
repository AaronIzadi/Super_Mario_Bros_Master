package SuperMario.graphic.view.animation;

import java.awt.image.BufferedImage;

public class NewAnimation {

    private int index = 0, count = 0;
    private final BufferedImage[] frames;
    private BufferedImage currentFrame;

    private final int length;

    public NewAnimation(BufferedImage... frames) {
        this.frames = frames;
        this.currentFrame = frames[0];
        this.length = frames.length;
    }

    public boolean animate(int speed) {
        count += speed;
        if (count > 60) {
            nextFrame();
            count = 0;
            return true;
        }
        return false;
    }

    public BufferedImage getCurrentFrame() {
        return currentFrame;
    }

    public int getLength() {
        return length;
    }
    private void nextFrame() {
        index = (index + 1) % length;
        currentFrame = frames[index];
    }
}
