package cz.cvut.fel.zahorto2.animalworld.model;

import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * World of the simulation.
 */
public class World implements Serializable {
    public interface TickListener {
        void onWorldTick(World world);
    }
    private final ArrayList<TickListener> listeners = new ArrayList<>();
    public void addTickListener(TickListener listener) {
        listeners.add(listener);
    }
    public void removeTickListener(TickListener listener) {
        listeners.remove(listener);
    }
    private void callTickListeners() {
        for (TickListener listener : listeners)
            listener.onWorldTick(this);
    }

    private int width;
    private int height;
    private int tickCounter = 0;

    private EntityMap entityMap;
    private TileGrid tileGrid;

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

    public void tick() {
        tileGrid.tick();
        entityMap.tick();
        tickCounter++;
        callTickListeners();
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
     * Gets number of ticks since this world has been created.
     */
    public int getTickCount() {
        return tickCounter;
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

    @Serial
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(entityMap);
        out.writeObject(tileGrid);
    }
    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        entityMap = (EntityMap) in.readObject();
        tileGrid = (TileGrid) in.readObject();

        width = tileGrid.getWidth();
        height = tileGrid.getHeight();

        assert width == entityMap.getWidth();
        assert height == entityMap.getHeight();
    }
}
