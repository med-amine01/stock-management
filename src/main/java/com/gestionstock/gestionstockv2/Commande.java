package com.gestionstock.gestionstockv2;

import Classes.Clnt;
import Classes.Cmd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Commande implements Initializable {


    //-------------- DONNÉES COMMANDE -----------------
    @FXML
    private TableView<Cmd> tableCMD;
    @FXML
    private TableColumn<Cmd, Integer> IDCMDcol;
    @FXML
    private TableColumn<Cmd, Double> MONTcol;
    @FXML
    private TableColumn<Cmd, Integer> CLIENTcol;
    @FXML
    private TableColumn<Cmd, String> DATEcol;
    @FXML
    private TableColumn<Cmd, Integer> EMPcol;
    @FXML
    private TableColumn<Cmd, String> ETATcol;



    //------------------ DONNÉES CLIENT ----------------
    @FXML
    private TableView<Clnt> tableClient;
    @FXML
    private TableColumn<Clnt, Integer> IDCLIENTcol;
    @FXML
    private TableColumn<Clnt, String> ADDcol;
    @FXML
    private TableColumn<Clnt, String> NOMcol;
    @FXML
    private TableColumn<Clnt, String> PRENOMcol;
    @FXML
    private TableColumn<Clnt, String> Telcol;
    @FXML
    private TableColumn<Clnt, String> mailCol;



    //-----------------------------------
    @FXML
    private CheckBox cbCMD;
    @FXML
    private ComboBox<String> client;
    @FXML
    private TextField date;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField inputCMD;
    @FXML
    private TextField inputClient;
    @FXML
    private Label mntTot;
    @FXML
    private Label user;

    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst,pst1,pst2,pst4;
    ObservableList<Clnt> listClient;
    ObservableList<Cmd> listCmd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listClient = getClients("");
        ActualiserClient(listClient);

        listCmd = getCommandes("");
        ActualiserCommande(listCmd);
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
    public ObservableList<Clnt> getClients(String sqlSearch)
    {
        ObservableList<Clnt> clientList = FXCollections.observableArrayList();
        connect();
        ResultSet rs;
        try
        {
            if(sqlSearch.equals(""))
            {
                pst = con.prepareStatement("select * from client where etat = 0");
                rs= pst.executeQuery();
            }
            else
            {
                pst = con.prepareStatement(sqlSearch);
                rs = pst.executeQuery();
            }

            Clnt clients;

            while (rs.next())
            {
                clients = new Clnt(rs.getInt("idclient"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("tel"),
                        rs.getString("mail"),
                        rs.getString("adresse"));
                clientList.add(clients);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return clientList;
    }

    //---------------------- chargement du tableau dans une liste-----------------------------
    public ObservableList<Cmd> getCommandes(String sqlSearch)
    {
        ObservableList<Cmd> cmdList = FXCollections.observableArrayList();
        connect();
        ResultSet rs;
        try
        {
            if(sqlSearch.equals(""))
            {
                pst = con.prepareStatement("select * from commande where etat = 0");
                rs= pst.executeQuery();
            }
            else
            {
                pst = con.prepareStatement(sqlSearch);
                rs = pst.executeQuery();
            }

            Cmd cmds;

            while (rs.next())
            {
                cmds = new Cmd(rs.getInt("idcmd"),
                        rs.getInt("idemp"),
                        rs.getInt("idclient"),
                        rs.getString("datecmd"),
                        rs.getDouble("montantTot"),
                        rs.getString("etatcmd"));
                cmdList.add(cmds);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return cmdList;
    }


    public void ActualiserClient(ObservableList<Clnt> list)
    {
        IDCLIENTcol.setCellValueFactory(new PropertyValueFactory<Clnt,Integer>("idclient"));
        NOMcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("nom"));
        PRENOMcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("prenom"));
        Telcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("tel"));
        mailCol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("mail"));
        ADDcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("adresse"));

        tableClient.setItems(list);
    }

    public void ActualiserCommande(ObservableList<Cmd> list)
    {
        IDCMDcol.setCellValueFactory(new PropertyValueFactory<Cmd,Integer>("idcmd"));
        CLIENTcol.setCellValueFactory(new PropertyValueFactory<Cmd,Integer>("idclient"));
        EMPcol.setCellValueFactory(new PropertyValueFactory<Cmd,Integer>("idemp"));
        DATEcol.setCellValueFactory(new PropertyValueFactory<Cmd,String>("datecmd"));
        MONTcol.setCellValueFactory(new PropertyValueFactory<Cmd,Double>("montant"));
        ETATcol.setCellValueFactory(new PropertyValueFactory<Cmd,String>("etatcmd"));

        tableCMD.setItems(list);
    }



    @FXML
    void AjouterCmdClick(ActionEvent event) {

    }

    @FXML
    void ModifierCmdClick(ActionEvent event) {

    }

    @FXML
    void ModifierLcClick(ActionEvent event) {

    }

    @FXML
    void SuppCmdClick(ActionEvent event) {

    }

    @FXML
    void backClick(ActionEvent event) {

    }

    @FXML
    void cmdClick(MouseEvent event) {

    }

    @FXML
    void dateClick(ActionEvent event) {

    }

    @FXML
    void decClick(ActionEvent event) {

    }

    @FXML
    void lignePieceClick(MouseEvent event) {

    }

    @FXML
    void rechClientClick(ActionEvent event) {

    }

    @FXML
    void rechCmdClick(ActionEvent event) {

    }

    @FXML
    void viderClick(ActionEvent event) {

    }


    //--------------------- set user name (passage entre les controlleurs) -------------------
    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }

}
