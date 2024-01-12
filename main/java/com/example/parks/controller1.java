package com.example.parks;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class controller1 {

    @FXML
    void paycontral(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fourth.fxml"));
        Stage stage = new Stage();
        stage.setTitle("缴费管理");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    void potcontral(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("second.fxml"));
        Stage stage = new Stage();
        stage.setTitle("车位管理");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    void stopcontral(ActionEvent event) throws  IOException {
        Parent root = FXMLLoader.load(getClass().getResource("third.fxml"));
        Stage stage = new Stage();
        stage.setTitle("车辆信息管理");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();

    }

}
