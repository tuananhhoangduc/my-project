/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import model.MedicalRecord;
import model.Patient;
import model.Doctor;
import model.Room; 
import service.MedicalRecordService;
import service.RoomService; 
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException; // Import SQLException
import java.util.logging.Level;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.math.BigDecimal;

/**
 *
 * @author LOQ
 */
public class medicalRecord extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(medicalRecord.class.getName());
    private MedicalRecordService medicalRecordService;
    private RoomService roomService; // Thêm RoomService
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<String, String> patientNameToIdMap;
    private Map<String, String> doctorNameToIdMap;
    private Map<String, String> roomNumberToIdMap; 
    private String currentEditingRecordId = null; 
    private String currentRoomId = null; 
    /**
     * Creates new form diagnosis
     */
    public medicalRecord() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
         medicalRecordService = new MedicalRecordService();
        roomService = new RoomService(); // Khởi tạo RoomService
        patientNameToIdMap = new HashMap<>();
        doctorNameToIdMap = new HashMap<>();
        roomNumberToIdMap = new HashMap<>();
        
        initializeTable();
        populateComboBoxes();
        loadMedicalRecords();
        addTableSelectionListener();
        
        setupPopupMenu();
    }
    
    private void initializeTable() {
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
            "Record ID", "Bệnh nhân", "Bác sĩ", "Phòng", "Ngày nhập viện",
            "Ngày xuất viện", "Chẩn đoán", "Kế hoạch điều trị", "Trạng thái"
        });
        
         if (jTableMedicalRecords != null) { 
             jTableMedicalRecords.setModel(tableModel);
             jTableMedicalRecords.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
             jTableMedicalRecords.setComponentPopupMenu(jPopupMenu1);
        } else {
             logger.severe(" jTableMedicalRecords chưa được khởi tạo!");
             JOptionPane.showMessageDialog(this, "Lỗi: Bảng hiển thị chưa được khởi tạo đúng cách!", "Lỗi Giao diện", JOptionPane.ERROR_MESSAGE);
         }
    }
    
     private void setupPopupMenu() {
         if (jMenuItem1 != null && jTableMedicalRecords != null && jPopupMenu1 != null) {
             jMenuItem1.setText("Xuất viện & Thanh toán"); // Đặt tên cho Menu Item

             
             jMenuItem1.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     handleDischargeAction(); 
                 }
             });

             
             jTableMedicalRecords.addMouseListener(new MouseAdapter() {
                 @Override
                 public void mousePressed(MouseEvent e) {
                     maybeShowPopup(e);
                 }

                 @Override
                 public void mouseReleased(MouseEvent e) {
                     maybeShowPopup(e);
                 }

                 private void maybeShowPopup(MouseEvent e) {
                     if (e.isPopupTrigger()) { 
                         int row = jTableMedicalRecords.rowAtPoint(e.getPoint());
                         if (row >= 0) {                             
                             jTableMedicalRecords.setRowSelectionInterval(row, row);
                             jPopupMenu1.show(e.getComponent(), e.getX(), e.getY());
                         }
                     }
                 }
             });

         } else {
              logger.warning("Không thể cấu hình PopupMenu: jMenuItem1, jTableMedicalRecords hoặc jPopupMenu1 là null.");
         }
     }
    
    private void handleDischargeAction() {
         
         int selectedRow = jTableMedicalRecords.getSelectedRow();
         if (selectedRow == -1) { /* ... */ return; }
         String recordID = tableModel.getValueAt(selectedRow, 0).toString();
         MedicalRecord selectedRecord = medicalRecordService.getMedicalRecordById(recordID);
         if (selectedRecord == null) { /* ... */ return; }
         if (selectedRecord.getDischargeDate() != null || !"Đang điều trị".equals(selectedRecord.getRecordStatus())) { /* ... */ return; }
         if (selectedRecord.getRoomID() == null) { /* ... */ return; }


         
         Room room = roomService.getRoomById(selectedRecord.getRoomID());
         BigDecimal totalCostDecimal = BigDecimal.ZERO; 
         if (room != null && room.getPricePerDay() != null) {
             Timestamp admissionTime = selectedRecord.getAdmissionDate();
             Timestamp dischargeTime = new Timestamp(new Date().getTime()); 
             long daysStayed = 0;
             if (admissionTime != null) {
                 long diffInMillis = dischargeTime.getTime() - admissionTime.getTime();
                 daysStayed = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                 if (diffInMillis % TimeUnit.DAYS.toMillis(1) > 0 || daysStayed == 0) {
                     daysStayed++;
                 }
             }
             
             BigDecimal price = room.getPricePerDay();
             BigDecimal days = BigDecimal.valueOf(daysStayed); 
             totalCostDecimal = price.multiply(days); 

             logger.info("Tính tiền phòng: " + daysStayed + " ngày * " + price + " = " + totalCostDecimal);
         } else {
              logger.warning("Không tìm thấy phòng hoặc giá phòng để tính tiền cho RoomID: " + selectedRecord.getRoomID());
              JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin phòng hoặc giá phòng để tính tiền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
              // Cân nhắc có nên return ở đây không, tùy yêu cầu
              // return;
         }
         

         // --- Mở payment ---
         logger.info("Mở cửa sổ Thanh toán cho Record ID: " + selectedRecord.getRecordID());
         // Gọi constructor mới nhận BigDecimal
         payment paymentWindow = new payment(this, selectedRecord, totalCostDecimal); // Truyền BigDecimal
         paymentWindow.setVisible(true);
     }

     
     public void refreshData() {
          logger.info("Làm mới dữ liệu sau khi xuất viện...");
          loadMedicalRecords();
          populateComboBoxes(); 
          clearFields();
     }

    
    private void loadMedicalRecords() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        try {
            List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
            if (records == null) {
                 JOptionPane.showMessageDialog(this, "Không thể tải danh sách bệnh án.", "Lỗi Service", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            for (MedicalRecord mr : records) {
                tableModel.addRow(new Object[]{
                    mr.getRecordID(),
                    mr.getPatientName(),
                    mr.getDoctorName(),
                    mr.getRoomNumber() != null ? mr.getRoomNumber() : "N/A",
                    mr.getAdmissionDate() != null ? dateTimeFormat.format(mr.getAdmissionDate()) : "",
                    mr.getDischargeDate() != null ? dateTimeFormat.format(mr.getDischargeDate()) : "",
                    mr.getDiagnosis(),
                    mr.getTreatmentPlan(),
                    mr.getRecordStatus()
                });
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Lỗi tải danh sách bệnh án", ex);
            JOptionPane.showMessageDialog(this, "Lỗi nghiêm trọng khi tải danh sách bệnh án: " + ex.getMessage(), "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Đổ dữ liệu Bệnh nhân, Bác sĩ, Phòng trống và Trạng thái vào ComboBoxes 
     */
    private void populateComboBoxes() {
        
        DefaultComboBoxModel<String> patientModel = new DefaultComboBoxModel<>();
        patientNameToIdMap.clear();
        patientModel.addElement("-- Chọn bệnh nhân --");
        try {
            List<Patient> patients = medicalRecordService.getAllPatientsForComboBox();
            if (patients != null) {
                 for (Patient p : patients) {
                    String displayText = p.getFullName() + " (" + p.getPatientID() + ")";
                    patientModel.addElement(displayText);
                    patientNameToIdMap.put(displayText, p.getPatientID());
                }
            }
        } catch (Exception ex) { logger.log(Level.WARNING, "Lỗi tải ComboBox Bệnh nhân", ex); }
         if (jComboBoxPatient != null) jComboBoxPatient.setModel(patientModel);
         else logger.severe("jComboBoxPatient chưa được khởi tạo!");


        
        DefaultComboBoxModel<String> doctorModel = new DefaultComboBoxModel<>();
        doctorNameToIdMap.clear();
        doctorModel.addElement("-- Chọn bác sĩ --");
         try {
            List<Doctor> doctors = medicalRecordService.getAllDoctorsForComboBox();
             if (doctors != null) {
                 for (Doctor d : doctors) {
                    String displayText = d.getFullName() + " (" + d.getDoctorID() + ")";
                    doctorModel.addElement(displayText);
                    doctorNameToIdMap.put(displayText, d.getDoctorID());
                }
            }
        } catch (Exception ex) { logger.log(Level.WARNING, "Lỗi tải ComboBox Bác sĩ", ex); }
         if (jComboBoxDoctor != null) jComboBoxDoctor.setModel(doctorModel);
         else logger.severe("jComboBoxDoctor chưa được khởi tạo!");


        
        DefaultComboBoxModel<String> roomModel = new DefaultComboBoxModel<>();
        roomNumberToIdMap.clear();
        roomModel.addElement("-- Ngoại trú / Không chọn phòng --");
        try {
            List<Room> availableRooms = roomService.getAvailableRooms();
            if (availableRooms != null) {
                for (Room r : availableRooms) {
                    String displayText = r.getRoomNumber() + " (" + r.getRoomType() + ") ["
                                       + (r.getCapacity() - r.getCurrentOccupancy()) + "/" + r.getCapacity() + "]";
                    roomModel.addElement(displayText);
                    roomNumberToIdMap.put(displayText, r.getRoomID());
                }
            }
        } catch (Exception ex) { logger.log(Level.WARNING, "Lỗi tải ComboBox Phòng", ex); }
         if (jComboBoxRoom != null) jComboBoxRoom.setModel(roomModel);
         else logger.severe("jComboBoxRoom chưa được khởi tạo!");


        
        DefaultComboBoxModel<String> statusModel = new DefaultComboBoxModel<>(new String[] { "Đang điều trị", "Đã xuất viện", "Đã hủy" });
         if (jComboBoxStatus != null) jComboBoxStatus.setModel(statusModel);
          else logger.severe("jComboBoxStatus chưa được khởi tạo!");
    }
    
    private void clearFields() {
        if(jTextFieldRecordID != null) jTextFieldRecordID.setText("");
        if(jComboBoxPatient != null) jComboBoxPatient.setSelectedIndex(0);
        if(jComboBoxDoctor != null) jComboBoxDoctor.setSelectedIndex(0);
        if(jComboBoxRoom != null) jComboBoxRoom.setSelectedIndex(0);
        if(jTextFieldAdmissionDate != null) jTextFieldAdmissionDate.setText("");
        if(jTextFieldDischargeDate != null) jTextFieldDischargeDate.setText("");
        if(jTextAreaDiagnosis != null) jTextAreaDiagnosis.setText("");
        if(jTextAreaTreatment != null) jTextAreaTreatment.setText("");
        if(jComboBoxStatus != null) jComboBoxStatus.setSelectedIndex(0);
        if(jTextFieldRecordID != null) jTextFieldRecordID.setEditable(true);
        if(jTextFieldSearch != null) jTextFieldSearch.setText("");
        if(jTableMedicalRecords != null) jTableMedicalRecords.clearSelection();
        currentEditingRecordId = null;
        currentRoomId = null;
    }
    
    private MedicalRecord getMedicalRecordFromFields() throws ParseException, IllegalArgumentException {
        MedicalRecord record = new MedicalRecord();

        String recordID = (jTextFieldRecordID != null) ? jTextFieldRecordID.getText().trim() : "";
        if (recordID.isEmpty()) throw new IllegalArgumentException("Record ID không được để trống.");
        record.setRecordID(recordID);

        String selectedPatientDisplay = (jComboBoxPatient != null && jComboBoxPatient.getSelectedIndex() > 0) ? (String) jComboBoxPatient.getSelectedItem() : null;
        if (selectedPatientDisplay == null) throw new IllegalArgumentException("Vui lòng chọn bệnh nhân.");
        record.setPatientID(patientNameToIdMap.get(selectedPatientDisplay));

        String selectedDoctorDisplay = (jComboBoxDoctor != null && jComboBoxDoctor.getSelectedIndex() > 0) ? (String) jComboBoxDoctor.getSelectedItem() : null;
        if (selectedDoctorDisplay == null) throw new IllegalArgumentException("Vui lòng chọn bác sĩ.");
        record.setDoctorID(doctorNameToIdMap.get(selectedDoctorDisplay));

        String selectedRoomDisplay = (jComboBoxRoom != null && jComboBoxRoom.getSelectedIndex() > 0) ? (String) jComboBoxRoom.getSelectedItem() : null;
        String roomId = null;
        if (selectedRoomDisplay != null) {
            roomId = roomNumberToIdMap.get(selectedRoomDisplay);
     
            if (roomId == null && selectedRoomDisplay.contains("(Đang dùng")) {
                 if (currentRoomId != null && currentEditingRecordId != null && currentEditingRecordId.equals(recordID)) {
                     roomId = currentRoomId; 
                 } else {
                     logger.warning("Không thể xác định RoomID cho phòng đang dùng: " + selectedRoomDisplay);
                     
                     throw new IllegalArgumentException("Phòng đang chọn có thể đã bị chiếm. Vui lòng Refresh và chọn lại.");
                 }
            }
        }
        record.setRoomID(roomId);


        String admissionDateStr = (jTextFieldAdmissionDate != null) ? jTextFieldAdmissionDate.getText().trim() : "";
        if (!admissionDateStr.isEmpty()) {
            try {
                record.setAdmissionDate(new Timestamp(dateTimeFormat.parse(admissionDateStr).getTime()));
            } catch (ParseException e) { throw new ParseException("Định dạng Ngày nhập viện sai (yyyy-MM-dd HH:mm:ss)", e.getErrorOffset()); }
        } else { throw new ParseException("Ngày nhập viện không được để trống", 0); }

        String dischargeDateStr = (jTextFieldDischargeDate != null) ? jTextFieldDischargeDate.getText().trim() : "";
        if (!dischargeDateStr.isEmpty()) {
             try {
                record.setDischargeDate(new Timestamp(dateTimeFormat.parse(dischargeDateStr).getTime()));
             } catch (ParseException e) { throw new ParseException("Định dạng Ngày xuất viện sai (yyyy-MM-dd HH:mm:ss)", e.getErrorOffset()); }
        } else { record.setDischargeDate(null); }

        String diagnosis = (jTextAreaDiagnosis != null) ? jTextAreaDiagnosis.getText().trim() : "";
        if (diagnosis.isEmpty()) logger.warning("Chẩn đoán bị bỏ trống cho Record ID: " + recordID);
        record.setDiagnosis(diagnosis);

        record.setTreatmentPlan((jTextAreaTreatment != null) ? jTextAreaTreatment.getText().trim() : "");

        record.setRecordStatus((jComboBoxStatus != null) ? jComboBoxStatus.getSelectedItem().toString() : "Đang điều trị");

        return record;
    }
    
    private void addTableSelectionListener() {
         if (jTableMedicalRecords == null) return;
        jTableMedicalRecords.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            if (!event.getValueIsAdjusting() && jTableMedicalRecords.getSelectedRow() != -1) {
                int selectedRow = jTableMedicalRecords.getSelectedRow();
                if (selectedRow >= tableModel.getRowCount()) {
                    logger.warning("Chỉ số hàng không hợp lệ: " + selectedRow);
                    return;
                }
                currentEditingRecordId = tableModel.getValueAt(selectedRow, 0).toString();

                if(jTextFieldRecordID != null) jTextFieldRecordID.setText(currentEditingRecordId);
                if(jTextFieldRecordID != null) jTextFieldRecordID.setEditable(false);

                String patientNameFromTable = tableModel.getValueAt(selectedRow, 1) != null ? tableModel.getValueAt(selectedRow, 1).toString() : "";
                selectComboBoxItemStartingWith(jComboBoxPatient, patientNameFromTable + " (");

                String doctorNameFromTable = tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "";
                selectComboBoxItemStartingWith(jComboBoxDoctor, doctorNameFromTable + " (");

                String roomNumberFromTable = tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "N/A";
                currentRoomId = findRoomIdFromTableData(currentEditingRecordId);
                if (!"N/A".equals(roomNumberFromTable) && jComboBoxRoom != null) {
                    boolean roomFoundInAvailable = selectComboBoxItemStartingWith(jComboBoxRoom, roomNumberFromTable + " ("); // Tìm trong phòng trống trước
                    if (!roomFoundInAvailable) {
                        logger.info("Phòng " + roomNumberFromTable + " của bệnh án đang sửa không có trong danh sách phòng trống.");
                        // Thêm phòng đang dùng vào ComboBox nếu chưa có
                        String occupiedRoomDisplay = roomNumberFromTable + " (Đang dùng)";
                         boolean exists = false;
                         for (int i = 0; i < jComboBoxRoom.getItemCount(); i++) {
                             if (jComboBoxRoom.getItemAt(i).equals(occupiedRoomDisplay)) {
                                 exists = true;
                                 break;
                             }
                         }
                         if (!exists && currentRoomId != null) {
                             ((DefaultComboBoxModel<String>)jComboBoxRoom.getModel()).insertElementAt(occupiedRoomDisplay, 1);
                         }
                         jComboBoxRoom.setSelectedItem(occupiedRoomDisplay);
                    }
                } else {
                    if(jComboBoxRoom != null) jComboBoxRoom.setSelectedIndex(0); // Ngoại trú
                }


                String admissionDateStr = tableModel.getValueAt(selectedRow, 4) != null ? tableModel.getValueAt(selectedRow, 4).toString() : "";
                if(jTextFieldAdmissionDate != null) jTextFieldAdmissionDate.setText(admissionDateStr);

                String dischargeDateStr = tableModel.getValueAt(selectedRow, 5) != null ? tableModel.getValueAt(selectedRow, 5).toString() : "";
                if(jTextFieldDischargeDate != null) jTextFieldDischargeDate.setText(dischargeDateStr);

                String diagnosis = tableModel.getValueAt(selectedRow, 6) != null ? tableModel.getValueAt(selectedRow, 6).toString() : "";
                if(jTextAreaDiagnosis != null) jTextAreaDiagnosis.setText(diagnosis);

                String treatmentPlan = tableModel.getValueAt(selectedRow, 7) != null ? tableModel.getValueAt(selectedRow, 7).toString() : "";
                 if(jTextAreaTreatment != null) jTextAreaTreatment.setText(treatmentPlan);

                String status = tableModel.getValueAt(selectedRow, 8) != null ? tableModel.getValueAt(selectedRow, 8).toString() : "";
                if(jComboBoxStatus != null) jComboBoxStatus.setSelectedItem(status);
            }
        });
    }
    
    
    private boolean selectComboBoxItemStartingWith(javax.swing.JComboBox<String> comboBox, String prefix) {
        if (comboBox == null || prefix == null) return false;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String item = comboBox.getItemAt(i);
            if (item != null && item.startsWith(prefix)) {
                comboBox.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }
    
    
    private void handleRoomUpdate(String oldRoomId, String newRoomId, String recordStatus) {
         boolean isCurrentlyOccupying = "Đang điều trị".equals(recordStatus);

         try {
             // TH1: Chuyển từ không phòng -> có phòng (hoặc thêm mới vào phòng)
             if (oldRoomId == null && newRoomId != null && isCurrentlyOccupying) {
                 roomService.updateRoomOccupancy(newRoomId, +1);
                 logger.info("Đã cập nhật phòng " + newRoomId + ", số người +1");
             }
             // TH2: Chuyển từ có phòng -> không phòng (xuất viện, hủy, chuyển ngoại trú)
             // Hoặc giữ nguyên ở phòng cũ nhưng không còn chiếm chỗ (xuất viện/hủy)
             else if (oldRoomId != null && (newRoomId == null || !isCurrentlyOccupying) ) {
                 roomService.updateRoomOccupancy(oldRoomId, -1);
                 logger.info("Đã cập nhật phòng " + oldRoomId + ", số người -1");
             }
             // TH3: Chuyển từ phòng A -> phòng B (và vẫn đang điều trị)
             else if (oldRoomId != null && newRoomId != null && !oldRoomId.equals(newRoomId) && isCurrentlyOccupying) {
                 roomService.updateRoomOccupancy(oldRoomId, -1);
                 logger.info("Đã cập nhật phòng " + oldRoomId + ", số người -1");
                 try {
                      roomService.updateRoomOccupancy(newRoomId, +1);
                      logger.info("Đã cập nhật phòng " + newRoomId + ", số người +1");
                 } catch (SQLException exNew) {
                      logger.log(Level.SEVERE, "Lỗi cập nhật phòng mới " + newRoomId + " (+1). Rollback phòng cũ!", exNew);
                      try { roomService.updateRoomOccupancy(oldRoomId, +1); } catch (SQLException exRollback) {}
                      throw exNew;
                 }
             }

             populateComboBoxes();

         } catch (SQLException ex) {
              logger.log(Level.SEVERE, "Lỗi nghiêm trọng khi cập nhật trạng thái phòng", ex);
              JOptionPane.showMessageDialog(this, "Lỗi nghiêm trọng khi cập nhật trạng thái phòng:\n" + ex.getMessage(), "Lỗi Cập nhật Phòng", JOptionPane.ERROR_MESSAGE);
         }
     } 
    
    private String findRoomIdFromTableData(String recordIdToFind) {
         try {
             MedicalRecord record = medicalRecordService.getMedicalRecordById(recordIdToFind);
             if (record != null) {
                 return record.getRoomID();
             }
         } catch (Exception e) {
             logger.log(Level.WARNING, "Không thể lấy RoomID từ dữ liệu", e);
         }
        return null;
    }
    
    private void searchMedicalRecord() {
         String recordID = (jTextFieldSearch != null) ? jTextFieldSearch.getText().trim() : "";
         if (recordID.isEmpty()) {
             loadMedicalRecords(); // Tải lại tất cả nếu ô trống
             return;
         }

         MedicalRecord record = medicalRecordService.getMedicalRecordById(recordID);

         if (tableModel == null) return;
         tableModel.setRowCount(0);

         if (record != null) {
             tableModel.addRow(new Object[]{
                 record.getRecordID(),
                 record.getPatientName(),
                 record.getDoctorName(),
                 record.getRoomNumber() != null ? record.getRoomNumber() : "N/A",
                 record.getAdmissionDate() != null ? dateTimeFormat.format(record.getAdmissionDate()) : "",
                 record.getDischargeDate() != null ? dateTimeFormat.format(record.getDischargeDate()) : "",
                 record.getDiagnosis(),
                 record.getTreatmentPlan(),
                 record.getRecordStatus()
             });
         } else {
             JOptionPane.showMessageDialog(this, "Không tìm thấy bệnh án với Record ID: " + recordID, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         }
     }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMedicalRecords = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButtonBack = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldAdmissionDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldRecordID = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButtonUpdate = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldDischargeDate = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxDoctor = new javax.swing.JComboBox<>();
        jComboBoxPatient = new javax.swing.JComboBox<>();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaDiagnosis = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaTreatment = new javax.swing.JTextArea();
        jComboBoxRoom = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jButtonSearch = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();

        jMenuItem1.setText("jMenuItem1");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableMedicalRecords.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableMedicalRecords);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));

        jButtonBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/arrow.png"))); // NOI18N
        jButtonBack.setText("BACK");
        jButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackActionPerformed(evt);
            }
        });

        jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        jButtonRefresh.setText("REFRESH");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 255, 255));
        jLabel12.setText("QUẢN LÝ BỆNH ÁN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRefresh)
                .addGap(14, 14, 14))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(223, 223, 223)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(256, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBack)
                    .addComponent(jButtonRefresh))
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Ngày nhập viện ");

        jTextFieldAdmissionDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAdmissionDateActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Record ID");

        jTextFieldRecordID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRecordIDActionPerformed(evt);
            }
        });

        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add-button.png"))); // NOI18N
        jButtonAdd.setText("ADD");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Kế hoạch điều trị");

        jButtonUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit.png"))); // NOI18N
        jButtonUpdate.setText("UPDATE");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.png"))); // NOI18N
        jButtonDelete.setText("DELETE");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/eraser.png"))); // NOI18N
        jButtonClear.setText("CLEAR");
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Chuẩn đoán");

        jTextFieldDischargeDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDischargeDateActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Ngày xuất viện");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Room ID");

        jComboBoxDoctor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxPatient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Bác sĩ");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Bệnh nhân");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Trạng thái");

        jTextAreaDiagnosis.setColumns(20);
        jTextAreaDiagnosis.setRows(5);
        jScrollPane2.setViewportView(jTextAreaDiagnosis);

        jTextAreaTreatment.setColumns(20);
        jTextAreaTreatment.setRows(5);
        jScrollPane3.setViewportView(jTextAreaTreatment);

        jComboBoxRoom.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButtonAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonDelete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonClear))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel11))
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldDischargeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldRecordID, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldAdmissionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldRecordID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextFieldAdmissionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextFieldDischargeDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jComboBoxRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdd)
                    .addComponent(jButtonUpdate)
                    .addComponent(jButtonDelete)
                    .addComponent(jButtonClear))
                .addGap(15, 15, 15))
        );

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));

        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search.png"))); // NOI18N
        jButtonSearch.setText("Tìm kiếm");
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jTextFieldSearch.setToolTipText("Nhập ID bệnh nhân");
        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSearch)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearch))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 754, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldRecordIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRecordIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRecordIDActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        try {
            MedicalRecord record = getMedicalRecordFromFields();
            medicalRecordService.addMedicalRecord(record);
             if (record.getRoomID() != null && "Đang điều trị".equals(record.getRecordStatus())) {
                handleRoomUpdate(null, record.getRoomID(), record.getRecordStatus());
             }
            JOptionPane.showMessageDialog(this, "Thêm bệnh án thành công!");
            loadMedicalRecords();
            clearFields();
        } catch (IllegalArgumentException | ParseException ex) {
             JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
             if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Lỗi: Record ID '" + (jTextFieldRecordID != null ? jTextFieldRecordID.getText() : "") + "' đã tồn tại!", "Lỗi Trùng lặp", JOptionPane.ERROR_MESSAGE);
            } else if (ex.getMessage().contains("đầy")) {
                 JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi Phòng", JOptionPane.ERROR_MESSAGE);
            } else {
                 JOptionPane.showMessageDialog(this, "Lỗi khi thêm vào CSDL: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
             logger.log(Level.SEVERE, "Lỗi không xác định khi thêm bệnh án", ex);
             JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + ex.getMessage(), "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        // TODO add your handling code here:
        if (currentEditingRecordId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bệnh án từ bảng để cập nhật!", "Chưa chọn bệnh án", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            MedicalRecord record = getMedicalRecordFromFields();
            medicalRecordService.updateMedicalRecord(record);
            handleRoomUpdate(currentRoomId, record.getRoomID(), record.getRecordStatus());
            JOptionPane.showMessageDialog(this, "Cập nhật bệnh án thành công!");
            loadMedicalRecords();
            clearFields();
        } catch (IllegalArgumentException | ParseException ex) {
             JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            if (ex.getMessage().contains("đầy")) {
                 JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi Phòng", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật CSDL: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
             logger.log(Level.SEVERE, "Lỗi không xác định khi cập nhật bệnh án", ex);
             JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + ex.getMessage(), "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jTextFieldDischargeDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDischargeDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDischargeDateActionPerformed

    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
        // TODO add your handling code here:
       searchMedicalRecord();
    }//GEN-LAST:event_jTextFieldSearchActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // TODO add your handling code here:
        if (currentEditingRecordId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bệnh án từ bảng để xóa!", "Chưa chọn bệnh án", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa bệnh án '" + currentEditingRecordId + "' không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String statusBeforeDelete = (jComboBoxStatus != null) ? jComboBoxStatus.getSelectedItem().toString() : "";
                String roomIdBeforeDelete = currentRoomId;

                medicalRecordService.deleteMedicalRecord(currentEditingRecordId);

                if (roomIdBeforeDelete != null && "Đang điều trị".equals(statusBeforeDelete)) {
                     handleRoomUpdate(roomIdBeforeDelete, null, statusBeforeDelete);
                }

                JOptionPane.showMessageDialog(this, "Xóa bệnh án thành công!");
                loadMedicalRecords();
                clearFields();
            } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(this, "Lỗi khi xóa khỏi CSDL: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                 logger.log(Level.SEVERE, "Lỗi không xác định khi xóa bệnh án", ex);
                 JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + ex.getMessage(), "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        // TODO add your handling code here:
        clearFields();
        loadMedicalRecords();
    }//GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // TODO add your handling code here:
        searchMedicalRecord();
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jTextFieldAdmissionDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAdmissionDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAdmissionDateActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        handleDischargeAction();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        // TODO add your handling code here:
        loadMedicalRecords();
        populateComboBoxes();
        clearFields();
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new medicalRecord().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonBack;
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JComboBox<String> jComboBoxDoctor;
    private javax.swing.JComboBox<String> jComboBoxPatient;
    private javax.swing.JComboBox<String> jComboBoxRoom;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableMedicalRecords;
    private javax.swing.JTextArea jTextAreaDiagnosis;
    private javax.swing.JTextArea jTextAreaTreatment;
    private javax.swing.JTextField jTextFieldAdmissionDate;
    private javax.swing.JTextField jTextFieldDischargeDate;
    private javax.swing.JTextField jTextFieldRecordID;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
