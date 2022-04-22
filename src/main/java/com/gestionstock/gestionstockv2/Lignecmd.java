package com.gestionstock.gestionstockv2;

import Classes.Lc;
import Classes.Pie;
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

import java.net.URL;
import java.security.interfaces.RSAKey;
import java.sql.*;
import java.text.DecimalFormat;
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

    ObservableList<Pie> listPiece;
    ObservableList<Lc> listLigneCmd;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listPiece = getPieces("");
        ActualiserPiece(listPiece);

        String sql = "select * from lignecmd where etat = 0 and idcmd = '"+getCmd()+"'";
        listLigneCmd = getLcs(sql);
        ActualiserLigneCmd(listLigneCmd);

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
                pst = con.prepareStatement("select * from lignecmd where etat = 0");
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
    void AjouterLCClick(ActionEvent event) {
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
                ResultSet rs ,rs1, rs2;
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
    void ModifierLCClick(ActionEvent event) {

    }

    @FXML
    void SuppLCClick(ActionEvent event) {

    }
    //---------------------------------------------------------------


    //------------- COMMANDES (passer / annuler)--------------
    @FXML
    void PasserCmdClick(ActionEvent event)
    {

    }

    @FXML
    void AnnulerCmdClick(ActionEvent event)
    {

    }
    //--------------------------------------------------------

    @FXML
    void backClick(ActionEvent event) {

    }

    @FXML
    void decClick(ActionEvent event) {

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
        }catch (NullPointerException e)
        {
            Message("Aucune ligne n'est sélectionnée");
        }
    }

    @FXML
    void rechLCClick(ActionEvent event) {

    }

    @FXML
    void rechPieceClick(ActionEvent event) {

    }

    @FXML
    void viderClick(ActionEvent event) {

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

}
