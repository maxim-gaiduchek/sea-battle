module com.maxim.gaiduchek.seabattle {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.maxim.gaiduchek.seabattle to javafx.fxml;
    exports com.maxim.gaiduchek.seabattle;
    exports com.maxim.gaiduchek.seabattle.controllers;
    opens com.maxim.gaiduchek.seabattle.controllers to javafx.fxml;
}