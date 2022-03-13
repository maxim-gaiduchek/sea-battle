package com.maxim.gaiduchek.seabattle.entities;

public class Coordinates {

    private final int x, y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // getters

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    // core

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        return x * 100 + y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                " (" + (x + 1) + "-" + ((char) ('a' + y)) + ")" +
                '}';
    }
}
