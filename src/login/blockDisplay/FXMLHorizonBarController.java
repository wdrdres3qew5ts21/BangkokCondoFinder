/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.blockDisplay;

import LoginPermission.MyConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import login.FXMLLoginController;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLHorizonBarController extends FXMLLoginController implements Initializable {
    @FXML
    public Text contactStaffText;
    private String roomIdSQl;
     private String descriptionSql;
    private String conId;
    @FXML
    Text metroText;
    @FXML
    public ImageView gymIcon;
    @FXML
    public ImageView poolIcon;
    @FXML
    public ImageView parkIcon;
    @FXML
    public Label facilities;
    @FXML
    public Label roomIdLabel;
    @FXML
    public Label typeLabel;
    @FXML
    public Label locationLabel;
    @FXML
    public Label metroLabel;
    @FXML
    public Label sqrLabel;
    @FXML
    public Label condoNameLabel;
    @FXML
    public Label bedroomLabel;
    @FXML
    public Label bathroomLabel;
    @FXML
    public Text locationText;
    private Connection con = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("SQL Room " + roomIdSQl);
        con = MyConnection.getConnection();
        String sql = "SELECT  distinct r.roomId,c.condoName,r.condoId,r.price,t.nType,a.city,r.bedrooms,r.bathrooms,r.sqMeters,\n"
                + "c.parking,c.fitness,c.swimming,r.detail FROM room r\n"
                + "left JOIN condo c ON\n"
                + "r.condoId = c.condoId\n"
                + "left JOIN area a ON\n"
                + "c.areaId = a.areaId\n"
                + "left JOIN roomType t ON\n"
                + "r.typeId = t.typeId\n"
                + "left join metro m ON\n"
                + "a.areaId = m.areaId\n"
                + "LEFT JOIN staff s ON c.staffId = s.staffId \n"
                + "WHERE r.roomId=? and r.condoId=?";
       
        
        try {
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, roomIdSQl);
            System.out.println("condo id" + roomIdSQl);
            pstm.setString(2, conId);
            ResultSet rs = pstm.executeQuery();
            rs.next();
            sqrLabel.setText((rs.getInt("r.sqMeters") != 0) ? rs.getInt("r.sqMeters") + "" : "-");
            roomIdLabel.setText((rs.getString("r.roomId") != null) ? rs.getString("r.roomId") : "-");
            typeLabel.setText((rs.getString("t.nType") != null) ? rs.getString("t.nType") : "-");
            locationText.setText((rs.getString("a.city") != null) ? rs.getString("a.city") : "-");
            condoNameLabel.setText((rs.getString("c.condoName") != null) ? rs.getString("c.condoName") : "-");
            //sqrLabel.setText(rs.getInt("r.sqMeters")+"");
            String test = null;
            bathroomLabel.setText((test == null) ? rs.getString("r.bathrooms") : "-");
            bedroomLabel.setText((rs.getInt("r.bedrooms") != 0) ? rs.getInt("r.bedrooms") + "" : "-");
            bathroomLabel.setText((rs.getInt("r.bathrooms") != 0) ? rs.getInt("r.bathrooms") + "" : "-");
            if (rs.getInt(10) == 1) {
                Image park = new Image("login/image/parking.png");
                parkIcon.setPreserveRatio(true);
                parkIcon.setImage(park);
            }
            if (rs.getInt(11) == 1) {
                Image gym = new Image("login/image/gymbig.jpg");
                gymIcon.setPreserveRatio(true);
                gymIcon.setImage(gym);
            }
            if (rs.getInt(12) == 1) {
                Image swim = new Image("login/image/pool.jpg");
                poolIcon.setPreserveRatio(true);
                poolIcon.setImage(swim);
            }
            System.out.println("fukit");
            System.out.println(conId);
            System.out.println(roomIdSQl);
             String sqlStaff = "SELECT staffFirstName,staffLastName,staffPhone FROM room r\n"
                + "LEFT JOIN condo c ON r.condoId = c.condoId\n"
                + "LEFT JOIN staff s ON c.staffId = s.staffId\n"
                + "WHERE r.roomId=? and r.condoId=?";
            //write staff detail
          pstm=con.prepareStatement(sqlStaff);
            pstm.setString(1, roomIdSQl);
            pstm.setString(2, conId);
            rs = pstm.executeQuery();
            if(rs.next()){
            String staffTxt = ((rs.getString(1) != null) ? rs.getString(1) : "-")+" "+((rs.getString(2) != null) ? rs.getString(2) : "-")
                    +((rs.getString(3) != null) ? rs.getString(3) : "-");//(rs.getString(1)!=null)?rs.getString(1):"-";
//            metroText.setText((rs.getString(1)!=null)?rs.getString(1):"-");
            System.out.println("Staff Debug" + staffTxt);
            contactStaffText.setText(staffTxt);
            //metroText.setText(metroTxt);
            while (rs.next()) {
//                metroTxt=(rs.getString(1)!=null)?rs.getString(1):"-";
                staffTxt += ","+((rs.getString(1) != null) ? rs.getString(1) : "-")+" "+((rs.getString(2) != null) ? rs.getString(2) : "-")
                    +((rs.getString(3) != null) ? rs.getString(3) : "-");
                System.out.println("!!! while " + staffTxt);
                contactStaffText.setText(staffTxt);

                //metroText.setText(metroText.getText()+", "+ ((rs.getString(1)!=null)?rs.getString(1):"-" ));
            }
        }
            pstm = con.prepareStatement("SELECT m.metroName  FROM room r\n"
                    + "left JOIN condo c ON\n"
                    + "r.condoId = c.condoId\n"
                    + "left JOIN area a ON\n"
                    + "c.areaId = a.areaId\n"
                    + "left  JOIN metro m ON\n"
                    + "a.areaId = m.areaId\n"
                    + "WHERE c.condoId=? AND r.roomId=? ");
            pstm.setString(1, conId);
            pstm.setString(2, roomIdSQl);
            rs = pstm.executeQuery();
            rs.next();
            String metroTxt = (rs.getString(1) != null) ? rs.getString(1) : "-";//(rs.getString(1)!=null)?rs.getString(1):"-";
//            metroText.setText((rs.getString(1)!=null)?rs.getString(1):"-");
            System.out.println("104debug" + metroTxt);
            metroText.setText(metroTxt);
            //metroText.setText(metroTxt);
            while (rs.next()) {
//                metroTxt=(rs.getString(1)!=null)?rs.getString(1):"-";
                metroTxt += (rs.getString(1) != null) ? rs.getString(1) : "-";
                System.out.println("!!! while " + metroTxt);
                metroText.setText(metroTxt);

                //metroText.setText(metroText.getText()+", "+ ((rs.getString(1)!=null)?rs.getString(1):"-" ));
            }

            // ((Text)detailPage.getChildren().get(4)).setText(rs.getString("r.detail"));//เขียนข้อมูลคอนโด
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDetailPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public FXMLHorizonBarController() {

    }

    public void setSQLRoomId(String roomId) {
        this.roomIdSQl = roomId;
    }

    public void setSQLCondoId(String condoId) {
        this.conId = condoId;
    }

}
