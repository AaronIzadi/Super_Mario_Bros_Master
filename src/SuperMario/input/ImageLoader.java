package SuperMario.input;


import SuperMario.model.hero.HeroType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    private BufferedImage heroForms;
    private final BufferedImage brickAnimation;
    private final BufferedImage backgroundImage , crossoverBackground;
    private final BufferedImage superMushroom, oneHeartUpMushroom, fireFlower, coin;
    private final BufferedImage border, ordinaryBrick, surpriseBrick, revealedPrizeBrick, revealedCheckPoint, prizeBrick, oneCoinBrick, fiveCoinBrick, slime, slimeOnTouch, groundBrick, pipe, smallPipe, upSidePipe, hole;
    private final BufferedImage goombaLeft, goombaRight, shell, koopaLeft, koopaRight, spinyLeft, spinyRight, piranhaOpen, piranhaClose, superStar, endFlag;
    private final BufferedImage startScreenImage, aboutScreenImage, helpScreenImage, checkPoint, gameOverScreen, storeScreenImage, loadGameScreen, pauseScreen;
    private final BufferedImage heartIcon;
    private final BufferedImage coinIcon;
    private final BufferedImage axe0, axe1, axe2, axe3;
    private final BufferedImage selectIcon;
    private final BufferedImage icon;
    private static final ImageLoader instance = new ImageLoader();

    private ImageLoader() {
        this.startScreenImage = loadImage("/states/start-screen.png");
        this.helpScreenImage = loadImage("/states/help-screen.png");
        this.aboutScreenImage = loadImage("/states/about-screen.png");
        this.gameOverScreen = loadImage("/states/game-over.png");
        this.storeScreenImage = loadImage("/states/store-screen.png");
        this.loadGameScreen = loadImage("/states/load-screen.png");
        this.pauseScreen = loadImage("/states/pause-screen.png");
        this.checkPoint = loadImage("/states/check point.png");
        this.revealedCheckPoint = loadImage("/check-point.png");
        BufferedImage sprite = loadImage("/sprite.png");
        this.axe0 = loadImage("/axe0.png");
        this.axe1 = loadImage("/axe1.png");
        this.axe2 = loadImage("/axe2.png");
        this.axe3 = loadImage("/axe3.png");
        this.upSidePipe = loadImage("/upside-pipe.png");
        this.crossoverBackground = loadImage("/crossover-background.png");
        this.brickAnimation = loadImage("/brick-animation.png");
        this.spinyLeft = loadImage("/spiny-left.png");
        this.spinyRight = loadImage("/spiny-right.png");
        this.backgroundImage = loadImage("/background.png");
        this.hole = loadImage("/hole.png");
        this.piranhaOpen = loadImage("/piranha-open.png");
        this.piranhaClose = loadImage("/piranha-close.png");
        this.smallPipe = loadImage("/pipe-small.png");
        this.border = loadImage("/border-brick.png");
        this.heartIcon = loadImage("/heart-icon.png");
        this.selectIcon = loadImage("/select-icon.png");
        this.prizeBrick = loadImage("/prize.png");
        this.shell = loadImage("/shell.png");
        this.slime = loadImage("/slime.png");
        this.slimeOnTouch = loadImage("/slime-on touch.png");
        this.icon = loadImage("/icon.jpg");
        this.coinIcon = getSubImage(sprite, 1, 5, 48, 48);
        this.superMushroom = getSubImage(sprite, 2, 5, 48, 48);
        this.oneHeartUpMushroom = getSubImage(sprite, 3, 5, 48, 48);
        this.fireFlower = getSubImage(sprite, 4, 5, 48, 48);
        this.superStar = getSubImage(sprite, 5, 5, 48, 48);
        this.coin = getSubImage(sprite, 1, 5, 48, 48);
        this.ordinaryBrick = getSubImage(sprite, 1, 1, 48, 48);
        this.surpriseBrick = getSubImage(sprite, 2, 1, 48, 48);
        this.oneCoinBrick = getSubImage(sprite, 1, 1, 48, 48);
        this.fiveCoinBrick = getSubImage(sprite, 1, 1, 48, 48);
        this.groundBrick = getSubImage(sprite, 2, 2, 48, 48);
        this.pipe = getSubImage(sprite, 3, 1, 96, 96);
        this.goombaLeft = getSubImage(sprite, 2, 4, 48, 48);
        this.goombaRight = getSubImage(sprite, 5, 4, 48, 48);
        this.koopaLeft = getSubImage(sprite, 1, 3, 48, 64);
        this.koopaRight = getSubImage(sprite, 4, 3, 48, 64);
        this.revealedPrizeBrick = getSubImage(sprite, 1, 2, 48, 48);
        this.endFlag = getSubImage(sprite, 5, 1, 48, 48);
    }

    public static ImageLoader getInstance() {
        return instance;
    }

    public BufferedImage loadImage(String path) {
        BufferedImage imageToReturn = null;

        try {
            imageToReturn = ImageIO.read(new File("src/SuperMario/resources/media" + path));
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
        if ((col == 1 || col == 4) && row == 3) {
            return image.getSubimage((col - 1) * 48, 128, w, h);
        }
        return image.getSubimage((col - 1) * 48, (row - 1) * 48, w, h);
    }

    public BufferedImage[] getHeroLeftFrames(int heroForm) {

        BufferedImage[] leftFrames;
        leftFrames = new BufferedImage[6];


        int col = 0, width = 0, height = 0;

        if (heroForm == 0) {
            col = 1;
            width = 52;
            height = 48;
        } else if (heroForm == 1) {
            col = 4;
            width = 48;
            height = 96;
        } else if (heroForm == 2) {
            col = 7;
            width = 48;
            height = 96;
        }

        return getHeroForms(leftFrames, heroForm, col, width, height);
    }

    public BufferedImage[] getHeroRightFrames(int heroForm) {

        BufferedImage[] rightFrames;
        rightFrames = new BufferedImage[6];

        int col = 0, width = 0, height = 0;

        if (heroForm == 0) {
            col = 2;
            width = 52;
            height = 48;
        } else if (heroForm == 1) {
            col = 5;
            width = 48;
            height = 96;
        } else if (heroForm == 2) {
            col = 8;
            width = 48;
            height = 96;
        }

        return getHeroForms(rightFrames, heroForm, col, width, height);
    }

    private BufferedImage[] getHeroForms(BufferedImage[] rightFrames, int heroForm, int col, int width, int height) {
        for (int i = 0; i < 6; i++) {
            if (i < 5) {
                rightFrames[i] = heroForms.getSubimage((col - 1) * width, (i) * height, width, height);
            }
            if (i == 5) {
                rightFrames[i] = heroForms.getSubimage((col - 1) * width, (i) * height, width, 48);
            }
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

    public BufferedImage getFireballImage() {
        return getSubImage(loadImage("/sprite.png"), 3, 4, 24, 24);
    }


    public void setHeroType(int heroType) {
        setHeroForms(heroType);
    }

    private void setHeroForms(int heroType) {
        switch (heroType) {
            case HeroType.LUIGI:
                heroForms = loadImage("/hero/luigi-forms.png");
                break;
            case HeroType.ROSS:
                heroForms = loadImage("/hero/ross-forms.png");
                break;
            case HeroType.TOAD:
                heroForms = loadImage("/hero/toad-forms.png");
                break;
            case HeroType.PRINCE_PEACH:
                heroForms = loadImage("/hero/prince peach-forms.png");
                break;
            default:
                heroForms = loadImage("/hero/mario-forms.png");
                break;
        }
    }


    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public BufferedImage getSuperMushroom() {
        return superMushroom;
    }

    public BufferedImage getOneHeartUpMushroom() {
        return oneHeartUpMushroom;
    }

    public BufferedImage getFireFlower() {
        return fireFlower;
    }

    public BufferedImage getCoin() {
        return coin;
    }

    public BufferedImage getBorder() {
        return border;
    }

    public BufferedImage getOrdinaryBrick() {
        return ordinaryBrick;
    }

    public BufferedImage getSurpriseBrick() {
        return surpriseBrick;
    }

    public BufferedImage getOneCoinBrick() {
        return oneCoinBrick;
    }

    public BufferedImage getFiveCoinBrick() {
        return fiveCoinBrick;
    }

    public BufferedImage getGroundBrick() {
        return groundBrick;
    }

    public BufferedImage getPipe() {
        return pipe;
    }

    public BufferedImage getSmallPipe() {
        return smallPipe;
    }

    public BufferedImage getHole() {
        return hole;
    }

    public BufferedImage getGoombaLeft() {
        return goombaLeft;
    }

    public BufferedImage getGoombaRight() {
        return goombaRight;
    }

    public BufferedImage getKoopaLeft() {
        return koopaLeft;
    }

    public BufferedImage getKoopaRight() {
        return koopaRight;
    }

    public BufferedImage getSpinyLeft() {
        return spinyLeft;
    }

    public BufferedImage getSpinyRight() {
        return spinyRight;
    }

    public BufferedImage getPiranhaOpen() {
        return piranhaOpen;
    }

    public BufferedImage getPiranhaClose() {
        return piranhaClose;
    }

    public BufferedImage getSuperStar() {
        return superStar;
    }

    public BufferedImage getEndFlag() {
        return endFlag;
    }

    public BufferedImage getStartScreenImage() {
        return startScreenImage;
    }

    public BufferedImage getAboutScreenImage() {
        return aboutScreenImage;
    }

    public BufferedImage getHelpScreenImage() {
        return helpScreenImage;
    }

    public BufferedImage getGameOverScreen() {
        return gameOverScreen;
    }

    public BufferedImage getStoreScreenImage() {
        return storeScreenImage;
    }

    public BufferedImage getLoadGameScreen() {
        return loadGameScreen;
    }

    public BufferedImage getPauseScreen() {
        return pauseScreen;
    }

    public BufferedImage getHeartIcon() {
        return heartIcon;
    }

    public BufferedImage getCoinIcon() {
        return coinIcon;
    }

    public BufferedImage getSelectIcon() {
        return selectIcon;
    }

    public BufferedImage getPrizeBrick() {
        return prizeBrick;
    }

    public Image getIcon() {
        return icon;
    }

    public BufferedImage[] axeFrames() {
        BufferedImage[] axeFrames = new BufferedImage[4];
        axeFrames[0] = axe0;
        axeFrames[1] = axe1;
        axeFrames[2] = axe2;
        axeFrames[3] = axe3;

        return axeFrames;
    }

    public BufferedImage getAxeUpRight() {
        return axe0;
    }

    public BufferedImage getAxeUpLeft() {
        return axe3;
    }

    public BufferedImage getShell() {
        return shell;
    }

    public BufferedImage getSlime() {
        return slime;
    }

    public BufferedImage getSlimeOnTouch() {
        return slimeOnTouch;
    }

    public BufferedImage getUpSidePipe() {
        return upSidePipe;
    }

    public BufferedImage getCrossoverBackground() {
        return crossoverBackground;
    }

    public BufferedImage getCheckPoint() {
        return checkPoint;
    }

    public BufferedImage getRevealedPrizeBrick() {
        return revealedPrizeBrick;
    }

    public BufferedImage getRevealedCheckPoint() {
        return revealedCheckPoint;
    }
}
