package cz.cvut.fel.zahorto2.animalworld.view.tiles;

import cz.cvut.fel.zahorto2.animalworld.model.tiles.Grass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GrassRenderer {
    private GrassRenderer() {}
    public static void render(Grass tile, GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.setLineWidth(0);
        graphicsContext.fillRect(0, 0, 1, 1);
    }
}
