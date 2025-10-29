/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package connection;
import model.Room;
import model.Patient;
import java.math.BigDecimal; // Import BigDecimal
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
public class RoomDao {
    private static final Logger logger = Logger.getLogger(RoomDao.class.getName());
    
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms ORDER BY RoomNumber"; // Sắp xếp theo số phòng

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Room room = new Room();
                room.setRoomID(rs.getString("RoomID"));
                room.setRoomNumber(rs.getString("RoomNumber"));
                room.setRoomType(rs.getString("RoomType"));
                room.setCapacity(rs.getInt("Capacity"));
                room.setCurrentOccupancy(rs.getInt("CurrentOccupancy"));
                room.setStatus(rs.getString("Status"));
                room.setPricePerDay(rs.getBigDecimal("PricePerDay")); // --- SỬA: Lấy giá ---
                rooms.add(room);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách phòng", ex);
        }
        return rooms;
    }
    /**
     * Lấy danh sách bệnh nhân đang ở trong một phòng cụ thể
     * (Sử dụng JOIN với MedicalRecords và Patients)
     * Chỉ lấy bệnh nhân có bệnh án "Đang điều trị"
     */
    public List<Patient> getPatientsInRoom(String roomId) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT p.* FROM Patients p " +
                     "JOIN MedicalRecords mr ON p.PatientID = mr.PatientID " +
                     "WHERE mr.RoomID = ? AND mr.RecordStatus = 'Đang điều trị'"; // Chỉ lấy BN đang điều trị

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, roomId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setPatientID(rs.getString("PatientID"));
                    patient.setFullName(rs.getString("FullName"));
                    patient.setDateOfBirth(rs.getDate("DateOfBirth"));
                    patient.setGender(rs.getString("Gender"));
                    patient.setPhoneNumber(rs.getString("PhoneNumber"));
                    // Lấy Address nếu cột tồn tại trong bảng Patients
                    try {
                        patient.setAddress(rs.getString("Address"));
                    } catch (SQLException e) {
                        logger.log(Level.FINE, "Cột Address không tồn tại hoặc lỗi khi lấy Address cho Patient " + patient.getPatientID(), e);
                        // Bỏ qua lỗi nếu cột Address không tồn tại
                    }
                    patients.add(patient);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy bệnh nhân trong phòng " + roomId, ex);
        }
        return patients;
    }
    /**
     * Cập nhật số lượng người hiện tại và trạng thái phòng
     * @param roomId ID phòng cần cập nhật
     * @param change Số lượng thay đổi (+1 khi thêm BN, -1 khi xóa/xuất viện)
     * @throws SQLException Nếu có lỗi SQL
     */
    public void updateRoomOccupancy(String roomId, int change) throws SQLException {
        Connection connection = null;
        PreparedStatement updateStmt = null;
        PreparedStatement selectStmt = null;
        String sqlSelect = "SELECT Capacity, CurrentOccupancy FROM Rooms WHERE RoomID = ?";
        // Cập nhật cả CurrentOccupancy và Status dựa trên số lượng mới
        String sqlUpdate = "UPDATE Rooms SET CurrentOccupancy = CurrentOccupancy + ?, " +
                           "Status = CASE " +
                           " WHEN (CurrentOccupancy + ?) <= 0 THEN 'Trống' " + // Sửa: <= 0 thì Trống
                           " WHEN (CurrentOccupancy + ?) < Capacity THEN 'Còn chỗ' " +
                           " ELSE 'Đầy' END " +
                           "WHERE RoomID = ?";

        try {
            connection = JDBCConnection.getJDBCConnection();
            connection.setAutoCommit(false); // Bắt đầu transaction nhỏ

            // Lấy thông tin hiện tại (để kiểm tra giới hạn)
            int capacity = 0;
            int currentOccupancy = 0;
            selectStmt = connection.prepareStatement(sqlSelect);
            selectStmt.setString(1, roomId);
            try(ResultSet rs = selectStmt.executeQuery()){
                if(rs.next()){
                    capacity = rs.getInt("Capacity");
                    currentOccupancy = rs.getInt("CurrentOccupancy");
                } else {
                     throw new SQLException("Không tìm thấy phòng với ID: " + roomId);
                }
            }
            selectStmt.close();

            // Kiểm tra ràng buộc
            if (change > 0 && currentOccupancy >= capacity) {
                 throw new SQLException("Phòng " + roomId + " đã đầy, không thể thêm bệnh nhân.");
            }
            if (change < 0 && currentOccupancy <= 0) {
                 logger.warning("Cố gắng giảm số người trong phòng " + roomId + " đã trống.");
                 connection.rollback(); // Hủy bỏ
                 return; // Không làm gì cả
            }

            // Tính toán số lượng người mới
            int newOccupancy = currentOccupancy + change;
            if (newOccupancy < 0) newOccupancy = 0; // Đảm bảo không âm

            // Sửa lại câu UPDATE để dùng giá trị newOccupancy tính toán status
             sqlUpdate = "UPDATE Rooms SET CurrentOccupancy = ?, " +
                           "Status = CASE " +
                           " WHEN ? <= 0 THEN 'Trống' " +
                           " WHEN ? < Capacity THEN 'Còn chỗ' " +
                           " ELSE 'Đầy' END " +
                           "WHERE RoomID = ?";


            // Thực hiện cập nhật
            updateStmt = connection.prepareStatement(sqlUpdate);
            updateStmt.setInt(1, newOccupancy); // Số lượng mới
            updateStmt.setInt(2, newOccupancy); // So sánh Status
            updateStmt.setInt(3, newOccupancy); // So sánh Status
            updateStmt.setString(4, roomId);
            updateStmt.executeUpdate();

            connection.commit(); // Hoàn tất

        } catch (SQLException ex) {
            if (connection != null) try { connection.rollback(); } catch (SQLException e) { logger.log(Level.SEVERE, "Lỗi rollback (cập nhật phòng)", e); }
            logger.log(Level.SEVERE, "Lỗi SQL khi cập nhật số người phòng " + roomId, ex);
            throw ex; // Ném lại lỗi
        } finally {
            if (updateStmt != null) try { updateStmt.close(); } catch (SQLException e) {}
            if (selectStmt != null) try { selectStmt.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.setAutoCommit(true); /* connection.close(); */ } catch (SQLException e) {}
        }
    }
    
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE CurrentOccupancy < Capacity ORDER BY RoomNumber";

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Room room = new Room();
                room.setRoomID(rs.getString("RoomID"));
                room.setRoomNumber(rs.getString("RoomNumber"));
                room.setRoomType(rs.getString("RoomType"));
                room.setCapacity(rs.getInt("Capacity"));
                room.setCurrentOccupancy(rs.getInt("CurrentOccupancy"));
                room.setStatus(rs.getString("Status"));
                room.setPricePerDay(rs.getBigDecimal("PricePerDay")); // --- SỬA: Lấy giá ---
                rooms.add(room);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách phòng trống/còn chỗ", ex);
        }
        return rooms;
    }
    
    public void addRoom(Room room) throws SQLException {
         // --- SỬA: Thêm PricePerDay vào câu SQL và PreparedStatement ---
         String sql = "INSERT INTO Rooms (RoomID, RoomNumber, RoomType, Capacity, PricePerDay, CurrentOccupancy, Status) " +
                      "VALUES (?, ?, ?, ?, ?, 0, 'Trống')";
         try (Connection connection = JDBCConnection.getJDBCConnection();
              PreparedStatement ps = connection.prepareStatement(sql)) {
             ps.setString(1, room.getRoomID());
             ps.setString(2, room.getRoomNumber());
             ps.setString(3, room.getRoomType());
             ps.setInt(4, room.getCapacity());
             // Xử lý giá, nếu null thì đặt là 0
             ps.setBigDecimal(5, room.getPricePerDay() != null ? room.getPricePerDay() : BigDecimal.ZERO);
             ps.executeUpdate();
         } catch (SQLException ex) {
              logger.log(Level.SEVERE, "Lỗi SQL khi thêm phòng", ex);
              throw ex;
         }
    }
    
    public void updateRoom(Room room) throws SQLException {
         String sql = "UPDATE Rooms SET RoomNumber = ?, RoomType = ?, Capacity = ?, PricePerDay = ? WHERE RoomID = ?";
          try (Connection connection = JDBCConnection.getJDBCConnection();
              PreparedStatement ps = connection.prepareStatement(sql)) {
             ps.setString(1, room.getRoomNumber());
             ps.setString(2, room.getRoomType());
             ps.setInt(3, room.getCapacity());
             ps.setBigDecimal(4, room.getPricePerDay() != null ? room.getPricePerDay() : BigDecimal.ZERO);
             ps.setString(5, room.getRoomID());
             ps.executeUpdate();
         } catch (SQLException ex) {
              logger.log(Level.SEVERE, "Lỗi SQL khi cập nhật phòng", ex);
              throw ex;
         }
     }
    
    public Room getRoomById(String roomId) {
        Room room = null;
        String sql = "SELECT * FROM Rooms WHERE RoomID = ?";

        try (Connection connection = JDBCConnection.getJDBCConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, roomId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    room = new Room();
                   room.setRoomID(rs.getString("RoomID"));
                    room.setRoomNumber(rs.getString("RoomNumber"));
                    room.setRoomType(rs.getString("RoomType"));
                    room.setCapacity(rs.getInt("Capacity"));
                    room.setCurrentOccupancy(rs.getInt("CurrentOccupancy"));
                    room.setStatus(rs.getString("Status"));
                    room.setPricePerDay(rs.getBigDecimal("PricePerDay")); // Gọi hàm map
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy phòng theo ID: " + roomId, ex);
        }
        return room;
    }
}
