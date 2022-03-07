package com.maxim.gaiduchek.seabattle.controllers;

import com.maxim.gaiduchek.seabattle.Game;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MainMenuController {

    @FXML
    protected void onGameSetupStartClick() throws IOException {
        Game.openGameSetupView();
    }
}