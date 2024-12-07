package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;

public class UserDetailController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label useridLabel;
    @FXML
    private Label userBioLabel;
    @FXML
    private Label userFollowerLabel;
    @FXML
    private Label userFollowingLabel;
    @FXML
    private AnchorPane userAvtImage;

    public void updateUserDetails(User user) {
        usernameLabel.setText(user.getUsername());
        useridLabel.setText(user.getUserId());
        userBioLabel.setText(user.getBio());
        userFollowerLabel.setText(user.getFollowersCount());
        userFollowingLabel.setText(user.getFollowingCount());
        // URL ảnh người dùng
        String imageUrl = user.getProfileImageUrl();  // Đường dẫn đến ảnh

        // URL ảnh mặc định khi xảy ra lỗi
        String defaultImageUrl = String.valueOf(getClass().getResource("/assets/icon/account_icon.png"));

        try {
            // Cố gắng tải ảnh từ URL
            Image image = new Image(imageUrl);

            // Kiểm tra nếu ảnh hợp lệ
            if (image.isError()) {
                throw new Exception("Error loading image from URL");
            }

            // Nếu không có lỗi, sử dụng URL ảnh người dùng
            userAvtImage.setStyle("-fx-background-radius: 50; -fx-border-radius: 50; " +
                    "-fx-background-image: url('" + imageUrl + "'); " +
                    "-fx-background-size: cover;");
        } catch (Exception e) {
            // Nếu xảy ra lỗi, sử dụng ảnh mặc định
            userAvtImage.setStyle("-fx-background-radius: 50; -fx-border-radius: 50; " +
                    "-fx-background-image: url('" + defaultImageUrl + "'); " +
                    "-fx-background-size: cover;");
        }
    }

    public void showUserDetail(User clickedUser) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/user_detail_view.fxml"));
        DialogPane dialogView = loader.load();
        UserDetailController controller = loader.getController();

        controller.updateUserDetails(clickedUser);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogView);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
}
