package SuperMario.input;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {

    private final Clip background;
    private long clipTime = 0;

    public SoundManager() {
        background = getClip(loadAudio("background"));
    }

    private AudioInputStream loadAudio(String url) {

        try {

            File file = new File("src/SuperMario/resources/audio/" + url + ".wav");
            return AudioSystem.getAudioInputStream(file);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private Clip getClip(AudioInputStream stream) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void resumeBackground() {
        background.setMicrosecondPosition(clipTime);
        background.start();
    }

    public void pauseBackground() {
        clipTime = background.getMicrosecondPosition();
        background.stop();
    }

    public void restartBackground() {
        clipTime = 0;
        resumeBackground();
    }

    public void playJump() {
        Clip clip = getClip(loadAudio("jump"));
        clip.start();
    }

    public void playCoin() {
        Clip clip = getClip(loadAudio("coin"));
        clip.start();
    }

    public void playFireball() {
        Clip clip = getClip(loadAudio("fireball"));
        clip.start();
    }

    public void playGameOver() {
        Clip clip = getClip(loadAudio("gameOver"));
        clip.start();
    }

    public void playStomp() {
        Clip clip = getClip(loadAudio("stomp"));
        clip.start();
    }

    public void playOneUp() {
        Clip clip = getClip(loadAudio("oneUp"));
        clip.start();
    }

    public void playPowerUp() {
        Clip clip = getClip(loadAudio("powerUp"));
        clip.start();
    }

    public void playHeroDies() {
        Clip clip = getClip(loadAudio("heroDies"));
        clip.start();
    }

    public void playBreakBrick() {
        Clip clip = getClip(loadAudio("breakBrick"));
        clip.start();
    }

    public void playHeroFalls() {
        Clip clip = getClip(loadAudio("heroFalls"));
        clip.start();
    }

    public void playKickEnemy() {
        Clip clip = getClip(loadAudio("kickEnemy"));
        clip.start();
    }

    public void playStageClear() {
        Clip clip = getClip(loadAudio("stageClear"));
        clip.start();
    }

    public void playFlagPole() {
        Clip clip = getClip(loadAudio("flagPole"));
        clip.start();
    }

    public void playSuperStar() {
        Clip clip = getClip(loadAudio("superStar"));
        clip.start();
        pauseBackground();
    }
}
