package cz.cvut.fel.zahorto2.animalworld.controller;

import javafx.beans.property.SimpleFloatProperty;

/**
 * Class for controlling the speed of the simulation.
 * The speed is measured in ticks per second.
 */
public class SimulationSpeed {
    public final SimpleFloatProperty speedProperty = new SimpleFloatProperty();
    private boolean singleStep = false;
    SimulationSpeed(float speed)
    {
        this.speedProperty.set(speed);
        this.speedProperty.addListener((observable, oldValue, newValue) -> {
            synchronized (this) {
                notifyAll();
            }
        });
    }
    public synchronized void singleStep()
    {
        this.speedProperty.set(0);
        this.singleStep = true;
        notifyAll();
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
            float currentSpeed = this.speedProperty.get();
            if (currentSpeed == 0) {
                wait();
                continue;
            }
            long end = (long) (start + 1000.0 / currentSpeed);
            if (System.currentTimeMillis() >= end)
                return;
            wait(end - System.currentTimeMillis());
        }
    }
}
