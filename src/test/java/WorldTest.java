import cz.cvut.fel.zahorto2.animalworld.model.World;
import org.junit.Test;
import static org.junit.Assert.*;
public class WorldTest {
    @Test
    public void worldCreation() {
        World world = new World(11, 17);

        assertEquals(11, world.getWidth());
        assertEquals(17, world.getHeight());

        assertEquals(11, world.getTileGrid().getWidth());
        assertEquals(17, world.getTileGrid().getHeight());

        assertEquals(11, world.getEntityMap().getWidth());
        assertEquals(17, world.getEntityMap().getHeight());
    }
}