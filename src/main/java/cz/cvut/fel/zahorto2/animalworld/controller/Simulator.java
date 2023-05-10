package cz.cvut.fel.zahorto2.animalworld.controller;

import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.model.WorldLoader;
import cz.cvut.fel.zahorto2.animalworld.view.WorldRenderer;
import javafx.fxml.FXML;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;

public class Simulator {
    private World world = new World(20, 20);
    private final Object worldLock = new Object();
    private final Thread simulationThread = new Thread(this::simulator);
    boolean running = true;
    SimulationSpeed speed = new SimulationSpeed(0);
    @FXML
    public WorldRenderer renderer;
    private final FileChooser fileChooser = new FileChooser();
    private Window window;

    private void simulator() {
        while (running) {
            try {
                speed.delay();
            } catch (InterruptedException e) {
                System.err.println("Simulator interrupted");
                Thread.currentThread().interrupt();
                return;
            }

            synchronized (worldLock) {
                world.getEntityMap().tick();
                world.getTileGrid().tick();
            }
            System.out.println("Tick");
        }
    }

    @FXML
    public void initialize() {
        renderer.setWorld(world);
        simulationThread.setDaemon(true);
        simulationThread.start();
        System.err.println("Simulator created");

        String bin = WorldLoader.BINARY_FILE_EXTENSION;
        String text = WorldLoader.TEXT_FILE_EXTENSION;
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("World files (*.%s)".formatted(bin), "*.%s".formatted(bin)));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("World text files (*.%s)".formatted(text), "*.%s".formatted(text)));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));

        renderer.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                window = newValue.getWindow();

                newValue.setOnDragOver(event -> {
                    System.out.println("Drag over");
                    Dragboard db = event.getDragboard();
                    if (db.hasFiles()) {
                        event.acceptTransferModes(TransferMode.LINK);
                    }
                    event.consume();
                });

                newValue.setOnDragDropped(event -> {
                    System.out.println("Drag dropped");
                    Dragboard db = event.getDragboard();
                    if (db.hasFiles()) {
                        System.out.println("File dropped");
                        File file = db.getFiles().get(0);
                        System.out.println(file.getAbsolutePath());
                        loadWorld(file);
                    }
                    event.setDropCompleted(true);
                });
            }
        });
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

        fileChooser.setTitle("Save World File");
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
        File file = fileChooser.showSaveDialog(window);
        if (file == null) {
            return;
        }
        fileChooser.setInitialDirectory(file.getParentFile());

        WorldLoader.save(world, file);
    }
    public void loadButtonEvent() {
        System.out.println("Load button pressed");

        fileChooser.setTitle("Open World File");
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
        File file = fileChooser.showOpenDialog(window);
        if (file == null) {
            return;
        }
        fileChooser.setInitialDirectory(file.getParentFile());

        loadWorld(file);
    }

    void loadWorld(File file) {
        speed.setSpeed(0);
        World newWorld = WorldLoader.load(file);
        if (newWorld != null) {
            synchronized (worldLock) {
                world = newWorld;
                renderer.setWorld(world);
            }
        }}
}
