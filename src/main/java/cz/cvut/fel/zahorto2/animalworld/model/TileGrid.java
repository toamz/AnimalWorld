package cz.cvut.fel.zahorto2.animalworld.model;

import cz.cvut.fel.zahorto2.animalworld.model.tiles.Tile;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;

/**
 * A tile grid.
 */
public class TileGrid {
    private final int width;
    private final int height;
    private final Tile[][] tiles;

    /**
     * Creates a new tile grid with the given dimensions.
     * @param width the width of the tile grid
     * @param height the height of the tile grid
     * @throws IllegalArgumentException if the dimensions are not positive
     */
    public TileGrid(int width, int height, TileType defaultTile) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Tile grid dimensions must be positive");
        if (defaultTile == null)
            throw new IllegalArgumentException("Default tile must not be null");

        this.width = width;
        this.height = height;

        this.tiles = new Tile[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                tiles[x][y] = defaultTile.createTile();
    }

    /**
     * Returns the tile at the given position.
     * @param x the x position
     * @param y the y position
     * @return the tile at the given position
     * @throws IllegalArgumentException if the position is out of bounds
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Tile position out of bounds");

        return tiles[x][y];
    }

    /**
     * Sets the tile at the given position.
     * @param tile the tile to set
     * @param x the x position
     * @param y the y position
     * @throws IllegalArgumentException if the position is out of bounds
     */
    public void setTile(Tile tile, int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Tile position out of bounds");
        if (tile == null)
            throw new IllegalArgumentException("Tile must not be null");

        tiles[x][y] = tile;
    }

    /**
     * Ticks all tiles.
     */
    public void tick() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                tile.tick();
    }
}
