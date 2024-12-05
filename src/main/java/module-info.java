module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires gs.core;
    requires gs.ui.javafx;

    opens core to javafx.fxml;

    exports core;
    exports ui;
}