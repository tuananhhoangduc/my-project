/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package connection;
import model.Room;
import model.Patient;
import java.math.BigDecimal;
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
        String sql = "SELECT * FROM Rooms ORDER BY RoomNumber"; 

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
                room.setPricePerDay(rs.getBigDecimal("PricePerDay")); 
                rooms.add(room);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách phòng", ex);
        }
        return rooms;
    }
    
    public void updateRoomOccupancy(String roomId, int change) throws SQLException {
        Connection connection = null;
        PreparedStatement updateStmt = null;
        PreparedStatement selectStmt = null;
        String sqlSelect = "SELECT Capacity, CurrentOccupancy FROM Rooms WHERE RoomID = ?";
        String sqlUpdate = "UPDATE Rooms SET CurrentOccupancy = CurrentOccupancy + ?, " +
                           "Status = CASE " +
                           " WHEN (CurrentOccupancy + ?) <= 0 THEN 'Trống' " + // Sửa: <= 0 thì Trống
                           " WHEN (CurrentOccupancy + ?) < Capacity THEN 'Còn chỗ' " +
                           " ELSE 'Đầy' END " +
                           "WHERE RoomID = ?";

        try {
            connection = JDBCConnection.getJDBCConnection();
            connection.setAutoCommit(false);
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

            
            if (change > 0 && currentOccupancy >= capacity) {
                 throw new SQLException("Phòng " + roomId + " đã đầy, không thể thêm bệnh nhân.");
            }
            if (change < 0 && currentOccupancy <= 0) {
                 logger.warning("Cố gắng giảm số người trong phòng " + roomId + " đã trống.");
                 connection.rollback(); 
                 return; 
            }

            int newOccupancy = currentOccupancy + change;
            if (newOccupancy < 0) newOccupancy = 0; 
             sqlUpdate = "UPDATE Rooms SET CurrentOccupancy = ?, " +
                           "Status = CASE " +
                           " WHEN ? <= 0 THEN 'Trống' " +
                           " WHEN ? < Capacity THEN 'Còn chỗ' " +
                           " ELSE 'Đầy' END " +
                           "WHERE RoomID = ?";

            updateStmt = connection.prepareStatement(sqlUpdate);
            updateStmt.setInt(1, newOccupancy); 
            updateStmt.setInt(2, newOccupancy); 
            updateStmt.setInt(3, newOccupancy); 
            updateStmt.setString(4, roomId);
            updateStmt.executeUpdate();

            connection.commit(); 

        } catch (SQLException ex) {
            if (connection != null) try { connection.rollback(); } catch (SQLException e) { logger.log(Level.SEVERE, "Lỗi rollback (cập nhật phòng)", e); }
            logger.log(Level.SEVERE, "Lỗi SQL khi cập nhật số người phòng " + roomId, ex);
            throw ex; 
        } finally {
            if (updateStmt != null) try { updateStmt.close(); } catch (SQLException e) {}
            if (selectStmt != null) try { selectStmt.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.setAutoCommit(true); } catch (SQLException e) {}
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
                room.setPricePerDay(rs.getBigDecimal("PricePerDay")); 
                rooms.add(room);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách phòng trống/còn chỗ", ex);
        }
        return rooms;
    }
    
    public void addRoom(Room room) throws SQLException {
         String sql = "INSERT INTO Rooms (RoomID, RoomNumber, RoomType, Capacity, PricePerDay, CurrentOccupancy, Status) " +
                      "VALUES (?, ?, ?, ?, ?, 0, 'Trống')";
         try (Connection connection = JDBCConnection.getJDBCConnection();
              PreparedStatement ps = connection.prepareStatement(sql)) {
             ps.setString(1, room.getRoomID());
             ps.setString(2, room.getRoomNumber());
             ps.setString(3, room.getRoomType());
             ps.setInt(4, room.getCapacity());
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
                    room.setPricePerDay(rs.getBigDecimal("PricePerDay")); 
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy phòng theo ID: " + roomId, ex);
        }
        return room;
    }
}
