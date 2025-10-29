/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package service;
import connection.PatientDao;
import model.Patient;
import java.sql.SQLException;
import java.util.List;
/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
public class PatientService {

    private PatientDao patientDao;

    public PatientService() {
        patientDao = new PatientDao();
    }

    public List<Patient> getAllPatients() {
        return patientDao.getAllPatients();
    }

    public void addPatient(Patient patient) throws SQLException {
        patientDao.addPatient(patient);
    }

    public void updatePatient(Patient patient) throws SQLException {
        patientDao.updatePatient(patient);
    }

    public void deletePatient(String patientID) throws SQLException {
        patientDao.deletePatient(patientID);
    }
    
     public Patient getPatientById(String patientID) {
        return patientDao.getPatientById(patientID);
    }
}
