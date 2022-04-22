package com.gestionstock.gestionstockv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InterVendeur implements Initializable {

    @FXML
    private Label sortieDate;

    @FXML
    private Label sortielabel;

    @FXML
    private Label user;

    private Stage stage;
    private Scene scene;
    private Parent root;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortieDate.setText(LocalDate.now().format(dateFormatter));
    }

    @FXML
    void clientClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
            root = loader.load();
            Client client = loader.getController();
            client.PrintUserName(user.getText());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cmdClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("commande.fxml"));
        root = loader.load();
        Commande commande = loader.getController();
        commande.PrintUserName(user.getText());
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void decClick(ActionEvent event) {

    }


    //--------------------- set user name (passage entre les controlleurs) -------------------
    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }

}
