/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.UserControl;

import configuration.LogReg_config;
import configuration.Validations;
import static configuration.Validations.ValidateInteger;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

/**
 *
 * @author user
 */
public class Refactorial extends javax.swing.JInternalFrame {

    /**
     * Creates new form Refactorial
     */
    public Refactorial() {
        initComponents();
        StyleFrame();
        BtnToggles();
    }
    
    public void BtnToggles(){
        ButtonGroup menuGroup = new ButtonGroup();
        
        menuGroup.add(userbtn);
        menuGroup.add(admin);
        
        animation.StyleToggleButtons(userbtn); 
        animation.StyleToggleButtons(admin); 
        animation.StyleToggleButtons(ussageToggle); 
    }
    
    public void StyleFrame(){
        this.setBorder(null);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
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
        userbtn.setSelected(true);
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

    public void UpdateUser(){
        LogReg_config conf = new LogReg_config();
        config confer = new config();
        Validations valid = new Validations();
        String access = "null";
        int finalID = 0;
        
        String id = userid.getText();
        
        if(id.isEmpty() || id.equals("User ID")){
            JOptionPane.showMessageDialog(
                null,
                "Empty Fields!",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }else{
        
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

            String nm = name4.getText();
            String bdg = badge4.getText();
            String ps = pass4.getText();
            String ussage = ussageToggle.getText();

            String hashpass = confer.hashPassword(ps);

            if(userbtn.isSelected()){
                access = "User";
            }else if(admin.isSelected()){
                access = "Admin";
            }

                String qry = "SELECT * FROM users WHERE user_id = ?";
                java.util.List<java.util.Map<String, Object>> result = confer.fetchRecords(qry, finalID);

            if(!result.isEmpty()){

                qry = "UPDATE users SET user_name = ?, user_badge = ?, user_hashpass = ?, user_access = ?, user_ussage = ? WHERE user_id = ?";
                confer.updateRecord(qry, nm, bdg, hashpass, access, ussage, finalID);
                
                JOptionPane.showMessageDialog(
                    null,
                    "User Succesfully Refactored!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                    );
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
    
    public void GetInfo(){
        StringBuilder error = new StringBuilder();
        config conf = new config();
        int finalID = 0;
        
        String id = userid.getText();
        
        if(id.isEmpty() || id.equals("User ID")){
            JOptionPane.showMessageDialog(
                null,
                "Empty Fields!",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }else{
        
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
            
            String pass = conf.hashPassword(ps);
            
            name4.setText(name);
            badge4.setText(bdg);
            pass4.setText(pass);
            
            if(access.equals("User")){
                userbtn.setSelected(true);
                admin.setSelected(false);
            }else if(access.equals("Admin")){
                admin.setSelected(true);
                userbtn.setSelected(false);
            }
            
            if(ussage.equals("Enable")){
                ussageToggle.setSelected(true);
                ussageToggle.setText("Enable");
            }else{
                ussageToggle.setSelected(false);
                ussageToggle.setText("Disable");
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
        userbtn = new javax.swing.JToggleButton();
        admin = new javax.swing.JToggleButton();
        jLabel31 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        ussageToggle = new javax.swing.JToggleButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        passStrength4 = new javax.swing.JProgressBar();
        jLabel29 = new javax.swing.JLabel();
        PassLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel1.setText("UPDATE USER");

        userid.setEditable(false);
        userid.setForeground(new java.awt.Color(153, 153, 153));
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

        name4.setEditable(false);
        name4.setBackground(new java.awt.Color(204, 204, 204));
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

        badge4.setEditable(false);
        badge4.setBackground(new java.awt.Color(204, 204, 204));
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

        pass4.setEditable(false);
        pass4.setBackground(new java.awt.Color(204, 204, 204));
        pass4.setForeground(new java.awt.Color(153, 153, 153));
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

        jPanel16.setBackground(new java.awt.Color(153, 153, 153));

        userbtn.setSelected(true);
        userbtn.setText("USER");
        userbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userbtnActionPerformed(evt);
            }
        });

        admin.setText("ADMIN");

        jLabel31.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel31.setText("ACCESS");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(admin, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(jLabel31)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(jLabel31)
                .addGap(1, 1, 1)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userbtn)
                    .addComponent(admin))
                .addGap(1, 1, 1))
        );

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
                .addComponent(ussageToggle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(59, 59, 59))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ussageToggle)
                .addContainerGap())
        );

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

        passStrength4.setForeground(new java.awt.Color(204, 204, 204));
        passStrength4.setOpaque(true);

        jLabel29.setText("Account password strength: ");

        PassLabel4.setBackground(new java.awt.Color(153, 153, 153));
        PassLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        PassLabel4.setForeground(new java.awt.Color(204, 204, 204));
        PassLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PassLabel4.setText("Null");
        PassLabel4.setOpaque(true);

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(userid, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(pass4))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(name4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(badge4, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passStrength4, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel29)
                                        .addGap(7, 7, 7)
                                        .addComponent(PassLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userid, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(badge4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(pass4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(passStrength4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(PassLabel4))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void useridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useridActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_useridActionPerformed

    private void name4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_name4MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(name4, "Username");         // TODO add your handling code here:
    }//GEN-LAST:event_name4MouseClicked

    private void name4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_name4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_name4ActionPerformed

    private void name4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_name4KeyReleased
        animation ani = new animation();

        String nm = name4.getText();

        if(!nm.equals("Username")){
            ani.validateRequired(name4);
        }else{
            name4.setBorder(redBorder);
        }// TODO add your handling code here:
    }//GEN-LAST:event_name4KeyReleased

    private void badge4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_badge4MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(badge4, "Badge");         // TODO add your handling code here:
    }//GEN-LAST:event_badge4MouseClicked

    private void badge4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_badge4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_badge4ActionPerformed

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

    private void pass4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pass4MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(pass4, "Password");         // TODO add your handling code here:
    }//GEN-LAST:event_pass4MouseClicked

    private void pass4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pass4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pass4ActionPerformed

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

                if(!password.equals("Password")){
                    int strength = ani.calculatePasswordStrength(password, passStrength4);
                    ChangeBarLabel(strength);
                }
            }
        }); // TODO add your handling code here:
    }//GEN-LAST:event_pass4KeyReleased

    private void userbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userbtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userbtnActionPerformed

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

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        EraseText();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        jLabel5.setBackground(new Color(102,102,102)); // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        jLabel5.setBackground(new Color(153,153,153));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        GetInfo();// TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        UpdateUser();// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        jLabel3.setBackground(new Color(102,102,102));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setBackground(new Color(153,153,153));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseExited

    private void useridMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_useridMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(userid, "User ID"); // TODO add your handling code here:
    }//GEN-LAST:event_useridMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel PassLabel4;
    private javax.swing.JToggleButton admin;
    private javax.swing.JTextField badge4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField name4;
    private javax.swing.JTextField pass4;
    private javax.swing.JProgressBar passStrength4;
    private javax.swing.JToggleButton userbtn;
    private javax.swing.JTextField userid;
    private javax.swing.JToggleButton ussageToggle;
    // End of variables declaration//GEN-END:variables
}
