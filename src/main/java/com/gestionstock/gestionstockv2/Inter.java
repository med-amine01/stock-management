package com.gestionstock.gestionstockv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Inter {

    @FXML
    private Button clientBtn;

    @FXML
    private Button decBtn;

    @FXML
    private Button empBtn;

    @FXML
    private Button entreeBtn;

    @FXML
    private Button fourBtn;

    @FXML
    private Button pieceBtn;

    @FXML
    private Button sortieBtn;

    @FXML
    private Label user;

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    void empClick(ActionEvent event)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("employe.fxml"));
            root = loader.load();
            Employe employe = loader.getController();
            employe.PrintUserName(user.getText());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void pieceClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("piece.fxml"));
            root = loader.load();
            Piece piece = loader.getController();
            piece.PrintUserName(user.getText());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void FourClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fournisseur.fxml"));
            root = loader.load();
            Fournisseur fournisseur = loader.getController();
            fournisseur.PrintUserName(user.getText());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clientClick(ActionEvent event) {

    }

    @FXML
    void entreeClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("entreeadmin.fxml"));
            root = loader.load();
            EntreeAdmin entreeAdmin = loader.getController();
            entreeAdmin.PrintUserName(user.getText());
            entreeAdmin.Timer();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void sortieClick(ActionEvent event) {

    }

    @FXML
    void decClick(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }





    public void PrintAdminName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }

}

