package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class NavbarController {
    @FXML
    private BorderPane rootPane; // Phải khớp với fx:id trong FXML

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    private void showSearchView() {
        loadView("/app/search_view.fxml");
    }

    @FXML
    private void showDashboard() {
        loadView("/app/dashboard_view.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlFile));
            rootPane.setCenter(view); // Thay thế nội dung
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
