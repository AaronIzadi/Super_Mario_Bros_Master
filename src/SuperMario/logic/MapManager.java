package SuperMario.logic;

import SuperMario.config.GameConstants;
import SuperMario.graphic.manager.MapCreator;
import SuperMario.graphic.view.states.GameState;
import SuperMario.graphic.view.states.MapSelection;
import SuperMario.logic.brick.BrickLogic;
import SuperMario.logic.collision.CollisionCoordinator;
import SuperMario.logic.collision.CollisionContext;
import SuperMario.logic.collision.MapCollisionCallbacks;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.logic.map.MapWorldLogic;
import SuperMario.logic.timer.EntityTimerLogic;
import SuperMario.logic.weapon.WeaponLogic;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.GroundBrick;
import SuperMario.model.obstacle.OrdinaryBrick;
import SuperMario.model.obstacle.CoinBrick;
import SuperMario.model.weapon.Fireball;

import java.awt.*;

public class MapManager implements MapCollisionCallbacks {

    private Map map;
    private Map crossover;
    private Hero hero;
    private double xBeforeCrossover;
    private double yBeforeCrossover;
    private double xHero;
    private double yHero;
    private double progressRate;
    private boolean isChecked = false;

    private GameEngine engine;
    private GameStateManager stateManager;
    private SoundManager soundManager;
    private UserData userData;

    public MapManager() {
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

    @Override
    public Map getActiveMap() {
        return stateManager.getGameState() == GameState.CROSSOVER ? crossover : map;
    }

    void updateLocations() {
        if (map == null) {
            return;
        }
        MapWorldLogic.updateLocations(map);
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
            playMapBackground();
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
            playMapBackground();
            return map;
        } else {
            stateManager.setGameState(GameState.START_SCREEN);
        }
        return null;
    }

    private boolean isMapCreated(String path, Hero hero) {
        engine.getImageLoader().setHeroType(hero.getType());
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
        MapWorldLogic.updateLocationsForCrossover(crossover);
    }

    public void resetCurrentMap(GameEngine engine) {
        EntityTimerLogic.clear();
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

    @Override
    public void reLoadCheckPoint(double x, double y) {
        Hero hero = getHero();
        hero.setVelY(0);
        hero.setX(x);
        hero.setY(y);
        hero.setJumping(false);
        hero.setFalling(false);
        engine.getCameraManager().reLoadCheckPoint(x);
    }

    @Override
    public void createCrossover(String path, Hero hero) {
        engine.getImageLoader().setHeroType(hero.getType());
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

    @Override
    public void acquirePoints(int point) {
        HeroLogic.acquirePoints(map.getHero(), point);
    }

    public Hero getHero() {
        return hero;
    }

    void setHero(Hero hero) {
        this.hero = hero;
    }

    @Override
    public Map getMap() {
        return map;
    }

    void setMap(Map map) {
        this.map = map;
    }

    void fire() {
        Fireball fireball = HeroLogic.fire(getHero());
        Map currentMap = stateManager.getGameState() == GameState.RUNNING ? map : crossover;
        if (fireball != null && !WeaponLogic.intersectsBrick(fireball, currentMap)) {
            currentMap.addFireball(fireball);
            soundManager.playFireball();
            Bowser bowser = currentMap.getBowser();
            if (bowser != null) {
                BowserLogic.canJump(bowser, Math.abs(bowser.getX() - fireball.getX()) >= (8 * 48));
            }
        }
    }

    void activateHammer() {
        if (getHero().getHammer() == null) {
            map.removeHammer();
        }

        if (map.getHammer() == null) {
            HeroLogic.activateHammer(getHero());
            Map currentMap = stateManager.getGameState() == GameState.RUNNING ? map : crossover;
            if (getHero().getHammer() != null) {
                getHero().setHammerActivated(true);
                currentMap.addHammer(getHero().getHammer());
            }
        }
    }

    @Override
    public boolean checkIfBowserDies() {
        if (map.getBowser().getHp() <= 0) {
            map.getAllObstacles().removeIf(brick -> brick instanceof GroundBrick);
            map.getGroundBricks().removeIf(brick -> brick instanceof GroundBrick);
            MapWorldLogic.stopBurning(map);
            soundManager.playBreakBrick();
            return true;
        }
        return false;
    }

    void throwHammer() {
        if (getHero().isHammerActivated()) {
            HeroLogic.throwHammer(getHero());
        }
    }

    boolean isGameOver() {
        return hero.getRemainingLives() == 0 || isTimeOver();
    }

    boolean isTimeOver() {
        return map != null && map.getRemainingTime() <= 0;
    }

    void decrementRemainingTime() {
        if (map != null) {
            map.setRemainingTime(map.getRemainingTime() - GameConstants.MAP_TIME_DECREMENT_SECONDS);
        }
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
        MapWorldLogic.drawMap(map, g2);
    }

    public void drawCrossover(Graphics2D g2) {
        MapWorldLogic.drawCrossover(crossover, g2);
    }

    int passMission() {
        if (hero.getX() >= map.getEndPoint().getX() && !map.getEndPoint().isTouched()) {
            map.getEndPoint().setTouched(true);
            soundManager.playFlagPole();
            return (int) getHero().getY() * 2;
        }
        return -1;
    }

    boolean endLevel() {
        return hero.getX() >= map.getEndPoint().getX() + 320;
    }

    void checkCollisions(GameEngine engine) {
        CollisionCoordinator.checkCollisions(new CollisionContext(this), engine);
    }

    @Override
    public void pauseInCheckPoint() {
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

    @Override
    public int calculateLosingCoins() {
        int n = isChecked ? 1 : 0;
        return (int) Math.floor((((n + 1) * getCoins()) + progressRate) / (n + 4));
    }

    public void addRevealedBrick(OrdinaryBrick ordinaryBrick) {
        getActiveMap().addRevealedBrick(ordinaryBrick);
    }

    public void addRevealedBrick(CoinBrick coinBrick) {
        getActiveMap().addRevealedBrick(coinBrick);
    }

    public int getRemainingTime() {
        return map != null ? (int) map.getRemainingTime() : 0;
    }

    void setChecked() {
        isChecked = false;
    }

    @Override
    public GameEngine getEngine() {
        return engine;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public double getXHero() {
        return xHero;
    }

    @Override
    public double getYHero() {
        return yHero;
    }

    @Override
    public void setXBeforeCrossover(double x) {
        xBeforeCrossover = x;
    }

    @Override
    public void setYBeforeCrossover(double y) {
        yBeforeCrossover = y;
    }

    @Override
    public double getXBeforeCrossover() {
        return xBeforeCrossover;
    }

    @Override
    public double getYBeforeCrossover() {
        return yBeforeCrossover;
    }

    private void playMapBackground() {
        if (userData.getWorldNumber() == MapSelection.BOSS_FIGHT.getWorldNumber()) {
            soundManager.playBossFightBackground();
        } else {
            soundManager.restartBackground();
        }
    }
}
