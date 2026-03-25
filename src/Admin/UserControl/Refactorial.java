/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.UserControl;

import configuration.LogReg_config;
import configuration.Validations;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author user
 */
public final class Refactorial extends javax.swing.JInternalFrame {

    /**
     * Creates new form Refactorial
     */
    private static int ID;
    
    public Refactorial() {
        initComponents();
        StyleFrame();
        BtnToggles();
        
        int UserID = (int) connector.DataSender();
        
        this.ID = UserID;
        AutoGet();
    }
    
    public void AutoGet(){
        userid.setText(String.valueOf(ID));
        SwingUtilities.invokeLater(() -> {
            GetInfo();
        });
    }
    
    public final void BtnToggles(){
        ButtonGroup menuGroup = new ButtonGroup();
        
        menuGroup.add(users);
        menuGroup.add(admin);
        menuGroup.add(dispatcher);
        
        animation.StyleToggleButtons(users); 
        animation.StyleToggleButtons(admin); 
        animation.StyleToggleButtons(dispatcher); 
        animation.StyleToggleButtons(ussageToggle); 
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    Border grayBorder = BorderFactory.createLineBorder(Color.GRAY, 2);
    Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
    Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
    
    private void ChangeBarLabel(int value){   
        
        passStrength4.setOpaque(true);
        passStrength4.setBorderPainted(false);
        passStrength4.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        passStrength4.setBackground(Color.LIGHT_GRAY);
        
        if(value <= 10 && value >= 1){
            PassLabel4.setText("Poor");
            PassLabel4.setForeground(new Color(204,0,51));
            passStrength4.setForeground(new Color(153,0,0));
        }else if(value <= 30 && value >= 11){
            PassLabel4.setText("Low");
            PassLabel4.setForeground(new Color(255,153,0));
            passStrength4.setForeground(new Color(255,153,0));
        }else if(value <= 50 && value >= 31){
            PassLabel4.setText("Good");
            PassLabel4.setForeground(new Color(255,255,0));
            passStrength4.setForeground(new Color(255,255,51));
        }else if(value <= 75 && value >= 51){
            PassLabel4.setText("Perfect");
            PassLabel4.setForeground(new Color(204,255,204));
            passStrength4.setForeground(new Color(0,255,0));
        }else if(value <= 99 && value >= 76){
            PassLabel4.setText("Excellent");
            PassLabel4.setForeground(new Color(0,204,51));
            passStrength4.setForeground(new Color(153,255,204));
        }else if(value == 100){
            PassLabel4.setText("Unique");
            PassLabel4.setForeground(new Color(102,102,255));
            passStrength4.setForeground(new Color(153,153,255));
        }else{
            PassLabel4.setText("Vulnerable");
            PassLabel4.setForeground(new Color(204,204,204));
            passStrength4.setForeground(new Color(204,204,204));
        }
    }
    
    public void EraseText(){
        users.setSelected(true);
        admin.setSelected(false);
        name4.setText("Username");
        badge4.setText("Badge");
        pass4.setText("Password");
        
        ussageToggle.setSelected(false);
        ussageToggle.setText("Disable");
        
        name4.setBorder(grayBorder);
        badge4.setBorder(grayBorder);
        pass4.setBorder(grayBorder);
    }

    public void UpdateUser() {
        config confer = new config();

        String id = userid.getText().trim();
        String nm = name4.getText().trim();
        String bdg = badge4.getText().trim();
        String ps = pass4.getText().trim();
        String ussage = ussageToggle.getText();
        String acc = "null";
        
        if(sysacc.isSelected()){
            acc = "open";
        } else {
            acc = "close";
        }

        if (id.isEmpty() || id.equals("User ID") ||
            nm.isEmpty() || nm.equals("Username") ||
            bdg.isEmpty() || bdg.equals("Badge") ||
            ps.isEmpty() || ps.equals("Password")) {

            JOptionPane.showMessageDialog(null, "Empty Fields!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int finalID;
        try {
            finalID = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "User ID must be an integer!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String access = users.isSelected() ? "User" :
                        dispatcher.isSelected() ? "Dispatcher":
                        admin.isSelected() ? "Admin" : null;

        String qry = "SELECT * FROM users WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = confer.fetchRecords(qry, finalID);

        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, "User ID does not exist!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.util.Map<String, Object> user = result.get(0);

        String oldName = (String) user.get("user_name");
        int parseBadge = ((Number) user.get("user_badge")).intValue();
        String oldBadge = String.valueOf(parseBadge);
        String oldPass = (String) user.get("user_hashpass");
        String oldAccess = (String) user.get("user_access");
        String oldUssage = (String) user.get("user_ussage");
        String oldAcc = (String) user.get("system_access");

        StringBuilder updateQuery = new StringBuilder("UPDATE users SET ");
        java.util.List<Object> params = new java.util.ArrayList<>();
        boolean hasChanges = false;

        if (!nm.equals(oldName)) {
            updateQuery.append("user_name = ?, ");
            params.add(nm);
            hasChanges = true;
        }

        if (!bdg.equals(oldBadge)) {
            updateQuery.append("user_badge = ?, ");
            params.add(bdg);
            hasChanges = true;
        }

        if (!ps.equals("HASHED")) {
            String hashpass = confer.hashPassword(ps);
            if (!hashpass.equals(oldPass)) {
                updateQuery.append("user_hashpass = ?, ");
                params.add(hashpass);
                hasChanges = true;
            }
        }

        if (access != null && !access.equals(oldAccess)) {
            updateQuery.append("user_access = ?, ");
            params.add(access);
            hasChanges = true;
        }

        if (!ussage.equals(oldUssage)) {
            updateQuery.append("user_ussage = ?, ");
            params.add(ussage);
            hasChanges = true;
        }
        
        if (!acc.equals(oldAcc)) {
            updateQuery.append("system_access = ?, ");
            params.add(acc);
            hasChanges = true;
        }

        if (!hasChanges) {
            JOptionPane.showMessageDialog(null, "No changes detected.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        updateQuery.setLength(updateQuery.length() - 2);

        updateQuery.append(" WHERE user_id = ?");
        params.add(finalID);

        confer.updateRecord(updateQuery.toString(), params.toArray());
        
        LocalDateTime now = LocalDateTime.now();
        Timestamp date = Timestamp.valueOf(now);
        String queryNow = "INSERT INTO logs(user_id, dateTime, log_action) VALUES(?,?,?)";
        confer.addRecordAndReturnId(queryNow, id, date, "Update");

        JOptionPane.showMessageDialog(null, "User successfully updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void GetInfo(){
        StringBuilder error = new StringBuilder();
        config conf = new config();
        int finalID = 0;
        
        String id = userid.getText();
        
        if(id.isEmpty() && id.equals("User ID")){
            JOptionPane.showMessageDialog(
                null,
                "Empty Fields!",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
        
        if(!id.isEmpty() && !id.equals("User ID")){
        
        try{
            finalID = Integer.parseInt((String) id);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(
                null,
                "Input Should be Integer!",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
        
        String qry = "SELECT * FROM users WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, finalID);
        
        System.out.println("Searching for user_id: '" + finalID + "'");
                    
        if(!result.isEmpty()){
            java.util.Map<String, Object> user = result.get(0);
            String name = user.get("user_name").toString();
            String bdg = user.get("user_badge").toString();
            String ps = user.get("user_hashpass").toString();
            String access = user.get("user_access").toString();
            String ussage = user.get("user_ussage").toString();
            String acc = user.get("system_access").toString();
            
            String pass = conf.hashPassword(ps);
            
            name4.setText(name);
            name4.setBackground(new Color(204,204,204));
            badge4.setText(bdg);
            badge4.setBackground(new Color(204,204,204));
            pass4.setText("HASHED");
            name4.setForeground(new Color(0,0,0));
            badge4.setForeground(new Color(0,0,0));
            pass4.setForeground(new Color(0,0,0));
            
            if(access.equals("User")){
                users.setSelected(true);
                admin.setSelected(false);
                dispatcher.setSelected(false);
            }else if(access.equals("Admin")){
                admin.setSelected(true);
                users.setSelected(false);
                dispatcher.setSelected(false);
            }else if(access.equals("Dispatcher")){
                admin.setSelected(false);
                users.setSelected(false);
                dispatcher.setSelected(true);
            }
            
            if(ussage.equals("Enable")){
                ussageToggle.setSelected(true);
                ussageToggle.setText("Enable");
            }else{
                ussageToggle.setSelected(false);
                ussageToggle.setText("Disable");
            }
            
            if(acc.equals("open")){
                sysacc.setSelected(true);
                sysacc.setText("Open");
            } else {
                sysacc.setSelected(false);
                sysacc.setText("Close");
            }
            
        }else{
                JOptionPane.showMessageDialog(
                    null,
                    "User ID does not exist!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
                );
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

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        userid = new javax.swing.JTextField();
        name4 = new javax.swing.JTextField();
        badge4 = new javax.swing.JTextField();
        pass4 = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        users = new javax.swing.JToggleButton();
        admin = new javax.swing.JToggleButton();
        jLabel31 = new javax.swing.JLabel();
        dispatcher = new javax.swing.JToggleButton();
        jPanel17 = new javax.swing.JPanel();
        ussageToggle = new javax.swing.JToggleButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        passStrength4 = new javax.swing.JProgressBar();
        jLabel29 = new javax.swing.JLabel();
        PassLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        sysacc = new javax.swing.JToggleButton();
        jLabel6 = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 421, 15));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel1.setText("UPDATE USER");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(167, 16, -1, -1));

        userid.setEditable(false);
        userid.setForeground(new java.awt.Color(51, 51, 51));
        userid.setText("User ID");
        userid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                useridMouseClicked(evt);
            }
        });
        userid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useridActionPerformed(evt);
            }
        });
        jPanel1.add(userid, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 44, 205, 48));

        name4.setEditable(false);
        name4.setBackground(new java.awt.Color(255, 255, 255));
        name4.setForeground(new java.awt.Color(153, 153, 153));
        name4.setText("Username");
        name4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                name4MouseClicked(evt);
            }
        });
        name4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                name4ActionPerformed(evt);
            }
        });
        name4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                name4KeyReleased(evt);
            }
        });
        jPanel1.add(name4, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 134, 197, 44));

        badge4.setEditable(false);
        badge4.setBackground(new java.awt.Color(255, 255, 255));
        badge4.setForeground(new java.awt.Color(153, 153, 153));
        badge4.setText("Badge");
        badge4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                badge4MouseClicked(evt);
            }
        });
        badge4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                badge4ActionPerformed(evt);
            }
        });
        badge4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                badge4KeyReleased(evt);
            }
        });
        jPanel1.add(badge4, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 134, 183, 44));

        pass4.setFont(new java.awt.Font("Tahoma", 2, 16)); // NOI18N
        pass4.setForeground(new java.awt.Color(153, 153, 153));
        pass4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pass4.setText("Password");
        pass4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pass4MouseClicked(evt);
            }
        });
        pass4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pass4ActionPerformed(evt);
            }
        });
        pass4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pass4KeyReleased(evt);
            }
        });
        jPanel1.add(pass4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 187, 391, 44));

        jPanel16.setBackground(new java.awt.Color(153, 153, 153));

        users.setSelected(true);
        users.setText("USER");
        users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersActionPerformed(evt);
            }
        });

        admin.setText("ADMIN");

        jLabel31.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel31.setText("ACCESS");

        dispatcher.setText("DISPATCHER");
        dispatcher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispatcherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel31))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(admin, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(users, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dispatcher, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(admin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(users)
                .addGap(2, 2, 2)
                .addComponent(dispatcher)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 238, 190, 140));

        jPanel17.setBackground(new java.awt.Color(153, 153, 153));

        ussageToggle.setText("Disable");
        ussageToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ussageToggleActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel4.setText("USSAGE");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ussageToggle, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(1, 1, 1)
                .addComponent(ussageToggle)
                .addGap(40, 40, 40))
        );

        jPanel1.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 308, -1, 70));

        jLabel5.setBackground(new java.awt.Color(153, 153, 153));
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("RESET");
        jLabel5.setOpaque(true);
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited(evt);
            }
        });
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 180, 40));

        jLabel2.setBackground(new java.awt.Color(153, 153, 153));
        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Get Info");
        jLabel2.setOpaque(true);
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 48, 168, 40));

        passStrength4.setForeground(new java.awt.Color(204, 204, 204));
        passStrength4.setOpaque(true);
        jPanel1.add(passStrength4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 443, 391, -1));

        jLabel29.setText("Account password strength: ");
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, -1, -1));

        PassLabel4.setBackground(new java.awt.Color(153, 153, 153));
        PassLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        PassLabel4.setForeground(new java.awt.Color(204, 204, 204));
        PassLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PassLabel4.setText("Null");
        PassLabel4.setOpaque(true);
        jPanel1.add(PassLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 460, 170, -1));

        jLabel3.setBackground(new java.awt.Color(153, 153, 153));
        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 204, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("CONFIRM");
        jLabel3.setOpaque(true);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, 193, 40));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        sysacc.setText("Open");
        sysacc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sysaccMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel6.setText("SYSTEM ACCESS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sysacc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel6)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(sysacc))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 190, 60));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 512));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setBackground(new Color(153,153,153));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        jLabel3.setBackground(new Color(102,102,102));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        UpdateUser();// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        GetInfo();// TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        jLabel5.setBackground(new Color(153,153,153));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        jLabel5.setBackground(new Color(102,102,102)); // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        EraseText();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseClicked

    private void ussageToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ussageToggleActionPerformed
        if(ussageToggle.isSelected()){
            String ussage = "Enable";
            ussageToggle.setText("Enable");
        }else{
            String ussage = "Disable";
            ussageToggle.setText("Disable");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_ussageToggleActionPerformed

    private void usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usersActionPerformed

    private void pass4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pass4KeyReleased
        animation ani = new animation();
        ani.validateRequired(pass4);

        pass4.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }

            private void updateStrength() {
                String password = pass4.getText();

                if(!password.equals("Password") || !password.equals("HASHED")){
                    int strength = ani.calculatePasswordStrength(password, passStrength4);
                    ChangeBarLabel(strength);
                }
            }
        }); // TODO add your handling code here:
    }//GEN-LAST:event_pass4KeyReleased

    private void pass4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pass4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pass4ActionPerformed

    private void pass4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pass4MouseClicked
        animation ani = new animation();
        
        String pass = pass4.getText();
        
        if(pass.equals("Password")){
            ani.addPlaceholder(pass4, pass);// TODO add your handling code here:
        }else if (pass.equals("HASHED")){
            ani.addPlaceholder(pass4, pass);   
        }
    }//GEN-LAST:event_pass4MouseClicked

    private void badge4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_badge4KeyReleased
        animation ani = new animation();
        Validations validate = new Validations();

        String bdg = badge4.getText();

        int valid = validate.BadgeValidate(bdg);

        switch (valid) {
            case 1:
            ani.validateRequired(badge4);
            break;
            case 0:
            badge4.setBorder(redBorder);
            break;
            case 3:
            badge4.setBorder(orangeBorder);
            break;
            default:
            badge4.setBorder(grayBorder);
            break;
        }        // TODO add your handling code here:
    }//GEN-LAST:event_badge4KeyReleased

    private void badge4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_badge4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_badge4ActionPerformed

    private void badge4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_badge4MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(badge4, "Badge");         // TODO add your handling code here:
    }//GEN-LAST:event_badge4MouseClicked

    private void name4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_name4KeyReleased
        animation ani = new animation();

        String nm = name4.getText();

        if(!nm.equals("Username")){
            ani.validateRequired(name4);
        }else{
            name4.setBorder(redBorder);
        }// TODO add your handling code here:
    }//GEN-LAST:event_name4KeyReleased

    private void name4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_name4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_name4ActionPerformed

    private void name4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_name4MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(name4, "Username");         // TODO add your handling code here:
    }//GEN-LAST:event_name4MouseClicked

    private void useridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useridActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_useridActionPerformed

    private void useridMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_useridMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(userid, "User ID"); // TODO add your handling code here:
    }//GEN-LAST:event_useridMouseClicked

    private void dispatcherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dispatcherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dispatcherActionPerformed

    private void sysaccMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sysaccMouseClicked
        if(sysacc.isSelected()){
            sysacc.setText("Open");
        }else{
            sysacc.setText("Close");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_sysaccMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel PassLabel4;
    private javax.swing.JToggleButton admin;
    private javax.swing.JTextField badge4;
    private javax.swing.JToggleButton dispatcher;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField name4;
    private javax.swing.JTextField pass4;
    private javax.swing.JProgressBar passStrength4;
    private javax.swing.JToggleButton sysacc;
    private javax.swing.JTextField userid;
    private javax.swing.JToggleButton users;
    private javax.swing.JToggleButton ussageToggle;
    // End of variables declaration//GEN-END:variables
}
