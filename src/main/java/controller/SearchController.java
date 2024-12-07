package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Nodes;
import model.Tweet;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SearchController {
    private final Nodes data = new Nodes();
    @FXML
    private VBox result;

    @FXML
    private TextField searchTextField;

    private ListView<User> listViewUsers;
    private ListView<Tweet> listViewTweets;

    private <T> ListView<T> createListView(Function<T, HBox> cellContentProvider) {
        ListView<T> listView = new ListView<>();
        listView.setCellFactory(param -> new ListCell<>() {
            private HBox content;

            {
                content = new HBox();
                content.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                content.setPrefWidth(200.0);
            }

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                double cellHeight = 25.0;
                listView.setPrefHeight(cellHeight * listView.getItems().size());
                if (item != null && !empty) {
                    content = cellContentProvider.apply(item);
                    setGraphic(content);
                } else {
                    setGraphic(null);
                }
            }
        });

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Object selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showDetailView(selectedItem);
                }
            }
        });

        return listView;
    }

    private HBox createUserCellContent(User user) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(20.0);
        imageView.setFitWidth(20.0);
        HBox.setMargin(imageView, new Insets(0, 5, 0, 5));
        try {
            imageView.setImage(new Image(user.getProfileImageUrl()));
        } catch (Exception e) {
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icon/account_icon.png"))));
        }

        Label label = new Label("[User]  " + user.getUsername());
        label.setPrefHeight(20.0);
        label.setPrefWidth(200.0);
        HBox.setMargin(label, new Insets(0, 0, 0, 10));

        return new HBox(imageView, label);
    }

    private HBox createTweetCellContent(Tweet tweet) {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icon/tweet_icon.png"))));
        imageView.setFitHeight(20.0);
        imageView.setFitWidth(20.0);
        HBox.setMargin(imageView, new Insets(0, 5, 0, 5));

        Label authorLabel = new Label("[Tweet]  " + tweet.getAuthor());
        authorLabel.setPrefHeight(20.0);
        authorLabel.setPrefWidth(200.0);
        HBox.setMargin(authorLabel, new Insets(0, 0, 0, 10));

        Label timeLabel = new Label(tweet.getCreatedAt());
        timeLabel.setPrefWidth(100.0);

        return new HBox(imageView, authorLabel, timeLabel);
    }

    @FXML
    public void initialize() {
        searchTextField.setOnAction(event -> search());

        listViewUsers = createListView(this::createUserCellContent);
        listViewTweets = createListView(this::createTweetCellContent);

        double cellHeight = 25.0;

        // Adjust the height of the list views based on the number of items
        listViewUsers.setPrefHeight(cellHeight * listViewUsers.getItems().size());
        listViewTweets.setPrefHeight(cellHeight * listViewTweets.getItems().size());

        System.out.println(listViewUsers.getItems().size());

        VBox listViewContainer = new VBox(10); // 10 is the spacing between the list views
        listViewContainer.getChildren().addAll(listViewUsers, listViewTweets);

        result.getChildren().clear();
        result.getChildren().add(listViewContainer);
    }


    public void search() {
        String searchText = searchTextField.getText();
        
        List<User> users = data.getUsers().stream().filter(s -> s.getBio().contains(searchText)).toList();
        List<Tweet> tweets = data.getTweets().stream().filter(s -> {
            List<String> hashtags = s.getHashtags();
            for(String hashtag : hashtags) {
                if(hashtag.contains(searchText))
                    return true;
            }
            return false;
        }).toList();
        
        listViewUsers.getItems().clear();
        listViewTweets.getItems().clear();
        listViewUsers.getItems().addAll(users);
        listViewTweets.getItems().addAll(tweets);
    }

    private void showDetailView(Object item) {
        try {
            FXMLLoader loader = new FXMLLoader();
            if (item instanceof User) {
                loader.setLocation(getClass().getResource("/path/to/user_detail_view.fxml"));
            } else if (item instanceof Tweet) {
                loader.setLocation(getClass().getResource("/path/to/tweet_detail_view.fxml"));
            }
            Parent detailView = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(detailView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}