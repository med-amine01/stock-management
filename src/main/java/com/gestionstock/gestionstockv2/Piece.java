package com.gestionstock.gestionstockv2;

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
import java.util.ResourceBundle;

public class Piece implements Initializable {

    @FXML
    private TableView<Pie> table;
    @FXML
    private TableColumn<Pie, Integer> FOURcol;
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
    private Button ajoutBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button confBtn;

    @FXML
    private Button dec;


    @FXML
    private ComboBox<Integer> idfourp;

    @FXML
    private TextField inputpiece;

    @FXML
    private TextField marquepiece;

    @FXML
    private Button modBtn;

    @FXML
    private TextField modelepiece;

    @FXML
    private TextField prixunitairep;

    @FXML
    private TextField quantitepiece;

    @FXML
    private Button rechBtn;

    @FXML
    private TextField seriepiece;

    @FXML
    private Button suppBtn;

    @FXML
    private Label user;


    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst,pst1;

    //------------------ LOAD ON OPEN ---------------------------
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Actualiser();
        setListeDeroulante();
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
    public ObservableList<Pie> getPieces()
    {
        ObservableList<Pie> piceList = FXCollections.observableArrayList();
        connect();
        try
        {
            pst = con.prepareStatement("select idpiece,marque,modele,serie,qte,prixunitaire,idfour from piece where etat = 0");
            ResultSet rs = pst.executeQuery();
            Pie pieces;

            while (rs.next())
            {
                pieces = new Pie(rs.getInt("idpiece"), rs.getString("marque"), rs.getString("modele"),rs.getString("serie"),
                        rs.getInt("qte"),rs.getDouble("prixunitaire"),rs.getInt("idfour"));
                piceList.add(pieces);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return piceList;
    }
    //---------------------- chargement du tableau dans une liste (Search)-----------------------------
    public ObservableList<Pie> getPieces(String sqlSearch)
    {
        ObservableList<Pie> piceList = FXCollections.observableArrayList();
        connect();
        try
        {
            pst = con.prepareStatement(sqlSearch);
            ResultSet rs = pst.executeQuery();
            Pie pieces;

            while (rs.next())
            {
                pieces = new Pie(rs.getInt("idpiece"), rs.getString("marque"), rs.getString("modele"),rs.getString("serie"),
                        rs.getInt("qte"),rs.getDouble("prixunitaire"),rs.getInt("idfour"));
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
    public void Actualiser()
    {
        ObservableList<Pie> list = getPieces();
        IDcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("idpiece"));
        MARcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("marque"));
        MODcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("modele"));
        SERcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("serie"));
        QTEcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("qte"));
        PRIXcol.setCellValueFactory(new PropertyValueFactory<Pie,Double>("prix"));
        FOURcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("idfour"));

