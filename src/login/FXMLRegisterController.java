/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import LoginPermission.AutoIncrement;
import LoginPermission.MyConnection;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.TemporalField;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Ivora
 */
public class FXMLRegisterController implements Initializable {

    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField confirmPWTextField;
    @FXML
    private JFXButton registerButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField phoneTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        genderComboBox.getItems().add("male");
        genderComboBox.getItems().add("female");

    }

    @FXML
    private void registerOnClick(ActionEvent event) {
        Connection con = MyConnection.getConnection();
        boolean approveForRegister = true;
        Alert alert = new Alert(AlertType.WARNING);
        alert.initStyle(StageStyle.UNDECORATED);

        try {
            //if นอกสุดดักคนเกรียนใส่อะไรแปลกๆเข้ามาเช่น !@!#@??><>
            if ((usernameTextField.getText().isEmpty() == true) | usernameTextField.getText().length() < 6) {
                approveForRegister = false;
                alert.setTitle("Empty Field!");
                alert.setHeaderText("Field Username is empty !!!");
                alert.setContentText("Please fied at least 6 character or more.");
                alert.showAndWait();
            } //ใส่รหัสผิดไม่เหมือนกัน
            else if (passwordTextField.getText().length() < 6) {
                approveForRegister = false;
                alert.setTitle("Empty Field!");
                alert.setHeaderText("Password Security Is Unsafe !!!");
                alert.setContentText("Password need at least 6 character or more.");
                alert.showAndWait();
            } else if (passwordTextField.getText().equals(confirmPWTextField.getText()) != true | passwordTextField.getText().isEmpty()) {
                approveForRegister = false;
                alert.setTitle("Password mismatch!");
                alert.setHeaderText("Password mismatch !!!");
                alert.setContentText("Confirm password not same as password field.");
                alert.showAndWait();
            } else if (nameTextField.getText().isEmpty() == true | lastnameTextField.getText().isEmpty() == true) {
                approveForRegister = false;
                alert.setTitle("Name Field is empty!");
                alert.setHeaderText("Name field is empty !!!");
                alert.setContentText("Make sure that Name and Lastname already field.");
                alert.showAndWait();
            } else if (emailTextField.getText().isEmpty() == true) {
                approveForRegister = false;
                alert.setTitle("Email Field is empty!");
                alert.setHeaderText("Email field is empty !!!");
                alert.setContentText("Make sure that Email already field.");
                alert.showAndWait();
            }
            //ผ่านการดักข้างบนมาได้
            if (approveForRegister == true) {
                PreparedStatement pstm = con.prepareStatement("select * from user WHERE userName=?");
                pstm.setString(1, usernameTextField.getText());
                ResultSet rs = pstm.executeQuery();

                //next มาดึงข้อมูลในชุดของ column แรกแล้วเช็คดูเรื่อง user กับ pass
                //ที่ user กรอกเข้ามานั้นตรงกับใน db หรือไม่
                if (rs.next()) {//เข้ามาในนี้แสดงว่าหาเจอ ต้องห้ามสมัคร !
                    approveForRegister = false;
                    alert.setTitle("Duplicate Account!");
                    alert.setHeaderText("Username Already register !");
                    alert.setContentText("Please change to another Username in order to register.");
                    alert.showAndWait();
                    //ดึงข้อมูลที่ user กรอกเข้ามาใช้ในระบบย
                }
                //ตรวจสอบ Email
                pstm = con.prepareStatement("select * from user WHERE email=?");
                pstm.setString(1, emailTextField.getText());
                rs = pstm.executeQuery();
                if (rs.next()) {//เข้ามาในนี้แสดงว่าหาเจอ ต้องห้ามสมัคร !
                    approveForRegister = false;
                    alert.setTitle("Duplicate Email!");
                    alert.setHeaderText("Email Already register !");
                    alert.setContentText("Please change to another Email in order to register.");
                    alert.showAndWait();
                    //ดึงข้อมูลที่ user กรอกเข้ามาใช้ในระบบย
                }
                //ผ่านการดักข้างบนมาได้

                if (approveForRegister == true) {
                    //insert ค่าลงไป
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Look, a Confirmation Dialog");
                    alert.setContentText("Are you ok with this?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        // ... user chose OK //insert
                        System.out.println("Ok!!!");
                        //ดึง Generate UseId ล่าสุดมาก่อน
                        pstm = con.prepareStatement("SELECT userId from user ORDER BY 1 desc LIMIT 1");
                        rs = pstm.executeQuery();
                        String lastetId = "";
                        if (rs.next()) {
                            lastetId = rs.getString(1);
                        } else {//กรณีไม่มี User สักคน
                            lastetId = "u0000";
                        }
                        pstm = con.prepareStatement("INSERT INTO user\n"
                                + "(userId, userName, userPassword, statusId, firstName, lastName, birthday, sex, phone, email)\n"
                                + "VALUES (?,?,?,?,?,?,?,?,?,?)");
                        pstm.setString(1, AutoIncrement.Generate(lastetId));
                        pstm.setString(2, usernameTextField.getText());
                        pstm.setString(3, passwordTextField.getText());
                        pstm.setInt(4, 0);
                        pstm.setString(5, nameTextField.getText());
                        pstm.setString(6, lastnameTextField.getText());
                        //java.sql.Date date = new java.sql.Date();
                        pstm.setDate(7,Date.valueOf(birthDatePicker.getValue()));
                        System.out.println(genderComboBox.getSelectionModel().toString());
                        pstm.setString(8, genderComboBox.getSelectionModel().getSelectedItem());
                        pstm.setString(9, phoneTextField.getText());
                        pstm.setString(10, emailTextField.getText());
                        pstm.executeUpdate();
                        alert = new Alert(AlertType.INFORMATION);
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Successful Register !!!");
                        alert.setContentText("Thank you for register BangkokCondoFinder.\nWe will redirect you to home page after this.");
                        alert.showAndWait();

                    } else {
                        //donothing
                        // ... user chose CANCEL or closed the dialog
                        System.out.println("Cancle !");
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLRegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
