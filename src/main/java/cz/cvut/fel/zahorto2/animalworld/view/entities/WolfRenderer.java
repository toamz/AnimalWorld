package cz.cvut.fel.zahorto2.animalworld.view.entities;

import cz.cvut.fel.zahorto2.animalworld.model.entities.Wolf;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WolfRenderer {
    private WolfRenderer() {}
    public static void render(Wolf wolf, GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.RED);
        graphicsContext.setLineWidth(0);
        graphicsContext.fillRect(0.2, 0.2, 0.6, 0.6);
    }
}
