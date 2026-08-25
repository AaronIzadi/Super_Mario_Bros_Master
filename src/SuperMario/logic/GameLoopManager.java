package SuperMario.logic;

import SuperMario.config.GameConstants;
import SuperMario.graphic.view.UI.UIManager;
import SuperMario.graphic.view.states.GameState;
import SuperMario.logic.timer.EntityTimerLogic;
import SuperMario.graphic.view.states.MapSelection;

public class GameLoopManager implements Runnable {

    private final GameEngine engine;
    private final MapManager mapManager;
    private final CameraManager cameraManager;
    private final GameStateManager stateManager;
    private final UIManager uiManager;
    private Thread thread;
    private boolean isRunning;

    public GameLoopManager(GameEngine engine,
                           MapManager mapManager,
                           CameraManager cameraManager,
                           GameStateManager stateManager,
                           InputManager inputManager,
                           UIManager uiManager) {
        this.engine = engine;
        this.mapManager = mapManager;
        this.cameraManager = cameraManager;
        this.stateManager = stateManager;
        this.uiManager = uiManager;
    }

    synchronized void start() {
        if (isRunning) return;
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        renderLoop();
        long lastTime = System.nanoTime();
        double ns = 1_000_000_000 / GameConstants.TICKS_PER_SECOND;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (isRunning && !thread.isInterrupted()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                if (stateManager.getGameState() == GameState.RUNNING || stateManager.getGameState() == GameState.CROSSOVER) {
                    gameLoop();
                }
                delta--;
            }

            if (stateManager.getGameState() != GameState.RUNNING) {
                timer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - timer > GameConstants.MAP_TIMER_INTERVAL_MS) {
                timer += GameConstants.MAP_TIMER_INTERVAL_MS;
                if (stateManager.getGameState() == GameState.RUNNING) {
                    mapManager.decrementRemainingTime();
                }
            }
        }
    }

    private void renderLoop() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(GameConstants.RENDER_SLEEP_MS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (stateManager.getGameState() == GameState.RUNNING) {
                        cameraManager.updateCamera();
                        if (engine.getUserData().getHero().getX() <= engine.getCameraManager().getCameraLocation().getX() && engine.getUserData().getHero().getVelX() < 0) {
                            engine.getUserData().getHero().setVelX(0);
                            engine.getUserData().getHero().setX(engine.getCameraManager().getCameraLocation().getX());
                        }
                    }
                } catch (RuntimeException ignored) {
                    // Hero or camera may be unavailable during transient game states.
                }
                render();
            }
        }).start();
    }

    private void render() {
        uiManager.repaint();
    }

    private void gameLoop() {
        EntityTimerLogic.tick(mapManager.getMap(), engine);

        if (stateManager.getGameState() != GameState.CROSSOVER) {
            mapManager.updateLocations();
            mapManager.checkCollisions(engine);
        }

        if (engine.isGameOver()) {
            stateManager.setGameState(GameState.GAME_OVER);
            engine.getSoundManager().pauseBackground();
        }

        if (stateManager.getGameState() == GameState.CROSSOVER) {
            mapManager.updateLocationsForCrossover();
            mapManager.checkCollisions(engine);
        }

        if (stateManager.getGameState() == GameState.RUNNING) {
            int missionPassed = mapManager.passMission();
            if (missionPassed > -1) {
                engine.getSoundManager().pauseBackground();
                mapManager.acquirePoints(missionPassed);
            } else if (mapManager.endLevel()) {
                engine.getSoundManager().pauseBackground();
                if (engine.getUserData().getWorldNumber() != MapSelection.BOSS_FIGHT.getWorldNumber()) {
                    engine.getSoundManager().playStageClear();
                }
                stateManager.setGameState(GameState.MISSION_PASSED);
            }
        }
    }
}
