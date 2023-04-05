package cz.cvut.fel.zahorto2.animalworld.Model;

/**
 * World of the simulation.
 */
public class World {
    private final int width;
    private final int height;

    private final EntityMap entityMap;

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
    }
}
