/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.UserControl;

import Profiles.session;
import configuration.config;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class Remove_Userclass {

    public static void remove() {
        session see = new session();
        config conf = new config();
        int sessionID = see.GetID();

        Object data = connector.DataSender();

        // Validate selection
        if (data == null) {
            JOptionPane.showMessageDialog(
                null,
                "Please select a User from the table!",
                "Selection Required",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Validate type
        if (!(data instanceof Integer)) {
            JOptionPane.showMessageDialog(
                null,
                "Invalid User ID!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int id = (int) data;

        if(sessionID != id){
            if (id <= 0) {
                JOptionPane.showMessageDialog(
                    null,
                    "Invalid User ID!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String qry = "DELETE FROM users WHERE user_id = ?";
            boolean deleted = conf.deleteRecord(qry, id);
            
            LocalDateTime now = LocalDateTime.now();
            Timestamp date = Timestamp.valueOf(now);
            String queryNow = "INSERT INTO logs(user_id, dateTime, log_action) VALUES(?,?,?)";
            conf.addRecordAndReturnId(queryNow, id, date, "Deleted");

            if (deleted) {
                JOptionPane.showMessageDialog(
                    null,
                    "User successfully removed.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                JDesktopPane desktop = connector.panelsender();

            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "User removal failed.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }else{
            JOptionPane.showMessageDialog(
                    null,
                    "User in Session: Log out first. Try other Account!",
                    "Deletion Error",
                    JOptionPane.ERROR_MESSAGE
                );
        }
    }
}