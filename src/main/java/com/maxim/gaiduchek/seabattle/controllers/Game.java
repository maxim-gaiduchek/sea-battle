package com.maxim.gaiduchek.seabattle.controllers;

import com.maxim.gaiduchek.seabattle.entities.Grid;
import javafx.animation.PauseTransition;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Random;

public class Game {

    public static Grid playerGrid, botGrid;
    private static GridPane playerGridPane, botGridPane;
    private static boolean isPlayerMoving = true;

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

            if (!isPlayerMoving) {
                if (!botGrid.isDefeated()) {
                    botShot();
                } else {
                    App.openEndGameAlert("Ви вийграли!");
                }
            }
        }
    }

    private static void botShot() {
        Random random = new Random();
        PauseTransition pause = new PauseTransition(Duration.millis(500));

        pause.setOnFinished(actionEvent -> {
            int x, y;

            do {
                x = random.nextInt(Grid.MAX_X + 1);
                y = random.nextInt(Grid.MAX_Y + 1);
            } while (!playerGrid.isNotShotted(x, y)); // !playerGrid.hasShip(x, y) ||

            isPlayerMoving = !playerGrid.shot(playerGridPane, x, y);

            if (playerGrid.isDefeated()) {
                App.openEndGameAlert("Ви програли \uD83D\uDE14");
            } else if (!isPlayerMoving) {
                pause.play();
            }
        });

        pause.play();
    }
}
