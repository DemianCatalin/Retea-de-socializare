package com.example.reteadesocializare;

import com.example.reteadesocializare.controller.Controller;
import com.example.reteadesocializare.domain.Friendship;
import com.example.reteadesocializare.domain.FriendshipState;
import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.exceptions.MyException;
import com.example.reteadesocializare.models.FriendModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class MainWindowController {
    private Controller controller;
    private Long id;
    private User user;
    private final ObservableList<FriendModel> friendsModel = FXCollections.observableArrayList();
    private final ObservableList<FriendModel> friendsModel1 = FXCollections.observableArrayList();
    private final ObservableList<FriendModel> friendsModel2 = FXCollections.observableArrayList();

    @FXML
    private Label lblnume;
    @FXML
    private Label lblemail;
    @FXML
    private Label lblnrprieteni;
    @FXML
    private ImageView avatar;
    @FXML
    private TableView<FriendModel> tableF;
    @FXML
    private TableColumn<FriendModel, String> nameCol;
    @FXML
    private TableColumn<FriendModel, String> emailCol;
    @FXML
    private TableColumn<FriendModel, String> dateCol;
    @FXML
    private TableView<FriendModel> tableC;
    @FXML
    private TableColumn<FriendModel, String> nameCol1;
    @FXML
    private TableColumn<FriendModel, String> dateCol1;
    @FXML
    private TextField searchT;
    @FXML
    private Label searchM;
    @FXML
    private TableView<FriendModel> tableC2;
    @FXML
    private TableColumn<FriendModel, String> nameCol2;
    @FXML
    private TableColumn<FriendModel, String> dateCol2;

    public void setController(Controller controller, Long id) {
        try {
            Image image = new Image(new FileInputStream("avatar.png"));
            avatar.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.controller = controller;
        this.id = id;
        user = controller.getServiceUser().findOne(id);
        lblnume.setText("Nume: " + user.getName());
        lblemail.setText("Email: " + user.getEmail());
        searchT.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                searchEnterKey();
            }
        } );

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableF.setItems(friendsModel);
        showFriends();
        nameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol1.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableC.setItems(friendsModel1);
        showRequests1();
        nameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol2.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableC2.setItems(friendsModel2);
        showRequests2();
    }

    private void searchEnterKey() {
        String name = searchT.getText();
        try {
            User user1 = controller.getServiceUser().findOneByPredicate(u -> u.getName().equals(name));
            controller.getServiceFriendship().add(id, user1.getId(), LocalDateTime.now(), FriendshipState.Pending);
            searchM.setText("Cerere trimisa!");
            searchM.setTextFill(Color.web("#7a75ff"));
            searchM.setVisible(true);
        }
        catch (MyException e) {
            searchM.setText(e.getMessage());
            searchM.setTextFill(Color.web("#ff5858"));
            searchM.setVisible(true);
        }
        showRequests2();
    }

    @FXML
    protected void deleteFriendClicked() {
        FriendModel fr = tableF.getSelectionModel().getSelectedItem();
        if (fr != null) {
            controller.getServiceFriendship().remove(fr.getId());
        }
        showFriends();
    }

    @FXML
    protected void acceptFriendClicked() {
        FriendModel fr = tableC.getSelectionModel().getSelectedItem();
        if (fr != null) {
            Friendship friendship = controller.getServiceFriendship().findOne(fr.getId());
            controller.getServiceFriendship().update(friendship.getId(), friendship.getFirst(), friendship.getSecond(), LocalDateTime.now(), FriendshipState.Accepted);
        }
        showFriends();
        showRequests1();
        showRequests2();
    }

    private void showFriends() {
        Collection<FriendModel> prieteni = new HashSet<>();
        Collection<Friendship> all = controller.getServiceFriendship().findAll();
        for (Friendship fr : all) {
            if (fr.contains(id) && fr.getState().equals(FriendshipState.Accepted)){
                if(fr.getFirst().equals(id))
                    user = controller.getServiceUser().findOne(fr.getSecond());
                else
                    user = controller.getServiceUser().findOne(fr.getFirst());
                prieteni.add(new FriendModel(fr.getId(), user.getName(), user.getEmail(), fr.getFriendsFrom().toString()));
            }
        }
        List<FriendModel> fr = new ArrayList<>(prieteni);
        friendsModel.setAll(fr);

        lblnrprieteni.setText(fr.size() + " prieteni");
    }

    private void showRequests1() {
        Collection<FriendModel> prieteni = new HashSet<>();
        Collection<Friendship> all = controller.getServiceFriendship().findAll();
        for (Friendship fr : all) {
            if (fr.getSecond().equals(id) && fr.getState().equals(FriendshipState.Pending)){
                if(fr.getFirst().equals(id))
                    user = controller.getServiceUser().findOne(fr.getSecond());
                else
                    user = controller.getServiceUser().findOne(fr.getFirst());
                prieteni.add(new FriendModel(fr.getId(), user.getName(), user.getEmail(), fr.getFriendsFrom().toString()));
            }
        }
        List<FriendModel> fr = new ArrayList<>(prieteni);
        friendsModel1.setAll(fr);
    }

    private void showRequests2() {
        Collection<FriendModel> prieteni = new HashSet<>();
        Collection<Friendship> all = controller.getServiceFriendship().findAll();
        for (Friendship fr : all) {
            if (fr.getFirst().equals(id) && fr.getState().equals(FriendshipState.Pending)){
                if(fr.getFirst().equals(id))
                    user = controller.getServiceUser().findOne(fr.getSecond());
                else
                    user = controller.getServiceUser().findOne(fr.getFirst());
                prieteni.add(new FriendModel(fr.getId(), user.getName(), user.getEmail(), fr.getFriendsFrom().toString()));
            }
        }
        List<FriendModel> fr = new ArrayList<>(prieteni);
        friendsModel2.setAll(fr);
    }

    @FXML
    protected void deleteRequestClicked() {
        FriendModel fr = tableC2.getSelectionModel().getSelectedItem();
        if (fr != null) {
            controller.getServiceFriendship().remove(fr.getId());
        }
        showRequests2();
    }

    private void openChat(Long id1, Long id2) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("message.fxml"));
            Stage stage = new Stage();
            stage.setTitle(controller.getServiceUser().findOne(id1).getName());
            stage.setScene(new Scene(fxmlLoader.load()));
            MessageController messageController = fxmlLoader.getController();
            controller.getServiceMessage().addObserver(messageController);
            messageController.setController(id1, id2, controller);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void messageClicked() {
        FriendModel fr = tableF.getSelectionModel().getSelectedItem();
        if (fr != null) {
            Friendship friendship = controller.getServiceFriendship().findOne(fr.getId());
            openChat(friendship.getFirst(), friendship.getSecond());
            openChat(friendship.getSecond(), friendship.getFirst());
        }
    }

    @FXML
    private void logOut(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(fxmlLoader.load()));
            LoginController loginController = fxmlLoader.getController();
            loginController.setController(controller);
            tableF.getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
