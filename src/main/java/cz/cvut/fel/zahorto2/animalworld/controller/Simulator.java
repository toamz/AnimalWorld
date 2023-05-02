package cz.cvut.fel.zahorto2.animalworld.controller;

import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.view.WorldRenderer;
import javafx.fxml.FXML;

public class Simulator {
    private final World world = new World(20, 20);
    private final Thread simulationThread = new Thread(this::simulator);
    boolean running = true;
    SimulationSpeed speed = new SimulationSpeed(0);
    @FXML
    public WorldRenderer renderer;

    private void simulator() {
        while (running) {
            try {
                speed.delay();
            } catch (InterruptedException e) {
                System.err.println("Simulator interrupted");
                Thread.currentThread().interrupt();
                return;
            }
            world.getEntityMap().tick();
            world.getTileGrid().tick();
            System.out.println("Tick");
        }
    }

    @FXML
    public void initialize() {
        renderer.setWorld(world);
        simulationThread.setDaemon(true);
        simulationThread.start();
        System.err.println("Simulator created");
    }

    public void startButtonEvent() {
        System.out.println("Start button pressed");
        speed.setSpeed(Float.POSITIVE_INFINITY);
    }
    public void stopButtonEvent() {
        System.out.println("Stop button pressed");
        speed.setSpeed(0);
    }
    public void pauseButtonEvent() {
        System.out.println("Pause button pressed");
    }
    public void resumeButtonEvent() {
        System.out.println("Resume button pressed");
    }
    public void stepButtonEvent() {
        System.out.println("Step button pressed");
        speed.singleStep();
    }
    public void saveButtonEvent() {
        System.out.println("Save button pressed");
    }
    public void loadButtonEvent() {
        System.out.println("Load button pressed");
    }
}
