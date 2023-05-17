package cz.cvut.fel.zahorto2.animalworld.view;

import javafx.scene.canvas.Canvas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Canvas that resizes itself to fill the whole window.
 */
public abstract class ResizableCanvas extends Canvas {
    private static final Logger logger = LogManager.getFormatterLogger(ResizableCanvas.class.getName());
    /**
     * Redraw the canvas when the size changes.
     */
    abstract void draw();

    @Override
    public boolean isResizable() {
        return true;
    }
    @Override
    public double maxHeight(double width) {
        return Double.POSITIVE_INFINITY;
    }
    @Override
    public double maxWidth(double height) {
        return Double.POSITIVE_INFINITY;
    }
    @Override
    public double minWidth(double height) {
        return 0;
    }
    @Override
    public double minHeight(double width) {
        return 0;
    }
    @Override
    public void resize(double width, double height) {
        logger.debug("Resizing to %.0f x %.0f", width, height);
        this.setWidth(width);
        this.setHeight(height);
        this.draw();
    }
}
