package cz.cvut.fel.zahorto2.animalworld.view;

import cz.cvut.fel.zahorto2.animalworld.model.World;
import cz.cvut.fel.zahorto2.animalworld.model.entities.EntityType;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.Grass;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

/**
 * Label that contains statistics about the world
 */
@SuppressWarnings("java:S110")
public class StatisticsLabel extends Label implements World.TickListener {
    private World world;
    private boolean needsUpdate = false;
    AnimationTimer updateTimer;
    public StatisticsLabel() {
        super();
        this.setText("No world loaded");

        // Timer that updates the statistics label
        updateTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!needsUpdate) {
                    return;
                }
                needsUpdate = false;
                updateStatistics();
                start();
            }
        };
        updateTimer.start();
    }

    /**
     * Get the number of entities of a given type
     * @param entityType Type of the entity to count
     */
    private int getEntityCount(EntityType entityType) {
        int count = 0;
        for (var entity : world.getEntityMap().getEntities()) {
            if (entity.getType() == entityType)
                count++;
        }
        return count;
    }

    private int getEatableGrassCount() {
        int count = 0;
        for (int x = 0; x < world.getTileGrid().getWidth(); x++) {
            for (int y = 0; y < world.getTileGrid().getHeight(); y++) {
                if (world.getTileGrid().getTile(x, y) instanceof Grass grass && grass.isEatable())
                        {count++;
                }
            }
        }
        return count;
    }

    /**
     * Update the statistics label
     */
    public void updateStatistics() {
        StringBuilder statistics = new StringBuilder();
        statistics.append("Ticks: ").append(world.getTickCount()).append("\n");
        statistics.append("Entities: ").append(world.getEntityMap().getEntities().length).append("\n");
        statistics.append("Sheep: ").append(getEntityCount(EntityType.SHEEP)).append("\n");
        statistics.append("Wolf: ").append(getEntityCount(EntityType.WOLF)).append("\n");
        statistics.append("\n");
        statistics.append("Eatable grass: ").append(getEatableGrassCount()).append("\n");

        setText(statistics.toString());
    }

    /**
     * Link the statistics with a world
     */
    public void setWorld(World world) {
        if (this.world != null)
            world.removeTickListener(this);
        this.world = world;
        world.addTickListener(this);
        updateStatistics();
    }

    /**
     * Schedule an update of the statistics when the world ticks
     */
    @Override
    public void onWorldTick(World world) {
        needsUpdate = true;
    }
}
