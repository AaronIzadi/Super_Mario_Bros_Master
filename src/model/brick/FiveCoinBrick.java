package model.brick;

import graphic.manager.GameEngine;
import model.prize.Prize;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class FiveCoinBrick extends SurpriseBrick {

    private final LinkedList<Prize> prizes = new LinkedList<>();
    private int numberOfCoinsLeft;

    public FiveCoinBrick(double x, double y, BufferedImage style, Prize prize) {
        super(x, y, style, prize);
        setBreakable(false);
        setEmpty(false);
        this.numberOfCoinsLeft = 5;
        for (int i = 0; i < numberOfCoinsLeft; i++) {
            prizes.add(prize);
        }
    }

    @Override
    public Prize reveal(GameEngine engine) {

        BufferedImage newStyle = engine.getImageLoader().loadImage("/sprite.png");
        newStyle = engine.getImageLoader().getSubImage(newStyle, 1, 2, 48, 48);

        Prize toReturn = null;

        if (prizes != null && numberOfCoinsLeft > 0) {
            prizes.get(0).reveal();
            toReturn = prizes.get(0);
            prizes.remove(numberOfCoinsLeft - 1);
            numberOfCoinsLeft--;
        }

        if (numberOfCoinsLeft <= 0) {
            setEmpty(true);
            setStyle(newStyle);
        }

        return toReturn;
    }
}
