package com.maxim.gaiduchek.seabattle.entities;

import com.maxim.gaiduchek.seabattle.controllers.App;
import javafx.scene.image.ImageView;

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
        return Math.abs(isHorizontally() ? begin.x() - end.x() : begin.y() - end.y()) + 1;
    }

    public ImageView getShipPart(int x, int y) {
        Coordinates cell = new Coordinates(x, y);

        if (getLength() == 1) {
            return App.getUnaryShipImageView();
        } else if (begin.equals(cell) || end.equals(cell)) {
            ImageView imageView = App.getCornerShipImageView();
            int rotate = isHorizontally() ?
                    (begin.x() < end.x() ? 0 : 180) - (end.equals(cell) ? 180 : 0) :
                    (begin.y() < end.y() ? 90 : -90) * (end.equals(cell) ? -1 : 1);

            imageView.setRotate(rotate);

            return imageView;
        } else {
            return App.getMiddleShipImageView();
        }
    }

    private boolean isHorizontally() {
        return begin.y() == end.y();
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
