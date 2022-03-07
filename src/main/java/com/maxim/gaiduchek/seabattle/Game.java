package com.maxim.gaiduchek.seabattle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Game extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        Game.stage = stage;

        Image icon = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("images/icon.png")));

        stage.setTitle("SeaBattle");
        stage.getIcons().add(icon);
        stage.setResizable(false);

        openMainMenuView();

        stage.show();
    }

    public static void openMainMenuView() throws IOException {
        openView("main-menu-view", 800);
    }

    public static void openGameSetupView() throws IOException {
        openView("game-setup-view", 800);
    }

    public static void openMainGameView() throws IOException {
        openView("main-game-view", 1200);
    }

    private static void openView(String viewName, int width) throws IOException {
        URL url = Objects.requireNonNull(Game.class.getResource(viewName + ".fxml"));

        stage.setScene(new Scene(FXMLLoader.load(url), width, 600));
    }

    public static void main(String[] args) {
        launch();
    }
}