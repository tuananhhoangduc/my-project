/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package service;
import connection.MedicalRecordDao;
import model.MedicalRecord;
import model.Doctor; 
import model.Patient; 
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * MSV: B23DCCN020
 * Họ và tên: Hoàng Đức Tuấn Anh
 * Bài Tập : 
 */
public class MedicalRecordService {
    
    private static final Logger logger = Logger.getLogger(MedicalRecordService.class.getName());
    private MedicalRecordDao medicalRecordDao;
    private DoctorService doctorService;
    private PatientService patientService;


    public MedicalRecordService() {
        medicalRecordDao = new MedicalRecordDao();
        doctorService = new DoctorService(); 
        patientService = new PatientService(); 
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordDao.getAllMedicalRecords();
    }
    
    public MedicalRecord getMedicalRecordById(String recordID) {
         try {
            return medicalRecordDao.getMedicalRecordById(recordID);
         } catch (Exception e) {
             logger.log(Level.SEVERE, "Lỗi nghiêm trọng khi lấy bệnh án theo ID từ Service: " + recordID, e);
             return null; 
         }
    }

    public void addMedicalRecord(MedicalRecord record) throws SQLException {
        medicalRecordDao.addMedicalRecord(record);
    }

    public void updateMedicalRecord(MedicalRecord record) throws SQLException {
        medicalRecordDao.updateMedicalRecord(record);
    }

    public void deleteMedicalRecord(String recordID) throws SQLException {
        medicalRecordDao.deleteMedicalRecord(recordID);
    }

    public List<Doctor> getAllDoctorsForComboBox() {
        return doctorService.getAllDoctors();
    }

    public List<Patient> getAllPatientsForComboBox() {
        return patientService.getAllPatients();
    }
}
