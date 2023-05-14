package cz.cvut.fel.zahorto2.animalworld.model.entities;

import cz.cvut.fel.zahorto2.animalworld.model.World;

public enum EntityType {
    WOLF,
    SHEEP;

    /**
     * Creates a new entity of type.
     * @return a new entity of type
     */
    public Entity createEntity(World world) {
        switch (this) {
            case WOLF:
                return new Wolf(world);
            case SHEEP:
                return new Sheep(world);
            default:
                throw new IllegalArgumentException("Unknown entity type");
        }
    }
}
