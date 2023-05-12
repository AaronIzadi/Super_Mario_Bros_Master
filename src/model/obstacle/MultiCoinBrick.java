package model.obstacle;

import logic.GameEngine;
import model.prize.Coin;
import model.prize.Prize;

import java.awt.image.BufferedImage;

public class MultiCoinBrick extends SurpriseBrick {

    private Prize prize;
    private int numberOfCoinsLeft;

    public MultiCoinBrick(double x, double y, BufferedImage style, Prize prize) {
        super(x, y, style, prize);
        setBreakable(false);
        setEmpty(false);
        this.numberOfCoinsLeft = 5;
        this.prize = prize;
    }

    @Override
    public Prize reveal(GameEngine engine) {

        BufferedImage newStyle = engine.getImageLoader().loadImage("/sprite.png");
        newStyle = engine.getImageLoader().getSubImage(newStyle, 1, 2, 48, 48);

        Prize toReturn = null;


        if (numberOfCoinsLeft > 0) {
            Coin coin = new Coin(((Coin) prize).getX(), ((Coin) prize).getY(), ((Coin) prize).getStyle(), 10);
            numberOfCoinsLeft--;
            toReturn = prize;
            prize.reveal();
            prize = coin;
        }

        if (numberOfCoinsLeft <= 0) {
            setEmpty(true);
            setStyle(newStyle);
        }

        return toReturn;
    }
}
