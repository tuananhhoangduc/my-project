/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package connection;
import model.Patient;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
public class PatientDao {
    private static final Logger logger = Logger.getLogger(PatientDao.class.getName());

    
    // Lấy tất cả bệnh nhân
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patients";

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientID(rs.getString("PatientID"));
                patient.setFullName(rs.getString("FullName"));
                patient.setDateOfBirth(rs.getDate("DateOfBirth"));
                patient.setGender(rs.getString("Gender"));
                patient.setPhoneNumber(rs.getString("PhoneNumber"));
                patient.setAddress(rs.getString("Address"));
                patients.add(patient);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách bệnh nhân", ex);
        }
        return patients;
    }

    // Thêm bệnh nhân mới
    public void addPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO Patients(PatientID, FullName, DateOfBirth, Gender, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, patient.getPatientID());
            preparedStatement.setString(2, patient.getFullName());
            preparedStatement.setDate(3, patient.getDateOfBirth());
            preparedStatement.setString(4, patient.getGender());
            preparedStatement.setString(5, patient.getPhoneNumber());
            preparedStatement.setString(6, patient.getAddress());
            preparedStatement.executeUpdate();
        }
    }

    // Cập nhật thông tin bệnh nhân
    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE Patients SET FullName = ?, DateOfBirth = ?, Gender = ?, PhoneNumber = ?, Address = ?  WHERE PatientID = ?";
        
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, patient.getFullName());
            preparedStatement.setDate(2, patient.getDateOfBirth());
            preparedStatement.setString(3, patient.getGender());
            preparedStatement.setString(4, patient.getPhoneNumber());
            preparedStatement.setString(5, patient.getAddress());
            preparedStatement.setString(5, patient.getPatientID());

            preparedStatement.executeUpdate();
        }
    }

    // Xóa bệnh nhân
    public void deletePatient(String patientID) throws SQLException {
        String sql = "DELETE FROM Patients WHERE PatientID = ?";
        
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, patientID);
            preparedStatement.executeUpdate();
        }
    }
    // Tìm bệnh nhân theo ID
    public Patient getPatientById(String patientID) {
        String sql = "SELECT * FROM Patients WHERE PatientID = ?";
        
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, patientID);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Patient patient = new Patient();
                    patient.setPatientID(rs.getString("PatientID"));
                    patient.setFullName(rs.getString("FullName"));
                    patient.setDateOfBirth(rs.getDate("DateOfBirth"));
                    patient.setGender(rs.getString("Gender"));
                    patient.setPhoneNumber(rs.getString("PhoneNumber"));
                    patient.setAddress(rs.getString("Address"));
                    return patient;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi tìm bệnh nhân", ex);
        }
        return null;
    }
}
