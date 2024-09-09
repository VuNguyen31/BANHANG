package BTL;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
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

public class DONHANG extends JFrame {

    private JTextField maDHField, maKHField, ngayDatHangField, tongTienField, maKMField;
    private JButton addButton, editButton, deleteButton, saveButton;
    private JTable orderTable;
    private DefaultTableModel orderModel;

    public DONHANG() {
        setTitle("Quản lý đơn hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel to input order details
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));

        maDHField = new JTextField(10);
        maKHField = new JTextField(20);
        ngayDatHangField = new JTextField(20);
        tongTienField = new JTextField(20);
        maKMField = new JTextField(10);

        inputPanel.add(new JLabel("Mã đơn hàng:"));
        inputPanel.add(maDHField);
        inputPanel.add(new JLabel("Mã khách hàng:"));
        inputPanel.add(maKHField);
        inputPanel.add(new JLabel("Ngày đặt hàng:"));
        inputPanel.add(ngayDatHangField);
        inputPanel.add(new JLabel("Tổng tiền:"));
        inputPanel.add(tongTienField);
        inputPanel.add(new JLabel("Mã khuyến mãi:"));
        inputPanel.add(maKMField);

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

        // Table to display orders
        String[] columns = {"Mã ĐH", "Mã KH", "Ngày đặt hàng", "Tổng tiền", "Mã KM"};
        orderModel = new DefaultTableModel(columns, 0);
        orderTable = new JTable(orderModel);
        JScrollPane tableScrollPane = new JScrollPane(orderTable);

        // Adding components to main frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        setVisible(true);

        // Adding action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrder();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editOrder();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOrder();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrder();
            }
        });

        loadOrders();
    }

    // Method to add an order
    private void addOrder() {
        String maDH = maDHField.getText();
        String maKH = maKHField.getText();
        String ngayDatHang = ngayDatHangField.getText();
        String tongTien = tongTienField.getText();
        String maKM = maKMField.getText();

        // Add order to the database
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "INSERT INTO DONHANG (MADH, MAKH, NGAYDATHANG, TONGTIEN, MAKHUYENMAI) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(maDH));
            stmt.setInt(2, Integer.parseInt(maKH));
            stmt.setDate(3, Date.valueOf(ngayDatHang));
            stmt.setBigDecimal(4, new BigDecimal(tongTien));
            stmt.setString(5, maKM);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm đơn hàng thành công!");
            loadOrders();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm đơn hàng: " + e.getMessage());
        }
    }

    // Method to edit an order
    private void editOrder() {
        // Similar logic to update a selected order
    }

    // Method to delete an order
    private void deleteOrder() {
        // Similar logic to delete a selected order
    }

    // Method to save the order (after adding/editing)
    private void saveOrder() {
        // Logic to save the changes to the database
    }

    // Method to load orders from the database
    private void loadOrders() {
        orderModel.setRowCount(0); // Clear table before loading
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM DONHANG";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int maDH = rs.getInt("MADH");
                int maKH = rs.getInt("MAKH");
                Date ngayDatHang = rs.getDate("NGAYDATHANG");
                BigDecimal tongTien = rs.getBigDecimal("TONGTIEN");
                String maKM = rs.getString("MAKHUYENMAI");

                orderModel.addRow(new Object[]{maDH, maKH, ngayDatHang, tongTien, maKM});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải đơn hàng: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DONHANG::new);
    }
}
