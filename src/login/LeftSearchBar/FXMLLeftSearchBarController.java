/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.LeftSearchBar;

import LoginPermission.MyConnection;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import login.FXMLLoginController;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLLeftSearchBarController extends FXMLLoginController implements Initializable {

    Connection con = MyConnection.getConnection();
    PreparedStatement pstm = null;
    @FXML
    private GridPane leftSearchBar;
    @FXML
    private JFXComboBox<String> areaComboBox;
    @FXML
    private JFXComboBox<String> propertyTypeComboBox;
    @FXML
    private JFXComboBox<Integer> bedroomComboBox;
    @FXML
    private JFXComboBox<String> areaByMetroComboBox;
    private Slider maxSlider;
    private Slider minSlider;
    @FXML
    private JFXTextField minField;
    @FXML
    private JFXTextField maxField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //load area จาก db   

        loadAreaComboBox();
//        minSlider.applyCss();
//        minSlider.layout();
//        Label valueSlider = new Label();
//        Pane thumb = (Pane) minSlider.lookup(".thumb");
//        if (thumb != null) {
//            System.out.println("not null thumb");
//        } else {
//            System.out.println("null thumb");
//        }
//        valueSlider.textProperty().bind(minSlider.valueProperty().asString("%.1f"));
        // thumb.getChildren().add(valueSlider);

        //valueSlider.textProperty().bind(maxSlider.valueProperty().asString());
        areaComboBox.setValue("All");
        areaByMetroComboBox.setValue("All");
        bedroomComboBox.setValue(0);
        propertyTypeComboBox.setValue("All");
    }

    public void loadAreaComboBox() {
        try {
            PreparedStatement pstm = con.prepareStatement("select city from area");
            ResultSet rs = pstm.executeQuery();
            areaComboBox.getItems().add("All");
            while (rs.next()) {
                areaComboBox.getItems().add(rs.getString(1));
            }
            pstm = con.prepareStatement("SELECT DISTINCT bedrooms from room order by 1;");
            rs = pstm.executeQuery();
            while (rs.next()) {
                bedroomComboBox.getItems().add(rs.getInt(1));
            }

            pstm = con.prepareStatement("SELECT DISTINCT nType from roomType ORDER BY 1");
            rs = pstm.executeQuery();
            propertyTypeComboBox.getItems().add("All");
            while (rs.next()) {
                propertyTypeComboBox.getItems().add(rs.getString(1));
            }
            //ดึงประเภทคอนโดออกมา

            //ดึงราคาต่ำสุดกับสูงสุดออกมา
            pstm = con.prepareStatement("(SELECT min(price) from room)\n"
                    + "union\n"
                    + "(SELECT max(price) from room)\n"
                    + "ORDER BY 1");
            rs = pstm.executeQuery();
            rs.next();
            int min=rs.getInt(1);
            pstm = con.prepareStatement("(SELECT max(price) from room)\n"
                    + "union\n"
                    + "(SELECT max(price) from room)\n"
                    + "ORDER BY 1");
            rs=pstm.executeQuery();
            rs.next();
            int max=rs.getInt(1);
            System.out.println("Min : "+min+" Max:"+max);
//            minSlider.setMin(min);
//            minSlider.setMax(max);
            rs.next();
//            maxSlider.setMin(min);
//            maxSlider.setMax(max);

            //con.close();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLLeftSearchBarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAreaByMetroComboBox() {
        try {
            PreparedStatement pstm = con.prepareStatement("select city from area");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                areaComboBox.getItems().add(rs.getString(1));
            }
            if (areaComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
                areaByMetroComboBox.setValue("All");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLLeftSearchBarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void findPropertyOnclick(ActionEvent event) {
        String area = "'_?'";
        String metro = "'_?'";
        String type = "'_?'";
        int bedroom = 0;
        int beginPrice = 0;
        int maxPrice = 0;
        if (areaComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
            area = "'_%'";
        } else {
            area = "'"+areaComboBox.getSelectionModel().getSelectedItem()+"'";
        }
        
        if (areaByMetroComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
            metro = "'_%'";
        } else {
            metro = "'"+areaByMetroComboBox.getSelectionModel().getSelectedItem()+"'";
        }
        
        if (propertyTypeComboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
            type = "'_%'";
        } else {
            type = "'"+propertyTypeComboBox.getSelectionModel().getSelectedItem()+"'";
        }
        bedroom = bedroomComboBox.getSelectionModel().getSelectedItem();
        beginPrice = Integer.parseInt(minField.getText());
        maxPrice = Integer.parseInt(maxField.getText());
  
        leftSearchBar(area, metro, bedroom, beginPrice, maxPrice, type);
    }

    @FXML
    private void areaComboOnClick(ActionEvent event) {
        areaByMetroComboBox.getItems().clear();
        areaByMetroComboBox.getItems().add("All");
        try {
            pstm = con.prepareStatement("SELECT metroName FROM metro JOIN area\n"
                    + "ON metro.areaId = area.areaId\n"
                    + "WHERE city=? ");
            pstm.setString(1, areaComboBox.getValue());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                areaByMetroComboBox.getItems().add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLLeftSearchBarController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
