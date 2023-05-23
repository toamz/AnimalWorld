package cz.cvut.fel.zahorto2.animalworld.model.entities;

import cz.cvut.fel.zahorto2.animalworld.CoordInt;
import cz.cvut.fel.zahorto2.animalworld.model.World;

public class Wolf extends Animal {

    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }

    public Wolf(World world){
        super(world);

        setEatEnergy(200);
        setMaxEnergy(300);
        setStartEnergy(100);
        setReproduceChance(10);
    }

    public Wolf(Wolf other){
        super(other);
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
        Wolf child = new Wolf(this);
        child.setEnergy(getStartEnergy());
        child.setAge(0);
        return child;
    }
}
