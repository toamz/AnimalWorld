package cz.cvut.fel.zahorto2.animalworld.model.entities;

import cz.cvut.fel.zahorto2.animalworld.CoordInt;
import cz.cvut.fel.zahorto2.animalworld.model.World;

public class Sheep extends Animal {
    public Sheep(World world) {
        super(world);
    }

    @Override
    protected boolean eat() {
        CoordInt position = this.getPosition();
        return world.getTileGrid().getTile(position.x, position.y).beEaten();
    }

    @Override
    protected Animal createBaby(Animal other) {
        return new Sheep(world);
    }
}
