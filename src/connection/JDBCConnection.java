/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package connection;

/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCConnection {
    
    private static final Logger logger = Logger.getLogger(JDBCConnection.class.getName());

    public static Connection getJDBCConnection(){
        final String url = "jdbc:mysql://127.0.0.1:3306/hospitaldb_basic";
        final String user = "root";
        final String password = "221230734";
        
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url , user , password);
            
        } catch (ClassNotFoundException ex) {
           
            logger.log(Level.SEVERE, "Không tìm thấy MySQL JDBC Driver", ex);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi kết nối SQL", ex);
        }
        return null;
    }
    
}