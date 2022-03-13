package com.maxim.gaiduchek.seabattle.controllers.fxml;

import com.maxim.gaiduchek.seabattle.controllers.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void onGameSetupClick() throws IOException {
        App.openGameSetupView();
    }
}