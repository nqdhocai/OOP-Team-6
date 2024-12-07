package ui;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Comparator;

public class TableRowClickPopupApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Tạo TableView
        TableView<User> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // Vô hiệu hóa resize

        // Tạo cột Username
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        usernameColumn.setPrefWidth(200);

        // Tạo cột Score
        TableColumn<User, Double> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());
        scoreColumn.setStyle("-fx-alignment: CENTER;");
        scoreColumn.setComparator(Comparator.naturalOrder());
        scoreColumn.setPrefWidth(48);

        // Thêm cột vào TableView
        tableView.getColumns().addAll(usernameColumn, scoreColumn);

        // Thêm dữ liệu
        tableView.getItems().addAll(
                new User("JohnDoe", 4.5),
                new User("JaneSmith", 3.8),
                new User("Alice", 4.9)
        );

        // Sự kiện khi click vào một hàng
        tableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) { // Single click
                    User clickedUser = row.getItem();
                    showPopup(clickedUser);
                }
            });
            return row;
        });

        // Layout chính
        StackPane root = new StackPane(tableView);
        Scene scene = new Scene(root, 250, 200);

        primaryStage.setTitle("Table Row Click Popup");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Hàm hiển thị popup
    private void showPopup(User user) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Details");
        alert.setHeaderText("Information about the selected user");
        alert.setContentText("Username: " + user.getUsername() + "\nScore: " + user.getScore());
        alert.showAndWait();
    }

    // Lớp User (Model)
    public static class User {
        private final SimpleStringProperty username;
        private final SimpleDoubleProperty score;

        public User(String username, double score) {
            this.username = new SimpleStringProperty(username);
            this.score = new SimpleDoubleProperty(score);
        }

        public String getUsername() {
            return username.get();
        }

        public SimpleStringProperty usernameProperty() {
            return username;
        }

        public double getScore() {
            return score.get();
        }

        public SimpleDoubleProperty scoreProperty() {
            return score;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
