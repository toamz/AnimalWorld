package cz.cvut.fel.zahorto2.animalworld.model.tiles;

/**
 * Enum of all tile types.
 */
public enum TileType {
    WATER,
    GRASS;

    /**
     * Creates a new tile of type.
     * @return a new tile of type
     */
    public Tile createTile() {
        switch (this) {
            case WATER:
                return new Water();
            case GRASS:
                return new Grass();
            default:
                throw new IllegalArgumentException("Unknown tile type");
        }
    }
}
