package cz.cvut.fel.zahorto2.animalworld.view.entities;

import cz.cvut.fel.zahorto2.animalworld.model.entities.Sheep;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SheepRenderer {
    private SheepRenderer() {}
    public static void render(Sheep sheep, GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.setLineWidth(0);
        graphicsContext.fillRect(0.2, 0.2, 0.6, 0.6);
    }
}
