package models;

public class Vector {
    private Double x;
    private Double y;

    public Vector(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Vector() {
    	this.x = 0.0;
    	this.y = 0.0;
	}

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Vector subtract(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector dot(Double c) {
        return new Vector(x * c, y * c);
    }

    public Double dot(Vector v) {
        return x * v.x + y * v.y;
    }

    public Double distance (Vector v) {
        return this.subtract(v).norm();
    }

    public Double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector normalize() {
        Double norm = norm();
        return this.dot(1.0 / norm);
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y + ")";
    }
}
