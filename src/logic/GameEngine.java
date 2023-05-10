package logic;

import graphic.manager.*;
import graphic.view.*;
import graphic.view.UIManager;
import model.Map;
import model.hero.Hero;
import model.hero.HeroType;
import repository.LoadGameRepository;
import repository.SaveGameRepository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameEngine implements Runnable {

    private final static int WIDTH = 1268, HEIGHT = 708;
    private UserData userData;
    private MapManager mapManager;
    private UIManager uiManager;
    private SoundManager soundManager;
    private GameState gameState;
    private boolean isRunning;
    private Camera camera;
    private ImageLoader imageLoader;
    private Thread thread;
    private StartScreenSelection startScreenSelection = StartScreenSelection.LOAD_SCREEN;
    private LoadGameScreenSelection loadGameScreenSelection = LoadGameScreenSelection.NEW_GAME;
    private PauseScreenSelection pauseScreenSelection = PauseScreenSelection.GO_TO_MAIN_MENU;
    private StoreScreenSelection storeScreenSelection = StoreScreenSelection.MARIO;
    private int selectedMap = 0;

    private GameEngine() {
        initial();
    }

    private void initial() {
        imageLoader = ImageLoader.getInstance();
        InputManager inputManager = new InputManager(this);
        gameState = GameState.START_SCREEN;
        camera = new Camera();
        uiManager = new UIManager(this, WIDTH, HEIGHT);
        soundManager = new SoundManager();
        mapManager = MapManager.getInstance();
        userData = UserData.getInstance();

        JFrame frame = new JFrame("Super Mario Bros.");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/graphic/media/icon.jpg");
        frame.setIconImage(icon);
        frame.add(uiManager);
        frame.addKeyListener(inputManager);
        frame.addMouseListener(inputManager);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        start();
    }

    private synchronized void start() {
        if (isRunning)
            return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void reset() {
        resetCamera();
        setGameState(GameState.START_SCREEN);
    }

    public void resetCamera() {
        camera = new Camera();
        soundManager.restartBackground();
    }

    public void selectMapViaMouse() {
        String path = uiManager.selectMapViaMouse(uiManager.getMousePosition());
        if (path != null) {
            createMap(path);
            //     mapManager.getHero().setMapPath(path);
        }
    }

    public void selectMapViaKeyboard() {
        String path = uiManager.selectMapViaKeyboard(selectedMap);
        if (path != null) {
            createMap(path);
            //        mapManager.getHero().setMapPath(path);
        }
    }

    public void changeSelectedMap(boolean up) {
        selectedMap = uiManager.changeSelectedMap(selectedMap, up);
    }

    private void createMap(String path) {
        boolean loaded = mapManager.createMap(path);
        userData.setHero(mapManager.getHero());
        userData.setMap(mapManager.getMap());
        if (loaded) {
            setGameState(GameState.RUNNING);
            soundManager.restartBackground();
        } else {
            setGameState(GameState.START_SCREEN);
        }
    }

    private Map createMap(String path, Hero hero) {
        boolean loaded = mapManager.createMap(path, hero);
        userData.setHero(hero);
        userData.setMap(mapManager.getMap());
        if (loaded) {
            setGameState(GameState.RUNNING);
            soundManager.restartBackground();
            return mapManager.getMap();
        } else {
            setGameState(GameState.START_SCREEN);
        }
        return null;
    }

    @Override
    public void run() {
        renderLoop();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (isRunning && !thread.isInterrupted()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                if (gameState == GameState.RUNNING) {
                    gameLoop();
                }
                delta--;
            }

            if (gameState != GameState.RUNNING) {
                timer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                mapManager.updateTime();
            }
        }
    }

    private void renderLoop() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(12);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    updateCamera();
                    if (userData.getHero().getX() <= this.getCameraLocation().getX() && userData.getHero().getVelX() < 0) {
                        userData.getHero().setVelX(0);
                        userData.getHero().setX(this.getCameraLocation().getX());
                    }

                } catch (Exception ignored) {

                }

                render();
            }
        }).start();
    }

    private void render() {
        uiManager.repaint();
    }

    private void gameLoop() {
        updateLocations();
        checkCollisions();

        if (isGameOver()) {
            setGameState(GameState.GAME_OVER);
        }

        int missionPassed = passMission();
        if (missionPassed > -1) {
            mapManager.acquirePoints(missionPassed);
        } else if (mapManager.endLevel())
            setGameState(GameState.MISSION_PASSED);
    }

    private void updateCamera() {
        Hero hero = mapManager.getHero();
        double heroVelocityX = hero.getVelX();
        double shiftAmount = 0;

        if (heroVelocityX > 0 && hero.getX() - 600 > camera.getX()) {
            shiftAmount = heroVelocityX;
        }

        camera.moveCam(shiftAmount, 0);
    }

    private void updateLocations() {
        mapManager.updateLocations();
    }

    private void checkCollisions() {
        mapManager.checkCollisions(this);
    }

    public void receiveInput(ButtonAction input) throws IOException {

        if (gameState == GameState.START_SCREEN) {
            if (input == ButtonAction.SELECT && startScreenSelection == StartScreenSelection.LOAD_SCREEN) {
                setGameState(GameState.LOAD_GAME);
            } else if (input == ButtonAction.SELECT && startScreenSelection == StartScreenSelection.VIEW_ABOUT) {
                setGameState(GameState.ABOUT_SCREEN);
            } else if (input == ButtonAction.SELECT && startScreenSelection == StartScreenSelection.VIEW_HELP) {
                setGameState(GameState.HELP_SCREEN);
            } else if (input == ButtonAction.GO_UP) {
                selectOption(true);
            } else if (input == ButtonAction.GO_DOWN) {
                selectOption(false);
            }
        } else if (gameState == GameState.LOAD_GAME) {
            if (input == ButtonAction.SELECT && loadGameScreenSelection == LoadGameScreenSelection.NEW_GAME) {
                startGame();
            } else if (input == ButtonAction.SELECT && loadGameScreenSelection == LoadGameScreenSelection.LOAD_GAME_1) {
                if (!userData.getLoadGameRepository().isFileNumberOneEmpty()) {
                    loadGame(LoadGameRepository.FILE_1);
                    setGameState(GameState.STORE_SCREEN);
                } else {
                    startGame();
                }
            } else if (input == ButtonAction.SELECT && loadGameScreenSelection == LoadGameScreenSelection.LOAD_GAME_2) {
                if (!userData.getLoadGameRepository().isFileNumberTwoEmpty()) {
                    loadGame(LoadGameRepository.FILE_2);
                    setGameState(GameState.STORE_SCREEN);
                } else {
                    startGame();
                }
            } else if (input == ButtonAction.SELECT && loadGameScreenSelection == LoadGameScreenSelection.LOAD_GAME_3) {
                if (!userData.getLoadGameRepository().isFileNumberThreeEmpty()) {
                    loadGame(LoadGameRepository.FILE_3);
                    setGameState(GameState.STORE_SCREEN);
                } else {
                    startGame();
                }
            } else if (input == ButtonAction.GO_UP) {
                selectToStartOrLoad(true);
            } else if (input == ButtonAction.GO_DOWN) {
                selectToStartOrLoad(false);
            }
        } else if (gameState == GameState.STORE_SCREEN) {
            if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.MARIO) {
                loadNewHero(HeroType.MARIO);
                setGameState(GameState.RUNNING);
            } else if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.LUIGI) {
                if (userData.getTypesOwned()[HeroType.LUIGI]) {
                    loadNewHero(HeroType.LUIGI);
                } else if (userData.getHero().getCoins() >= 15) {
                    mapManager.getHero().setCoins(mapManager.getHero().getCoins() - 15);
                    buyAndLoadNewHero(HeroType.LUIGI);
                }
                setGameState(GameState.RUNNING);
            } else if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.PRINCE_PEACH) {
                if (userData.getTypesOwned()[HeroType.PRINCE_PEACH]) {
                    loadNewHero(HeroType.PRINCE_PEACH);
                } else if (userData.getHero().getCoins() >= 40) {
                    mapManager.getHero().setCoins(mapManager.getHero().getCoins() - 40);
                    buyAndLoadNewHero(HeroType.PRINCE_PEACH);
                }
                setGameState(GameState.RUNNING);
            } else if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.ROSS) {
                if (userData.getTypesOwned()[HeroType.ROSS]) {
                    loadNewHero(HeroType.ROSS);
                } else if (userData.getHero().getCoins() >= 30) {
                    mapManager.getHero().setCoins(mapManager.getHero().getCoins() - 30);
                    buyAndLoadNewHero(HeroType.ROSS);
                }
                setGameState(GameState.RUNNING);
            } else if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.TOAD) {
                if (userData.getTypesOwned()[HeroType.TOAD]) {
                    loadNewHero(HeroType.TOAD);
                } else if (userData.getHero().getCoins() >= 35) {
                    mapManager.getHero().setCoins(mapManager.getHero().getCoins() - 35);
                    buyAndLoadNewHero(HeroType.TOAD);
                }
                setGameState(GameState.RUNNING);
            } else if (input == ButtonAction.MOVE_LEFT) {
                selectHero(true);
            } else if (input == ButtonAction.MOVE_RIGHT) {
                selectHero(false);
            }
        } else if (gameState == GameState.MAP_SELECTION) {
            if (input == ButtonAction.SELECT) {
                selectMapViaKeyboard();
            } else if (input == ButtonAction.GO_UP) {
                changeSelectedMap(true);
            } else if (input == ButtonAction.GO_DOWN) {
                changeSelectedMap(false);
            }
        } else if (gameState == GameState.RUNNING) {
            if (input == ButtonAction.JUMP) {
                if (userData.getHero().getType() == HeroType.LUIGI) {
                    userData.getHero().jumpForLuigi(this);
                } else if (userData.getHero().getType() == HeroType.PRINCE_PEACH) {
                    userData.getHero().jumpForPrincePeach(this);
                } else {
                    userData.getHero().jump(this);
                }
            } else if (input == ButtonAction.MOVE_RIGHT) {
                if (userData.getHero().getType() == HeroType.PRINCE_PEACH) {
                    userData.getHero().moveForPrincePeach(true, camera);
                } else if (userData.getHero().getType() == HeroType.ROSS) {
                    userData.getHero().moveForRoss(true, camera);
                } else {
                    userData.getHero().move(true, camera);
                }
            } else if (input == ButtonAction.MOVE_LEFT) {
                if (userData.getHero().getType() == HeroType.PRINCE_PEACH) {
                    userData.getHero().moveForPrincePeach(false, camera);
                } else if (userData.getHero().getType() == HeroType.ROSS) {
                    userData.getHero().moveForRoss(false, camera);
                } else {
                    userData.getHero().move(false, camera);
                }
            } else if (input == ButtonAction.ACTION_COMPLETED) {
                userData.getHero().setVelX(0);
            } else if (input == ButtonAction.FIRE) {
                mapManager.fire(this);
            } else if (input == ButtonAction.PAUSE_RESUME) {
                pauseGame();
            }
        } else if (gameState == GameState.PAUSED) {
            if (input == ButtonAction.SELECT && pauseScreenSelection == PauseScreenSelection.GO_TO_MAIN_MENU) {
                saveGame(SaveGameRepository.FILE_1);
                gameState = GameState.START_SCREEN;
            } else if (input == ButtonAction.SELECT && pauseScreenSelection == PauseScreenSelection.SAVE_ON_FILE_1) {
                saveGame(SaveGameRepository.FILE_1);
            } else if (input == ButtonAction.SELECT && pauseScreenSelection == PauseScreenSelection.SAVE_ON_FILE_2) {
                saveGame(SaveGameRepository.FILE_2);
            } else if (input == ButtonAction.SELECT && pauseScreenSelection == PauseScreenSelection.SAVE_ON_FILE_3) {
                saveGame(SaveGameRepository.FILE_3);
            } else if (input == ButtonAction.GO_UP) {
                selectToResume(true);
            } else if (input == ButtonAction.GO_DOWN) {
                selectToResume(false);
            } else if (input == ButtonAction.PAUSE_RESUME) {
                pauseGame();
            }
        } else if (gameState == GameState.GAME_OVER && input == ButtonAction.GO_TO_START_SCREEN) {
            reset();
        } else if (gameState == GameState.MISSION_PASSED && input == ButtonAction.GO_TO_START_SCREEN) {
            reset();
        }

        if (input == ButtonAction.GO_TO_START_SCREEN) {
            setGameState(GameState.START_SCREEN);
        }
    }

    private void buyAndLoadNewHero(int type) {
        userData.getTypesOwned()[type] = true;
        loadNewHero(type);
    }

    private void loadNewHero(int type) {
        mapManager.getHero().setType(type);
        mapManager.getHero().getHeroForm().setType(type);
        imageLoader.setHeroType(type);
        imageLoader.setHeroForms(type);
        userData.getHero().setType(type);
    }

    private void selectToStartOrLoad(boolean selectUp) {
        loadGameScreenSelection = loadGameScreenSelection.select(selectUp);
    }

    private void loadGame(int fileNumber) throws IOException {
        userData = userData.getLoadGameRepository().getUserData(fileNumber);
        userData.setHero(userData.getHero());
        userData.setTypesOwned(userData.getTypesOwned());
        mapManager.setMap(createMap(userData.getHero().getMapPath(), userData.getHero()));
        mapManager.setHero(userData.getHero());
    }

    private void saveGame(int fileNumber) {
        mapManager.getHero().setMapPath(mapManager.getMap().getPath());
        userData.getSaveGameRepository().addUserData(userData, fileNumber);
    }

    private void selectOption(boolean selectUp) {
        startScreenSelection = startScreenSelection.select(selectUp);
    }

    private void selectToResume(boolean selectUp) {
        pauseScreenSelection = pauseScreenSelection.select(selectUp);
    }

    private void selectHero(boolean selectLeft) {
        storeScreenSelection = storeScreenSelection.select(selectLeft);
    }

    private void startGame() {
        if (gameState != GameState.GAME_OVER) {
            setGameState(GameState.MAP_SELECTION);
        }
    }

    private void pauseGame() {
        if (gameState == GameState.RUNNING) {
            setGameState(GameState.PAUSED);
            soundManager.pauseBackground();
        } else if (gameState == GameState.PAUSED) {
            setGameState(GameState.RUNNING);
            soundManager.resumeBackground();
        }
    }

    public void shakeCamera() {
        camera.shakeCamera();
    }

    private boolean isGameOver() {
        if (gameState == GameState.RUNNING) {
            return mapManager.isGameOver();
        }
        return false;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public GameState getGameState() {
        return gameState;
    }

    public StartScreenSelection getStartScreenSelection() {
        return startScreenSelection;
    }

    public LoadGameScreenSelection getLoadGameScreenSelection() {
        return loadGameScreenSelection;
    }

    public PauseScreenSelection getPauseScreenSelection() {
        return pauseScreenSelection;
    }

    public StoreScreenSelection getStoreScreenSelection() {
        return storeScreenSelection;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getScore() {
        return mapManager.getScore();
    }

    public int getRemainingLives() {
        return mapManager.getRemainingLives();
    }

    public int getCoins() {
        return mapManager.getCoins();
    }

    public int getSelectedMap() {
        return selectedMap;
    }

    public void drawMap(Graphics2D g2) {
        mapManager.drawMap(g2);
    }

    public Point getCameraLocation() {
        return new Point((int) camera.getX(), (int) camera.getY());
    }

    private int passMission() {
        return mapManager.passMission();
    }

    public void playCoin() {
        soundManager.playCoin();
    }

    public void playOneUp() {
        soundManager.playOneUp();
    }

    public void playSuperMushroom() {
        soundManager.playSuperMushroom();
    }

    public void playHeroDies() {
        soundManager.playHeroDies();
    }

    public void playGameOver() {
        soundManager.playGameOver();
    }

    public void playJump() {
        soundManager.playJump();
    }

    public void playFireFlower() {
        soundManager.playFireFlower();
    }

    public void playFireball() {
        soundManager.playFireball();
    }

    public void playStomp() {
        soundManager.playStomp();
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public static void main(String... args) {
        new GameEngine();
    }

    public int getRemainingTime() {
        return mapManager.getRemainingTime();
    }

    public UserData getUserData() {
        return userData;
    }
}