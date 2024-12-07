package controller;

import core.DashboardDataController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.User;

import java.io.IOException;
import java.util.Comparator;


public class DashboardController {
    private DashboardDataController dashboardDataController = new DashboardDataController();
    private UserDetailController userDetailController = new UserDetailController();

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private AnchorPane userRankingPane;

    @FXML
    private AnchorPane graphViewPane;

    @FXML
    private Label totalUserLabel;

    @FXML
    private Label totalTweetLabel;

    @FXML
    public void initialize() {
        updateTotalTweet();
        updateTotalUser();
        updateUserRankingTable();
    }

    public void updateTotalUser() {
        int totalUser = dashboardDataController.getTotalUsers();
        totalUserLabel.setText(String.valueOf(totalUser));
    }

    public void updateTotalTweet() {
        int totalTweet = dashboardDataController.getTotalTweets();
        totalTweetLabel.setText(String.valueOf(totalTweet));
    }

    public void updateUserRankingTable(){
        TableView<User> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // Vô hiệu hóa resize

        // Tạo cột Username
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        usernameColumn.setPrefWidth(200);

        // Tạo cột Score
        TableColumn<User, Double> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getScore()).asObject());
        scoreColumn.setStyle("-fx-alignment: CENTER;");
        scoreColumn.setComparator(Comparator.naturalOrder());
        scoreColumn.setPrefWidth(48);

        tableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) { // Single click
                    User clickedUser = row.getItem();
                    try {
                        userDetailController.showUserDetail(clickedUser);
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("User Information");
                        alert.setHeaderText(null);  // Không cần tiêu đề cho phần header
                        alert.setContentText("User information not found!");  // Nội dung thông báo

                        // Hiển thị hộp thoại
                        alert.showAndWait();
                    }
                }
            });
            return row;
        });

        // Thêm cột vào TableView
        tableView.getColumns().addAll(usernameColumn, scoreColumn);

        // Thêm dữ liệu
        tableView.setItems(dashboardDataController.getUserRows());

        userRankingPane.getChildren().add(tableView);
    }
}
