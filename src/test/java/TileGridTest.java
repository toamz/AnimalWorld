import cz.cvut.fel.zahorto2.animalworld.model.TileGrid;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TileGridTest {
    @Test
    public void tileGridCreation() {
        TileGrid tileGrid = new TileGrid(11, 17, TileType.GRASS);

        assertEquals(11, tileGrid.getWidth());
        assertEquals(17, tileGrid.getHeight());

        for (int x = 0; x < tileGrid.getWidth(); x++)
            for (int y = 0; y < tileGrid.getHeight(); y++)
                assertEquals(TileType.GRASS, tileGrid.getTile(x, y).getType());
    }

    @Test
    public void tileGridSetTile() {
        TileGrid tileGrid = new TileGrid(11, 17, TileType.GRASS);
        TestingTile testingTile = new TestingTile();
        tileGrid.setTile(testingTile, 5, 5);

        assertEquals(testingTile, tileGrid.getTile(5, 5));
    }



    @Test
    public void tick() {
        TileGrid tileGrid = new TileGrid(11, 17, TileType.GRASS);
        TestingTile testingTile = new TestingTile();
        tileGrid.setTile(testingTile, 5, 5);

        assertEquals(0, testingTile.tickCount);
        tileGrid.tick();
        assertEquals(1, testingTile.tickCount);
        tileGrid.tick();
        assertEquals(2, testingTile.tickCount);
    }
}
