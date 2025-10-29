/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package service;
import connection.RoomDao;
import model.Room;
import model.Patient;
import java.math.BigDecimal; 
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
public class RoomService {
     private RoomDao roomDao;
      private static final Logger logger = Logger.getLogger(RoomService.class.getName());
    public RoomService() {
        roomDao = new RoomDao();
    }
    
    public List<Room> getAllRooms() {
        return roomDao.getAllRooms();
    }
    
    public List<Patient> getPatientsInRoom(String roomId) {
        return roomDao.getPatientsInRoom(roomId);
    }
    
     /**
     * Gọi DAO để cập nhật số lượng người trong phòng
     * @param roomId ID phòng
     * @param change +1 hoặc -1
     * @throws SQLException Nếu có lỗi SQL từ DAO
     */
    public void updateRoomOccupancy(String roomId, int change) throws SQLException {
        roomDao.updateRoomOccupancy(roomId, change);
    }
    
    public Room getRoomById(String roomId) {
        try {
            return roomDao.getRoomById(roomId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi Service khi lấy phòng theo ID: " + roomId, e);
            return null; 
        }
    } 
    
    public List<Room> getAvailableRooms() {
        return roomDao.getAvailableRooms();
    }

   
    
}
