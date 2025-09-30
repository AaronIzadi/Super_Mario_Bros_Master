package SuperMario.logic;

import SuperMario.graphic.view.UI.UIManager;
import SuperMario.graphic.view.states.GameState;
import SuperMario.graphic.view.states.MapSelection;
import SuperMario.graphic.view.states.StoreScreenSelection;
import SuperMario.input.ImageLoader;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.HeroForm;

import javax.swing.*;
import java.io.IOException;

public class GameEngine {

    private GameLoopManager loopManager;
    private InputManager inputManager;
    private UIManager uiManager;
    private MapManager mapManager;
    private UserData userData;
    private CameraManager cameraManager;
    private SoundManager soundManager;
    private GameStateManager stateManager;
    private ImageLoader imageLoader;

    private boolean isMute = false;

    private static final GameEngine instance = new GameEngine();


    private GameEngine() {
        initial();
    }

    public static GameEngine getInstance() {
        return instance;
    }

    private void initial() {
        userData = UserData.getInstance();
        imageLoader = ImageLoader.getInstance();
        soundManager = new SoundManager();
        stateManager = new GameStateManager();
        cameraManager = new CameraManager(this);
        mapManager = MapManager.getInstance();
        mapManager.initialize(this);

        inputManager = new InputManager(this, userData, mapManager, cameraManager);
        uiManager = new UIManager(this, 1268, 708);
        loopManager = new GameLoopManager(this, mapManager, cameraManager, stateManager, inputManager, uiManager);

        JFrame frame = new JFrame("Super Mario Bros.");
        frame.setIconImage(imageLoader.getIcon());
        frame.add(uiManager);
        frame.addKeyListener(inputManager.getInputReceiver());
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void start() {
        loopManager.start();
    }

    void startGame(int worldNumber) {
        if (stateManager.getGameState() != GameState.GAME_OVER) {
            mapManager.selectMap(worldNumber);
        }
    }

    void pauseInCheckPoint() {
        if (stateManager.getGameState() == GameState.RUNNING) {
            stateManager.setGameState(GameState.CHECKPOINT);
            soundManager.pauseBackground();
        } else if (stateManager.getGameState() == GameState.CHECKPOINT) {
            stateManager.setGameState(GameState.RUNNING);
            soundManager.resumeBackground();
        }
    }

    void pauseGame() {
        if (stateManager.getGameState() == GameState.RUNNING) {
            stateManager.setGameState(GameState.PAUSED);
            soundManager.pauseBackground();
        } else if (stateManager.getGameState() == GameState.PAUSED) {
            stateManager.setGameState(GameState.RUNNING);
            soundManager.resumeBackground();
        }
    }

    void checkAndThenLoadFile(int fileId) throws IOException {
        if (2 < fileId || fileId < 0) {
            startGame(fileId);
            return;
        }
        if (!userData.getLoadGameRepository().isFileEmpty(fileId)) {
            loadGame(fileId);
            stateManager.setGameState(GameState.STORE_SCREEN);
        } else {
            startGame(MapSelection.WORLD_1.getWorldNumber());
        }
    }

    void checkIfThenBuyHero(StoreScreenSelection selection) {
        int heroId = selection.getColumnNumber();
        Hero hero = userData.getHero();

        if (userData.getTypesOwned()[heroId]) {
            loadNewHero(heroId);
        } else if (hero.getCoins() >= selection.getHeroPrice()) {
            hero.setCoins(hero.getCoins() - selection.getHeroPrice());
            buyAndLoadNewHero(heroId);
        }
        stateManager.setGameState(GameState.RUNNING);
    }

    void loadGame(int fileNumber) throws IOException {
        userData = userData.getLoadGameRepository().getUserData(fileNumber);
        userData.setHero(userData.getHero());
        userData.setTypesOwned(userData.getTypesOwned());
        mapManager.setMap(mapManager.createMap(userData.getMapPath(), userData.getHero()));
        mapManager.setHero(userData.getHero());
        cameraManager.resetCamera();
    }

    void buyAndLoadNewHero(int type) {
        userData.getTypesOwned()[type] = true;
        loadNewHero(type);
    }

    void loadNewHero(int type) {
        mapManager.getHero().setType(type);
        mapManager.getHero().getHeroForm().setHeroType(type);
        imageLoader.setHeroType(type);
        userData.getHero().setType(type);
        int heroFormId = userData.getHero().isSuper() ? 1 : 0;
        if (userData.getHero().getHeroForm().ifCanShootFire()) {
            heroFormId = 2;
        }
        userData.getHero().setHeroForm(
                new HeroForm(
                        imageLoader.getHeroLeftFrames(heroFormId),
                        imageLoader.getHeroRightFrames(heroFormId),
                        userData.getHero().isSuper(),
                        userData.getHero().getHeroForm().ifCanShootFire(),
                        type));
    }

    void saveGame(int fileNumber) {
        userData.setMapPath(mapManager.getMap().getPath());
        userData.getSaveGameRepository().addUserData(userData, fileNumber);
    }

    void reset() {
        userData.clear();
        cameraManager.resetCamera();
        stateManager.setGameState(GameState.START_SCREEN);
        soundManager.pauseBackGround();
    }

    void loadNextLevel(int worldNumber) {
        userData.setWorldNumber(worldNumber);
        userData.setMapPath(inputManager.getMapSelection().getMapPath(worldNumber));
        mapManager.setMap(mapManager.createMap(inputManager.getMapSelection().getMapPath(worldNumber), userData.getHero()));
        mapManager.resetCurrentMap(this);
        mapManager.updateLocations();
        cameraManager.updateCamera();
        mapManager.setChecked();
        if (worldNumber == MapSelection.BOSS_FIGHT.getWorldNumber()) {
            soundManager.playBossFightBackground();
        } else {
            soundManager.playBackground();
        }
        stateManager.setGameState(GameState.RUNNING);
    }

    boolean isGameOver() {
        if (stateManager.getGameState() == GameState.RUNNING) {
            return mapManager.isGameOver();
        }
        return false;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public GameStateManager getStateManager() {
        return stateManager;
    }

    public UserData getUserData() {
        return userData;
    }


    public ImageLoader getImageLoader() {
        return imageLoader;
    }


    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }
}