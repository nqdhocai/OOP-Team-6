import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Đường dẫn đến file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/your.fxml"));

            // Load file FXML
            Parent root = loader.load();

            // Tạo Scene và hiển thị
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("FXML Debug Example");
            primaryStage.show();
        } catch (IOException e) {
            // In lỗi nếu không load được file FXML
            System.err.println("Error loading FXML file:");
            e.printStackTrace();
        } catch (Exception ex) {
            // In lỗi khác (nếu có)
            System.err.println("General error:");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
