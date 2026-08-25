package SuperMario.logic.collision;

import SuperMario.logic.GameEngine;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;

import java.util.Timer;

public interface MapCollisionCallbacks {

    Map getActiveMap();

    Map getMap();

    Hero getHero();

    GameEngine getEngine();

    boolean isChecked();

    double getXHero();

    double getYHero();

    void setXBeforeCrossover(double x);

    void setYBeforeCrossover(double y);

    double getXBeforeCrossover();

    double getYBeforeCrossover();

    void resetCurrentMap(GameEngine engine);

    void reLoadCheckPoint(double x, double y);

    void createCrossover(String path, Hero hero);

    void acquirePoints(int point);

    boolean checkIfBowserDies();

    int calculateLosingCoins();

    void pauseInCheckPoint();

    void setGrabTimer(Timer timer);

    Timer getGrabTimer();
}
