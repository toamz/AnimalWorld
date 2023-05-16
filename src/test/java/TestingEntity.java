import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;
import cz.cvut.fel.zahorto2.animalworld.model.entities.EntityType;

public class TestingEntity implements Entity {
    public int tickCount = 0;
    public int dieCount = 0;

    @Override
    public void tick() {
        tickCount++;
    }

    @Override
    public void die() {
        dieCount++;
    }

    @Override
    public EntityType getType() {
        return null;
    }
}
