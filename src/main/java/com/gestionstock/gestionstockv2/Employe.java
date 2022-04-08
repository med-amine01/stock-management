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
import java.util.Optional;
import java.util.ResourceBundle;

public class Employe implements Initializable {
    @FXML
    private TableView<Emp> table;
    @FXML
    private TableColumn<Emp, String> ADDcol;
    @FXML
    private TableColumn<Emp, Integer> IDcol;
    @FXML
    private TableColumn<Emp, String> MAILcol;
    @FXML
    private TableColumn<Emp, String> NOMcol;
    @FXML
    private TableColumn<Emp, String> PRENOMcol;
    @FXML
    private TableColumn<Emp, Double> SALcol;
    @FXML
    private TableColumn<Emp, String> POSTcol;



    @FXML
    private ComboBox<?> PostEmp;
    @FXML
    private TextField addEmp;
    @FXML
    private Button ajouBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button confBtn;
    @FXML
    private Button dec;
    @FXML
    private TextField inputEmp;
    @FXML
    private TextField mailEmp;
    @FXML
    private Button modBtn;
    @FXML
    private TextField nomEmp;
    @FXML
    private TextField preEmp;
    @FXML
    private TextField pwdEmp;
    @FXML
    private Button rechBtn;
    @FXML
    private TextField salEmp;
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
    public ObservableList<Emp> getEmployees()
    {
        ObservableList<Emp> empList = FXCollections.observableArrayList();
        connect();
        try
        {
            pst = con.prepareStatement("select idemp,nom,prenom,adresse,mail,salaire,post from employe where etat = 0");
            ResultSet rs = pst.executeQuery();
            Emp employees;

            while (rs.next())
            {
                employees = new Emp(rs.getInt("idemp"), rs.getString("nom"), rs.getString("prenom"),rs.getString("adresse"),
                        rs.getString("mail"),rs.getString("post"),rs.getDouble("salaire"));
                empList.add(employees);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return empList;
    }
    //---------------------- chargement du tableau dans une liste (Search)-----------------------------
    public ObservableList<Emp> getEmployees(String sqlSearch)
    {
        ObservableList<Emp> empList = FXCollections.observableArrayList();
        connect();
        try
        {
            pst = con.prepareStatement(sqlSearch);
            ResultSet rs = pst.executeQuery();
            Emp employees;

            while (rs.next())
            {
                employees = new Emp(rs.getInt("idemp"), rs.getString("nom"), rs.getString("prenom"),rs.getString("adresse"),
                        rs.getString("mail"),rs.getString("post"),rs.getDouble("salaire"));
                empList.add(employees);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return empList;
    }

    //---------------------------- ACTUALISER -----------------------------
    public void Actualiser()
    {
        ObservableList<Emp> list = getEmployees();
        IDcol.setCellValueFactory(new PropertyValueFactory<Emp,Integer>("id"));
        NOMcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("nom"));
        PRENOMcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("prenom"));
        ADDcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("adresse"));
        MAILcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("mail"));
        POSTcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("poste"));
        SALcol.setCellValueFactory(new PropertyValueFactory<Emp,Double>("salaire"));

