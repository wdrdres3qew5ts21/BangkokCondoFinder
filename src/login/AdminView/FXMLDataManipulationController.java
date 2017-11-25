/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.AdminView;

import DataBaseORM.Condo;
import DataBaseORM.Room;
import LoginPermission.MyConnection;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLDataManipulationController implements Initializable {
    private ResultSet rs=null;
    private PreparedStatement pstm = null;
    private Connection con = MyConnection.getConnection();
    
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
    private TableColumn<Condo,String> condoIdColumn;
    @FXML
    private TableColumn<Condo,String> condoNameColumn;
    @FXML
    private TableColumn<Condo,String> areaColumn;
    @FXML
    private TableColumn<Condo,String> parkingColumn;
    @FXML
    private TableColumn<Condo,String> swimmingColumn;
    @FXML
    private TableColumn<Condo,String> fitnessColumn;
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
    private TableView<?> roomTable;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //ตั้งไว้เพื่อให้มันสามารถเข้าไปหาชื่อ column ได้ถูกจาก object เองแบบเทพๆ <Generic,DataType>("attributeName")
        condoTable.setEditable(true);
        condoIdColumn.setCellValueFactory(new PropertyValueFactory<Condo,String>("condoId"));
        condoNameColumn.setCellValueFactory(new PropertyValueFactory<Condo,String>("condoName"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<Condo,String>("area"));
        parkingColumn.setCellValueFactory(new PropertyValueFactory<Condo,String>("parking"));
        swimmingColumn.setCellValueFactory(new PropertyValueFactory<Condo,String>("swimming"));    
        fitnessColumn.setCellValueFactory(new PropertyValueFactory<Condo,String>("fitness"));   
        //-----------------
        //set room
         condoTable.setEditable(true);
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<Room,String>("roomId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Room,String>("typeId"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Room,Integer>("price"));
        bedroomColumn.setCellValueFactory(new PropertyValueFactory<Room,Integer>("parking"));
        bathroomColumn.setCellValueFactory(new PropertyValueFactory<Room,Integer>("swimming"));    
        sqMeterColumn.setCellValueFactory(new PropertyValueFactory<Room,Integer>("fitness"));   
        loadManageCondo();
    }
    public void loadManageRoom(){
        roomTable.setEditable(true);
    }



    
    
    public void loadManageCondo() {
        condoTable.setEditable(true);
        condoTable.getItems().addAll(getCondo());
    }
    
    public ObservableList<Condo> getCondo(){
        ObservableList<Condo> condos=FXCollections.observableArrayList();
         try {
            pstm = con.prepareStatement("SELECT c.condoId,c.condoName,a.city,c.parking,c.swimming,c.fitness FROM condo c left join area a\n"
                    + "ON c.areaId = a.areaId");
            rs=pstm.executeQuery();
            while(rs.next()){
                System.out.println("table Condo ---");
                condos.add(new Condo(rs.getString("c.condoId"), rs.getString("c.condoName")
                        , rs.getString("a.city"),rs.getInt("c.parking"), rs.getInt("c.swimming"), rs.getInt("c.fitness")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDataManipulationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return condos;
    }
    
}
