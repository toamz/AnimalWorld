package cz.cvut.fel.zahorto2.animalworld.view;

import cz.cvut.fel.zahorto2.animalworld.CoordDouble;
import cz.cvut.fel.zahorto2.animalworld.CoordInt;
import cz.cvut.fel.zahorto2.animalworld.model.entities.Entity;
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Renderer for the world and entities with selection of entities.
 */
public class WorldEntitySelectionRenderer extends WorldRenderer{
    private final Logger logger = LogManager.getFormatterLogger(WorldEntitySelectionRenderer.class.getName());
    Entity selectedEntity;

    /**
     * Listener for entity selection.
     */
    public interface EntitySelectionListener {
        void onEntitySelected(Entity entity);
    }
    List<EntitySelectionListener> entitySelectionListeners = new java.util.ArrayList<>();
    public void addEntitySelectionListener(EntitySelectionListener listener) {
        entitySelectionListeners.add(listener);
    }
    public void removeEntitySelectionListener(EntitySelectionListener listener) {
        entitySelectionListeners.remove(listener);
    }
    private void notifyEntitySelectionListeners(Entity entity) {
        for (EntitySelectionListener listener : entitySelectionListeners) {
            listener.onEntitySelected(entity);
        }
    }

    public WorldEntitySelectionRenderer() {
        super();
    }

    @Override
    void draw() {
        super.draw();
        if (selectedEntity != null) {
            GraphicsContext gc = this.getGraphicsContext2D();
            CoordInt entityPosition = world.getEntityMap().getEntityPosition(selectedEntity);
            gc.save();
            gc.transform(transform);
            gc.translate(entityPosition.x, entityPosition.y);
            gc.setStroke(Color.RED);
            gc.setLineWidth(0.1);
            gc.strokeRect(0, 0, 1, 1);
            gc.restore();
        }
    }

    @Override
    public void handle(Event event) {
        if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            //Select hovered entity
            MouseEvent mouseEvent = (MouseEvent) event;
            if (!mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                return;
            }
            CoordDouble mouseCoord = new CoordDouble(mouseEvent.getX(), mouseEvent.getY());
            CoordDouble tileCoord = viewToMapCoord(mouseCoord);
            selectedEntity = world.getEntityMap().getEntity((int)tileCoord.x, (int)tileCoord.y);
            logger.info("Selected entity: %s", selectedEntity);
            notifyEntitySelectionListeners(selectedEntity);
            needsRepaint = true;
            return;
        }

        super.handle(event);
    }
}
