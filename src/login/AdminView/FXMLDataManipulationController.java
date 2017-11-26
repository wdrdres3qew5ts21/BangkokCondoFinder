/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.AdminView;

import DataBaseORM.Condo;
import DataBaseORM.Room;
import LoginPermission.MyConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SortEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import login.FXMLLoginController;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLDataManipulationController extends FXMLLoginController implements Initializable {

    private ResultSet rs = null;
    private PreparedStatement pstm = null;
    private Connection con = MyConnection.getConnection();
    private String checkTab = "";

    @FXML
    private ScrollPane condoScrollPane;
    @FXML
    private TableView<Condo> condoTable;
    @FXML
    private ScrollPane staffManageScrollPane;
    @FXML
    private ScrollPane roomManageScrollPane;
    @FXML
    private ScrollPane viewChartManageScrollPane;
    @FXML
    private TableColumn<Condo, String> condoIdColumn;
    @FXML
    private TableColumn<Condo, String> condoNameColumn;
    @FXML
    private TableColumn<Condo, String> areaColumn;
    @FXML
    private TableColumn<Condo, String> parkingColumn;
    @FXML
    private TableColumn<Condo, String> swimmingColumn;
    @FXML
    private TableColumn<Condo, String> fitnessColumn;
    @FXML
    private TableView<?> staffTable;
    @FXML
    private TableColumn<?, ?> staffIdColumn1;
    @FXML
    private TableColumn<?, ?> condoNameColumn1;
    @FXML
    private TableColumn<?, ?> areaColumn1;
    @FXML
    private TableColumn<?, ?> parkingColumn1;
    @FXML
    private TableColumn<?, ?> swimmingColumn1;
    @FXML
    private TableColumn<?, ?> fitnessColumn1;
    @FXML
    private TableColumn<Room, String> roomIdColumn;
    @FXML
    private TableColumn<Room, String> typeColumn;
    @FXML
    private TableColumn<Room, Integer> priceColumn;
    @FXML
    private TableColumn<Room, Integer> bedroomColumn;
    @FXML
    private TableColumn<Room, Integer> bathroomColumn;
    @FXML
    private TableColumn<Room, Integer> sqMeterColumn;
    @FXML
    private TableView<Room> roomTable;
    @FXML
    private Tab condoTab;
    @FXML
    private Tab staffTab;
    @FXML
    private Tab roomTab;
    @FXML
    private Tab viewTab;
    @FXML
    private JFXButton insertButton;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private Label idLabel;

    Alert alert = new Alert(Alert.AlertType.WARNING);
    @FXML
    private JFXTextField idTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Alert Dialog กรณีเลือก Tab Update ผิด
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Empty Field!");

        //ตั้งไว้เพื่อให้มันสามารถเข้าไปหาชื่อ column ได้ถูกจาก object เองแบบเทพๆ <Generic,DataType>("attributeName")
        condoTable.setEditable(true);
        condoIdColumn.setCellValueFactory(new PropertyValueFactory<Condo, String>("condoId"));
        condoNameColumn.setCellValueFactory(new PropertyValueFactory<Condo, String>("condoName"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<Condo, String>("area"));
        parkingColumn.setCellValueFactory(new PropertyValueFactory<Condo, String>("parking"));
        swimmingColumn.setCellValueFactory(new PropertyValueFactory<Condo, String>("swimming"));
        fitnessColumn.setCellValueFactory(new PropertyValueFactory<Condo, String>("fitness"));
        //-----------------
        //set room
        condoTable.setEditable(true);
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("roomId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Room, Integer>("price"));
        bedroomColumn.setCellValueFactory(new PropertyValueFactory<Room, Integer>("bedroom"));
        bathroomColumn.setCellValueFactory(new PropertyValueFactory<Room, Integer>("bathroom"));
        sqMeterColumn.setCellValueFactory(new PropertyValueFactory<Room, Integer>("sqMeter"));
        loadManageCondo();
        loadManageRoom();
    }

    public void loadManageRoom() {
        roomTable.setEditable(true);
        roomTable.getItems().addAll(getRoom());
    }

    public ObservableList<Room> getRoom() {
        ObservableList<Room> room = FXCollections.observableArrayList();
        try {
            pstm = con.prepareStatement("SELECT r.roomId,t.nType,r.price,r.bedrooms,r.bathrooms,r.sqMeters\n"
                    + "FROM room r JOIN roomType t ON r.typeId = t.typeId;");
            rs = pstm.executeQuery();
            while (rs.next()) {
                System.out.println("table Condo ---");
                room.add(new Room(rs.getString("r.roomId"), rs.getString("t.nType"), rs.getInt("r.bedrooms"), rs.getInt("r.bathrooms"), rs.getInt("price"), rs.getInt("r.sqMeters")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLDataManipulationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return room;
    }

    public void loadManageCondo() {
        condoTable.setEditable(true);
        condoTable.getItems().addAll(getCondo());
    }

    public ObservableList<Condo> getCondo() {
        ObservableList<Condo> condos = FXCollections.observableArrayList();
        try {
            pstm = con.prepareStatement("SELECT c.condoId,c.condoName,a.city,c.parking,c.swimming,c.fitness FROM condo c left join area a\n"
                    + "ON c.areaId = a.areaId");
            rs = pstm.executeQuery();
            while (rs.next()) {
                System.out.println("table Condo ---");
                condos.add(new Condo(rs.getString("c.condoId"), rs.getString("c.condoName"),
                        rs.getString("a.city"), rs.getInt("c.parking"), rs.getInt("c.swimming"), rs.getInt("c.fitness")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLDataManipulationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return condos;
    }
    boolean firstTime=true;
    @FXML
    private void condoSelectedTab(Event event) {
        checkTab = "condo";
        idLabel.setText(checkTab.toUpperCase()+"ID");
        System.out.println(checkTab);
//        if(firstTime==true){
//            firstTime=false;
//        }
//        sle{
//        System.out.println("Sorry for Inconvinince ! Maintenance");
//        alert.setHeaderText("Invalid Input !!!");
//        alert.setContentText("Now you in " + checkTab.toUpperCase() + " Please Input ID that need to be Cxxxx only");
//        alert.showAndWait();
    }

    @FXML
    private void staffSelectedTab(Event event) {
        checkTab = "staff";
        idLabel.setText(checkTab.toUpperCase()+"ID");
    }

    @FXML
    private void roomSelectedTab(Event event) {
        checkTab = "room";
        idLabel.setText(checkTab.toUpperCase()+"ID");

        System.out.println(checkTab);
    }

    @FXML
    private void insertButtonOnClick(ActionEvent event) {
        System.out.println(checkTab);
       
        if(validSelectTab()==true){
            //เช็คต่อว่ากำลังทำงานของ Tab ไหน
            if(checkTab.equals("room")){
                
            }
            else if(checkTab.equals("condo")){
                
            }
            else if(checkTab.equals("staff")){
                
            }
        }

    }

    @FXML
    private void updateButtonOnClick(ActionEvent event) {
        if(validSelectTab()==true){
            //เช็คต่อว่ากำลังทำงานของ Tab ไหน
            if(checkTab.equals("room")){
                
            }
            else if(checkTab.equals("condo")){
                
            }
            else if(checkTab.equals("staff")){
                
            }
        }
    }

    @FXML
    private void deleteButtonOnClick(ActionEvent event) {
        if(validSelectTab()==true){
            //implement 
            //เช็คต่อว่ากำลังทำงานของ Tab ไหน
            if(checkTab.equals("room")){
                
            }
            else if(checkTab.equals("condo")){
                
            }
            else if(checkTab.equals("staff")){
                
            }
        }
    }

    public boolean validSelectTab() {
        boolean isValid = false;
        if (checkTab.equalsIgnoreCase("staff")) {
            if (idLabel.getText().charAt(0) == 's') {
                isValid = true;
            } else {
                //alrt wrong input
                alert.setHeaderText("Invalid Input !!!");
                alert.setContentText("Now you in " + checkTab.toUpperCase() + " Please Input ID that need to be sxxxx only");
                alert.showAndWait();
            }

        } else if (checkTab.equalsIgnoreCase("room")) {
            if (idLabel.getText().charAt(0) == 'r') {
                isValid = true;

            } else {
                //alrt wrong input
                alert.setHeaderText("Invalid Input !!!");
                alert.setContentText("Now you in " + checkTab.toUpperCase() + " Please Input ID that need to be rxxxx only");
                alert.showAndWait();
            }
        } else if (checkTab.equalsIgnoreCase("condo")) {
            if (idLabel.getText().charAt(0) == 'c') {
                isValid = true;
            } else {
                //alrt wrong input
                alert.setHeaderText("Invalid Input !!!");
                alert.setContentText("Now you in " + checkTab.toUpperCase() + " Please Input ID that need to be cxxxx only");
                alert.showAndWait();
            }
        }
        return isValid;
    }

    @FXML
    private void roomTableOnClick(MouseEvent event) {
        idTextField.setText(roomTable.getSelectionModel().getSelectedItem().getRoomId());
        
    }

    @FXML
    private void condoTableOnClick(MouseEvent event) {
         idTextField.setText(condoTable.getSelectionModel().getSelectedItem().getCondoId());
    }

    @FXML
    private void staffTableOnClick(MouseEvent event) {
        idTextField.setText(staffTable.getSelectionModel().getSelectedItem().toString());
    }

   

 

}
