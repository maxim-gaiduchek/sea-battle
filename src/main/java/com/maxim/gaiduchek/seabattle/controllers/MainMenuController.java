package com.maxim.gaiduchek.seabattle.controllers;

import com.maxim.gaiduchek.seabattle.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainMenuController {

    @FXML
    protected void onGameSetupClick() throws IOException {
        App.openGameSetupView();
    }
}