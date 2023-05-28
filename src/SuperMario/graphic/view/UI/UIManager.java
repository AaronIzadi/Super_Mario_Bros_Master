package SuperMario.graphic.view.UI;

import SuperMario.input.FontLoader;

import SuperMario.graphic.view.states.GameState;
import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.model.hero.HeroType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UIManager extends JPanel {

    private final GameEngine engine;
    private final Font gameFont;
    private final BufferedImage startScreenImage, aboutScreenImage, helpScreenImage, checkPointScreen, gameOverScreen, storeScreenImage, loadGameScreen, pauseScreen;
    private final BufferedImage heartIcon;
    private final BufferedImage coinIcon;
    private final BufferedImage selectIcon;

    public UIManager(GameEngine engine, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));

        this.engine = engine;
        ImageLoader loader = ImageLoader.getInstance();

        this.heartIcon = loader.getHeartIcon();
        this.coinIcon = loader.getCoinIcon();
        this.selectIcon = loader.getSelectIcon();
        this.startScreenImage = loader.getStartScreenImage();
        this.helpScreenImage = loader.getHelpScreenImage();
        this.aboutScreenImage = loader.getAboutScreenImage();
        this.gameOverScreen = loader.getGameOverScreen();
        this.storeScreenImage = loader.getStoreScreenImage();
        this.loadGameScreen = loader.getLoadGameScreen();
        this.pauseScreen = loader.getPauseScreen();
        this.checkPointScreen = loader.getCheckPoint();

        this.gameFont = new FontLoader().getFont();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        GameState gameState = engine.getGameState();

        if (gameState == GameState.START_SCREEN) {
            drawStartScreen(g2);
        } else if (gameState == GameState.LOAD_GAME) {
            drawLoadGameScreen(g2);
        } else if (gameState == GameState.STORE_SCREEN) {
            drawStoreScreen(g2);
        } else if (gameState == GameState.ABOUT_SCREEN) {
            drawAboutScreen(g2);
        } else if (gameState == GameState.HELP_SCREEN) {
            drawHelpScreen(g2);
        } else if (gameState == GameState.GAME_OVER) {
            drawGameOverScreen(g2);
        } else if (gameState == GameState.CROSSOVER) {
            Point camLocation = engine.getCrossoverCameraLocation();
            g2.translate(-camLocation.x, -camLocation.y);
            engine.drawCrossover(g2);
            g2.translate(camLocation.x, camLocation.y);
            drawPoints(g2);
            drawRemainingLives(g2);
            drawAcquiredCoins(g2);
        } else {
            Point camLocation = engine.getCameraLocation();
            g2.translate(-camLocation.x, -camLocation.y);
            engine.drawMap(g2);
            g2.translate(camLocation.x, camLocation.y);

            drawPoints(g2);
            drawRemainingLives(g2);
            drawAcquiredCoins(g2);
            drawRemainingTime(g2);
            drawWorldNumber(g2);

            if (gameState == GameState.PAUSED) {
                drawPauseScreen(g2);
            } else if (gameState == GameState.CHECKPOINT) {
                drawCheckPointScreen(g2);
            } else if (gameState == GameState.MISSION_PASSED) {
                drawVictoryScreen(g2);
            }
        }
        g2.dispose();
    }

    private void drawVictoryScreen(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(40f));
        g2.setColor(Color.WHITE);
        String displayedStr = "YOU WON!";
        String nextLine = "Press enter to continue.";
        int stringLength = g2.getFontMetrics().stringWidth(displayedStr);
        int nextLineLength = g2.getFontMetrics().stringWidth(nextLine);
        g2.drawString(displayedStr, (getWidth() - stringLength) / 2, 300);
        g2.drawString(nextLine, (getWidth() - nextLineLength) / 2, 400);
    }

    private void drawCheckPointScreen(Graphics2D g2) {
        int col = engine.getCheckPointSelection().getColumnNumber();
        g2.drawImage(checkPointScreen, 0, 0, null);
        g2.drawImage(selectIcon, col * 350 + 320, 360, null);
    }

    private void drawHelpScreen(Graphics2D g2) {
        g2.drawImage(helpScreenImage, 0, 0, null);
    }

    private void drawAboutScreen(Graphics2D g2) {
        g2.drawImage(aboutScreenImage, 0, 0, null);
    }

    private void drawGameOverScreen(Graphics2D g2) {
        g2.drawImage(gameOverScreen, 0, 0, null);
        g2.setFont(gameFont.deriveFont(50f));
        g2.setColor(new Color(238, 28, 46));
        String acquiredPoints;
        acquiredPoints = "Score:" + engine.getScore();
        int stringLength = g2.getFontMetrics().stringWidth(acquiredPoints);
        int stringHeight = g2.getFontMetrics().getHeight();
        g2.drawString(acquiredPoints, (getWidth() - stringLength) / 2, getHeight() - stringHeight * 4);
    }

    private void drawStoreScreen(Graphics2D g2) {
        int column = engine.getStoreScreenSelection().getColumnNumber();
        g2.drawImage(storeScreenImage, 0, 0, null);
        g2.drawImage(selectIcon, column * 225 + 70, 255, null);

        //Coins
        g2.setFont(gameFont.deriveFont(30f));
        g2.setColor(Color.WHITE);
        String coins = "" + engine.getCoins();
        g2.drawString(coins, 85, 70);

        //Price:

        //Luigi
        String buy = "Buy for";
        if (!engine.getUserData().getTypesOwned()[HeroType.LUIGI]) {
            setFontAndColor(g2);
            g2.drawString(buy, 320, 500);

            setFontAndColor(g2);
            g2.drawString("15", 370, 535);
        }
        //Prince Peach
        if (!engine.getUserData().getTypesOwned()[HeroType.PRINCE_PEACH]) {
            setFontAndColor(g2);
            g2.drawString(buy, 560, 500);

            setFontAndColor(g2);
            g2.drawString("40", 610, 535);
        }
        //Ross
        if (!engine.getUserData().getTypesOwned()[HeroType.ROSS]) {
            setFontAndColor(g2);
            g2.drawString(buy, 780, 500);

            setFontAndColor(g2);
            g2.drawString("30", 830, 535);
        }
        //Toad
        if (!engine.getUserData().getTypesOwned()[HeroType.TOAD]) {
            setFontAndColor(g2);
            g2.drawString(buy, 1000, 500);

            setFontAndColor(g2);
            g2.drawString("35", 1050, 535);
        }
    }

    private void setFontAndColor(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(20f));
        g2.setColor(Color.WHITE);
    }

    private void drawPauseScreen(Graphics2D g2) {
        int row = engine.getPauseScreenSelection().getLineNumber();
        g2.drawImage(pauseScreen, 0, 0, null);
        g2.drawImage(selectIcon, 285, row * 95 + 195, null);
    }

    private void drawRemainingTime(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(25f));
        g2.setColor(Color.WHITE);
        String displayedStr = "TIME:" + engine.getRemainingTime();
        g2.drawString(displayedStr, 850, 50);
    }

    private void drawAcquiredCoins(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(30f));
        g2.setColor(Color.WHITE);
        String displayedStr;

        displayedStr = "" + engine.getCoins();

        g2.drawImage(coinIcon, getWidth() - 115, 10, null);
        g2.drawString(displayedStr, getWidth() - 65, 50);
    }

    private void drawRemainingLives(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(30f));
        g2.setColor(Color.WHITE);
        String displayedStr;

        displayedStr = "" + engine.getRemainingLives();
        g2.drawImage(heartIcon, 30, 10, null);
        g2.drawString(displayedStr, 80, 50);
    }

    private void drawWorldNumber(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(25f));
        g2.setColor(Color.WHITE);
        String displayedStr;
        String worldNum = engine.getUserData().getWorldNumber() == 6 ? "BOSS!" : String.valueOf(engine.getUserData().getWorldNumber() + 1);
        displayedStr = "World:" + worldNum;
        g2.drawString(displayedStr, 550, 50);
    }

    private void drawPoints(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(25f));
        g2.setColor(Color.WHITE);
        String displayedStr;
        displayedStr = "Points:" + engine.getScore();
        g2.drawString(displayedStr, 200, 50);
    }

    private void drawStartScreen(Graphics2D g2) {
        int row = engine.getStartScreenSelection().getLineNumber();
        g2.drawImage(startScreenImage, 0, 0, null);
        g2.drawImage(selectIcon, 375, row * 70 + 415, null);
    }


    private void drawLoadGameScreen(Graphics2D g2) {
        int row = engine.getLoadGameScreenSelection().getLineNumber();
        g2.drawImage(loadGameScreen, 0, 0, null);
        g2.drawImage(selectIcon, 450, row * 70 + 290, null);
    }


    public GameEngine getEngine() {
        return engine;
    }
}