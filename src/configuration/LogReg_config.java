/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import Admin.AdminDashboard;
import dispatcher.DispatcherDashboard;
import Profiles.session;
import User.UserDashboard;
import User.User_Permission;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author user
 */
public class LogReg_config {
        
    public static int Register(String nm, String bdg, String ps, JFrame frame){
        config conf = new config();
        animation ani = new animation();
        StringBuilder errors = new StringBuilder();
        int Finalbadge = 0;
        boolean loop = true;
        System.out.println(ps);
        
        while(loop){
            try{
                
                if (ps == null || ps.trim().isEmpty() || ps.equals("Password")) {
                    errors.append("• Password cannot be empty.\n");
                }
                
                if (nm == null || nm.trim().isEmpty() || nm.equals("Username")) {
                    errors.append("• Username cannot be empty.\n");
                }

                if (bdg == null || bdg.trim().isEmpty() || bdg.equals("Badge")) {
                    errors.append("• Badge cannot be empty.\n");
                }else{
                    Finalbadge = Integer.parseInt(bdg);
                }

                loop = false;
            }catch(NumberFormatException e){
                errors.append("• Warning at Badge\n");
                errors.append("• Minimum 10 Items\n");
                errors.append("• Should be Digits\n");

                JOptionPane.showMessageDialog(
                    null,
                    errors.toString(),
                    "Validation Errors",
                    JOptionPane.ERROR_MESSAGE
                );

                return 0;
            }
        }
            
        if(errors.length() > 0) {
            JOptionPane.showMessageDialog(
                null,
                errors.toString(),
                "Validation Errors",
                JOptionPane.ERROR_MESSAGE
            );
        }else{

        String qry = "SELECT * FROM users WHERE user_badge = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, bdg);
        
        int valuebadge = bdg.length();
        int valuepass = ps.length();

        if (valuebadge < 4) {
            errors.append("• Badge must be at least 4 digits long\n");
        }

        if (!result.isEmpty()) {
            errors.append("• Badge already exists\n");
        }

        if (valuepass < 0) {
            errors.append("• Invalid password\n");
        }

        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(
                null,
                errors.toString(),
                "Validation Errors",
                JOptionPane.ERROR_MESSAGE
            );
     
        }else{
            User_Permission perms = new User_Permission(nm, Finalbadge, ps, frame);
            perms.setVisible(true);
            System.out.println("Account Created: " + ps);
            
            ani.BuyTimeLoaddingFrame();
           
        }
        
                return 0;
            }
        return 0;
    }
    
    public static int DetailsCompleted(String nm, int Finalbadge, String FinalPass){
        config conf = new config();

        JFrame successFrame = new JFrame("Success");
        successFrame.setSize(350, 200);
        successFrame.setLocationRelativeTo(null);
        successFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Account created successfully!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> successFrame.dispose());

        JPanel panel = new JPanel(new BorderLayout());
        okButton.setSize(300, 20);
        panel.add(label, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);

        String sql = "INSERT INTO users(user_name, user_hashpass, user_badge, user_access, user_ussage, system_access) VALUES (?,?,?,?,?,?)";
        int id = conf.addRecordAndReturnId(sql, nm, FinalPass, Finalbadge, "User", "Disable", "open");
        
        LocalDateTime now = LocalDateTime.now();
        Timestamp date = Timestamp.valueOf(now);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        String queryNow = "INSERT INTO notification(user_id, n_content, date, read) VALUES (?, ?,?, ?)";
        conf.addRecordAndReturnId(queryNow, id, "Successfully Registered", formattedDate, false);

        queryNow = "INSERT INTO logs(user_id, dateTime, log_action) VALUES(?,?,?)";
        conf.addRecordAndReturnId(queryNow, id, formattedDate, "Register");

        successFrame.add(panel);
        successFrame.setVisible(true);

        return 0;
    }
    
    public static int LogIn(String nm, String badge, String ps, JFrame currentFrame){
        config conf = new config();
        session see = new session();
        StringBuilder errors = new StringBuilder();
        boolean loop = true;
        int Finalbadge = 0;
        
        String hashPass = conf.hashPassword(ps);
        
        while(loop){
            try{
                if (nm == null || nm.trim().isEmpty() || nm.equals("Username")) {
                    errors.append("• Username cannot be empty.\n");
                }

                if (badge == null || badge.trim().isEmpty() || badge.equals("Badge")) {
                    errors.append("• Badge cannot be empty.\n");
                }else{
                    Finalbadge = Integer.parseInt(badge);
                }

                if (ps == null || ps.trim().isEmpty() || ps.equals("Password")) {
                    errors.append("• Password cannot be empty.\n");
                }

                loop = false;
            }catch(NumberFormatException e){
                errors.append("• Warning at Badge\n");
                errors.append("• Minimum 10 Items\n");
                errors.append("• Should be Digits\n");

                JOptionPane.showMessageDialog(
                    null,
                    errors.toString(),
                    "Validation Errors",
                    JOptionPane.ERROR_MESSAGE
                );

                return 0;
            }
        }
        
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(
                null, 
                errors.toString(),
                "Validation Errors", 
                JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        String qry = "SELECT * FROM users WHERE user_name = ? AND user_badge = ? AND user_hashpass = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, nm, badge, hashPass); 
        
        if(nm.equals("Username") || ps.equals("Password") || nm.equals("") || ps.equals("")){
            JOptionPane.showMessageDialog(
                null,
                "Enter Account!",
                "Empty Fields",
                JOptionPane.ERROR_MESSAGE
            );
        }else if (result.isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "Account does not Exist!",
                "Account Error",
                JOptionPane.ERROR_MESSAGE
            );
        } else {
            java.util.Map<String, Object> user = result.get(0);
            String access = user.get("user_access").toString();
            String systemAccess = user.get("system_access").toString();
            int id = ((Number) user.get("user_id")).intValue();
            
            JFrame Dashboard = null;
            
            if(systemAccess.equals("open")){
                switch(access){
                    case "Admin":
                        see.SaveLogIn(id);
                        Dashboard = new AdminDashboard();
                        
                        break;
                    case "User":
                        see.SaveLogIn(id);
                        Dashboard = new UserDashboard();
                    
                        break;
                    case "Dispatcher":
                        see.SaveLogIn(id);
                        Dashboard = new DispatcherDashboard();
                        
                        break;
                }
            } else {
                int close = showTimedDialog();
                if(close == 3){
                    System.exit(0);
                }
                return 0;
            }
                
                LocalDateTime now = LocalDateTime.now();
                Timestamp date = Timestamp.valueOf(now);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = now.format(formatter);
                String queryNow = "INSERT INTO notification(user_id, n_content, date, read) VALUES (?, ?,?, ?)";
                conf.addRecordAndReturnId(queryNow, see.GetID(), "Successfully Logged In:", formattedDate, false);
                
                queryNow = "INSERT INTO logs(user_id, dateTime, log_action) VALUES(?,?,?)";
                conf.addRecordAndReturnId(queryNow, see.GetID(), formattedDate, "Logged In");
                
                animation.showLoadingAndOpen(currentFrame, Dashboard, "Logging In . . .");
        }
        
        return 0;
    }
    
    public static void RegisterByAdmin(String nm, String bdg, String ps, String access, String ussage, String acc) {
    config conf = new config();
    StringBuilder errors = new StringBuilder();
    int Finalbadge = 0;

    if (nm == null || nm.trim().isEmpty() || nm.equals("Username")) {
        errors.append("• Username cannot be empty.\n");
    }

    if (bdg == null || bdg.trim().isEmpty() || bdg.equals("Badge")) {
        errors.append("• Badge cannot be empty.\n");
    } else {
        try {
            Finalbadge = Integer.parseInt(bdg);
            if (bdg.length() < 4) {
                errors.append("• Badge must be at least 4 digits long\n");
            }
        } catch (NumberFormatException e) {
            errors.append("• Badge must be digits only\n");
        }
    }

    if (ps == null || ps.trim().isEmpty() || ps.equals("Password")) {
        errors.append("• Password cannot be empty.\n");
    } else if (ps.length() < 4) {
        errors.append("• Password must be at least 4 characters long\n");
    }

    if (errors.length() == 0) {
        String qry = "SELECT * FROM users WHERE user_badge = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, bdg);

        if (!result.isEmpty()) {
            errors.append("• Badge already exists\n");
        }
    }

    if (errors.length() > 0) {
        JOptionPane.showMessageDialog(
            null,
            errors.toString(),
            "Validation Errors",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }else{
    
        String hashpass = conf.hashPassword(ps);
        String qry = "INSERT INTO users(user_name, user_badge, user_hashpass, user_access, user_ussage, system_access) VALUES(?,?,?,?,?,?)";
        int id = conf.addRecordAndReturnId(qry, nm, Finalbadge, hashpass, access, ussage, acc);
        
        LocalDateTime now = LocalDateTime.now();
        Timestamp date = Timestamp.valueOf(now);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        String queryNow = "INSERT INTO notification(user_id, n_content, date, read) VALUES (?, ?,?, ?)";
        conf.addRecordAndReturnId(queryNow, id, "Successfully Logged In:", formattedDate, false);

        queryNow = "INSERT INTO logs(user_id, dateTime, log_action) VALUES(?,?,?)";
        conf.addRecordAndReturnId(queryNow, id, formattedDate, "Logged In");

        JOptionPane.showMessageDialog(
            null,
            "User added successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
    
    public static int showTimedDialog() {
        final int[] timeLeft = {10};

        JOptionPane optionPane = new JOptionPane(
                "<html>You are prohibited to enter this website:<br>closing in <b>10</b></html>",
                JOptionPane.ERROR_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Prohibited");

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft[0]--;
                
                if (timeLeft[0] >= 4 && timeLeft[0] < 11) {
                    optionPane.setMessage(
                        "<html>You are prohibited to enter this website:<br>closing in <b><font size='4'>"
                        + timeLeft[0] + "</font></b></html>"
                    );
                } else {
                    optionPane.setMessage(
                        "<html>You are prohibited to enter this website:<br>closing in <b><font size='4' color='red'>"
                        + timeLeft[0] + "</font></b></html>"
                    );
                }

                if (timeLeft[0] <= 0) {
                    dialog.dispose();
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
        dialog.setVisible(true);

        return 3;
    }

    
    

}

    
