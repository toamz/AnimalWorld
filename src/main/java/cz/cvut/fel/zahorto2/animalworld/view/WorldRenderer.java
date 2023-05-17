package cz.cvut.fel.zahorto2.animalworld.view;

import cz.cvut.fel.zahorto2.animalworld.CoordDouble;
import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;
import cz.cvut.fel.zahorto2.animalworld.view.entities.EntityRenderer;
import cz.cvut.fel.zahorto2.animalworld.view.tiles.TileRenderer;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import javafx.scene.transform.NonInvertibleTransformException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldRenderer extends ResizableCanvas implements EventHandler<Event> {
    private static final Logger logger = LogManager.getFormatterLogger(WorldRenderer.class.getName());
    AnimationTimer repaintTimer;

    public WorldRenderer() {
        this.addEventHandler(ScrollEvent.SCROLL, this);
        this.addEventHandler(MouseEvent.MOUSE_MOVED, this);
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
        transform.appendScale(10, 10);

        // Timer that repaints the canvas every frame
        repaintTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
                start();
            }
        };
        repaintTimer.start();
    }
    Affine transform = new Affine();
    private World world;
    void draw() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        gc.save();
        gc.transform(transform);
        drawTiles(gc);
        drawGrid(gc);
        drawEntities(gc);
        gc.restore();
    }

    CoordDouble viewToMapCoord(CoordDouble canvasPos) {
        try {
            Point2D result = transform.inverseTransform(canvasPos.x, canvasPos.y);
            return new CoordDouble(result.getX(), result.getY());
        } catch (NonInvertibleTransformException e) {
            logger.error("Could not invert transform", e);
            return null;
        }
    }

    void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.02);
        for (double x = 0; x < world.getWidth() + 1; x += 1) {
            gc.strokeLine(x, 0, x, world.getHeight());
        }
        for (double y = 0; y < world.getHeight() + 1; y += 1) {
            gc.strokeLine(0, y, world.getWidth(), y);
        }
    }

    void drawTiles(GraphicsContext gc) {
        gc.save();
        for (int x = 0; x < world.getWidth(); x++) {
            gc.save();
            for (int y = 0; y < world.getHeight(); y++) {
                TileRenderer.render(world.getTileGrid().getTile(x, y), gc);
                gc.translate(0, 1);
            }
            gc.restore();
            gc.translate(1, 0);
        }
        gc.restore();
    }

    void drawEntities(GraphicsContext gc) {
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Entity entity = world.getEntityMap().getEntity(x, y);
                if (entity == null) continue;

                gc.save();
                gc.translate(x, y);
                EntityRenderer.render(entity, gc);
                gc.restore();
            }
        }
    }

    void handleScrollEvent(ScrollEvent scrollEvent) {
        CoordDouble mousePos = viewToMapCoord(new CoordDouble(scrollEvent.getX(), scrollEvent.getY()));
        double zoomFactor = scrollEvent.getDeltaY() * 0.001 + 1;
        transform.appendTranslation(mousePos.x, mousePos.y);
        transform.appendScale(zoomFactor, zoomFactor);
        transform.appendTranslation(-mousePos.x, -mousePos.y);
        logger.info("Zooming by %s to %s", zoomFactor, mousePos);
    }

    CoordDouble dragLast = null;
    @Override
    public void handle(Event event) {
        if (event instanceof ScrollEvent scrollEvent) {
            handleScrollEvent(scrollEvent);
        }
        if (event instanceof MouseEvent mouseEvent) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED && mouseEvent.isMiddleButtonDown()) {
                dragLast = new CoordDouble(mouseEvent.getX(), mouseEvent.getY());
                logger.info("Dragging from %s", dragLast);
            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED && !mouseEvent.isMiddleButtonDown()) {
                logger.info("Stopped dragging to %s", dragLast);
                dragLast = null;

            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED && dragLast != null) {
                CoordDouble mousePos = new CoordDouble(mouseEvent.getX(), mouseEvent.getY());
                CoordDouble delta = CoordDouble.subtract(mousePos, dragLast);
                dragLast = mousePos;
                transform.prependTranslation(delta.x, delta.y);
                logger.debug("Dragging by %s", delta);
            }
        }
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
