package cz.cvut.fel.zahorto2.animalworld.model.tiles;

public class Water implements Tile {

    @Override
    public TileType getType() {
        return TileType.WATER;
    }

    @Override
    public void tick() {
        // do nothing
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public boolean isEatable() {
        return false;
    }

    @Override
    public boolean beEaten() {
        return false;
    }
}
