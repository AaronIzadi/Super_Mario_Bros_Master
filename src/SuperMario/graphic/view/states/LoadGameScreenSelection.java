package SuperMario.graphic.view.states;

public enum LoadGameScreenSelection {

    NEW_GAME(0),
    LOAD_GAME_1(1),
    LOAD_GAME_2(2),
    LOAD_GAME_3(3);


    private final int lineNumber;

    LoadGameScreenSelection(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public LoadGameScreenSelection getSelection(int number) {
        if (number == 0)
            return NEW_GAME;
        else if (number == 1)
            return LOAD_GAME_1;
        else if (number == 2)
            return LOAD_GAME_2;
        else if (number == 3)
            return LOAD_GAME_3;
        else
            return null;
    }

    public LoadGameScreenSelection select(boolean toUp) {

        int selection;

        if (lineNumber > -1 && lineNumber < 4) {
            selection = lineNumber - (toUp ? 1 : -1);
            if (selection == -1)
                selection = 3;
            else if (selection == 4)
                selection = 0;
            return getSelection(selection);
        }

        return null;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
