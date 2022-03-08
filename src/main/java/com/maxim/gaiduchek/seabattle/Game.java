package com.maxim.gaiduchek.seabattle;

import com.maxim.gaiduchek.seabattle.entities.Grid;

public class Game {

    public static Grid playerGrid, botGrid;

    public static void generatePlayerGrid() {
        if (playerGrid == null) {
            playerGrid = new Grid();
        }
    }

    public static void generateBotGrid() {
        if (botGrid == null) {
            botGrid = new Grid();
        }
    }
}
