package controller;

import core.SearchDataController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.SearchResult;
import model.Tweet;
import model.User;


import java.util.*;

public class SearchController {
    private final Map<String, Boolean> SEARCH_TYPE = new HashMap<>();
    private final SearchDataController SEARCH_DATA_CONTROLLER = new SearchDataController();
    private final UserDetailController USER_DETAIL_CONTROLLER = new UserDetailController();
    private final TweetDetailController TWEET_DETAIL_CONTROLLER = new TweetDetailController();
    @FXML
    private TextField searchTextField;

    @FXML
    private ListView<SearchResult> resultListView;

    @FXML
    public void initialize() {
        SEARCH_TYPE.put("user", true);
        SEARCH_TYPE.put("tweet", true);
        updateSearchResultView();
    }

    @FXML
    public void updateSearchResultView() {
        String keyword = searchTextField.getText();
        if (keyword.isEmpty()) {
            keyword = "";
        }
        ;

        List<SearchResult> searchResults = new ArrayList<>();

        if (SEARCH_TYPE.get("user")) {
            List<User> userResults = SEARCH_DATA_CONTROLLER.searchUser(keyword);
            if (userResults != null) {
                for (User user : userResults) {
                    searchResults.add(
                            new SearchResult("user", user.getUserId())
                    );
                }
            }
        }
        if (SEARCH_TYPE.get("tweet")) {
            List<Tweet> tweetResults = SEARCH_DATA_CONTROLLER.searchTweet(keyword);
            if (tweetResults != null) {
                for (Tweet tweet : tweetResults) {
                    searchResults.add(
                            new SearchResult("tweet", tweet.getTweetId())
                    );
                }
            }
        }
        resultListView.getItems().clear();
        resultListView.getItems().addAll(searchResults);
        resultListView.setCellFactory(new Callback<ListView<SearchResult>, ListCell<SearchResult>>() {
            @Override
            public ListCell<SearchResult> call(ListView<SearchResult> param) {
                return new ListCell<SearchResult>() {
                    @Override
                    protected void updateItem(SearchResult item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            if (item.getType().equals("user")) {
                                User user = SEARCH_DATA_CONTROLLER.getNodesData().getUser(item.getId());
                                setGraphic(createUserSearchResultCell(user));
                            } else {
                                Tweet tweet = SEARCH_DATA_CONTROLLER.getNodesData().getTweet(item.getId());
                                setGraphic(createTweetSearchResultCell(tweet));
                            }
                        }
                    }
                };
            }
        });
    }

    private HBox createUserSearchResultCell(User user) {
        HBox hbox = new HBox(10);
        VBox vbox = new VBox(10);
        Label userId = new Label(user.getUserId());
        Label username = new Label(user.getUsername());

        username.setStyle("-fx-font-weight: bold; -fxfont-size:14px");
        vbox.getChildren().addAll(username, userId);

        Image userAvt = new Image(String.valueOf(getClass().getResource("/assets/icon/account_icon.png")));

        ImageView imageView = new ImageView(userAvt);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        hbox.getChildren().addAll(imageView, vbox);

        hbox.setOnMouseClicked(event -> {
            try {
                USER_DETAIL_CONTROLLER.showUserDetail(user);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Information");
                alert.setHeaderText(null);  // Không cần tiêu đề cho phần header
                alert.setContentText("User information not found!");  // Nội dung thông báo

                // Hiển thị hộp thoại
                alert.showAndWait();
            }
        });

        return hbox;
    }

    private HBox createTweetSearchResultCell(Tweet tweet) {
        HBox hbox = new HBox(10);
        VBox vbox = new VBox(10);
        Label userId = new Label(tweet.getAuthor());
        Label content = new Label(tweet.getContent());

        userId.setStyle("-fx-font-weight: bold; -fxfont-size:14px");
        vbox.getChildren().addAll(userId, content);

        Image userAvt = new Image(String.valueOf(getClass().getResource("/assets/icon/tweet_icon.png")));

        ImageView imageView = new ImageView(userAvt);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        hbox.getChildren().addAll(imageView, vbox);

        hbox.setOnMouseClicked(event -> {
            try {
                TWEET_DETAIL_CONTROLLER.showTweetDetail(tweet);
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tweet Information");
                alert.setHeaderText(null);  // Không cần tiêu đề cho phần header
                alert.setContentText("Tweet information not found!");  // Nội dung thông báo

                // Hiển thị hộp thoại
                alert.showAndWait();
            }
        });

        return hbox;
    }

    // Hàm mở popup filter để chọn loại kết quả hiển thị
    @FXML
    private void openFilterPopup() {
        // Tạo một cửa sổ mới (popup)
        Stage filterStage = new Stage();
        filterStage.initModality(Modality.APPLICATION_MODAL);

        Label title = new Label("Result Filter");

        // Tạo các CheckBox cho việc lọc kết quả
        CheckBox userCheckBox = new CheckBox("User");
        CheckBox tweetCheckBox = new CheckBox("Tweet");

        userCheckBox.setSelected(SEARCH_TYPE.get("user"));
        tweetCheckBox.setSelected(SEARCH_TYPE.get("tweet"));

        // Tạo nút "OK" để áp dụng lựa chọn
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            SEARCH_TYPE.put("user", userCheckBox.isSelected());
            SEARCH_TYPE.put("tweet", tweetCheckBox.isSelected());
            filterStage.close();
        });

        // Đặt layout cho cửa sổ filter
        VBox vbox = new VBox(10, title, userCheckBox, tweetCheckBox, okButton);
        Scene scene = new Scene(vbox, 50, 110);
        filterStage.setScene(scene);
        filterStage.setTitle("Result Filter");
        filterStage.show();
    }
}
