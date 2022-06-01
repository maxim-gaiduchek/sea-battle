package com.maxim.gaiduchek.seabattle.controllers.fxml;

import com.maxim.gaiduchek.seabattle.controllers.App;
import com.maxim.gaiduchek.seabattle.controllers.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button loadPreviousGameButton;

    public void initialize() throws IOException, ClassNotFoundException {
        if (Game.loadGame()) {
            loadPreviousGameButton.setDisable(false);
        }
    }

    @FXML
    private void onGameSetupClick() throws IOException {
        App.openGameSetupView();
    }

    @FXML
    private void onGameResetClick() throws IOException {
        App.openMainGameView();
    }
}