/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.AdminView;

import DataBaseORM.Condo;
import DataBaseORM.Room;
import DataBaseORM.Staff;
import LoginPermission.MyConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SortEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private TableView<Staff> staffTable;
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
    @FXML
    private TableColumn<Staff, String> firstNameColumn;
    @FXML
    private TableColumn<Staff, String> lastNameColumn;
    @FXML
    private TableColumn<Staff, String> reportToColumn;
    private TableColumn<Staff, String> bossNameColumn;
    private TableColumn<Staff, String> bossLastNameColumn;
    @FXML
    private TableColumn<Staff, String> staffIdColumn;
    @FXML
    private TableColumn<Staff, String> cityColumn;
    @FXML
    private TableColumn<Staff, String> positionColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Alert Dialog กรณีเลือก Tab Update ผิด
        if (idLabel == null) {
            System.out.println("sdfsdfs!!!! fuuu ID Label null");
        }
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
        //---------------- set user
        staffTable.setEditable(true);
        staffIdColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("staffId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("lastName"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("city"));
        reportToColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("reportTo"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("position"));
        loadManageCondo();
        loadManageRoom();
        loadManageStaff();
    }

    public void loadManageStaff() {
        staffTable.getItems().addAll(getStaff());
    }

    public ObservableList<Staff> getStaff() {
        ObservableList<Staff> staff = FXCollections.observableArrayList();
        try {
            pstm = con.prepareStatement("SELECT s.staffId,u.firstName,u.firstName,a.city,s.reportTo,p.nPosition\n"
                    + "FROM staff s\n"
                    + "left JOIN user u ON u.userId=s.staffId\n"
                    + "left JOIN position p ON s.positionId = p.positionId\n"
                    + "left JOIN area a ON s.areaId = a.areaId\n"
                    + "ORDER BY 1");
            rs = pstm.executeQuery();
            while (rs.next()) {
                System.out.println("table Condo ---");
                staff.add(new Staff(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLDataManipulationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return staff;
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
    boolean firstTime = true;

    @FXML
    private void condoSelectedTab(Event event) {
        checkTab = "condo";
        if (idLabel == null) {
            System.out.println("dfsd!!!! Null uuuuuuuuuuuuu");
        }
        if (firstTime == true) {
            idLabel.setText(checkTab.toUpperCase() + "ID");
            firstTime = false;
        } else {
            idLabel.setText(checkTab.toUpperCase() + "ID");
        }

        System.out.println(checkTab);
    }

    @FXML
    private void staffSelectedTab(Event event) {
        checkTab = "staff";
        idLabel.setText(checkTab.toUpperCase() + "ID");
    }

    @FXML
    private void roomSelectedTab(Event event) {
        checkTab = "room";
        idLabel.setText(checkTab.toUpperCase() + "ID");

        System.out.println(checkTab);
    }

    @FXML
    private void insertButtonOnClick(ActionEvent event) {
        System.out.println(checkTab);

        if (validSelectTab() == true) {
            //เช็คต่อว่ากำลังทำงานของ Tab ไหน
            if (checkTab.equals("room")) {

            } else if (checkTab.equals("condo")) {

            } else if (checkTab.equals("staff")) {

            }
        }

    }

    @FXML
    private void updateButtonOnClick(ActionEvent event) {
        boolean isInsert = false;
        if (validSelectTab() == true) {
            //เช็คต่อว่ากำลังทำงานของ Tab ไหน
            if (checkTab.equals("room")) {
                FXMLLoader roomFXML = new FXMLLoader(FXMLDataManipulationController.class.getResource("MainWindowView.fxml"));
                // roomFXML.setLocation();
                // FXMLRoomFormController roomController=new FXMLRoomFormController();
                // roomFXML.setController(roomController);

                //JFXDialogLayout popup=condoFXML.load();
                try {
                    AnchorPane condoPopup = roomFXML.load();

                    System.out.println("adfdsfds" + condoPopup.toString());
                    mainView.setCenter(condoPopup);
                    System.out.println("success!!");

                } catch (IOException ex) {
                    Logger.getLogger(FXMLDataManipulationController.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (checkTab.equals("condo")) {

            } else if (checkTab.equals("staff")) {

            }
        }
    }

    @FXML
    private void deleteButtonOnClick(ActionEvent event) {
        if (validSelectTab() == true) {
            //implement 
            //เช็คต่อว่ากำลังทำงานของ Tab ไหน
            if (checkTab.equals("room")) {

            } else if (checkTab.equals("condo")) {

            } else if (checkTab.equals("staff")) {
                String staffId = idTextField.getText();
                if (!(staffId.isEmpty() | staffId == null)) {
                    staffTable.getItems().clear();
                    try {
                        pstm = con.prepareStatement("DELETE FROM staff WHERE staffId=?");
                        pstm.setString(1, staffId);
                        pstm.executeUpdate();
                        staffTable.getItems().clear();
                        loadManageStaff();
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLDataManipulationController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
    }

    public boolean validSelectTab() {
        boolean isValid = false;
        if (checkTab.equalsIgnoreCase("staff")) {
            if (idTextField.getText().charAt(0) == 's') {
                isValid = true;
            } else {
                //alrt wrong input
                alert.setHeaderText("Invalid Input !!!");
                alert.setContentText("Now you in " + checkTab.toUpperCase() + " Please Input ID that need to be sxxxx only");
                alert.showAndWait();
            }

        } else if (checkTab.equalsIgnoreCase("room")) {
            if (idTextField.getText().charAt(0) == 'r') {
                isValid = true;

            } else {
                //alrt wrong input
                alert.setHeaderText("Invalid Input !!!");
                alert.setContentText("Now you in " + checkTab.toUpperCase() + " Please Input ID that need to be rxxxx only");
                alert.showAndWait();
            }
        } else if (checkTab.equalsIgnoreCase("condo")) {
            if (idTextField.getText().charAt(0) == 'c') {
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
        idTextField.setText(staffTable.getSelectionModel().getSelectedItem().getStaffId());
    }

}
