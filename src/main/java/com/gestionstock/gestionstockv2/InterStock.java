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

public class InterStock implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst;

    @FXML
    private Label entreelabel;
    @FXML
    private Label user;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
    @FXML
    private Label entreeDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect();
        entreeDate.setText(LocalDate.now().format(dateFormatter));
        try
        {
            pst = con.prepareStatement("select count(*) from entree where date like '"+ entreeDate.getText()+"%' and etat = 0");
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                entreelabel.setText(rs.getString("COUNT(*)"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
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
    void entreeClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("entree.fxml"));
        root = loader.load();
        Entree entree = loader.getController();
        entree.PrintUserName(user.getText());
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }


    //--------------- DECONNEXION ------------
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

    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }
}
