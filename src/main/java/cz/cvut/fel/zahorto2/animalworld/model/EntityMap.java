package cz.cvut.fel.zahorto2.animalworld.model;

import cz.cvut.fel.zahorto2.animalworld.CoordInt;
import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * A map of entities.
 */
public class EntityMap implements Serializable {
    private int width;
    private int height;
    private Entity[][] entities;
    private Map<Entity, CoordInt> entityPositions;
    private Random random = new Random();

    /**
     * Creates a new entity map with the given dimensions.
     * @param width the width of the entity map
     * @param height the height of the entity map
     * @throws IllegalArgumentException if the dimensions are not positive
     */
    public EntityMap(int width, int height) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Entity map dimensions must be positive");
        this.width = width;
        this.height = height;
        this.entities = new Entity[width][height];
        this.entityPositions = new HashMap<>();
    }

    /**
     * Returns the entity at the given position, or null if there is no entity there.
     * @param x the x position
     * @param y the y position
     * @return the entity at the given position, or null if there is no entity there
     * @throws IllegalArgumentException if the position is out of bounds
     */
    public synchronized Entity getEntity(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Entity position out of bounds");
        return entities[x][y];
    }

    /**
     * Returns the position of the given entity, or null if the entity is not in the world.
     * @param entity the entity
     * @return the position of the entity, or null if the entity is not in the world
     */
    public synchronized CoordInt getEntityPosition(Entity entity) {
        return entityPositions.get(entity);
    }

    /**
     * Gets array of all entities in the map.
     * @implNote the value returned is a copy and won't be modified as the map updates to prevent concurrent modification exceptions
     */
    public synchronized Entity[] getEntities() {
        return entityPositions.keySet().toArray(new Entity[0]);
    }

    /**
     * Adds the given entity to the world at the given position and removes it from its previous position if it was in the world before.
     * If the entity is null, the entity at the given position is removed.
     * @param entity the entity to add, or null to remove the entity at the given position
     * @param x the x position
     * @param y the y position
     * @throws IllegalArgumentException if the position is out of bounds or already occupied
     * @throws NullPointerException if the entity is null
     */
    public synchronized void setEntity(Entity entity, int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Entity position out of bounds");

        if (entity == null) {
            entityPositions.remove(entities[x][y]);
            entities[x][y] = null;
            return;
        }

        if (entityPositions.containsKey(entity)){
            CoordInt oldPosition = entityPositions.get(entity);
            entities[oldPosition.x][oldPosition.y] = null;
        }
        if (entities[x][y] != null)
            throw new IllegalArgumentException("Entity position already occupied");

        entityPositions.put(entity, new CoordInt(x, y));
        entities[x][y] = entity;
    }

    /**
     * Ticks all entities in the world.
     * The entities are ticked in an arbitrary order.
     */
    public void tick() {
        // make a copy of the entities to avoid concurrent modification
        Entity[] entitiesCopy = entityPositions.keySet().toArray(new Entity[0]);
        for (Entity entity : entitiesCopy) {
            if (!entityPositions.containsKey(entity)) // something might have killed the entity
                continue;
            entity.tick();
        }
    }

    public CoordInt[] getNeighbours(CoordInt position) {
        int x = position.x;
        int y = position.y;
        CoordInt[] neighbours = new CoordInt[] {
                new CoordInt(x - 1, y),
                new CoordInt(x, y - 1),
                new CoordInt(x, y + 1),
                new CoordInt(x + 1, y)
        };

        ArrayList<CoordInt> validNeighbours = new ArrayList<>(neighbours.length);
        for (CoordInt neighbour : neighbours) {
            if (neighbour.x < 0 || neighbour.x >= width || neighbour.y < 0 || neighbour.y >= height)
                continue;
            validNeighbours.add(neighbour);
        }

        return validNeighbours.toArray(new CoordInt[0]);
    }

    public synchronized CoordInt getRandomEmptyNeighbour(CoordInt position) {
        CoordInt[] neighbours = this.getNeighbours(position);

        CoordInt[] emptyNeighbours = new CoordInt[4];
        int emptyNeighboursCount = 0;
        for (CoordInt neighbour : neighbours) {
            if (entities[neighbour.x][neighbour.y] == null)
                emptyNeighbours[emptyNeighboursCount++] = neighbour;
        }

        if (emptyNeighboursCount == 0)
            return null;
        return emptyNeighbours[random.nextInt(emptyNeighboursCount)];
    }

    public CoordInt getRandomNeighbour(CoordInt position) {
        CoordInt[] neighbours = this.getNeighbours(position);
        return neighbours[random.nextInt(neighbours.length)];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(entities);
    }
    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        entities = (Entity[][]) in.readObject();
        entityPositions = new HashMap<>();
        for (int x = 0; x < entities.length; x++) {
            for (int y = 0; y < entities[x].length; y++) {
                if (entities[x][y] != null)
                    entityPositions.put(entities[x][y], new CoordInt(x, y));
            }
        }
        width = entities.length;
        height = entities[0].length;

        random = new Random();
    }
}
