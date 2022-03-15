package com.maxim.gaiduchek.seabattle.entities;

import com.maxim.gaiduchek.seabattle.controllers.App;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.Random;

public class Grid {

    public static final int MAX_X = 10, MAX_Y = 10;

    private final Ship[][] grid = new Ship[MAX_Y + 1][MAX_X + 1];
    private final boolean[][] shots = new boolean[MAX_Y + 1][MAX_X + 1];

    public Grid() {
        for (Ship[] row : grid) Arrays.fill(row, null);
        for (boolean[] row : shots) Arrays.fill(row, false);

        Random random = new Random();

        for (int length = 5; length >= 1; length--) {
            for (int i = 5 - length + 1; i > 0; i--) {
                Coordinates begin, end;

                while (true) {
                    if (random.nextBoolean()) {
                        int x = random.nextInt(MAX_X - length + 2), y = random.nextInt(MAX_Y + 1);

                        begin = new Coordinates(x, y);
                        end = new Coordinates(x + length - 1, y);
                    } else {
                        int x = random.nextInt(MAX_X + 1), y = random.nextInt(MAX_Y - length + 2);

                        begin = new Coordinates(x, y);
                        end = new Coordinates(x, y + length - 1);
                    }

                    boolean toBreak = true;

                    for (int x = begin.x(); x <= end.x() && toBreak; x++) {
                        for (int y = begin.y(); y <= end.y() && toBreak; y++) {
                            if (hasShipNearby(x, y)) {
                                toBreak = false;
                            }
                        }
                    }

                    if (toBreak) break;
                }

                Ship ship = new Ship(begin, end);

                for (int x = begin.x(); x <= end.x(); x++) {
                    for (int y = begin.y(); y <= end.y(); y++) {
                        setShip(ship, x, y);
                    }
                }
            }
        }

        System.out.println("Randomly generated grid:");
        outputGrid();
    }

    // getters

    private Ship getShip(Coordinates coordinates) {
        return getShip(coordinates.x(), coordinates.y());
    }

    public Ship getShip(int x, int y) {
        return grid[y][x];
    }

    public boolean isNotShotted(int x, int y) {
        return !shots[y][x];
    }

    public boolean isDestroyed(int x, int y) {
        return isDestroyed(getShip(x, y));
    }

    private boolean isDestroyed(Ship ship) {
        Coordinates begin = ship.getBegin(), end = ship.getEnd();

        for (int x = begin.x(); x <= end.x(); x++) {
            for (int y = begin.y(); y <= end.y(); y++) {
                if (isNotShotted(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean hasShip(Coordinates coordinates) {
        return hasShip(coordinates.x(), coordinates.y());
    }

    public boolean hasShip(int x, int y) {
        return getShip(x, y) != null;
    }

    private void setShip(Ship ship, int x, int y) {
        grid[y][x] = ship;
    }

    private void setShotted(int x, int y) {
        shots[y][x] = true;
    }

    public boolean hasShipNearby(Coordinates coordinates) {
        return hasShipNearby(coordinates.x(), coordinates.y());
    }

    public boolean hasShipNearby(int cx, int cy) {
        for (int x = Math.max(cx - 1, 0); x <= Math.min(cx + 1, MAX_X); x++) {
            for (int y = Math.max(cy - 1, 0); y <= Math.min(cy + 1, MAX_Y); y++) {
                if (hasShip(x, y)) {
                    return true;
                }
            }
        }

        return false;
    }

    // other

    public boolean shot(GridPane gridPane, int x, int y) {
        if (isNotShotted(x, y)) {
            setShotted(x, y);

            if (hasShip(x, y)) {
                Ship ship = getShip(x, y);

                if (isDestroyed(ship)) {
                    Coordinates begin = ship.getBegin(), end = ship.getEnd();

                    for (int cx = Math.max(begin.x() - 1, 0); cx <= Math.min(end.x() + 1, Grid.MAX_X); cx++) {
                        for (int cy = Math.max(begin.y() - 1, 0); cy <= Math.min(end.y() + 1, Grid.MAX_Y); cy++) {
                            if (hasShip(cx, cy)) {
                                gridPane.add(ship.getShipPart(cx, cy), cx, cy);
                                gridPane.add(App.getHitShotImageView(), cx, cy);
                            } else if (isNotShotted(cx, cy)) {
                                gridPane.add(App.getMissedShotImageView(), cx, cy);
                            }
                            setShotted(cx, cy);
                        }
                    }
                }

                gridPane.add(App.getHitShotImageView(), x, y);

                return true;
            } else {
                gridPane.add(App.getMissedShotImageView(), x, y);

                return false;
            }
        }

        return false;
    }

    public boolean isDefeated() {
        for (int x = 0; x <= MAX_X; x++) {
            for (int y = 0; y <= MAX_Y; y++) {
                if (hasShip(x, y) && isNotShotted(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void outputGrid() {
        for (Ship[] row : grid) {
            for (Ship ship : row) {
                System.out.print((ship == null ? "." : "0") + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public enum Shot {
        NONE, MISSED, HIT
    }
}
