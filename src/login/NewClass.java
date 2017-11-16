/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import LoginPermission.MyConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivora
 */
public class NewClass {
    public static void main(String[] args) {
        Connection con=MyConnection.getConnection();
        try {
            InputStream in=new FileInputStream(new File("C:\\Users\\Ivora\\Documents\\NetBeansProjects\\JavaFXApplication11\\src\\login\\image\\c1.jpg"));
            PreparedStatement pstm=con.prepareStatement("insert into TestPic values(9,?)");
            pstm.setBlob(1, in);
            pstm.executeUpdate();
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
