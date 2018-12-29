package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();
        // Closing SessionFactory at close sample.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        loader.load();
        Controller controller = loader.getController();
        primaryStage.setOnCloseRequest(event -> {
            controller.endSessionFactory();
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
