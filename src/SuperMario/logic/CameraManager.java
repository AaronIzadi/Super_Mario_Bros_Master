package SuperMario.logic;

import SuperMario.graphic.manager.Camera;
import SuperMario.graphic.view.states.MapSelection;
import SuperMario.model.hero.Hero;

import java.awt.*;

public class CameraManager {

    private Camera mainCamera;
    private Camera crossoverCamera;
    private final GameEngine engine;

    public CameraManager(GameEngine engine) {
        mainCamera = new Camera();
        crossoverCamera = new Camera();
        this.engine = engine;
    }

    Camera getMainCamera() {
        return mainCamera;
    }

    Camera getCrossoverCamera() {
        return crossoverCamera;
    }

    public void shakeCamera() {
        mainCamera.shakeCamera();
    }

    void resetCamera() {
        mainCamera = new Camera();
        if (engine.getUserData().getWorldNumber() != MapSelection.BOSS_FIGHT.getWorldNumber()) {
            engine.getSoundManager().restartBackground();
        }
    }

    void reLoadCheckPoint(double x) {
        mainCamera.setX(x - 300);
        if (engine.getUserData().getWorldNumber() == MapSelection.BOSS_FIGHT.getWorldNumber()) {
            engine.getSoundManager().playBossFightBackground();
        } else {
            engine.getSoundManager().restartBackground();
        }
    }

    void updateCamera() {
        Hero hero = engine.getMapManager().getHero();
        double heroVelocityX = hero.getVelX();
        double shiftAmount = 0;

        if (heroVelocityX > 0 && hero.getX() - 600 > mainCamera.getX()) {
            shiftAmount = heroVelocityX;
        }

        mainCamera.moveCam(shiftAmount, 0);
    }

    public Point getCameraLocation() {
        return new Point((int) mainCamera.getX(), (int) mainCamera.getY());
    }

    public Point getCrossoverCameraLocation() {
        return new Point((int) crossoverCamera.getX(), (int) crossoverCamera.getY());
    }
}
