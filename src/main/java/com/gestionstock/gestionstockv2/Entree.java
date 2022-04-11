package com.gestionstock.gestionstockv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Entree {

    @FXML
    private TableColumn<?, ?> DATEcol;

    @FXML
    private TableColumn<?, ?> FOURcol;

    @FXML
    private TableColumn<?, ?> IDEncol;

    @FXML
    private TableColumn<?, ?> IDcol;

    @FXML
    private TableColumn<?, ?> MARcol;

    @FXML
    private TableColumn<?, ?> MODcol;

    @FXML
    private TableColumn<?, ?> MONTcol;

    @FXML
    private TableColumn<?, ?> PIECEcol;

    @FXML
    private TableColumn<?, ?> PRIXcol;

    @FXML
    private TableColumn<?, ?> QTENTcol;

    @FXML
    private TableColumn<?, ?> QTEcol;

    @FXML
    private TableColumn<?, ?> SERcol;

    @FXML
    private TextField date;

    @FXML
    private TextField fournisseur;

    @FXML
    private TextField inputEntree;

    @FXML
    private TextField inputpiece;

    @FXML
    private TextField piece;

    @FXML
    private TextField qte;

    @FXML
    private TableView<?> tableEntree;

    @FXML
    private TableView<?> tablePiece;

    @FXML
    private Label user;

    @FXML
    void ajoutClick(ActionEvent event) {

    }

    @FXML
    void backClick(ActionEvent event) {

    }

    @FXML
    void consulterClick(ActionEvent event) {

    }

    @FXML
    void decClick(ActionEvent event) {

    }

    @FXML
    void ligneEntreClick(MouseEvent event) {

    }

    @FXML
    void lignePieceClick(MouseEvent event) {

    }

    @FXML
    void rechEntreeClick(ActionEvent event) {

    }

    @FXML
    void rechPieceClick(ActionEvent event) {

    }

    @FXML
    void viderClick(ActionEvent event) {

    }



    //--------------------- set user name (passage entre les controlleurs) -------------------
    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }

}
