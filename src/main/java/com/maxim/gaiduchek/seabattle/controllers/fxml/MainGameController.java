package com.maxim.gaiduchek.seabattle.controllers.fxml;

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
        Game.generateBotGrid();
        Game.setPlayerGridPane(playerGripPane);
        Game.setBotGridPane(botGridPane);

        for (int x = 0; x <= Grid.MAX_X; x++) {
            for (int y = 0; y <= Grid.MAX_Y; y++) {
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
            }
        }
    }
}
