package SuperMario.logic.enemy;

import SuperMario.config.GameConstants;
import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.physics.Physics;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.model.enemy.Goomba;
import SuperMario.model.enemy.KoopaTroopa;
import SuperMario.model.enemy.Piranha;
import SuperMario.model.enemy.Spiny;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.enemy.bowser.Bowser;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class EnemyLogic {

    private EnemyLogic() {
    }

    public static void draw(Goomba goomba, Graphics g) {
        EntityRenderer.drawSprite(goomba, g);
        animate(goomba);
    }

    public static void animate(Goomba goomba) {
        Animation animation = goomba.getAnimation();
        if (animation == null) {
            return;
        }
        boolean isAnimationTicked = animation.animate(5);
        if (isAnimationTicked) {
            goomba.setStyle(animation.getCurrentFrame());
        }
    }

    public static void update(Goomba goomba) {
        Physics.updateLocation(goomba);
    }

    public static void draw(KoopaTroopa koopa, Graphics g) {
        if (koopa.isHit()) {
            g.drawImage(koopa.getShell(), (int) koopa.getX(), (int) koopa.getY() + 26, null);
        } else {
            if (koopa.getVelX() > 0) {
                g.drawImage(koopa.getRightImage(), (int) koopa.getX(), (int) koopa.getY(), null);
            } else {
                EntityRenderer.drawSprite(koopa, g);
            }
        }
    }

    public static void update(KoopaTroopa koopa) {
        if (!koopa.isHit()) {
            Physics.updateLocation(koopa);
            koopa.setLastVelX(koopa.getVelX());
        } else if (koopa.getShellRecoveryTicks() == 0) {
            koopa.setShellRecoveryTicks(GameConstants.msToTicks(GameConstants.KOOPA_SHELL_RECOVERY_MS));
        }
    }

    public static void moveAfterHit(KoopaTroopa koopa) {
        if (koopa.getLastVelX() > 0) {
            koopa.setX(koopa.getX() + 24);
        } else {
            koopa.setX(koopa.getX() - 24);
        }
    }

    public static void draw(Piranha piranha, Graphics g) {
        if (piranha.getY() >= 580) {
            piranha.setY(580);
            if (piranha.getVelY() != 0) {
                piranha.setVelY(0);
                schedulePiranhaMovement(piranha, 1, GameConstants.PIRANHA_GO_UP_DELAY_MS);
            }
        }
        if (piranha.getY() <= 480) {
            piranha.setY(480);
            if (piranha.getVelY() != 0) {
                piranha.setVelY(0);
                schedulePiranhaMovement(piranha, -1, GameConstants.PIRANHA_GO_DOWN_DELAY_MS);
            }
        }
        EntityRenderer.drawSprite(piranha, g);
        animate(piranha);
    }

    private static void schedulePiranhaMovement(Piranha piranha, double velY, int delayMs) {
        if (piranha.getMovementDelayTicks() == 0) {
            piranha.setPendingVelY(velY);
            piranha.setMovementDelayTicks(GameConstants.msToTicks(delayMs));
        }
    }

    public static void animate(Piranha piranha) {
        Animation animation = piranha.getAnimation();
        if (animation == null) {
            return;
        }
        boolean isAnimationTicked = animation.animate(7);
        if (isAnimationTicked) {
            piranha.setStyle(animation.getCurrentFrame());
        }
    }

    public static void update(Piranha piranha) {
        Physics.updateLocation(piranha);
    }

    public static void draw(Spiny spiny, Graphics g) {
        if (spiny.getVelX() > 0) {
            g.drawImage(spiny.getRightImage(), (int) spiny.getX(), (int) spiny.getY(), null);
            spiny.setToRight(true);
        } else {
            EntityRenderer.drawSprite(spiny, g);
            spiny.setToRight(false);
        }
    }

    public static void moveFaster(Spiny spiny) {
        if (spiny.isToRight()) {
            spiny.setVelX(6);
        } else {
            spiny.setVelX(-6);
        }
    }

    public static void moveNormal(Spiny spiny) {
        if (spiny.isToRight()) {
            spiny.setVelX(3);
        } else {
            spiny.setVelX(-3);
        }
    }

    public static void update(Spiny spiny) {
        Physics.updateLocation(spiny);
    }

    public static void draw(Enemy enemy, Graphics g) {
        if (enemy instanceof Goomba) {
            draw((Goomba) enemy, g);
        } else if (enemy instanceof KoopaTroopa) {
            draw((KoopaTroopa) enemy, g);
        } else if (enemy instanceof Piranha) {
            draw((Piranha) enemy, g);
        } else if (enemy instanceof Spiny) {
            draw((Spiny) enemy, g);
        } else if (enemy instanceof Bowser) {
            BowserLogic.draw((Bowser) enemy, g);
        }
    }

    public static void update(Enemy enemy) {
        if (enemy instanceof Goomba) {
            update((Goomba) enemy);
        } else if (enemy instanceof KoopaTroopa) {
            update((KoopaTroopa) enemy);
        } else if (enemy instanceof Piranha) {
            update((Piranha) enemy);
        } else if (enemy instanceof Spiny) {
            update((Spiny) enemy);
        } else if (enemy instanceof Bowser) {
            BowserLogic.update((Bowser) enemy);
        }
    }
}
