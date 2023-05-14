package cz.cvut.fel.zahorto2.animalworld.model.entities;

import cz.cvut.fel.zahorto2.animalworld.CoordInt;
import cz.cvut.fel.zahorto2.animalworld.model.World;

import java.util.Random;

/**
 * Abstract class for all animals.
 * Each tick the animal ages and loses energy.
 * If the animal has no energy or is too old, it dies.
 * It tries to eat grass, then reproduce, or move.
 */
public abstract class Animal implements Entity {
    Random random = new Random();
    private int age = 0;
    private int energy = getStartEnergy();
    protected final World world;

    protected Animal(World world) {
        this.world = world;
    }

    @Override
    public void tick() {
        age++;
        energy--;

        if (energy <= 0 || age >= getMaxAge()) {
            this.die();
            return;
        }
        if (energy < getMaxEnergy() - getEatEnergy() && this.eat()) {
            energy += getEatEnergy();
            return;
        }
        if (energy > getStartEnergy() + 10 && this.reproduce()) {
            energy -= getStartEnergy();
            return;
        }
        this.move();
    }

    @Override
    public void die() {
        CoordInt position = this.getPosition();
        world.getEntityMap().setEntity(null, position.x, position.y);
    }

    /**
     * Get the position of the animal in the world.
     * @return the position
     * @throws IllegalStateException if the animal is not in the world
     */
    protected CoordInt getPosition() {
        CoordInt position = world.getEntityMap().getEntityPosition(this);
        if (position == null) {
            throw new IllegalStateException("Animal is not in the world");
        }
        return position;
    }

    /**
     * Try to eat grass at the current position.
     * @return true if the sheep ate grass, false otherwise
     */
    protected abstract boolean eat();

    /**
     * Try to move to a random empty neighbour position.
     */
    private void move() {
        CoordInt oldPosition = this.getPosition();
        CoordInt newPosition = world.getEntityMap().getRandomEmptyNeighbour(oldPosition);
        if (newPosition != null && world.getTileGrid().getTile(newPosition.x, newPosition.y).isWalkable()) {
            world.getEntityMap().setEntity(this, newPosition.x, newPosition.y);
        }
    }

    /**
     * Try to create a baby.
     * @param other the other animal
     * @return true if the baby was created, false otherwise
     */
    protected abstract Animal createBaby(Animal other);

    /**
     * Try to reproduce with another animal at a random neighbour position.
     * @return true if the baby was created, false otherwise
     */
    private boolean reproduce() {
        if (age < getReproduceAge() || random.nextInt(getReproduceChance()) != 0) {
            return false;
        }
        CoordInt position = this.getPosition();
        CoordInt otherPosition = world.getEntityMap().getRandomNeighbour(position);
        if (otherPosition != null && world.getTileGrid().getTile(otherPosition.x, otherPosition.y).isWalkable()) {
            Entity other = world.getEntityMap().getEntity(otherPosition.x, otherPosition.y);
            if (other == null) {
                return false;
            }
            if (other.getClass() == this.getClass()) {
                CoordInt babyPosition = world.getEntityMap().getRandomEmptyNeighbour(position);
                if (babyPosition != null) {
                    Animal baby = this.createBaby((Animal) other);
                    world.getEntityMap().setEntity(baby, babyPosition.x, babyPosition.y);
                    return true;
                }
            }
        }
        return false;
    }

    public int getAge() {
        return age;
    }

    public int getEnergy() {
        return energy;
    }

    protected int getMaxEnergy() {
        return 100;
    }
    protected int getStartEnergy() {
        return 20;
    }
    protected int getEatEnergy() {
        return 15;
    }
    protected int getReproduceChance() {
        return 10;
    }
    protected int getMaxAge() {
        return 10000;
    }
    protected int getReproduceAge() {
        return 1000;
    }
}
