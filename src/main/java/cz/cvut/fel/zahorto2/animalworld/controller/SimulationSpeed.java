package cz.cvut.fel.zahorto2.animalworld.controller;

/**
 * Class for controlling the speed of the simulation.
 * The speed is measured in ticks per second.
 */
public class SimulationSpeed {
    private float speed;
    private boolean singleStep = false;
    SimulationSpeed(float speed)
    {
        this.speed = speed;
    }
    public synchronized void setSpeed(float speed)
    {
        this.speed = speed;
        notifyAll();
    }
    public synchronized void singleStep()
    {
        this.speed = 0;
        this.singleStep = true;
        notifyAll();
    }
    public synchronized float getSpeed()
    {
        return speed;
    }

    public synchronized void delay() throws InterruptedException
    {
        long start = System.currentTimeMillis();
        while (true)
        {
            if (singleStep)
            {
                singleStep = false;
                return;
            }
            if (speed == 0) {
                wait();
                continue;
            }
            long end = start + 1000 / (long) speed;
            if (System.currentTimeMillis() >= end)
                return;
            wait(end - System.currentTimeMillis());
        }
    }
}
