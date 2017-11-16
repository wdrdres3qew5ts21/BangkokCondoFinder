/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoginPermission;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivora
 */
public class MyConnection {

    private static Connection con = null;

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://13.229.119.168:3306/BangkokCondo", "root", "lnwza");
            System.out.println("ต่อติด !!!");
        } catch (ClassNotFoundException ex) {
            System.out.println("ลืมโหลด Driver");
        } catch (SQLException ex) {
            System.out.println("เน็ตหลุด/IP พัง");
        }

        return con;
    }

}
