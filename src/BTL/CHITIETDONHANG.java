package BTL;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CHITIETDONHANG extends JFrame {

    private JTextField maDHField, maSPField, soLuongField, donGiaField;
    private JButton addButton, editButton, deleteButton, saveButton;
    private JTable chiTietTable;
    private DefaultTableModel chiTietModel;

    public CHITIETDONHANG() {
        setTitle("Quản lý chi tiết đơn hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel to input order details
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));

        maDHField = new JTextField(10);
        maSPField = new JTextField(20);
        soLuongField = new JTextField(20);
        donGiaField = new JTextField(20);

        inputPanel.add(new JLabel("Mã đơn hàng:"));
        inputPanel.add(maDHField);
        inputPanel.add(new JLabel("Mã sản phẩm:"));
        inputPanel.add(maSPField);
        inputPanel.add(new JLabel("Số lượng:"));
        inputPanel.add(soLuongField);
        inputPanel.add(new JLabel("Đơn giá:"));
        inputPanel.add(donGiaField);

        // Button panel for operations
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Thêm");
        editButton = new JButton("Sửa");
        deleteButton = new JButton("Xóa");
        saveButton = new JButton("Lưu");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        // Table to display order details
        String[] columns = {"Mã ĐH", "Mã SP", "Số lượng", "Đơn giá"};
        chiTietModel = new DefaultTableModel(columns, 0);
        chiTietTable = new JTable(chiTietModel);
        JScrollPane tableScrollPane = new JScrollPane(chiTietTable);

        // Adding components to main frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        setVisible(true);

        // Adding action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addChiTietDonHang();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editChiTietDonHang();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteChiTietDonHang();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChiTietDonHang();
            }
        });

        loadChiTietDonHang();
    }

    // Method to add an order detail
    private void addChiTietDonHang() {
        String maDH = maDHField.getText();
        String maSP = maSPField.getText();
        String soLuong = soLuongField.getText();
        String donGia = donGiaField.getText();

        // Add order detail to the database
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "INSERT INTO CHITIETDONHANG (MADH, MASP, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(maDH));
            stmt.setInt(2, Integer.parseInt(maSP));
            stmt.setInt(3, Integer.parseInt(soLuong));
            stmt.setBigDecimal(4, new BigDecimal(donGia));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm chi tiết đơn hàng thành công!");
            loadChiTietDonHang();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm chi tiết đơn hàng: " + e.getMessage());
        }
    }

    // Method to edit an order detail
    private void editChiTietDonHang() {
        // Similar logic to update a selected order detail
    }

    // Method to delete an order detail
    private void deleteChiTietDonHang() {
        // Similar logic to delete a selected order detail
    }

    // Method to save the order detail (after adding/editing)
    private void saveChiTietDonHang() {
        // Logic to save the changes to the database
    }

    // Method to load order details from the database
    private void loadChiTietDonHang() {
        chiTietModel.setRowCount(0); // Clear table before loading
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM CHITIETDONHANG";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int maDH = rs.getInt("MADH");
                int maSP = rs.getInt("MASP");
                int soLuong = rs.getInt("SOLUONG");
                BigDecimal donGia = rs.getBigDecimal("DONGIA");

                chiTietModel.addRow(new Object[]{maDH, maSP, soLuong, donGia});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chi tiết đơn hàng: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CHITIETDONHANG::new);
    }
}
