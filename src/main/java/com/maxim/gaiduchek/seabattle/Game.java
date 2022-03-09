package com.maxim.gaiduchek.seabattle;

import com.maxim.gaiduchek.seabattle.entities.Grid;
import javafx.scene.layout.GridPane;

import java.util.Random;

public class Game {

    public static Grid playerGrid, botGrid;
    private static GridPane playerGridPane, botGridPane;

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
        botGrid.shot(botGridPane, x, y);

        if (!botGrid.isDefeated()) {
            botShot();
        } else {
            App.openEndGameAlert("Ви вийграли!");
        }
    }

    private static void botShot() {
        Random random = new Random();
        int x = random.nextInt(Grid.MAX_X + 1), y = random.nextInt(Grid.MAX_Y + 1);

        /*while (!playerGrid.hasShip(x, y) || !playerGrid.isNotShotted(x, y)) {
            x = random.nextInt(Grid.MAX_X + 1);
            y = random.nextInt(Grid.MAX_Y + 1);
        }*/

        playerGrid.shot(playerGridPane, x, y);

        if (playerGrid.isDefeated()) {
            App.openEndGameAlert("Ви програли \uD83D\uDE14");
        }
    }
}
