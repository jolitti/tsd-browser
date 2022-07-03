package it.unipd.sweng;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Loads the FXMl file of the start page and sets the scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it.unipd.sweng/StartPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Trust Service Browser");
        stage.setScene(scene);
        //sets the dimension of the window according to the screen size
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        stage.setMinWidth(screenSize.width/2);
        stage.setMinHeight(screenSize.height/2);
        stage.setMaxHeight(screenSize.height);
        stage.setMaxWidth(screenSize.width);
        //shows  the stage
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}