/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import model.MedicalRecord;
import model.Room;
import service.MedicalRecordService;
import service.RoomService;
import java.math.BigDecimal;
import javax.swing.*;
// --- SỬA: Import Frame thay vì chỉ AWT ---
import java.awt.Color;
import java.awt.Frame;
// --- KẾT THÚC SỬA ---
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author LOQ
 */
public class payment extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(payment.class.getName());
     private MedicalRecord currentRecord;
    private medicalRecord parentForm; // Tham chiếu đến form cha
    private MedicalRecordService medicalRecordService;
    private RoomService roomService;
    private DecimalFormat currencyFormat = new DecimalFormat("#,##0 VND");
    private BigDecimal totalCostDecimal = BigDecimal.ZERO;

    /**
     * Creates new form payment
     */
    public payment() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Sửa thành DISPOSE
         setLocationRelativeTo(null); // Căn giữa
         setTitle("Thanh toán");
    }
    
    public payment(Frame owner, MedicalRecord record, BigDecimal totalCostDecimal) {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner); // Căn giữa theo Frame cha
        setTitle("Xác nhận Thanh toán cho BA: " + record.getRecordID());

        // --- Khởi tạo ---
        this.currentRecord = record;
        this.medicalRecordService = new MedicalRecordService();
        this.roomService = new RoomService();
        if (owner instanceof medicalRecord) {
            this.parentForm = (medicalRecord) owner;
        }

        // --- Hiển thị tổng tiền ---
        this.totalCostDecimal = totalCostDecimal != null ? totalCostDecimal : BigDecimal.ZERO; // Đảm bảo không null
        if (jTextField1 != null) {
            jTextField1.setText(currencyFormat.format(this.totalCostDecimal));
             jTextField1.setEditable(false);
             jTextField1.setBackground(Color.LIGHT_GRAY);
             jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        }

        // --- Thêm sự kiện cho nút Xác nhận ---
        setupActionListener();
    }
    
    private void setupActionListener() {
        if (jButton1 != null) {
            // Xóa listener cũ nếu có
            for(ActionListener al : jButton1.getActionListeners()) {
                 jButton1.removeActionListener(al);
            }
            // Thêm listener mới
            jButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    confirmDischargeAndPayment();
                }
            });
        } else {
             logger.warning("jButton1 (Nút Xác nhận) chưa được khởi tạo!");
        }
    }
    
      private void confirmDischargeAndPayment() {
        if (currentRecord == null) {
            JOptionPane.showMessageDialog(this, "Thiếu thông tin bệnh án.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận đã thanh toán đủ ("+ currencyFormat.format(totalCostDecimal) + ") và thực hiện xuất viện cho bệnh án " + currentRecord.getRecordID() + "?",
                "Xác nhận Xuất viện",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // 1. Cập nhật MedicalRecord
                Timestamp dischargeTime = new Timestamp(new Date().getTime());
                currentRecord.setDischargeDate(dischargeTime);
                currentRecord.setRecordStatus("Đã xuất viện");
                medicalRecordService.updateMedicalRecord(currentRecord);

                // 2. Cập nhật Room (nếu có)
                String roomIdToUpdate = currentRecord.getRoomID();
                if (roomIdToUpdate != null) {
                     try {
                        roomService.updateRoomOccupancy(roomIdToUpdate, -1);
                        logger.info("Đã cập nhật phòng " + roomIdToUpdate + ", số người -1");
                     } catch (SQLException roomEx) {
                         logger.log(Level.SEVERE, "Lỗi khi cập nhật phòng " + roomIdToUpdate + " (-1)", roomEx);
                         JOptionPane.showMessageDialog(this, "Đã cập nhật bệnh án nhưng có lỗi khi cập nhật trạng thái phòng:\n" + roomEx.getMessage(), "Lỗi Cập nhật Phòng", JOptionPane.WARNING_MESSAGE);
                     }
                }

                JOptionPane.showMessageDialog(this, "Đã xuất viện và cập nhật trạng thái thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                // 3. Gọi refreshData() của form cha
                if (parentForm != null) {
                    parentForm.refreshData();
                }

                // 4. Đóng cửa sổ payment
                dispose();

            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Lỗi SQL khi cập nhật xuất viện cho Record ID: " + currentRecord.getRecordID(), ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật CSDL:\n" + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                 logger.log(Level.SEVERE, "Lỗi không xác định khi xuất viện", ex);
                 JOptionPane.showMessageDialog(this, "Lỗi không xác định:\n" + ex.getMessage(), "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
            }
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(241, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THANH TOÁN");

        jLabel2.setText("Tiền mặt (VND) :");

        jTextField1.setEnabled(false);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 204, 255));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Xác nhận ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new payment().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
