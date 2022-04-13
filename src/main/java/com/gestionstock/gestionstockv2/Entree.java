package com.gestionstock.gestionstockv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Entree implements Initializable {



    //---------DONNÉE PIECE --------------
    @FXML
    private TableView<Pie> tablePiece;
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


    //----------- ENTREE -----------------
    @FXML
    private TableView<Ent> tableEntree;
    @FXML
    private TableColumn<Ent, String> DATEcol;
    @FXML
    private TableColumn<Ent,String> FOURcol;
    @FXML
    private TableColumn<Ent,Integer> IDEncol;
    @FXML
    private TableColumn<Ent,Double> MONTcol;
    @FXML
    private TableColumn<Ent,Integer> PIECEcol;
    @FXML
    private TableColumn<Ent,Integer> QTENTcol;



    @FXML
    private TextField date;

    @FXML
    private TextField fournisseur;

    @FXML
    private TextField inputEntree;

    @FXML
    private TextField inputpiece;

    @FXML
    private TextField piece;

    @FXML
    private TextField qte;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label user;

    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst;
    ObservableList<Pie> listPiece;
    ObservableList<Ent> listEntree;
    String pattern = "dd/MM/YYYY";
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        datePicker.setPromptText(LocalDate.now().format(dateFormatter));
        datePicker.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });



        listPiece = getPieces("");
        listEntree = getEntree("");
        ActualiserPiece(listPiece);
        ActualiserEntree(listEntree);
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

    //---------------------- chargement du tableau dans une liste-----------------------------
    public ObservableList<Pie> getPieces(String sqlSearch)
    {
        ObservableList<Pie> piceList = FXCollections.observableArrayList();
        connect();
        ResultSet rs;
        try
        {
            if(sqlSearch.equals(""))
            {
                pst = con.prepareStatement("select * from piece where etat = 0");
                rs= pst.executeQuery();
            }
            else
            {
                pst = con.prepareStatement(sqlSearch);
                rs = pst.executeQuery();
            }

            Pie pieces;

            while (rs.next())
            {
                pieces = new Pie(rs.getInt("idpiece"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getString("serie"),
                        rs.getInt("qte"),
                        rs.getDouble("prixunitaire"),
                        rs.getInt("idfour"),
                        rs.getString("etat"));
                piceList.add(pieces);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return piceList;
    }

    //------------------- CHARGEMENT ----------------
    public ObservableList<Ent> getEntree(String sqlSearch)
    {
        ObservableList<Ent> entreeList = FXCollections.observableArrayList();
        connect();
        ResultSet rs;
        try
        {

            if(sqlSearch.equals(""))
            {
                pst = con.prepareStatement("select entree.* , fournisseur.nom from entree,fournisseur where entree.idfour = fournisseur.idfour and entree.etat = 0");
                rs= pst.executeQuery();
            }
            else
            {
                pst = con.prepareStatement(sqlSearch);
                rs = pst.executeQuery();
            }

            Ent entrees;

            while (rs.next())
            {
                entrees = new Ent(rs.getInt("identree"), rs.getInt("idpiece"),
                        rs.getString("nom"), rs.getInt("qte"),
                        rs.getString("date"),rs.getDouble("montant"));
                entreeList.add(entrees);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return entreeList;
    }


    public void ActualiserPiece(ObservableList<Pie> list)
    {
        IDcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("idpiece"));
        MARcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("marque"));
        MODcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("modele"));
        SERcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("serie"));
        QTEcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("qte"));
        PRIXcol.setCellValueFactory(new PropertyValueFactory<Pie,Double>("prix"));

        tablePiece.setItems(list);
    }
    public void ActualiserEntree(ObservableList<Ent> list)
    {
        IDEncol.setCellValueFactory(new PropertyValueFactory<Ent,Integer>("id"));
        PIECEcol.setCellValueFactory(new PropertyValueFactory<Ent,Integer>("idpiece"));
        FOURcol.setCellValueFactory(new PropertyValueFactory<Ent,String>("nomfour"));
        QTENTcol.setCellValueFactory(new PropertyValueFactory<Ent,Integer>("qte"));
        DATEcol.setCellValueFactory(new PropertyValueFactory<Ent,String>("date"));
        MONTcol.setCellValueFactory(new PropertyValueFactory<Ent,Double>("montant"));

        tableEntree.setItems(list);
    }



    @FXML
    void ajoutClick(ActionEvent event) {

    }

    @FXML
    void backClick(ActionEvent event) {

    }

    @FXML
    void consulterClick(ActionEvent event) {

    }

    @FXML
    void decClick(ActionEvent event) {

    }

    @FXML
    void ligneEntreClick(MouseEvent event) {

    }

    @FXML
    void lignePieceClick(MouseEvent event) {
        Pie pie = tablePiece.getSelectionModel().getSelectedItem();
        String nomfour = "";
        try {
            pst = con.prepareStatement("select nom from fournisseur where idfour = "+pie.getIdfour()+";");
            System.out.println(pie.getIdfour());
            ResultSet rs2 = pst.executeQuery();

            while(rs2.next()) //parcours sur les tentatives
            {
                nomfour = rs2.getString("nom");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


        piece.setText(String.valueOf(pie.getIdpiece()));
        fournisseur.setText(nomfour);
        qte.setText("0");
        date.setText(LocalDate.now().format(dateFormatter));
    }

    @FXML
    void rechEntreeClick(ActionEvent event)
    {
        String rech = inputEntree.getText().trim();
        String date = dateClick(event);

        if(rech.equals(""))
        {
            if(date.equals(""))
            {
                String rqt = "select entree.* , fournisseur.nom from entree,fournisseur where entree.idfour = fournisseur.idfour and entree.etat = 0 " +
                        "HAVING date like '"+LocalDate.now().format(dateFormatter)+"%'";
                listEntree = getEntree(rqt);
                ActualiserEntree(listEntree);
            }
            else
            {
                String rqt = "select entree.* , fournisseur.nom from entree,fournisseur where entree.idfour = fournisseur.idfour and entree.etat = 0 " +
                        "HAVING date like '"+date+"%'";
                listEntree = getEntree(rqt);
                ActualiserEntree(listEntree);
            }

        }
        else
        {
            if(ChampsIdEstInt(rech)) //3RAFNA ELI HOA YFARKESS BEL ID
            {
                String rqt = "select entree.* , fournisseur.nom from entree, fournisseur " +
                "where entree.idfour = fournisseur.idfour and date like '"+date+"%' and entree.etat = 0 HAVING identree = "+rech;
                listEntree = getEntree(rqt);

                if(listEntree.isEmpty())
                {
                    Message("ID <"+rech+"> n'existe pas !!");
                    inputEntree.setText("");
                    inputEntree.requestFocus();
                }
                else
                {
                    ActualiserEntree(listEntree);
                }
            }
            else
            {
                String rqt = "select entree.* , fournisseur.nom from entree, fournisseur " +
                "where entree.idfour = fournisseur.idfour and date like '"+date+"%' and entree.etat = 0 HAVING nom ="+rech;
                listEntree = getEntree(rqt);
                if(listEntree.isEmpty())
                {
                    Message("ID <"+rech+"> n'existe pas !!");
                    inputEntree.setText("");
                    inputEntree.requestFocus();
                }
                else
                {
                    ActualiserEntree(listEntree);
                }
            }
        }
    }

    @FXML
    void rechPieceClick(ActionEvent event)
    {

        String rech = inputpiece.getText().trim();
        if(rech.equals(""))
        {
            listPiece = getPieces("");
            ActualiserPiece(listPiece);
        }
        else
        {
            if(ChampsIdEstInt(rech)) //3RAFNA ELI HOA YFARKESS BEL ID
            {
                String rqt = "select * from piece where etat = 0 and idpiece = "+rech;
                listPiece = getPieces(rqt);

                if(listPiece.isEmpty())
                {
                    Message("ID <"+rech+"> n'existe pas !!");
                    inputpiece.setText("");
                    inputpiece.requestFocus();
                }
                else
                {
                    ActualiserPiece(listPiece);
                }
            }
            else
            {
                String rqt = "select * from piece where marque like '"+rech+"%'" +
                        "or modele like '"+rech+"%' or serie like '"+rech+"%' HAVING etat = 0";
                listPiece = getPieces(rqt);
                if(listPiece.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !");
                    inputpiece.setText("");
                    inputpiece.requestFocus();
                }
                else
                {
                    ActualiserPiece(listPiece);
                }
            }
        }

    }

    @FXML
    void viderClick(ActionEvent event)
    {
        piece.setText("");
        fournisseur.setText("");
        qte.setText("");
        date.setText("");
    }

    @FXML
    String dateClick(ActionEvent event)
    {
        try {
            return datePicker.getValue().format(dateFormatter);
        }
        catch (Exception e)
        {
            return "";
        }
    }

    //--------------------- set user name (passage entre les controlleurs) -------------------
    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }

    //------------------------- alert message ---------------------
    private void Message(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    //----------------------------id chiffres-----------------------------
    public boolean ChampsIdEstInt(String champsId)
    {
        boolean b = false ;
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

    //------------------------------- verification tous les champs ----------------------------
    public boolean ChampEstVide(String...champs)
    {
        boolean b = false;
        for(String ch : champs)
        {
            if(ch.length() == 0)
            {
                b = true;
                break;
            }
            else
            {
                b = false;
            }
        }
        return b;
    }

    //------------------ champ est double----------------
    public boolean ChampPrixEstDouble(String prix)
    {
        boolean b = false ;
        try
        {
            Double.parseDouble(prix);
            b = true;
        }
        catch(NumberFormatException e)
        {
            b = false;
        }
        return b;
    }

    //-------------------------- Champ qte ---------------------
    public boolean QteSupOuEgaleZero(String champsId)
    {
        int qte = Integer.parseInt(champsId);
        if(qte >= 0)
        {
            return true;
        }
        return false;
    }

}
