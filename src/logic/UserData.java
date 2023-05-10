package logic;

import model.Map;
import model.hero.Hero;
import model.hero.HeroType;
import repository.LoadGameRepository;
import repository.SaveGameRepository;

import java.util.ArrayList;

public class UserData {

    private Hero hero;
    private Map map;
    private boolean[] typesOwned;
    private final LoadGameRepository loadGameRepository;
    private final SaveGameRepository saveGameRepository;
    private static final UserData instance = new UserData();

    private UserData(){
        this.loadGameRepository = new LoadGameRepository();
        this.saveGameRepository = new SaveGameRepository();
        this.typesOwned = new boolean[5];
        setTypesOwned();
    }

    public static UserData getInstance() {
        return instance;
    }

    private void setTypesOwned(){
        typesOwned[HeroType.MARIO] = true;
        typesOwned[HeroType.LUIGI] = false;
        typesOwned[HeroType.PRINCE_PEACH] = false;
        typesOwned[HeroType.ROSS] = false;
        typesOwned[HeroType.TOAD] = false;
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

    public boolean[] getTypesOwned() {
        return typesOwned;
    }

    public void setTypesOwned(boolean[] typesOwned) {
        this.typesOwned = typesOwned;
    }

    public LoadGameRepository getLoadGameRepository() {
        return loadGameRepository;
    }

    public SaveGameRepository getSaveGameRepository() {
        return saveGameRepository;
    }
}
