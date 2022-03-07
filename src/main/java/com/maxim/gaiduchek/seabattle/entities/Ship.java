package com.maxim.gaiduchek.seabattle.entities;

public class Ship {

    private final Coordinates begin, end;

    public Ship(Coordinates begin, Coordinates end) {
        this.begin = begin;
        this.end = end;
    }

    public int getLength() {
        return Math.abs(begin.getX() == end.getX() ? begin.getY() - end.getY() : begin.getX() - end.getX()) + 1;
    }

    public boolean isOnShip(Coordinates c) {
        if (begin.getX() == c.getX()) {
            return begin.getY() <= c.getY() && c.getY() <= end.getY() || end.getY() <= c.getY() && c.getY() <= begin.getY();
        } else {
            return begin.getX() <= c.getX() && c.getX() <= end.getX() || end.getX() <= c.getX() && c.getX() <= begin.getX();
        }
    }

    // core

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        if (!begin.equals(ship.begin)) return false;
        return end.equals(ship.end);
    }

    @Override
    public int hashCode() {
        int result = begin.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }
}
