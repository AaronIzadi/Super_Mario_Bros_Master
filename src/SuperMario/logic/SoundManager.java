package SuperMario.logic;

import SuperMario.input.SoundLoader;

public class SoundManager {

    private final SoundLoader soundLoader;

    public SoundManager() {
        this.soundLoader = new SoundLoader();
    }

    public void playJump() {
        soundLoader.playJump();
    }

    public void playCoin() {
        soundLoader.playCoin();
    }

    public void playBackground() {
        soundLoader.playBackground();
    }

    public void pauseBackground() {
        soundLoader.pauseBackground();
    }

    public void heroWinsOverBowser(){
        soundLoader.playBowserDies();
        soundLoader.stopBowserBackground();
        soundLoader.playStageClear();
    }

    public void playOneUp() {
        soundLoader.playOneUp();
    }

    public void playPowerUp() {
        soundLoader.playPowerUp();
    }

    public void playHeroDies() {
        soundLoader.playHeroDies();
    }

    public void playGameOver() {
        soundLoader.playGameOver();
    }

    public void playFireball() {
        soundLoader.playFireball();
    }

    public void playHeroFalls() {
        soundLoader.playHeroFalls();
    }

    public void playKickEnemy() {
        soundLoader.playKickEnemy();
    }

    public void playBreakBrick() {
        soundLoader.playBreakBrick();
    }

    public void playStageClear() {
        soundLoader.playStageClear();
    }

    public void playFlagPole() {
        soundLoader.playFlagPole();
    }

    public void playSuperStar() {
        soundLoader.playSuperStar();
    }

    public void playBowserDies() {
        soundLoader.playBowserDies();
    }

    public void playBowserFireBall() {
        soundLoader.playBowserFireBall();
    }

    public void playPipe() {
        soundLoader.playPipe();
    }

    public void playBossFightBackground() {
        soundLoader.playBowserBackground();
    }

    public void stopBossFightBackground() {
        soundLoader.stopBowserBackground();
    }

    public void pauseBackGround() {
        soundLoader.pauseBackground();
        GameEngine.getInstance().setMute(true);
    }

    public void resumeBackground() {
        soundLoader.resumeBackground();
        GameEngine.getInstance().setMute(false);
    }

    public void restartBackground(){
        soundLoader.restartBackground();
    }

    public void playStomp() {
        soundLoader.playStomp();
    }

}
