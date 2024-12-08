package controller;

import core.DashboardDataController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Tweet;
import model.User;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;

import java.util.Comparator;
import java.util.List;


public class DashboardController {

    private final DashboardDataController dashboardDataController = new DashboardDataController();
    private final UserDetailController userDetailController = new UserDetailController();

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
        updateGraphView("");
    }
    public void showAllNode(){
        updateGraphView("");
    }
    public void showUser(){
        updateGraphView("user");
    }
    public void showTweet(){
        updateGraphView("tweet");
    }

    private void updateTotalUser() {
        int totalUser = dashboardDataController.getTotalUsers();
        totalUserLabel.setText(String.valueOf(totalUser));
    }

    private void updateTotalTweet() {
        int totalTweet = dashboardDataController.getTotalTweets();
        totalTweetLabel.setText(String.valueOf(totalTweet));
    }

    private void updateUserRankingTable(){
        TableView<User> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // Vô hiệu hóa resize
        tableView.setPrefHeight(430);

        // Tạo cột Username
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        usernameColumn.setPrefWidth(225);

        // Tạo cột Score
        TableColumn<User, Double> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getScore()).asObject());
        scoreColumn.setStyle("-fx-alignment: CENTER;");
        scoreColumn.setComparator(Comparator.naturalOrder());
        scoreColumn.setPrefWidth(75);

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

    private void updateGraphView(String graphType){
        MultiGraph totalGraph = new MultiGraph("Social Network");
        MultiGraph userGraph = new MultiGraph("User Network");
        MultiGraph tweetGraph = new MultiGraph("Tweet Network");

        MultiGraph graph;

        List<User> users = dashboardDataController.getUsers();
        List<Tweet> tweets = dashboardDataController.getTweets();

        for (User user : users) {
            String nodeStyles = "size: "+( user.getScore() * 5) +"px; fill-color: #444;";

            totalGraph.addNode(user.getUserId()).setAttribute("ui.label", user.getUsername());
            userGraph.addNode(user.getUserId()).setAttribute("ui.label", user.getUsername());

            totalGraph.getNode(user.getUserId()).setAttribute("ui.style", nodeStyles);
            userGraph.getNode(user.getUserId()).setAttribute("ui.style", nodeStyles);
        }

        for (Tweet tweet : tweets) {
            totalGraph.addNode(tweet.getTweetId());
            tweetGraph.addNode(tweet.getTweetId());
        }

        for (User user : users) {
            List<String> userTweets = user.getTweets();
            List<String> followings = user.getFollowing();

            for (String userTweet : userTweets) {
                String edgeId = user.getUserId() + userTweet;

                totalGraph.addEdge(edgeId, user.getUserId(), userTweet);
            }

            for (String following : followings) {
                String edgeId = user.getUserId() + following;

                totalGraph.addEdge(edgeId, user.getUserId(), following);
                userGraph.addEdge(edgeId, user.getUserId(), following);
            }
        }

        switch (graphType){
            case "user":
                graph = userGraph;
                break;
            case "tweet":
                graph = tweetGraph;
                break;
            default:
                graph = totalGraph;
        }

        // Create GraphStream JavaFX viewer
        Viewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout(); // Enable auto layout

        // Add default view
        Node graphView = (Node) viewer.addDefaultView(false);

        graphView.setStyle("-fx-pref-width: 550px; -fx-pref-height: 430px; -fx-background-color: #EEE");

        ScrollPane graphPane = new ScrollPane();
        graphPane.setContent(graphView);

        graphViewPane.getChildren().add(graphPane);
    }

}
