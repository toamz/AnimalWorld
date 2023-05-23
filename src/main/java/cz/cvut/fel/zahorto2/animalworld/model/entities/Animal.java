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
    private int energy;
    protected final World world;

    protected Animal(World world) {
        this.world = world;
        energy = getStartEnergy();
    }
    protected Animal(Animal other) {
        this.world = other.world;
        this.age = other.age;
        this.energy = other.energy;
        this.maxEnergy = other.maxEnergy;
        this.startEnergy = other.startEnergy;
        this.eatEnergy = other.eatEnergy;
        this.reproduceChance = other.reproduceChance;
        this.maxAge = other.maxAge;
        this.reproduceAge = other.reproduceAge;
    }

    private int maxEnergy = 100;
    private int startEnergy = 20;
    private int eatEnergy = 15;
    private int reproduceChance = 10;
    private int maxAge = 10000;
    private int reproduceAge = 1000;

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
    public int getMaxEnergy() {
        return maxEnergy;
    }
    public int getStartEnergy() {
        return startEnergy;
    }
    public int getEatEnergy() {
        return eatEnergy;
    }
    public int getMaxAge() {
        return maxAge;
    }
    public int getReproduceChance() {
        return reproduceChance;
    }
    public int getReproduceAge() {
        return reproduceAge;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }
    public void setEatEnergy(int eatEnergy) {
        this.eatEnergy = eatEnergy;
    }
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
    public void setReproduceChance(int reproduceChance) {
        this.reproduceChance = reproduceChance;
    }
    public void setReproduceAge(int reproduceAge) {
        this.reproduceAge = reproduceAge;
    }
}
