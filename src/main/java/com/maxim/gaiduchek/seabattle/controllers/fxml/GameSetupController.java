package com.maxim.gaiduchek.seabattle.controllers.fxml;

import com.maxim.gaiduchek.seabattle.controllers.App;
import com.maxim.gaiduchek.seabattle.controllers.Game;
import com.maxim.gaiduchek.seabattle.entities.Coordinates;
import com.maxim.gaiduchek.seabattle.entities.Grid;
import com.maxim.gaiduchek.seabattle.entities.Ship;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameSetupController {

    @FXML
    private Button startGameButton;
    @FXML
    private Button generateButton;
    @FXML
    private Button clearButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane shipsContainer;
    @FXML
    private Label leftClickHint;
    @FXML
    private Label rightClickHint;
    private final List<Label> shipsLabels = new ArrayList<>();
    private final List<HBox> shipsBoxes = new ArrayList<>();

    // selected ship params

    private int selectedShipLength = -1;
    private boolean isSelectedShipHorizontal = true;
    private int lastX = Grid.MAX_X, lastY = Grid.MAX_Y;

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
            if (row > Grid.MAX_SHIP_LENGTH) {
                continue;
            }

            if (node instanceof Button) {
                Button button = (Button) node;
                int length = row + 1;
                final int finalRow = row;

                button.setOnMouseClicked(mouseEvent -> {
                    if (Game.playerGrid.isLengthNotCompleted(length)) {
                        if (selectedShipLength != length) {
                            selectedShipLength = length;

                            resetButtonsOpacity();
                            setButtonsOpacity(finalRow, 0.3);
                            showOnShipPickHints();
                        } else {
                            resetShipSelection();
                            setButtonsOpacity(finalRow, 0);
                            showOnShipDropHints();
                        }
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
                Ship ship = getSelectedShip();
                Coordinates coordinates = getGridPaneCoordinatesOfMouse(mouseEvent);

                if (lastX != coordinates.x() || lastY != coordinates.y()) {
                    Ship newShip = getSelectedShip(coordinates.x(), coordinates.y());

                    if (canSelectedShipBePlaced(newShip)) {
                        lastX = coordinates.x();
                        lastY = coordinates.y();

                        removeShipFromGridPane(ship);
                        drawShipSilhouette(newShip);
                    }
                }
            }
        });
        gridPane.setOnMouseExited(mouseEvent -> {
            removeShipFromGridPane(getSelectedShip());
            lastX = Grid.MAX_X + 1;
            lastY = Grid.MAX_Y + 1;
        });
        gridPane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && selectedShipLength != -1) {
                Ship ship = getSelectedShip();

                if (canSelectedShipBePlaced(ship)) {
                    removeShipFromGridPane(ship);
                    Game.playerGrid.addShip(ship);
                    updateGrid();
                    clearButton.setDisable(false);
                    decrementShipCount(selectedShipLength);

                    lastX = Grid.MAX_X + 1;
                    lastY = Grid.MAX_Y + 1;

                    if (Game.playerGrid.isFullyCompleted()) {
                        startGameButton.setDisable(false);
                    }
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                if (selectedShipLength == -1) {
                    Coordinates coordinates = getGridPaneCoordinatesOfMouse(mouseEvent);

                    if (Game.playerGrid.hasShip(coordinates)) {
                        Ship ship = Game.playerGrid.getShip(coordinates);

                        Game.playerGrid.removeShip(coordinates);
                        incrementShipCount(ship.getLength());
                        startGameButton.setDisable(true);
                        updateGrid();
                    }
                } else {
                    removeShipFromGridPane(getSelectedShip());

                    isSelectedShipHorizontal = !isSelectedShipHorizontal;

                    Ship newShip = getSelectedShip();

                    if (canSelectedShipBePlaced(newShip)) {
                        drawShipSilhouette(newShip);
                    }
                }
            }
        });
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
        setAllShipsCounts();
        startGameButton.setDisable(false);
        generateButton.setDisable(false);
        clearButton.setDisable(false);
    }

    @FXML
    private void onClearButtonClick() {
        resetShipSelection();
        Game.reset();
        updateGrid();
        setAllShipsCounts();
        resetButtonsOpacity();
        clearButton.setDisable(true);
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

    private void drawShipSilhouette(Ship ship) {
        ship.forEachCoordinate((x, y) -> {
            ImageView shipImage = ship.getShipPart(x, y);

            shipImage.setOpacity(0.5);

            gridPane.add(shipImage, x, y);
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

    private void resetButtonsOpacity() {
        for (Node node1 : shipsContainer.getChildren()) {
            if (node1 instanceof Button) {
                node1.setOpacity(0);
            }
        }
    }

    private void setButtonsOpacity(int row, double opacity) {
        for (Node node1 : shipsContainer.getChildren()) {
            if (node1 instanceof Button) {
                Integer row1 = GridPane.getRowIndex(node1);

                if (row1 == null) {
                    row1 = 0;
                }

                if (row1.equals(row)) {
                    node1.setOpacity(opacity);
                }
            }
        }
    }

    private void decrementShipCount(int shipLength) {
        Label label = shipsLabels.get(shipLength - 1);
        int maxCount = Grid.MAX_SHIP_LENGTH - shipLength + 1;
        int count = maxCount - Game.playerGrid.getShipsCount(shipLength);

        label.setText(count + "/" + maxCount);

        if (count <= 0) {
            setShipSelectInactive(shipLength);
            resetButtonsOpacity();
            resetShipSelection();
        }
    }

    private void incrementShipCount(int shipLength) {
        Label label = shipsLabels.get(shipLength - 1);
        int maxCount = Grid.MAX_SHIP_LENGTH - shipLength + 1;
        int count = maxCount - Game.playerGrid.getShipsCount(shipLength);

        label.setText(count + "/" + maxCount);

        if (count > 0) {
            setShipSelectActive(shipLength);
        }
    }

    private void setAllShipsCounts() {
        Grid.forEachShipLength(length -> {
            int maxCount = Grid.MAX_SHIP_LENGTH - length + 1;
            int count = maxCount - Game.playerGrid.getShipsCount(length);

            shipsLabels.get(length - 1).setText(count + "/" + maxCount);

            if (count <= 0) {
                setShipSelectInactive(length);
            } else {
                setShipSelectActive(length);
            }
        });
    }

    private void setShipSelectActive(int shipLength) {
        shipsLabels.get(shipLength - 1).setOpacity(1);
        shipsBoxes.get(shipLength - 1).setOpacity(1);
    }

    private void setShipSelectInactive(int shipLength) {
        shipsLabels.get(shipLength - 1).setOpacity(0.5);
        shipsBoxes.get(shipLength - 1).setOpacity(0.5);
    }

    private Coordinates getGridPaneCoordinatesOfMouse(MouseEvent mouseEvent) {
        int x = (int) ((Grid.MAX_X + 1) * mouseEvent.getX() / gridPane.getWidth());
        int y = (int) ((Grid.MAX_Y + 1) * mouseEvent.getY() / gridPane.getHeight());

        return new Coordinates(x, y);
    }

    private Ship getSelectedShip() {
        return getSelectedShip(lastX, lastY);
    }

    private Ship getSelectedShip(int x, int y) {
        int beginX, endX;
        int beginY, endY;

        if (isSelectedShipHorizontal) {
            beginX = Math.min(Math.max(x - (int) (Math.ceil(selectedShipLength / 2.0) - 1), 0), Grid.MAX_X - selectedShipLength + 1);
            endX = beginX + selectedShipLength - 1;
            beginY = endY = y;
        } else {
            beginY = Math.min(Math.max(y - (int) (Math.ceil(selectedShipLength / 2.0) - 1), 0), Grid.MAX_Y - selectedShipLength + 1);
            endY = beginY + selectedShipLength - 1;
            beginX = endX = x;
        }

        return new Ship(new Coordinates(beginX, beginY), new Coordinates(endX, endY));
    }

    private void resetShipSelection() {
        selectedShipLength = -1;
        isSelectedShipHorizontal = true;
        lastX = Grid.MAX_X + 1;
        lastY = Grid.MAX_Y + 1;
    }

    private void removeShipFromGridPane(Ship ship) {
        for (Node node : new HashSet<>(gridPane.getChildren())) {
            if (node instanceof ImageView) {
                Integer x = GridPane.getColumnIndex(node);
                Integer y = GridPane.getRowIndex(node);

                if (x == null) x = 0;
                if (y == null) y = 0;

                if (ship.isOnShip(x, y)) {
                    gridPane.getChildren().remove(node);
                }
            }
        }
    }

    private boolean canSelectedShipBePlaced(Ship ship) {
        if (ship.getBeginX() < 0 || ship.getEndX() > Grid.MAX_X || ship.getBeginY() < 0 || ship.getEndY() > Grid.MAX_Y) {
            return false;
        }

        for (int x = ship.getBeginX(); x <= ship.getEndX(); x++) {
            for (int y = ship.getBeginY(); y <= ship.getEndY(); y++) {
                if (Game.playerGrid.hasShipNearby(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}
