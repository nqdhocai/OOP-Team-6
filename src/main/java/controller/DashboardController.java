package controller;

import core.DashboardDataController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Tweet;
import model.User;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.camera.Camera;

import java.util.Comparator;
import java.util.List;


public class DashboardController {

    private DashboardDataController dashboardDataController = new DashboardDataController();
    private UserDetailController userDetailController = new UserDetailController();

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

    public void showAllNode() {
        updateGraphView("");
    }

    public void showUser() {
        updateGraphView("user");
    }

    public void showTweet() {
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

    private void updateUserRankingTable() {
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
        scoreColumn.setCellFactory(column -> {
            return new TableCell<User, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Chuyển đổi giá trị thành chuỗi với 2 chữ số sau dấu phẩy
                        setText(String.format("%.2f", item));
                    }
                }
            };
        });
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
        tableView.setItems(FXCollections.observableList(dashboardDataController.getUsers()));
        userRankingPane.getChildren().clear();
        userRankingPane.getChildren().add(tableView);
    }

    private void updateGraphView(String graphType) {
        MultiGraph totalGraph = new MultiGraph("Social Network");
        MultiGraph userGraph = new MultiGraph("User Network");
        MultiGraph tweetGraph = new MultiGraph("Tweet Network");

        MultiGraph graph;

        List<User> users = dashboardDataController.getUsers();
        List<Tweet> tweets = dashboardDataController.getTweets();

        for (User user : users) {
            String nodeStyles = "size: " + (user.getScore()) + "px; fill-color: #444;";

            totalGraph.addNode(user.getUserId()).setAttribute("ui.label", user.getUsername());
            userGraph.addNode(user.getUserId()).setAttribute("ui.label", user.getUsername());

            totalGraph.getNode(user.getUserId()).setAttribute("ui.style", nodeStyles);
            userGraph.getNode(user.getUserId()).setAttribute("ui.style", nodeStyles);
        }

        for (Tweet tweet : tweets) {
            String nodeStyles = "fill-color: #B1F0F7;";

            totalGraph.addNode(tweet.getTweetId()).setAttribute("ui.style", nodeStyles);

            tweetGraph.addNode(tweet.getTweetId()).setAttribute("ui.label", tweet.getAuthor());
            tweetGraph.getNode(tweet.getTweetId()).setAttribute("ui.style", nodeStyles);
        }

        for (User user : users) {
            List<String> userTweets = user.getTweets();
            List<String> followings = user.getFollowing();

            for (String userTweet : userTweets) {
                String nodeStyles = "fill-color: #B1F0F7;";
                String edgeId = user.getUserId() + userTweet;

                try {
                    totalGraph.addEdge(edgeId, user.getUserId(), userTweet);
                } catch (Exception ElementNotFoundException) {
                    totalGraph.addNode(userTweet).setAttribute("ui.style", nodeStyles);
                    totalGraph.addEdge(edgeId, user.getUserId(), userTweet);
                }

            }

            for (String following : followings) {
                String edgeId = user.getUserId() + following;
                String nodeStyles = "fill-color: #444;";
                try {
                    totalGraph.addEdge(edgeId, user.getUserId(), following);
                } catch (Exception ElementNotFoundException) {
                    totalGraph.addNode(following).setAttribute("ui.style", nodeStyles);
                    totalGraph.addEdge(edgeId, user.getUserId(), following);
                }
                try {
                    userGraph.addEdge(edgeId, user.getUserId(), following);
                } catch (Exception ElementNotFoundException) {
                    userGraph.addNode(following).setAttribute("ui.style", nodeStyles);
                    userGraph.addEdge(edgeId, user.getUserId(), following);
                }

            }
        }

        switch (graphType) {
            case "user":
                graph = userGraph;
                break;
            case "tweet":
                graph = tweetGraph;
                break;
            default:
                graph = totalGraph;
        }
        String cssStyle = "graph {fill-color: #EEEEEE;} node {text-alignment: under;}";
        graph.setAttribute("ui.stylesheet", cssStyle);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        // Create GraphStream JavaFX viewer
        Viewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout(); // Enable auto layout

        // Add default view
        View graphView = viewer.addDefaultView(false);

        ((Node) graphView).setStyle("-fx-pref-width: 540px; -fx-pref-height: 420px;");

        ScrollPane graphPane = new ScrollPane();
        graphPane.setPrefWidth(550);
        graphPane.setPrefHeight(430);

        graphPane.setContent((Node) graphView);

        graphViewPane.getChildren().add(graphPane);
        graphPane.setOnScroll(event -> {
            double zoomFactor = 1.1; // Hệ số zoom
            if (event.getDeltaY() > 0) {
                graphView.getCamera().setViewPercent(graphView.getCamera().getViewPercent() / zoomFactor);
            } else {
                graphView.getCamera().setViewPercent(graphView.getCamera().getViewPercent() * zoomFactor);
            }
            event.consume();
        });

        final ObjectProperty<Point2D> lastMousePosition = new SimpleObjectProperty<>();
        ((Node) graphView).setOnMousePressed(event -> {
            lastMousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
        });

        ((Node) graphView).setOnMouseDragged(event -> {
            Camera camera = graphView.getCamera();
            final double DRAG_SENSITIVITY = 0.05 / camera.getViewPercent();
            if (lastMousePosition.get() != null) {
                double deltaX = event.getSceneX() - lastMousePosition.get().getX();
                double deltaY = event.getSceneY() - lastMousePosition.get().getY();

                // Áp dụng hệ số giảm độ dịch chuyển
                deltaX *= DRAG_SENSITIVITY;
                deltaY *= DRAG_SENSITIVITY;

                camera.setViewCenter(
                        camera.getViewCenter().x - deltaX * camera.getViewPercent(),
                        camera.getViewCenter().y + deltaY * camera.getViewPercent(),
                        camera.getViewCenter().z
                );

                lastMousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
            }
        });

        ((Node) graphView).setOnMouseReleased(event -> {
            lastMousePosition.set(null);
        });
    }

}
