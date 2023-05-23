package cz.cvut.fel.zahorto2.animalworld.view.properties;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

@SuppressWarnings("java:S110")
public class IntField extends TextField {
    public SimpleIntegerProperty valueProperty = new SimpleIntegerProperty();
    IntField() {
        super();
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                setText(newValue.replaceAll("\\D", ""));
            }
            if (getText().length() > 0) {
                valueProperty.set(Integer.parseInt(getText()));
            } else {
                valueProperty.set(0);
            }
        });
        valueProperty.addListener((observable, oldValue, newValue) -> setText(newValue.toString()));
    }
}
