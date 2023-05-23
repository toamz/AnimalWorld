package cz.cvut.fel.zahorto2.animalworld.view.properties;

import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;

public interface EntityPropertyAccessor {
    String[] getIntProperties();
    int getIntProperty(Entity entity, String property);
    void setIntProperty(Entity entity, String property, int value);
}
