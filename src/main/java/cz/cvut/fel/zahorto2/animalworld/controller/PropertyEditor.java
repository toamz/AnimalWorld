package cz.cvut.fel.zahorto2.animalworld.controller;

import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.view.WorldEntitySelectionRenderer;
import cz.cvut.fel.zahorto2.animalworld.view.properties.EntityProperties;
import javafx.fxml.FXML;

/**
 * Controller for the property editor
 */
public class PropertyEditor {
    @FXML
    public WorldEntitySelectionRenderer renderer;
    public EntityProperties entityProperties;

    @FXML
    public void initialize() {
        renderer.addEntitySelectionListener(entity -> entityProperties.setEntity(entity));
    }

    public void setWorld(World world) {
        renderer.setWorld(world);
        entityProperties.setWorld(world);
    }
}
