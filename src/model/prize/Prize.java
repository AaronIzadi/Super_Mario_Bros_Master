package model.prize;

import graphic.manager.GameEngine;
import model.hero.Hero;

import java.awt.*;

public interface Prize {

    int getPoint();
    void reveal();
    Rectangle getBounds();
    void onTouch(Hero hero, GameEngine engine);

}
