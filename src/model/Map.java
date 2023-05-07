package model;

import model.brick.Brick;
import model.brick.Hole;
import model.brick.OrdinaryBrick;
import model.enemy.Enemy;
import model.prize.*;
import model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Map {

    private double remainingTime;
    private Hero hero;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private ArrayList<Hole> holes = new ArrayList<>();
     private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Brick> groundBricks = new ArrayList<>();
    private ArrayList<Prize> revealedPrizes = new ArrayList<>();
    private ArrayList<Brick> revealedBricks = new ArrayList<>();
    private ArrayList<Fireball> fireballs = new ArrayList<>();
    private Flag endPoint;
    private BufferedImage backgroundImage;
    private String path;


    public Map() {
    }

    public Map(Hero hero) {
        this.hero = hero;
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

    public ArrayList<Brick> getAllBricks() {
        ArrayList<Brick> allBricks = new ArrayList<>();

        allBricks.addAll(bricks);
        allBricks.addAll(groundBricks);

        return allBricks;
    }

    public void addBrick(Brick brick) {
        this.bricks.add(brick);
    }

    public void addGroundBrick(Brick brick) {
        this.groundBricks.add(brick);
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void drawMap(Graphics2D g2) {
        drawBackground(g2);
        drawPrizes(g2);
        drawBricks(g2);
        drawEnemies(g2);
        drawFireballs(g2);
        drawHero(g2);
        endPoint.draw(g2);
    }

    private void drawFireballs(Graphics2D g2) {
        for (Fireball fireball : fireballs) {
            fireball.draw(g2);
        }
    }

    private void drawPrizes(Graphics2D g2) {
        for (Prize prize : revealedPrizes) {
            if (prize instanceof Coin) {
                ((Coin) prize).draw(g2);
            } else if (prize instanceof PrizeItems) {
                ((PrizeItems) prize).draw(g2);
            }
        }
    }

    private void drawBackground(Graphics2D g2) {
        g2.drawImage(backgroundImage, 0, 0, null);
    }

    private void drawBricks(Graphics2D g2) {
        for (Brick brick : bricks) {
            if (brick != null)
                brick.draw(g2);
        }

        for (Brick brick : groundBricks) {
            brick.draw(g2);
        }
    }

    private void drawEnemies(Graphics2D g2) {
        for (Enemy enemy : enemies) {
            if (enemy != null)
                enemy.draw(g2);
        }
    }

    private void drawHero(Graphics2D g2) {
        hero.draw(g2);
    }

    public void updateLocations() {
        hero.updateLocation();
        for (Enemy enemy : enemies) {
            enemy.updateLocation();
        }

        for (Iterator<Prize> prizeIterator = revealedPrizes.iterator(); prizeIterator.hasNext(); ) {
            Prize prize = prizeIterator.next();
            if (prize instanceof Coin) {
                ((Coin) prize).updateLocation();
                if (((Coin) prize).getRevealBoundary() > ((Coin) prize).getY()) {
                    prizeIterator.remove();
                }
            } else if (prize instanceof PrizeItems) {
                ((PrizeItems) prize).updateLocation();
            }
        }

        for (Fireball fireball : fireballs) {
            fireball.updateLocation();
        }

        for (Iterator<Brick> brickIterator = revealedBricks.iterator(); brickIterator.hasNext(); ) {
            OrdinaryBrick brick = (OrdinaryBrick) brickIterator.next();
            brick.animate();
            if (brick.getFrames() < 0) {
                bricks.remove(brick);
                brickIterator.remove();
            }
        }

        endPoint.updateLocation();
    }

    public double getBottomBorder() {
        return 720 - 95;
    }

    public void addRevealedPrize(Prize prize) {
        revealedPrizes.add(prize);
    }

    public void addFireball(Fireball fireball) {
        fireballs.add(fireball);
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

    public void removeFireball(Fireball object) {
        fireballs.remove(object);
    }

    public void removeEnemy(Enemy object) {
        enemies.remove(object);
    }

    public void removePrize(Prize object) {
        revealedPrizes.remove(object);
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

    public void addHoles(Hole hole) {
        this.holes.add(hole);
    }


}
