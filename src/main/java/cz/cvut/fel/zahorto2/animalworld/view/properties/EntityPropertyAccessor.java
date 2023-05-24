package cz.cvut.fel.zahorto2.animalworld.view.properties;

import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;

/**
 * Interface for accessing properties of an entity.
 */
public interface EntityPropertyAccessor {
    /**
     * Returns a list of all properties that can be accessed.
     */
    String[] getIntProperties();

    /**
     * Returns the value of the given property of the entity.
     */
    int getIntProperty(Entity entity, String property);

    /**
     * Sets the value of the given property of the entity.
     */
    void setIntProperty(Entity entity, String property, int value);
}
