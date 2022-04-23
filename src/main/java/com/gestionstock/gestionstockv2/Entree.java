package com.gestionstock.gestionstockv2;

import Classes.Ent;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

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


    //-------------- ENTREE -----------------
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
    private Label mntTot;


    @FXML
    private TextField date;
    @FXML
    private ComboBox<String> fournisseur;
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
    PreparedStatement pst,pst1,pst2,pst3;
    ObservableList<Pie> listPiece;
    ObservableList<Ent> listEntree;
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


        listPiece = getPieces("");
        listEntree = getEntree("");
        ActualiserPiece(listPiece);
        ActualiserEntree(listEntree);
        setListeDeroulante();

        Timer();
        montantTotale();
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
                entrees = new Ent(rs.getInt("identree"),
                        rs.getInt("idpiece"),
                        rs.getString("nom"),
                        rs.getInt("qte"),
                        rs.getString("date"),
                        rs.getDouble("montant"));
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

    //------------------ AJOUT ENTREE ----------------------------
    @FXML
    void ajoutClick(ActionEvent event)
    {
        String qtee;
        qtee = qte.getText().trim();

        if(ChampEstVide(qtee))
        {
            Message("Verifiez Les Champs !!");
            qte.requestFocus();
        }
        else
        {
            if(!ChampsIdEstInt(qtee))
            {
                Message("quantité invalide");
                qte.setText("");
                qte.requestFocus();
            }
            else
            {
                if(!QteSupStrictementZero(qtee))
                {
                    Message("quantité invalide");
                    qte.setText("");
                    qte.requestFocus();
                }
                else
                {
                    try
                    {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Confirmation de l'entree");
                        alert.setContentText("êtes vous sûr d'ajouter la quantité <"+qtee+"> ?");

                        Optional<ButtonType> r = alert.showAndWait();
                        if (r.get() == ButtonType.OK){
                            pst1 = con.prepareStatement("select prixunitaire from piece where idpiece ="+piece.getText()+";");
                            ResultSet rs1 = pst1.executeQuery();

//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY-hh:mm:ss");
//                            LocalDateTime dateTime = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY"); // 16 4 pm 20/04/2022
                            LocalDate d = LocalDate.now(); //
                            String currentDate = d.format(formatter).concat("-").concat(String.valueOf(LocalTime.now())); //15

                            pst2 = con.prepareStatement("select idfour from fournisseur where nom ='"+fournisseur.getSelectionModel().getSelectedItem()+"';");
                            ResultSet rs2 = pst2.executeQuery();

                            pst3 = con.prepareStatement("select idemp from employe where nom ='"+user.getText()+"';");
                            ResultSet rs3 = pst3.executeQuery();



                            pst = con.prepareStatement("insert into entree (qte,date,montant,idpiece,idfour,idemp,etat,confirmEntree) values (?,?,?,?,?,?,?,?)");
                            while (rs1.next() && rs2.next() && rs3.next())
                            {
                                //Double montant1 = (parseInt(qtee) * Double.parseDouble(rs1.getString("prixunitaire")));

                                Double montant1 = parseInt(qtee) * rs1.getDouble("prixunitaire");

                                pst.setString(1, qtee);
                                pst.setString(2, currentDate);
                                pst.setDouble(3, Double.parseDouble(new DecimalFormat("####.###").format(montant1).replace(',' , '.')));
                                //pst.setDouble(3, montant1);
                                pst.setString(4, piece.getText());
                                pst.setString(5, rs2.getString("idfour"));
                                pst.setString(6, rs3.getString("idemp"));
                                pst.setString(7,"0");
                                pst.setString(8,"NoConfirm");
                                pst.executeUpdate();
                                Message("entrée Ajoutée !!");
                                listEntree = getEntree("");
                                ActualiserEntree(listEntree);
                                viderClick(event);
                                montantTotale();
                            }
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


    //---------------------------- TIMER CONFIRM ----------------------------------
    public void Timer()
    {
        LocalDateTime cureentDate  = LocalDateTime.now();
        LocalDateTime date2 ;


        try
        {
            pst = con.prepareStatement("select date from entree where confirmEntree ='NoConfirm'");
            ResultSet rs = pst.executeQuery();
            while(rs.next())
            {
                int [] tabdate = convert(rs.getString("date")) ;
                date2 = LocalDateTime.of(tabdate[0],tabdate[1],tabdate[2],tabdate[3],tabdate[4],tabdate[5]);
                date2 = date2.plusMinutes(1);

                if(cureentDate.isAfter(date2))
                {
                    pst = con.prepareStatement("update entree set confirmEntree = ? where date = '"+rs.getString("date")+"'");
                    pst.setString(1, "Timer");
                    pst.executeUpdate();

                    pst1 = con.prepareStatement("update piece, entree set piece.qte = piece.qte + entree.qte where piece.idpiece = entree.idpiece");
                    pst1.execute();
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //--------------- Convert String to local date time ----------------
    public int [] convert(String d)
    {
        String [] alldate = d.split("-") ;
        String date = alldate[0]; //date ["14/04/2022"] -> type string
        String time = alldate[1]; //time ["02:34:36"] -> type string
        String [] splitdate = date.split("/");
        String day = splitdate[0];
        String month = splitdate[1];
        String year = splitdate[2];
        String [] splitTime = time.split(":");
        String heure = splitTime[0];
        String min = splitTime[1];
        String sec = splitTime[2];
        Double sectoDouble = Double.parseDouble(sec);

        int [] tabdatetime = {
                Integer.parseInt(year),
                Integer.parseInt(month),
                Integer.parseInt(day),
                Integer.parseInt(heure),
                Integer.parseInt(min),
                sectoDouble.intValue()};

        return tabdatetime;

    }

    @FXML
    void backClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("interstock.fxml"));
            root = loader.load();
            InterStock interStock = loader.getController();
            interStock.PrintUserName(user.getText());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
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
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-------------- CALCULE MONTANT TOLTALE DES ENTRÉES ------------
    public void montantTotale()
    {
        connect();
        ResultSet rs;
        try {
            pst = con.prepareStatement("SELECT SUM(montant) FROM entree where etat = 0");
            rs= pst.executeQuery();

            while (rs.next())
            {
                mntTot.setText(Double.parseDouble(new DecimalFormat("#####.####").format(rs.getDouble("SUM(montant)"))
                        .replace(',' , '.'))+" DT");
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    //-------------------- LIGNE PIECE CLICK ---------------------
    @FXML
    void lignePieceClick(MouseEvent event)
    {
        try {
            Pie pie = tablePiece.getSelectionModel().getSelectedItem();
            piece.setText(String.valueOf(pie.getIdpiece()));
            fournisseur.getSelectionModel().select(indexInListFournisseur(String.valueOf(pie.getIdfour())));;
            qte.setText("0");
            date.setText(LocalDate.now().format(dateFormatter));
        }catch (NullPointerException e)
        {
            Message("Aucune ligne n'est sélectionnée");
        }

    }

    //----------------------- RECHERCHE ENTREE ---------------------
    @FXML
    void rechEntreeClick(ActionEvent event)
    {
        String rech = inputEntree.getText().trim();
        String date = dateClick(event);
        Timer();

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

    //------------------------ RECHERCHE PIECE -----------------------
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
        fournisseur.getSelectionModel().select(-1);
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
        boolean b;
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
        }
        return b;
    }

    //-------------------------- Champ qte ---------------------
    public boolean QteSupStrictementZero(String champsId)
    {
        int qte = Integer.parseInt(champsId);
        if(qte > 0)
        {
            return true;
        }
        return false;
    }


    public void setListeDeroulante()
    {
        try {
            pst = con.prepareStatement("select nom from fournisseur");
            ResultSet rs = pst.executeQuery();


            while (rs.next())
            {
                fournisseur.getItems().add(rs.getString("nom"));
            }


        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public int indexInListFournisseur(String idfour)
    {
        try
        {
            //bech yjib juste (idfour) piece mte3 nom fournisseur
            pst = con.prepareStatement("select nom from fournisseur where idfour = "+idfour);
            ResultSet rs1 = pst.executeQuery();
            String s ="";
            while (rs1.next())
            {
                s = rs1.getString("nom");
            }

            //bech yjib les pieces lkol w yee9if aand id
            ArrayList<String> idfo = new ArrayList<>();
            pst = con.prepareStatement("select nom from fournisseur");
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                idfo.add(rs.getString("nom"));
                if(rs.getString("nom").equals(s))
                {
                    return idfo.indexOf(s);// ----> yraja3 indice mte3ou fi wosset list !!!!!!!!!
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
