package cz.cvut.fel.zahorto2.animalworld.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/simulator.fxml"));
        Parent root = fxmlLoader.load();

        Simulator controller = fxmlLoader.getController();
        controller.setParameters(getParameters()); // pass command line arguments to the controller

        stage.setTitle("Animal World");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
}
