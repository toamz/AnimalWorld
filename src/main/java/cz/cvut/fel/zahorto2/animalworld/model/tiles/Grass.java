package cz.cvut.fel.zahorto2.animalworld.model.tiles;

public class Grass implements Tile {
    public static final int EDIBLE_AGE = 100;
    int age = EDIBLE_AGE;

    @Override
    public TileType getType() {
        return TileType.GRASS;
    }

    @Override
    public void tick() {
        age++;
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public boolean isEatable() {
        return age > EDIBLE_AGE;
    }

    @Override
    public boolean beEaten() {
        if (isEatable()) {
            age = 0;
            return true;
        }
        return false;
    }

    public int getAge() {
        return age;
    }
}
