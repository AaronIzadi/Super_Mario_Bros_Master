package graphic.manager;

import model.GameObject;
import model.Map;
import model.brick.Brick;
import model.brick.Hole;
import model.brick.OrdinaryBrick;
import model.enemy.*;
import model.prize.Fireball;
import model.hero.Hero;
import model.prize.PrizeItems;
import model.prize.Coin;
import model.prize.Prize;
import graphic.view.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MapManager {

    private Map map = new Map();

    public MapManager() {
    }

    public void updateLocations() {
        if (map == null)
            return;

        map.updateLocations();
    }

    public void resetCurrentMap(GameEngine engine) {
        Hero hero = getHero();
        hero.resetLocation();
        engine.resetCamera();
        createMap(engine.getImageLoader(), map.getPath());
        map.setHero(hero);
    }

    public boolean createMap(ImageLoader loader, String path) {
        MapCreator mapCreator = new MapCreator(loader);
        map = mapCreator.createMap("/maps/" + path);
        return map != null;
    }


    public void acquirePoints(int point) {
        map.getHero().acquirePoints(point);
    }

    public Hero getHero() {
        return map.getHero();
    }

    public void setHero(Hero hero) {
        map.setHero(hero);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void fire(GameEngine engine) {
        Fireball fireball = getHero().fire();
        if (fireball != null) {
            map.addFireball(fireball);
            engine.playFireball();
        }
    }

    public boolean isGameOver() {
        return getHero().getRemainingLives() == 0 || map.isTimeOver();
    }

    public int getScore() {
        return getHero().getPoints();
    }

    public int getRemainingLives() {
        return getHero().getRemainingLives();
    }

    public int getCoins() {
        return getHero().getCoins();
    }

    public void drawMap(Graphics2D g2) {
        map.drawMap(g2);
    }

    public int passMission() {
        if (getHero().getX() >= map.getEndPoint().getX() && !map.getEndPoint().isTouched()) {
            map.getEndPoint().setTouched(true);
            int height = (int) getHero().getY();
            return height * 2;
        } else
            return -1;
    }

    public boolean endLevel() {
        return getHero().getX() >= map.getEndPoint().getX() + 320;
    }

    public void checkCollisions(GameEngine engine) {
        if (map == null) {
            return;
        }

        checkBottomCollisions(engine);
        checkTopCollisions(engine);
        checkHeroHorizontalCollision(engine);
        checkEnemyCollisions();
        checkPrizeCollision();
        checkPrizeContact(engine);
        checkFireballContact(engine);
    }


    private void checkBottomCollisions(GameEngine engine) {
        Hero hero = getHero();
        ArrayList<Brick> bricks = map.getAllBricks();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<GameObject> toBeRemoved = new ArrayList<>();

        Rectangle heroBottomBounds = hero.getBottomBounds();

        if (!hero.isJumping()) {
            hero.setFalling(true);
        }

        for (Brick brick : bricks) {
            Rectangle brickTopBounds = brick.getTopBounds();
            if (heroBottomBounds.intersects(brickTopBounds)) {
                if (!(brick instanceof Hole)) {
                    hero.setY(brick.getY() - hero.getDimension().height + 1);
                    hero.setFalling(false);
                    hero.setVelY(0);
                } else {
                    hero.setY(720 - hero.getDimension().height - 1);
                    hero.setVelY(0);
                    hero.setFalling(true);
                    hero.onTouchHole(engine);
                    resetCurrentMap(engine);
                }
            }
        }
        for (Enemy enemy : enemies) {
            Rectangle enemyTopBounds = enemy.getTopBounds();
            if (heroBottomBounds.intersects(enemyTopBounds) && !(enemy instanceof Spiny) && !(enemy instanceof Piranha)) {
                if (enemy instanceof KoopaTroopa) {
                    KoopaTroopa koopaTroopa = ((KoopaTroopa) enemy);
                    if (!koopaTroopa.isHit()) {
                        koopaTroopa.setHit(true);
                        koopaTroopa.setXHit(koopaTroopa.getX());
                        koopaTroopa.moveAfterHit();
                        hero.setTimerToRun();
                    } else if (koopaTroopa.isHit() && Math.abs(koopaTroopa.getXHit() - koopaTroopa.getX()) == 96.0) {
                        acquirePoints(2);
                        if (engine.getHero() != null) {
                            engine.getHero().acquirePoints(2);
                        }
                        toBeRemoved.add(enemy);
                        engine.playStomp();
                    }
                } else {
                    acquirePoints(1);
                    if (engine.getHero() != null) {
                        engine.getHero().acquirePoints(1);
                    }
                    toBeRemoved.add(enemy);
                    engine.playStomp();
                }
            }
        }

        if (hero.getY() + hero.getDimension().height >= map.getBottomBorder()) {
            hero.setY(map.getBottomBorder() - hero.getDimension().height);
            hero.setFalling(false);
            hero.setVelY(0);
        }

        removeObjects(toBeRemoved);
    }

    public void setTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("3s");
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    private void checkTopCollisions(GameEngine engine) {
        Hero hero = getHero();
        ArrayList<Brick> bricks = map.getAllBricks();

        Rectangle heroTopBounds = hero.getTopBounds();
        for (Brick brick : bricks) {
            Rectangle brickBottomBounds = brick.getBottomBounds();
            if (heroTopBounds.intersects(brickBottomBounds)) {
                hero.setVelY(0);
                hero.setY(brick.getY() + brick.getDimension().height);
                Prize prize = brick.reveal(engine);
                if (prize != null)
                    map.addRevealedPrize(prize);
            }
        }
    }

    private void checkHeroHorizontalCollision(GameEngine engine) {
        Hero hero = getHero();
        ArrayList<Brick> bricks = map.getAllBricks();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<GameObject> toBeRemoved = new ArrayList<>();

        boolean heroDies = false;
        boolean toRight = hero.getToRight();

        Rectangle heroBounds = toRight ? hero.getRightBounds() : hero.getLeftBounds();

        for (Brick brick : bricks) {
            Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();

            if (heroBounds.intersects(brickBounds)) {
                hero.setVelX(0);
                if (toRight)
                    hero.setX(brick.getX() - hero.getDimension().width);
                else
                    hero.setX(brick.getX() + brick.getDimension().width);
            }
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            if (heroBounds.intersects(enemyBounds) && !hero.isFalling()) {
                heroDies = hero.onTouchEnemy(engine);
                toBeRemoved.add(enemy);
            }
        }
        removeObjects(toBeRemoved);


        if (hero.getX() <= engine.getCameraLocation().getX() && hero.getVelX() < 0) {
            hero.setVelX(0);
            hero.setX(engine.getCameraLocation().getX());
        }

        if (heroDies) {
            resetCurrentMap(engine);
        }
    }

    private void checkEnemyCollisions() {
        ArrayList<Brick> bricks = map.getAllBricks();
        ArrayList<Enemy> enemies = map.getEnemies();

        for (Enemy enemy : enemies) {
            if (enemy instanceof Spiny && getHero().getY() + getHero().getStyle().getHeight() == map.getBottomBorder()) {
                Spiny spiny = (Spiny) enemy;
                if (Math.abs(spiny.getX() - getHero().getX()) <= 192) {
                    spiny.moveFaster();
                } else {
                    spiny.moveNormal();
                }
            }
        }

        for (Enemy enemy : enemies) {
            boolean standsOnBrick = false;

            for (Brick brick : bricks) {
                Rectangle enemyBounds = enemy.getLeftBounds();
                Rectangle brickBounds = brick.getRightBounds();

                Rectangle enemyBottomBounds = enemy.getBottomBounds();
                Rectangle brickTopBounds = brick.getTopBounds();

                if (enemy.getVelX() > 0) {
                    enemyBounds = enemy.getRightBounds();
                    brickBounds = brick.getLeftBounds();
                }

                if (enemyBounds.intersects(brickBounds)) {
                    enemy.setVelX(-enemy.getVelX());
                }

                if (enemyBottomBounds.intersects(brickTopBounds)) {
                    enemy.setFalling(false);
                    enemy.setVelY(0);
                    enemy.setY(brick.getY() - enemy.getDimension().height);
                    standsOnBrick = true;
                }
            }

            if (enemy.getY() + enemy.getDimension().height > map.getBottomBorder()) {
                enemy.setFalling(false);
                enemy.setVelY(0);
                enemy.setY(map.getBottomBorder() - enemy.getDimension().height);
            }

            if (!standsOnBrick && enemy.getY() < map.getBottomBorder()) {
                enemy.setFalling(true);
            }
        }
    }

    private void checkPrizeCollision() {
        ArrayList<Prize> prizes = map.getRevealedPrizes();
        ArrayList<Brick> bricks = map.getAllBricks();

        for (Prize prize : prizes) {
            if (prize instanceof PrizeItems) {
                PrizeItems boost = (PrizeItems) prize;
                Rectangle prizeBottomBounds = boost.getBottomBounds();
                Rectangle prizeRightBounds = boost.getRightBounds();
                Rectangle prizeLeftBounds = boost.getLeftBounds();
                boost.setFalling(true);

                for (Brick brick : bricks) {
                    Rectangle brickBounds;

                    if (boost.isFalling()) {
                        brickBounds = brick.getTopBounds();

                        if (brickBounds.intersects(prizeBottomBounds)) {
                            boost.setFalling(false);
                            boost.setVelY(0);
                            boost.setY(brick.getY() - boost.getDimension().height + 1);
                            if (boost.getVelX() == 0)
                                boost.setVelX(2);
                        }
                    }

                    if (boost.getVelX() > 0) {
                        brickBounds = brick.getLeftBounds();

                        if (brickBounds.intersects(prizeRightBounds)) {
                            boost.setVelX(-boost.getVelX());
                        }
                    } else if (boost.getVelX() < 0) {
                        brickBounds = brick.getRightBounds();

                        if (brickBounds.intersects(prizeLeftBounds)) {
                            boost.setVelX(-boost.getVelX());
                        }
                    }
                }

                if (boost.getY() + boost.getDimension().height > map.getBottomBorder()) {
                    boost.setFalling(false);
                    boost.setVelY(0);
                    boost.setY(map.getBottomBorder() - boost.getDimension().height);
                    if (boost.getVelX() == 0)
                        boost.setVelX(2);
                }

            }
        }
    }

    private void checkPrizeContact(GameEngine engine) {
        ArrayList<Prize> prizes = map.getRevealedPrizes();
        ArrayList<GameObject> toBeRemoved = new ArrayList<>();

        Rectangle marioBounds = getHero().getBounds();
        for (Prize prize : prizes) {
            Rectangle prizeBounds = prize.getBounds();
            if (prizeBounds.intersects(marioBounds)) {
                prize.onTouch(getHero(), engine);
                toBeRemoved.add((GameObject) prize);
            } else if (prize instanceof Coin) {
                prize.onTouch(getHero(), engine);
            }
        }

        removeObjects(toBeRemoved);
    }

    private void checkFireballContact(GameEngine engine) {
        ArrayList<Fireball> fireballs = map.getFireballs();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<Brick> bricks = map.getAllBricks();
        ArrayList<GameObject> toBeRemoved = new ArrayList<>();

        for (Fireball fireball : fireballs) {
            Rectangle fireballBounds = fireball.getBounds();

            for (Enemy enemy : enemies) {
                Rectangle enemyBounds = enemy.getBounds();
                if (fireballBounds.intersects(enemyBounds)) {
                    if (enemy instanceof Goomba) {
                        acquirePoints(1);
                        if (engine.getHero() != null) {
                            engine.getHero().acquirePoints(1);
                        }
                    } else if (enemy instanceof KoopaTroopa) {
                        acquirePoints(2);
                        if (engine.getHero() != null) {
                            engine.getHero().acquirePoints(2);
                        }
                    } else if (enemy instanceof Spiny) {
                        acquirePoints(3);
                        if (engine.getHero() != null) {
                            engine.getHero().acquirePoints(3);
                        }
                    } else {
                        acquirePoints(1);
                        if (engine.getHero() != null) {
                            engine.getHero().acquirePoints(1);
                        }
                    }
                    toBeRemoved.add(enemy);
                    toBeRemoved.add(fireball);
                }
            }

            for (Brick brick : bricks) {
                Rectangle brickBounds = brick.getBounds();
                if (fireballBounds.intersects(brickBounds)) {
                    toBeRemoved.add(fireball);
                }
            }
        }

        removeObjects(toBeRemoved);
    }

    private void removeObjects(ArrayList<GameObject> list) {
        if (list == null)
            return;

        for (GameObject object : list) {
            if (object instanceof Fireball) {
                map.removeFireball((Fireball) object);
            } else if (object instanceof Enemy) {
                map.removeEnemy((Enemy) object);
            } else if (object instanceof Coin || object instanceof PrizeItems) {
                map.removePrize((Prize) object);
            }
        }
    }

    public void addRevealedBrick(OrdinaryBrick ordinaryBrick) {
        map.addRevealedBrick(ordinaryBrick);
    }

    public void updateTime() {
        if (map != null)
            map.updateTime(1);
    }

    public int getRemainingTime() {
        return (int) map.getRemainingTime();
    }
}
