package SuperMario.logic.timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Frame-based delayed actions, ticked once per game-loop update (~60 Hz).
 */
public final class GameTimer {

    private static final List<ScheduledAction> actions = new ArrayList<>();

    private GameTimer() {
    }

    public static void schedule(int ticks, Runnable action) {
        if (ticks <= 0) {
            action.run();
            return;
        }
        actions.add(new ScheduledAction(ticks, action));
    }

    public static void tick() {
        Iterator<ScheduledAction> iterator = actions.iterator();
        while (iterator.hasNext()) {
            ScheduledAction scheduled = iterator.next();
            scheduled.remainingTicks--;
            if (scheduled.remainingTicks <= 0) {
                scheduled.action.run();
                iterator.remove();
            }
        }
    }

    public static void clear() {
        actions.clear();
    }

    private static final class ScheduledAction {
        private int remainingTicks;
        private final Runnable action;

        private ScheduledAction(int ticks, Runnable action) {
            this.remainingTicks = ticks;
            this.action = action;
        }
    }
}
