package com.gestionstock.gestionstockv2;

import Classes.Emp;
import Classes.Four;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Fournisseur implements Initializable {

    @FXML
    private TableView<Four> table;
    @FXML
    private TableColumn<Four, String> ADRESSEcol;
    @FXML
    private TableColumn<Four, Integer> IDcol;
    @FXML
    private TableColumn<Four, String> MAILcol;
    @FXML
    private TableColumn<Four, String> NOMcol;
    @FXML
    private TableColumn<Four, String> TELcol;

    @FXML
    private TextField addFour;


    @FXML
    private TextField inputFour;

    @FXML
    private TextField mailFour;

    @FXML
    private TextField nomFour;

    @FXML
    private TextField telFour;


    @FXML
    private Label user;

    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst,pst1;
    ObservableList<Four> list;


    //------------------ LOAD ON OPEN ---------------------------
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = getFournisseurs();
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
    public ObservableList<Four> getFournisseurs()
    {
        ObservableList<Four> fourList = FXCollections.observableArrayList();
        connect();
        try
        {
            pst = con.prepareStatement("select idfour,nom,adresse,tel,mail from fournisseur where etat = 0");
            ResultSet rs = pst.executeQuery();
            Four fournisseurs;

            while (rs.next())
            {
                fournisseurs = new Four(rs.getInt("idfour"), rs.getString("nom"), rs.getString("adresse"),rs.getString("tel"),
                        rs.getString("mail"));
                fourList.add(fournisseurs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return fourList;
    }
    //---------------------- chargement du tableau dans une liste (Search)-----------------------------
    public ObservableList<Four> getFournisseurs(String sqlSearch)
    {
        ObservableList<Four> fourList = FXCollections.observableArrayList();
        connect();
        try
        {
            pst = con.prepareStatement(sqlSearch);
            ResultSet rs = pst.executeQuery();
            Four fournisseurs;

            while (rs.next())
            {
                fournisseurs = new Four(rs.getInt("idfour"), rs.getString("nom"),rs.getString("adresse"),
                        rs.getString("tel"),rs.getString("mail"));
                fourList.add(fournisseurs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return fourList;
    }
    //---------------------------- ACTUALISER -----------------------------
    public void Actualiser(ObservableList<Four> list)
    {
        IDcol.setCellValueFactory(new PropertyValueFactory<Four,Integer>("idfour"));
        NOMcol.setCellValueFactory(new PropertyValueFactory<Four,String>("nomfour"));
        ADRESSEcol.setCellValueFactory(new PropertyValueFactory<Four,String>("addfour"));
        TELcol.setCellValueFactory(new PropertyValueFactory<Four,String>("telfour"));
        MAILcol.setCellValueFactory(new PropertyValueFactory<Four,String>("mailfour"));

        table.setItems(list);
    }

    //------------------------ AJOUTER -------------------------------
    @FXML
    void ajoutClick(ActionEvent event)
    {
        String nom,adresse,mail,numtel;
        nom = nomFour.getText().trim();
        adresse = addFour.getText().trim();
        mail = mailFour.getText().trim();
        numtel = telFour.getText().trim();

        if(ChampEstVide(nom,adresse,mail,numtel))
        {
            Message("Verifiez Les Champs !!");
            nomFour.requestFocus();
        }
        else
        {
            if(ChampTelEstInteger(numtel) == false || numtel.length() != 8)
            {
                Message( "Le Fournisseur Déjà Existe ou Numéro Téléphone invalide");
                telFour.setText("");
                telFour.requestFocus();
            }
            else
            {
                if(valideMail(mail) == false)
                {
                    Message("Le Fournisseur Déjà Existe ou mail invalide");
                    mailFour.setText("");
                    mailFour.requestFocus();
                }
                else
                {
                    try
                    {
                        pst = con.prepareStatement("insert into fournisseur (nom,adresse,tel,mail,etat) values (?,?,?,?,?)");
                        pst.setString(1, nom);
                        pst.setString(2, adresse);
                        pst.setString(3, numtel);
                        pst.setString(4, mail);
                        pst.setString(5, "0");
                        pst.executeUpdate();
                        Message("Fournisseur Ajouté !!");
                        list = getFournisseurs();
                        Actualiser(list);
                        nomFour.setText("");
                        addFour.setText("");
                        mailFour.setText("");
                        telFour.setText("");
                        nomFour.requestFocus();
                    }
                    catch (SQLException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
    //--------------------- SELECT ROW DISPLAY ON TEXT FIELDS -----------------
    public void ligneClick(javafx.scene.input.MouseEvent mouseEvent)
    {
        Four fournisseur = table.getSelectionModel().getSelectedItem();

        nomFour.setText(fournisseur.getNomfour());
        addFour.setText(fournisseur.getAddfour());
        telFour.setText(fournisseur.getTelfour());
        mailFour.setText(fournisseur.getMailfour());

    }
    //----------------------- CONFIRMER MODIFER -------------------------------
    @FXML
    void modClick(ActionEvent event) {
        Four fournisseur = table.getSelectionModel().getSelectedItem();
        String nom,adresse,tel,mail;
        nom = nomFour.getText().trim();
        adresse = addFour.getText().trim();
        tel = telFour.getText().trim();
        mail = mailFour.getText().trim();


        if(ChampEstVide(nom,adresse,tel,mail))
        {
            Message( "Verifiez Les Champs !!");
            nomFour.requestFocus();
        }
        else
        {
            if(ChampTelEstInteger(tel) == false)// || telEstUnique(tel,idconf) == false)
            {
                Message("Verifiez Le Numéro du Téléphone !!");
                telFour.setText("");
                telFour.requestFocus();
            }
            else
            {
                if (valideMail(mail) == false)//|| MailEstUnique(mail,idconf) == false  )
                {
                    Message("Fournisseur Déjà Existe ou  mail invalide");
                    mailFour.setText("");
                    mailFour.requestFocus();
                }
                else
                {
                    try
                    {
                        pst = con.prepareStatement("update fournisseur set nom = ?, adresse = ? , tel = ?, mail = ? where idfour = ? ");

                        pst.setString(1, nom);
                        pst.setString(2, adresse);
                        pst.setString(3, tel);
                        pst.setString(4, mail);
                        pst.setString(5, String.valueOf(fournisseur.getIdfour()));
                        pst.executeUpdate();
                        Message("Fournisseur Modifié !!");
                        viderClick(event);
                        list = getFournisseurs();
                        Actualiser(list);
                        inputFour.requestFocus();

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
    //----------------------------------- SUPPRIMER ---------------------------------------
    @FXML
    void suppClick(ActionEvent event)
    {
        Four fournisseur = table.getSelectionModel().getSelectedItem();
        String id ="";
        String res = "";
        try {
            id = String.valueOf(fournisseur.getIdfour());
        }catch (Exception e)
        {
            res = "Veuillez sélectionner un fournisseur !";
        }


        if(id.equals(""))
        {
            Message("Impossible De Supprimer");
            inputFour.setText("");
            inputFour.requestFocus();
        }
        else
        {
            try
            {
                pst1 = con.prepareStatement("select idfour, nom, adresse, tel, mail from fournisseur where idfour = "+id+";");
                ResultSet rs1 = pst1.executeQuery();

                while(rs1.next())
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Le fournisseur "+id+" sera supprimé");
                    alert.setContentText("Vous êtes sûr de vouloir supprimer ?");
                    Optional<ButtonType> r = alert.showAndWait();
                    if (r.get() == ButtonType.OK)
                    {
                        try
                        {
                            pst = con.prepareStatement("update fournisseur set etat = ? where idfour = ? ");
                            pst.setString(1, "1");
                            pst.setString(2, id);
                            pst.executeUpdate();
                            Message("Fournisseur Supprimé !!");
                            list = getFournisseurs();
                            Actualiser(list);
                            inputFour.setText("");
                            inputFour.requestFocus();
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

    //----------------------- Rechercher---------------------------
    @FXML
    void rechClick(ActionEvent event) {
        String rech = inputFour.getText().trim();
        if(rech.equals(""))
        {
            list = getFournisseurs();
            Actualiser(list);
        }
        else
        {
            if(ChampsIdEstInt(rech))
            {
                String rqt = "select idfour,nom,adresse,tel,mail from fournisseur where etat = 0 and idfour = "+rech;
                list = getFournisseurs(rqt);
                if(list.isEmpty())
                {
                    Message("ID <"+rech+"> n'existe pas !!");
                    inputFour.setText("");
                    inputFour.requestFocus();
                }
                else
                {
                    Actualiser(list);
                }
            }
            else
            {
                String rqt ="select idfour,nom,adresse,tel,mail from fournisseur where (nom like '"+rech+"%' or adresse like '"+rech+"%' or mail like '"+rech+"%') and etat=0";
                list = getFournisseurs(rqt);
                list = getFournisseurs(rqt);
                if(list.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !");
                    inputFour.setText("");
                    inputFour.requestFocus();
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
    @FXML
    void viderClick(ActionEvent event) {
        nomFour.setText("");
        addFour.setText("");
        mailFour.setText("");
        telFour.setText("");
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

}
