package com.maxim.gaiduchek.seabattle.controllers;

import com.maxim.gaiduchek.seabattle.entities.Coordinates;
import com.maxim.gaiduchek.seabattle.entities.Grid;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Random;

public class Game {

    public static Grid playerGrid, botGrid;
    private static GridPane playerGridPane, botGridPane;
    private static boolean isPlayerMoving = true;
    private static Coordinates playerFirstShipPart, playerSecondShipPart;

    public static void generatePlayerGrid() {
        playerGrid = new Grid();
    }

    public static void generateBotGrid() {
        botGrid = new Grid();
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
            int x, y;

            if (playerFirstShipPart != null) {
                Coordinates nextShot = getNextDetectedShipShot();

                x = nextShot.x();
                y = nextShot.y();
            } else {
                Random random = new Random();

                do {
                    x = random.nextInt(Grid.MAX_X + 1);
                    y = random.nextInt(Grid.MAX_Y + 1);
                } while (!playerGrid.isNotShotted(x, y)); // !playerGrid.hasShip(x, y) ||
            }

            if (playerGrid.shot(playerGridPane, x, y)) {
                if (playerGrid.isDefeated()) {
                    App.openEndGameAlert("Ви програли \uD83D\uDE14");
                } else {
                    if (!playerGrid.isDestroyed(x, y)) {
                        if (playerFirstShipPart != null) {
                            playerSecondShipPart = new Coordinates(x, y);
                        } else {
                            playerFirstShipPart = new Coordinates(x, y);
                        }
                    } else {
                        playerFirstShipPart = null;
                        playerSecondShipPart = null;
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
}
