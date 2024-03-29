package SuperMario.repository;

import SuperMario.logic.UserData;
import SuperMario.model.hero.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static SuperMario.repository.SavePaths.*;

public class LoadGameRepository {

    private JSONObject object = new JSONObject();

    public UserData getUserData(int fileNumber) throws IOException {

        jsonToReadFile(fileNumber);

        Long typeAsLong = (Long) object.get("Hero Type");
        int type = typeAsLong.intValue();
        Boolean isSuper = (Boolean) object.get("Hero form is super");
        Boolean canShootFire = (Boolean) object.get("Hero form can shoot");
        Long formTypeAsLong = (Long) object.get("Hero form type");
        int formType = formTypeAsLong.intValue();
        Long livesAsLong = (Long) object.get("Remaining Lives");
        int lives = livesAsLong.intValue();
        Long pointsAsLong = (Long) object.get("Points");
        int points = pointsAsLong.intValue();
        Double timer = (Double) object.get("Invincibility Timer");
        Double x = (Double) object.get("Dimensional X");
        Double y = (Double) object.get("Dimensional Y");
        Long coinsAsLong = (Long) object.get("Coins");
        int coins = coinsAsLong.intValue();
        Boolean toRight = (Boolean) object.get("To right");
        Long heightAsLong = (Long) object.get("Dimensional height");
        int height = heightAsLong.intValue();
        Long widthAsLong = (Long) object.get("Dimensional width");
        int width = widthAsLong.intValue();
        Double gravityAcc = (Double) object.get("Gravity Acc");
        Boolean falling = (Boolean) object.get("Falling");
        Boolean jumping = (Boolean) object.get("Jumping");
        String mapPath = (String) object.get("Map path");
        Long worldNumberAsLong = (Long) object.get("World number");
        int worldNumber = worldNumberAsLong.intValue();
        boolean[] typesOwned = new boolean[5];
        typesOwned[HeroType.MARIO] = true;
        typesOwned[HeroType.LUIGI] = (Boolean) object.get("Owns Luigi");
        typesOwned[HeroType.PRINCESS_PEACH] = (Boolean) object.get("Owns Princess Peach");
        typesOwned[HeroType.ROSALINA] = (Boolean) object.get("Owns Rosalina");
        typesOwned[HeroType.TOAD] = (Boolean) object.get("Owns Toad");

        Hero hero;

        switch (type){
            case HeroType.LUIGI: {
                hero = new Luigi(x, y, width, height, type, formType, isSuper, canShootFire);
                break;
            }
            case HeroType.PRINCESS_PEACH: {
                hero = new PrincessPeach(x, y, width, height, type, formType, isSuper, canShootFire);
                break;
            }
            case HeroType.ROSALINA: {
                hero = new Rosalina(x, y, width, height, type, formType, isSuper, canShootFire);
                break;
            }
            case HeroType.TOAD:{
                hero = new Toad(x, y, width, height, type, formType, isSuper, canShootFire);
                break;
            }
            default:{
                hero = new Mario(x, y, width, height, type, formType, isSuper, canShootFire);
                break;
            }
        }
        UserData userData = UserData.getInstance();

        hero.getHeroForm().setCanShootFire(canShootFire);
        hero.setRemainingLives(lives);
        hero.setPoints(points);
        hero.setInvincibilityTimer(timer);
        hero.setCoins(coins);
        hero.setToRight(toRight);
        hero.setGravityAcc(gravityAcc);
        hero.setFalling(falling);
        hero.setJumping(jumping);

        userData.setTypesOwned(typesOwned);
        userData.setHero(hero);
        userData.setMapPath(mapPath);
        userData.setWorldNumber(worldNumber);

        return userData;
    }

    private void jsonToReadFile(int fileNumber) throws IOException {

        String address = filePaths[0];

        if (isFileIdValid(fileNumber)) {
            address = filePaths[fileNumber];
        }

        Charset encoding = Charset.defaultCharset();
        List<String> lines = Files.readAllLines(Paths.get(address), encoding);

        JSONParser parser = new JSONParser();
        String s = String.join("\n", lines);
        try {
            object = (JSONObject) parser.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isFileEmpty(int fileId) {
        if (!isFileIdValid(fileId)) {
            return false;
        }
        File file = new File(filePaths[fileId]);
        return file.length() == 0;
    }

}
