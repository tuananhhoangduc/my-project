/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package connection;
import model.MedicalRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
public class MedicalRecordDao {
    private static final Logger logger = Logger.getLogger(MedicalRecordDao.class.getName());
    
    public List<MedicalRecord> getAllMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT mr.*, p.FullName AS PatientName, d.FullName AS DoctorName, r.RoomNumber " +
                     "FROM MedicalRecords mr " +
                     "JOIN Patients p ON mr.PatientID = p.PatientID " +
                     "JOIN Doctors d ON mr.DoctorID = d.DoctorID " +
                     "LEFT JOIN Rooms r ON mr.RoomID = r.RoomID"; 

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                MedicalRecord record = new MedicalRecord();
                record.setRecordID(rs.getString("RecordID"));
                record.setPatientID(rs.getString("PatientID"));
                record.setDoctorID(rs.getString("DoctorID"));
                record.setRoomID(rs.getString("RoomID"));
                record.setAdmissionDate(rs.getTimestamp("AdmissionDate"));
                record.setDischargeDate(rs.getTimestamp("DischargeDate"));
                record.setDiagnosis(rs.getString("Diagnosis"));
                record.setTreatmentPlan(rs.getString("TreatmentPlan")); 
                record.setRecordStatus(rs.getString("RecordStatus"));
                record.setPatientName(rs.getString("PatientName"));
                record.setDoctorName(rs.getString("DoctorName"));
                record.setRoomNumber(rs.getString("RoomNumber")); 

                records.add(record);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách bệnh án", ex);
        }
        return records;
    }
    
    /**
     * Lấy một bệnh án theo RecordID 
     */
    public MedicalRecord getMedicalRecordById(String recordID) {
        MedicalRecord record = null;
        String sql = "SELECT mr.*, p.FullName AS PatientName, d.FullName AS DoctorName, r.RoomNumber " +
                     "FROM MedicalRecords mr " +
                     "JOIN Patients p ON mr.PatientID = p.PatientID " +
                     "JOIN Doctors d ON mr.DoctorID = d.DoctorID " +
                     "LEFT JOIN Rooms r ON mr.RoomID = r.RoomID " +
                     "WHERE mr.RecordID = ?"; 
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, recordID); 
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    record = new MedicalRecord(); 
                    record.setRecordID(rs.getString("RecordID"));
                    record.setPatientID(rs.getString("PatientID"));
                    record.setDoctorID(rs.getString("DoctorID"));
                    record.setRoomID(rs.getString("RoomID"));
                    record.setAdmissionDate(rs.getTimestamp("AdmissionDate"));
                    record.setDischargeDate(rs.getTimestamp("DischargeDate"));
                    record.setDiagnosis(rs.getString("Diagnosis"));
                    record.setTreatmentPlan(rs.getString("TreatmentPlan"));
                    record.setRecordStatus(rs.getString("RecordStatus"));
                    record.setPatientName(rs.getString("PatientName"));
                    record.setDoctorName(rs.getString("DoctorName"));
                    record.setRoomNumber(rs.getString("RoomNumber"));
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi tìm bệnh án theo ID: " + recordID, ex);
        }
        return record;
    }

    public void addMedicalRecord(MedicalRecord record) throws SQLException {
        String sql = "INSERT INTO MedicalRecords (RecordID, PatientID, DoctorID, RoomID, AdmissionDate, DischargeDate, Diagnosis, TreatmentPlan, RecordStatus) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, record.getRecordID());
            preparedStatement.setString(2, record.getPatientID());
            preparedStatement.setString(3, record.getDoctorID());
            
            if (record.getRoomID() != null && !record.getRoomID().isEmpty()) {
                 preparedStatement.setString(4, record.getRoomID()); 
            } else {
                 preparedStatement.setNull(4, java.sql.Types.VARCHAR); 
            }
            preparedStatement.setTimestamp(5, record.getAdmissionDate()); 
            // Xử lý DischargeDate có thể NULL
            if (record.getDischargeDate() != null) {
                preparedStatement.setTimestamp(6, record.getDischargeDate()); 
            } else {
                 preparedStatement.setNull(6, java.sql.Types.TIMESTAMP); 
            }
            preparedStatement.setString(7, record.getDiagnosis()); 
            preparedStatement.setString(8, record.getTreatmentPlan()); 
            preparedStatement.setString(9, record.getRecordStatus()); 

            preparedStatement.executeUpdate();
        }
        
    }

    public void updateMedicalRecord(MedicalRecord record) throws SQLException {
         String sql = "UPDATE MedicalRecords SET PatientID = ?, DoctorID = ?, RoomID = ?, AdmissionDate = ?, DischargeDate = ?, Diagnosis = ?, TreatmentPlan = ?, RecordStatus = ? " +
                      "WHERE RecordID = ?";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, record.getPatientID());
            preparedStatement.setString(2, record.getDoctorID());
            if (record.getRoomID() != null && !record.getRoomID().isEmpty()) {
                 preparedStatement.setString(3, record.getRoomID()); 
            } else {
                 preparedStatement.setNull(3, java.sql.Types.VARCHAR); 
            }
            preparedStatement.setTimestamp(4, record.getAdmissionDate()); 
             if (record.getDischargeDate() != null) {
                preparedStatement.setTimestamp(5, record.getDischargeDate()); 
            } else {
                 preparedStatement.setNull(5, java.sql.Types.TIMESTAMP); 
            }
            preparedStatement.setString(6, record.getDiagnosis()); 
            preparedStatement.setString(7, record.getTreatmentPlan()); 
            preparedStatement.setString(8, record.getRecordStatus()); 
            preparedStatement.setString(9, record.getRecordID()); 

            preparedStatement.executeUpdate();
        }
        
    }

    // Xóa một bệnh án theo RecordID 
    public void deleteMedicalRecord(String recordID) throws SQLException {
        String sql = "DELETE FROM MedicalRecords WHERE RecordID = ?";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recordID);
            preparedStatement.executeUpdate();
        }
        
    }
    
    //  Tìm kiếm bệnh án theo ID 
    public List<MedicalRecord> searchMedicalRecordsById(String recordID) {
        List<MedicalRecord> records = new ArrayList<>();
        
         String sql = "SELECT mr.*, p.FullName AS PatientName, d.FullName AS DoctorName, r.RoomNumber " +
                     "FROM MedicalRecords mr " +
                     "JOIN Patients p ON mr.PatientID = p.PatientID " +
                     "JOIN Doctors d ON mr.DoctorID = d.DoctorID " +
                     "LEFT JOIN Rooms r ON mr.RoomID = r.RoomID " +
                     "WHERE mr.RecordID LIKE ?"; 

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + recordID + "%");
            try(ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    MedicalRecord record = new MedicalRecord();
                    record.setRecordID(rs.getString("RecordID"));
                    record.setPatientID(rs.getString("PatientID"));
                    record.setDoctorID(rs.getString("DoctorID"));
                    record.setRoomID(rs.getString("RoomID"));
                    record.setAdmissionDate(rs.getTimestamp("AdmissionDate"));
                    record.setDischargeDate(rs.getTimestamp("DischargeDate"));
                    record.setDiagnosis(rs.getString("Diagnosis"));
                    record.setTreatmentPlan(rs.getString("TreatmentPlan"));
                    record.setRecordStatus(rs.getString("RecordStatus"));
                    record.setPatientName(rs.getString("PatientName"));
                    record.setDoctorName(rs.getString("DoctorName"));
                    record.setRoomNumber(rs.getString("RoomNumber"));
                    records.add(record);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi tìm kiếm bệnh án theo ID: " + recordID, ex);
        }
        return records;
    }
}
