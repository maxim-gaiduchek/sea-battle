package com.maxim.gaiduchek.seabattle.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {

    public static final int MAX_X = 10, MAX_Y = 10;

    private List<Ship> ships = new ArrayList<>();

    public Grid() {
        Random random = new Random();

        for (int length = 5; length >= 1; length--) {
            for (int i = 5 - length + 1; i > 0; i--) {
                Coordinates begin, end;

                while (true) {
                    if (random.nextBoolean()) {
                        int x = random.nextInt(MAX_X - length + 1), y = random.nextInt(MAX_Y + 1);

                        begin = new Coordinates(x, y);
                        end = new Coordinates(x + length, y);
                    } else {
                        int x = random.nextInt(MAX_X + 1), y = random.nextInt(MAX_Y - length + 1);

                        begin = new Coordinates(x, y);
                        end = new Coordinates(x, y + length);
                    }

                    if (!isOnShip(begin) && !isOnShip(end)) {
                        break;
                    }
                }

                ships.add(new Ship(begin, end));
            }
        }
    }

    public boolean isOnShip(Coordinates coordinates) {
        for (Ship ship : ships) {
            if (ship.isOnShip(coordinates)) {
                return true;
            }
        }

        return false;
    }
}
