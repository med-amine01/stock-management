package com.gestionstock.gestionstockv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Sortie {

    @FXML
    private TableView<?> tableSortie;
    @FXML
    private TableColumn<?, ?> DATESORcol;
    @FXML
    private TableColumn<?, ?> IDSORcol;
    @FXML
    private TableColumn<?, ?> MONTSORcol;
    @FXML
    private TableColumn<?, ?> NOMVENcol;
    @FXML
    private TableColumn<?, ?> QTESORTcol;

    //------------------------------------

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField inputSortie;
    @FXML
    private Label mntTot;
    @FXML
    private Label user;

    @FXML
    void backClick(ActionEvent event) {

    }

    @FXML
    void dateClick(ActionEvent event) {

    }

    @FXML
    void decClick(ActionEvent event) {

    }

    @FXML
    void rechSortieClick(ActionEvent event) {

    }

    public void PrintUserName(String CurrentUserName) {
        user.setText(CurrentUserName);
    }
}
