package cz.cvut.fel.zahorto2.animalworld.controller;

import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.view.PropertyEditorRenderer;
import cz.cvut.fel.zahorto2.animalworld.view.WorldRenderer;
import cz.cvut.fel.zahorto2.animalworld.view.properties.EntityProperties;
import javafx.fxml.FXML;

public class PropertyEditor {
    @FXML
    public PropertyEditorRenderer renderer;
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
