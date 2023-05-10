package logic;

import model.Map;
import model.hero.Hero;
import repository.LoadGameRepository;
import repository.SaveGameRepository;

import java.util.ArrayList;

public class UserData {

    private Hero hero;
    private Map map;
    private ArrayList<Integer> typesOwned;
    private final LoadGameRepository loadGameRepository;
    private final SaveGameRepository saveGameRepository;

    private static final UserData instance = new UserData();

    private UserData(){
        this.typesOwned = new ArrayList<>();
        this.loadGameRepository = new LoadGameRepository();
        this.saveGameRepository = new SaveGameRepository();
    }

    public static UserData getInstance() {
        return instance;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public ArrayList<Integer> getTypesOwned() {
        return typesOwned;
    }

    public void setTypesOwned(ArrayList<Integer> typesOwned) {
        this.typesOwned = typesOwned;
    }

    public LoadGameRepository getLoadGameRepository() {
        return loadGameRepository;
    }

    public SaveGameRepository getSaveGameRepository() {
        return saveGameRepository;
    }
}
