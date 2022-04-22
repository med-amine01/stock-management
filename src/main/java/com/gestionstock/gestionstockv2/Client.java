package com.gestionstock.gestionstockv2;

import Classes.Clnt;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class Client implements Initializable {
    @FXML
    private TableView<Clnt> table;

    @FXML
    private TableColumn<Clnt, String> ADDcol;

    @FXML
    private TableColumn<Clnt, Integer> IDcol;

    @FXML
    private TableColumn<Clnt, String> MAILcol;

    @FXML
    private TableColumn<Clnt, String> NOMcol;

    @FXML
    private TableColumn<Clnt, String> PRENOMcol;

    @FXML
    private TableColumn<Clnt, String> TELcol;

    @FXML
    private TextField addClient;
    @FXML
    private TextField telClient;
    @FXML
    private TextField inputClient;
    @FXML
    private TextField mailClient;
    @FXML
    private TextField nomClient;
    @FXML
    private TextField prenomClient;

    @FXML
    private Label user;
    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst,pst1;
    ObservableList<Clnt> list;


    //------------------ LOAD ON OPEN ---------------------------
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = getClients();
        Actualiser(list);
    }
    //-------------------- Connection à la base de donnée -----------------------
    public void connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/gestionstock", "root","");
            System.out.println("Connecté !!");

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
    public ObservableList<Clnt> getClients()
    {
        ObservableList<Clnt> clientList = FXCollections.observableArrayList();
        connect();
        try
        {
            pst = con.prepareStatement("select idclient,nom,prenom,tel,mail,adresse from client where etat = 0");
            ResultSet rs = pst.executeQuery();
            Clnt clients;

            while (rs.next())
            {
                clients = new Clnt(rs.getInt("idclient"), rs.getString("nom"),rs.getString("prenom"), rs.getString("tel"),rs.getString("mail"),
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
    //---------------------- chargement du tableau dans une liste (Search)-----------------------------
    public ObservableList<Clnt> getClients(String sqlSearch)
    {
        ObservableList<Clnt> clientList = FXCollections.observableArrayList();
        connect();
        try
        {
            pst = con.prepareStatement(sqlSearch);
            ResultSet rs = pst.executeQuery();
            Clnt clients;

            while (rs.next())
            {
                clients = new Clnt(rs.getInt("idclient"), rs.getString("nom"), rs.getString("prenom"),rs.getString("tel"),
                        rs.getString("mail"),rs.getString("adresse"));
                clientList.add(clients);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return clientList;
    }
    //---------------------------- ACTUALISER -----------------------------
    public void Actualiser(ObservableList<Clnt> list)
    {
        IDcol.setCellValueFactory(new PropertyValueFactory<Clnt,Integer>("idclient"));
        NOMcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("nom"));
        PRENOMcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("prenom"));
        TELcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("tel"));
        MAILcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("mail"));
        ADDcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("adresse"));

        table.setItems(list);
    }



    @FXML
    void ajoutClick(ActionEvent event) {
        String nom,prenom,adresse,mail,numtel;
        nom = nomClient.getText().trim();
        prenom = prenomClient.getText().trim();
        numtel = telClient.getText().trim();
        mail = mailClient.getText().trim();
        adresse = addClient.getText().trim();

        if(ChampEstVide(nom,prenom,adresse,numtel))
        {
            Message("Verifiez Les Champs !!");
            nomClient.requestFocus();
        }
        else
        {
            if(ChampTelEstInteger(numtel) == false || numtel.length() != 8)
            {
                Message( "Le client Déjà Existe ou Numéro Téléphone invalide");
                telClient.setText("");
                telClient.requestFocus();
            }
            else
            {
                if (!mail.equals(""))
                {
                    if(valideMail(mail) == false)
                    {
                        Message("Le client Déjà Existe ou mail invalide");
                        mailClient.setText("");
                        mailClient.requestFocus();
                    }
                    else
                    {
                        try
                        {
                            pst = con.prepareStatement("insert into client (nom,prenom,tel,mail,adresse,etat) values (?,?,?,?,?,?)");
                            pst.setString(1, nom);
                            pst.setString(2, prenom);
                            pst.setString(3, numtel);
                            pst.setString(4, mail);
                            pst.setString(5, adresse);
                            pst.setString(6, "0");
                            pst.executeUpdate();
                            Message("Client Ajouté !!");
                            list = getClients();
                            Actualiser(list);
                            nomClient.setText("");
                            prenomClient.setText("");
                            telClient.setText("");
                            mailClient.setText("");
                            addClient.setText("");
                            nomClient.requestFocus();
                        }
                        catch (SQLException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                }
                else
                {
                    try
                    {
                        pst = con.prepareStatement("insert into client (nom,prenom,tel,mail,adresse,etat) values (?,?,?,?,?,?)");
                        pst.setString(1, nom);
                        pst.setString(2, prenom);
                        pst.setString(3, numtel);
                        pst.setString(4, "-");
                        pst.setString(5, adresse);
                        pst.setString(6, "0");
                        pst.executeUpdate();
                        Message("Client Ajouté !!");
                        Actualiser(list);
                        nomClient.setText("");
                        prenomClient.setText("");
                        telClient.setText("");
                        mailClient.setText("");
                        addClient.setText("");
                        nomClient.requestFocus();
                    }
                    catch (SQLException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
    @FXML
    void ligneClick(MouseEvent event) {
        Clnt client = table.getSelectionModel().getSelectedItem();

        nomClient.setText(client.getNom());
        prenomClient.setText(client.getPrenom());
        telClient.setText(client.getTel());
        mailClient.setText(client.getMail());
        addClient.setText(client.getAdresse());
    }
    @FXML
    void modClick(ActionEvent event) {
        Clnt client = table.getSelectionModel().getSelectedItem();
        String nom,prenom,adresse,tel,mail;
        nom = nomClient.getText().trim();
        prenom = prenomClient.getText().trim();
        tel = telClient.getText().trim();
        mail = mailClient.getText().trim();
        adresse = addClient.getText().trim();



        if(ChampEstVide(nom,prenom,adresse,tel,mail))
        {
            Message( "Verifiez Les Champs !!");
            nomClient.requestFocus();
        }
        else
        {
            if(ChampTelEstInteger(tel) == false)// || telEstUnique(tel,idconf) == false)
            {
                Message("Verifiez Le Numéro du Téléphone !!");
                telClient.setText("");
                telClient.requestFocus();
            }
            else
            {
                if (valideMail(mail) == false)//|| MailEstUnique(mail,idconf) == false  )
                {
                    Message("client Déjà Existe ou  mail invalide");
                    mailClient.setText("");
                    mailClient.requestFocus();
                }
                else
                {
                    try
                    {
                        pst = con.prepareStatement("update client set nom = ?, prenom = ?,tel = ? , mail = ?, adresse = ? where idclient = ? ");

                        pst.setString(1, nom);
                        pst.setString(2, prenom);
                        pst.setString(3, tel);
                        pst.setString(4, mail);
                        pst.setString(5, adresse);
                        pst.setString(6, String.valueOf(client.getIdclient()));
                        pst.executeUpdate();
                        Message("client Modifié !!");
                        viderClick(event);
                        list = getClients();
                        Actualiser(list);
                        inputClient.requestFocus();

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

    }
    @FXML
    void suppClick(ActionEvent event) {
        Clnt client = table.getSelectionModel().getSelectedItem();
        String id ="";
        String res = "";
        try {
            id = String.valueOf(client.getIdclient());
        }catch (Exception e)
        {
            res = "Veuillez sélectionner un fournisseur !";
        }


        if(id.equals(""))
        {
            Message("Impossible De Supprimer");
            inputClient.setText("");
            inputClient.requestFocus();
        }
        else
        {
            try
            {
                pst1 = con.prepareStatement("select idclient, nom,prenom, tel, mail, adresse from client where idclient = "+id+";");
                ResultSet rs1 = pst1.executeQuery();

                while(rs1.next())
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Le client "+id+" sera supprimé");
                    alert.setContentText("Vous êtes sûr de vouloir supprimer ?");
                    Optional<ButtonType> r = alert.showAndWait();
                    if (r.get() == ButtonType.OK)
                    {
                        try
                        {
                            pst = con.prepareStatement("update client set etat = ? where idclient = ? ");
                            pst.setString(1, "1");
                            pst.setString(2, id);
                            pst.executeUpdate();
                            Message("client Supprimé !!");
                            list = getClients();
                            Actualiser(list);
                            inputClient.setText("");
                            inputClient.requestFocus();
                        }
                        catch (SQLException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void rechClick(ActionEvent event) {
        String rech = inputClient.getText().trim();
        if(rech.equals(""))
        {
            list = getClients();
            Actualiser(list);
        }
        else
        {
            if(ChampsIdEstInt(rech))
            {
                String rqt = "select idclient,nom,prenom,tel,mail,adresse from client where etat = 0 and idclient = "+rech;
                list = getClients(rqt);
                if(list.isEmpty())
                {
                    Message("ID <"+rech+"> n'existe pas !!");
                    inputClient.setText("");
                    inputClient.requestFocus();
                }
                else
                {
                    Actualiser(list);
                }
            }
            else
            {
                String rqt ="select idclient,nom,prenom,tel,mail,adresse from client where (nom like '"+rech+"%' or prenom like '"+rech+"%' or mail like '"+rech+"%' or adresse like '"+rech+"%') and etat=0";
                list = getClients(rqt);
                list = getClients(rqt);
                if(list.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !");
                    inputClient.setText("");
                    inputClient.requestFocus();
                }
                else
                {
                    Actualiser(list);
                }
            }
        }
    }




    @FXML
    void viderClick(ActionEvent event) {
        nomClient.setText("");
        prenomClient.setText("");
        telClient.setText("");
        mailClient.setText("");
        addClient.setText("");
    }
    @FXML
    void backClick(ActionEvent event) {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inter.fxml"));
            root = loader.load();
            Inter inter = loader.getController();
            inter.PrintAdminName(user.getText());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    void decClick(ActionEvent event) {
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
    //------------------------- alert message ---------------------
    private void Message(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
    //--------------------- Est ENTIER -----------------------------------
    public boolean ChampTelEstInteger(String tel)
    {
        boolean b = false ;
        try
        {
            Integer.parseInt(tel);
            b = true;
        }
        catch(NumberFormatException e)
        {
            b = false;
        }
        return b;

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
    //----------------------- mail form ---------------------------
    public boolean valideMail(String inputMail)
    {
        if(inputMail.indexOf('@') != -1 && inputMail.indexOf('.') != -1)
        {
            int indiceAlt= inputMail.indexOf('@');
            String login = inputMail.substring(0,indiceAlt);
            String domain = inputMail.substring(indiceAlt,inputMail.lastIndexOf('.'));
            String ext = inputMail.substring(inputMail.lastIndexOf('.')+1);

            if(login.length()<2)
            {
                return false;
            }

            if(domain.length() <1)
            {
                return false;
            }

            if(ext.length() <2 || ext.length() > 3)
            {
                return false ;
            }
        }
        else
        {
            return false;
        }
        return true ;
    }
    //--------------------- set user name (passage entre les controlleurs) -------------------
    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }

}
