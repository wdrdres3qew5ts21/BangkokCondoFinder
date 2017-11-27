/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import Animation.Fade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import LoginPermission.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTabPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import javax.swing.JButton;
import login.blockDisplay.FXMLPaginationController;

/**
 *
 * @author Ivora
 */
public class FXMLLoginController extends LoginPermission implements Initializable {

    public String leftSqlSearch = "";
    private String currentPage = "home";
    private final double IMG_WIDTH = 600;
    private final double IMG_HEIGHT = 300;
    public static int pageCallBack = 0;
    private final int NUM_OF_IMGS = 3;
    private final int SLIDE_FREQ = 4; // in secs
    //ทุกอย่างต้องมี @FXML ถ้าเกิดเป็น โค้ดที่มาจาก JavaFX แล้วก็อย่าลืมดู controller fx:controller คือตัวที่จะโยงมายัง
    //หน้า controlller class javaของเรา
    private TextField nameTextField;
    private Label nameLabel;
    @FXML
    private StackPane backgroundStackPane;
    @FXML
    public BorderPane mainView;
    @FXML
    private JFXButton homeButton;
    @FXML
    private JFXButton condoSaleButton;
    @FXML
    private JFXButton condoRentButton;
    @FXML
    private JFXButton allCondoButton;
    public static BorderPane legacyMainView;//BackUp mainViewเอาไว้
    public static Pagination legacyPage;//backUp page ไว้กรณีเรากดเปลี่ยนหน้า

