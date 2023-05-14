package cz.cvut.fel.zahorto2.animalworld.model.entities;

import cz.cvut.fel.zahorto2.animalworld.CoordInt;
import cz.cvut.fel.zahorto2.animalworld.model.World;

public class Wolf extends Animal {

    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }

    @Override
    protected int getEatEnergy() {
        return 200;
    }

    @Override
    protected int getMaxEnergy() {
        return 300;
    }

    @Override
    protected int getStartEnergy() {
        return 100;
    }

    @Override
    protected int getReproduceChance() {
        return 10;
    }

    public Wolf(World world){
        super(world);
    }

    @Override
    protected boolean eat() {
        CoordInt position = this.getPosition();
        CoordInt[] randomNeighbour = world.getEntityMap().getNeighbours(position);
        for (CoordInt coordInt : randomNeighbour) {
            Entity entity = world.getEntityMap().getEntity(coordInt.x, coordInt.y);
            if (entity instanceof Sheep sheep) {
                sheep.die();
                return true;
            }
        }
        return false;
    }

    @Override
    protected Animal createBaby(Animal other) {
        return new Wolf(world);
    }
}
