package com.maxim.gaiduchek.seabattle.controllers;

import com.maxim.gaiduchek.seabattle.entities.Coordinates;
import com.maxim.gaiduchek.seabattle.entities.Grid;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Random;

public class Game {

    public static Grid playerGrid, botGrid;
    private static GridPane playerGridPane, botGridPane;
    private static boolean isPlayerMoving = true;
    private static Coordinates playerFirstShipPart, playerSecondShipPart;
    private static int[] playerShipsCount;

    public static void reset() {
        playerGrid = null;
        botGrid = null;

        playerGridPane = null;
        botGridPane = null;

        isPlayerMoving = true;

        playerFirstShipPart = null;
        playerSecondShipPart = null;

        playerShipsCount = null;
    }

    public static void generatePlayerGrid() {
        playerGrid = new Grid();
        playerShipsCount = generateShipsCount();
    }

    public static void generateBotGrid() {
        botGrid = new Grid();
    }

    public static int[] generateShipsCount() {
        int[] shipsCount = new int[Grid.MAX_SHIP_LENGTH];

        for (int length = Grid.MAX_SHIP_LENGTH; length >= 1; length--) {
            shipsCount[length - 1] = Grid.MAX_SHIP_LENGTH - length + 1;
        }

        return shipsCount;
    }

    public static void setPlayerGridPane(GridPane playerGridPane) {
        Game.playerGridPane = playerGridPane;
    }

    public static void setBotGridPane(GridPane botGridPane) {
        Game.botGridPane = botGridPane;
    }

    public static void playerShot(int x, int y) {
        if (isPlayerMoving) {
            isPlayerMoving = botGrid.shot(botGridPane, x, y);

            if (botGrid.isDefeated()) {
                App.openEndGameAlert("ВИ ВИЙГРАЛИ!!!");
            } else if (!isPlayerMoving) {
                botShot();
            }
        }
    }

    private static void botShot() {
        PauseTransition pause = new PauseTransition(Duration.millis(500));

        pause.setOnFinished(actionEvent -> {
            int x = 0, y = 0;

            if (playerFirstShipPart != null) {
                Coordinates nextShot = getNextDetectedShipShot();

                x = nextShot.x();
                y = nextShot.y();
            } else {
                Random random = new Random();

                for (int len : Arrays.asList(4, 5, 3, 2, 1)) { // 4 in the beginning because there is more chance to shot on 4, then on 5
                    if (playerShipsCount[len - 1] > 0) {
                        do { // TODO search len-th ships in coordinates, if there is a place for this ship
                            if (random.nextBoolean()) {
                                x = (random.nextInt(Grid.MAX_X / len + (len == 1 ? 1 : 0)) + 1) * len - 1;
                                y = random.nextInt(Grid.MAX_Y + 1);
                            } else {
                                x = random.nextInt(Grid.MAX_X + 1);
                                y = (random.nextInt(Grid.MAX_Y / len + (len == 1 ? 1 : 0)) + 1) * len - 1;
                            }
                        } while ((len != 1 && gcd(x + 1, y + 1) % len == 0) || !playerGrid.isNotShotted(x, y));

                        break;
                    }
                }
            }

            if (playerGrid.shot(playerGridPane, x, y)) {
                if (playerGrid.isDefeated()) {
                    Platform.runLater(() -> App.openEndGameAlert("Ви програли \uD83D\uDE14"));
                } else {
                    if (playerGrid.isDestroyed(x, y)) {
                        playerFirstShipPart = null;
                        playerSecondShipPart = null;
                        playerShipsCount[playerGrid.getShip(x, y).getLength() - 1]--;
                    } else {
                        if (playerFirstShipPart != null) {
                            playerSecondShipPart = new Coordinates(x, y);
                        } else {
                            playerFirstShipPart = new Coordinates(x, y);
                        }
                    }

                    pause.play();
                }
            } else {
                playerSecondShipPart = null;
                isPlayerMoving = true;
            }
        });

        pause.play();
    }

    private static Coordinates getNextDetectedShipShot() {
        int x, y;

        assert playerFirstShipPart != null;
        if (playerSecondShipPart != null) {
            if (playerSecondShipPart.x() == 0 || playerSecondShipPart.x() == Grid.MAX_X) {
                x = playerFirstShipPart.x() - Integer.compare(playerSecondShipPart.x(), playerFirstShipPart.x());
            } else {
                x = playerSecondShipPart.x() + Integer.compare(playerSecondShipPart.x(), playerFirstShipPart.x());
            }

            if (playerSecondShipPart.y() == 0 || playerSecondShipPart.y() == Grid.MAX_Y) {
                y = playerFirstShipPart.y() - Integer.compare(playerSecondShipPart.y(), playerFirstShipPart.y());
            } else {
                y = playerSecondShipPart.y() + Integer.compare(playerSecondShipPart.y(), playerFirstShipPart.y());
            }

            if (!playerGrid.isNotShotted(x, y)) {
                x = playerFirstShipPart.x() - Integer.compare(playerSecondShipPart.x(), playerFirstShipPart.x());
                y = playerFirstShipPart.y() - Integer.compare(playerSecondShipPart.y(), playerFirstShipPart.y());
            }
        } else {
            int shipX = playerFirstShipPart.x(), shipY = playerFirstShipPart.y();

            if (shipX > 0 && playerGrid.isNotShotted(shipX - 1, shipY)) {
                x = shipX - 1;
                y = shipY;
            } else if (shipX < Grid.MAX_X && playerGrid.isNotShotted(shipX + 1, shipY)) {
                x = shipX + 1;
                y = shipY;
            } else if (shipY > 0 && playerGrid.isNotShotted(shipX, shipY - 1)) {
                x = shipX;
                y = shipY - 1;
            } else { // if (shipX < Grid.MAX_Y)
                x = shipX;
                y = shipY + 1;
            }
        }

        return new Coordinates(x, y);
    }

    private static int gcd(int a, int b) {
        while (a % b > 0 && b % a > 0) {
            if (a > b) {
                a %= b;
            } else {
                b %= a;
            }
        }

        return Math.min(a, b);
    }
}
