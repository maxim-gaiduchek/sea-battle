package com.maxim.gaiduchek.seabattle.entities;

public class Cell {

    private Ship ship;
    private boolean shotted;

    public Cell() {
        shotted = false;
    }

    public Cell(boolean shotted) {
        this.shotted = shotted;
    }

    // getters

    public Ship getShip() {
        return ship;
    }

    public boolean hasShip() {
        return ship != null;
    }

    public boolean isShotted() {
        return shotted;
    }

    // setters

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setShotted() {
        shotted = true;
    }

    // core

    @Override
    public String toString() {
        return "Cell{" +
                "ship=" + ship +
                ", shotted=" + shotted +
                '}';
    }
}
