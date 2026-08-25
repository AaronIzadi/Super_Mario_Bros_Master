package SuperMario.model.obstacle;

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

    public int getNumberOfCoinsLeft() {
        return numberOfCoinsLeft;
    }

    public void setNumberOfCoinsLeft(int numberOfCoinsLeft) {
        this.numberOfCoinsLeft = numberOfCoinsLeft;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    @Override
    public Prize getPrize() {
        return prize;
    }
}
