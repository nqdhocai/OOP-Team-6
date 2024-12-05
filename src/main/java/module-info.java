module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires gs.core;
    requires gs.ui.javafx;
    requires com.fasterxml.jackson.databind;
    requires MaterialFX;

    opens core to javafx.fxml;

    exports core;
    exports ui;
    exports controller;
    opens controller to javafx.fxml;
    exports model to com.fasterxml.jackson.databind;
}