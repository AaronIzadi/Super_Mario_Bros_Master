package SuperMario.logic;

import SuperMario.config.GameConstants;
import SuperMario.graphic.manager.MapCreator;
import SuperMario.graphic.view.states.GameState;
import SuperMario.graphic.view.states.MapSelection;
import SuperMario.input.ImageLoader;
import SuperMario.logic.brick.BrickLogic;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.logic.enemy.EnemyLogic;
import SuperMario.logic.prize.PrizeLogic;
import SuperMario.model.GameObject;
import SuperMario.model.enemy.bowser.Bomb;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.enemy.bowser.Fire;
import SuperMario.model.map.Map;
import SuperMario.model.enemy.*;
import SuperMario.model.hero.Hero;
import SuperMario.model.obstacle.*;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.Prize;
import SuperMario.model.prize.PrizeItems;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;


import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MapManager {

    private Timer grabTimer;
    private Map map;
    private Map crossover;
    private Hero hero;
    private double xBeforeCrossover;
    private double yBeforeCrossover;
    private double xHero;
    private double yHero;
    private double progressRate;
    private boolean isChecked = false;
    private final ArrayList<GameObject> toBeRemoved = new ArrayList<>();

    private GameEngine engine;
    private GameStateManager stateManager;
    private SoundManager soundManager;
    private UserData userData;

    private static final MapManager instance = new MapManager();

    private MapManager() {
    }

    public static MapManager getInstance() {
        return instance;
    }

    public void initialize(GameEngine engine) {
        this.engine = engine;
        stateManager = engine.getStateManager();
        soundManager = engine.getSoundManager();
        userData = engine.getUserData();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    private Map getActiveMap() {
        return stateManager.getGameState() == GameState.CROSSOVER ? crossover : map;
    }

    void updateLocations() {
        if (map == null) {
            return;
        }
        map.updateLocations();
        if (getMap().getEndPoint() != null) {
            progressRate = getHero().getX() / getMap().getEndPoint().getX();
        } else {
            progressRate = 0.0000001;
        }
    }

    void createMap(String path) {
        boolean loaded = isMapCreated(path);
        userData.setHero(hero);
        userData.setMap(map);
        if (loaded) {
            stateManager.setGameState(GameState.RUNNING);
            if (engine.getUserData().getWorldNumber() == MapSelection.BOSS_FIGHT.getWorldNumber()) {
                soundManager.playBossFightBackground();
            } else {
                soundManager.restartBackground();
            }
        } else {
            stateManager.setGameState(GameState.START_SCREEN);
        }
    }

    private boolean isMapCreated(String path) {
        MapCreator mapCreator = new MapCreator();
        map = mapCreator.createMap("/maps/" + path);
        hero = map.getHero();
        return map != null;
    }

    Map createMap(String path, Hero hero) {
        boolean loaded = isMapCreated(path, hero);
        userData.setHero(hero);
        userData.setMap(map);
        if (loaded) {
            stateManager.setGameState(GameState.RUNNING);
            if (userData.getWorldNumber() == MapSelection.BOSS_FIGHT.getWorldNumber()) {
                soundManager.playBossFightBackground();
            } else {
                soundManager.restartBackground();
            }
            return map;
        } else {
            stateManager.setGameState(GameState.START_SCREEN);
        }
        return null;
    }

    private boolean isMapCreated(String path, Hero hero) {
        ImageLoader.getInstance().setHeroType(hero.getType());
        MapCreator mapCreator = new MapCreator(hero);
        map = mapCreator.createMap("/maps/" + path);
        map.setHero(hero);
        setHero(hero);
        return map != null;
    }

    void updateLocationsForCrossover() {
        if (crossover == null) {
            return;
        }
        crossover.updateLocationsForCrossover();
    }

    void resetCurrentMap(GameEngine engine) {
        Hero hero = getHero();
        hero.setVelX(0);
        hero.setVelY(0);
        hero.setX(50);
        hero.setY(100);
        hero.setJumping(false);
        hero.setFalling(true);
        engine.getCameraManager().resetCamera();
        if (MapSelection.BOSS_FIGHT.getMapPath(MapSelection.BOSS_FIGHT.getWorldNumber()).equals(map.getPath())) {
            isMapCreated(map.getPath(), hero);
        }
    }

    private void reLoadCheckPoint(double x, double y) {
        Hero hero = getHero();
        hero.setVelY(0);
        hero.setX(x);
        hero.setY(y);
        hero.setJumping(false);
        hero.setFalling(false);
        engine.getCameraManager().reLoadCheckPoint(x);
    }

    private void createCrossover(String path, Hero hero) {
        ImageLoader.getInstance().setHeroType(hero.getType());
        MapCreator mapCreator = new MapCreator();
        crossover = mapCreator.createCrossOver("/maps/" + path, hero);
    }

    void selectMap(int worldNumber) {
        String path = engine.getInputManager().getMapSelection().selectMap(worldNumber);
        if (path != null) {
            if (userData.getHero() != null) {
                createMap(path, userData.getHero());
            } else {
                createMap(path);
            }
        }
    }

    void acquirePoints(int point) {
        map.getHero().acquirePoints(point);
    }

    public Hero getHero() {
        return hero;
    }

    void setHero(Hero hero) {
        this.hero = hero;
    }

    Map getMap() {
        return map;
    }

    void setMap(Map map) {
        this.map = map;
    }

    void fire() {
        Fireball fireball = getHero().fire();
        Map currentMap;
        if (stateManager.getGameState() == GameState.RUNNING) {
            currentMap = map;
        } else {
            currentMap = crossover;
        }
        if (fireball != null) {
            currentMap.addFireball(fireball);
            soundManager.playFireball();

            if (currentMap.getBowser() != null) {
                currentMap.getBowser().canJump(Math.abs(currentMap.getBowser().getX() - fireball.getX()) >= (8 * 48));
            }
        }
    }

    void activateAxe() {

        if (getHero().getAxe() == null) {
            map.removeAxe();
        }

        if (map.getAxe() == null) {
            getHero().activateAxe();
            Map currentMap;
            if (stateManager.getGameState() == GameState.RUNNING) {
                currentMap = map;
            } else {
                currentMap = crossover;
            }

            if (getHero().getAxe() != null) {
                getHero().setAxeActivated(true);
                currentMap.addAxe(getHero().getAxe());
            }
        }
    }

    private boolean checkIfBowserDies() {
        if (map.getBowser().getHp() <= 0) {
            map.getAllObstacles().removeIf(brick -> brick instanceof GroundBrick);
            map.getGroundBricks().removeIf(brick -> brick instanceof GroundBrick);
            map.stopBurning();
            soundManager.playBreakBrick();
            return true;
        }
        return false;
    }

    void throwAxe() {
        if (getHero().isAxeActivated()) {
            getHero().throwAxe();
        }
    }

    boolean isGameOver() {
        return hero.getRemainingLives() == 0 || map.isTimeOver();
    }

    public int getScore() {
        return hero.getPoints();
    }

    public int getRemainingLives() {
        return hero.getRemainingLives();
    }

    public int getCoins() {
        return hero.getCoins();
    }

    public void drawMap(Graphics2D g2) {
        map.drawMap(g2);
    }

    public void drawCrossover(Graphics2D g2) {
        crossover.drawCrossover(g2);
    }

    int passMission() {
        if (hero.getX() >= map.getEndPoint().getX() && !map.getEndPoint().isTouched()) {
            map.getEndPoint().setTouched(true);
            soundManager.playFlagPole();
            int height = (int) getHero().getY();
            return height * 2;
        } else {
            return -1;
        }
    }

    boolean endLevel() {
        return hero.getX() >= map.getEndPoint().getX() + 320;
    }

    void checkCollisions(GameEngine engine) {
        if (map == null) {
            return;
        }
        checkBottomCollisions(engine);
        checkTopCollisions(engine);
        checkHeroHorizontalCollision(engine);
        checkEnemyCollisions();
        checkPrizeCollision();
        checkPrizeContact(engine);
        checkWeaponContact();

        if (map.getBowser() != null) {
            checkEnemyWeaponContact();
        }

        if (map.getBowser() != null) {
            checkBowserPossibleCollisions(map.getBowser());
            for (Bomb bomb : map.getBowser().getBomb()) {
                checkBowserPossibleCollisions(bomb);
            }
            if (hero.isGrabbed()) {
                updateGrabState();
            }
        }
    }


    private void checkBowserPossibleCollisions(GameObject object) {
        Map currentMap = getActiveMap();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        Rectangle bottomBounds = object.getBottomBounds();

        boolean toRight = object.isToRight();

        Rectangle horizontalBounds = toRight ? object.getRightBounds() : object.getLeftBounds();

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = !toRight ? obstacle.getRightBounds() : obstacle.getLeftBounds();

            if (horizontalBounds.intersects(obstacleBounds)) {
                object.setVelX(object.getVelX() * -1);
                if (toRight) {
                    object.setX(obstacle.getX() - object.getDimension().width);
                } else {
                    object.setX(obstacle.getX() + obstacle.getDimension().width);
                }
            }
        }

        for (Obstacle obstacle : obstacles) {

            Rectangle obstacleTopBounds = obstacle.getTopBounds();
            if (bottomBounds.intersects(obstacleTopBounds)) {

                if (!(obstacle instanceof Hole)) {
                    object.setY(obstacle.getY() - object.getDimension().height + 1);
                    object.setFalling(false);
                    object.setVelY(0);
                } else {
                    object.setFalling(true);
                }
            }
        }

        Rectangle topBounds = object.getTopBounds();

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBottomBounds = obstacle.getBottomBounds();
            if (topBounds.intersects(obstacleBottomBounds)) {
                object.setVelY(0);
                object.setY(obstacle.getY() + obstacle.getDimension().height);
                if (obstacle instanceof Brick) {
                    BrickLogic.reveal((Brick) obstacle, engine);
                }
            }
        }

        if (object.getY() + object.getDimension().height >= map.getBottomBorder() - (2 * GameConstants.TILE_SIZE)) {
            if (object instanceof Bowser && !((Bowser) object).hasTouchedGround()) {
                engine.getCameraManager().shakeCamera();
                if (hero.getBottomBounds().getY() >= GameConstants.GROUND_BRICK_Y) {
                    hero.onTouchEnemy(engine, 0);
                }
                ((Bowser) object).setHasTouchedGround(true);
            } else if (object instanceof Bomb) {
                BowserLogic.setHasIntersect((Bomb) object, true);
            }
            object.setFalling(false);
        }

    }

    private void checkBottomCollisions(GameEngine engine) {
        Map currentMap = getActiveMap();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        ArrayList<Enemy> enemies = currentMap.getEnemies();

        Rectangle heroBottomBounds = hero.getBottomBounds();

        boolean heroHasBottomIntersection = false;

        for (Obstacle obstacle : obstacles) {


            if (obstacle instanceof Brick && (int) hero.getBottomBounds().getY() >= GameConstants.GROUND_BRICK_Y) {
                ((Brick) obstacle).setTimer(0);
            }

            Rectangle obstacleTopBounds = obstacle.getTopBounds();
            if (heroBottomBounds.intersects(obstacleTopBounds)) {
                if (userData.getWorldNumber() == MapSelection.BOSS_FIGHT.getWorldNumber()) {
                    if (obstacle instanceof Brick) {
                        if (BrickLogic.isTimeToBreak((Brick) obstacle)) {
                            toBeRemoved.add(obstacle);
                        }
                    }
                }


                if (!(obstacle instanceof Hole)) {
                    hero.setY(obstacle.getY() - hero.getDimension().height + 1);
                    hero.setFalling(false);
                    hero.setVelY(0);
                    heroHasBottomIntersection = true;
                    if (obstacle instanceof Slime) {
                        BrickLogic.setOnTouch((Slime) obstacle, true);
                        hero.jumpOnSlime();
                        soundManager.playJump();
                    }
                    if (obstacle instanceof CrossoverTunnel && !((CrossoverTunnel) obstacle).isRevealed() && engine.getInputManager().getInputReceiver().isDown()) {
                        if (stateManager.getGameState() == GameState.RUNNING) {
                            soundManager.playPipe();
                            xBeforeCrossover = hero.getX();
                            yBeforeCrossover = hero.getY();
                            ((CrossoverTunnel) obstacle).setRevealed(true);
                            stateManager.setGameState(GameState.CROSSOVER);
                            if (userData.getWorldNumber() == 0) {
                                createCrossover(MapSelection.CROSSOVER_1.getMapPath(MapSelection.CROSSOVER_1.getWorldNumber()), hero);
                            } else if (userData.getWorldNumber() == 1) {
                                createCrossover(MapSelection.CROSSOVER_2.getMapPath(MapSelection.CROSSOVER_2.getWorldNumber()), hero);
                            } else {
                                createCrossover(MapSelection.CROSSOVER_3.getMapPath(MapSelection.CROSSOVER_3.getWorldNumber()), hero);
                            }
                        } else {
                            stateManager.setGameState(GameState.RUNNING);
                            hero.setCrouching(false);
                            hero.setLocation(xBeforeCrossover, yBeforeCrossover);
                        }
                    }
                } else {
                    hero.setFalling(true);
                }
            }
        }

        hero.setFalling(!heroHasBottomIntersection);

        for (Enemy enemy : enemies) {
            Rectangle enemyTopBounds = enemy.getTopBounds();
            if (heroBottomBounds.intersects(enemyTopBounds) && !(enemy instanceof Spiny) && !(enemy instanceof Piranha)) {
                if (enemy instanceof Bowser) {
                    int newHP = ((Bowser) enemy).getHp() > 3 ? (((Bowser) enemy).getHp() - 3) : 0;
                    BowserLogic.setHp((Bowser) enemy, newHP);
                    soundManager.playStomp();
                    hero.setFalling(false);
                    hero.jump();
                    soundManager.playJump();
                    if (checkIfBowserDies()) {
                        toBeRemoved.add(enemy);
                        map.setBowser(null);
                        soundManager.heroWinsOverBowser();
                    }
                } else if (enemy instanceof KoopaTroopa) {
                    KoopaTroopa koopaTroopa = ((KoopaTroopa) enemy);
                    if (!koopaTroopa.isHit()) {
                        koopaTroopa.setHit(true);
                        EnemyLogic.moveAfterHit(koopaTroopa);
                        hero.setTimerToRun();
                    } else {
                        acquirePoints(2);
                        toBeRemoved.add(enemy);
                        soundManager.playStomp();
                    }
                    hero.setFalling(false);
                    hero.jumpOnEnemy();
                    soundManager.playJump();
                } else {
                    acquirePoints(1);
                    toBeRemoved.add(enemy);
                    soundManager.playStomp();
                    hero.setFalling(false);
                    hero.jumpOnEnemy();
                    soundManager.playJump();
                }
            }
        }

        if (hero.getY() >= map.getBottomBorder()) {
            hero.onTouchBorder(engine, calculateLosingCoins());
            if (isChecked) {
                reLoadCheckPoint(xHero, yHero);
            } else {
                resetCurrentMap(engine);
            }
        }

        removeObjects(toBeRemoved);
    }


    private void checkTopCollisions(GameEngine engine) {
        Map currentMap = getActiveMap();

        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        Rectangle heroTopBounds = hero.getTopBounds();

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBottomBounds = obstacle.getBottomBounds();
            if (!(obstacle instanceof Hole) && !(obstacle instanceof CheckPoint) && heroTopBounds.intersects(obstacleBottomBounds)) {
                hero.setVelY(0);
                hero.setY(obstacle.getY() + obstacle.getDimension().height);
                if (obstacle instanceof Brick) {
                    Prize prize = BrickLogic.reveal((Brick) obstacle, engine);
                    if (prize != null) {
                        currentMap.addRevealedPrize(prize);
                    }
                }
            } else if (obstacle instanceof CheckPoint && heroTopBounds.intersects(obstacleBottomBounds)) {
                if (!((CheckPoint) obstacle).isRevealed()) {
                    pauseInCheckPoint();
                } else {
                    hero.setVelY(0);
                    hero.setY(obstacle.getY() + obstacle.getDimension().height);
                }
            }
        }
    }

    private void checkHeroHorizontalCollision(GameEngine engine) {
        Map currentMap = getActiveMap();

        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        ArrayList<Enemy> enemies = currentMap.getEnemies();

        boolean heroDies = false;
        boolean toRight = hero.getToRight();

        Rectangle heroBounds = toRight ? hero.getRightBounds() : hero.getLeftBounds();

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = !toRight ? obstacle.getRightBounds() : obstacle.getLeftBounds();

            if (heroBounds.intersects(obstacleBounds)) {
                hero.setVelX(0);
                if (toRight) {
                    hero.setX(obstacle.getX() - hero.getDimension().width);
                } else {
                    hero.setX(obstacle.getX() + obstacle.getDimension().width);
                }
            }
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            if (heroBounds.intersects(enemyBounds) && !hero.isFalling()) {

                if (!hero.hasStarPower()) {
                    if (enemy instanceof Bowser && ((Bowser) enemy).isGrabAttackOn()) {
                        if (!hero.isGrabbed()) {
                            hero.setGrabbed(true);
                            BowserLogic.stopMoving(map.getBowser());
                            ((Bowser) enemy).setCanHurt(false);
                            setTimerForGrabAttack();
                        }
                    } else if (enemy instanceof Bowser && ((Bowser) enemy).canHurt()) {
                        heroDies = hero.onTouchEnemy(engine, calculateLosingCoins());
                    } else {
                        heroDies = hero.onTouchEnemy(engine, calculateLosingCoins());
                        if (enemy instanceof KoopaTroopa) {
                            ((KoopaTroopa) enemy).setHit(true);
                        } else if (enemy instanceof Goomba) {
                            toBeRemoved.add(enemy);
                        }
                    }
                } else {
                    if (enemy instanceof Bowser) {
                        BowserLogic.setHp((Bowser) enemy, ((Bowser) enemy).getHp() - 1);
                        if (checkIfBowserDies()) {
                            toBeRemoved.add(enemy);
                            map.setBowser(null);
                            soundManager.heroWinsOverBowser();
                        }
                    } else {
                        toBeRemoved.add(enemy);
                    }
                }
            }
        }
        removeObjects(toBeRemoved);

        if (heroDies) {
            if (isChecked) {
                reLoadCheckPoint(xHero, yHero);
            } else {
                resetCurrentMap(engine);
            }
        }
    }

    private void updateGrabState() {
        if (hero.getNumberOfTryToEscape() >= 10) {
            hero.setGrabbed(false);
            double x = map.getBowser().isToRight() ? (104 + 96 - 24) : (-96 + 24);
            hero.setX(hero.getX() + x);
            hero.setNumberOfTryToEscape(0);
            grabTimer.cancel();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    map.getBowser().setCanHurt(true);
                    map.getBowser().setCoolDownFinished(true);
                    BowserLogic.moveNormal(map.getBowser(), map.getBowser().isToRight());
                    map.getBowser().setGrabAttackOn(false);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 4000);
        } else {
            map.getBowser().setCoolDownFinished(false);
        }
    }


    private void setTimerForGrabAttack() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (hero.isGrabbed()) {
                    hero.setGrabbed(false);
                    hero.onTouchEnemy(engine, 0);
                    double x = map.getBowser().isToRight() ? (104 + 96 - 24) : (-96 + 24);
                    hero.setX(hero.getX() + x);
                    hero.setNumberOfTryToEscape(0);
                    BowserLogic.moveNormal(map.getBowser(), map.getBowser().isToRight());
                    map.getBowser().setGrabAttackOn(false);
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            map.getBowser().setCanHurt(true);
                            map.getBowser().setCoolDownFinished(true);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 4000);
                }
            }
        };
        grabTimer = new Timer();
        grabTimer.schedule(task, 5000);
    }

    private void checkEnemyCollisions() {
        Map currentMap = getActiveMap();

        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        ArrayList<Enemy> enemies = getEnemies(currentMap);

        for (Enemy enemy : enemies) {
            if (!(enemy instanceof Piranha)) {
                boolean standsOnBrick = false;

                for (Obstacle obstacle : obstacles) {
                    Rectangle enemyBounds = enemy.getLeftBounds();
                    Rectangle obstacleRightBounds = obstacle.getRightBounds();

                    Rectangle enemyBottomBounds = enemy.getBottomBounds();
                    Rectangle obstacleTopBounds = obstacle.getTopBounds();

                    if (enemy.getVelX() > 0) {
                        enemyBounds = enemy.getRightBounds();
                        obstacleRightBounds = obstacle.getLeftBounds();
                    }

                    if (enemyBounds.intersects(obstacleRightBounds)) {
                        enemy.setVelX(-enemy.getVelX());
                    }

                    if (enemyBottomBounds.intersects(obstacleTopBounds)) {
                        enemy.setFalling(false);
                        enemy.setVelY(0);
                        enemy.setY(obstacle.getY() - enemy.getDimension().height);
                        standsOnBrick = true;
                    }
                }

                if (enemy.getY() + enemy.getDimension().height >= map.getBottomBorder()) {
                    enemy.setFalling(false);
                    enemy.setVelY(0);
                }

                if (!standsOnBrick && enemy.getY() < map.getBottomBorder()) {
                    enemy.setFalling(true);
                }
            }
        }
    }

    private ArrayList<Enemy> getEnemies(Map currentMap) {
        ArrayList<Enemy> enemies = currentMap.getEnemies();


        for (Enemy enemy : enemies) {
            if (enemy instanceof Spiny && ((getHero().getY() + getHero().getStyle().getHeight()) == (enemy.getY() + enemy.getStyle().getHeight() + 1))) {
                Spiny spiny = (Spiny) enemy;
                if (Math.abs(spiny.getX() - getHero().getX()) <= 192) {
                    EnemyLogic.moveFaster(spiny);
                } else {
                    EnemyLogic.moveNormal(spiny);
                }
            }
        }
        return enemies;
    }

    private void checkPrizeCollision() {
        Map currentMap = getActiveMap();

        ArrayList<Prize> prizes = currentMap.getRevealedPrizes();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();

        for (Prize prize : prizes) {
            if (prize instanceof PrizeItems) {
                PrizeItems boost = (PrizeItems) prize;
                Rectangle prizeBottomBounds = boost.getBottomBounds();
                Rectangle prizeRightBounds = boost.getRightBounds();
                Rectangle prizeLeftBounds = boost.getLeftBounds();
                boost.setFalling(true);

                for (Obstacle obstacle : obstacles) {
                    Rectangle obstacleBounds;

                    if (boost.isFalling()) {
                        obstacleBounds = obstacle.getTopBounds();

                        if (obstacleBounds.intersects(prizeBottomBounds)) {
                            boost.setFalling(false);
                            boost.setVelY(0);
                            boost.setY(obstacle.getY() - boost.getDimension().height + 1);
                            if (boost.getVelX() == 0)
                                boost.setVelX(2);
                        }
                    }

                    if (boost.getVelX() > 0) {
                        obstacleBounds = obstacle.getLeftBounds();

                        if (obstacleBounds.intersects(prizeRightBounds)) {
                            boost.setVelX(-boost.getVelX());
                        }
                    } else if (boost.getVelX() < 0) {
                        obstacleBounds = obstacle.getRightBounds();

                        if (obstacleBounds.intersects(prizeLeftBounds)) {
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
        Map currentMap = getActiveMap();

        ArrayList<Prize> prizes = currentMap.getRevealedPrizes();

        Rectangle heroBounds = hero.getBounds();
        for (Prize prize : prizes) {
            Rectangle prizeBounds = prize.getBounds();
            if (prizeBounds.intersects(heroBounds)) {
                PrizeLogic.onTouch(prize, getHero(), engine);
                toBeRemoved.add((GameObject) prize);
            } else if (prize instanceof Coin) {
                PrizeLogic.onTouch(prize, getHero(), engine);
            }
        }

        removeObjects(toBeRemoved);
    }

    private void checkWeaponContact() {
        Map currentMap = getActiveMap();

        ArrayList<Fireball> fireballs = currentMap.getFireballs();
        Axe axe = getMap().getHero().getAxe();


        if (axe != null) {
            checkWeaponCollision(axe);
        }

        for (Fireball fireball : fireballs) {
            checkWeaponCollision(fireball);
        }

        removeObjects(toBeRemoved);

    }

    private void checkWeaponCollision(GameObject object) {

        Map currentMap = getActiveMap();

        ArrayList<Enemy> enemies = currentMap.getEnemies();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();

        Rectangle objectBounds = object.getBounds();


        Bowser bowser = currentMap.getBowser();

        if (object instanceof Fireball && bowser != null) {
            double distance;
            if (object.isToRight() && !bowser.isToRight()) {
                distance = bowser.getX() - object.getX();
            } else if (!object.isToRight() && bowser.isToRight()) {
                distance = object.getX() - bowser.getX();
            } else {
                distance = 0;
            }

            if (distance <= (2 * GameConstants.TILE_SIZE) && distance >= GameConstants.TILE_SIZE) {
                BowserLogic.jump(bowser);
            }
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            if (objectBounds.intersects(enemyBounds)) {
                if (enemy instanceof Bowser) {
                    BowserLogic.setHp((Bowser) enemy, ((Bowser) enemy).getHp() - 1);
                    if (checkIfBowserDies()) {
                        toBeRemoved.add(enemy);
                        map.setBowser(null);
                        soundManager.heroWinsOverBowser();
                    }
                } else if (enemy instanceof Goomba) {
                    acquirePoints(1);
                    toBeRemoved.add(enemy);
                } else if (enemy instanceof KoopaTroopa) {
                    KoopaTroopa koopaTroopa = ((KoopaTroopa) enemy);
                    if (!koopaTroopa.isHit()) {
                        koopaTroopa.setHit(true);
                        EnemyLogic.moveAfterHit(koopaTroopa);
                    } else {
                        acquirePoints(2);
                        toBeRemoved.add(enemy);
                    }
                } else if (enemy instanceof Spiny) {
                    acquirePoints(3);
                    toBeRemoved.add(enemy);
                } else {
                    acquirePoints(1);
                    toBeRemoved.add(enemy);
                }
                soundManager.playKickEnemy();
                toBeRemoved.add(object);
            }
        }

        if (object instanceof Fireball || (object instanceof Axe && hero.getAxe().isReleased())) {
            Axe thrownAxe = object instanceof Axe ? (Axe) object : null;
            for (Obstacle obstacle : obstacles) {
                if (obstacle instanceof GroundBrick) {
                    continue;
                }
                if (thrownAxe != null
                        && Math.abs(thrownAxe.getX() - thrownAxe.getXReleasePoint()) < GameConstants.AXE_MIN_TRAVEL_BEFORE_BLOCK) {
                    continue;
                }
                Rectangle obstacleBounds = obstacle.getBounds();
                if (objectBounds.intersects(obstacleBounds)) {
                    toBeRemoved.add(object);
                }
            }
        }

    }

    private void checkEnemyWeaponContact() {

        for (Fire fire : map.getBowser().getFire()) {
            checkEnemyWeaponCollision(fire);
        }

        for (Bomb bomb : map.getBowser().getBomb()) {
            checkEnemyWeaponCollision(bomb);
        }

        removeObjects(toBeRemoved);
    }

    private void checkEnemyWeaponCollision(GameObject object) {

        Map currentMap = getActiveMap();

        ArrayList<Enemy> enemies = currentMap.getEnemies();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();

        Rectangle objectBounds = object.getBounds();
        if (object instanceof Bomb && ((Bomb) object).isExploded()) {
            objectBounds.y -= 48;
            objectBounds.x -= 48;
            objectBounds.height += 48;
            objectBounds.width += 48;
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            if (objectBounds.intersects(enemyBounds)) {
                if (enemy instanceof Bowser) {
                    if (object instanceof Bomb && ((Bomb) object).isExploded()) {
                        BowserLogic.setHp((Bowser) enemy, ((Bowser) enemy).getHp() - 1);
                        if (checkIfBowserDies()) {
                            toBeRemoved.add(enemy);
                            map.setBowser(null);
                            soundManager.heroWinsOverBowser();
                        }
                    }
                } else if (enemy instanceof Goomba) {
                    toBeRemoved.add(enemy);
                } else if (enemy instanceof KoopaTroopa) {
                    KoopaTroopa koopaTroopa = ((KoopaTroopa) enemy);
                    if (!koopaTroopa.isHit()) {
                        koopaTroopa.setHit(true);
                        EnemyLogic.moveAfterHit(koopaTroopa);
                    } else {
                        toBeRemoved.add(enemy);
                    }
                } else if (enemy instanceof Spiny) {
                    toBeRemoved.add(enemy);
                } else if (enemy instanceof Piranha) {
                    toBeRemoved.add(enemy);
                }

                if (object instanceof Bomb) {
                    if (!((Bomb) object).hasIntersect() && !(enemy instanceof Bowser)) {
                        BowserLogic.setHasIntersect((Bomb) object, true);
                    } else if (((Bomb) object).hasIntersect() && ((Bomb) object).isExploded()) {
                        toBeRemoved.add(enemy);
                    }
                } else if (object instanceof Fire && !(enemy instanceof Bowser)) {
                    toBeRemoved.add(object);
                }
            }
        }

        if (objectBounds.intersects(hero.getBounds())) {
            if (object instanceof Bomb) {
                if (!((Bomb) object).hasIntersect()) {
                    BowserLogic.setHasIntersect((Bomb) object, true);
                } else if (((Bomb) object).hasIntersect() && ((Bomb) object).isExploded()) {
                    hero.onTouchEnemy(engine, 0);
                    ((Bomb) object).setTimeToVanish(true);
                }
            } else if (object instanceof Fire) {
                toBeRemoved.add(object);
                hero.onTouchEnemy(engine, 0);
            }
        }

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = obstacle.getBounds();
            if (object instanceof Fire) {
                obstacleBounds = object.isToRight() ? obstacle.getLeftBounds() : obstacle.getRightBounds();
            }
            if (objectBounds.intersects(obstacleBounds)) {
                if (object instanceof Bomb) {
                    if (!((Bomb) object).hasIntersect()) {
                        BowserLogic.setHasIntersect((Bomb) object, true);
                    } else if (((Bomb) object).hasIntersect() && ((Bomb) object).isExploded()) {
                        toBeRemoved.add(obstacle);
                    }
                } else if (object instanceof Fire) {
                    toBeRemoved.add(object);
                }
            }
        }

        if (object instanceof Bomb && ((Bomb) object).isTimeToVanish()) {
            toBeRemoved.add(object);
        }
    }


    private void removeObjects(ArrayList<GameObject> list) {
        if (list == null) {
            return;
        }

        Map currentMap = getActiveMap();

        for (GameObject object : list) {
            if (object instanceof Fireball) {
                currentMap.removeFireball((Fireball) object);
            } else if (object instanceof Enemy) {
                currentMap.removeEnemy((Enemy) object);
            } else if (object instanceof Coin || object instanceof PrizeItems) {
                currentMap.removePrize((Prize) object);
            } else if (object instanceof Brick) {
                currentMap.removeObstacle((Brick) object);
            } else if (object instanceof Fire) {
                if (map.getBowser() != null) {
                    currentMap.getBowser().getFire().remove(object);
                }
            } else if (object instanceof Bomb) {
                if (map.getBowser() != null) {
                    currentMap.getBowser().getBomb().remove(object);
                }
            } else if (object instanceof Axe) {
                hero.deactivateAxe();
            }
        }
    }

    private void pauseInCheckPoint() {
        if (stateManager.getGameState() == GameState.RUNNING) {
            stateManager.setGameState(GameState.CHECKPOINT);
            soundManager.pauseBackground();
        } else if (stateManager.getGameState() == GameState.CHECKPOINT) {
            stateManager.setGameState(GameState.RUNNING);
            soundManager.resumeBackground();
        }
    }

    public void handleCheckPoint(boolean isChecked) {
        this.isChecked = isChecked;
        if (isChecked) {
            Point point = BrickLogic.check(map.getCheckPoint(), true);
            xHero = point.getX();
            yHero = point.getY() - 48 + 1;
            int price = (int) (progressRate * getCoins());
            getHero().setCoins(getCoins() - price);
        } else {
            BrickLogic.check(map.getCheckPoint(), false);
            int reward = (int) (progressRate * getCoins() * 0.25);
            getHero().setCoins(getCoins() + reward);
        }
    }

    private int calculateLosingCoins() {
        int n = isChecked ? 1 : 0;
        return (int) Math.floor((((n + 1) * getCoins()) + progressRate) / (n + 4));
    }

    public void addRevealedBrick(OrdinaryBrick ordinaryBrick) {
        Map currentMap = getActiveMap();
        currentMap.addRevealedBrick(ordinaryBrick);
    }

    public void addRevealedBrick(CoinBrick coinBrick) {
        Map currentMap = getActiveMap();
        currentMap.addRevealedBrick(coinBrick);
    }

    public void updateTime() {
        if (map != null) {
            map.updateTime(1);
        }
    }

    public int getRemainingTime() {
        return (int) map.getRemainingTime();
    }

    void setChecked() {
        isChecked = false;
    }
}
