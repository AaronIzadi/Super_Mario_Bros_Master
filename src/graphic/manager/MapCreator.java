package graphic.manager;

import model.Flag;
import model.brick.*;
import model.enemy.Piranha;
import model.enemy.Spiny;
import model.prize.*;
import graphic.view.ImageLoader;
import model.Map;
import model.enemy.Goomba;
import model.enemy.KoopaTroopa;
import model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

class MapCreator {

    private final ImageLoader imageLoader;
    private final BufferedImage backgroundImage;
    private final BufferedImage superMushroom, oneHeartUpMushroom, fireFlower, coin;
    private final BufferedImage ordinaryBrick, surpriseBrick, oneCoinBrick, fiveCoinBrick, groundBrick, pipe, hole;
    private final BufferedImage goombaLeft, goombaRight, koopaLeft, koopaRight, spinyLeft, spinyRight, piranha, superStar, endFlag;


    MapCreator(ImageLoader imageLoader) {

        this.imageLoader = imageLoader;
        BufferedImage sprite = imageLoader.loadImage("/sprite.png");
        this.spinyLeft = imageLoader.loadImage("/spiny-left.png");
        this.spinyRight = imageLoader.loadImage("/spiny-right.png");
        this.backgroundImage = imageLoader.loadImage("/background.png");
        this.hole = imageLoader.loadImage("/hole.png");
        this.piranha = imageLoader.loadImage("/piranha.png");
        this.superMushroom = imageLoader.getSubImage(sprite, 2, 5, 48, 48);
        this.oneHeartUpMushroom = imageLoader.getSubImage(sprite, 3, 5, 48, 48);
        this.fireFlower = imageLoader.getSubImage(sprite, 4, 5, 48, 48);
        this.superStar = imageLoader.getSubImage(sprite, 5, 5, 48, 48);
        this.coin = imageLoader.getSubImage(sprite, 1, 5, 48, 48);
        this.ordinaryBrick = imageLoader.getSubImage(sprite, 1, 1, 48, 48);
        this.surpriseBrick = imageLoader.getSubImage(sprite, 2, 1, 48, 48);
        this.oneCoinBrick = imageLoader.getSubImage(sprite, 1, 1, 48, 48);
        this.fiveCoinBrick = imageLoader.getSubImage(sprite, 1, 1, 48, 48);
        this.groundBrick = imageLoader.getSubImage(sprite, 2, 2, 48, 48);
        this.pipe = imageLoader.getSubImage(sprite, 3, 1, 96, 96);
        this.goombaLeft = imageLoader.getSubImage(sprite, 2, 4, 48, 48);
        this.goombaRight = imageLoader.getSubImage(sprite, 5, 4, 48, 48);
        this.koopaLeft = imageLoader.getSubImage(sprite, 1, 3, 48, 64);
        this.koopaRight = imageLoader.getSubImage(sprite, 4, 3, 48, 64);
        this.endFlag = imageLoader.getSubImage(sprite, 5, 1, 48, 48);

    }

    Map createMap(String mapPath) {
        BufferedImage mapImage = imageLoader.loadImage(mapPath);

        if (mapImage == null) {
            System.out.println("Given path is invalid...");
            return null;
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
        int oneCoinBrick = new Color(255, 124, 0).getRGB();
        int fiveCoinBrick = new Color(20, 100, 40).getRGB();

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
                } else if (currentPixel == surpriseBrick) {
                    Prize prize = generateRandomPrize(xLocation, yLocation);
                    Brick brick = new SurpriseBrick(xLocation, yLocation, this.surpriseBrick, prize);
                    map.addBrick(brick);
                } else if (currentPixel == oneCoinBrick) {
                    Prize prize = new Coin(xLocation, yLocation, this.coin, 10);
                    Brick brick = new OneCoinBrick(xLocation, yLocation, this.oneCoinBrick, prize);
                    map.addBrick(brick);
                } else if (currentPixel == fiveCoinBrick) {
                    Prize prize = new Coin(xLocation, yLocation, this.coin, 10);
                    Brick brick = new FiveCoinBrick(xLocation, yLocation, this.fiveCoinBrick, prize);
                    map.addBrick(brick);
                } else if (currentPixel == pipe) {
                    Brick brick = new Pipe(xLocation, yLocation, this.pipe);
                    map.addGroundBrick(brick);
                } else if (currentPixel == groundBrick) {
                    Brick brick = new GroundBrick(xLocation, yLocation, this.groundBrick);
                    map.addGroundBrick(brick);
                } else if (currentPixel == goomba) {
                    Goomba enemy = new Goomba(xLocation, yLocation, this.goombaLeft);
                    enemy.setRightImage(goombaRight);
                    map.addEnemy(enemy);
                } else if (currentPixel == koopa) {
                    KoopaTroopa enemy = new KoopaTroopa(xLocation, yLocation, this.koopaLeft);
                    enemy.setRightImage(koopaRight);
                    map.addEnemy(enemy);
                } else if (currentPixel == spiny) {
                    Spiny enemy = new Spiny(xLocation, yLocation, this.spinyLeft);
                    enemy.setRightImage(spinyRight);
                    map.addEnemy(enemy);
                } else if (currentPixel == piranha) {
                    Piranha enemy = new Piranha(xLocation, yLocation, this.piranha);
                    enemy.setRightImage(this.piranha);
                    map.addEnemy(enemy);
                } else if (currentPixel == hero) {
                    Hero heroObject = new Hero(xLocation, yLocation);
                    map.setHero(heroObject);
                } else if (currentPixel == end) {
                    Flag endPoint = new Flag(xLocation + 24, yLocation, endFlag);
                    map.setEndPoint(endPoint);
                }
            }
        }

        System.out.println("Loading map.");
        return map;
    }

    private Prize generateRandomPrize(double x, double y) {
        Prize generated;
        int random = (int) (Math.random() * 12);

        if (random == 0) { //super mushroom
            generated = new SuperMushroom(x, y, this.superMushroom);
        } else if (random == 1) { //fire flower
            generated = new FireFlower(x, y, this.fireFlower);
        } else if (random == 2) { //one heart up mushroom
            generated = new OneHeartUpMushroom(x, y, this.oneHeartUpMushroom);
        } else if (random == 3) { //superstar
            generated = new SuperStar(x, y, this.superStar);
        } else { //coin
            generated = new Coin(x, y, this.coin, 10);
        }
        return generated;
    }

}
