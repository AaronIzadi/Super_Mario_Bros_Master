package SuperMario.repository;

import SuperMario.logic.UserData;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.HeroType;
import org.json.simple.JSONObject;

import java.io.FileWriter;

import static SuperMario.repository.SavePaths.filePaths;
import static SuperMario.repository.SavePaths.isFileIdValid;

public class SaveGameRepository {
    private final JSONObject object = new JSONObject();
    public void addUserData(UserData userData, int fileNumber) {

        Hero hero = userData.getHero();

        object.put("Hero Type", hero.getType());
        object.put("Hero form is super", hero.getHeroForm().isSuper());
        object.put("Hero form can shoot", hero.getHeroForm().ifCanShootFire());
        object.put("Hero form type", hero.getHeroForm().getHeroType());
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

        object.put("Owns Luigi", userData.getTypesOwned()[HeroType.LUIGI]);
        object.put("Owns Princess Peach", userData.getTypesOwned()[HeroType.PRINCESS_PEACH]);
        object.put("Owns Rosalina", userData.getTypesOwned()[HeroType.ROSALINA]);
        object.put("Owns Toad", userData.getTypesOwned()[HeroType.TOAD]);

        object.put("Map path", userData.getMapPath());
        object.put("World number" , userData.getWorldNumber());

        addToFile(fileNumber);
    }
    private void addToFile(int fileNumber) {
        if (!isFileIdValid(fileNumber)) {
            return;
        }

        try (FileWriter file = new FileWriter(filePaths[fileNumber])) {
            file.write(object.toString());
        } catch (Exception e) {
            System.out.println("ERROR AT SAVING GAME ON SLOT " + fileNumber + " !");
        }
    }

}
