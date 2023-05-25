package SuperMario.model.obstacle;



import SuperMario.logic.GameEngine;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.Prize;

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

        BufferedImage newStyle = engine.getImageLoader().getRevealedPrizeBrick();

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

    @Override
    public void animate() {
    }
}