        table.setItems(list);
    }

    //------------------------ AJOUTER -------------------------------
    @FXML
    void ajoutClick(ActionEvent event)
    {

        String marque,modele,serie,qte,prixunitaire ;
        marque = marquepiece.getText().trim();
        modele = modelepiece.getText().trim();
        serie = seriepiece.getText().trim();
        qte = quantitepiece.getText().trim();
        prixunitaire = prixunitairep.getText().trim();


        if(ChampEstVide(marque, modele, serie, qte, prixunitaire))
        {
            Message("Verifiez Les Champs !!");
            marquepiece.requestFocus();
        }
        else
        {
            if(ChampsIdEstInt(qte)==false)
            {
                Message("quantité invalide");
                quantitepiece.setText("");
                quantitepiece.requestFocus();
            }
            else
            {
                if(QteSupOuEgaleZero(qte) == false)
                {
                    Message("quantité invalide");
                    quantitepiece.setText("");
                    quantitepiece.requestFocus();
                }
                else
                {
                    if(ChampPrixEstDouble(prixunitaire)==false)
                    {
                        Message("prix unitaire invalide !!");
                        prixunitairep.setText("");
                        prixunitairep.requestFocus();
                    }
                    else
                    {
                        if(PrixSupZero(prixunitaire) == false)
                        {
                            Message("prix unitaire invalide !!");
                            prixunitairep.setText("");
                            prixunitairep.requestFocus();
                        }
                        else
                        {
                            if(MMSEstUnique(marque, "marque") || MMSEstUnique(modele, "modele") || MMSEstUnique(serie, "serie"))
                            {
                                if( idfourp.getSelectionModel().getSelectedIndex() == -1)
                                {
                                    Message("Choisissez le fournisseur !");
                                    idfourp.requestFocus();
                                }
                                else
                                {
                                    try
                                    {
                                        pst = con.prepareStatement("insert into piece (marque,modele,serie,qte,prixunitaire,idfour,etat) values (?,?,?,?,?,?,?)");
                                        pst.setString(1, marque);
                                        pst.setString(2, modele);
                                        pst.setString(3, serie);
                                        pst.setString(4, qte);
                                        pst.setString(5, prixunitaire);
                                        pst.setString(6, idfourp.getSelectionModel().getSelectedItem().toString());
                                        pst.setString(7, "0");


                                        pst.executeUpdate();
                                        Message("pièce Ajoutée !!");
                                        Actualiser();
                                        marquepiece.setText("");
                                        modelepiece.setText("");
                                        seriepiece.setText("");
                                        quantitepiece.setText("");
                                        prixunitairep.setText("");
                                    }
                                    catch (SQLException e1)
                                    {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                            else
                            {
                                Message("Marque et Modele et Série Déjà EXIST !!");
                                marquepiece.setText("");
                                modelepiece.setText("");
                                seriepiece.setText("");
                                marquepiece.requestFocus();
                            }
                        }
                    }
                }
            }
        }
    }


    //---------------------- RECHERCHER --------------------
    @FXML
    void rechClick(ActionEvent event)
    {
        String rech = inputpiece.getText().trim();
        if(rech.equals(""))
        {
            Actualiser();
        }
        else
        {
            if(ChampsIdEstInt(rech)== true) //3RAFNA ELI HOA YFARKESS BEL ID
            {
                if(IdExist(rech)==false)
                {
                    Message("ID n'existe pas !!");
                    inputpiece.setText("");
                    inputpiece.requestFocus();
                }
                else
                {

                    String rqt = "select idpiece,marque,modele,serie,qte,prixunitaire,idfour from piece where etat = 0 and idpiece = "+rech;
                    ObservableList<Pie> list = getPieces(rqt);
                    IDcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("idpiece"));
                    MARcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("marque"));
                    MODcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("modele"));
                    SERcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("serie"));
                    QTEcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("qte"));
                    PRIXcol.setCellValueFactory(new PropertyValueFactory<Pie,Double>("prix"));
                    FOURcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("idfour"));
                    table.setItems(list);
                }
            }
            else
            {
                String rqt = "select idpiece,marque,modele,serie,qte,prixunitaire,idfour from piece where marque like '"+rech+"%'" +
                        "or modele like '"+rech+"%' or serie like '"+rech+"%' HAVING etat = 0";
                ObservableList<Pie> list = getPieces(rqt);
                IDcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("idpiece"));
                MARcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("marque"));
                MODcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("modele"));
                SERcol.setCellValueFactory(new PropertyValueFactory<Pie,String>("serie"));
                QTEcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("qte"));
                PRIXcol.setCellValueFactory(new PropertyValueFactory<Pie,Double>("prix"));
                FOURcol.setCellValueFactory(new PropertyValueFactory<Pie,Integer>("idfour"));
                table.setItems(list);
            }
        }
    }

    //-------------------------- MODIFIER --------------------
    @FXML
    void modClick(ActionEvent event)
    {
        String id=inputpiece.getText();
        if(ChampsIdEstInt(id)==false || IdExist(id)==false)
        {
            Message("Impossible De Modifier (id invalide)");
            inputpiece.setText("");
            inputpiece.requestFocus();
        }
        else
        {

            try
            {
                pst = con.prepareStatement("select marque,modele,serie,qte,prixunitaire,idfour from piece where idpiece = "+id+";");
                ResultSet rs = pst.executeQuery();

                while(rs.next())
                {
                    marquepiece.setText(rs.getString("marque"));
                    modelepiece.setText(rs.getString("modele"));
                    seriepiece.setText(rs.getString("serie"));
                    quantitepiece.setText(rs.getString("qte"));
                    prixunitairep.setText(rs.getString("prixunitaire"));
                    idfourp.getSelectionModel().select(indexInListFournisseur(id));


                }
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    //------------------ CONFIRMATION MODIFIER ----------------
    @FXML
    void confClick(ActionEvent event)
    {
        String marque,modele,serie,qte,prixunitaire, idconf ;
        idconf = inputpiece.getText().trim();
        marque = marquepiece.getText().trim();
        modele = modelepiece.getText().trim();
        serie = seriepiece.getText().trim();
        qte = quantitepiece.getText().trim();
        prixunitaire = prixunitairep.getText().trim();

        if(ChampEstVide(marque, modele, serie, qte, prixunitaire))
        {
            Message("Verifiez Les Champs !!");
            marquepiece.requestFocus();
        }
        else
        {
            if(ChampsIdEstInt(qte)==false)
            {
                Message("quantité invalide");
                quantitepiece.setText("");
                quantitepiece.requestFocus();
            }
            else
            {
                if(QteSupOuEgaleZero(qte) == false)
                {
                    Message("quantité invalide");
                    quantitepiece.setText("");
                    quantitepiece.requestFocus();
                }
                else
                {
                    if(ChampPrixEstDouble(prixunitaire)==false)
                    {
                        Message("prix unitaire invalide !!");
                        prixunitairep.setText("");
                        prixunitairep.requestFocus();
                    }
                    else
                    {
                        if(PrixSupZero(prixunitaire) == false)
                        {
                            Message("prix unitaire invalide !!");
                            prixunitairep.setText("");
                            prixunitairep.requestFocus();
                        }
                        else
                        {
                            if(MMSEstUnique(marque, "marque", idconf) || MMSEstUnique(modele, "modele",idconf) || MMSEstUnique(serie, "serie",idconf))
                            {
                                try
                                {
                                    pst = con.prepareStatement("update piece set marque = ? , modele = ? , serie = ? , qte = ? , prixunitaire = ? , idfour = ? where idpiece = ?");
                                    pst.setString(1, marque);
                                    pst.setString(2, modele);
                                    pst.setString(3, serie);
                                    pst.setString(4, qte);
                                    pst.setString(5, prixunitaire);
                                    pst.setString(6, idfourp.getSelectionModel().getSelectedItem().toString());
                                    pst.setString(7, idconf);
                                    pst.executeUpdate();
                                    Message("Pièce Modifié !!");
                                    Actualiser();
                                    inputpiece.setText("");
                                    marquepiece.setText("");
                                    modelepiece.setText("");
                                    seriepiece.setText("");
                                    quantitepiece.setText("");
                                    prixunitairep.setText("");
                                    inputpiece.requestFocus();
                                }
                                catch (SQLException e1)
                                {
                                    e1.printStackTrace();
                                }
                            }
                            else
                            {
                                Message("Marque et Modele et Série Déjà EXIST !!");
                                marquepiece.setText("");
                                modelepiece.setText("");
                                seriepiece.setText("");
                                marquepiece.requestFocus();
                            }
                        }
                    }
                }
            }
        }
    }

    //----------------- SUPPRIMER ---------------------------
    @FXML
    void suppClick(ActionEvent event)
    {
        String id=inputpiece.getText();
        if(ChampsIdEstInt(id)==false || IdExist(id)==false)
        {
            Message("Impossible De Supprimer (champ invalide ou ID n'existe pas)");
            inputpiece.setText("");
            inputpiece.requestFocus();
        }
        else
        {
            try
            {
                pst = con.prepareStatement("update piece set etat = ? where idpiece = ? ");
                pst.setString(1, "1");
                pst.setString(2, id);
                pst.executeUpdate();
                Message("Pièce Supprimée !!");
                Actualiser();
                inputpiece.setText("");
                inputpiece.requestFocus();
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
        }
    }


    //------------------- RETOUR interface -----------------------
    @FXML
    void backClick(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inter.fxml"));
            root = loader.load();
            Inter inter = loader.getController();
            inter.PrintAdminName(user.getText());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    //----------------- DECONNEXION ----------------------------
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


    //--------------------------id exist---------------------------------
    public boolean IdExist(String id)
    {
        ArrayList listid = new ArrayList();
        try
        {
            pst = con.prepareStatement("select idpiece from piece where etat = 0");
            ResultSet rs = pst.executeQuery();

            //empiler tabid avec les id à partir de la base de donnée
            while(rs.next())
            {
                listid.add(rs.getString("idpiece"));
            }


            int i = 0 ;
            boolean bool = false;
            while(i<listid.size() || bool == true)
            {
                if(id.equals(listid.get(i)))
                {
                    bool = true;
                    return true;
                }
                i++;
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
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

    //-------------------------- Champ prix ---------------------
    public boolean PrixSupZero(String champsId)
    {
        double prix = Double.parseDouble(champsId);
        if(prix > 0.0)
        {
            return true;
        }
        return false;
    }


    //------------------------ list déroulante ------------------
    public void setListeDeroulante()
    {
        try {
            pst = con.prepareStatement("select idfour from fournisseur");
            ResultSet rs = pst.executeQuery();


            while (rs.next())
            {
                idfourp.getItems().add(rs.getInt("idfour"));
            }


        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //------------------- Marque Serie Modele est UNIQUE Modification----------------------
    public boolean MMSEstUnique(String mms, String rqt, String id)
    {
        ArrayList<String> listMMS = new ArrayList();
        try
        {
            pst = con.prepareStatement("select "+rqt+" from piece where idpiece != '"+id+ "'");
            ResultSet rs = pst.executeQuery();


            while(rs.next())
            {
                listMMS.add(rs.getString(rqt));
            }


            int i = 0 ;
            boolean bool = false;
            while(i<listMMS.size() || bool == true)
            {
                if(mms.equals(listMMS.get(i))) // l9a mms fi base donc mahich unique
                {
                    bool = true;
                    return false;
                }
                i++;
            }

            listMMS.clear();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true; //donc unique eli jebeteha en para
    }
    //------------------- Marque Serie Modele est UNIQUE----------------------
    public boolean MMSEstUnique(String mms, String rqt)
    {
        ArrayList<String> listMMS = new ArrayList();
        try
        {
            pst = con.prepareStatement("select "+rqt+" from piece");
            ResultSet rs = pst.executeQuery();


            while(rs.next())
            {
                listMMS.add(rs.getString(rqt));
            }


            int i = 0 ;
            boolean bool = false;
            while(i<listMMS.size() || bool == true)
            {
                if(mms.equals(listMMS.get(i))) // l9a mms fi base donc mahich unique
                {
                    bool = true;
                    return false;
                }
                i++;
            }

            listMMS.clear();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true; //donc unique eli jebeteha en para
    }

    //---------------------- indexInList du fournisseur selon id piece---------------
    public int indexInListFournisseur(String id)
    {
        try
        {
            //bech yjib juste (idfour) piece mte3 id
            pst = con.prepareStatement("select idfour from piece where idpiece = "+id);
            ResultSet rs1 = pst.executeQuery();
            String s ="";
            while (rs1.next())
            {
                s = rs1.getString("idfour");
            }

            //bech yjib les pieces lkol w yee9if aand id
            ArrayList<String> idfo = new ArrayList<>();
            pst = con.prepareStatement("select idfour from piece");
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                idfo.add(rs.getString("idfour"));
                if(rs.getString("idfour").equals(s))
                {
                    return idfo.indexOf(s);
                }
            }

            idfo.clear();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }


}
