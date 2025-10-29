/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package connection;
import model.Doctor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
public class DoctorDao {
     private static final Logger logger = Logger.getLogger(DoctorDao.class.getName());
     
     public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM Doctors";

        // Sử dụng try-with-resources để tự động đóng kết nối và statement
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorID(rs.getString("DoctorID"));
                doctor.setFullName(rs.getString("FullName"));
                doctor.setGender(rs.getString("Gender"));
                doctor.setPhoneNumber(rs.getString("PhoneNumber"));
                doctor.setSpecialty(rs.getString("Specialty"));
    
                doctors.add(doctor);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách bác sĩ", ex);
             JOptionPane.showMessageDialog(null, 
                    "Lỗi nghiêm trọng: Không thể tải danh sách bác sĩ.\n" + ex.getMessage(), 
                    "Lỗi CSDL", 
                    JOptionPane.ERROR_MESSAGE);
        }
        return doctors;
    }

    /**
     * Thêm một bác sĩ mới vào CSDL
     */
    public void addDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO Doctors(DoctorID, FullName, Gender, PhoneNumber, Specialty) VALUES(?, ?, ?, ?, ?)";
        
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, doctor.getDoctorID());
            preparedStatement.setString(2, doctor.getFullName());
            preparedStatement.setString(3, doctor.getGender());
            preparedStatement.setString(4, doctor.getPhoneNumber());
            preparedStatement.setString(5, doctor.getSpecialty());
            
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Cập nhật thông tin bác sĩ
     */
    public void updateDoctor(Doctor doctor) throws SQLException {
        String sql = "UPDATE Doctors SET FullName = ?, Gender = ?, PhoneNumber = ?, Specialty = ? WHERE DoctorID = ?";
        
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, doctor.getFullName());
            preparedStatement.setString(2, doctor.getGender());
            preparedStatement.setString(3, doctor.getPhoneNumber());
            preparedStatement.setString(4, doctor.getSpecialty());
            preparedStatement.setString(5, doctor.getDoctorID());
            
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Xóa một bác sĩ
     * @param doctorID
     * @throws java.sql.SQLException
     */
    public void deleteDoctor(String doctorID) throws SQLException {
        String sql = "DELETE FROM Doctors WHERE DoctorID = ?";
        
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, doctorID);
            preparedStatement.executeUpdate();
        }
    }
}
