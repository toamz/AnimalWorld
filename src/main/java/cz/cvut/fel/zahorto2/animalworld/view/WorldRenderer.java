package cz.cvut.fel.zahorto2.animalworld.view;

import cz.cvut.fel.zahorto2.animalworld.CoordDouble;
import cz.cvut.fel.zahorto2.animalworld.model.World;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public class WorldRenderer extends ResizableCanvas implements EventHandler<Event> {
    public WorldRenderer() {
        this.addEventHandler(ScrollEvent.SCROLL, this);
        this.addEventHandler(MouseEvent.MOUSE_MOVED, this);
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
    }
    CoordDouble offset = new CoordDouble(0, 0);
    double scale = 20;
    private World world;
    void draw() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(Color.BLACK);

        drawGrid(gc);
    }

    CoordDouble viewToMapCoord(CoordDouble canvasPos) {
        return CoordDouble.add(canvasPos.multiply(1 / scale), offset);
    }

    CoordDouble mapToViewCoord(CoordDouble mapPos) {
        return CoordDouble.subtract(mapPos, offset).multiply(scale);
    }

    void drawGrid(GraphicsContext gc) {

        for (double x = 0; x < world.getWidth() + 1; x += 1) {
            CoordDouble lineStart = mapToViewCoord(new CoordDouble(x, 0));
            CoordDouble lineEnd = mapToViewCoord(new CoordDouble(x, world.getHeight()));
            gc.strokeLine(lineStart.getX(), lineStart.getY(), lineEnd.getX(), lineEnd.getY());
        }
        for (double y = 0; y < world.getHeight() + 1; y += 1) {
            CoordDouble lineStart = mapToViewCoord(new CoordDouble(0, y));
            CoordDouble lineEnd = mapToViewCoord(new CoordDouble(world.getWidth(), y));
            gc.strokeLine(lineStart.getX(), lineStart.getY(), lineEnd.getX(), lineEnd.getY());
        }
    }

    void handleScrollEvent(ScrollEvent scrollEvent) {
        CoordDouble mousePos = viewToMapCoord(new CoordDouble(scrollEvent.getX(), scrollEvent.getY()));
        double zoomFactor = scrollEvent.getDeltaY() * 0.001 + 1;
        double oldScale = scale;
        scale *= zoomFactor;
        scale = Math.max(5, scale);
        scale = Math.min(500, scale);
        zoomFactor = scale / oldScale;

        offset.subtract(mousePos);
        offset.multiply(1/zoomFactor);
        offset.add(mousePos);
        draw();
        System.out.println("Zoomed " + zoomFactor);
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
            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED && !mouseEvent.isMiddleButtonDown()) {
                dragLast = null;
            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED && dragLast != null) {
                CoordDouble mousePos = new CoordDouble(mouseEvent.getX(), mouseEvent.getY());
                CoordDouble delta = CoordDouble.subtract(mousePos, dragLast);
                offset = CoordDouble.subtract(offset, CoordDouble.multiply(delta, 1 / scale));
                draw();
                dragLast = mousePos;
            }
            CoordDouble mousePos = new CoordDouble(mouseEvent.getX(), mouseEvent.getY());
            CoordDouble mapPos = viewToMapCoord(mousePos);
            System.out.printf("Moved   : %2.2f, %2.2f : %2.2f, %2.2f%n", mousePos.getX(), mousePos.getY(), mapPos.getX(), mapPos.getY());
        }
    }

    public void setWorld(World world) {
        this.world = world;
        draw();
    }
}
