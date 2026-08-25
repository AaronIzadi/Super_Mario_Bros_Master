package SuperMario.model.prize;

public enum PrizeType {

    SUPER_MUSHROOM(30),
    FIRE_FLOWER(20),
    HEART_MUSHROOM(50),
    SUPER_STAR(40);

    private final int points;

    PrizeType(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
