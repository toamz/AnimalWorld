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

/**
 * Renderer for the world and entities.
 * Handles middle mouse button panning and scrolling with support for smooth scroll (usually on touchpads).
 */
public class WorldRenderer extends ResizableCanvas implements EventHandler<Event>, World.TickListener {
    private static final Logger logger = LogManager.getFormatterLogger(WorldRenderer.class.getName());
    AnimationTimer repaintTimer;
    boolean needsRepaint = true;

    public WorldRenderer() {
        this.addEventHandler(ScrollEvent.SCROLL, this);
        this.addEventHandler(MouseEvent.MOUSE_MOVED, this);
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
        transform.appendScale(10, 10); // default zoom - 10 pixels per tile

        // Timer that repaints the canvas every frame
        repaintTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!needsRepaint) {
                    return;
                }
                needsRepaint = false;
                draw();
            }
        };

        // Make sure it stops when the window is closed, otherwise it would keep running in the background and cause a memory leak
        this.sceneProperty().addListener((observable, oldScene, newScene) ->
                newScene.windowProperty().addListener((observable1, oldWindow, newWindow) -> {
                            newWindow.setOnCloseRequest(event -> repaintTimer.stop());
                            repaintTimer.start();
                        }
                )
        );
    }

    Affine transform = new Affine(); // View transform
    protected World world;

    void draw() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());

        if (world == null) {
            return;
        }

        gc.save();
        gc.transform(transform);


        gc.save();
        CoordDouble topLeft = viewToMapCoord(new CoordDouble(0, 0));
        CoordDouble bottomRight = viewToMapCoord(new CoordDouble(getWidth(), getHeight()));

        int minX = (int) Math.min(Math.max(topLeft.x, 0), world.getWidth());
        int minY = (int) Math.min(Math.max(topLeft.y, 0), world.getHeight());

        int maxX = (int) Math.min(Math.max(bottomRight.x + 1, 0), world.getWidth());
        int maxY = (int) Math.min(Math.max(bottomRight.y + 1, 0), world.getHeight());

        logger.debug("Drawing from %d,%d to %d,%d", minX, minY, maxX, maxY);


        drawTiles(gc, minX, minY, maxX, maxY);
        drawGrid(gc);
        drawEntities(gc, minX, minY, maxX, maxY);
        gc.restore();
    }

    /**
     * Converts a canvas position to a map coordinate using the current view transform.
     */
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

    void drawTiles(GraphicsContext gc, int minX, int minY, int maxX, int maxY) {
        int currentX = 0;
        int currentY = 0;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                gc.translate((double) x - currentX, (double) y - currentY);
                currentX = x;
                currentY = y;
                TileRenderer.render(world.getTileGrid().getTile(x, y), gc);
            }
        }
        gc.restore();
    }

    void drawEntities(GraphicsContext gc, int minX, int minY, int maxX, int maxY) {
        gc.save();

        int currentX = 0;
        int currentY = 0;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                Entity entity = world.getEntityMap().getEntity(x, y);
                if (entity == null) continue;

                gc.translate((double) x - currentX, (double) y - currentY);
                currentX = x;
                currentY = y;
                EntityRenderer.render(entity, gc);
            }
        }
        gc.restore();
    }

    void handleScrollEvent(ScrollEvent scrollEvent) {
        CoordDouble mousePos = viewToMapCoord(new CoordDouble(scrollEvent.getX(), scrollEvent.getY()));
        double zoomFactor = scrollEvent.getDeltaY() * 0.001 + 1;
        transform.appendTranslation(mousePos.x, mousePos.y);
        transform.appendScale(zoomFactor, zoomFactor);
        transform.appendTranslation(-mousePos.x, -mousePos.y);
        needsRepaint = true;
        logger.info("Zooming by %s to %s", zoomFactor, mousePos);
    }

    CoordDouble dragLast = null;

    /**
     * Handles events from the canvas. Currently only handles scrolling and middle mouse button panning.
     * @param event the event which occurred
     */
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
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED && dragLast != null &&!mouseEvent.isMiddleButtonDown()) {
                logger.info("Stopped dragging to %s", dragLast);
                dragLast = null;

            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED && dragLast != null) {
                CoordDouble mousePos = new CoordDouble(mouseEvent.getX(), mouseEvent.getY());
                CoordDouble delta = CoordDouble.subtract(mousePos, dragLast);
                dragLast = mousePos;
                transform.prependTranslation(delta.x, delta.y);
                needsRepaint = true;
                logger.debug("Dragging by %s", delta);
            }
        }
    }

    /**
     * Link the renderer with a world
     */
    public void setWorld(World world) {
        if (this.world != null) {
            this.world.removeTickListener(this);
        }
        this.world = world;
        world.addTickListener(this);
    }

    /**
     * Updates the view when the world ticks
     */
    @Override
    public void onWorldTick(World world) {
        needsRepaint = true;
    }
}
