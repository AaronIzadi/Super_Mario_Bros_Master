package SuperMario.config;

/**
 * Central place for gameplay and display constants used across the project.
 */
public final class GameConstants {

    private GameConstants() {
    }

    public static final int TILE_SIZE = 48;
    public static final int WORLD_HEIGHT = 720;
    public static final int GROUND_Y = WORLD_HEIGHT - (2 * TILE_SIZE);
    public static final int GROUND_BRICK_Y = WORLD_HEIGHT - (3 * TILE_SIZE);

    public static final int WINDOW_WIDTH = 1268;
    public static final int WINDOW_HEIGHT = 708;

    public static final double GRAVITY = 0.38;

    public static final double TICKS_PER_SECOND = 60.0;
    public static final int RENDER_SLEEP_MS = 12;
    public static final int MAP_TIMER_INTERVAL_MS = 1000;

    public static final int STAR_POWER_DURATION_MS = 15_000;
    public static final int STAR_RUN_DURATION_MS = 1_000;
    public static final int AXE_COOLDOWN_MS = 3_000;

    public static final int KOOPA_SHELL_RECOVERY_MS = 3_000;
    public static final int PIRANHA_GO_DOWN_DELAY_MS = 2_000;
    public static final int PIRANHA_GO_UP_DELAY_MS = 3_000;
    public static final int BOWSER_HP_RECOVERY_MS = 1_000;
    public static final int BOWSER_ATTACK_COOLDOWN_MS = 3_000;
    public static final int BOWSER_GRAB_ATTACK_WAIT_MS = 4_000;
    public static final int BOMB_EXPLODE_DELAY_MS = 2_500;
    public static final int BOMB_VANISH_DELAY_MS = 1_500;
    public static final int SLIME_RESTYLE_MS = 500;
    public static final int SUPER_STAR_JUMP_DELAY_MS = 1_000;
    public static final int GRAB_ATTACK_TIMEOUT_MS = 5_000;
    public static final int GRAB_RECOVERY_MS = 4_000;

    /** Horizontal throw speed (pixels per tick). */
    public static final double AXE_THROW_SPEED = 9.0;
    /** Initial upward velocity for the throw arc. */
    public static final double AXE_THROW_LIFT = 2.0;
    /** Gravity applied while the axe is in flight (lighter than the hero). */
    public static final double AXE_FLIGHT_GRAVITY = 0.28;
    /** Horizontal drag per tick while outbound (0–1). */
    public static final double AXE_AIR_DRAG = 0.992;
    /** Max travel distance before the axe turns back. */
    public static final int AXE_MAX_RANGE = 4 * TILE_SIZE;
    /** Max homing speed on return. */
    public static final double AXE_RETURN_SPEED_MAX = 11.0;
    /** Distance to the catch point at which the axe is collected. */
    public static final double AXE_CATCH_RADIUS = 30.0;
    /** Ignore block collisions until the axe has traveled this far from the throw point. */
    public static final double AXE_MIN_TRAVEL_BEFORE_BLOCK = TILE_SIZE * 1.5;
    /** Vertical offset above the hero while the axe is held. */
    public static final int AXE_HOLD_OFFSET_Y = 10;
    public static final int STANDING_TIMER_MS = 4_000;

    public static final int HERO_DEFAULT_SIZE = TILE_SIZE;
    public static final int CROUCH_HEIGHT = 50;
    public static final int SUPER_HERO_HEIGHT = 96;
    public static final int CROUCH_OFFSET = SUPER_HERO_HEIGHT - CROUCH_HEIGHT;

    public static final int MIN_SAVE_SLOT = 0;
    public static final int MAX_SAVE_SLOT = 2;

    public static int msToTicks(long milliseconds) {
        return (int) (milliseconds * TICKS_PER_SECOND / 1000.0);
    }
}
