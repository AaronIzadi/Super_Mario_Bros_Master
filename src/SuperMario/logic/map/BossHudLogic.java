package SuperMario.logic.map;

import SuperMario.input.ImageLoader;
import SuperMario.model.map.HitPoints;

import java.awt.image.BufferedImage;

public final class BossHudLogic {

    private BossHudLogic() {
    }

    public static void initialize(ImageLoader imageLoader) {
        HitPoints.getInstance().setFrames(imageLoader.getHitPointFrames());
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
