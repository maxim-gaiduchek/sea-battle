package com.maxim.gaiduchek.seabattle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class App extends Application {

    private static Stage stage;

    // view controller

    public static void openMainMenuView() throws IOException {
        openView("main-menu-view", 800, 600);
    }

    public static void openGameSetupView() throws IOException {
        openView("game-setup-view", 800, 600);
    }

    public static void openMainGameView() throws IOException {
        openView("main-game-view", 1245, 700);
    }

    private static void openView(String viewName, int width, int height) throws IOException {
        stage.close();

        URL url = Objects.requireNonNull(App.class.getResource(viewName + ".fxml"));

        stage.setScene(new Scene(FXMLLoader.load(url), width, height));
        stage.show();
    }

    // images

    public static ImageView getShipPartImageView() {
        return getGridCellImageView("ship-part");
    }

    public static ImageView getMissedShotImageView() {
        return getGridCellImageView("missed-shot");
    }

    private static ImageView getGridCellImageView(String name) {
        ImageView imageView = new ImageView();
        InputStream imageInputStream = App.class.getResourceAsStream("images/" + name + ".png");

        imageView.setImage(new Image(Objects.requireNonNull(imageInputStream)));
        imageView.setFitHeight(49);
        imageView.setFitWidth(49);
        GridPane.setHalignment(imageView, HPos.CENTER);
        GridPane.setValignment(imageView, VPos.CENTER);

        return imageView;
    }

    // main

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;

        Image icon = new Image(Objects.requireNonNull(App.class.getResourceAsStream("images/icon.png")));

        stage.setTitle("SeaBattle");
        stage.getIcons().add(icon);
        stage.setResizable(false);

        openMainMenuView();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}