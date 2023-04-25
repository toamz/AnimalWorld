package cz.cvut.fel.zahorto2.animalworld;

public class CoordInt {
    public int x;
    public int y;

    public CoordInt(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public CoordInt copy() {
        return new CoordInt(x, y);
    }

    public CoordInt add(CoordInt other) {
        x += other.x;
        y += other.y;
        return this;
    }
    public CoordInt subtract(CoordInt other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public static CoordInt add(CoordInt a, CoordInt b) {
        return new CoordInt(a.x + b.x, a.y + b.y);
    }
    public static CoordInt subtract(CoordInt a, CoordInt b) {
        return new CoordInt(a.x - b.x, a.y - b.y);
    }

    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + "]";
    }
}
