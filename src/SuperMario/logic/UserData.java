package SuperMario.logic;


import SuperMario.model.map.Map;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.HeroType;
import SuperMario.repository.LoadGameRepository;
import SuperMario.repository.SaveGameRepository;

public class UserData {

    private Hero hero;
    private Map map;
    private String mapPath;
    private int worldNumber;
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
        typesOwned[HeroType.PRINCESS_PEACH] = false;
        typesOwned[HeroType.ROSALINA] = false;
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

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }

    public int getWorldNumber() {
        return worldNumber;
    }

    public void setWorldNumber(int worldNumber) {
        this.worldNumber = worldNumber;
    }

    public void clear(){
        hero = null;
        map = null;
        worldNumber = 0;
        setTypesOwned();
    }
}
