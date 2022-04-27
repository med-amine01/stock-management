package com.gestionstock.gestionstockv2;

import Classes.Lc;
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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class Lignecmd  implements Initializable {


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


    //--------- DONNÉE LC ----------------
    @FXML
    private TableView<Lc> tableLC;
    @FXML
    private TableColumn<Lc, Integer> IDCMDLCcol;
    @FXML
    private TableColumn<Lc, Integer> IDLCcol;
    @FXML
    private TableColumn<Lc, Integer> IDPLCcol;
    @FXML
    private TableColumn<Lc, Double> PRIXLCcol;
    @FXML
    private TableColumn<Lc, Integer> QTELCcol;



    @FXML
    private TextField inputLC;
    @FXML
    private TextField inputpiece;
    @FXML
    private Label mntTot;
    @FXML
    private TextField piece;
    @FXML
    private TextField qte;
    private int cmd;
    @FXML
    private Label user;

    Connection con;
    PreparedStatement pst,pst1,pst2,pst3;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String idlc;



    ObservableList<Pie> listPiece;
    ObservableList<Lc> listLigneCmd;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listPiece = getPieces("");
        ActualiserPiece(listPiece);

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
    public ObservableList<Lc> getLcs(String sqlSearch)
    {
        ObservableList<Lc> LcList = FXCollections.observableArrayList();
        connect();
        ResultSet rs;
        try
        {
            if(sqlSearch.equals(""))
            {
                pst = con.prepareStatement("select * from lignecmd where etat = 0 and idcmd = "+getCmd());
                rs= pst.executeQuery();
            }
            else
            {
                pst = con.prepareStatement(sqlSearch);
                rs = pst.executeQuery();
            }

            Lc lignescmd;

            while (rs.next())
            {
                lignescmd = new Lc(rs.getInt("idlc"),rs.getInt("idcmd"),
                        rs.getInt("idpiece"),rs.getInt("qtelc"),
                        rs.getDouble("prixlc"));
                LcList.add(lignescmd);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return LcList;
    }
    public void ActualiserLigneCmd(ObservableList<Lc> list)
    {
        IDLCcol.setCellValueFactory(new PropertyValueFactory<Lc,Integer>("idlc"));
        IDCMDLCcol.setCellValueFactory(new PropertyValueFactory<Lc,Integer>("idcmd"));
        IDPLCcol.setCellValueFactory(new PropertyValueFactory<Lc,Integer>("idpiece"));
        QTELCcol.setCellValueFactory(new PropertyValueFactory<Lc,Integer>("qtelc"));
        PRIXLCcol.setCellValueFactory(new PropertyValueFactory<Lc,Double>("prixlc"));

        tableLC.setItems(list);
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




    //------------------------ CRUD LIGNE CMD -------------------------
    @FXML
    void AjouterLCClick(ActionEvent event)
    {
        String idpiece = piece.getText();
        String qtee = qte.getText();

        if(ChampEstVide(idpiece,qtee))
        {
            Message("Verifiez Les Champs !!");
        }
        else
        {
            if(!ChampsEstInt(qtee) && !QteSupZero(qtee))
            {
                Message("quantité invalide");
                qte.setText("");
                qte.requestFocus();
            }
            else
            {
                ResultSet rs, rs1, rs2;
                try {
                    pst = con.prepareStatement("select qte from piece where idpiece ='"+idpiece+"'");
                    rs = pst.executeQuery();

                    pst2 = con.prepareStatement("select prixunitaire from piece where idpiece ='"+idpiece+"'");
                    rs2 = pst2.executeQuery();
                    while (rs.next() && rs2.next())
                    {
                        if(Integer.parseInt(qtee) <= rs.getInt("qte"))
                        {
                            double mntlc = Integer.parseInt(qtee) * Double.parseDouble(rs2.getString("prixunitaire"));
                            double insertmnt = Double.parseDouble(new DecimalFormat("#####.####").format(mntlc).replace(',' , '.'));

                            pst1 = con.prepareStatement("insert into lignecmd (idcmd, idpiece, qtelc, prixlc, etat) values (?,?,?,?,?)");
                            pst1.setString(1,String.valueOf(getCmd()));
                            pst1.setString(2,idpiece);
                            pst1.setString(3,qtee);
                            pst1.setString(4,String.valueOf(insertmnt));
                            pst1.setString(5,"0");
                            pst1.executeUpdate();
                            Message("La ligne a été ajoutée");
                            listLigneCmd = getLcs("");
                            ActualiserLigneCmd(listLigneCmd);
                            viderClick(event);

                            pst2 = con.prepareStatement("select sum(prixlc) from lignecmd where idcmd = "+getCmd());
                            rs1 = pst2.executeQuery();
                            while (rs1.next())
                            {
                                mntTot.setText(rs1.getString("sum(prixlc)"));
                            }
                        }
                        else
                        {
                            Message("La quantité demandé est supérieur au stock <"+rs.getInt("qte")+">");
                            qte.setText("");
                            qte.requestFocus();
                        }
                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
    @FXML
    void ModifierLCClick(ActionEvent event)
    {

        String idpiece = piece.getText();
        String qtee = qte.getText();
        try {
            if (idlc.isEmpty())
            {
                Message("veuillez selectionné la ligne à modifier ! ");
            }
            else
            {
                if(ChampEstVide(idpiece,qtee))
                {
                    Message("Verifiez Les Champs !!");
                }
                else
                {
                    if(!ChampsEstInt(qtee) && !QteSupZero(qtee))
                    {
                        Message("quantité invalide");
                        qte.setText("");
                        qte.requestFocus();
                    }
                    else
                    {
                        ResultSet rs, rs2;
                        try
                        {
                            pst = con.prepareStatement("select qte from piece where idpiece ='"+idpiece+"'");
                            rs = pst.executeQuery();

                            pst2 = con.prepareStatement("select prixunitaire from piece where idpiece ='"+idpiece+"'");
                            rs2 = pst2.executeQuery();
                            while (rs.next() && rs2.next())
                            {
                                if(Integer.parseInt(qtee) <= rs.getInt("qte"))
                                {
                                    double mntlc = Integer.parseInt(qtee) * Double.parseDouble(rs2.getString("prixunitaire"));
                                    double insertmnt = Double.parseDouble(new DecimalFormat("#####.####").format(mntlc).replace(',' , '.'));

                                    pst1 = con.prepareStatement("update lignecmd set idpiece = ? , qtelc = ? , prixlc = ?  where idlc = ?");
                                    pst1.setString(1,idpiece);
                                    pst1.setString(2,qtee);
                                    pst1.setString(3,String.valueOf(insertmnt));
                                    pst1.setString(4,idlc);
                                    pst1.executeUpdate();
                                    Message("La ligne a été modifiée");
                                    listLigneCmd = getLcs("");
                                    ActualiserLigneCmd(listLigneCmd);
                                    viderClick(event);
                                    idlc = "";
                                }
                                else
                                {
                                    Message("La quantité demandé est supérieur au stock <"+rs.getInt("qte")+">");
                                    qte.setText("");
                                    qte.requestFocus();
                                }
                            }
                        }
                        catch (SQLException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        catch (NullPointerException e)
        {
            Message("veuillez selectionné la ligne à modifier !");
        }

    }
    @FXML
    void SuppLCClick(ActionEvent event)
    {
        try
        {
            if(idlc.isEmpty())
            {
                Message("veuillez selectionné la ligne à supprimer");
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Supprimer la ligne de commande ? ");
                alert.setContentText("Vous allez SUPPRIMER la linge suivante  <"+idlc+"> !");

                Optional<ButtonType> r = alert.showAndWait();
                if (r.get() == ButtonType.OK)
                {
                    try
                    {
                        ResultSet rs1;
                        pst = con.prepareStatement("delete from lignecmd where idlc = "+ idlc);
                        pst.execute();
                        Message("La ligne a été Supprimé");
                        listLigneCmd = getLcs("");
                        ActualiserLigneCmd(listLigneCmd);
                        viderClick(event);
                        idlc = "";
                        pst2 = con.prepareStatement("select sum(prixlc) from lignecmd where idcmd = "+getCmd());
                        rs1 = pst2.executeQuery();
                        while (rs1.next())
                        {
                            mntTot.setText(rs1.getString("sum(prixlc)"));
                        }
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (NullPointerException e)
        {
            Message("veuillez selectionné la ligne à supprimer");
        }



    }
    @FXML
    void rechLCClick(ActionEvent event)
    {
        String rech = inputLC.getText().trim();
        if(rech.equals(""))
        {
            listLigneCmd = getLcs("");
            ActualiserLigneCmd(listLigneCmd);
        }
        else
        {
            String rqt = "select * from lignecmd where idlc like '"+rech+"%'" +
                    "or idpiece like '"+rech+"%' and idcmd = "+getCmd();
            listLigneCmd = getLcs(rqt);
            if(listLigneCmd.isEmpty())
            {
                Message("<"+rech+"> n'existe pas !");
                inputLC.setText("");
                inputLC.requestFocus();
            }
            else
            {
                ActualiserLigneCmd(listLigneCmd);
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
            if(ChampsEstInt(rech)) //3RAFNA ELI HOA YFARKESS BEL ID
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
    //---------------------------------------------------------------





    //------------- COMMANDES (passer / annuler)--------------
    @FXML
    void PasserCmdClick(ActionEvent event)
    {
        //BUG RECHERCHE + PASSER CMD
        //il faut initialisé le tableau
        listLigneCmd = getLcs("");
        ActualiserLigneCmd(listLigneCmd);


        double mnt = 0.0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Passer une commande ");
        alert.setContentText("Vous allez passer la commande suivante  <"+getCmd()+"> !");

        Optional<ButtonType> r = alert.showAndWait();
        if (r.get() == ButtonType.OK){
            try
            {
                for(int i=0; i<tableLC.getItems().size(); i++)
                {
                    mnt += tableLC.getItems().get(i).getPrixlc();
                    pst1 = con.prepareStatement("update piece set qte = qte - ? where idpiece = ?");
                    pst1.setString(1, String.valueOf(tableLC.getItems().get(i).getQtelc()));
                    pst1.setString(2, String.valueOf(tableLC.getItems().get(i).getIdpiece()));
                    pst1.executeUpdate();
                }

                pst2 = con.prepareStatement("update commande set etatcmd = ? , montantTot = ? where idcmd = ?");
                pst2.setString(1, "Passer");
                pst2.setString(2,String.valueOf(mnt));
                pst2.setString(3,String.valueOf(getCmd()));
                pst2.executeUpdate();
                Message("la commande <"+getCmd()+"> a été PASSER");
                listPiece = getPieces("");
                ActualiserPiece(listPiece);
                backClick(event);

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void AnnulerCmdClick(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Annuler une commande ");
        alert.setContentText("Vous allez ANNULER la commande suivante  <"+getCmd()+"> !");

        Optional<ButtonType> r = alert.showAndWait();
        if (r.get() == ButtonType.OK){
            try
            {

                pst2 = con.prepareStatement("update commande set etatcmd = ? where idcmd = ?");
                pst2.setString(1, "Annuler");
                pst2.setString(2,String.valueOf(getCmd()));
                pst2.executeUpdate();
                Message("la commande <"+getCmd()+"> a été ANNULER");
                backClick(event);

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    //--------------------------------------------------------


    void backClick(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("commande.fxml"));
            root = loader.load();
            Commande cmd = loader.getController();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            cmd.printSumPrixCmd();
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

    @FXML
    void lignePieceClick(MouseEvent event)
    {
        try {
            Pie pie = tablePiece.getSelectionModel().getSelectedItem();
            piece.setText(String.valueOf(pie.getIdpiece()));
        }catch (NullPointerException e)
        {
            Message("Aucune ligne n'est sélectionnée");
        }
    }

    @FXML
    void lignelcClick(MouseEvent event)
    {
        try {
            Lc lc = tableLC.getSelectionModel().getSelectedItem();
            piece.setText(String.valueOf(lc.getIdpiece()));
            qte.setText(String.valueOf(lc.getQtelc()));
            idlc = String.valueOf(lc.getIdlc());
        }catch (NullPointerException e)
        {
            idlc = "";
            Message("Aucune ligne n'est sélectionnée");
        }
    }

    @FXML
    void viderClick(ActionEvent event)
    {
        piece.setText("");
        qte.setText("");
    }

    //------------ get idcmd ----------------
    public void setCmd(int cmd)
    {
        this.cmd = cmd;
    }

    public int getCmd()
    {
        return this.cmd;
    }

    //------------------------- alert message ---------------------
    private void Message(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    //-------------------------- Champ qte ---------------------
    public boolean QteSupZero(String champsId)
    {
        int qte = Integer.parseInt(champsId);
        if(qte > 0)
        {
            return true;
        }
        return false;
    }

    //----------------------------est chiffres-----------------------------
    public boolean ChampsEstInt(String champsId)
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




}
