/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.blockDisplay;

import Animation.Fade;
import LoginPermission.MyConnection;
import com.jfoenix.controls.JFXScrollPane;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import login.FXMLLoginController;

/**
 * FXML Controller class
 *
 *
 * @author Ivora
 */
public class FXMLPaginationController extends FXMLLoginController implements Initializable {
//    private Pagination page;

    @FXML
    private Pagination pagination;
    private String sqlCountContentNumber;//sql สำหรับใส่มาจากอีก controller ให้นับจำนวนห้อง
    private String sqlSelectRoomToDisplay;//sql สำหรับใส่มาจากอีกหน้าแสดงรายละเอียดของข้อมูล

    public int itemsPerPage() {
        return 1;
    }
    /**
     * Initializes the controller class.
     */
    private double numberOfRow = 17.0;
    private int contentPerPage = 5;
    //เราจะเขียนลง page[0] หมายถึงหน้าที่ 1 นั้นมี content เนื้อหาอะไรบ้างนั่นเอง
    public ArrayList<ScrollPane> page = new ArrayList<ScrollPane>();

    public FXMLPaginationController() {

    }

    public FXMLPaginationController(int numberOfRow) {
        this.numberOfRow = numberOfRow;
    }

    public void setSqlCountContentNumber(String sql) {
        this.sqlCountContentNumber = sql;
    }

