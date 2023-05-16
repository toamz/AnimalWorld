import cz.cvut.fel.zahorto2.animalworld.model.tiles.Tile;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;

public class TestingTile implements Tile {
    public int tickCount = 0;

    @Override
    public void tick() {
        tickCount++;
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

    @Override
    public TileType getType() {
        return null;
    }
}
