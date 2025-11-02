/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package service;
import connection.AccountDao;
import model.Account;
import java.util.Collections; 
import java.util.List; 
import java.util.logging.Level; 
import java.util.logging.Logger; 
import java.sql.SQLException;
/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
public class AccountService {
     private AccountDao accountDao;
    private static final Logger logger = Logger.getLogger(AccountService.class.getName());
    
    public AccountService() {
        this.accountDao = new AccountDao();
    }
    
    
    public Account login(String username, String password) {
        return accountDao.login(username, password);
    }
    
    public List<Account> getAllAccounts() {
            return accountDao.getAllAccounts();
    }
    
    public Account getAccountById(String accountID) {
            return accountDao.getAccountById(accountID);
    }
    
    public void addAccount(Account account) throws SQLException, IllegalArgumentException {
        if (account.getUsername().isEmpty() || account.getPassword().isEmpty() || account.getAccountId().isEmpty()) {
            throw new IllegalArgumentException("ID, Username và Password không được để trống.");
        }
        
        accountDao.addAccount(account);
    }

    public void updateAccount(Account account) throws SQLException, IllegalArgumentException {
        if (account.getPassword().isEmpty()) {
             throw new IllegalArgumentException("Password không được để trống.");
        }
        accountDao.updateAccount(account);
    }

    public void deleteAccount(String accountID) throws SQLException {
        if (accountID == null || accountID.isEmpty()) {
            throw new IllegalArgumentException("Account ID không hợp lệ.");
        }
        accountDao.deleteAccount(accountID);
    }
    
}
