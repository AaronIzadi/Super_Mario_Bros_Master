package repository;

import logic.MapManager;
import graphic.view.ImageLoader;
import logic.UserData;
import model.Map;
import model.hero.Hero;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoadGameRepository {

    private JSONObject object = new JSONObject();

    public static final int FILE_1 = 1;
    public static final int FILE_2 = 2;
    public static final int FILE_3 = 3;

    public static final String pathToFile1 = "data/data-game-1.txt";
    public static final String pathToFile2 = "data/data-game-2.txt";
    public static final String pathToFile3 = "data/data-game-3.txt";

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
        ArrayList<Long> typesOwnedAsLong = (ArrayList<Long>) object.get("Types owned");
        ArrayList<Integer> typesOwned = null;
        for (Long types : typesOwnedAsLong) {
            typesOwned.add(types.intValue());
        }
        String mapPath = (String) object.get("Map path");

        Hero hero = new Hero(x, y);
        UserData userData = UserData.getInstance();

        hero.setType(type);
        hero.setRemainingLives(lives);
        hero.setPoints(points);
        hero.setInvincibilityTimer(timer);
        hero.setCoins(coins);
        hero.setToRight(toRight);
        hero.setDimension(new Dimension(width, height));
        hero.setGravityAcc(gravityAcc);
        hero.setFalling(falling);
        hero.setJumping(jumping);
        hero.getHeroForm().setType(formType);
        hero.getHeroForm().setSuper(isSuper);
        hero.getHeroForm().setCanShootFire(canShootFire);
        hero.setMapPath(mapPath);

        userData.setTypesOwned(typesOwned);
        userData.setHero(hero);
        userData.setMap(getMap(fileNumber));

        return userData;
    }

    public Map getMap(int fileNumber) throws IOException {

        jsonToReadFile(fileNumber);
        String mapPath = (String) object.get("Map path");
        MapManager.getInstance().createMap(mapPath);

        return MapManager.getInstance().getMap();
    }


    private void jsonToReadFile(int fileNumber) throws IOException {

        String address;

        if (fileNumber == FILE_1) {
            address = pathToFile1;
        } else if (fileNumber == FILE_2) {
            address = pathToFile2;
        } else {
            address = pathToFile3;
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

    public boolean isFileNumberOneEmpty() {
        File file1 = new File(pathToFile1);
        return file1.length() == 0;
    }

    public boolean isFileNumberTwoEmpty() {
        File file2 = new File(pathToFile2);
        return file2.length() == 0;
    }

    public boolean isFileNumberThreeEmpty() {
        File file3 = new File(pathToFile3);
        return file3.length() == 0;
    }

}
