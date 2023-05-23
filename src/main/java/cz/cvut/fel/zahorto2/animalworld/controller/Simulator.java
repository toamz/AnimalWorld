package cz.cvut.fel.zahorto2.animalworld.controller;

import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.model.WorldLoader;
import cz.cvut.fel.zahorto2.animalworld.view.StatisticsLabel;
import cz.cvut.fel.zahorto2.animalworld.view.WorldRenderer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;

import java.io.*;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Simulator {
    private static final Logger logger = LogManager.getFormatterLogger(Simulator.class.getName());
    @FXML
    public StatisticsLabel statisticsLabel;
    private World world = new World(20, 20);
    private final Thread simulationThread = new Thread(this::simulator);
    boolean running = true;
    SimulationSpeed speed = new SimulationSpeed(0);
    @FXML
    public WorldRenderer renderer;
    @FXML
    public Slider speedSlider;
    private final FileChooser fileChooser = new FileChooser();
    private Window window;

    private void simulator() {
        while (running) {
            try {
                speed.delay();
            } catch (InterruptedException e) {
                logger.info("Simulation thread interrupted");
                Thread.currentThread().interrupt();
                return;
            }

            world.tick();
        }
    }

    @FXML
    public void initialize() {
        renderer.setWorld(world);
        statisticsLabel.setWorld(world);
        simulationThread.setDaemon(true);
        simulationThread.start();
        logger.info("Simulator created");

        String bin = WorldLoader.BINARY_FILE_EXTENSION;
        String text = WorldLoader.TEXT_FILE_EXTENSION;
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("World files (*.%s)".formatted(bin), "*.%s".formatted(bin)));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("World text files (*.%s)".formatted(text), "*.%s".formatted(text)));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));

        renderer.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                linkSceneAfterLoad(newValue);
            }
        });

        setupSpeedSlider();
    }

    private void setupSpeedSlider() {
        speedSlider.setLabelFormatter(new DoubleStringConverter(){
            @Override
            public String toString(Double object) {
                if (object == 0) {
                    return "Paused";
                }
                if (object == speedSlider.getMax()) {
                    return "Max";
                }
                return super.toString(object);
            }
        });
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            float val = newValue.floatValue();
            if (val == speedSlider.getMax()) {
                speed.speedProperty.set(Float.POSITIVE_INFINITY);
            } else {
                speed.speedProperty.set(val);
            }
        });
        speed.speedProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue.floatValue() == Float.POSITIVE_INFINITY) {
                speedSlider.setValue(speedSlider.getMax());
            } else {
                speedSlider.setValue(newValue.floatValue());
            }
        });
    }

    void linkSceneAfterLoad(Scene scene){
        window = scene.getWindow();

        scene.setOnDragOver(event -> {
            logger.debug("Drag over");
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK);
            }
            event.consume();
        });

        scene.setOnDragDropped(event -> {
            logger.info("Drag dropped");
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                logger.info("File dropped");
                File file = db.getFiles().get(0);
                loadWorld(file);
            }
            event.setDropCompleted(true);
        });
    }

    public void startButtonEvent() {
        logger.info("Start button pressed");
        speed.speedProperty.set(Float.POSITIVE_INFINITY);
    }
    public void stopButtonEvent() {
        logger.info("Stop button pressed");
        speed.speedProperty.set(0);
    }
    public void stepButtonEvent() {
        logger.info("Step button pressed");
        speed.singleStep();
    }
    public void saveButtonEvent() {
        logger.info("Save button pressed");

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
        logger.info("Load button pressed");

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
        logger.info("Loading world %s", file.getAbsolutePath());

        speed.speedProperty.set(0);
        World newWorld = WorldLoader.load(file);
        if (newWorld != null) {
            world = newWorld;
            renderer.setWorld(world);
            statisticsLabel.setWorld(world);
        }
    }

    public void openPropertyEditor() {
        logger.info("Opening property editor");

        speed.speedProperty.set(0);

        // load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/propertyEditor.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            logger.error("Failed to load property editor", e);
            return;
        }

        // Set the controller and show the dialog.
        PropertyEditor controller = loader.getController();
        controller.setWorld(world);

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(loader.getRoot()));
        dialogStage.setTitle("Property Editor");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(window);
        dialogStage.showAndWait();
    }

    public void setParameters(Application.Parameters parameters) {
        logger.info("Setting parameters");

        if (!parameters.getUnnamed().isEmpty()) {
            loadWorld(new File(parameters.getUnnamed().get(0)));
        }

        for (Map.Entry<String, String> parameter : parameters.getNamed().entrySet()) {
            switch (parameter.getKey()) {
                case "speed" -> {
                    try {
                        logger.info("Setting speed to %s", parameter.getValue());
                        speed.speedProperty.set(Float.parseFloat(parameter.getValue()));
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid speed parameter %s", parameter.getValue());
                    }
                }
                default -> logger.warn("Unknown parameter %s=%s", parameter.getKey(), parameter.getValue());
            }
        }
    }
}
