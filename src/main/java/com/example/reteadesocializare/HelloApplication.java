package com.example.reteadesocializare;

import com.example.reteadesocializare.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);

        Controller controller = new Controller();
        LoginController loginController = fxmlLoader.getController();
        loginController.setController(controller);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}