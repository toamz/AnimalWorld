package cz.cvut.fel.zahorto2.animalworld.view.properties;

import cz.cvut.fel.zahorto2.animalworld.model.entities.Animal;
import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;

/**
 * Class for accessing properties of an animal.
 */
@SuppressWarnings("java:S1192")
public class AnimalPropertyAccessor implements EntityPropertyAccessor{
    public String[] getIntProperties() {
        return new String[] {
            "energy",
            "age",
            "maxEnergy",
            "startEnergy",
            "eatEnergy",
            "reproduceChance",
            "maxAge",
            "reproduceAge"
        };
    }

    @Override
    public int getIntProperty(Entity entity, String property) {
        Animal animal = (Animal) entity;

        switch (property) {
            case "energy" -> { return animal.getEnergy(); }
            case "age" -> { return animal.getAge(); }
            case "maxEnergy" -> { return animal.getMaxEnergy(); }
            case "startEnergy" -> { return animal.getStartEnergy(); }
            case "eatEnergy" -> { return animal.getEatEnergy(); }
            case "reproduceChance" -> { return animal.getReproduceChance(); }
            case "maxAge" -> { return animal.getMaxAge(); }
            case "reproduceAge" -> { return animal.getReproduceAge(); }
            default -> throw new IllegalArgumentException("Invalid property: " + property);
        }
    }

    public void setIntProperty(Entity entity, String property, int value) {
        Animal animal = (Animal) entity;

        switch (property) {
            case "energy" -> animal.setEnergy(value);
            case "age" -> animal.setAge(value);
            case "maxEnergy" -> animal.setMaxEnergy(value);
            case "startEnergy" -> animal.setStartEnergy(value);
            case "eatEnergy" -> animal.setEatEnergy(value);
            case "reproduceChance" -> animal.setReproduceChance(value);
            case "maxAge" -> animal.setMaxAge(value);
            case "reproduceAge" -> animal.setReproduceAge(value);
            default -> throw new IllegalArgumentException("Invalid property: " + property);
        }
    }
}
