package imageChange;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("viewer.fxml"));
        primaryStage.setTitle("Seam Carving Project");
        final Scene scene = new Scene(root, 1600, 900);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

        public static void main (String[]args){
            launch(args);
        }
    }
