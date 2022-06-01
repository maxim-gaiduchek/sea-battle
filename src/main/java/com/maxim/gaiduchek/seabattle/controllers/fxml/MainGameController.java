package com.maxim.gaiduchek.seabattle.controllers.fxml;

import com.maxim.gaiduchek.seabattle.controllers.App;
import com.maxim.gaiduchek.seabattle.controllers.Game;
import com.maxim.gaiduchek.seabattle.entities.Grid;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MainGameController {

    @FXML
    private GridPane playerGripPane, botGridPane;

    public void initialize() {
        if (Game.botGrid == null) {
            Game.generateBotGrid();
        }

        Game.setPlayerGridPane(playerGripPane);
        Game.setBotGridPane(botGridPane);

        Grid.forEachCoordinate((x, y) -> {
            Button button = new Button();

            button.setOpacity(0);
            button.setMinSize(49, 49);
            button.setPrefSize(49, 49);
            button.setMaxSize(49, 49);
            GridPane.setHalignment(button, HPos.CENTER);
            GridPane.setValignment(button, VPos.CENTER);

            final int finalX = x, finalY = y;

            button.setOnMouseClicked(mouseEvent -> Game.playerShot(finalX, finalY));

            botGridPane.add(button, x, y);

            drawCell(playerGripPane, Game.playerGrid, x, y);
            drawCell(botGridPane, Game.botGrid, x, y);
        });
    }

    private void drawCell(GridPane gridPane, Grid grid, int x, int y) {
        if (grid.isShotted(x, y)) {
            if (grid.hasShip(x, y)) {
                if (grid.isDestroyed(x, y)) {
                    gridPane.add(grid.getShip(x, y).getShipPart(x, y), x, y);
                }
                gridPane.add(App.getHitShotImageView(), x, y);
            } else {
                gridPane.add(App.getMissedShotImageView(), x, y);
            }
        }
    }
}
