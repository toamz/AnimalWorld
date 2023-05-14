package cz.cvut.fel.zahorto2.animalworld.view.tiles;

import cz.cvut.fel.zahorto2.animalworld.model.tiles.Water;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WaterRenderer {
    private WaterRenderer() {}
    public static void render(Water tile, GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.rgb(32, 32, 255));
        graphicsContext.setLineWidth(0);
        graphicsContext.fillRect(0, 0, 1, 1);
    }
}
