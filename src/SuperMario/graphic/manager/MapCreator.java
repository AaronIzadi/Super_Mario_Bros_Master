package SuperMario.graphic.manager;

import SuperMario.model.map.Flag;
import SuperMario.model.map.Map;
import SuperMario.model.enemy.Goomba;
import SuperMario.model.enemy.KoopaTroopa;
import SuperMario.model.enemy.Piranha;
import SuperMario.model.enemy.Spiny;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.Mario;
import SuperMario.model.obstacle.*;
import SuperMario.model.prize.*;

import SuperMario.input.ImageLoader;


import java.awt.*;
import java.awt.image.BufferedImage;

public class MapCreator {

    private int heroType;
    private Hero hero;
    private ImageLoader imageLoader;
    private BufferedImage backgroundImage, crossoverBackground;
    private BufferedImage superMushroom, oneHeartUpMushroom, fireFlower, coin;
    private BufferedImage border, ordinaryBrick, surpriseBrick, prizeBrick, slime, slimeOnTouch, oneCoinBrick, fiveCoinBrick, groundBrick, pipe, smallPipe, upSidePipe, hole;
    private BufferedImage goombaLeft, goombaRight, shell, koopaLeft, koopaRight, spinyLeft, spinyRight, piranhaOpen, piranhaClose, superStar, endFlag;


    public MapCreator() {
        this.imageLoader = ImageLoader.getInstance();
        loadImages();
    }

    public MapCreator(Hero hero) {
        this.hero = hero;
        this.imageLoader = ImageLoader.getInstance();
        imageLoader.setHeroType(hero.getType());
        loadImages();
    }

    private void loadImages() {
        this.spinyLeft = imageLoader.getSpinyLeft();
        this.spinyRight = imageLoader.getSpinyRight();
        this.backgroundImage = imageLoader.getBackgroundImage();
        this.crossoverBackground = imageLoader.getCrossoverBackground();
        this.hole = imageLoader.getHole();
        this.piranhaOpen = imageLoader.getPiranhaOpen();
        this.piranhaClose = imageLoader.getPiranhaClose();
        this.smallPipe = imageLoader.getSmallPipe();
        this.border = imageLoader.getBorder();
        this.superMushroom = imageLoader.getSuperMushroom();
        this.oneHeartUpMushroom = imageLoader.getOneHeartUpMushroom();
        this.fireFlower = imageLoader.getFireFlower();
        this.superStar = imageLoader.getSuperStar();
        this.coin = imageLoader.getCoin();
        this.ordinaryBrick = imageLoader.getOrdinaryBrick();
        this.surpriseBrick = imageLoader.getSurpriseBrick();
        this.prizeBrick = imageLoader.getPrizeBrick();
        this.oneCoinBrick = imageLoader.getOneCoinBrick();
        this.fiveCoinBrick = imageLoader.getFiveCoinBrick();
        this.groundBrick = imageLoader.getGroundBrick();
        this.pipe = imageLoader.getPipe();
        this.upSidePipe = imageLoader.getUpSidePipe();
        this.goombaLeft = imageLoader.getGoombaLeft();
        this.goombaRight = imageLoader.getGoombaRight();
        this.koopaLeft = imageLoader.getKoopaLeft();
        this.koopaRight = imageLoader.getKoopaRight();
        this.shell = imageLoader.getShell();
        this.slime = imageLoader.getSlime();
        this.slimeOnTouch = imageLoader.getSlimeOnTouch();
        this.endFlag = imageLoader.getEndFlag();
    }

