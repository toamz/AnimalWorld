package cz.cvut.fel.zahorto2.animalworld.view.entities;

import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;
import cz.cvut.fel.zahorto2.animalworld.model.entities.Sheep;
import cz.cvut.fel.zahorto2.animalworld.model.entities.Wolf;
import javafx.scene.canvas.GraphicsContext;

public class EntityRenderer {
    EntityRenderer() {}

    public static void render(Entity tile, GraphicsContext graphicsContext) {
        if (tile instanceof Wolf wolf) {
            WolfRenderer.render(wolf, graphicsContext);
        } else if (tile instanceof Sheep sheep) {
            SheepRenderer.render(sheep, graphicsContext);
        } else {
            throw new IllegalArgumentException("Unknown tile type");
        }
    }
}
