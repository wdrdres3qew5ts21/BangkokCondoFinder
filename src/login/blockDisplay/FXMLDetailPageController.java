/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.blockDisplay;

import LoginPermission.MyConnection;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import login.FXMLLoginController;
import static login.FXMLLoginController.pageCallBack;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLDetailPageController extends FXMLLoginController implements Initializable {

    Pagination page = null;
    //int pageCallBack=0;
    @FXML
    private GridPane detailPage;
    @FXML
    private ImageView imageView;
    @FXML
    private Label priceLabel;
    private String sqlCondoId;
    private String sqlRoomId;
    @FXML
    private JFXButton backButton;
    private FXMLLoader paginationCallBack;//บันทึก Object ของ Pagination เอาไว้เพื่อจะได้ย้อนหน้ากลับไปได้
    Connection con = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //ดึงข้อมูลจากระบบมาเขียน
        System.out.println("SQL Room " + sqlRoomId);
        con = MyConnection.getConnection();
        String sql = "SELECT  distinct r.roomId,p.picture,r.typeId,r.condoId,r.price,t.nType,a.city,r.bedrooms,r.bathrooms,r.sqMeters,\n"
                + "c.parking,c.fitness,c.swimming,r.detail FROM room r\n"
                + "left JOIN condo c ON\n"
                + "r.condoId = c.condoId\n"
                + "left JOIN area a ON\n"
                + "c.areaId = a.areaId\n"
                + "left JOIN roomType t ON\n"
                + "r.typeId = t.typeId\n"
                + "left join metro m ON\n"
                + "a.areaId = m.areaId\n"
                + "left JOIN picture p ON\n"
                + "r.picId=p.picId\n"
                + "WHERE r.roomId=? and r.condoId=?";
        try {
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, sqlRoomId);
            System.out.println("condo id" + sqlCondoId);
            pstm.setString(2, sqlCondoId);
            ResultSet rs = pstm.executeQuery();
            rs.next();
            System.out.println(detailPage.getChildren().get(0));//รูปคอนโด 
            String type="";
            if(rs.getInt("r.typeId")==1){
                type="SALE/THB";
            }
            else{
                type=" RENT/THB";
            }
            setPrice(rs.getDouble("r.price") +type);
            InputStream in = null;
            Image image = null;
            java.sql.Blob blob = rs.getBlob("p.picture");
            if (blob != null) {
                in = blob.getBinaryStream();
                image = new Image(in);
            } else {
                System.out.println("picture is null :");
                image = new Image("login/image/notfoundImg.png");
            }
            setPictureTitle(image);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDetailPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPrice(String price) {
        this.priceLabel.setText(price + "");
    }

    public String getSqlCondoId() {
        return sqlCondoId;
    }

    public void setPictureTitle(Image in) {
        this.imageView.setImage(in);
        this.imageView.setFitWidth(250);
        this.imageView.setPreserveRatio(true);
    }

    public void setSqlCondoId(String sqlCondoId) {
        this.sqlCondoId = sqlCondoId;
    }

    public FXMLDetailPageController() {

    }

    /**
     * เรามี Method นี้ไว้จำว่าเราจะส่ง roomId
     * อันไหนเข้ามาเพื่อจะได้ดึงข้อมูลมูลมาแสดงในหน้านี้
     *
     * @param sql
     */
    public void setSQLRoomId(String sql) {
        this.sqlRoomId = sql;
    }

    public int getPageCallBack() {
        return pageCallBack;
    }

    public void setPageCallBack(int pageCallBack) {
        this.pageCallBack = pageCallBack;
    }

    @FXML
    private void backButtonOnClick(ActionEvent event) {
        System.out.println("Page Call Back : " + pageCallBack);
        page = legacyPage;
        legacyMainView.setCenter(page);
        System.out.println("กดปุ่ม !!!");
    }

}
