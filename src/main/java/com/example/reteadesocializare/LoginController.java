package com.example.reteadesocializare;

import com.example.reteadesocializare.controller.Controller;
import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.exceptions.MyException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private Controller controller;
    @FXML
    private TextField loginEmail;
    @FXML
    private TextField loginPassword;
    @FXML
    private Label loginError;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void switchWindow(Long id) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("mainWindow.fxml"));
        Stage stage = new Stage();
        stage.setTitle("MyNetwork");
        stage.setScene(new Scene(fxmlLoader.load()));
        MainWindowController windowController = fxmlLoader.getController();
        windowController.setController(controller, id);
        loginError.getScene().getWindow().hide();
        stage.show();
    }

    @FXML
    protected void onConnectButtonClick() {
        String email = loginEmail.getText();
        String password = loginPassword.getText();
        try {
            User user = controller.getServiceUser().findOneByPredicate(user1 -> user1.getEmail().equals(email) && user1.getPassword().equals(password));
            try {
                switchWindow(user.getId());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MyException e) {
            loginError.setVisible(true);
        }

    }
}