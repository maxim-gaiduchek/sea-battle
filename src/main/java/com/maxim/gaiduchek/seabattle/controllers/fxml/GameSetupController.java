package com.maxim.gaiduchek.seabattle.controllers.fxml;

import com.maxim.gaiduchek.seabattle.controllers.App;
import com.maxim.gaiduchek.seabattle.controllers.Game;
import com.maxim.gaiduchek.seabattle.entities.Grid;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameSetupController {

    @FXML
    private Button startGameButton;
    @FXML
    private Button generateButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane shipsContainer;
    @FXML
    private Label leftClickHint;
    @FXML
    private Label rightClickHint;
    private List<Label> shipsLabels = new ArrayList<>();
    private List<HBox> shipsBoxes = new ArrayList<>();

    // selected ship params

    private int selectedShipLength = -1;
    private boolean isSelectedShipHorizontal = true;
    private int lastX = 0, lastY = 0;

    public void initialize() {
        Game.reset();
        showOnShipDropHints();

        for (Node node : shipsContainer.getChildren()) {
            Integer col = GridPane.getColumnIndex(node);

            if (col == null) {
                col = 0;
            }
            if (col > 1) {
                continue;
            }

            Integer row = GridPane.getRowIndex(node);

            if (row == null) {
                row = 0;
            }

            if (node instanceof Button) {
                Button button = (Button) node;
                int length = row + 1, count = Grid.MAX_SHIP_LENGTH - row;

                button.setOnMouseClicked(mouseEvent -> {
                    if (selectedShipLength != length) {
                        selectedShipLength = length;
                    } else {
                        resetShipSelection();
                    }
                });
            } else {
                if (col == 0) {
                    shipsLabels.add((Label) node);
                } else if (col == 1) {
                    shipsBoxes.add((HBox) node);
                }
            }
        }

        gridPane.setOnMouseMoved(mouseEvent -> {
            if (selectedShipLength != -1) {
                removeElement(gridPane, lastX, lastY);

                lastX = (int) ((Grid.MAX_X + 1) * mouseEvent.getX() / gridPane.getWidth());
                lastY = (int) ((Grid.MAX_Y + 1) * mouseEvent.getY() / gridPane.getHeight());

                ImageView shipImage = App.getUnaryShipImageView();

                shipImage.setOpacity(0.5);

                gridPane.add(shipImage, lastX, lastY);
            }
        });
        gridPane.setOnMouseExited(mouseEvent -> removeElement(gridPane, lastX, lastY));
    }

    // button

    @FXML
    private void onGameStartClick() throws IOException {
        App.openMainGameView();
    }

    @FXML
    private void onRandomizeGridClick() {
        startGameButton.setDisable(true);
        generateButton.setDisable(true);
        Game.generatePlayerGrid();
        updateGrid();
        startGameButton.setDisable(false);
        generateButton.setDisable(false);
    }

    // utils

    private void updateGrid() {
        gridPane.getChildren().removeIf(node -> node instanceof ImageView);

        Grid.forEachCoordinate((x, y) -> {
            if (Game.playerGrid.hasShip(x, y)) {
                gridPane.add(Game.playerGrid.getShip(x, y).getShipPart(x, y), x, y);
            } else if (Game.playerGrid.hasShipNearby(x, y)) {
                gridPane.add(App.getMissedShotImageView(), x, y);
            }
        });
    }

    private void showOnShipPickHints() {
        leftClickHint.setVisible(true);
        rightClickHint.setText("ПКМ - повернути корабель");
    }

    private void showOnShipDropHints() {
        leftClickHint.setVisible(false);
        rightClickHint.setText("ПКМ - видалити корабель");
    }

    private void setShipSelectActive(int shipLength) {
        shipsLabels.get(shipLength - 1).setOpacity(1);
        shipsBoxes.get(shipLength - 1).setOpacity(1);
    }

    private void setShipSelectInactive(int shipLength) {
        shipsLabels.get(shipLength - 1).setOpacity(0.5);
        shipsBoxes.get(shipLength - 1).setOpacity(0.5);
    }

    private void resetShipSelection() {
        selectedShipLength = -1;
        isSelectedShipHorizontal = true;
    }

    private void removeElement(GridPane gridPane, int x, int y) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView &&
                    GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                gridPane.getChildren().remove(node);
                break;
            }
        }
    }
}
