package SuperMario.logic;

import SuperMario.graphic.view.states.*;

public class GameStateManager {

    private GameState gameState;

    public GameStateManager() {
        this.gameState = GameState.START_SCREEN;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }
}
