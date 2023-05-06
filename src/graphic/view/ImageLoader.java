package graphic.view;

import model.hero.HeroType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ImageLoader {

    private BufferedImage heroForms;
    private BufferedImage brickAnimation;
    private final String marioFormsPath = "/mario-forms.png";
    private final String luigiFormsPath = "/luigi-forms.png";
    private final String rossFormsPath = "/ross-forms.png";
    private final String toadFormsPath = "/toad-forms.png";
    private final String PrincePeachFormsPath = "/prince peach-forms.png";


    public ImageLoader(int heroType) {

        switch (heroType) {
            case HeroType.LUIGI:
                heroForms = loadImage(luigiFormsPath);
                break;
            case HeroType.ROSS:
                heroForms = loadImage(rossFormsPath);
                break;
            case HeroType.TOAD:
                heroForms = loadImage(toadFormsPath);
                break;
            case HeroType.PRINCE_PEACH:
                heroForms = loadImage(PrincePeachFormsPath);
                break;
            default:
                heroForms = loadImage(marioFormsPath);
                break;
        }

        brickAnimation = loadImage("/brick-animation.png");
    }

    public BufferedImage loadImage(String path) {
        BufferedImage imageToReturn = null;

        try {
            imageToReturn = ImageIO.read(Objects.requireNonNull(getClass().getResource("/graphic/media" + path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageToReturn;
    }

    public BufferedImage loadImage(File file) {
        BufferedImage imageToReturn = null;

        try {
            imageToReturn = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageToReturn;
    }

    public BufferedImage getSubImage(BufferedImage image, int col, int row, int w, int h) {
        if ((col == 1 || col == 4) && row == 3) { //koopa
            return image.getSubimage((col - 1) * 48, 128, w, h);
        }
        return image.getSubimage((col - 1) * 48, (row - 1) * 48, w, h);
    }

    public BufferedImage[] getLeftFrames(int marioForm) {
        BufferedImage[] leftFrames = new BufferedImage[5];
        int col = 1;
        int width = 52, height = 48;

        if (marioForm == 1) {
            col = 4;
            width = 48;
            height = 96;
        } else if (marioForm == 2) {
            col = 7;
            width = 48;
            height = 96;
        }

        for (int i = 0; i < 5; i++) {
            leftFrames[i] = heroForms.getSubimage((col - 1) * width, (i) * height, width, height);
        }
        return leftFrames;
    }

    public BufferedImage[] getRightFrames(int marioForm) {
        BufferedImage[] rightFrames = new BufferedImage[5];
        int col = 2;
        int width = 52, height = 48;

        if (marioForm == 1) {
            col = 5;
            width = 48;
            height = 96;
        } else if (marioForm == 2) {
            col = 8;
            width = 48;
            height = 96;
        }

        for (int i = 0; i < 5; i++) {
            rightFrames[i] = heroForms.getSubimage((col - 1) * width, (i) * height, width, height);
        }
        return rightFrames;
    }

    public BufferedImage[] getBrickFrames() {
        BufferedImage[] frames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = brickAnimation.getSubimage(i * 105, 0, 105, 105);
        }
        return frames;
    }

    public BufferedImage getFireballImage(int type) {
        return getSubImage(loadImage("/sprite.png"), 3, 4, 24, 24);
    }

}
