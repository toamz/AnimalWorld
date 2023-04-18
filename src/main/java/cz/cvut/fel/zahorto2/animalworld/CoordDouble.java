package cz.cvut.fel.zahorto2.animalworld;

public class CoordDouble {
    private double x;
    private double y;

    public CoordDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public CoordDouble copy() {
        return new CoordDouble(x, y);
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }

    public CoordDouble add(CoordDouble other) {
        x += other.x;
        y += other.y;
        return this;
    }
    public CoordDouble subtract(CoordDouble other) {
        x -= other.x;
        y -= other.y;
        return this;
    }
    public CoordDouble multiply(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public static CoordDouble add(CoordDouble a, CoordDouble b) {
        return new CoordDouble(a.x + b.x, a.y + b.y);
    }
    public static CoordDouble subtract(CoordDouble a, CoordDouble b) {
        return new CoordDouble(a.x - b.x, a.y - b.y);
    }
    public static CoordDouble multiply(CoordDouble a, double scalar) { return new CoordDouble(a.x * scalar, a.y * scalar); }

    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + "]";
    }
}
