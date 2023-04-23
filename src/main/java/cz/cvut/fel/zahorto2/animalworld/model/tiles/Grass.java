package cz.cvut.fel.zahorto2.animalworld.model.tiles;

public class Grass implements Tile {
    int age = 0;

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
        return age > 10;
    }

    @Override
    public boolean beEaten() {
        if (isEatable()) {
            age = 0;
            return true;
        }
        return false;
    }
}
