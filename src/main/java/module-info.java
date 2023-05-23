module animalworld {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.apache.logging.log4j;

    opens cz.cvut.fel.zahorto2.animalworld to javafx.fxml, javafx.base, javafx.graphics, javafx.controls;
    opens cz.cvut.fel.zahorto2.animalworld.controller to javafx.fxml, javafx.base, javafx.graphics, javafx.controls;
    opens cz.cvut.fel.zahorto2.animalworld.view to javafx.fxml, javafx.base, javafx.graphics, javafx.controls;
    opens cz.cvut.fel.zahorto2.animalworld.view.properties to javafx.fxml, javafx.base, javafx.graphics, javafx.controls;
    opens css;
}
