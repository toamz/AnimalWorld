package cz.cvut.fel.zahorto2.animalworld.model;

import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;

/**
 * World of the simulation.
 */
public class World {
    private final int width;
    private final int height;

    private final EntityMap entityMap;
    private final TileGrid tileGrid;

    /**
     * Creates a new world with the given dimensions.
     * @param width the width of the world
     * @param height the height of the world
     * @throws IllegalArgumentException if the dimensions are not positive
     */
    public World(int width, int height) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("World dimensions must be positive");
        this.width = width;
        this.height = height;
        this.entityMap = new EntityMap(width, height);
        this.tileGrid = new TileGrid(width, height, TileType.GRASS);
    }

    /**
     * Returns the width of the world.
     * @return the width of the world
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the world.
     * @return the height of the world
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns entity map.
     * @return entity map
     */
    public EntityMap getEntityMap() {
        return entityMap;
    }

    /**
     * Returns tile grid.
     * @return tile grid
     */
    public TileGrid getTileGrid() {
        return tileGrid;
    }
}