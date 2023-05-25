package SuperMario.graphic.manager;

import SuperMario.input.ButtonAction;
import SuperMario.graphic.view.states.GameState;
import SuperMario.logic.GameEngine;
import org.json.simple.parser.ParseException;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.awt.event.KeyEvent.*;


public class InputManager implements KeyListener, MouseListener {

    private static final InputManager instance = new InputManager();
    private final Set<Integer> keyPressed;

    private InputManager() {
        keyPressed = new HashSet<>();
    }

    public static InputManager getInstance() {
        return instance;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyPressed.add(keyCode);
        GameState state = GameEngine.getInstance().getGameState();
        ButtonAction currentAction = ButtonAction.NO_ACTION;
        boolean notRunningState = state == GameState.START_SCREEN || state == GameState.LOAD_GAME || state == GameState.PAUSED;
        if (keyCode == VK_UP) {
            if (notRunningState) {
                currentAction = ButtonAction.GO_UP;
            } else {
                if (keyPressed.contains(VK_DOWN)) {
                    currentAction = ButtonAction.AXE;
                } else {
                    currentAction = ButtonAction.JUMP;
                }
            }
        } else if (keyCode == VK_DOWN) {
            if (notRunningState) {
                currentAction = ButtonAction.GO_DOWN;
            } else {
                if (keyPressed.contains(VK_UP)) {
                    currentAction = ButtonAction.AXE;
                } else {
                    currentAction = ButtonAction.SIT;
                }
            }
        } else if (keyCode == VK_RIGHT) {
            currentAction = ButtonAction.MOVE_RIGHT;
        } else if (keyCode == VK_LEFT) {
            currentAction = ButtonAction.MOVE_LEFT;
        } else if (keyCode == VK_ENTER) {
            currentAction = ButtonAction.SELECT;
        } else if (keyCode == VK_ESCAPE) {
            if (state == GameState.RUNNING || state == GameState.PAUSED) {
                currentAction = ButtonAction.PAUSE_RESUME;
            } else {
                currentAction = ButtonAction.GO_TO_START_SCREEN;
            }
        } else if (keyCode == VK_SPACE) {
            currentAction = ButtonAction.FIRE;
            if (GameEngine.getInstance().getUserData().getHero().getAxe() != null) {
                currentAction = ButtonAction.THROW_AXE;
            }
        }


        try {
            notifyInput(currentAction);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        keyPressed.remove(event.getKeyCode());
        try {
            notifyInput(ButtonAction.ACTION_COMPLETED);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void notifyInput(ButtonAction action) throws IOException, ParseException {
        if (action != ButtonAction.NO_ACTION)
            GameEngine.getInstance().receiveInput();
    }

    @Override
    public void mousePressed(MouseEvent e) {
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

    public boolean isPressed(int keyCode) {
        return keyPressed.contains(keyCode);
    }

    public boolean isEnter() {
        return isPressed(VK_ENTER);
    }

    public boolean isRight() {
        return isPressed(VK_RIGHT);
    }

    public boolean isLeft() {
        return isPressed(VK_LEFT);
    }

    public boolean isUp() {
        return isPressed(VK_UP);
    }

    public boolean isDown() {
        return isPressed(VK_DOWN);
    }

    public boolean isSpace() {
        return isPressed(VK_SPACE);
    }

    public boolean isEscape() {
        return isPressed(VK_ESCAPE);
    }

    public boolean isEmpty() {
        return keyPressed.isEmpty();
    }

    public boolean isLeftAndRightSelected() {
        return isLeft() && isRight();
    }

    public boolean isUpAndDownSelected() {
        return keyPressed.contains(VK_DOWN) && keyPressed.contains(VK_UP);
    }
}