    @Override //การทำงานคือ inititalize() จะถูกมาเรียกก่อน
    public void initialize(URL url, ResourceBundle rb) {
        MyConnection.getConnection();
        try {
            // TODO
            // callHome();

            mainView.setLeft(FXMLLoader.load(getClass().getResource("LeftSearchBar/FXMLLeftSearchBar.fxml")));
            legacyMainView = mainView;//ยัดค่า mainView ลงนี้เพื่อ backup
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //setStyle() เพื่อเข้าไปตั้งค่า css จากใน controller ได้นั่นเอง
        //  backgroundStackPane.setStyle("-fx-background-image: url(\"http://gematsu.com/wp-content/uploads/2016/12/Blue-Reflection-Christmas-2016-Wallpaper.jpg\");");
    }

    private void clickButtonAction(ActionEvent event) {
        System.out.println("Click!");
        nameLabel.setText(nameTextField.getText());
        callLogin();
    }

    @FXML //กดปุ่ม Home เมนูหลัก
    private void homeActionListener(ActionEvent event) {
        currentPage = "home";
        //callHome();
        //  mainView.setStyle("-fx-background-image: url(\"http://gematsu.com/wp-content/uploads/2016/12/Blue-Reflection-Christmas-2016-Wallpaper.jpg\");");

    }

    public void leftSearchBar(String area, String metro, int bedroom, int minPrice, int maxPrice, String type) {
        FXMLLoader fxmlAll = new FXMLLoader();
        fxmlAll.setLocation(FXMLLoginController.class.getResource("blockDisplay/FXMLPagination.fxml"));
        FXMLPaginationController allController = new FXMLPaginationController();
        System.out.println("Count Content todisplay LeftBAr!!!");
        if (!type.equals("'_%'")) {
            System.out.println("SELECT  count(*) FROM room r\n"
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
                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n"
                    + "AND t.nType=" + type);
            System.out.println("--------------------------------------------------------------------");
            allController.setSqlCountContentNumber("SELECT  count(*) r\n"
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
                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n"
                    + "AND t.nType=" + type);
            System.out.println("Content todisplay LeftBAr!!!");
            allController.setSqlContentToDisplay("SELECT  * FROM room r\n"
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
                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n"
                    + "AND t.nType=" + type);
            System.out.println("Left Search Passs!!!");
            fxmlAll.setController(allController);
        } else {
            System.out.println("All item");
            System.out.println("SELECT  count(*) FROM room r\n"
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
                + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
                + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n");
            System.out.println("-------------------------------------------------------------------");
            allController.setSqlCountContentNumber("SELECT count(*) FROM room r\n"
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
                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n");
            allController.setSqlContentToDisplay("SELECT  * FROM room r\n"
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
                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n");
            fxmlAll.setController(allController);
        }
//        fxmlAll.setController(allController);

        try {
            System.out.println("Debug Main !!!!!!"+" DEbug!!!");
            System.out.println(mainView.toString());
            mainView.setCenter(fxmlAll.load());
            legacyPage = (Pagination) mainView.getCenter();
            System.out.println("Legacy DEbug" + legacyPage);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

//        FXMLLoader fxmlAll = new FXMLLoader();
//        fxmlAll.setLocation(FXMLLoginController.class.getResource("blockDisplay/FXMLPagination.fxml"));
//        FXMLPaginationController allController = new FXMLPaginationController();
//        System.out.println("SELECT  count(*) FROM room r\n"
//                + "left JOIN condo c ON\n"
//                + "r.condoId = c.condoId\n"
//                + "left JOIN area a ON\n"
//                + "c.areaId = a.areaId\n"
//                + "left JOIN roomType t ON\n"
//                + "r.typeId = t.typeId\n"
//                + "left join metro m ON\n"
//                + "a.areaId = m.areaId\n"
//                + "left JOIN picture p ON\n"
//                + "r.picId=p.picId\n"
//                + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
//                + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n"
//                + "AND t.nType=" + type);
//        System.out.println("Count Content todisplay LeftBAr!!!");
//        if (!type.equals("'_%'")) {
//            System.out.println("Some hing thype!!!");
//            allController.setSqlCountContentNumber("SELECT  distinct c.condoName,r.roomId,p.picture,r.typeId,r.condoId,r.price,t.nType,a.city,r.bedrooms,r.bathrooms,r.sqMeters,\n"
//                    + "c.parking,c.fitness,c.swimming,r.detail FROM room r\n"
//                    + "left JOIN condo c ON\n"
//                    + "r.condoId = c.condoId\n"
//                    + "left JOIN area a ON\n"
//                    + "c.areaId = a.areaId\n"
//                    + "left JOIN roomType t ON\n"
//                    + "r.typeId = t.typeId\n"
//                    + "left join metro m ON\n"
//                    + "a.areaId = m.areaId\n"
//                    + "left JOIN picture p ON\n"
//                    + "r.picId=p.picId\n"
//                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
//                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n"
//                    + "AND t.nType=" + type);
//            System.out.println("Content todisplay LeftBAr!!!");
//            allController.setSqlContentToDisplay("SELECT  distinct c.condoName,r.roomId,p.picture,r.typeId,r.condoId,r.price,t.nType,a.city,r.bedrooms,r.bathrooms,r.sqMeters,\n"
//                    + "c.parking,c.fitness,c.swimming,r.detail FROM room r\n"
//                    + "left JOIN condo c ON\n"
//                    + "r.condoId = c.condoId\n"
//                    + "left JOIN area a ON\n"
//                    + "c.areaId = a.areaId\n"
//                    + "left JOIN roomType t ON\n"
//                    + "r.typeId = t.typeId\n"
//                    + "left join metro m ON\n"
//                    + "a.areaId = m.areaId\n"
//                    + "left JOIN picture p ON\n"
//                    + "r.picId=p.picId\n"
//                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
//                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n"
//                    + "AND t.nType=" + type);
//            System.out.println("Left Search Passs!!!");
//            fxmlAll.setController(allController);
//        } else {
//            System.out.println("All item");
//            allController.setSqlCountContentNumber("SELECT count(*) FROM room r\n"
//                    + "left JOIN condo c ON\n"
//                    + "r.condoId = c.condoId\n"
//                    + "left JOIN area a ON\n"
//                    + "c.areaId = a.areaId\n"
//                    + "left JOIN roomType t ON\n"
//                    + "r.typeId = t.typeId\n"
//                    + "left join metro m ON\n"
//                    + "a.areaId = m.areaId\n"
//                    + "left JOIN picture p ON\n"
//                    + "r.picId=p.picId\n"
//                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
//                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n");
//            System.out.println("Content todisplay LeftBAr!!!");
//            allController.setSqlContentToDisplay("SELECT  distinct c.condoName,r.roomId,p.picture,r.typeId,r.condoId,r.price,t.nType,a.city,r.bedrooms,r.bathrooms,r.sqMeters,\n"
//                    + "c.parking,c.fitness,c.swimming,r.detail FROM room r\n"
//                    + "left JOIN condo c ON\n"
//                    + "r.condoId = c.condoId\n"
//                    + "left JOIN area a ON\n"
//                    + "c.areaId = a.areaId\n"
//                    + "left JOIN roomType t ON\n"
//                    + "r.typeId = t.typeId\n"
//                    + "left join metro m ON\n"
//                    + "a.areaId = m.areaId\n"
//                    + "left JOIN picture p ON\n"
//                    + "r.picId=p.picId\n"
//                    + "WHERE a.city LIKE " + area + " AND m.metroName LIKE " + metro + " AND r.bedrooms >=" + bedroom + " AND\n"
//                    + "r.price BETWEEN " + minPrice + " AND " + maxPrice + "\n");
//            System.out.println("Left Search Passs!!!");
//            fxmlAll.setController(allController);
//        }
////        fxmlAll.setController(allController);
//
//        try {
//            mainView.setCenter(fxmlAll.load());
//            legacyPage = (Pagination) mainView.getCenter();
//            System.out.println("Legacy DEbug" + legacyPage);
//        } catch (IOException ex) {
//            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void loadSQLSearch() {

    }

    @FXML //กดปุ่ม SaleCo//ndo
    private void saleActionListener(ActionEvent event) {
        //where type = ?
        currentPage = "sale";
        callCondoForSale();

    }

    @FXML //กดปุ่ม RentCondo
    private void rentActionListener(ActionEvent event) {
        currentPage = "rent";
        callCondoForRent();
    }

    @FXML //กดปุ่มเรียกAll 
    private void allActionListener(ActionEvent event) {
        FXMLLoader fxmlAll = new FXMLLoader();
        fxmlAll.setLocation(FXMLLoginController.class.getResource("blockDisplay/FXMLPagination.fxml"));
        FXMLPaginationController allController = new FXMLPaginationController();
        allController.setSqlCountContentNumber("select count(*) from room");
        allController.setSqlContentToDisplay("SELECT  * FROM room r\n"
                + "left JOIN condo c ON\n"
                + "r.condoId = c.condoId\n"
                + "left JOIN area a ON\n"
                + "c.areaId = a.areaId\n"
                + "left JOIN picture p ON\n"
                + "r.picId=p.picId");
        fxmlAll.setController(allController);

        try {
            mainView.setCenter(fxmlAll.load());
            legacyPage = (Pagination) mainView.getCenter();
            System.out.println("Legacy DEbug" + legacyPage);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void callHome() {
        try {
            mainView.setCenter(FXMLLoader.load(getClass().getResource("home/FXMLHome.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //fx:controller="login.blockDisplay.FXMLPaginationController"
    @FXML
    public void callCondoForRent() {
        //สำคัญโคตรๆ ! ใช้วิธีนี้ในการแก้ปัญหา jave reflection
        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(FXMLLoginController.class.getResource("blockDisplay/FXMLPagination.fxml"));
        FXMLPaginationController rentController = new FXMLPaginationController();
        rentController.setSqlCountContentNumber("select count(*) from room where typeId=2 ");
        rentController.setSqlContentToDisplay("SELECT  * FROM room r\n"
                + "left JOIN condo c ON\n"
                + "r.condoId = c.condoId\n"
                + "left JOIN area a ON\n"
                + "c.areaId = a.areaId\n"
                + "left JOIN picture p ON\n"
                + "r.picId=p.picId\n"
                + "where typeId=2");
        fxml.setController(rentController);
        try {
            mainView.setCenter(fxml.load());
//            fxml.setController(new FXMLPaginationController());
            //ค
            legacyPage = (Pagination) (mainView.getCenter());
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //เรียก GUI Login Method
    public void callLogin() {

        Connection con = MyConnection.getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = con.prepareStatement("SELECT * FROM user JOIN status\n"
                    + "ON user.statusId = status.statusId");
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.setTitle("Login Authentication");
            dialog.setHeaderText("Welcome To BangkokCondoFinder");
            // Set the icon (must be included in the project).
            //dialog.setGraphic(new ImageView(this.getClass().getResource("image/login.png").toString()));
            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField username = new TextField();
            username.setPromptText("Username");
            PasswordField password = new PasswordField();
            password.setPromptText("Password");
            Button register = new Button("Register");
            grid.add(new Label("Username:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Password:"), 0, 1);
            grid.add(password, 1, 1);

            grid.add(register, 1, 2);
            register.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Window window = dialog.getDialogPane().getScene().getWindow();
                    window.hide();
                    FXMLLoader register = new FXMLLoader();
                    register.setLocation(FXMLLoginController.class.getResource("FXMLRegister.fxml"));
                    AnchorPane registerPane = null;
                    try {
                        registerPane = register.load();
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    mainView.setCenter(new ScrollPane(registerPane));
                    Fade.setFadeIn(mainView.getCenter());
                }
            });
            // Enable/Disable login button depending on whether a username was entered.
            Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
            loginButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            username.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });
            dialog.getDialogPane().setContent(grid);
            // Request focus on the username field by default.
            Platform.runLater(() -> username.requestFocus());
            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(username.getText(), password.getText());
                }
                return null;
            });
            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(usernamePassword -> {
                System.out.println(usernamePassword.getKey() + usernamePassword.getValue());
                Alert alert = new Alert(AlertType.ERROR);
                alert.initStyle(StageStyle.UNDECORATED);
                if (authorizationLogin(usernamePassword.getKey(), usernamePassword.getValue())) {
                    alert = new Alert(AlertType.INFORMATION);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setHeaderText("Login Success Welcome Back " + getUserName());
                    alert.setContentText("I have a great message for you!");
                    alert.showAndWait();
                    //ถ้า สogin ผ่านก็จะเข้ามาในนี้เลย
                    //จะเปลี่ยน Icon login เป็นสถานะเขา
                    //ถ้าเกิดเป็น Addmin จะวาปทุกหน้าออกไปให้หมดเลย
                    if (getStatus() == 1) {//admin
                        //mainView.getLeft().setVisible(false);
                        FXMLLoader fxmlAdmin = new FXMLLoader();
                        fxmlAdmin.setLocation(FXMLLoginController.class.getResource("AdminView/FXMLDataManipulation.fxml"));

                        try {
                            AnchorPane adminTabPane = fxmlAdmin.load();

                            mainView.setCenter(adminTabPane);

                        } catch (IOException ex) {
                            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                } else {
                    alert.setHeaderText("Fail To Authentication !");
                    alert.setContentText("Please Check Your Username And Password Again !");
                    alert.showAndWait();
                }
            });

        } catch (SQLException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void callCondoForSale() {
        //สำคัญโคตรๆ ! ใช้วิธีนี้ในการแก้ปัญหา jave reflection
        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(FXMLLoginController.class.getResource("blockDisplay/FXMLPagination.fxml"));
        FXMLPaginationController saleController = new FXMLPaginationController();
        saleController.setSqlCountContentNumber("select count(*) from room where typeId=1 ");
        saleController.setSqlContentToDisplay("SELECT  * FROM room r\n"
                + "left JOIN condo c ON\n"
                + "r.condoId = c.condoId\n"
                + "left JOIN area a ON\n"
                + "c.areaId = a.areaId\n"
                + "left JOIN picture p ON\n"
                + "r.picId=p.picId" + " where typeId=1");
        fxml.setController(saleController);
        try {
            mainView.setCenter(fxml.load());
            //บันทึก state ของ pagination ไว้
            legacyPage = (Pagination) (mainView.getCenter());
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Fade.setFadeIn(mainView.getCenter());

    }

}
