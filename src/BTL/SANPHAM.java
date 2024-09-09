package BTL;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class SANPHAM extends JFrame {

    private JTextField maSPField, tenSPField, giaField, moTaField, maNCCField;
    private JButton addButton, editButton, deleteButton, saveButton;
    private JTable productTable;
    private DefaultTableModel productModel;

    public SANPHAM() {
        setTitle("Quản lý sản phẩm");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel to input product details
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));

        maSPField = new JTextField(10);
        tenSPField = new JTextField(20);
        giaField = new JTextField(20);
        moTaField = new JTextField(30);
        maNCCField = new JTextField(10);

        inputPanel.add(new JLabel("Mã sản phẩm:"));
        inputPanel.add(maSPField);
        inputPanel.add(new JLabel("Tên sản phẩm:"));
        inputPanel.add(tenSPField);
        inputPanel.add(new JLabel("Giá:"));
        inputPanel.add(giaField);
        inputPanel.add(new JLabel("Mô tả:"));
        inputPanel.add(moTaField);
        inputPanel.add(new JLabel("Mã nhà cung cấp:"));
        inputPanel.add(maNCCField);

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

        // Table to display products
        String[] columns = {"Mã SP", "Tên SP", "Giá", "Mô tả", "Mã NCC"};
        productModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(productModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);

        // Adding components to main frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        setVisible(true);

        // Adding action listeners for buttons
        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        saveButton.addActionListener(e -> saveProduct());

        // Adding selection listener to the table
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                    int selectedRow = productTable.getSelectedRow();
                    maSPField.setText(productModel.getValueAt(selectedRow, 0).toString());
                    tenSPField.setText(productModel.getValueAt(selectedRow, 1).toString());
                    giaField.setText(productModel.getValueAt(selectedRow, 2).toString());
                    moTaField.setText(productModel.getValueAt(selectedRow, 3).toString());
                    maNCCField.setText(productModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        loadProducts();
    }

    // Method to add a product
    private void addProduct() {
        String maSP = maSPField.getText();
        String tenSP = tenSPField.getText();
        String gia = giaField.getText();
        String moTa = moTaField.getText();
        String maNCC = maNCCField.getText();

        // Add product to the database
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "INSERT INTO SANPHAM (MASP, TENSP, GIA, MOTA, MANCC) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(maSP));
            stmt.setString(2, tenSP);
            stmt.setBigDecimal(3, new BigDecimal(gia));
            stmt.setString(4, moTa);
            stmt.setInt(5, Integer.parseInt(maNCC));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");

            // Refresh product list
            loadProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm sản phẩm: " + e.getMessage());
        }
    }

    // Method to edit a product
    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để sửa!");
            return;
        }

        String maSP = maSPField.getText();
        String tenSP = tenSPField.getText();
        String gia = giaField.getText();
        String moTa = moTaField.getText();
        String maNCC = maNCCField.getText();

        try (Connection conn = DatabaseConnection.connect()) {
            String query = "UPDATE SANPHAM SET TENSP=?, GIA=?, MOTA=?, MANCC=? WHERE MASP=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, tenSP);
            stmt.setBigDecimal(2, new BigDecimal(gia));
            stmt.setString(3, moTa);
            stmt.setInt(4, Integer.parseInt(maNCC));
            stmt.setInt(5, Integer.parseInt(maSP));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sản phẩm đã được sửa thành công!");

            // Refresh product list
            loadProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa sản phẩm: " + e.getMessage());
        }
    }

    // Method to delete a product
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để xóa!");
            return;
        }

        String maSP = productModel.getValueAt(selectedRow, 0).toString();
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "DELETE FROM SANPHAM WHERE MASP=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(maSP));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sản phẩm đã được xóa thành công!");

            // Refresh product list
            loadProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa sản phẩm: " + e.getMessage());
        }
    }

    // Method to save the product (this could be used for further operations if necessary)
    private void saveProduct() {
        // Saving logic can be implemented for more complex functionality.
        JOptionPane.showMessageDialog(this, "Lưu sản phẩm thành công!");
    }

    // Method to load products from the database
    private void loadProducts() {
        productModel.setRowCount(0); // Clear table before loading
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM SANPHAM";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int maSP = rs.getInt("MASP");
                String tenSP = rs.getString("TENSP");
                BigDecimal gia = rs.getBigDecimal("GIA");
                String moTa = rs.getString("MOTA");
                int maNCC = rs.getInt("MANCC");

                productModel.addRow(new Object[]{maSP, tenSP, gia, moTa, maNCC});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải sản phẩm: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SANPHAM::new);
    }
}
