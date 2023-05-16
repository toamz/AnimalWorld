import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.model.WorldLoader;
import cz.cvut.fel.zahorto2.animalworld.model.entities.EntityType;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class WorldLoaderTest {
    @Test
    public void loadWorldText() throws IOException {
        File file = File.createTempFile("world", ".txt");

        java.nio.file.Files.write(file.toPath(), "2;2\nGRASS;;WATER;WOLF\nGRASS;SHEEP;GRASS;\n".getBytes());

        World world = WorldLoader.loadText(file);

        assertEquals(2, world.getWidth());
        assertEquals(2, world.getHeight());

        assertEquals(TileType.GRASS, world.getTileGrid().getTile(0, 0).getType());
        assertEquals(TileType.WATER, world.getTileGrid().getTile(1, 0).getType());
        assertEquals(TileType.GRASS, world.getTileGrid().getTile(0, 1).getType());
        assertEquals(TileType.GRASS, world.getTileGrid().getTile(1, 1).getType());

        assertNull(world.getEntityMap().getEntity(0, 0));
        assertEquals(EntityType.WOLF, world.getEntityMap().getEntity(1, 0).getType());
        assertEquals(EntityType.SHEEP, world.getEntityMap().getEntity(0, 1).getType());
        assertNull(world.getEntityMap().getEntity(1, 1));
    }

    @Test
    public void saveWorldText() throws IOException {
        World world = new World(2, 2);
        world.getTileGrid().setTile(TileType.GRASS.createTile(), 0, 0);
        world.getTileGrid().setTile(TileType.WATER.createTile(), 1, 0);
        world.getTileGrid().setTile(TileType.GRASS.createTile(), 0, 1);
        world.getTileGrid().setTile(TileType.GRASS.createTile(), 1, 1);
        world.getEntityMap().setEntity(EntityType.WOLF.createEntity(world), 1, 0);
        world.getEntityMap().setEntity(EntityType.SHEEP.createEntity(world), 0, 1);

        File file = File.createTempFile("world", ".txt");
        WorldLoader.saveText(world, file);

        String worldText = new String(java.nio.file.Files.readAllBytes(file.toPath()));

        assertEquals( String.format("2;2%nGRASS;;WATER;WOLF%nGRASS;SHEEP;GRASS;%n"), worldText); }
}
