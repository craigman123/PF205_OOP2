/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author user
 */
public class config {
    
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:armex.db"); // Establish connection
            
            Statement stmt = con.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }
    
    public int addRecordAndReturnId(String query, Object... params) {
        int generatedId = -1;
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting record: " + e.getMessage());
        }
        return generatedId;
    }
    
    public String hashPassword(String password) {
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        
        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
        System.out.println("Error hashing password: " + e.getMessage());
        return null;
        }
    }
    
    public java.util.List<java.util.Map<String, Object>> fetchRecords(String sqlQuery, Object... values) {
    java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                java.util.Map<String, Object> row = new java.util.HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                records.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching records: " + e.getMessage());
        }

        return records;
    }
    
//    public void displayData(String sql, javax.swing.JTable table) {
//        try (Connection conn = connectDB();
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            // This line automatically maps the Resultset to your JTable
//            table.setModel(DbUtils.resultSetToTableModel(rs));
//
//        } catch (SQLException e) {
//            System.out.println("Error displaying data: " + e.getMessage());
//        }
//    }
    
    
    private void addDeleteButton(JTable table) {

    TableColumn actionColumn = table.getColumn("Action");

    actionColumn.setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
        JButton btn = new JButton("Delete");
        btn.setBackground(Color.RED);
        btn.setForeground(Color.WHITE);
        return btn;
    });

    actionColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()) {

        private final JButton button = new JButton("Delete");
        private int row;

        {
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);

            button.addActionListener(e -> {
                int id = Integer.parseInt(
                        table.getValueAt(row, 0).toString() 
                );

                int confirm = JOptionPane.showConfirmDialog(
                        table,
                        "Delete this user?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteUserFromDatabase(id);
                    ((DefaultTableModel) table.getModel()).removeRow(row);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }
    });
}
    
    private void deleteUserFromDatabase(int id) {
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM users WHERE user_id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void displayData(String sql, JTable table) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        TableModel dbModel = DbUtils.resultSetToTableModel(rs);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };

        for (int i = 0; i < dbModel.getColumnCount(); i++) {
            model.addColumn(dbModel.getColumnName(i));
        }

        model.addColumn("Action");

        for (int row = 0; row < dbModel.getRowCount(); row++) {
            Object[] rowData = new Object[model.getColumnCount()];
            for (int col = 0; col < dbModel.getColumnCount(); col++) {
                rowData[col] = dbModel.getValueAt(row, col);
            }
            rowData[rowData.length - 1] = "Delete";
            model.addRow(rowData);
        }

        table.setModel(model);
        addDeleteButton(table);

    } catch (SQLException e) {
        System.out.println("Error displaying data: " + e.getMessage());
    }
}
    
//    public void hideHashColumn(JTable table, String columnName) {
//        int colIndex = table.getColumnModel().getColumnIndex(columnName);
//
//        table.getColumnModel().getColumn(colIndex).setCellRenderer(new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(
//                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//
//                super.getTableCellRendererComponent(table, "Hashed", isSelected, hasFocus, row, column);
//                setHorizontalAlignment(SwingConstants.CENTER);
//                return this;
//            }
//        });
//    }


}