    public Map createCrossOver(String path, Hero hero) {

        BufferedImage crossoverImage = imageLoader.loadImage(path);
        this.hero = hero;

        if (this.hero != null) {
            updateImageLoader(heroType);
        }

        Map crossover = new Map(hero);
        crossover.setRemainingTime(100);
        crossover.setBackgroundImage(crossoverBackground);
        int pixelMultiplier = 48;

        int heroColor = new Color(160, 160, 160).getRGB();
        int border = new Color(127, 51, 0).getRGB();
        int ordinaryBrick = new Color(0, 0, 255).getRGB();
        int upSidePipe = new Color(180, 255, 180).getRGB();
        int surpriseBrick = new Color(255, 255, 0).getRGB();
        int slime = new Color(100, 255, 100).getRGB();
        int tunnel = new Color(112, 146, 190).getRGB();


        for (int x = 0; x < crossoverImage.getWidth(); x++) {
            for (int y = 0; y < crossoverImage.getHeight(); y++) {

                int currentPixel = crossoverImage.getRGB(x, y);
                int xLocation = x * pixelMultiplier;
                int yLocation = y * pixelMultiplier;

                if (currentPixel == border) {
                    Brick brick = new Border(xLocation, yLocation, this.border);
                    crossover.addBrick(brick);
                } else if (currentPixel == surpriseBrick) {
                    Prize prize = generateRandomPrizeForCrossover(xLocation, yLocation);
                    SurpriseBrick brick = new SurpriseBrick(xLocation, yLocation, this.surpriseBrick, prize);
                    BufferedImage[] frames = new BufferedImage[2];
                    frames[0] = this.surpriseBrick;
                    frames[1] = this.prizeBrick;
                    brick.setFrames(frames);
                    crossover.addBrick(brick);
                } else if (currentPixel == tunnel) {
                    Brick brick = new CrossoverTunnel(xLocation, yLocation, this.pipe);
                    crossover.addGroundBrick(brick);
                } else if (currentPixel == slime) {
                    Slime brick = new Slime(xLocation, yLocation, this.slime);
                    brick.slimeOnTouch(slimeOnTouch);
                    crossover.addBrick(brick);
                } else if (currentPixel == ordinaryBrick) {
                    Brick brick = new OrdinaryBrick(xLocation, yLocation, this.ordinaryBrick);
                    crossover.addBrick(brick);
                } else if (currentPixel == upSidePipe) {
                    Brick brick = new Pipe(xLocation, yLocation, this.upSidePipe);
                    crossover.addBrick(brick);
                } else {
                    setHero(crossover, heroColor, currentPixel, xLocation, yLocation);
                }
            }
        }

        System.out.println("Loading crossover.");
        return crossover;

    }

    private void setHero(Map map, int hero, int currentPixel, int xLocation, int yLocation) {
        if (currentPixel == hero) {
            if (this.hero == null) {
                Hero heroObject = new Mario(xLocation, yLocation);
                map.setHero(heroObject);
            } else {
                this.hero.setX(xLocation);
                this.hero.setY(yLocation);
                setHeroType();
                imageLoader.setHeroType(heroType);
                updateImageLoader(heroType);
                map.setHero(this.hero);
            }
        }
    }

