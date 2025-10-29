/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;
import model.Room;
import model.Patient;
import service.RoomService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author LOQ
 */
public class roompanel extends javax.swing.JPanel {
    private static final Logger logger = Logger.getLogger(roompanel.class.getName());
    private RoomService roomService = new RoomService(); // Khởi tạo Service
    private final Color COLOR_EMPTY = new Color(198, 239, 206); // Xanh lá nhạt (Trống)
    private final Color COLOR_AVAILABLE = new Color(255, 243, 205); // Vàng nhạt (Còn chỗ)
    private final Color COLOR_FULL = new Color(255, 205, 210); // Đỏ nhạt (Đầy)
    private final SimpleDateFormat dobFormat = new SimpleDateFormat("dd/MM/yyyy");

    private Room roomData;
    /**
     * Creates new form roompanel1
     */
    public roompanel() {
        initComponents();
        
        setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Thêm viền
        setPreferredSize(new Dimension(120, 100));
    }

    public roompanel(Room room) {
        initComponents(); // Khởi tạo các JLabel đã kéo thả
        this.roomData = room;

        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setPreferredSize(new Dimension(120, 100)); // Đặt kích thước

        displayRoomInfo(); // Hiển thị thông tin
        setupClickListener(); // Thêm sự kiện click
    }
    
    private void displayRoomInfo() {
        if (roomData == null) return;

        // Đảm bảo các JLabel đã được khởi tạo (từ initComponents)
        if (numberLabel != null) numberLabel.setText("Phòng: " + roomData.getRoomNumber());
        if (typeLabel != null) typeLabel.setText(roomData.getRoomType());
        if (occupancyLabel != null) occupancyLabel.setText("Số người: " + roomData.getCurrentOccupancy() + "/" + roomData.getCapacity());
        if (statusLabel != null) statusLabel.setText("Trạng thái: " + roomData.getStatus());

        // --- Logic đặt màu nền ---
        Color backgroundColor = Color.LIGHT_GRAY; // Màu mặc định
        if (roomData.getStatus() != null) {
            String statusLower = roomData.getStatus().toLowerCase();
             // Ưu tiên kiểm tra số lượng trước
             if (roomData.getCurrentOccupancy() == 0) {
                 backgroundColor = COLOR_EMPTY;
             } else if (roomData.getCurrentOccupancy() < roomData.getCapacity()) {
                 backgroundColor = COLOR_AVAILABLE;
             } else { // >= capacity
                 backgroundColor = COLOR_FULL;
             }
             // Ghi đè nếu trạng thái là đầy/trống rõ ràng (dù số lượng có thể khác)
             if ("trống".equals(statusLower)) {
                 backgroundColor = COLOR_EMPTY;
             } else if ("đầy".equals(statusLower)) {
                 backgroundColor = COLOR_FULL;
             }
        }
        setBackground(backgroundColor);
        updateLabelForeground(backgroundColor); // Cập nhật màu chữ
    }

    /** Cập nhật màu chữ của JLabel để dễ đọc trên nền */
    private void updateLabelForeground(Color background) {
        double luminance = (0.299 * background.getRed() + 0.587 * background.getGreen() + 0.114 * background.getBlue()) / 255;
        Color foreground = (luminance > 0.5) ? Color.BLACK : Color.WHITE;

        if (numberLabel != null) numberLabel.setForeground(foreground);
        if (typeLabel != null) typeLabel.setForeground(foreground);
        if (occupancyLabel != null) occupancyLabel.setForeground(foreground);
        if (statusLabel != null) statusLabel.setForeground(foreground);
    }


    /** Thêm MouseListener để xử lý sự kiện click */
    private void setupClickListener() {
        // Chỉ cho phép click nếu phòng đang có người
        if (roomData != null && roomData.getCurrentOccupancy() > 0) {
            setCursor(new Cursor(Cursor.HAND_CURSOR)); // Đổi con trỏ

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPatientDetails();
                }
                // --- THÊM MỚI: Hiệu ứng khi di chuột ---
                 @Override
                 public void mouseEntered(MouseEvent e) {
                     setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Viền xanh đậm khi di vào
                 }
                 @Override
                 public void mouseExited(MouseEvent e) {
                     setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Trở lại viền xám
                 }
                 // --- KẾT THÚC THÊM MỚI ---
            });
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            setToolTipText("Phòng trống"); // Gợi ý
        }
    }

    /** Lấy danh sách bệnh nhân và hiển thị trong Dialog */
    private void showPatientDetails() {
        if (roomData == null) return;

        try {
            List<Patient> patientsInRoom = roomService.getPatientsInRoom(roomData.getRoomID());
            // Lấy Frame cha (home) để hiển thị Dialog ở giữa
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
//            patientDetailDialog dialog = new patientDetailDialog(parentFrame, true, roomData.getRoomNumber(), patientsInRoom);
//            dialog.setVisible(true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Lỗi khi lấy hoặc hiển thị chi tiết bệnh nhân phòng " + roomData.getRoomNumber(), ex);
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xem chi tiết bệnh nhân:\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        numberLabel = new javax.swing.JLabel();
        occupancyLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setPreferredSize(new java.awt.Dimension(120, 100));
        setLayout(new java.awt.GridLayout(4, 1));

        numberLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numberLabel.setText(" Phòng : ");
        numberLabel.setAlignmentX(0.5F);
        numberLabel.setFocusable(false);
        add(numberLabel);

        occupancyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        occupancyLabel.setText("Số người :");
        occupancyLabel.setAlignmentX(0.5F);
        add(occupancyLabel);

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setText("Trạng thái : ");
        statusLabel.setAlignmentX(0.5F);
        add(statusLabel);

        typeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        typeLabel.setText("Loại : ");
        typeLabel.setAlignmentX(0.5F);
        typeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                typeLabelMouseEntered(evt);
            }
        });
        add(typeLabel);
    }// </editor-fold>//GEN-END:initComponents

    private void typeLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_typeLabelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_typeLabelMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel numberLabel;
    private javax.swing.JLabel occupancyLabel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}
