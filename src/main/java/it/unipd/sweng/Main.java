package it.unipd.sweng;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it.unipd.sweng/StartPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Trusted List");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(750);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}