package graphic.manager;

import graphic.view.*;
import graphic.view.UIManager;
import model.Map;
import model.enemy.Enemy;
import model.enemy.Spiny;
import model.hero.Hero;
import model.hero.HeroType;
import repository.LoadGameRepository;
import repository.SaveGameRepository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GameEngine implements Runnable {

    private final static int WIDTH = 1268, HEIGHT = 708;

    private Hero hero;
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

    private final LoadGameRepository loadGameRepository = new LoadGameRepository();
    private final SaveGameRepository saveGameRepository = new SaveGameRepository();

    private GameEngine() {
        initial();
    }

    private void initial() {
        int selectedType = HeroType.MARIO;
        imageLoader = new ImageLoader(selectedType);
        InputManager inputManager = new InputManager(this);
        gameState = GameState.START_SCREEN;
        camera = new Camera();
        uiManager = new UIManager(this, WIDTH, HEIGHT);
        soundManager = new SoundManager();
        mapManager = new MapManager();

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
            mapManager.getHero().setMapPath(path);
        }
    }

    public void selectMapViaKeyboard() {
        String path = uiManager.selectMapViaKeyboard(selectedMap);
        if (path != null) {
            createMap(path);
            mapManager.getHero().setMapPath(path);
        }
    }

    public void changeSelectedMap(boolean up) {
        selectedMap = uiManager.changeSelectedMap(selectedMap, up);
    }

    private Map createMap(String path) {
        boolean loaded = mapManager.createMap(imageLoader, path);
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
            render();

            if (gameState != GameState.RUNNING) {
                timer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                mapManager.updateTime();
            }
        }
    }

    private void render() {
        uiManager.repaint();
    }

    private void gameLoop() {
        updateLocations();
        checkCollisions();
        updateCamera();

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
                if (!loadGameRepository.isFileNumberOneEmpty()) {
                    Hero hero = loadGameRepository.getHero(LoadGameRepository.FILE_1);
                    this.hero = hero;
                    mapManager.setHero(hero);
                    mapManager.setMap(createMap(hero.getMapPath()));
                    setGameState(GameState.STORE_SCREEN);
                } else {
                    startGame();
                }
            } else if (input == ButtonAction.SELECT && loadGameScreenSelection == LoadGameScreenSelection.LOAD_GAME_2) {
                if (!loadGameRepository.isFileNumberTwoEmpty()) {
                    Hero hero = loadGameRepository.getHero(LoadGameRepository.FILE_2);
                    this.hero = hero;
                    mapManager.setHero(hero);
                    mapManager.setMap(createMap(hero.getMapPath()));
                    setGameState(GameState.STORE_SCREEN);
                } else {
                    startGame();
                }
            } else if (input == ButtonAction.SELECT && loadGameScreenSelection == LoadGameScreenSelection.LOAD_GAME_3) {
                if (!loadGameRepository.isFileNumberThreeEmpty()) {
                    Hero hero = loadGameRepository.getHero(LoadGameRepository.FILE_3);
                    this.hero = hero;
                    mapManager.setHero(hero);
                    mapManager.setMap(createMap(hero.getMapPath()));
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
                mapManager.getHero().setType(HeroType.MARIO);
                setGameState(GameState.RUNNING);
            } else if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.LUIGI) {
                if (ownsLuigi()) {
                    mapManager.getHero().setType(HeroType.LUIGI);
                    setGameState(GameState.RUNNING);
                }
                if (canBuyLuigi()) {
                    mapManager.getHero().setCoins(mapManager.getHero().getCoins() - 25);
                    mapManager.getHero().getTypesOwned().add(HeroType.LUIGI);
                    mapManager.getHero().setType(HeroType.LUIGI);
                    setGameState(GameState.RUNNING);
                }
            } else if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.PRINCE_PEACH) {
                if (ownsPrincePeach()) {
                    mapManager.getHero().setType(HeroType.PRINCE_PEACH);
                    setGameState(GameState.RUNNING);
                }
                if (canBuyPrincePeach()) {
                    mapManager.getHero().setCoins(mapManager.getHero().getCoins() - 50);
                    mapManager.getHero().getTypesOwned().add(HeroType.PRINCE_PEACH);
                    mapManager.getHero().setType(HeroType.PRINCE_PEACH);
                    setGameState(GameState.RUNNING);
                }
            } else if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.ROSS) {
                if (ownsRoss()) {
                    mapManager.getHero().setType(HeroType.ROSS);
                    setGameState(GameState.RUNNING);
                }
                if (canBuyRoss()) {
                    mapManager.getHero().setCoins(mapManager.getHero().getCoins() - 40);
                    mapManager.getHero().getTypesOwned().add(HeroType.ROSS);
                    mapManager.getHero().setType(HeroType.ROSS);
                    setGameState(GameState.RUNNING);
                }
            } else if (input == ButtonAction.SELECT && storeScreenSelection == StoreScreenSelection.TOAD) {
                if (ownsToad()) {
                    mapManager.getHero().setType(HeroType.TOAD);
                    setGameState(GameState.RUNNING);
                }
                if (canBuyToad()) {
                    mapManager.getHero().setCoins(mapManager.getHero().getCoins() - 45);
                    mapManager.getHero().getTypesOwned().add(HeroType.TOAD);
                    mapManager.getHero().setType(HeroType.TOAD);
                    setGameState(GameState.RUNNING);
                }
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

            Hero hero = mapManager.getHero();
            ArrayList<Enemy> enemies = mapManager.getMap().getEnemies();

            if (input == ButtonAction.JUMP) {
                if (hero.getType() == HeroType.LUIGI) {
                    hero.jumpForLuigi(this);
                } else if (hero.getType() == HeroType.PRINCE_PEACH) {
                    hero.jumpForPrincePeach(this);
                } else {
                    hero.jump(this);
                }
            } else if (input == ButtonAction.MOVE_RIGHT) {
                if (hero.getType() == HeroType.PRINCE_PEACH) {
                    hero.moveForPrincePeach(true, camera);
                } else if (hero.getType() == HeroType.ROSS) {
                    hero.moveForRoss(true, camera);
                } else {
                    hero.move(true, camera);
                }
            } else if (input == ButtonAction.MOVE_LEFT) {
                if (hero.getType() == HeroType.PRINCE_PEACH) {
                    hero.moveForPrincePeach(false, camera);
                } else if (hero.getType() == HeroType.ROSS) {
                    hero.moveForRoss(false, camera);
                } else {
                    hero.move(false, camera);
                }
            } else if (input == ButtonAction.ACTION_COMPLETED) {
                hero.setVelX(0);
            } else if (input == ButtonAction.FIRE) {
                mapManager.fire(this);
            } else if (input == ButtonAction.PAUSE_RESUME) {
                pauseGame();
            }
        } else if (gameState == GameState.PAUSED) {
            if (input == ButtonAction.SELECT && pauseScreenSelection == PauseScreenSelection.GO_TO_MAIN_MENU) {
                mapManager.getHero().setMapPath(mapManager.getMap().getPath());
                saveGameRepository.addHero(mapManager.getHero(), 1);
                saveGameRepository.addMap(mapManager.getHero(), 1);
                gameState = GameState.START_SCREEN;
            } else if (input == ButtonAction.SELECT && pauseScreenSelection == PauseScreenSelection.SAVE_ON_FILE_1) {
                mapManager.getHero().setMapPath(mapManager.getMap().getPath());
                saveGameRepository.addHero(mapManager.getHero(), 1);
                saveGameRepository.addMap(mapManager.getHero(), 1);
            } else if (input == ButtonAction.SELECT && pauseScreenSelection == PauseScreenSelection.SAVE_ON_FILE_2) {
                mapManager.getHero().setMapPath(mapManager.getMap().getPath());
                saveGameRepository.addHero(mapManager.getHero(), 2);
                saveGameRepository.addMap(mapManager.getHero(), 2);
            } else if (input == ButtonAction.SELECT && pauseScreenSelection == PauseScreenSelection.SAVE_ON_FILE_3) {
                mapManager.getHero().setMapPath(mapManager.getMap().getPath());
                saveGameRepository.addHero(mapManager.getHero(), 3);
                saveGameRepository.addMap(mapManager.getHero(), 3);
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

    private void selectToStartOrLoad(boolean selectUp) {
        loadGameScreenSelection = loadGameScreenSelection.select(selectUp);
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
        if (gameState == GameState.RUNNING)
            return mapManager.isGameOver();
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
        if (hero != null) {
            return hero.getPoints();
        }
        return mapManager.getScore();
    }

    public int getRemainingLives() {
        if (hero != null) {
            return hero.getRemainingLives();
        }
        return mapManager.getRemainingLives();
    }

    public int getCoins() {
        if (hero != null) {
            return hero.getCoins();
        }
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

    public boolean canBuyLuigi() {
        return mapManager.getHero().getCoins() >= 25;
    }

    public boolean canBuyPrincePeach() {
        return mapManager.getHero().getCoins() >= 50;
    }

    public boolean canBuyRoss() {
        return mapManager.getHero().getCoins() >= 40;
    }

    public boolean canBuyToad() {
        return mapManager.getHero().getCoins() >= 45;
    }

    public boolean ownsLuigi() {
        for (int type : mapManager.getHero().getTypesOwned()) {
            if (type == HeroType.LUIGI) {
                return true;
            }
        }
        return false;
    }

    public boolean ownsPrincePeach() {
        for (int type : mapManager.getHero().getTypesOwned()) {
            if (type == HeroType.PRINCE_PEACH) {
                return true;
            }
        }
        return false;
    }

    public boolean ownsRoss() {
        for (int type : mapManager.getHero().getTypesOwned()) {
            if (type == HeroType.ROSS) {
                return true;
            }
        }
        return false;
    }

    public boolean ownsToad() {
        for (int type : mapManager.getHero().getTypesOwned()) {
            if (type == HeroType.TOAD) {
                return true;
            }
        }
        return false;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }
}


