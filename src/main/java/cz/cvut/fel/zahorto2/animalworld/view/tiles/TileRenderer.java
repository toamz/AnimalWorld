package cz.cvut.fel.zahorto2.animalworld.view.tiles;

import cz.cvut.fel.zahorto2.animalworld.model.tiles.Grass;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.Tile;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.Water;
import javafx.scene.canvas.GraphicsContext;

public class TileRenderer {
    private TileRenderer() {}
    public static void render(Tile tile, GraphicsContext graphicsContext) {
        if (tile instanceof Water water) {
            WaterRenderer.render(water, graphicsContext);
        } else if (tile instanceof Grass grass) {
            GrassRenderer.render(grass, graphicsContext);
        } else {
            throw new IllegalArgumentException("Unknown tile type");
        }
    }
}
