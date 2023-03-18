package com.example.reteadesocializare;

import com.example.reteadesocializare.controller.Controller;
import com.example.reteadesocializare.utils.Observer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.List;

public class MessageController implements Observer {
    private Long id;
    private Long idF;
    private Controller controller;

    @FXML
    private TextField msg;

    @FXML
    private TextArea chat;

    public void setController(Long id1, Long id2, Controller controller) {
        id = id1;
        idF = id2;
        this.controller = controller;

        msg.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                msgEnterKey();
            }
        } );

        chat.setEditable(false);
        chat.setMouseTransparent(true);
        chat.setFocusTraversable(false);
        List<String> rez = controller.getServiceMessage().findAll(id);
        for (String str : rez) {
            chat.appendText(str);
        }
    }

    public void msgEnterKey() {
        if (msg.getText().isEmpty())
            return;
        controller.getServiceMessage().save(id, idF, msg.getText(), controller.getServiceUser().findOne(id).getName());
        msg.clear();
    }

    @Override
    public void update() {
        chat.appendText(controller.getServiceMessage().findLast(id));
    }
}
