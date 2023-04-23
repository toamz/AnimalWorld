package cz.cvut.fel.zahorto2.animalworld.model.tiles;

/**
 * A tile in the world.
 */
public interface Tile {
    /**
     * Called every tick.
     */
    void tick();

    /**
     * Provides information about the tile being walkable.
     * @return true if the tile is walkable, false otherwise
     */
    boolean isWalkable();

    /**
     * Provides information about the tile being eatable.
     * The value might change during the simulation (e.g. grass was eaten and is growing).
     * @return true if the tile is eatable, false otherwise
     */
    boolean isEatable();

    /**
     * Called when an entity tries to eat the tile.
     * @return true if the tile was eaten, false otherwise
     */
    boolean beEaten();
}
