package controller;

import core.DashboardDataController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import model.UserRow;
import ui.TableRowClickPopupApp;

import java.util.Comparator;


public class DashboardController {
    private DashboardDataController dashboardDataController = new DashboardDataController();

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
        TableView<UserRow> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // Vô hiệu hóa resize

        // Tạo cột Username
        TableColumn<UserRow, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        usernameColumn.setPrefWidth(200);

        // Tạo cột Score
        TableColumn<UserRow, Double> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());
        scoreColumn.setStyle("-fx-alignment: CENTER;");
        scoreColumn.setComparator(Comparator.naturalOrder());
        scoreColumn.setPrefWidth(48);

        // Thêm cột vào TableView
        tableView.getColumns().addAll(usernameColumn, scoreColumn);

        // Thêm dữ liệu
        tableView.setItems(dashboardDataController.getUserRows());

        userRankingPane.getChildren().add(tableView);
    }
}
