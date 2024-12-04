module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires gs.core;
    requires gs.ui.javafx;

    opens app to javafx.fxml;
    exports ui;
    exports core;
    exports models;
    exports crawl;
}