/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import static configuration.config.connectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JTable;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author user
 */
public class Admin_config {
    
    public void displayData(String sql, JTable table) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        table.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
    }
    
    public static void DisplayStatus(JComboBox<String> statusCombo){
        statusCombo.removeAllItems();
    
        statusCombo.addItem("All");
        statusCombo.addItem("Active");
        statusCombo.addItem("Inactive");
        statusCombo.addItem("Pending");
        statusCombo.addItem("Suspended");
        statusCombo.addItem("Sold Out");
        statusCombo.addItem("Archived");
    }
}
