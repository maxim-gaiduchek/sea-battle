package com.maxim.gaiduchek.seabattle.controllers;

import com.maxim.gaiduchek.seabattle.entities.Coordinates;
import com.maxim.gaiduchek.seabattle.entities.Grid;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.*;
import java.util.Random;

public class Game {

    private static final String FILENAME = "game.dat";

    public static Grid playerGrid = new Grid(), botGrid;
    private static GridPane playerGridPane, botGridPane;
    private static boolean isPlayerMoving = true;
    private static Coordinates playerFirstShipPart, playerSecondShipPart;

    private Game() {
    }

    // creating

    public static void reset() {
        playerGrid = new Grid();
        botGrid = null;

        playerGridPane = null;
        botGridPane = null;

        isPlayerMoving = true;

        playerFirstShipPart = null;
        playerSecondShipPart = null;
    }

    public static void generatePlayerGrid() {
        playerGrid = Grid.generate();
    }

    public static void generateBotGrid() {
        botGrid = Grid.generate();
    }

    public static void saveGame() throws IOException {
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(FILENAME));

        if (botGrid != null) {
            out.writeBoolean(true);
            out.writeObject(playerGrid);
            out.writeObject(botGrid);
            out.writeBoolean(isPlayerMoving);
        } else {
            out.writeBoolean(false);
        }

        out.close();
    }

    public static boolean loadGame() throws IOException, ClassNotFoundException {
        File file = new File(FILENAME);

        if (!file.exists()) {
            return false;
        }

        ObjectInput in = new ObjectInputStream(new FileInputStream(file));

        if (!in.readBoolean()) {
            return false;
        }

        playerGrid = (Grid) in.readObject();
        botGrid = (Grid) in.readObject();
        isPlayerMoving = in.readBoolean();

        in.close();
        file.delete();

        return true;

    }

    // game

    public static void setPlayerGridPane(GridPane playerGridPane) {
        Game.playerGridPane = playerGridPane;
    }

    public static void setBotGridPane(GridPane botGridPane) {
        Game.botGridPane = botGridPane;
    }

    public static void playerShot(int x, int y) {
        if (isPlayerMoving && !botGrid.isShotted(x, y)) {
            isPlayerMoving = botGrid.shot(botGridPane, x, y);

            if (botGrid.isDefeated()) {
                Platform.runLater(() -> App.openEndGameAlert("ВИ ВИЙГРАЛИ!!!"));
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

                for (int i = playerGrid.getShipsCountLength() - 1; i >= 0; i--) {
                    if (playerGrid.getShipsCount(i + 1) > 0) {
                        int len = i + 1;

                        do {
                            if (random.nextBoolean()) {
                                x = (random.nextInt(Grid.MAX_X / len + (i == 0 ? 1 : 0)) + 1) * len - 1;
                                y = random.nextInt(Grid.MAX_Y + 1);
                            } else {
                                x = random.nextInt(Grid.MAX_X + 1);
                                y = (random.nextInt(Grid.MAX_Y / len + (i == 0 ? 1 : 0)) + 1) * len - 1;
                            }
                        } while ((i != 0 && gcd(x + 1, y + 1) % len == 0) || playerGrid.isShotted(x, y));

                        break;
                    }
                }
            }

            if (playerGrid.shot(playerGridPane, x, y)) {
                if (playerGrid.isDefeated()) {
                    Platform.runLater(() -> App.openEndGameAlert("Ви програли :("));
                } else {
                    if (playerGrid.isDestroyed(x, y)) {
                        playerFirstShipPart = null;
                        playerSecondShipPart = null;
                        playerGrid.decrementShipsCount(playerGrid.getShip(x, y).getLength());
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

    // utils

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

            if (playerGrid.isShotted(x, y)) {
                x = playerFirstShipPart.x() - Integer.compare(playerSecondShipPart.x(), playerFirstShipPart.x());
                y = playerFirstShipPart.y() - Integer.compare(playerSecondShipPart.y(), playerFirstShipPart.y());
            }
        } else {
            int shipX = playerFirstShipPart.x(), shipY = playerFirstShipPart.y();

            if (shipX > 0 && !playerGrid.isShotted(shipX - 1, shipY)) {
                x = shipX - 1;
                y = shipY;
            } else if (shipX < Grid.MAX_X && !playerGrid.isShotted(shipX + 1, shipY)) {
                x = shipX + 1;
                y = shipY;
            } else if (shipY > 0 && !playerGrid.isShotted(shipX, shipY - 1)) {
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
