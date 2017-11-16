/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.blockDisplay;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import login.FXMLLoginController;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLBlockDisplayController extends FXMLLoginController implements Initializable {

    String sqlCondoRoomId = null;
    //ต้องจำ Stage เอาไว้ว่าสมัยก่อนเรามาจากไหนจะได้ย้อนกลับไปที่หน้านั้นได้เวลากดปุ่มย้อนกลับ
    //int pageCallBack = 0;
    @FXML
    private GridPane blockDisplayPane;
    @FXML
    private Label condoAttributeLabel;
    @FXML
    private Label areaLabel;
    @FXML
    private Label salePriceLabel;
    @FXML
    private Label rentPriceLabel;
    @FXML
    private JFXButton viewMoreButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public FXMLBlockDisplayController() {

    }

    //เก็บรายละเอียด detail ของ sql ที่จะใช้เอาไปสร้าง
    public String getSqlCondoRoomId() {
        return sqlCondoRoomId;
    }

    public void setSqlCondoRoomId(String sqlCondoRoomId) {
        this.sqlCondoRoomId = sqlCondoRoomId;
        System.out.println("Set ID : " + sqlCondoRoomId);
    }

    public void setPageCallBack(int callBack) {
        this.pageCallBack = callBack;
    }

    public int getPageCallBack() {
        return this.pageCallBack;
    }

    @FXML
    private void viewButtonOnClick(ActionEvent event) {
        FXMLLoader detailPage = new FXMLLoader();
        detailPage.setLocation(FXMLDetailPageController.class.getResource("FXMLDetailPage.fxml"));
        FXMLDetailPageController detailPageControll = new FXMLDetailPageController();
        //ส่ง roomId จากที่โหลดเข้ามาตอน FXMLPagination 
        detailPage.setController(detailPageControll);
        detailPageControll.setSQLRoomId(sqlCondoRoomId);         
        System.out.println("กดปุ่ม view !!!");
        //-----------------------------------------------------------------------------------------     
        try {
            GridPane detail = detailPage.load();
            System.out.println();
            if (detail != null) {
                System.out.println("not null scrollpane");
                TilePane tile = new TilePane();
                tile.setPrefColumns(1);
                //tile.setPrefRows(SLIDE_FREQ);
                tile.setPadding(new Insets(5, 0, 5, 0));
                tile.setVgap(4);
                tile.setHgap(4);
                tile.getChildren().add(detail);
             
                tile.getChildren().add(FXMLLoader.load(getClass().getResource("FXML.fxml")));
                legacyMainView.setCenter(new ScrollPane(tile));
            }
            System.out.println("null scrollpane");

        } catch (IOException ex) {
            Logger.getLogger(FXMLDetailPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("กดปุ่ม !!!");
    }
}
