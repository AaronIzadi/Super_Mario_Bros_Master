package SuperMario.graphic.view.states;

public enum PauseScreenSelection {

    GO_TO_MAIN_MENU(0),
    SAVE_ON_FILE_1(1),
    SAVE_ON_FILE_2(2),
    SAVE_ON_FILE_3(3),
    MUTE_BACKGROUND_SOUND(4);


    private final int lineNumber;

    PauseScreenSelection(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public PauseScreenSelection getSelection(int number) {
        if (number == 0)
            return GO_TO_MAIN_MENU;
        else if (number == 1)
            return SAVE_ON_FILE_1;
        else if (number == 2)
            return SAVE_ON_FILE_2;
        else if (number == 3)
            return SAVE_ON_FILE_3;
        else if (number == 4)
            return MUTE_BACKGROUND_SOUND;
        else
            return null;
    }

    public PauseScreenSelection select(boolean toUp) {

        int selection;

        if (lineNumber > -1 && lineNumber < 5) {
            selection = lineNumber - (toUp ? 1 : -1);
            if (selection == -1)
                selection = 4;
            else if (selection == 5)
                selection = 0;
            return getSelection(selection);
        }

        return null;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
