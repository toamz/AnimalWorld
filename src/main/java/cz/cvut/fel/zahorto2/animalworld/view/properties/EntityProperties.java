package cz.cvut.fel.zahorto2.animalworld.view.properties;

import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Class which holds a list of all changeable properties of an entity.
 */
@SuppressWarnings("java:S110")
public class EntityProperties extends VBox {
    private World world;
    protected EntityPropertyAccessor propertyAccessor;

    public EntityProperties() {
        super();
    }

    /**
     * Sets the world in which the entity is. This is used to add the "Apply to all" button.
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Sets the entity whose properties should be displayed.
     * The entity has to have PropertyAccessor defined for its type.
     */
    public void setEntity(Entity entity) {
        this.getChildren().clear();
        if (entity == null) {
            return;
        }

        switch (entity.getType()) {
            case WOLF, SHEEP:
                propertyAccessor = new AnimalPropertyAccessor();
                break;
            default:
                throw new IllegalArgumentException("Invalid entity type: " + entity.getType());
        }

        var children = this.getChildren();
        children.clear();

        for (String property : propertyAccessor.getIntProperties()) {
            children.add(new Label(property));
            IntField intField = new IntField();
            intField.valueProperty.set(propertyAccessor.getIntProperty(entity, property));
            intField.valueProperty.addListener((observable, oldValue, newValue) -> propertyAccessor.setIntProperty(entity, property, newValue.intValue()));
            children.add(intField);
        }

        if (world != null) {
            addApplyToAllButton(entity, children);
        }
    }

    /**
     * Adds the "Apply to all" button to the list of properties.
     * This button sets the properties of all entities of the same type as the given entity to the same values.
     * World has to be set before calling this method.
     */
    private void addApplyToAllButton(Entity entity, ObservableList<Node> children) {
        Button applyToAll = new Button("Apply to all");
        applyToAll.setOnAction(event -> {
            for (Entity e : world.getEntityMap().getEntities()) {
                if (e.getType() == entity.getType()) {
                    for (String property : propertyAccessor.getIntProperties()) {
                        propertyAccessor.setIntProperty(e, property, propertyAccessor.getIntProperty(entity, property));
                    }
                }
            }
        });
        children.add(applyToAll);
    }
}
