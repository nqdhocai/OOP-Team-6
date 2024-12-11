module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires gs.core;
    requires gs.ui.javafx;
    requires com.fasterxml.jackson.databind;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.edge_driver;
    requires io.github.bonigarcia.webdrivermanager;

    opens core to javafx.fxml;

    exports core;
    exports app;
    exports controller;
    opens controller to javafx.fxml;
    exports model to com.fasterxml.jackson.databind;
}