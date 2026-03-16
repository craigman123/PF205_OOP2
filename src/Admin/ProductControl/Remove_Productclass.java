/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.ProductControl;

import configuration.config;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class Remove_Productclass {
    public static void remove(){
        config conf = new config();
        
        Object data = connector.datasender();

        // Validate selection
        if (data == null) {
            JOptionPane.showMessageDialog(
                null,
                "Please select a product from the table!",
                "Selection Required",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Validate type
        if (!(data instanceof Integer)) {
            JOptionPane.showMessageDialog(
                null,
                "Invalid product ID!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int id = (Integer) data;
        System.out.println(id);

        if (id <= 0) {
            JOptionPane.showMessageDialog(
                null,
                "Invalid product ID!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String qry = "DELETE FROM products WHERE prod_id = ?";
        boolean deleted = conf.deleteRecord(qry, id);
        
        if (deleted) {
            JOptionPane.showMessageDialog(
                null,
                "Product successfully removed.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
                
                
            );
            JDesktopPane desktop = connector.PanelSender();
            desktop.removeAll();
        } else {
            JOptionPane.showMessageDialog(
                null,
                "Product removal failed.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
