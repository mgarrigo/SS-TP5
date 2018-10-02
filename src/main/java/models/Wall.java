package models;

public class Wall {
    private Vector start;
    private Vector end;

    public Double getLength() {
        return start.distance(end);
    }

    public Double getAngle() {
        return Math.acos((end.getX() - start.getX()) / getLength());
    }

    public Vector getNormalVersor() {
        Vector tangent = getTangentVersor();
        return new Vector(-tangent.getY(), tangent.getX());
    }

    public Vector getTangentVersor() {
        return end.subtract(start).normalize();
    }
}