    public void setSqlContentToDisplay(String sql) {
        this.sqlSelectRoomToDisplay = sql;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Connection con = MyConnection.getConnection();
        PreparedStatement pstm = null;
//        numberOfRow = 0.0;
        contentPerPage = 5;//ตั้งจำนวนข้อมูลที่จะแสดงต่อหน้านึง
        //จำนวนของหน้าก็คือขึ้นกับสูตรของ content ที่เราตั้งเอาไว้เช่นมีหน้านึงมี 5 ข้อมูลแสดงว่าต้อง หารด้วย 5 แล้วปัดขึ้น
        //ตัวอย่างการทำงานเช่น 3/5 =0.xx เราก็ปัดขึ้นเป็น 1 เราจะได้มา 1 หน้า
        int numberOfPage = 0;
        try {
            //เราาต้อง reuse code ตรงนี้คือหมายความว่ามีการใช้ method ซ้ำได้
            pstm = con.prepareStatement(sqlCountContentNumber);
            //pstm = con.prepareStatement("select count(*) from condo");
            ResultSet rs = pstm.executeQuery();
            rs.next();
            // numberOfRow = 18;
            numberOfRow = rs.getInt(1) + 0.0;
            System.out.println("Debug สำหรับทำ OOP ผ่าน Controller : " + rs.getInt(1));
            //ไดด้จำนวนข้อมูลที่จะมาสร้างหน้าแล้วก็ต้องเข้าสูตรที่ใชในการสร้่างหน้านั่นเอง
            if (numberOfRow % contentPerPage == 0) {
                numberOfPage = (int) (numberOfRow / contentPerPage);
            } else {
                numberOfPage = (int) (Math.floor((numberOfRow / contentPerPage) + 1));
            }
            pagination.setPageCount(numberOfPage);
            //เีขยนข้อมูลก็ต้องบนหน้านี้้เหมือนกันโดยดึงมาจาก preparedStatement
            TilePane temptTile = new TilePane();
            temptTile.setPrefColumns(1);
            //tile.setPrefRows(SLIDE_FREQ);
            temptTile.setPadding(new Insets(5, 0, 5, 0));
            temptTile.setVgap(4);
            temptTile.setHgap(4);
            //เขียนในแต่ล่ะรอบว่าให้เขียนเนื้อหาอะไรลง block
            pstm = con.prepareStatement(sqlSelectRoomToDisplay);
            rs = pstm.executeQuery();
            rs.next();//ต้อง next ก่อนครั้งนึง
            for (int i = 0, j = 0; i < numberOfRow; i++) {
                //ทุกๆครั้งที่เขียนครบตาม contentPerPage ต้องขึ้นหน้าใหม่
                //i = จำนวนข้อมูลทั้งหมด ถ้าหารลงตัวการ contentPerPage แสดงว่าต้องขึ้นหน้าใหม่แล้ว, j = จำนวนหน้า
                if (i != 0 && i % contentPerPage == 0) {//หารลงตัวต้องขึ้นหน้าใหม่ แต่ !!! 0 มันลงตัวในครั้งแรกเลยต้องดักเอาไว้ก่อนนั่นเอง
                    System.out.println("จำนวน Tile ที่ใส่ลง ArrayList : " + (j + 1));
                    System.out.println("ข้อมูลชิ้นที่ : " + i);
                    page.add(new ScrollPane(temptTile));
                    temptTile = new TilePane();
                    temptTile.setPrefColumns(1);
                    //tile.setPrefRows(SLIDE_FREQ);
                    temptTile.setPadding(new Insets(5, 0, 5, 0));
                    temptTile.setVgap(4);
                    temptTile.setHgap(4);
                    j++;
                }
                FXMLLoader fxmlGrid = new FXMLLoader();
                fxmlGrid.setLocation(FXMLPaginationController.class.getResource("FXMLBlockDisplay.fxml"));
                FXMLBlockDisplayController blockController = new FXMLBlockDisplayController();
                blockController.setSqlCondoRoomId(rs.getString("roomId"));
                blockController.setSqlCondoId(rs.getString("condoId"));
                fxmlGrid.setController(blockController);
                //GridPane tempGrid = FXMLLoader.load(getClass().getResource("FXMLBlockDisplay.fxml"));
                GridPane tempGrid = fxmlGrid.load();
                try {
                    //ดึงข้อมูลจาก database มาเขียนีั            
                    System.out.println("WTF Test 1 ! ");
                    ((Label) (tempGrid.getChildren().get(0))).setText((rs.getDouble("r.price")!=0.0)?rs.getDouble("r.price")+"":"-");
                    System.out.println("WTF Test 2 ! " + tempGrid.getChildren().get(1));
                    ((Label) (tempGrid.getChildren().get(1))).setText((rs.getString("city")!=null)?rs.getString("city"):"-");
                    System.out.println("WTF Test 3 ! " + tempGrid.getChildren().get(2));
                    String bed=(rs.getInt("r.bedrooms") != 0) ? rs.getInt("r.bedrooms") + "" : "-";
                    String bath=(rs.getInt("r.bathrooms") != 0) ? rs.getInt("r.bathrooms") + "" : "-";
                    ((Label) (tempGrid.getChildren().get(2))).setText("Condo/" + bed+ "bed/" + bath + "bath");
                    System.out.println("WTF Test 4 ! " + tempGrid.getChildren().get(3));//ราคาเช่าต่อเดือน
                    // ((Label) (tempGrid.getChildren().get(3))).setText(rs.getString(2));//ราคาเช่าต่อเดือน
                    //System.out.println("WTF Test 5 ! " + tempGrid.getChildren().get(4));
                    //((Label) (tempGrid.getChildren().get(4))).setText(rs.getString(2));
                    //System.out.println("WTF Test 6 ! " + tempGrid.getChildren().get(5));
                    //System.out.println(((Text) (tempGrid.getChildren().get(5))).wrappingWidthProperty());
                    String detailSet = (rs.getString("detail")!=null)?rs.getString("detail"):"-";
                    detailSet = (detailSet.length() > 185) ? detailSet.substring(0, 185) + " [More...]" : detailSet;
                    ((Text) (tempGrid.getChildren().get(5))).setText(detailSet);
                    InputStream in = null;
                    ImageView image = null;

                    java.sql.Blob blob = rs.getBlob("p.picture");
                    if (blob != null) {
                        in = blob.getBinaryStream();
                        image = new ImageView(new Image(in));

                    } else {
                        System.out.println("picture is null :");
                        image = new ImageView(new Image("login/image/notfoundImg.png"));
                    }
                    image.setFitWidth(150);
                    image.setPreserveRatio(true);
                    tempGrid.add(image, 0, 2);
                    System.out.println("Add Data ไปกี่ครั้ง" + sqlSelectRoomToDisplay);
                    rs.next();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                //  ImageView image = new ImageView("login/image/c1.jpg");

//                PreparedStatement pic = con.prepareStatement("select picture from picture where picId=?");
//                ResultSet rsp = pic.executeQuery();
//                rsp.next();
                //System.out.println(GridPane.get);
                temptTile.getChildren().add(tempGrid);
            }

            page.add(new ScrollPane(temptTile));

        } catch (SQLException ex) {
            Logger.getLogger(FXMLPaginationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLPaginationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("จำนวนข้อมูลที่จะดึงออกมา : " + numberOfRow);
        System.out.println("จำนวนหน้าที่จะสร้าง : " + numberOfPage);

        //ใช้ในการกดปุ่มเปลี่ยนหน้าโดยเวลากดเลขหน้าจะเข้าไป แล้วมันจะไปดึง Node ที่เราเตรียมไว้ในใส้ออกมานั่นเอง
        pagination.setPageFactory(new Callback<Integer, Node>() {
            //ทำการเขียน Anoonymous Class ข้างในไปเลยเพื่อจะใช้ method สร้างหน้า
            //method Call มีการ new Object ทุกครั้งที่สร้างหน้าใหม่ซึ่งดูแล้วเปลือง memory จริงๆควรจะทำ ArrayList
            //เอาไว้ดักเวลากดหน้าเช่นกดหน้า 1 ก็จำเม็มข้อมูลหน้า 1 ไว้แล้ววันหลังก็เอาไปใช้ได้เลย แต่ขอพักไว้ก่อน ไว้ทำตอนว่างล่ะกันนะจ้ะ
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex >= page.size()) {
                    return null;
                } else {
                    pageCallBack = pageIndex;//บันทึก state ไว้ว่ากดมาจากหน้าไหน
                    System.out.println("New Object !!!");
                    System.out.println("หน้าที่จะไป : " + pageIndex);
                    return page.get(pageIndex);//createPage(pageIndex);
                }
            }

            private Node createPage(Integer pageIndex) {
                /*การทำงานก็คือเราเห็นแล้วว่ามันมี Node หลายตัวมากๆถ้าแค่กดจิ้มวาปไปหน้านั้นเฉยๆมันไม่พอ
                แต่เราต้องทำให้มันจำได้ดว้ยว่าหน้าที่มันไปนี้เนี่ยมีข้อมูลอะไรอยู่ในั้นแล้วฉะนั้นครั้งแรกเลยคือเราต้องวน loop 
                ดึงข้อมูลออกมาให้หมดก่อนแล้วไปยัดลง TilePane ซึ่งเปรียบเสมือนหน้าหนึ่งตัวอย่างเช่น
                TilePane หนึ่งมีด้วยกันคือ 5 Content นั่นเอง แล้วนำ TilePane นี้เก็บลง ArrayList<TilePane>
                แล้วพอเรากดปุปเราก็แค่ดึงข้อมูลออกมาได้เลย
                Gridpane content 5 ตัว >> TilePane contentPerPage >> ArrayList<TilePane> page  
                 */
                TilePane tile = new TilePane();
                tile.setPrefColumns(1);
                //tile.setPrefRows(SLIDE_FREQ);
                tile.setPadding(new Insets(5, 0, 5, 0));
                tile.setVgap(4);
                tile.setHgap(4);
                JFXScrollPane scrollPane = null;
                try {//วน loop ดึงข้อมูล block จาก db มาใช้นั่นเองซึ่ง block dispaly ก็จะแสดงรายละเอียดต่างๆ
                    for (int i = 0; i < contentPerPage; i++) {
                        tile.getChildren().add(FXMLLoader.load(getClass().getResource("FXMLBlockDisplay.fxml")));
                    }
                    //ดึง scrolll pane สำหรับเลื่อนลงมาแล้วเซ็ทตรงกลาง
                    scrollPane = FXMLLoader.load(getClass().getResource("FXMLScrollPane.fxml"));
                    System.out.println(getClass().getResource("FXMLScrollPane.fxml").getFile());
                    scrollPane.setContent(tile);
                    //Parent middle = FXMLLoader.load(getClass().getResource("condoForSale/FXMLCondoForSale.fxml"));
                    //mainView.setCenter(middle);
                    System.out.println("กดโหลด scrollpane สำเร็จ!");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                return scrollPane;
            }

        });
        //หลังจาก Render ทุกอย่างเสร็จแล้วเราจะเก็บ Stage ของหน้าเราเอาไว้ว่าค้างอยู่ที่หน้าไหนซึ่งจะเป็น Static ใช้มุกเดียว ตัวก่อน 
        //legacyMainView นั่นเอง อันนี้จะเป็น legacyPagination

        Fade.setFadeIn(pagination);

    }

    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        try {
            ObservableList<Node> childrens = gridPane.getChildren();
            for (Node node : childrens) {
                if (node != null) {
                    System.out.println("not null for node child");
                    if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                        System.out.println("yes found!");
                        result = node;
                        break;
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

}
