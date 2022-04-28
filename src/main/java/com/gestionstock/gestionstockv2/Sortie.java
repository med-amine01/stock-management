package com.gestionstock.gestionstockv2;

import Classes.Clnt;
import Classes.Cmd;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Sortie implements Initializable {
    //-------------- DONNÉES COMMANDE -----------------
    @FXML
    private TableView<Cmd> tableCMD;
    @FXML
    private TableColumn<Cmd, Integer> IDCMDcol;
    @FXML
    private TableColumn<Cmd, Double> MONTcol;
    @FXML
    private TableColumn<Cmd, String> DATEcol;
    @FXML
    private TableColumn<Cmd, Integer> EMPcol;
    @FXML
    private TableColumn<Cmd, String> ETATcol;




    @FXML
    private CheckBox cbCMD;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField inputCMD;
    @FXML
    private Label mntTot;
    @FXML
    private Label user;

    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst;
    ObservableList<Cmd> listCmd;

    private static String idemp;


    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        datePicker.setPromptText(LocalDate.now().format(dateFormatter));
        datePicker.setConverter(new StringConverter<>() {

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
                        rs.getString("cinClient"),
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

    public void ActualiserCommande(ObservableList<Cmd> list)
    {
        IDCMDcol.setCellValueFactory(new PropertyValueFactory<Cmd,Integer>("idcmd"));
        EMPcol.setCellValueFactory(new PropertyValueFactory<Cmd,Integer>("idemp"));
        DATEcol.setCellValueFactory(new PropertyValueFactory<Cmd,String>("datecmd"));
        MONTcol.setCellValueFactory(new PropertyValueFactory<Cmd,Double>("montant"));
        ETATcol.setCellValueFactory(new PropertyValueFactory<Cmd,String>("etatcmd"));

        tableCMD.setItems(list);
    }


    @FXML
    void rechSortieClick(ActionEvent event) {
        String rech = inputCMD.getText().trim();
        String date = dateClick(event);

        if(rech.equals(""))
        {
            if(date.equals(""))
            {
                if(cbCMD.isSelected())
                {
                    String rqt = "select commande.* , employe.nom from commande,employe where commande.idemp = employe.idemp and commande.etatcmd ='Passer'" +
                            "HAVING datecmd like '"+LocalDate.now().format(dateFormatter)+"%' ";
                    listCmd = getCommandes(rqt);
                    ActualiserCommande(listCmd);
                    datePicker.setValue(null);
                }
                else
                {
                    String rqt = "select commande.* , employe.nom from commande,employe where commande.idemp = employe.idemp " +
                            "HAVING datecmd like '"+LocalDate.now().format(dateFormatter)+"%' ";
                    listCmd = getCommandes(rqt);
                    ActualiserCommande(listCmd);
                    datePicker.setValue(null);
                }
            }
            else
            {
                String rqt = "select commande.* , employe.nom from commande,employe where commande.idemp = employe.idemp and commande.etat = 0 " +
                        "HAVING datecmd like '"+date+"%'";
                listCmd = getCommandes(rqt);
                ActualiserCommande(listCmd);
                datePicker.setValue(null);
            }

        }
        else
        {
            if(ChampsIdEstInt(rech)) //3RAFNA ELI HOA YFARKESS BEL ID
            {
                String rqt = "select commande.* , employe.nom from commande,employe where commande.idemp = employe.idemp " +
                        "HAVING datecmd like '"+date+"%' and ( cinClient like '"+rech+"%' or idcmd like '"+rech+"%')";
                listCmd = getCommandes(rqt);

                if(listCmd.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !!");
                    inputCMD.setText("");
                    inputCMD.requestFocus();
                }
                else
                {
                    ActualiserCommande(listCmd);
                    datePicker.setValue(null);
                }
            }
            else
            {
                String rqt = "select commande.* , client.nom, employe.nom from commande, client, employe " +
                        "where commande.cinClient = client.cinClient and date like '"+date+"%' HAVING client.nom like = '"+rech+"%'";
                listCmd = getCommandes(rqt);
                if(listCmd.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !!");
                    inputCMD.setText("");
                    inputCMD.requestFocus();
                }
                else
                {
                    ActualiserCommande(listCmd);
                    datePicker.setValue(null);
                }
            }
        }
    }
    @FXML
    void backClick(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inter.fxml"));
            root = loader.load();
            Inter inter = loader.getController();
            inter.PrintAdminName(user.getText());
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
        boolean b  ;
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

    public static String getIdemp() {
        return idemp;
    }

    public void setIdemp(String idemp) {
        this.idemp = idemp;
    }

    public void printSumPrixCmd()
    {
        try
        {
            double mnt = 0.0;
            ResultSet rs ;
            pst = con.prepareStatement("select sum(montantTot) from commande where datecmd like '"+LocalDate.now().format(dateFormatter)+"%' group by idemp");
            rs = pst.executeQuery();
            while (rs.next())
            {
                mnt = rs.getDouble("sum(montantTot)");
            }

            mntTot.setText(Double.parseDouble(new DecimalFormat("#####.####").format(mnt).replace(',' , '.'))+" DT");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
