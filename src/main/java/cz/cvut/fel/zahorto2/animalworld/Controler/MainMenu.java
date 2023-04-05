package cz.cvut.fel.zahorto2.animalworld.Controler;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class MainMenu {
    public void loadButtonEvent(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", ".jpeg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println(file.getAbsolutePath());
        }
        // Open simulator
    }

    public void newButtonEvent(ActionEvent actionEvent) {
        // Open editor
    }

    public void quitButtonEvent(ActionEvent actionEvent) {
        Platform.exit();
    }
}
