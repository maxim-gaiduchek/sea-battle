package com.maxim.gaiduchek.seabattle.controllers.fxml;

import com.maxim.gaiduchek.seabattle.controllers.App;
import com.maxim.gaiduchek.seabattle.controllers.Game;
import com.maxim.gaiduchek.seabattle.entities.Grid;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class GameSetupController {

    @FXML
    private Button startGameButton;
    @FXML
    private GridPane gridPane;

    public void initialize() {
        Game.reset();
    }

    // button

    @FXML
    private void onGameStartClick() throws IOException {
        App.openMainGameView();
    }

    @FXML
    private void onRandomizeGridClick() {
        startGameButton.setDisable(true);
        Game.generatePlayerGrid();
        updateGrid();
        startGameButton.setDisable(false);
    }

    // utils

    private void updateGrid() {
        gridPane.getChildren().removeIf(node -> node instanceof ImageView);

        for (int x = 0; x <= Grid.MAX_X; x++) {
            for (int y = 0; y <= Grid.MAX_Y; y++) {
                if (Game.playerGrid.hasShip(x, y)) {
                    gridPane.add(Game.playerGrid.getShip(x, y).getShipPart(x, y), x, y);
                } else if (Game.playerGrid.hasShipNearby(x, y)) {
                    gridPane.add(App.getMissedShotImageView(), x, y);
                }
            }
        }
    }
}
