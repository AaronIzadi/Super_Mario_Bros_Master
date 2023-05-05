package graphic.game_view;

public enum StoreScreenSelection {
    MARIO(0),
    LUIGI(1),
    PRINCE_PEACH(2),
    ROSS(3),
    TOAD(4);


    private final int columnNumber;

    StoreScreenSelection(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public StoreScreenSelection getSelection(int number) {
        if (number == 0)
            return MARIO;
        else if (number == 1)
            return LUIGI;
        else if (number == 2)
            return PRINCE_PEACH;
        else if (number == 3)
            return ROSS;
        else if (number == 4)
            return TOAD;
        else
            return null;
    }

    public StoreScreenSelection select(boolean toLeft) {

        int selection;

        if(columnNumber > -1 && columnNumber < 5){
            selection = columnNumber - (toLeft ? 1 : -1);
            if(selection == -1)
                selection = 4;
            else if(selection == 5)
                selection = 0;
            return getSelection(selection);
        }

        return null;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
