package graphic.view;

import java.awt.*;

public class MapSelectionItem {

    private final String name;
    private Point location;
    private Dimension dimension;

    public MapSelectionItem(String map, Point location) {
        this.location = location;
        this.name = map;
        this.dimension = new Dimension();
    }


    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
