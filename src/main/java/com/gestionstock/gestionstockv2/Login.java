package com.gestionstock.gestionstockv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static java.sql.DriverManager.getConnection;

public class Login {
    Connection con;
    PreparedStatement pst;

    @FXML
    private TextField idLog;
    @FXML
    private PasswordField pwdLog;

    private Stage stage;
    private Scene scene;
    private Parent root,root2;


    @FXML
    void authClick(ActionEvent event) throws IOException {
        connect();
        Authentifier(event);

    }

    //-----------------Connection to database----------------------
    public void connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = getConnection("jdbc:mysql://localhost/gestionstock", "root","");

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

    //------------------------- auth --------------------------
    public void Authentifier(ActionEvent event) throws IOException
    {
        String id = idLog.getText().trim();
        String pwd = pwdLog.getText().trim();
        if(ChampEstVide(id, pwd))
        {
            Message("Verifiez vos champs");
        }
        else
        {
            if(!ChampsIdEstInt(id) || !IdExist(id))
            {
                Message("ID incorrect");
                idLog.setText("");
                idLog.requestFocus();
            }
            else
            {
                try
                {
                    pst = con.prepareStatement("select idemp, password, post , nom from employe where idemp = "+id+";");
                    ResultSet rs = pst.executeQuery();

                    while(rs.next()) //parcours sur les ids et passwd
                    {
                        if(rs.getString("idemp").equals(id) && pwd.equals(rs.getString("password"))) //les champs valides (auth)
                        {
                            //===================================== ADMIN ======================================================
                            if(rs.getString("post").equals("ADMIN")) // get poste employe (admin)
                            {
                                try
                                {
                                    pst = con.prepareStatement("select tentative from employe where idemp = "+id+";");
                                    ResultSet rs1 = pst.executeQuery(); // tentative

                                    while(rs1.next()) //parcours sur les tentatives
                                    {

                                        if(rs1.getString("tentative").equals("0"))
                                        {
                                            ChangerPwd(id,"ADMIN",rs.getString("nom"),event);
                                        }
                                        else
                                        {
                                            //Message("Authentifié ADMIN");
                                            AdminWindow(rs.getString("nom"),event);
                                        }
                                    }
                                }
                                catch (SQLException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }

                            //========================================== STOCK ==================================================
                            if(rs.getString("post").equals("Stock"))
                            {
                                try
                                {
                                    pst = con.prepareStatement("select tentative from employe where idemp = "+id+";");
                                    ResultSet rs2 = pst.executeQuery();

                                    while(rs2.next()) //parcours sur les tentatives
                                    {
                                        System.out.println(rs2.getString("tentative"));

                                        if(rs2.getString("tentative").equals("0"))
                                        {
                                            ChangerPwd(id,"Stock",rs.getString("nom"),event);
                                        }
                                        else
                                        {
                                            //Message("Authentifié Stock");
                                            StockWindow(rs.getString("nom"),event);
                                        }
                                    }
                                }
                                catch (SQLException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }


                            //=================================== Vendeur =====================================================
                            if(rs.getString("post").equals("Vendeur"))
                            {
                                try
                                {
                                    pst = con.prepareStatement("select tentative from employe where idemp = "+id+";");
                                    ResultSet rs3 = pst.executeQuery();

                                    while(rs3.next()) //parcours sur les tentatives
                                    {
                                        if(rs3.getString("tentative").equals("0"))
                                        {
                                            ChangerPwd(id,"Vendeur",rs.getString("nom"),event);
                                        }
                                        else
                                        {
                                            Message("Authentifié Vendeur");
                                            //VendeurWindow(id);
                                            //frameLogin.dispose();
                                        }
                                    }
                                }
                                catch (SQLException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }

                        }
                        else
                        {
                            Message("ID ou Mot de passe ne correspond pas");
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

    //--------------------- ADMIN WINDOW -----------------------
    public void AdminWindow(String currentuser, ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inter.fxml"));
            root = loader.load();
            Inter interAdmin = loader.getController();
            interAdmin.PrintAdminName(currentuser);
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    // ------------------------ Stock Window ---------------------------
    public void StockWindow(String currentuser, ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("entree.fxml"));
            root = loader.load();
            Entree entree = loader.getController();
            entree.PrintUserName(currentuser);
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    // ------------------------ Vendeur Window ---------------------------
    public void VendeurWindow(String currentuser, ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inter.fxml"));
            root = loader.load();
            Inter interAdmin = loader.getController();
            interAdmin.PrintAdminName(currentuser);
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }

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
            pst = con.prepareStatement("select idemp from employe where etat = 0"); //les employés actifs (non supprimé)
            ResultSet rs = pst.executeQuery();

            while(rs.next())
            {
                listid.add(rs.getString("idemp"));
            }


            for(int j=0; j<listid.size();j++)
            {
                if(id.equals(listid.get(j)))
                {
                    return true;
                }
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

    //------------------------- alert message ---------------------
    private void Message(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }


    //-------------- Changer mot de passe -------------
    public void ChangerPwd(String id , String post, String CurentUser,ActionEvent event)
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Pour la raison du sécurité vous devez changer votre mot de passe");
        dialog.setContentText("Entrez nouveau mot de passe :");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            if(result.get().equals(""))
            {
                Message("Verfiez mot de passe !!");
            }
            else
            {
                try
                {
                    pst = con.prepareStatement("update employe set tentative = 1 , password = ? where idemp = ? ");
                    pst.setString(1, result.get());
                    pst.setString(2, id);
                    pst.executeUpdate();
                    Message("Mot de passe a été changé");

                    //ADMIN window
                    if(post.equals("ADMIN"))
                    {
                        AdminWindow(CurentUser,event);
                    }
                    // stock window
                    if(post.equals("Stock"))
                    {
                       //StockWindow(CurentUser, event);
                    }
                    //vendeur window
                    if(post.equals("Vendeur"))
                    {
                       //VendeurWindow(CurentUser, event);
                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
