package tiles;

import cz.cvut.fel.zahorto2.animalworld.model.TileGrid;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.Grass;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrassTest {
    @Test
    public void grassCreation() {
        Grass grass = new Grass();

        assertEquals(TileType.GRASS, grass.getType());
        assertEquals(Grass.EDIBLE_AGE, grass.getAge());
        assertTrue(grass.isEatable());
        assertTrue(grass.isWalkable());
    }

    @Test
    public void beEaten() {
        Grass grass = new Grass();
        assertEquals(Grass.EDIBLE_AGE, grass.getAge());

        assertTrue(grass.beEaten());
        assertEquals(0, grass.getAge());
        assertFalse(grass.isEatable());

        assertFalse(grass.beEaten());
    }

    @Test
    public void tick() {
        Grass grass = new Grass();

        int age = grass.getAge();
        grass.tick();
        assertEquals(age + 1, grass.getAge());

        assertTrue(grass.beEaten());
        age = grass.getAge();
        grass.tick();
        assertEquals(age + 1, grass.getAge());

        for (int i = age + 1; i <= Grass.EDIBLE_AGE; i++) {
            grass.tick();
            assertEquals(i + 1, grass.getAge());
        }

    }
}
