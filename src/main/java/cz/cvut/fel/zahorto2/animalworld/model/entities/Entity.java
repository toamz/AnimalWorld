package cz.cvut.fel.zahorto2.animalworld.model.entities;

import java.io.Serializable;

public interface Entity extends Serializable {
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
