package repository;

import logic.UserData;
import model.hero.Hero;
import org.json.simple.JSONObject;

import java.io.FileWriter;

public class SaveGameRepository {

    private final JSONObject object = new JSONObject();

    public static final int FILE_1 = 1;
    public static final int FILE_2 = 2;
    public static final int FILE_3 = 3;

    public static final String pathToFile1 = "data/data-game-1.txt";
    public static final String pathToFile2 = "data/data-game-2.txt";
    public static final String pathToFile3 = "data/data-game-3.txt";


    public void addUserData(UserData userData, int fileNumber) {

        Hero hero = userData.getHero();

        object.put("Hero Type", hero.getType());
        object.put("Hero form is super", hero.getHeroForm().isSuper());
        object.put("Hero form can shoot", hero.getHeroForm().ifCanShootFire());
        object.put("Hero form type", hero.getHeroForm().getType());
        object.put("Remaining Lives", hero.getRemainingLives());
        object.put("Points", hero.getPoints());
        object.put("Invincibility Timer", hero.getInvincibilityTimer());
        object.put("Dimensional X", hero.getX());
        object.put("Dimensional Y", hero.getY());
        object.put("Coins", hero.getCoins());
        object.put("To right", hero.getToRight());
        object.put("Dimensional height", hero.getDimension().height);
        object.put("Dimensional width", hero.getDimension().width);
        object.put("Gravity Acc", hero.getGravityAcc());
        object.put("Falling", hero.isFalling());
        object.put("Jumping", hero.isJumping());

        object.put("Types owned", userData.getTypesOwned());

        object.put("Map path", userData.getHero().getMapPath());

        addToFile(fileNumber);
    }
    private void addToFile(int fileNumber) {
        if (fileNumber == FILE_1) {
            try (FileWriter file = new FileWriter(pathToFile1)) {
                file.write(object.toString());
            } catch (Exception e) {
                System.out.println("ERROR AT SAVING GAME!");
            }
        } else if (fileNumber == FILE_2) {
            try (FileWriter file = new FileWriter(pathToFile2)) {
                file.write(object.toString());
            } catch (Exception e) {
                System.out.println("ERROR AT SAVING GAME!");
            }
        } else if (fileNumber == FILE_3) {
            try (FileWriter file = new FileWriter(pathToFile3)) {
                file.write(object.toString());
            } catch (Exception e) {
                System.out.println("ERROR AT SAVING GAME!");
            }
        }
    }

}
