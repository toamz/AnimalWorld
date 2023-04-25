package cz.cvut.fel.zahorto2.animalworld.model.entities;

public interface Entity {
    /**
     * Called every tick.
     */
    void tick();

    /**
     * Called when something kills the entity.
     * The entity should be removed from the world.
     */
    void die();
}
