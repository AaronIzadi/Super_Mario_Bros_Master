package SuperMario.graphic.view.states;

public enum CheckPointSelection {

    YES(0),
    NO(1);
    private final int columnNumber;

    CheckPointSelection(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public CheckPointSelection getSelection(int number) {
        if (number == 0)
            return YES;
        else if (number == 1)
            return NO;
        else
            return null;
    }


    public CheckPointSelection select(boolean toLeft) {

        int selection;

        if(columnNumber > -1 && columnNumber < 2){
            selection = columnNumber - (toLeft ? 1 : -1);
            if(selection == -1)
                selection = 1;
            else if(selection == 2)
                selection = 0;
            return getSelection(selection);
        }

        return null;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
