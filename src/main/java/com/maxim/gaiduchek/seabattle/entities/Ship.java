package com.maxim.gaiduchek.seabattle.entities;

public class Ship {

    private final Coordinates begin, end;

    public Ship(Coordinates begin, Coordinates end) {
        this.begin = begin;
        this.end = end;
    }

    public Coordinates getBegin() {
        return begin;
    }

    public Coordinates getEnd() {
        return end;
    }

    public int getLength() {
        return Math.abs(begin.x() == end.x() ? begin.y() - end.y() : begin.x() - end.x()) + 1;
    }

    public boolean isOnShip(Coordinates c) {
        if (begin.x() == c.x()) {
            return begin.y() <= c.y() && c.y() <= end.y() || end.y() <= c.y() && c.y() <= begin.y();
        } else {
            return begin.x() <= c.x() && c.x() <= end.x() || end.x() <= c.x() && c.x() <= begin.x();
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
