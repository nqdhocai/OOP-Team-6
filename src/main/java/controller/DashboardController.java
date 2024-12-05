package controller;

import core.DashboardDataController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class DashboardController {
    private DashboardDataController dashboardDataController = new DashboardDataController();

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private Label totalUserLabel;

    @FXML
    private Label totalTweetLabel;

    @FXML
    public void initialize() {
        updateTotalTweet();
        updateTotalUser();
    }

    public void updateTotalUser() {
        int totalUser = dashboardDataController.getTotalUsers();
        totalUserLabel.setText(String.valueOf(totalUser));
    }

    public void updateTotalTweet() {
        int totalTweet = dashboardDataController.getTotalTweets();
        totalTweetLabel.setText(String.valueOf(totalTweet));
    }
}
