package SuperMario.logic.map;

import SuperMario.model.map.HitPoints;

import java.awt.image.BufferedImage;

public final class BossHudLogic {

    private BossHudLogic() {
    }

    public static HitPoints createHitPoints(BufferedImage[] frames) {
        HitPoints hitPoints = new HitPoints();
        hitPoints.setFrames(frames);
        return hitPoints;
    }

    public static void setStyle(HitPoints hitPoints, int hp) {
        BufferedImage[] frames = hitPoints.getFrames();
        if (hp != 0) {
            hitPoints.setStyleImage(frames[hp - 1]);
        } else {
            hitPoints.setStyleImage(null);
        }
    }
}
