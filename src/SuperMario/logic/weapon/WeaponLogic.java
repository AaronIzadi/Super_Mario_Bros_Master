package SuperMario.logic.weapon;

import SuperMario.config.GameConstants;
import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.logic.physics.Physics;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.Brick;
import SuperMario.model.obstacle.Obstacle;
import SuperMario.model.weapon.Hammer;
import SuperMario.model.weapon.Fireball;

import java.awt.*;

public final class WeaponLogic {

    private WeaponLogic() {
    }

    public static boolean intersectsBrick(Fireball fireball, Map map) {
        return intersectsBrick(fireball.getBounds(), map);
    }

    public static boolean update(Fireball fireball, Map map) {
        double previousX = fireball.getX();
        double previousY = fireball.getY();
        Physics.updateLocation(fireball);
        return intersectsBrick(movementBounds(fireball, previousX, previousY), map);
    }

    private static boolean intersectsBrick(Rectangle bounds, Map map) {
        for (Obstacle obstacle : map.getAllObstacles()) {
            if (obstacle instanceof Brick && bounds.intersects(obstacle.getBounds())) {
                return true;
            }
        }
        return false;
    }

    private static Rectangle movementBounds(Fireball fireball, double previousX, double previousY) {
        Rectangle current = fireball.getBounds();
        Rectangle previous = new Rectangle((int) previousX, (int) previousY, current.width, current.height);
        int x1 = Math.min(current.x, previous.x);
        int y1 = Math.min(current.y, previous.y);
        int x2 = Math.max(current.x + current.width, previous.x + previous.width);
        int y2 = Math.max(current.y + current.height, previous.y + previous.height);
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    public static Hammer createHammer(Hero hero) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        double x = hero.getToRight()
                ? hero.getX() + 24
                : hero.getX() - GameConstants.TILE_SIZE;
        return new Hammer(
                x,
                hero.getY(),
                imageLoader.getHammerUpRight(),
                hero,
                new Animation(imageLoader.hammerFrames()),
                imageLoader.getHammerUpLeft());
    }

    public static void draw(Hammer hammer, Graphics g) {
        Hero hero = hammer.getHero();
        if (!hammer.isReleased()) {
            if (hero.getToRight()) {
                EntityRenderer.drawSprite(hammer, g);
            } else {
                g.drawImage(hammer.getLeftStyle(), (int) hammer.getX(), (int) hammer.getY(), null);
            }
        } else {
            animate(hammer);
            EntityRenderer.drawSprite(hammer, g);
        }
    }

    public static void animate(Hammer hammer) {
        boolean isAnimationTicked = hammer.getHammerAnimation().animate(25);
        if (isAnimationTicked) {
            hammer.setStyle(hammer.getHammerAnimation().getCurrentFrame());
        }
    }

    public static void setReleased(Hammer hammer, boolean released, double xReleasePoint) {
        hammer.setReleasedFlag(released);
        hammer.setXReleasePoint(xReleasePoint);
        if (released) {
            hammer.setGotThere(false);
            hammer.setGotBack(false);
            double direction = hammer.getHero().getToRight() ? 1.0 : -1.0;
            hammer.setVelX(direction * GameConstants.HAMMER_THROW_SPEED);
            hammer.setVelY(GameConstants.HAMMER_THROW_LIFT);
        }
    }

    public static void update(Hammer hammer) {
        Hero hero = hammer.getHero();

        if (!hammer.isGotThere()) {
            updateOutbound(hammer);
            if (Math.abs(hammer.getX() - hammer.getXReleasePoint()) >= GameConstants.HAMMER_MAX_RANGE) {
                hammer.setGotThere(true);
            }
        } else {
            updateReturn(hammer, hero);
        }
    }

    private static void updateOutbound(Hammer hammer) {
        hammer.setVelY(hammer.getVelY() - GameConstants.HAMMER_FLIGHT_GRAVITY);
        hammer.setVelX(hammer.getVelX() * GameConstants.HAMMER_AIR_DRAG);
        hammer.setY(hammer.getY() - hammer.getVelY());
        hammer.setX(hammer.getX() + hammer.getVelX());
    }

    private static void updateReturn(Hammer hammer, Hero hero) {
        double catchX = hero.getX() + (hero.getToRight() ? 24 : -GameConstants.TILE_SIZE);
        double catchY = hero.getY() + GameConstants.TILE_SIZE / 2.0;

        double dx = catchX - hammer.getX();
        double dy = catchY - hammer.getY();
        double distance = Math.hypot(dx, dy);

        if (distance < GameConstants.HAMMER_CATCH_RADIUS) {
            HeroLogic.deactivateHammer(hero);
            return;
        }

        double speed = Math.min(GameConstants.HAMMER_RETURN_SPEED_MAX, 4.0 + distance * 0.14);
        hammer.setVelX((dx / distance) * speed);
        hammer.setVelY(-(dy / distance) * speed);

        hammer.setY(hammer.getY() - hammer.getVelY());
        hammer.setX(hammer.getX() + hammer.getVelX());

        hammer.setGotBack(true);
    }
}
