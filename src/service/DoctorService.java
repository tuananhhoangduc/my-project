/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package service;
import connection.DoctorDao;
import model.Doctor;
import java.util.List;
import java.sql.SQLException;
/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
public class DoctorService {
    private DoctorDao doctorDao;

    public DoctorService() {
        doctorDao = new DoctorDao();
    }

    public List<Doctor> getAllDoctors() {
        return doctorDao.getAllDoctors();
    }

    public void addDoctor(Doctor doctor) throws SQLException {
        doctorDao.addDoctor(doctor);
    }

    public void updateDoctor(Doctor doctor) throws SQLException {
        doctorDao.updateDoctor(doctor);
    }

    public void deleteDoctor(String doctorID) throws SQLException {
        doctorDao.deleteDoctor(doctorID);
    }
}

