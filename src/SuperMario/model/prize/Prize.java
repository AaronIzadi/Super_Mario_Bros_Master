package SuperMario.model.prize;


import SuperMario.logic.GameEngine;
import SuperMario.model.hero.Hero;

import java.awt.*;

public interface Prize {

    int getPoint();

    void reveal();

    Rectangle getBounds();

    void onTouch(Hero hero, GameEngine engine);

}
