package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import model.Nodes;
import model.Tweet;

public class TweetDetailController extends DetailController {

    @FXML
    private Label hashtagsLabel;
    @FXML
    private Label contentLabel;
    @FXML
    private Label uploadTimeLabel;

    private Nodes nodes = new Nodes(String.valueOf(getClass().getResource("/data/NodeData.json").getPath()));

    public void showTweetDetail(Tweet clickedTweet) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/tweet_detail_view.fxml"));
        DialogPane dialogView = loader.load();
        TweetDetailController controller = loader.getController();

        controller.updateTweetDetail(clickedTweet);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogView);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void updateTweetDetail(Tweet clickedTweet) {
        String authorName = nodes.getUser(clickedTweet.getAuthor()).getUsername();
        StringBuilder hashtags = new StringBuilder();
        for (String hashtag : clickedTweet.getHashtags()) {
            hashtags.append(hashtag).append(" ");
        }
        Image userAvatar = new Image(String.valueOf(getClass().getResource("/assets/icon/user_avt.png")));

        ImageView imageView = new ImageView(userAvatar);;

        // Tạo một Circle để cắt hình ảnh thành hình tròn
        Circle clip = new Circle();
        clip.setCenterX(25); // Vị trí tâm của Circle
        clip.setCenterY(25); // Vị trí tâm của Circle
        clip.setRadius(25);  // Bán kính của Circle

        // Cài đặt Clip cho ImageView
        imageView.setClip(clip);

        imageView.setFitWidth(50);  // Chiều rộng của hình ảnh
        imageView.setFitHeight(50); // Chiều cao của hình ảnh
        imageView.setPreserveRatio(true);

        userAvt.getChildren().add(imageView);
        userNameLabel.setText(authorName);
        userIDLabel.setText("@"+clickedTweet.getAuthor());
        hashtagsLabel.setText(hashtags.toString());
        contentLabel.setText(clickedTweet.getContent());
        uploadTimeLabel.setText(clickedTweet.getCreatedAt());
    }
}
