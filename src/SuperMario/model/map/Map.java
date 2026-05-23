package SuperMario.model.map;

import SuperMario.config.GameConstants;
import SuperMario.logic.map.MapWorldLogic;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.hero.Hero;
import SuperMario.model.obstacle.*;
import SuperMario.model.prize.Prize;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {

    private double remainingTime;
    private Hero hero;
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Obstacle> groundBricks = new ArrayList<>();
    private final ArrayList<Prize> revealedPrizes = new ArrayList<>();
    private final ArrayList<Brick> revealedBricks = new ArrayList<>();
    private final ArrayList<Fireball> fireballs = new ArrayList<>();
    private CheckPoint checkPoint;
    private Bowser bowser;
    private Axe axe;
    private Castle castle;
    private Flag endPoint;
    private BufferedImage backgroundImage;
    private String path;

    public Map() {
    }

    public Map(Hero hero) {
        this.hero = hero;
    }

    public void drawMap(Graphics2D g2) {
        MapWorldLogic.drawMap(this, g2);
    }

    public void drawCrossover(Graphics2D g2) {
        MapWorldLogic.drawCrossover(this, g2);
    }

    public void updateLocations() {
        MapWorldLogic.updateLocations(this);
    }

    public void updateLocationsForCrossover() {
        MapWorldLogic.updateLocationsForCrossover(this);
    }

    public void stopBurning() {
        MapWorldLogic.stopBurning(this);
    }

    public ArrayList<Obstacle> getAllObstacles() {
        ArrayList<Obstacle> allObstacles = new ArrayList<>();

        allObstacles.addAll(obstacles);
        allObstacles.addAll(groundBricks);

        return allObstacles;
    }

    public double getBottomBorder() {
        return GameConstants.WORLD_HEIGHT;
    }

    public void addRevealedPrize(Prize prize) {
        revealedPrizes.add(prize);
    }

    public void addFireball(Fireball fireball) {
        fireballs.add(fireball);
    }

    public void addAxe(Axe axe) {
        this.axe = axe;
    }

    public void removeAxe() {
        this.axe = null;
    }

    public Axe getAxe() {
        return axe;
    }

    public void setEndPoint(Flag endPoint) {
        this.endPoint = endPoint;
    }

    public Flag getEndPoint() {
        return endPoint;
    }

    public void addRevealedBrick(OrdinaryBrick ordinaryBrick) {
        revealedBricks.add(ordinaryBrick);
    }

    public void addRevealedBrick(CoinBrick coinBrick) {
        revealedBricks.add(coinBrick);
    }

    public void removeFireball(Fireball object) {
        fireballs.remove(object);
    }

    public void removeEnemy(Enemy object) {
        enemies.remove(object);
    }

    public void removePrize(Prize object) {
        revealedPrizes.remove(object);
    }

    public void removeObstacle(Obstacle obstacle) {
        obstacles.remove(obstacle);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void updateTime(double passed) {
        remainingTime = remainingTime - passed;
    }

    public boolean isTimeOver() {
        return remainingTime <= 0;
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public void setCheckPoint(CheckPoint checkPoint) {
        this.checkPoint = checkPoint;
    }

    public CheckPoint getCheckPoint() {
        return checkPoint;
    }

    public void setCastle(Castle castle) {
        this.castle = castle;
    }

    public Castle getCastle() {
        return castle;
    }

    public void setBowser(Bowser bowser) {
        this.bowser = bowser;
    }

    public Bowser getBowser() {
        return bowser;
    }

    public ArrayList<Obstacle> getGroundBricks() {
        return groundBricks;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Fireball> getFireballs() {
        return fireballs;
    }

    public ArrayList<Prize> getRevealedPrizes() {
        return revealedPrizes;
    }

    public ArrayList<Brick> getRevealedBricks() {
        return revealedBricks;
    }

    public void addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
    }

    public void addGroundBrick(Obstacle brick) {
        this.groundBricks.add(brick);
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }
}
