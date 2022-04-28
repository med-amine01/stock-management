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
    private TableColumn<Clnt, String> IDcol;

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
    private TextField CIN;


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
        list = getClients("");
        Actualiser(list);
    }
    //-------------------- Connection à la base de donnée -----------------------
    public void connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/gestionstock", "root","");

        }
        catch (ClassNotFoundException | SQLException ex)
        {
            ex.printStackTrace();

        }
    }


    //---------------------- chargement du tableau dans une liste (Search)-----------------------------
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
                clients = new Clnt(rs.getString("cinClient"), rs.getString("nom"),
                        rs.getString("prenom"),rs.getString("tel"),
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
        IDcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("cinClient"));
        NOMcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("nom"));
        PRENOMcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("prenom"));
        TELcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("tel"));
        MAILcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("mail"));
        ADDcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("adresse"));

        table.setItems(list);
    }




    //------------------------ CRUD CLIENT ----------------------------
    @FXML
    void ajoutClick(ActionEvent event)
    {
        String cin,nom,prenom,adresse,mail,numtel;
        cin = CIN.getText().trim();
        nom = nomClient.getText().trim();
        prenom = prenomClient.getText().trim();
        numtel = telClient.getText().trim();
        mail = mailClient.getText().trim();
        adresse = addClient.getText().trim();

        if(ChampEstVide(cin,nom,prenom,adresse,numtel))
        {
            Message("Verifiez Les Champs !!");
            nomClient.requestFocus();
        }
        else
        {
            if(cin.length() != 8 || !ChampTelEstInteger(cin))
            {
                Message( "Numéro CIN invalide");
                CIN.setText("");
                CIN.requestFocus();
            }
            else
            {
                if(!ChampTelEstInteger(numtel) || numtel.length() != 8)
                {
                    Message( "Numéro Téléphone invalide");
                    telClient.setText("");
                    telClient.requestFocus();
                }
                else
                {
                        try
                        {
                            pst = con.prepareStatement("insert into client (cinClient,nom,prenom,tel,mail,adresse,etat) values (?,?,?,?,?,?,?)");
                            pst.setString(1, cin);
                            pst.setString(2, nom);
                            pst.setString(3, prenom);
                            pst.setString(4, numtel);
                            if (mail.equals(""))
                            {
                                pst.setString(5, "-");
                            }
                            else
                            {
                                if(!valideMail(mail))
                                {
                                    Message("mail invalide");
                                    mailClient.setText("");
                                    mailClient.requestFocus();
                                }
                                else
                                {
                                    pst.setString(5, mail.replace(" ",""));
                                }
                            }
                            pst.setString(6, adresse);
                            pst.setString(7, "0");
                            pst.executeUpdate();
                            Message("Client Ajouté !!");
                            list = getClients("");
                            Actualiser(list);
                            viderClick(event);
                        }
                        catch (SQLIntegrityConstraintViolationException e)
                        {
                            try
                            {
                                ResultSet rs22;
                                pst = con.prepareStatement("select etat from client where cinClient = "+cin);
                                rs22 = pst.executeQuery();
                                while (rs22.next())
                                {
                                    if(rs22.getString("etat").equals("1"))
                                    {
                                        pst1 = con.prepareStatement("update client set etat = ? where cinClient = ? ");

                                        pst1.setString(1, "0");
                                        pst1.setString(2, cin);

                                        pst1.executeUpdate();
                                        Message("Client ajouté (le client restoré) !!");
                                        viderClick(event);
                                        list = getClients("");
                                        Actualiser(list);
                                        inputClient.requestFocus();
                                    }
                                    else
                                    {
                                        Message("Le client déjà existe !");
                                        viderClick(event);
                                    }
                                }
                            }
                            catch (SQLException e1)
                            {
                                e1.printStackTrace();
                            }

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
    void modClick(ActionEvent event) {
        Clnt client = table.getSelectionModel().getSelectedItem();
        String cin,nom,prenom,adresse,tel,mail;
        cin = CIN.getText().trim();
        nom = nomClient.getText().trim();
        prenom = prenomClient.getText().trim();
        tel = telClient.getText().trim();
        mail = mailClient.getText().trim();
        adresse = addClient.getText().trim();



        if(ChampEstVide(nom,prenom,adresse,tel))
        {
            Message( "Verifiez Les Champs !!");
            nomClient.requestFocus();
        }
        else
        {
            if(cin.length() != 8 || !ChampTelEstInteger(cin))
            {
                Message( "Numéro CIN invalide");
                CIN.setText("");
                CIN.requestFocus();
            }
            else
            {
                if (!ChampTelEstInteger(tel) || tel.length() != 8)
                {
                    Message("Verifiez Le Numéro du Téléphone !!");
                    telClient.setText("");
                    telClient.requestFocus();
                }
                else
                {
                    try {

                        pst = con.prepareStatement("update client set cinClient = ? , nom = ?, prenom = ?,tel = ? , mail = ?, adresse = ? where cinClient = ? ");

                        pst.setString(1, cin);
                        pst.setString(2, nom);
                        pst.setString(3, prenom);
                        pst.setString(4, tel);
                        if (mail.equals(""))
                        {
                            pst.setString(5, "-");
                            pst.setString(6, adresse);
                            pst.setString(7, client.getCinClient()); // cin 9dima avant modification
                            pst.executeUpdate();
                            Message("client Modifié !!");
                            viderClick(event);
                            list = getClients("");
                            Actualiser(list);
                            inputClient.requestFocus();
                        }
                        else
                        {
                            if (!valideMail(mail))
                            {
                                Message("mail invalide");
                                mailClient.setText("");
                                mailClient.requestFocus();
                            }
                            else
                            {
                                pst.setString(5, mail.replace(" ",""));
                                pst.setString(6, adresse);
                                pst.setString(7, client.getCinClient()); // cin 9dima avant modification
                                pst.executeUpdate();
                                Message("client Modifié !!");
                                viderClick(event);
                                list = getClients("");
                                Actualiser(list);
                                inputClient.requestFocus();
                            }
                        }
                    }
                    catch (SQLIntegrityConstraintViolationException e)
                    {
                        Message("client déjà existe");
                    }
                    catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

    }
    @FXML
    void suppClick(ActionEvent event) {
        Clnt client = table.getSelectionModel().getSelectedItem();


        if(client.getCinClient().equals(""))
        {
            Message("Impossible De Supprimer");
            inputClient.setText("");
            inputClient.requestFocus();
        }
        else
        {
            try
            {
                pst1 = con.prepareStatement("select Cinclient, nom,prenom, tel, mail, adresse from client where cinClient = "+client.getCinClient()+";");
                ResultSet rs1 = pst1.executeQuery();

                while(rs1.next())
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Le client "+client.getCinClient()+" sera supprimé");
                    alert.setContentText("Vous êtes sûr de vouloir supprimer ?");
                    Optional<ButtonType> r = alert.showAndWait();
                    if (r.get() == ButtonType.OK)
                    {
                        try
                        {
                            pst = con.prepareStatement("update client set etat = ? where cinClient = ? ");
                            pst.setString(1, "1");
                            pst.setString(2, client.getCinClient());
                            pst.executeUpdate();
                            Message("client Supprimé !!");
                            list = getClients("");
                            Actualiser(list);
                            inputClient.setText("");
                            inputClient.requestFocus();
                            viderClick(event);
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
            list = getClients("");
            Actualiser(list);
        }
        else
        {
            if(ChampsIdEstInt(rech))
            {
                String rqt = "select cinClient,nom,prenom,tel,mail,adresse from client where etat = 0 and cinClient = "+rech;
                list = getClients(rqt);
                if(list.isEmpty())
                {
                    Message("CIN <"+rech+"> n'existe pas !!");
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
                String rqt ="select cinClient,nom,prenom,tel,mail,adresse from client where (nom like '"+rech+"%' or prenom like '"+rech+"%' or mail like '"+rech+"%' or adresse like '"+rech+"%') and etat=0";
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
    //-------------------------------------------------------------------




    
    @FXML
    void ligneClick(MouseEvent event) {

        try {
            Clnt client = table.getSelectionModel().getSelectedItem();
            CIN.setText(client.getCinClient());
            nomClient.setText(client.getNom());
            prenomClient.setText(client.getPrenom());
            telClient.setText(client.getTel());
            mailClient.setText(client.getMail());
            addClient.setText(client.getAdresse());
        }
        catch (NullPointerException e)
        {
            Message("Veuillez sélectionner un client !");
        }

    }
    @FXML
    void viderClick(ActionEvent event) {
        CIN.setText("");
        nomClient.setText("");
        prenomClient.setText("");
        telClient.setText("");
        mailClient.setText("");
        addClient.setText("");
        CIN.requestFocus();
    }
    @FXML
    void backClick(ActionEvent event) {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("intervendeur.fxml"));
            root = loader.load();
            InterVendeur interVendeur = loader.getController();
            interVendeur.PrintUserName(user.getText());
            interVendeur.countNb();
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
        boolean b  ;
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
