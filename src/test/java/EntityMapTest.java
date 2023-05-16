import cz.cvut.fel.zahorto2.animalworld.CoordInt;
import cz.cvut.fel.zahorto2.animalworld.model.EntityMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntityMapTest {
    @Test
    public void entityMapCreation() {
        EntityMap entityMap = new EntityMap(11, 17);

        assertEquals(11, entityMap.getWidth());
        assertEquals(17, entityMap.getHeight());

        for (int x = 0; x < entityMap.getWidth(); x++)
            for (int y = 0; y < entityMap.getHeight(); y++)
                assertNull(entityMap.getEntity(x, y));
    }

    @Test
    public void setEntityAdd() {
        EntityMap entityMap = new EntityMap(11, 17);
        TestingEntity entity = new TestingEntity();

        entityMap.setEntity(entity, 0, 0);
        assertEquals(entity, entityMap.getEntity(0, 0));
    }

    @Test
    public void setEntityRemove() {
        EntityMap entityMap = new EntityMap(11, 17);
        TestingEntity entity = new TestingEntity();

        entityMap.setEntity(entity, 0, 0);
        assertEquals(entity, entityMap.getEntity(0, 0));

        entityMap.setEntity(null, 0, 0);
        assertNull(entityMap.getEntity(0, 0));
    }

    @Test
    public void setEntityMove() {
        EntityMap entityMap = new EntityMap(11, 17);
        TestingEntity entity = new TestingEntity();

        entityMap.setEntity(entity, 0, 0);
        assertEquals(entity, entityMap.getEntity(0, 0));

        entityMap.setEntity(entity, 10, 16);
        assertEquals(entity, entityMap.getEntity(10, 16));
        assertNull(entityMap.getEntity(0, 0));
    }

    @Test
    public void getEntityPosition() {
        EntityMap entityMap = new EntityMap(11, 17);
        TestingEntity entity = new TestingEntity();

        entityMap.setEntity(entity, 0, 0);
        assertEquals(0, entityMap.getEntityPosition(entity).x);
        assertEquals(0, entityMap.getEntityPosition(entity).y);

        entityMap.setEntity(entity, 10, 16);
        assertEquals(10, entityMap.getEntityPosition(entity).x);
        assertEquals(16, entityMap.getEntityPosition(entity).y);

        entityMap.setEntity(entity, 5, 8);
        assertEquals(5, entityMap.getEntityPosition(entity).x);
        assertEquals(8, entityMap.getEntityPosition(entity).y);
    }

    @Test
    public void tick() {
        EntityMap entityMap = new EntityMap(11, 17);
        TestingEntity entity = new TestingEntity();

        entityMap.setEntity(entity, 0, 0);
        entityMap.tick();
        assertEquals(1, entity.tickCount);

        entityMap.setEntity(entity, 10, 16);
        entityMap.tick();
        assertEquals(2, entity.tickCount);

        entityMap.setEntity(entity, 5, 8);
        entityMap.tick();
        assertEquals(3, entity.tickCount);
    }

    private boolean contains(Object[] array, Object object) {
        for (Object arrayObject : array)
            if (arrayObject.equals(object))
                return true;

        return false;
    }

    /**
     * Checks if two arrays contain the same objects in any order.
     */
    private boolean arraysSame(Object[] array1, Object[] array2) {
        if (array1.length != array2.length)
            return false;

        for (Object object : array1)
            if (!contains(array2, object))
                return false;

        for (Object object : array2)
            if (!contains(array1, object))
                return false;

        return true;
    }

    @Test
    public void getNeighboursEmpty() {
        EntityMap entityMap = new EntityMap(11, 17);

        CoordInt[] neighbours = entityMap.getNeighbours(new CoordInt(0, 0));
        assertTrue(arraysSame(neighbours, new CoordInt[] {
            new CoordInt(1, 0),
            new CoordInt(0, 1)
        }));

        neighbours = entityMap.getNeighbours(new CoordInt(10, 16));
        assertTrue(arraysSame(neighbours, new CoordInt[] {
            new CoordInt(9, 16),
            new CoordInt(10, 15)
        }));

        neighbours = entityMap.getNeighbours(new CoordInt(5, 8));
        assertTrue(arraysSame(neighbours, new CoordInt[] {
            new CoordInt(4, 8),
            new CoordInt(6, 8),
            new CoordInt(5, 7),
            new CoordInt(5, 9)
        }));
    }

    @Test
    public void getNeighboursFull() {
        EntityMap entityMap = new EntityMap(11, 17);

        entityMap.setEntity(new TestingEntity(), 0, 0);
        entityMap.setEntity(new TestingEntity(), 1, 0);
        entityMap.setEntity(new TestingEntity(), 0, 1);

        CoordInt[] neighbours = entityMap.getNeighbours(new CoordInt(0, 0));
        assertTrue(arraysSame(neighbours, new CoordInt[] {
            new CoordInt(1, 0),
            new CoordInt(0, 1)
        }));
    }

    @Test
    public void getRandomNeighbour() {
        EntityMap entityMap = new EntityMap(11, 17);

        CoordInt neighbour = entityMap.getRandomNeighbour(new CoordInt(0, 0));
        assertTrue(contains(new CoordInt[] {
            new CoordInt(1, 0),
            new CoordInt(0, 1)
        }, neighbour));

        neighbour = entityMap.getRandomNeighbour(new CoordInt(10, 16));
        assertTrue(contains(new CoordInt[] {
            new CoordInt(9, 16),
            new CoordInt(10, 15)
        }, neighbour));
    }

    @Test
    public void getRandomEmptyNeighbour() {
        EntityMap entityMap = new EntityMap(11, 17);

        entityMap.setEntity(new TestingEntity(), 0, 0);
        entityMap.setEntity(new TestingEntity(), 1, 0);

        CoordInt neighbour = entityMap.getRandomEmptyNeighbour(new CoordInt(0, 0));
        assertTrue(contains(new CoordInt[] {
            new CoordInt(0, 1)
        }, neighbour));

        neighbour = entityMap.getRandomEmptyNeighbour(new CoordInt(10, 16));
        assertTrue(contains(new CoordInt[] {
            new CoordInt(9, 16),
            new CoordInt(10, 15)
        }, neighbour));

        neighbour = entityMap.getRandomEmptyNeighbour(new CoordInt(5, 8));
        assertTrue(contains(new CoordInt[] {
            new CoordInt(4, 8),
            new CoordInt(6, 8),
            new CoordInt(5, 7),
            new CoordInt(5, 9)
        }, neighbour));

        entityMap.setEntity(new TestingEntity(), 4, 8);
        entityMap.setEntity(new TestingEntity(), 5, 7);

        neighbour = entityMap.getRandomEmptyNeighbour(new CoordInt(5, 8));
        assertTrue(contains(new CoordInt[] {
            new CoordInt(6, 8),
            new CoordInt(5, 9)
        }, neighbour));
    }
}
