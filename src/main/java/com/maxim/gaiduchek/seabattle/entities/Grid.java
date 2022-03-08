package com.maxim.gaiduchek.seabattle.entities;

import java.util.Arrays;
import java.util.Random;

public class Grid {

    public static final int MAX_X = 10, MAX_Y = 10;

    private final Ship[][] grid = new Ship[MAX_Y + 1][MAX_X + 1];
    private final Shot[][] shots = new Shot[MAX_Y + 1][MAX_X + 1];

    public Grid() {
        for (Ship[] row : grid) Arrays.fill(row, null);
        for (Shot[] row : shots) Arrays.fill(row, Shot.NONE);

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

                    for (int x = begin.getX(); x <= end.getX() && toBreak; x++) {
                        for (int y = begin.getY(); y <= end.getY() && toBreak; y++) {
                            if (hasShipNearby(x, y)) {
                                toBreak = false;
                            }
                        }
                    }

                    if (toBreak) break;
                }

                Ship ship = new Ship(begin, end);

                for (int x = begin.getX(); x <= end.getX(); x++) {
                    for (int y = begin.getY(); y <= end.getY(); y++) {
                        setShip(ship, x, y);
                    }
                }
            }
        }

        System.out.println("Randomly generated grid:");
        outputGrid();
    }

    private Ship getShip(Coordinates coordinates) {
        return getShip(coordinates.getX(), coordinates.getY());
    }

    private Ship getShip(int x, int y) {
        return grid[y][x];
    }

    public boolean hasShip(Coordinates coordinates) {
        return hasShip(coordinates.getX(), coordinates.getY());
    }

    public boolean hasShip(int x, int y) {
        return getShip(x, y) != null;
    }

    private void setShip(Ship ship, int x, int y) {
        grid[y][x] = ship;
    }

    public boolean hasShipNearby(Coordinates coordinates) {
        return hasShipNearby(coordinates.getX(), coordinates.getY());
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
        NONE, SHOTTED
    }
}
