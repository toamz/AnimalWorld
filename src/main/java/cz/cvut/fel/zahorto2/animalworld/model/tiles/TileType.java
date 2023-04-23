package cz.cvut.fel.zahorto2.animalworld.model.tiles;

/**
 * Enum of all tile types.
 */
public enum TileType {
    GRASS;

    /**
     * Creates a new tile of type.
     * @return a new tile of type
     */
    public Tile createTile() {
        switch (this) {
            case GRASS:
                return new Grass();
            default:
                throw new IllegalArgumentException("Unknown tile type");
        }
    }
}