        table.setItems(list);
    }

    //------------------------ AJOUTER -------------------------------
    @FXML
    void ajoutClick(ActionEvent event) {

        String nom,prenom,adresse,mail,post,password,salaire;
        nom = nomEmp.getText().trim();
        prenom = preEmp.getText().trim();
        adresse = addEmp.getText().trim();
        mail = mailEmp.getText().trim();
        post = PostEmp.getSelectionModel().getSelectedItem().toString();
        password = pwdEmp.getText().trim();
        salaire = salEmp.getText().trim();

        if(ChampEstVide(nom, prenom, adresse, mail, password, salaire))
        {
            Message("Verifiez Les Champs !!");
            nomEmp.requestFocus();
        }
        else
        {
            if(!MailEstUniqueAjout(mail) || !valideMail(mail))
            {
                Message("L'employé Déjà Existe ou  mail invalide");
                mailEmp.setText("");
                mailEmp.requestFocus();
            }
            else
            {
                if(!ChampSalaireEstDouble(salaire))
                {
                    Message("Verifiez Le salaire !!");
                    salEmp.setText("");
                    salEmp.requestFocus();
                }
                else
                {
                    if( PostEmp.getSelectionModel().getSelectedIndex() == -1)
                    {
                        Message("Choisissez la poste !");
                        PostEmp.requestFocus();
                    }
                    else
                    {
                        try
                        {
                            pst = con.prepareStatement("insert into employe (password,nom,prenom,adresse,mail,salaire,post,tentative,etat) values (?,?,?,?,?,?,?,?,?)");
                            pst.setString(1, password);
                            pst.setString(2, nom);
                            pst.setString(3, prenom);
                            pst.setString(4, adresse);
                            pst.setString(5, mail);
                            pst.setString(6, salaire);
                            pst.setString(7, post);
                            pst.setString(8, "0");
                            pst.setString(9, "0");
                            pst.executeUpdate();
                            Message("Employé Ajouté !!");
                            Actualiser();
                            nomEmp.setText("");
                            preEmp.setText("");
                            addEmp.setText("");
                            mailEmp.setText("");
                            pwdEmp.setText("");
                            salEmp.setText("");
                            nomEmp.requestFocus();
                        }
                        catch (SQLException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    //------------------------ MODIFER-------------------------
    @FXML
    void modClick(ActionEvent event) {
        String id=inputEmp.getText();
        if(ChampsIdEstInt(id)==false || IdExist(id)==false)
        {
            Message("Impossible De Modifier (id invalide)");
            inputEmp.setText("");
            inputEmp.requestFocus();
        }
        else
        {
            try
            {
                pst = con.prepareStatement("select nom,prenom,adresse,mail,salaire,post,password from employe where idemp = "+id+";");
                ResultSet rs = pst.executeQuery();

                while(rs.next())
                {
                    nomEmp.setText(rs.getString("nom"));
                    preEmp.setText(rs.getString("prenom"));
                    addEmp.setText(rs.getString("adresse"));
                    mailEmp.setText(rs.getString("mail"));
                    salEmp.setText(rs.getString("salaire"));
                    pwdEmp.setText(rs.getString("password"));

                    if(rs.getString("post").equals("Stock"))
                    {
                        PostEmp.getSelectionModel().select(0);
                    }

                    if(rs.getString("post").equals("Vendeur"))
                    {
                        PostEmp.getSelectionModel().select(1);
                    }

                    if(rs.getString("post").equals("ADMIN"))
                    {
                        PostEmp.getSelectionModel().select(3);
                    }
                }
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    //----------------------- CONFIRMER MODIFER -------------------------------
    @FXML
    void confClick(ActionEvent event) {
        String nom,prenom,adresse,mail,post,password,salaire,idconf;
        nom = nomEmp.getText().trim();
        idconf = inputEmp.getText().trim();
        prenom = preEmp.getText().trim();
        adresse = addEmp.getText().trim();
        mail = mailEmp.getText().trim();
        post = PostEmp.getSelectionModel().getSelectedItem().toString();
        password = pwdEmp.getText().trim();
        salaire = salEmp.getText().trim();


        if(ChampEstVide(nom, prenom, adresse, mail, password, salaire))
        {
            Message("Verifiez Les Champs !!");
            nomEmp.requestFocus();
        }
        else
        {
            if(!MailEstUnique(mail,idconf) || !valideMail(mail))
            {
                Message( "L'employé Déjà Existe ou  mail invalide");
                mailEmp.setText("");
                mailEmp.requestFocus();
            }
            else
            {
                if (!ChampSalaireEstDouble(salaire))
                {
                    Message("Verifiez Le salaire !!");
                    salEmp.setText("");
                    salEmp.requestFocus();
                }
                else
                {
                    try
                    {
                        pst = con.prepareStatement("update employe set nom = ? , prenom = ? , adresse = ? , mail = ? , salaire = ? , post = ? , password = ? where idemp = ? ");
                        pst.setString(1, nom);
                        pst.setString(2, prenom);
                        pst.setString(3, adresse);
                        pst.setString(4, mail);
                        pst.setString(5, salaire);
                        pst.setString(6, post);
                        pst.setString(7, password);
                        pst.setString(8, idconf);
                        pst.executeUpdate();
                        Message("Employé Modifié !!");
                        Actualiser();
                        inputEmp.requestFocus();

                    }
                    catch (SQLException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    //----------------------------------- SUPPRIMER ---------------------------------------
    @FXML
    void suppClick(ActionEvent event) {
        String id=inputEmp.getText();
        if(ChampsIdEstInt(id)==false || IdExist(id)==false)
        {
            Message("Impossible De Supprimer");
            inputEmp.setText("");
            inputEmp.requestFocus();
        }
        else
        {
            //if deleting an admin it should require an auth of that specific admin (simplier)
            //defaut : admin should authentify at least once before getting deleted
            try
            {
                pst1 = con.prepareStatement("select idemp, password, post , nom from employe where idemp = "+id+";");
                ResultSet rs1 = pst1.executeQuery();

                while(rs1.next())
                {
                    if(rs1.getString("post").equals("ADMIN"))
                    {
                        String mdp;
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setHeaderText("La Suppression d'un Admin exige son mot de passe !!");
                        dialog.setContentText("mot de passe de l'Admin à supprimer :");

                        Optional<String> result = dialog.showAndWait();

                        if (result.isPresent())
                        {
                            mdp = result.get().trim();

                            if((rs1.getString("idemp").equals(id) && mdp.equals(rs1.getString("password"))))
                            {
                                try
                                {
                                    pst = con.prepareStatement("update employe set etat = ? where idemp = ? ");
                                    pst.setString(1, "1");
                                    pst.setString(2, id);
                                    pst.executeUpdate();
                                    Message("Admin Supprimé !!");
                                    Actualiser();
                                    inputEmp.setText("");
                                    inputEmp.requestFocus();
                                }
                                catch (SQLException e1)
                                {
                                    e1.printStackTrace();
                                }
                            }
                            else
                            {
                                Message("Impossible De Supprimer");
                            }
                        }

                    }
                    else
                    {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("L'employé "+id+" sera supprimer");
                        alert.setContentText("Vous êtes sûr de vouloir supprimer ?");

                        Optional<ButtonType> r = alert.showAndWait();
                        if (r.get() == ButtonType.OK){
                            try
                            {
                                pst = con.prepareStatement("update employe set etat = ? where idemp = ? ");
                                pst.setString(1, "1");
                                pst.setString(2, id);
                                pst.executeUpdate();
                                Message("Employé Supprimé !!");
                                Actualiser();
                                inputEmp.setText("");
                                inputEmp.requestFocus();
                            }
                            catch (SQLException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }

        }
    }

    //----------------------- Rechercher---------------------------
    @FXML
    void rechClick(ActionEvent event) {
        String rech = inputEmp.getText().trim();
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
                    inputEmp.setText("");
                    inputEmp.requestFocus();
                }
                else
                {

                    String rqt = "select idemp,nom,prenom,adresse,mail,salaire,post from employe where etat = 0 and idemp = "+rech;
                    ObservableList<Emp> list = getEmployees(rqt);
                    IDcol.setCellValueFactory(new PropertyValueFactory<Emp,Integer>("id"));
                    NOMcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("nom"));
                    PRENOMcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("prenom"));
                    ADDcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("adresse"));
                    MAILcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("mail"));
                    POSTcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("poste"));
                    SALcol.setCellValueFactory(new PropertyValueFactory<Emp,Double>("salaire"));
                    table.setItems(list);
                }
            }
            else
            {
                String rqt = "select idemp,nom,prenom,adresse,mail,salaire,post from employe where nom like '"+rech+"%'" +
                        "or prenom like '"+rech+"%' or adresse like '"+rech+"%' or post like '"+rech+"%' GROUP BY etat HAVING etat = 0";
                ObservableList<Emp> list = getEmployees(rqt);
                IDcol.setCellValueFactory(new PropertyValueFactory<Emp,Integer>("id"));
                NOMcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("nom"));
                PRENOMcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("prenom"));
                ADDcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("adresse"));
                MAILcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("mail"));
                POSTcol.setCellValueFactory(new PropertyValueFactory<Emp,String>("poste"));
                SALcol.setCellValueFactory(new PropertyValueFactory<Emp,Double>("salaire"));
                table.setItems(list);
            }
        }
    }


    @FXML
    void postClick(ActionEvent event) {

    }


    @FXML
    void backClick(ActionEvent event) {
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







    //------------------------- alert message ---------------------
    private void Message(String msg) {
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
            pst = con.prepareStatement("select idemp from employe where etat = 0");
            ResultSet rs = pst.executeQuery();

            //empiler tabid avec les id à partir de la base de donnée
            while(rs.next())
            {
                listid.add(rs.getString("idemp"));
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

    //------------------------- verification champ salaire ----------------------
    public boolean ChampSalaireEstDouble(String salaire)
    {
        boolean b = false ;
        try
        {
            Double.parseDouble(salaire);
            b = true;
        }
        catch(NumberFormatException e)
        {
            b = false;
        }
        return b;
    }


    //-------------------- mail unique -------------------------
    public boolean MailEstUnique(String inputMail, String id)
    {
        ArrayList listmail = new ArrayList();
        try
        {
            pst = con.prepareStatement("select mail from employe where idemp != '"+id+"' and etat = 0");
            ResultSet rs = pst.executeQuery();

            //empiler tabmail avec les mail à partir de la base de donnée
            while(rs.next())
            {
                listmail.add(rs.getString("mail"));
            }

            int i = 0 ;
            boolean bool = false;
            while(i<listmail.size() || bool == true)
            {
                if(inputMail.equals(listmail.get(i)))
                {
                    bool = true;
                    return false;
                }
                i++;
            }

            listmail.clear();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true ;
    }

    //-------------------- mail unique pour l'ajout -------------------
    public boolean MailEstUniqueAjout(String inputMail)
    {
        ArrayList listmail = new ArrayList();
        try
        {
            pst = con.prepareStatement("select mail from employe where etat = 0");
            ResultSet rs = pst.executeQuery();

            //empiler tabmail avec les mail à partir de la base de donnée
            while(rs.next())
            {
                listmail.add(rs.getString("mail"));
            }

            int i = 0 ;
            boolean bool = false;
            while(i<listmail.size() || bool == true)
            {
                if(inputMail.equals(listmail.get(i)))
                {
                    bool = true;
                    return false;
                }
                i++;
            }

            listmail.clear();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true ;
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
}