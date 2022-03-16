package com.maxim.gaiduchek.seabattle.controllers;

import com.maxim.gaiduchek.seabattle.Main;
import com.maxim.gaiduchek.seabattle.entities.Coordinates;
import com.maxim.gaiduchek.seabattle.entities.Ship;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        URL url = Objects.requireNonNull(Main.class.getResource(viewName + ".fxml"));

        stage.setScene(new Scene(FXMLLoader.load(url), width, height));
        stage.show();
    }

    // images

    private static Image getAppIcon() {
        return new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/icon.png")));
    }

    public static ImageView getHitShotImageView() {
        return getGridCellImageView("hit-shot");
    }

    public static ImageView getMissedShotImageView() {
        return getGridCellImageView("missed-shot");
    }

    public static ImageView getCornerShipImageView() {
        return getGridCellImageView("corner-ship-part");
    }

    public static ImageView getMiddleShipImageView() {
        return getGridCellImageView("middle-ship-part");
    }

    public static ImageView getUnaryShipImageView() {
        return getGridCellImageView("unary-ship-part");
    }

    private static ImageView getGridCellImageView(String name) {
        ImageView imageView = new ImageView();
        InputStream imageInputStream = Main.class.getResourceAsStream("images/" + name + ".png");

        imageView.setImage(new Image(Objects.requireNonNull(imageInputStream)));
        imageView.setFitHeight(49);
        imageView.setFitWidth(49);
        GridPane.setHalignment(imageView, HPos.CENTER);
        GridPane.setValignment(imageView, VPos.CENTER);

        return imageView;
    }

    // alerts

    public static void openEndGameAlert(String prompt) {
        prompt += "\n\nХочете пограти ще раз?";
        Alert alert = new Alert(Alert.AlertType.NONE, prompt, ButtonType.YES, ButtonType.NO);

        alert.setTitle("SeaBattle");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(getAppIcon());
        alert.showAndWait();

        try {
            if (alert.getResult() == ButtonType.YES) {
                App.openGameSetupView();
            } else if (alert.getResult() == ButtonType.NO) {
                Platform.exit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void openCloseApplicationAlert() {
        ButtonType no = new ButtonType("Ні", ButtonBar.ButtonData.OK_DONE);
        ButtonType yes = new ButtonType("Так", ButtonBar.ButtonData.CANCEL_CLOSE);
        String prompt = "Ви дійсно хочете вийти з гри?";
        Alert alert = new Alert(Alert.AlertType.NONE, prompt, no, yes);

        alert.setTitle("SeaBattle");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(getAppIcon());
        alert.showAndWait();

        if (alert.getResult().equals(no)) {
            alert.close();
        } else if (alert.getResult().equals(yes)) {
            Platform.exit();
        }
    }

    // animations

    public static void playAppearingAnimation(Node node) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), node);

        transition.setFromX(1.33);
        transition.setFromY(1.33);
        transition.setToX(1);
        transition.setToY(1);

        transition.play();
    }

    public static void playRotateAnimation(Node node) {
        RotateTransition rotate = new RotateTransition(Duration.millis(200), node);

        rotate.setToAngle(node.getRotate() + 90);

        rotate.play();
    }

    // main

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;

        stage.setTitle("SeaBattle");
        stage.getIcons().add(getAppIcon());
        stage.setResizable(false);

        stage.setOnCloseRequest(windowEvent -> {
            openCloseApplicationAlert();
            windowEvent.consume();
        });

        openMainMenuView();

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}