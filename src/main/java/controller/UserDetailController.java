package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import model.User;

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
        String defaultImageUrl = String.valueOf(getClass().getResource("/assets/icon/user_avt.png"));
        Image userAvatar;
        ImageView imageView;

        try {
            userAvatar = new Image(imageUrl);
            if (userAvatar.isError()) {
                throw new Exception("Error loading image from URL");
            }
        } catch (Exception e) {
            userAvatar = new Image(defaultImageUrl);
        }

        imageView = new ImageView(userAvatar);

        // Tạo một Circle để cắt hình ảnh thành hình tròn
        Circle clip = new Circle();
        clip.setCenterX(50); // Vị trí tâm của Circle
        clip.setCenterY(50); // Vị trí tâm của Circle
        clip.setRadius(50);  // Bán kính của Circle

        // Cài đặt Clip cho ImageView
        imageView.setClip(clip);

        imageView.setFitWidth(100);  // Chiều rộng của hình ảnh
        imageView.setFitHeight(100); // Chiều cao của hình ảnh
        imageView.setPreserveRatio(true);

        userAvtImage.getChildren().add(imageView);
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
