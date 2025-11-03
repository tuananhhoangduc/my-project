/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package connection;



import connection.JDBCConnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account; 
import java.util.List;
import java.util.ArrayList;


public class AccountDao {
    
    private static final Logger logger = Logger.getLogger(AccountDao.class.getName());

    
    public Account login(String username, String password) {
        
        String sql = "SELECT * FROM Accounts WHERE Username = ? AND Password = ?";
        
        try (Connection conn = JDBCConnection.getJDBCConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) {
                logger.log(Level.SEVERE, "Không thể kết nối đến CSDL.");
                return null;
            }
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setUsername(rs.getString("Username"));
                    account.setPassword(rs.getString("Password"));
                    account.setRole(rs.getString("Role"));
                    account.setAccountId(rs.getString("AccountID"));
                    
                    return account;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Lỗi khi truy vấn đăng nhập", e);
        }
        return null; 
    }
    
    
     public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Accounts";

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getString("AccountID"));
                account.setUsername(rs.getString("Username"));
                account.setPassword(rs.getString("Password"));
                account.setRole(rs.getString("Role"));
                accounts.add(account);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách tài khoản", ex);
        }
        return accounts;
    }
     
     /**
     * Thêm tài khoản mới
     */
    public void addAccount(Account account) throws SQLException {
        String sql = "INSERT INTO Accounts (AccountID, Username, Password, Role) VALUES (?, ?, ?, ?)";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, account.getAccountId());
            ps.setString(2, account.getUsername());
            ps.setString(3, account.getPassword()); 
            ps.setString(4, account.getRole());

            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi SQL khi thêm tài khoản", ex);
            throw ex;
        }
    }

    /*
      Cập nhật thông tin tài khoản 
     */
    public void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE Accounts SET Password = ?, Role = ? WHERE AccountID = ?";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, account.getPassword()); 
            ps.setString(2, account.getRole());
            ps.setString(3, account.getAccountId());

            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi SQL khi cập nhật tài khoản", ex);
            throw ex;
        }
    }
    
    /*
    Tìm tài khoản bằng ID
    */
    public Account getAccountById(String accountID) {
        String sql = "SELECT * FROM Accounts WHERE AccountID = ?";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, accountID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getString("AccountID"));
                    account.setUsername(rs.getString("Username"));
                    account.setPassword(rs.getString("Password"));
                    account.setRole(rs.getString("Role"));
                    return account;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi tìm tài khoản theo ID: " + accountID, ex);
        }
        return null;
    }
    /**
     * Xóa tài khoản
     */
    public void deleteAccount(String accountID) throws SQLException {
        String sqlUpdateDoctor = "UPDATE Doctors SET AccountID = NULL WHERE AccountID = ?";
        Connection connection = JDBCConnection.getJDBCConnection();
        PreparedStatement psUpdate = connection.prepareStatement(sqlUpdateDoctor);
             
        psUpdate.setString(1, accountID);
        psUpdate.executeUpdate(); 

        String sqlDeleteAccount = "DELETE FROM Accounts WHERE AccountID = ?";
        PreparedStatement psDelete = connection.prepareStatement(sqlDeleteAccount);
            psDelete.setString(1, accountID);
            psDelete.executeUpdate();
       
    }
}
