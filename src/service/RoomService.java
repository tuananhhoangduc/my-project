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
    
    public void updateRoomOccupancy(String roomId, int change) throws SQLException {
        roomDao.updateRoomOccupancy(roomId, change);
    }
    
    public Room getRoomById(String roomId) {

            return roomDao.getRoomById(roomId);
    } 
    
    public List<Room> getAvailableRooms() {
        return roomDao.getAvailableRooms();
    }

   
    
}
