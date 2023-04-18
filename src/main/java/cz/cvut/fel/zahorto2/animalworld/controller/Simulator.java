package cz.cvut.fel.zahorto2.animalworld.controller;

import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.view.WorldRenderer;
import javafx.fxml.FXML;

public class Simulator {
    private final World world = new World(20, 20);
    @FXML
    public WorldRenderer renderer;

    @FXML
    public void initialize() {
        renderer.setWorld(world);
        System.err.println("Simulator created");
    }

    public void startButtonEvent() {
        System.out.println("Start button pressed");
    }
    public void stopButtonEvent() {
        System.out.println("Stop button pressed");
    }
    public void pauseButtonEvent() {
        System.out.println("Pause button pressed");
    }
    public void resumeButtonEvent() {
        System.out.println("Resume button pressed");
    }
    public void stepButtonEvent() {
        System.out.println("Step button pressed");
    }
    public void saveButtonEvent() {
        System.out.println("Save button pressed");
    }
    public void loadButtonEvent() {
        System.out.println("Load button pressed");
    }
}
