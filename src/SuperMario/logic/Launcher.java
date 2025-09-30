package SuperMario.logic;

public class Launcher {
    public static void main(String[] args) {
        GameEngine gameEngine = GameEngine.getInstance();
        gameEngine.start();
    }
}
