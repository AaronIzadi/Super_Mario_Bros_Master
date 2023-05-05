package graphic.manager;

import model.Flag;
import model.brick.*;
import model.prize.*;
import graphic.game_view.ImageLoader;
import model.Map;
import model.enemy.Goomba;
import model.enemy.KoopaTroopa;
import model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

class MapCreator {

    private final ImageLoader imageLoader;
    private final BufferedImage backgroundImage;
    private final BufferedImage superMushroom, oneUpMushroom, fireFlower, coin;
    private final BufferedImage ordinaryBrick, surpriseBrick, groundBrick, pipe, hole;
    private final BufferedImage goombaLeft, goombaRight, koopaLeft, koopaRight, endFlag;


    MapCreator(ImageLoader imageLoader) {

        this.imageLoader = imageLoader;
        BufferedImage sprite = imageLoader.loadImage("/sprite.png");
        this.backgroundImage = imageLoader.loadImage("/background.png");
        this.hole = imageLoader.loadImage("/hole.png");
        this.superMushroom = imageLoader.getSubImage(sprite, 2, 5, 48, 48);
        this.oneUpMushroom = imageLoader.getSubImage(sprite, 3, 5, 48, 48);
        this.fireFlower = imageLoader.getSubImage(sprite, 4, 5, 48, 48);
        this.coin = imageLoader.getSubImage(sprite, 1, 5, 48, 48);
        this.ordinaryBrick = imageLoader.getSubImage(sprite, 1, 1, 48, 48);
        this.surpriseBrick = imageLoader.getSubImage(sprite, 2, 1, 48, 48);
        this.groundBrick = imageLoader.getSubImage(sprite, 2, 2, 48, 48);
        this.pipe = imageLoader.getSubImage(sprite, 3, 1, 96, 96);
        this.goombaLeft = imageLoader.getSubImage(sprite, 2, 4, 48, 48);
        this.goombaRight = imageLoader.getSubImage(sprite, 5, 4, 48, 48);
        this.koopaLeft = imageLoader.getSubImage(sprite, 1, 3, 48, 64);
        this.koopaRight = imageLoader.getSubImage(sprite, 4, 3, 48, 64);
        this.endFlag = imageLoader.getSubImage(sprite, 5, 1, 48, 48);

    }

    Map createMap(String mapPath, int i) {
        BufferedImage mapImage = imageLoader.loadImage(mapPath);

        if (mapImage == null) {
            System.out.println("Given path is invalid...");
            return null;
        }

        Map createdMap = new Map();
        createdMap.setRemainingTime(400);
        createdMap.setBackgroundImage(backgroundImage);
        String[] paths = mapPath.split("/");
        createdMap.setPath(paths[paths.length - 1]);

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

        for (int x = 0; x < mapImage.getWidth(); x++) {
            for (int y = 0; y < mapImage.getHeight(); y++) {

                int currentPixel = mapImage.getRGB(x, y);
                int xLocation = x * pixelMultiplier;
                int yLocation = y * pixelMultiplier;

                if (currentPixel == ordinaryBrick) {
                    Brick brick = new OrdinaryBrick(xLocation, yLocation, this.ordinaryBrick);
                    createdMap.addBrick(brick);
                } else if (currentPixel == hole) {
                    Hole brick = new Hole(xLocation, yLocation, this.hole);
                    createdMap.addBrick(brick);
                    createdMap.addHoles(brick);
                } else if (currentPixel == surpriseBrick) {
                    Prize prize = generateRandomPrize(xLocation, yLocation);
                    Brick brick = new SurpriseBrick(xLocation, yLocation, this.surpriseBrick, prize);
                    createdMap.addBrick(brick);
                } else if (currentPixel == pipe) {
                    Brick brick = new Pipe(xLocation, yLocation, this.pipe);
                    createdMap.addGroundBrick(brick);
                } else if (currentPixel == groundBrick) {
                    Brick brick = new GroundBrick(xLocation, yLocation, this.groundBrick);
                    createdMap.addGroundBrick(brick);
                } else if (currentPixel == goomba) {
                    Goomba enemy = new Goomba(xLocation, yLocation, this.goombaLeft);
                    enemy.setRightImage(goombaRight);
                    createdMap.addEnemy(enemy);
                } else if (currentPixel == koopa) {
                    KoopaTroopa enemy = new KoopaTroopa(xLocation, yLocation, this.koopaLeft);
                    enemy.setRightImage(koopaRight);
                    createdMap.addEnemy(enemy);
                } else if (currentPixel == hero) {
                    Hero heroObject = new Hero(xLocation, yLocation);
                    createdMap.setHero(heroObject);
                } else if (currentPixel == end) {
                    Flag endPoint = new Flag(xLocation + 24, yLocation, endFlag);
                    createdMap.setEndPoint(endPoint);
                }
            }
        }

        System.out.println("Map is being created.");
        return createdMap;
    }

    private Prize generateRandomPrize(double x, double y) {
        Prize generated;
        int random = (int) (Math.random() * 12);

        if (random == 0) { //super mushroom
            generated = new SuperMushroom(x, y, this.superMushroom);
        } else if (random == 1) { //fire flower
            generated = new FireFlower(x, y, this.fireFlower);
        } else if (random == 2) { //one up mushroom
            generated = new OrdinaryMushroom(x, y, this.oneUpMushroom);
        } else { //coin
            generated = new Coin(x, y, this.coin, 50);
        }

        return generated;
    }


}
