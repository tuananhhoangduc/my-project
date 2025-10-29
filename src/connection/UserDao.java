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
import java.sql.*;
import java.util.*;
import model.User;
public class UserDao {
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        Connection connection = JDBCConnection.getJDBCConnection();
        
        String sql = "SELECT * FROM USER";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                User user = new User();
                
                user.setId(rs.getInt("ID"));
                user.setName(rs.getString("NAME"));
                user.setPhone(rs.getString("PHONE"));
                user.setUsername(rs.getString("USERNAME"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setAbout(rs.getString("ABOUT"));
                user.setFavourites(rs.getString("FAVOURITES"));
                user.setRole(rs.getString("ROLE"));
                
                users.add(user);
            }
        } catch (SQLException ex) {
            System.getLogger(UserDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return users;
    }
    
    public User getUserById(int id){
        
        Connection connection = JDBCConnection.getJDBCConnection();
      
        String sql = "SELECT * FROM USER WHERE ID = ?";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                User user = new User();
                
                user.setId(rs.getInt("ID"));
                user.setName(rs.getString("NAME"));
                user.setPhone(rs.getString("PHONE"));
                user.setUsername(rs.getString("USERNAME"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setAbout(rs.getString("ABOUT"));
                user.setFavourites(rs.getString("FAVOURITES"));
                user.setRole(rs.getString("ROLE"));
                
                return user;
            }
        } catch (SQLException ex) {
            System.getLogger(UserDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
    
    public void addUser(User user) {
        Connection connection = JDBCConnection.getJDBCConnection();
        
        String sql = "INSERT INTO USER(NAME, PHONE, USERNAME, PASSWORD,ABOUT, FAVOURITES, ROLE ) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getAbout());
            preparedStatement.setString(6, user.getFavourites());
            preparedStatement.setString(7, user.getRole());
            
            int rs = preparedStatement.executeUpdate();
            System.out.println(rs);
        } catch (SQLException ex) {
            System.getLogger(UserDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    public void updateUser(User user) {
        Connection connection = JDBCConnection.getJDBCConnection();
        
        String sql = "UPDATE User SET NAME = ?, PHONE = ?,"
                + " USERNAME= ?, PASSWORD = ?,ABOUT = ?, FAVOURITES = ?, ROLE = ? WHERE ID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getAbout());
            preparedStatement.setString(6, user.getFavourites());
            preparedStatement.setString(7, user.getRole());
            preparedStatement.setInt(8, user.getId());
            
            int rs = preparedStatement.executeUpdate();
            System.out.println(rs);
        } catch (SQLException ex) {
            System.getLogger(UserDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    public void deleteUser(int id) {
        Connection connection = JDBCConnection.getJDBCConnection();
        
        String sql = "delete from User where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rs = preparedStatement.executeUpdate();
            System.out.println(rs);
        } catch (SQLException ex) {
            System.getLogger(UserDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}

