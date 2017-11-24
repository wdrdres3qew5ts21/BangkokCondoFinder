/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoginPermission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivora
 */
public class LoginPermission {//class สำหรับทำ session ว่าเรา login เข้ามาจริงๆแล้วนะ
private static String userId="";

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        LoginPermission.userId = userId;
    }
    private static boolean loginStatus = false;
    private static String userName = "";
    private static String password = "";
    private static int status = 0;
    private static Connection con;

    public static boolean isLoginStatus() {
        return loginStatus;
    }

    public static void setLoginStatus(boolean loginStatus) {
        LoginPermission.loginStatus = loginStatus;
    }

    public static String getUserName() {
        return userName;
    }

    public static int getStatus() {
        return status;
    }

    public static void setStatus(int status) {
        LoginPermission.status = status;
    }

    public static void setUserName(String userName) {
        LoginPermission.userName = userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        LoginPermission.password = password;
    }

    public boolean authorizationLogin(String user, String pass) {
        boolean status = false;
        Connection con = MyConnection.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM user u JOIN status s\n"
                    + "ON u.statusId = s.statusId\n"
                    + "WHERE u.userName=? AND u.userPassword=?");
            statement.setString(1, user);
            statement.setString(2, pass);
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            //next มาดึงข้อมูลในชุดของ column แรกแล้วเช็คดูเรื่อง user กับ pass
            //ที่ user กรอกเข้ามานั้นตรงกับใน db หรือไม่
            if (rs.next()) {//login success
                status = true;
                //ดึงข้อมูลที่ user กรอกเข้ามาใช้ในระบบย
                setUserName(user);
                setPassword(pass);
                setStatus(rs.getInt("s.statusId"));
                setUserId(rs.getString("u.userId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginPermission.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLoginStatus(status);
        System.out.println("LogIn Status From Parent Session : " + isLoginStatus());
        System.out.println("User : " + getUserName() + " Password : " + getPassword());
        return status;
    }

}
