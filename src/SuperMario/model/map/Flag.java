package SuperMario.model.map;

import SuperMario.logic.map.FlagLogic;
import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class Flag extends GameObject {

    private boolean touched = false;

    public Flag(double x, double y, BufferedImage style) {
        super(x, y, style);
    }

    @Override
    public void updateLocation(){
        FlagLogic.updateLocation(this);
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }
}
