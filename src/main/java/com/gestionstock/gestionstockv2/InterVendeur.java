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
import java.sql.*;
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
    private static String idemp;
    Connection con;
    PreparedStatement pst;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect();

    }

    //-------------------- Connection à la base de donnée -----------------------
    public void connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/gestionstock", "root","");

        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
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
        commande.setIdemp(getIdemp());

        commande.printSumPrixCmd();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void PieceClick(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("piecevendeur.fxml"));
            root = loader.load();
            PieceVendeur pieceVendeur = loader.getController();
            pieceVendeur.PrintUserName(user.getText());
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
    void decClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void countNb()
    {
        try
        {
            pst = con.prepareStatement("select count(*) from commande where datecmd like '"+ LocalDate.now().format(dateFormatter)+"%' and idemp = "+getIdemp());
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                sortielabel.setText(rs.getString("COUNT(*)"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        sortieDate.setText(LocalDate.now().format(dateFormatter));
    }



    //--------------------- set user name (passage entre les controlleurs) -------------------
    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }

    public static String getIdemp() {
        return idemp;
    }

    public void setIdemp(String idemp) {
        InterVendeur.idemp = idemp;
    }
}
