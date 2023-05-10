package graphic.manager;

import logic.ButtonAction;
import logic.GameEngine;
import logic.GameState;
import org.json.simple.parser.ParseException;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;


public class InputManager implements KeyListener, MouseListener {

    private final GameEngine engine;

    public InputManager(GameEngine engine) {
        this.engine = engine;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        GameState state = engine.getGameState();
        ButtonAction currentAction = ButtonAction.NO_ACTION;

        boolean notRunningState = state == GameState.START_SCREEN || state == GameState.LOAD_GAME || state == GameState.MAP_SELECTION || state == GameState.PAUSED;
        if (keyCode == KeyEvent.VK_UP) {
            if (notRunningState)
                currentAction = ButtonAction.GO_UP;
            else
                currentAction = ButtonAction.JUMP;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            if (notRunningState)
                currentAction = ButtonAction.GO_DOWN;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            currentAction = ButtonAction.MOVE_RIGHT;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            currentAction = ButtonAction.MOVE_LEFT;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            currentAction = ButtonAction.SELECT;
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            if (state == GameState.RUNNING || state == GameState.PAUSED)
                currentAction = ButtonAction.PAUSE_RESUME;
            else
                currentAction = ButtonAction.GO_TO_START_SCREEN;

        } else if (keyCode == KeyEvent.VK_SPACE) {
            currentAction = ButtonAction.FIRE;
        }


        try {
            notifyInput(currentAction);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (engine.getGameState() == GameState.MAP_SELECTION) {
            engine.selectMapViaMouse();
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_LEFT) {
            try {
                notifyInput(ButtonAction.ACTION_COMPLETED);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void notifyInput(ButtonAction action) throws IOException, ParseException {
        if (action != ButtonAction.NO_ACTION)
            engine.receiveInput(action);
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