    public Map createMap(String mapPath) {
        BufferedImage mapImage = imageLoader.loadImage(mapPath);

        if (mapImage == null) {
            System.out.println("Given path is invalid...");
            return null;
        }

        if (this.hero != null) {
            updateImageLoader(heroType);
        }

        Map map = new Map();
        map.setRemainingTime(400);
        map.setBackgroundImage(backgroundImage);
        String[] paths = mapPath.split("/");
        map.setPath(paths[paths.length - 1]);

        int pixelMultiplier = 48;

        int hero = new Color(160, 160, 160).getRGB();
        int ordinaryBrick = new Color(0, 0, 255).getRGB();
        int surpriseBrick = new Color(255, 255, 0).getRGB();
        int groundBrick = new Color(255, 0, 0).getRGB();
        int pipe = new Color(0, 255, 0).getRGB();
        int goomba = new Color(0, 255, 255).getRGB();
        int koopa = new Color(255, 0, 255).getRGB();
        int end = new Color(160, 0, 160).getRGB();
        int hole = new Color(200, 191, 231).getRGB();
        int spiny = new Color(128, 255, 128).getRGB();
        int piranha = new Color(200, 124, 124).getRGB();
        int coinBrick = new Color(255, 124, 0).getRGB();
        int multiCoinBrick = new Color(20, 100, 40).getRGB();
        int smallPipe = new Color(34, 177, 76).getRGB();
        int border = new Color(127, 51, 0).getRGB();
        int slime = new Color(100, 255, 100).getRGB();
        int crossover = new Color(112, 146, 190).getRGB();

        for (int x = 0; x < mapImage.getWidth(); x++) {
            for (int y = 0; y < mapImage.getHeight(); y++) {

                int currentPixel = mapImage.getRGB(x, y);
                int xLocation = x * pixelMultiplier;
                int yLocation = y * pixelMultiplier;

                if (currentPixel == ordinaryBrick) {
                    Brick brick = new OrdinaryBrick(xLocation, yLocation, this.ordinaryBrick);
                    map.addBrick(brick);
                } else if (currentPixel == hole) {
                    Hole brick = new Hole(xLocation, yLocation, this.hole);
                    map.addBrick(brick);
                    map.addHoles(brick);
                } else if (currentPixel == border) {
                    Brick brick = new Border(xLocation, yLocation, this.border);
                    map.addBrick(brick);
                } else if (currentPixel == slime) {
                    Slime brick = new Slime(xLocation, yLocation, this.slime);
                    brick.slimeOnTouch(slimeOnTouch);
                    map.addBrick(brick);
                } else if (currentPixel == surpriseBrick) {
                    Prize prize = generateRandomPrize(xLocation, yLocation);
                    SurpriseBrick brick = new SurpriseBrick(xLocation, yLocation, this.surpriseBrick, prize);
                    BufferedImage[] frames = new BufferedImage[2];
                    frames[0] = this.surpriseBrick;
                    frames[1] = this.prizeBrick;
                    brick.setFrames(frames);
                    map.addBrick(brick);
                } else if (currentPixel == coinBrick) {
                    Prize prize = new Coin(xLocation, yLocation, this.coin, 10);
                    Brick brick = new CoinBrick(xLocation, yLocation, this.oneCoinBrick, prize);
                    map.addBrick(brick);
                } else if (currentPixel == multiCoinBrick) {
                    Prize prize = new Coin(xLocation, yLocation, this.coin, 10);
                    Brick brick = new MultiCoinBrick(xLocation, yLocation, this.fiveCoinBrick, prize);
                    map.addBrick(brick);
                } else if (currentPixel == smallPipe) {
                    Brick brick = new SmallPipe(xLocation, yLocation, this.smallPipe);
                    map.addGroundBrick(brick);
                } else if (currentPixel == groundBrick) {
                    Brick brick = new GroundBrick(xLocation, yLocation, this.groundBrick);
                    map.addGroundBrick(brick);
                } else if (currentPixel == goomba) {
                    Goomba enemy = new Goomba(xLocation, yLocation, this.goombaLeft);
                    BufferedImage[] frames = new BufferedImage[2];
                    frames[0] = this.goombaLeft;
                    frames[1] = this.goombaRight;
                    enemy.setFrames(frames);
                    map.addEnemy(enemy);
                } else if (currentPixel == koopa) {
                    KoopaTroopa enemy = new KoopaTroopa(xLocation, yLocation, this.koopaLeft);
                    enemy.setRightImage(koopaRight);
                    enemy.setShell(this.shell);
                    map.addEnemy(enemy);
                } else if (currentPixel == spiny) {
                    Spiny enemy = new Spiny(xLocation, yLocation, this.spinyLeft);
                    enemy.setRightImage(spinyRight);
                    map.addEnemy(enemy);
                } else if (currentPixel == piranha) {
                    Piranha enemy = new Piranha(xLocation, yLocation, this.piranhaClose);
                    BufferedImage[] frames = new BufferedImage[2];
                    frames[0] = this.piranhaClose;
                    frames[1] = this.piranhaOpen;
                    enemy.setFrames(frames);
                    map.addEnemy(enemy);
                } else if (currentPixel == pipe) {
                    Brick brick = new Pipe(xLocation, yLocation, this.pipe);
                    map.addGroundBrick(brick);
                } else if (currentPixel == crossover) {
                    Brick brick = new CrossoverTunnel(xLocation, yLocation, this.pipe);
                    map.addGroundBrick(brick);
                } else if (currentPixel == end) {
                    Flag endPoint = new Flag(xLocation + 24, yLocation, endFlag);
                    map.setEndPoint(endPoint);
                } else setHero(map, hero, currentPixel, xLocation, yLocation);
            }
        }

        System.out.println("Loading map.");
        return map;
    }

    private Prize generateRandomPrize(double x, double y) {
        Prize generated;
        int random = (int) (Math.random() * 12);

        if (random == 0 || random == 1) {
            generated = new SuperMushroom(x, y, this.superMushroom);
        } else if (random == 2 || random == 3) {
            generated = new FireFlower(x, y, this.fireFlower);
        } else if (random == 4) {
            generated = new OneHeartUpMushroom(x, y, this.oneHeartUpMushroom);
        } else if (random == 5) {
            generated = new SuperStar(x, y, this.superStar);
        } else {
            generated = new Coin(x, y, this.coin, 10);
        }
        return generated;
    }

    private Prize generateRandomPrizeForCrossover(double x, double y) {
        Prize generated;
        int random = (int) (Math.random() * 12);

        if (random == 0) {
            generated = new SuperMushroom(x, y, this.superMushroom);
        } else if (random == 1) {
            generated = new FireFlower(x, y, this.fireFlower);
        } else if (random == 2) {
            generated = new OneHeartUpMushroom(x, y, this.oneHeartUpMushroom);
        } else {
            generated = new Coin(x, y, this.coin, 10);
        }
        return generated;
    }

    private void updateImageLoader(int heroType) {
        imageLoader.setHeroType(heroType);
        imageLoader = ImageLoader.getInstance();
    }

    private void setHeroType() {
        this.heroType = this.hero.getType();
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}
