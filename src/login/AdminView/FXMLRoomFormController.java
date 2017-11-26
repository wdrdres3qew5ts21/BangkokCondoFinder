/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.AdminView;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLRoomFormController implements Initializable {

    @FXML
    private TextField bathroomTextField;
    @FXML
    private ComboBox<String> condoNameTextField;
    @FXML
    private TextField bedroomTextField;
    @FXML
    private TextField sqMeterTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextArea decriptionTextArea;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton actionButton;

    /**
     * Initializes the controller class.
     */
    Connection con;
    String condoName = "";
    boolean isInsert = true;
    private PreparedStatement pstm = null;
    private ResultSet rs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        try {
            pstm = con.prepareStatement("select DISTINCT condoName from condo");
            rs = pstm.executeQuery();
            while (rs.next()) {
                condoNameTextField.getItems().add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLRoomFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void backButtonOnClick(ActionEvent event) {
    }

    @FXML
    private void actionSQLButton(ActionEvent event) {
        ResultSet rs = null;
        
        if (isInsert == true) {//insert
            try {
                pstm = con.prepareStatement("");
            } catch (SQLException ex) {
                Logger.getLogger(FXMLRoomFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {//update

        }
    }

}
