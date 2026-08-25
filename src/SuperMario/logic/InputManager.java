package SuperMario.logic;

import SuperMario.graphic.manager.Camera;
import SuperMario.graphic.manager.InputReceiver;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.graphic.view.states.*;

import java.io.IOException;

public class InputManager {

    private final InputReceiver inputReceiver;
    private final GameEngine engine;
    private final GameStateManager stateManager;
    private final UserData userData;
    private final MapManager mapManager;
    private final CameraManager cameraManager;
    private final MapSelection mapSelection;
    private StartScreenSelection startScreenSelection;
    private LoadGameScreenSelection loadGameScreenSelection;
    private PauseScreenSelection pauseScreenSelection;
    private StoreScreenSelection storeScreenSelection;
    private CheckPointSelection checkPointSelection;


    public InputManager(GameEngine engine, UserData userData, MapManager mapManager, CameraManager cameraManager) {
        this.inputReceiver = InputReceiver.getInstance();
        this.engine = engine;
        this.userData = userData;
        this.mapManager = mapManager;
        this.cameraManager = cameraManager;
        this.stateManager = engine.getStateManager();
        this.startScreenSelection = StartScreenSelection.LOAD_SCREEN;
        this.loadGameScreenSelection = LoadGameScreenSelection.NEW_GAME;
        this.pauseScreenSelection = PauseScreenSelection.GO_TO_MAIN_MENU;
        this.storeScreenSelection = StoreScreenSelection.MARIO;
        this.checkPointSelection = CheckPointSelection.YES;
        this.mapSelection = MapSelection.WORLD_1;
    }

