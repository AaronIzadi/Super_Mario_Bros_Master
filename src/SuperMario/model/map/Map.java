package SuperMario.model.map;


import SuperMario.logic.GameEngine;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.hero.Hero;
import SuperMario.model.obstacle.*;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.Prize;
import SuperMario.model.prize.PrizeItems;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

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
        if (bowser != null) {
            bowser.attack(hero);
            if (bowser.getFire() != null) {
                bowser.getFire().draw(g2);
            }
            if (bowser.getBomb() != null) {
                if (bowser.getBomb().isTimeToVanish()) {
                    bowser.setBomb(null);
                } else {
                    bowser.getBomb().draw(g2);
                }
            }
        }
        drawEnemies(g2);
        drawBricks(g2);
        drawFireballs(g2);
        if (castle != null) {
            castle.draw(g2);
        }
        endPoint.draw(g2);
        drawHero(g2);
    }

    public void drawCrossover(Graphics2D g2) {
        drawBricks(g2);
        drawPrizes(g2);
        drawHero(g2);
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

        if (bowser != null) {
            bowser.setToRight(getHero().getX() > bowser.getX());
            if (bowser.getFire() != null) {
                bowser.getFire().updateLocation();
            }
            if (bowser.getBomb() != null) {
                bowser.getBomb().updateLocation();
            }
        }

        for (Enemy enemy : enemies) {
            enemy.updateLocation();
        }

        updatePrizeLocation();

        if (axe != null && axe.isReleased()) {
            axe.updateLocation();
        }

        for (Fireball fireball : fireballs) {
            fireball.updateLocation();
        }

        for (Iterator<Brick> brickIterator = revealedBricks.iterator(); brickIterator.hasNext(); ) {
            Brick brick = brickIterator.next();
            CoinBrick ifOneCoin;
            OrdinaryBrick ifOrdinary;

            if (brick instanceof CoinBrick) {
                ifOneCoin = (CoinBrick) brick;
                ifOneCoin.animate();
                if (ifOneCoin.getFrames() < 0) {
                    bricks.remove(brick);
                    getHero().acquirePoints(1);
                    brickIterator.remove();
                }
            } else {
                ifOrdinary = (OrdinaryBrick) brick;
                ifOrdinary.animate();
                if (ifOrdinary.getFrames() < 0) {
                    bricks.remove(brick);
                    getHero().acquirePoints(1);
                    brickIterator.remove();
                }
            }
        }
        endPoint.updateLocation();
    }

    public void updateLocationsForCrossover() {
        hero.updateLocation();
        updatePrizeLocation();
    }

    private void updatePrizeLocation() {
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
    }

    public double getBottomBorder() {
        return 720;
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

    public void removeBrick(Brick brick) {
        bricks.remove(brick);
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

    public void setCheckPoint(CheckPoint checkPoint) {
        this.checkPoint = checkPoint;
    }

    public CheckPoint getCheckPoint() {
        return checkPoint;
    }

    public void setCastle(Castle castle) {
        this.castle = castle;
    }

    public void setBowser(Bowser bowser) {
        this.bowser = bowser;
        if (bowser == null) {
            GameEngine.getInstance().playBowserDies();
            GameEngine.getInstance().stopBossFightBackground();
            GameEngine.getInstance().playStageClear();
        }
    }

    public Bowser getBowser() {
        return bowser;
    }

    public ArrayList<Brick> getGroundBricks() {
        return groundBricks;
    }
}
