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
        // --- SỬA: Thêm lại LEFT JOIN Rooms và cột TreatmentPlan ---
        String sql = "SELECT mr.*, p.FullName AS PatientName, d.FullName AS DoctorName, r.RoomNumber " +
                     "FROM MedicalRecords mr " +
                     "JOIN Patients p ON mr.PatientID = p.PatientID " +
                     "JOIN Doctors d ON mr.DoctorID = d.DoctorID " +
                     "LEFT JOIN Rooms r ON mr.RoomID = r.RoomID"; // Thêm lại JOIN Rooms

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                MedicalRecord record = new MedicalRecord();
                record.setRecordID(rs.getString("RecordID"));
                record.setPatientID(rs.getString("PatientID"));
                record.setDoctorID(rs.getString("DoctorID"));
                record.setRoomID(rs.getString("RoomID")); // --- SỬA: Lấy lại RoomID ---
                record.setAdmissionDate(rs.getTimestamp("AdmissionDate"));
                record.setDischargeDate(rs.getTimestamp("DischargeDate"));
                record.setDiagnosis(rs.getString("Diagnosis"));
                record.setTreatmentPlan(rs.getString("TreatmentPlan")); // --- SỬA: Lấy lại TreatmentPlan ---
                record.setRecordStatus(rs.getString("RecordStatus"));
                record.setPatientName(rs.getString("PatientName"));
                record.setDoctorName(rs.getString("DoctorName"));
                record.setRoomNumber(rs.getString("RoomNumber")); // --- SỬA: Lấy lại RoomNumber ---

                records.add(record);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách bệnh án", ex);
        }
        return records;
    }
    
    /**
     * Lấy một bệnh án cụ thể theo RecordID (Tìm chính xác)
     */
    public MedicalRecord getMedicalRecordById(String recordID) {
        MedicalRecord record = null;
        String sql = "SELECT mr.*, p.FullName AS PatientName, d.FullName AS DoctorName, r.RoomNumber " +
                     "FROM MedicalRecords mr " +
                     "JOIN Patients p ON mr.PatientID = p.PatientID " +
                     "JOIN Doctors d ON mr.DoctorID = d.DoctorID " +
                     "LEFT JOIN Rooms r ON mr.RoomID = r.RoomID " +
                     "WHERE mr.RecordID = ?"; // Dùng = để tìm chính xác

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, recordID); // Tìm chính xác ID
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    record = new MedicalRecord(); // Khởi tạo đối tượng
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
        // --- SỬA: Thêm lại RoomID và TreatmentPlan vào câu SQL và tham số ---
        String sql = "INSERT INTO MedicalRecords (RecordID, PatientID, DoctorID, RoomID, AdmissionDate, DischargeDate, Diagnosis, TreatmentPlan, RecordStatus) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, record.getRecordID());
            preparedStatement.setString(2, record.getPatientID());
            preparedStatement.setString(3, record.getDoctorID());
            // Xử lý RoomID có thể NULL
            if (record.getRoomID() != null && !record.getRoomID().isEmpty()) {
                 preparedStatement.setString(4, record.getRoomID()); // --- SỬA: Index 4 ---
            } else {
                 preparedStatement.setNull(4, java.sql.Types.VARCHAR); // --- SỬA: Index 4 ---
            }
            preparedStatement.setTimestamp(5, record.getAdmissionDate()); // --- SỬA: Index 5 ---
            // Xử lý DischargeDate có thể NULL
            if (record.getDischargeDate() != null) {
                preparedStatement.setTimestamp(6, record.getDischargeDate()); // --- SỬA: Index 6 ---
            } else {
                 preparedStatement.setNull(6, java.sql.Types.TIMESTAMP); // --- SỬA: Index 6 ---
            }
            preparedStatement.setString(7, record.getDiagnosis()); // --- SỬA: Index 7 ---
            preparedStatement.setString(8, record.getTreatmentPlan()); // --- SỬA: Index 8 (Thêm lại) ---
            preparedStatement.setString(9, record.getRecordStatus()); // --- SỬA: Index 9 ---

            preparedStatement.executeUpdate();
        }
        // Lưu ý quan trọng về cập nhật trạng thái phòng sẽ được bàn sau
    }

    public void updateMedicalRecord(MedicalRecord record) throws SQLException {
         // --- SỬA: Thêm lại RoomID và TreatmentPlan ---
         String sql = "UPDATE MedicalRecords SET PatientID = ?, DoctorID = ?, RoomID = ?, AdmissionDate = ?, DischargeDate = ?, Diagnosis = ?, TreatmentPlan = ?, RecordStatus = ? " +
                      "WHERE RecordID = ?";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, record.getPatientID());
            preparedStatement.setString(2, record.getDoctorID());
            // Xử lý RoomID
            if (record.getRoomID() != null && !record.getRoomID().isEmpty()) {
                 preparedStatement.setString(3, record.getRoomID()); // --- SỬA: Index 3 ---
            } else {
                 preparedStatement.setNull(3, java.sql.Types.VARCHAR); // --- SỬA: Index 3 ---
            }
            preparedStatement.setTimestamp(4, record.getAdmissionDate()); // --- SỬA: Index 4 ---
             // Xử lý DischargeDate
             if (record.getDischargeDate() != null) {
                preparedStatement.setTimestamp(5, record.getDischargeDate()); // --- SỬA: Index 5 ---
            } else {
                 preparedStatement.setNull(5, java.sql.Types.TIMESTAMP); // --- SỬA: Index 5 ---
            }
            preparedStatement.setString(6, record.getDiagnosis()); // --- SỬA: Index 6 ---
            preparedStatement.setString(7, record.getTreatmentPlan()); // --- SỬA: Index 7 (Thêm lại) ---
            preparedStatement.setString(8, record.getRecordStatus()); // --- SỬA: Index 8 ---
            preparedStatement.setString(9, record.getRecordID()); // Điều kiện WHERE, Index = 9

            preparedStatement.executeUpdate();
        }
        // Lưu ý về cập nhật trạng thái phòng
    }

    // Hàm deleteMedicalRecord giữ nguyên
    public void deleteMedicalRecord(String recordID) throws SQLException {
        String sql = "DELETE FROM MedicalRecords WHERE RecordID = ?";
        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recordID);
            preparedStatement.executeUpdate();
        }
         // Lưu ý về cập nhật trạng thái phòng
    }
    
    // ---  Hàm tìm kiếm bệnh án theo ID ---
    public List<MedicalRecord> searchMedicalRecordsById(String recordID) {
        List<MedicalRecord> records = new ArrayList<>();
        // Dùng lại câu SQL JOIN để lấy đủ thông tin
         String sql = "SELECT mr.*, p.FullName AS PatientName, d.FullName AS DoctorName, r.RoomNumber " +
                     "FROM MedicalRecords mr " +
                     "JOIN Patients p ON mr.PatientID = p.PatientID " +
                     "JOIN Doctors d ON mr.DoctorID = d.DoctorID " +
                     "LEFT JOIN Rooms r ON mr.RoomID = r.RoomID " +
                     "WHERE mr.RecordID LIKE ?"; // Dùng LIKE để tìm gần đúng

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + recordID + "%"); // Tìm kiếm chứa ID nhập vào
            try(ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    // ... (Code tạo đối tượng MedicalRecord giống hệt getAllMedicalRecords) ...
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
