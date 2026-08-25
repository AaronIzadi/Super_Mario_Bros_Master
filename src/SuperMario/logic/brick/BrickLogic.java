package SuperMario.logic.brick;

import SuperMario.config.GameConstants;
import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.logic.MapManager;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.model.hero.Hero;
import SuperMario.model.obstacle.*;
import SuperMario.logic.prize.PrizeLogic;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.Prize;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class BrickLogic {

    private BrickLogic() {
    }

    public static Prize reveal(Brick brick, GameEngine engine) {
        if (brick instanceof OrdinaryBrick) {
            return reveal((OrdinaryBrick) brick, engine);
        } else if (brick instanceof SurpriseBrick) {
            if (brick instanceof CoinBrick) {
                return reveal((CoinBrick) brick, engine);
            } else if (brick instanceof MultiCoinBrick) {
                return reveal((MultiCoinBrick) brick, engine);
            }
            return reveal((SurpriseBrick) brick, engine);
        }
        return null;
    }

    public static void animate(Brick brick) {
        if (brick instanceof OrdinaryBrick) {
            animate((OrdinaryBrick) brick);
        } else if (brick instanceof CoinBrick) {
            animate((CoinBrick) brick);
        } else if (brick instanceof SurpriseBrick) {
            animate((SurpriseBrick) brick);
        } else if (brick instanceof CheckPoint) {
            animate((CheckPoint) brick);
        }
    }

    public static void draw(Obstacle obstacle, Graphics g) {
        if (obstacle instanceof SurpriseBrick) {
            if (obstacle instanceof CoinBrick) {
                draw((CoinBrick) obstacle, g);
            } else {
                draw((SurpriseBrick) obstacle, g);
            }
        } else if (obstacle instanceof CheckPoint) {
            draw((CheckPoint) obstacle, g);
        } else if (obstacle instanceof LavaBorder) {
            draw((LavaBorder) obstacle, g);
        } else if (obstacle instanceof Slime) {
            draw((Slime) obstacle, g);
        } else {
            EntityRenderer.drawSprite(obstacle, g);
        }
    }

    public static void initializeOrdinaryBrick(OrdinaryBrick brick) {
        BufferedImage[] frames = ImageLoader.getInstance().getBrickFrames();
        brick.setAnimation(new Animation(frames));
        brick.setBreaking(false);
        brick.setFrameCount(frames.length - 1);
    }

    public static void initializeCoinBrick(CoinBrick brick) {
        BufferedImage[] frames = ImageLoader.getInstance().getBrickFrames();
        brick.setAnimation(new Animation(frames));
        brick.setFrameCount(frames.length);
    }

    public static boolean isTimeToBreak(Brick brick) {
        long start = brick.getStart();
        long finish = brick.getFinish();
        long timer = brick.getTimer();

        if (start == 0) {
            brick.setStart(System.currentTimeMillis());
        } else {
            finish = System.currentTimeMillis();
            brick.setFinish(finish);
            timer = finish - start;
            brick.setTimer(timer);
            if (timer >= 2000) {
                return true;
            }
            brick.setFinish(0);
        }

        return timer >= 2000;
    }

    public static Prize reveal(OrdinaryBrick brick, GameEngine engine) {
        MapManager manager = engine.getMapManager();
        if (!manager.getHero().isSuper()) {
            return null;
        }
        brick.setBreaking(true);
        manager.addRevealedBrick(brick);
        engine.getSoundManager().playBreakBrick();

        double newX = brick.getX() - 27, newY = brick.getY() - 27;
        brick.setLocation(newX, newY);

        return null;
    }

    public static void animate(OrdinaryBrick brick) {
        if (brick.isBreaking()) {
            Animation animation = brick.getAnimation();
            boolean isAnimationTicked = animation.animate(30);
            if (isAnimationTicked) {
                brick.setStyle(animation.getCurrentFrame());
                brick.decrementFrames();
            }
        }
    }

    public static Prize reveal(SurpriseBrick brick, GameEngine engine) {
        BufferedImage newStyle = engine.getImageLoader().getRevealedPrizeBrick();

        Prize prize = brick.getPrize();
        if (prize != null) {
            PrizeLogic.reveal(prize);
        }

        brick.setEmpty(true);
        brick.setStyle(newStyle);

        Prize toReturn = prize;
        brick.setPrize(null);
        return toReturn;
    }

    public static void animate(SurpriseBrick brick) {
        Animation animation = brick.getAnimation();
        if (animation == null) {
            return;
        }
        boolean isAnimationTicked = animation.animate(5);
        if (isAnimationTicked) {
            brick.setStyle(animation.getCurrentFrame());
        }
    }

    public static void draw(SurpriseBrick brick, Graphics g) {
        EntityRenderer.drawSprite(brick, g);
        if (!brick.isEmpty()) {
            animate(brick);
        }
    }

    public static Prize reveal(CoinBrick brick, GameEngine engine) {
        Prize prize = brick.getPrize();
        if (prize != null) {
            PrizeLogic.reveal(prize);

            brick.setEmpty(true);
            brick.setBreakable(true);
            Prize toReturn = prize;
            brick.setPrize(null);
            return toReturn;
        } else {
            MapManager manager = engine.getMapManager();
            if (!manager.getHero().isSuper()) {
                return null;
            }

            manager.addRevealedBrick(brick);
            engine.getSoundManager().playBreakBrick();

            double newX = brick.getX() - 27, newY = brick.getY() - 27;
            brick.setLocation(newX, newY);

            return null;
        }
    }

    public static void draw(CoinBrick brick, Graphics g) {
        EntityRenderer.drawSprite(brick, g);
    }

    public static void animate(CoinBrick brick) {
        Animation animation = brick.getAnimation();
        boolean isAnimationTicked = animation.animate(30);
        if (isAnimationTicked) {
            brick.setStyle(animation.getCurrentFrame());
            brick.decrementFrames();
        }
    }

    public static Prize reveal(MultiCoinBrick brick, GameEngine engine) {
        BufferedImage newStyle = engine.getImageLoader().getRevealedPrizeBrick();

        Prize toReturn = null;
        int coinsLeft = brick.getNumberOfCoinsLeft();
        Prize prize = brick.getPrize();

        if (coinsLeft > 0) {
            Coin coin = new Coin(((Coin) prize).getX(), ((Coin) prize).getY(), ((Coin) prize).getStyle(), 10);
            brick.setNumberOfCoinsLeft(coinsLeft - 1);
            toReturn = prize;
            PrizeLogic.reveal(prize);
            brick.setPrize(coin);
        }

        if (brick.getNumberOfCoinsLeft() <= 0) {
            brick.setEmpty(true);
            brick.setStyle(newStyle);
        }

        return toReturn;
    }

    public static void animate(MultiCoinBrick brick) {
    }

    public static Point check(CheckPoint checkpoint, boolean checked) {
        checkpoint.setChecked(checked);
        checkpoint.setRevealed(true);
        BufferedImage newStyle;

        if (checked) {
            newStyle = ImageLoader.getInstance().getRevealedCheckPoint();
        } else {
            newStyle = ImageLoader.getInstance().getRevealedPrizeBrick();
        }

        checkpoint.setStyle(newStyle);
        checkpoint.setEmpty(true);

        return new Point((int) checkpoint.getX(), (int) checkpoint.getY());
    }

    public static void draw(CheckPoint checkpoint, Graphics g) {
        EntityRenderer.drawSprite(checkpoint, g);
        if (!checkpoint.isChecked() && !checkpoint.isEmpty()) {
            animate(checkpoint);
        }
    }

    public static void animate(CheckPoint checkpoint) {
        Animation animation = checkpoint.getAnimation();
        if (animation == null) {
            return;
        }
        boolean isAnimationTicked = animation.animate(5);
        if (isAnimationTicked) {
            checkpoint.setStyle(animation.getCurrentFrame());
        }
    }

    public static void draw(LavaBorder lava, Graphics g) {
        if (lava.isBurn()) {
            animate(lava);
        } else {
            lava.setStyle(lava.getMainStyle());
        }
        EntityRenderer.drawSprite(lava, g);
    }

    public static void animate(LavaBorder lava) {
        Animation animation = lava.getAnimation();
        if (animation == null) {
            return;
        }
        boolean isAnimationTicked = animation.animate(8);
        if (isAnimationTicked) {
            lava.setStyle(animation.getCurrentFrame());
        }
    }

    public static void draw(Slime slime, Graphics g) {
        if (slime.isOnTouch()) {
            g.drawImage(slime.getSlimeOnTouch(), (int) slime.getX() - 4, (int) slime.getY(), null);
        } else {
            EntityRenderer.drawSprite(slime, g);
        }
    }

    public static void setOnTouch(Slime slime, boolean onTouch) {
        slime.setOnTouchFlag(onTouch);
        slime.setRestyleTicks(GameConstants.msToTicks(GameConstants.SLIME_RESTYLE_MS));
    }

    public static boolean onTouchHero(CrossoverTunnel tunnel, Hero hero) {
        hero.setVelY(-5);
        return hero.getY() == 600;
    }
}