    public void receiveInput() throws IOException {

        if (stateManager.getGameState() == GameState.START_SCREEN) {

            if (inputReceiver.isEnter()) {
                switch (startScreenSelection) {
                    case LOAD_SCREEN:
                        stateManager.setGameState(GameState.LOAD_GAME);
                        break;
                    case VIEW_ABOUT:
                        stateManager.setGameState(GameState.ABOUT_SCREEN);
                        break;
                    case VIEW_HELP:
                        stateManager.setGameState(GameState.HELP_SCREEN);
                        break;
                }
            } else if (inputReceiver.isUp()) {
                selectOptionsOnStart(true);
            } else if (inputReceiver.isDown()) {
                selectOptionsOnStart(false);
            }

        } else if (stateManager.getGameState() == GameState.LOAD_GAME) {

            if (inputReceiver.isEnter()) {
                switch (loadGameScreenSelection) {
                    case NEW_GAME:
                        engine.startGame(MapSelection.WORLD_1.getWorldNumber());
                        break;
                    case LOAD_GAME_1:
                        engine.checkAndThenLoadFile(0);
                        break;
                    case LOAD_GAME_2:
                        engine.checkAndThenLoadFile(1);
                        break;
                    case LOAD_GAME_3:
                        engine.checkAndThenLoadFile(2);
                        break;
                }
            } else if (inputReceiver.isUp()) {
                selectToStartOrLoad(true);
            } else if (inputReceiver.isDown()) {
                selectToStartOrLoad(false);
            }

        } else if (stateManager.getGameState() == GameState.STORE_SCREEN) {

            if (inputReceiver.isEnter()) {
                engine.checkIfThenBuyHero(storeScreenSelection);
            } else if (inputReceiver.isLeft()) {
                selectHero(true);
            } else if (inputReceiver.isRight()) {
                selectHero(false);
            }

        } else if (stateManager.getGameState() == GameState.RUNNING || stateManager.getGameState() == GameState.CROSSOVER) {

            if (inputReceiver.isUpAndDownSelected()) {
                mapManager.activateAxe();
            } else if (inputReceiver.isUp()) {
                HeroLogic.jump(userData.getHero());
                mapManager.getSoundManager().playJump();
            } else if (inputReceiver.isDown()) {
                HeroLogic.sit(userData.getHero());
            } else if (inputReceiver.isRight()) {
                if (userData.getHero().isGrabbed()) {
                    userData.getHero().addNumberOfTryToEscape();
                } else {
                    Camera currentCam = stateManager.getGameState() == GameState.RUNNING ? cameraManager.getMainCamera() : cameraManager.getCrossoverCamera();
                    HeroLogic.move(userData.getHero(), true, currentCam);
                }
            } else if (inputReceiver.isLeft()) {
                if (userData.getHero().isGrabbed()) {
                    userData.getHero().addNumberOfTryToEscape();
                } else {
                    Camera currentCam = stateManager.getGameState() == GameState.RUNNING ? cameraManager.getMainCamera() : cameraManager.getCrossoverCamera();
                    HeroLogic.move(userData.getHero(), false, currentCam);
                }
            } else if (inputReceiver.isEmpty()) {
                userData.getHero().setVelX(0);
                HeroLogic.getUp(userData.getHero());
                if (userData.getHero().isSuper()) {
                    userData.getHero().getDimension().height = 96;
                }
            } else if (inputReceiver.isSpace()) {
                if (userData.getHero().isAxeActivated()) {
                    mapManager.throwAxe();
                } else {
                    mapManager.fire();
                }
            } else if (inputReceiver.isEscape()) {
                engine.pauseGame();
            }

        } else if (stateManager.getGameState() == GameState.CHECKPOINT) {

            if (inputReceiver.isEnter()) {
                mapManager.handleCheckPoint(checkPointSelection == CheckPointSelection.YES);
                engine.pauseInCheckPoint();
            } else if (inputReceiver.isLeft()) {
                selectToSaveOnCheckPoint(true);
            } else if (inputReceiver.isRight()) {
                selectToSaveOnCheckPoint(false);
            }

        } else if (stateManager.getGameState() == GameState.PAUSED) {

            if (inputReceiver.isEnter()) {
                switch (pauseScreenSelection) {
                    case GO_TO_MAIN_MENU:
                        engine.saveGame(0);
                        stateManager.setGameState(GameState.START_SCREEN);
                        break;
                    case SAVE_ON_FILE_1:
                        engine.saveGame(0);
                        break;
                    case SAVE_ON_FILE_2:
                        engine.saveGame(1);
                        break;
                    case SAVE_ON_FILE_3:
                        engine.saveGame(2);
                        break;
                    case MUTE_BACKGROUND_SOUND: {
                        if (engine.isMute()) {
                            engine.getSoundManager().resumeBackground();
                        } else {
                            engine.getSoundManager().pauseBackGround();
                        }
                        stateManager.setGameState(GameState.RUNNING);
                        ;
                        break;
                    }
                }
            } else if (inputReceiver.isUp()) {
                selectToResume(true);
            } else if (inputReceiver.isDown()) {
                selectToResume(false);
            } else if (inputReceiver.isEscape()) {
                engine.pauseGame();
            }

        } else if (stateManager.getGameState() == GameState.GAME_OVER && inputReceiver.isEscape()) {
            stateManager.setGameState(GameState.RUNNING);
            ;
            engine.reset();
        } else if (stateManager.getGameState() == GameState.MISSION_PASSED) {

            if (inputReceiver.isEnter()) {
                int nextWorld;
                if (userData.getWorldNumber() == 0) {
                    nextWorld = MapSelection.WORLD_2.getWorldNumber();
                    engine.loadNextLevel(nextWorld);
                } else if (userData.getWorldNumber() == 1) {
                    nextWorld = MapSelection.WORLD_3.getWorldNumber();
                    engine.loadNextLevel(nextWorld);
                } else if (userData.getWorldNumber() == 2) {
                    nextWorld = MapSelection.BOSS_FIGHT.getWorldNumber();
                    engine.loadNextLevel(nextWorld);
                }
            } else if (inputReceiver.isEscape()) {
                engine.reset();
            }
            engine.getSoundManager().pauseBackGround();

        }

        boolean isGameRunning = stateManager.getGameState() == GameState.RUNNING || stateManager.getGameState() == GameState.CROSSOVER
                || stateManager.getGameState() == GameState.CHECKPOINT || stateManager.getGameState() == GameState.PAUSED;

        if (!isGameRunning && inputReceiver.isEscape()) {
            stateManager.setGameState(GameState.START_SCREEN);
        }
    }

    public MapSelection getMapSelection() {
        return mapSelection;
    }

    public InputReceiver getInputReceiver() {
        return inputReceiver;
    }

    private void selectOptionsOnStart(boolean selectUp) {
        startScreenSelection = startScreenSelection.select(selectUp);
    }

    private void selectToSaveOnCheckPoint(boolean selectLeft) {
        checkPointSelection = checkPointSelection.select(selectLeft);
    }

    private void selectToResume(boolean selectUp) {
        pauseScreenSelection = pauseScreenSelection.select(selectUp);
    }

    private void selectHero(boolean selectLeft) {
        storeScreenSelection = storeScreenSelection.select(selectLeft);
    }

    private void selectToStartOrLoad(boolean selectUp) {
        loadGameScreenSelection = loadGameScreenSelection.select(selectUp);
    }

    public CheckPointSelection getCheckPointSelection() {
        return checkPointSelection;
    }

    public StoreScreenSelection getStoreScreenSelection() {
        return storeScreenSelection;
    }

    public PauseScreenSelection getPauseScreenSelection() {
        return pauseScreenSelection;
    }

    public LoadGameScreenSelection getLoadGameScreenSelection() {
        return loadGameScreenSelection;
    }

    public StartScreenSelection getStartScreenSelection() {
        return startScreenSelection;
    }
}
