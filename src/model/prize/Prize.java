package model.prize;

import logic.GameEngine;
import model.hero.Hero;

import java.awt.*;

public interface Prize {

    int getPoint();
    void reveal();
    Rectangle getBounds();
    void onTouch(Hero hero, GameEngine engine);

}
