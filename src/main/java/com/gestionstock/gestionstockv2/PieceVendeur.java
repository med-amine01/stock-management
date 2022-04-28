package com.gestionstock.gestionstockv2;

import Classes.Pie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PieceVendeur implements Initializable {

    @FXML
    private TableView<Pie> table;
    @FXML
    private TableColumn<Pie, String> FOURcol;
    @FXML
    private TableColumn<Pie, Integer> IDcol;
    @FXML
    private TableColumn<Pie, String> MARcol;
    @FXML
    private TableColumn<Pie, String> MODcol;
    @FXML
    private TableColumn<Pie, Double> PRIXcol;
    @FXML
    private TableColumn<Pie, Integer> QTEcol;
    @FXML
    private TableColumn<Pie, String> SERcol;

    @FXML
    private TextField inputpiece;
    @FXML
    private Label user;
    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst1;
    ObservableList<Pie> list;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = getPieces("");
        Actualiser(list);
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

    //---------------------- chargement du tableau dans une liste (Search)-----------------------------
    public ObservableList<Pie> getPieces(String sqlSearch)
    {
        ObservableList<Pie> piceList = FXCollections.observableArrayList();
        connect();
        ResultSet rs;
        try
        {
            if(sqlSearch.equals(""))
            {
                pst1 = con.prepareStatement("SELECT piece.* , nom FROM piece, fournisseur where piece.idfour = fournisseur.idfour and piece.etat = 0; ");
                rs = pst1.executeQuery();
            }
            else
            {
                pst1 = con.prepareStatement(sqlSearch);
                rs = pst1.executeQuery();
            }

            Pie pieces;

            while (rs.next())
            {
                pieces = new Pie(rs.getInt("idpiece"), rs.getString("marque"),
                        rs.getString("modele"),rs.getString("serie"),
                        rs.getInt("qte"),rs.getDouble("prixunitaire"),
                        rs.getString("nom"),rs.getString("etat"));
                piceList.add(pieces);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return piceList;
    }
    //---------------------------- ACTUALISER -----------------------------
    public void Actualiser(ObservableList<Pie> list)
    {
        IDcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("idpiece"));
        MARcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("marque"));
        MODcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("modele"));
        SERcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("serie"));
        QTEcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("qte"));
        PRIXcol.setCellValueFactory(new PropertyValueFactory<Pie,Double>("prix"));
        FOURcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("nom"));

        table.setItems(list);
    }

    @FXML
    void rechClick(ActionEvent event)
    {
        String rech = inputpiece.getText().trim();
        if(rech.equals(""))
        {
            list = getPieces("");
            Actualiser(list);
        }
        else
        {
            if(ChampsIdEstInt(rech)) //3RAFNA ELI HOA YFARKESS BEL ID
            {
                String rqt = "select * from piece where etat = 0 and idpiece = "+rech;
                list = getPieces(rqt);

                if(list.isEmpty())
                {
                    Message("ID <"+rech+"> n'existe pas !!");
                    inputpiece.setText("");
                    inputpiece.requestFocus();
                }
                else
                {
                    Actualiser(list);
                }
            }
            else
            {
                String rqt = "select * from piece where marque like '"+rech+"%'" +
                        "or modele like '"+rech+"%' or serie like '"+rech+"%' HAVING etat = 0";
                list = getPieces(rqt);
                if(list.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !");
                    inputpiece.setText("");
                    inputpiece.requestFocus();
                }
                else
                {
                    Actualiser(list);
                }
            }
        }
    }
    @FXML
    void backClick(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("intervendeur.fxml"));
            root = loader.load();
            InterVendeur interVendeur = loader.getController();
            interVendeur.PrintUserName(user.getText());
            interVendeur.countNb();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void decClick(ActionEvent event)
    {
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



    //------------------------- alert message ---------------------
    private void Message(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    //--------------------- set user name (passage entre les controlleurs) -------------------
    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }

    //----------------------------id chiffres-----------------------------
    public boolean ChampsIdEstInt(String champsId)
    {
        boolean b ;
        try
        {
            Integer.parseInt(champsId);
            b = true;
        }
        catch(NumberFormatException e)
        {
            b = false;
        }
        return b;
    }

}
