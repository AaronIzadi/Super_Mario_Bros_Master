package SuperMario.logic.weapon;

import SuperMario.config.GameConstants;
import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.logic.physics.Physics;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.model.hero.Hero;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

import java.awt.*;

public final class WeaponLogic {

    private WeaponLogic() {
    }

    public static void update(Fireball fireball) {
        Physics.updateLocation(fireball);
    }

    public static Axe createAxe(Hero hero) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        double x = hero.getToRight()
                ? hero.getX() + 24
                : hero.getX() - GameConstants.TILE_SIZE;
        return new Axe(
                x,
                hero.getY(),
                imageLoader.getAxeUpRight(),
                hero,
                new Animation(imageLoader.axeFrames()),
                imageLoader.getAxeUpLeft());
    }

    public static void draw(Axe axe, Graphics g) {
        Hero hero = axe.getHero();
        if (!axe.isReleased()) {
            if (hero.getToRight()) {
                EntityRenderer.drawSprite(axe, g);
            } else {
                g.drawImage(axe.getLeftStyle(), (int) axe.getX(), (int) axe.getY(), null);
            }
        } else {
            animate(axe);
            EntityRenderer.drawSprite(axe, g);
        }
    }

    public static void animate(Axe axe) {
        boolean isAnimationTicked = axe.getAxeAnimation().animate(25);
        if (isAnimationTicked) {
            axe.setStyle(axe.getAxeAnimation().getCurrentFrame());
        }
    }

    public static void setReleased(Axe axe, boolean released, double xReleasePoint) {
        axe.setReleasedFlag(released);
        axe.setXReleasePoint(xReleasePoint);
        if (released) {
            axe.setGotThere(false);
            axe.setGotBack(false);
            double direction = axe.getHero().getToRight() ? 1.0 : -1.0;
            axe.setVelX(direction * GameConstants.AXE_THROW_SPEED);
            axe.setVelY(GameConstants.AXE_THROW_LIFT);
        }
    }

    public static void update(Axe axe) {
        Hero hero = axe.getHero();

        if (!axe.isGotThere()) {
            updateOutbound(axe);
            if (Math.abs(axe.getX() - axe.getXReleasePoint()) >= GameConstants.AXE_MAX_RANGE) {
                axe.setGotThere(true);
            }
        } else {
            updateReturn(axe, hero);
        }
    }

    private static void updateOutbound(Axe axe) {
        axe.setVelY(axe.getVelY() - GameConstants.AXE_FLIGHT_GRAVITY);
        axe.setVelX(axe.getVelX() * GameConstants.AXE_AIR_DRAG);
        axe.setY(axe.getY() - axe.getVelY());
        axe.setX(axe.getX() + axe.getVelX());
    }

    private static void updateReturn(Axe axe, Hero hero) {
        double catchX = hero.getX() + (hero.getToRight() ? 24 : -GameConstants.TILE_SIZE);
        double catchY = hero.getY() + GameConstants.TILE_SIZE / 2.0;

        double dx = catchX - axe.getX();
        double dy = catchY - axe.getY();
        double distance = Math.hypot(dx, dy);

        if (distance < GameConstants.AXE_CATCH_RADIUS) {
            HeroLogic.deactivateAxe(hero);
            return;
        }

        double speed = Math.min(GameConstants.AXE_RETURN_SPEED_MAX, 4.0 + distance * 0.14);
        axe.setVelX((dx / distance) * speed);
        axe.setVelY(-(dy / distance) * speed);

        axe.setY(axe.getY() - axe.getVelY());
        axe.setX(axe.getX() + axe.getVelX());

        axe.setGotBack(true);
    }
}
