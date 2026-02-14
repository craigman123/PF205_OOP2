/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.UserControl;

import configuration.animation;
import configuration.config;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author user
 */
public class Remove_User extends javax.swing.JInternalFrame {

    /**
     * Creates new form Remove_User
     */
    public Remove_User() {
        initComponents();
        StyleFrame();
    }
    
    public void StyleFrame(){
        this.setBorder(null);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
    }
    
    public void EraseUser(){
        config conf = new config();
        int finalID = 0;
        
        String mainid = mainID.getText();
        
        if(mainid.isEmpty() || mainid.equals("Enter ID")){
            JOptionPane.showMessageDialog(
                null,
                "Empty Fields!",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
            return; 
        }

        try {
            finalID = Integer.parseInt(mainid);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                null,
                "Input should be an integer!",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
            return; 
        }

        String qry = "SELECT * FROM users WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, finalID);

        System.out.println("Searching for user_id: '" + finalID + "'");

        if(!result.isEmpty()){
            qry = "DELETE FROM users WHERE user_id = ?";
            conf.deleteRecord(qry, finalID);
            JOptionPane.showMessageDialog(
                null,
                "User ID " + finalID + " deleted successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                null,
                "User ID does not exist!",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
    
    public void EraseMultipleUser(){
        config conf = new config();
        int finalID = 0;
        StringBuilder error = new StringBuilder();
        int successCount = 0;
        
        JTextField[] fields = { id1, id2, id3, id4, id5, id6, id7, id8 };
        
        boolean allEmpty = true;

        for (int i = 0; i < fields.length; i++) {

            String text = fields[i].getText();

            if (text != null) {
                text = text.trim();

                if (!text.isEmpty() && !text.equals("ID " + (i + 1))) {
                    allEmpty = false;
                    break;
                }
            }
        }

        if (allEmpty) {
            JOptionPane.showMessageDialog(
                null,
                "Enter at least one ID",
                "Empty Fields",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        for (int i = 0; i < fields.length; i++) {

            String text = fields[i].getText().trim();

            if (text != null && !text.trim().isEmpty() && !text.trim().equals("ID " + (i + 1))) {

                try {
                    
                    System.out.println(text);
                    finalID = Integer.parseInt(text);

                    String qry = "SELECT * FROM users WHERE user_id = ?";
                    java.util.List<java.util.Map<String, Object>> result =
                            conf.fetchRecords(qry, finalID);

                    if (!result.isEmpty()) {

                        qry = "DELETE FROM users WHERE user_id = ?";
                        conf.deleteRecord(qry, finalID);

                        fields[i].setText("ID " + (1 + i));
                        successCount++;

                    } else {
                        error.append("TextField ")
                             .append(i + 1)
                             .append(": User ID ")
                             .append(finalID)
                             .append(" does not exist.\n");
                    }
                    

                } catch (NumberFormatException e) {
                    error.append("TextField ")
                         .append(i + 1)
                         .append(": Input must be an all integer.\n");
                }
            }
        }

        if (error.length() > 0) {
            JOptionPane.showMessageDialog(
                    null,
                    error.toString(),
                    "Errors Found",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        if (successCount > 0) {
            JOptionPane.showMessageDialog(
                    null,
                    successCount + " user(s) successfully deleted.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
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
        jLabel1 = new javax.swing.JLabel();
        mainID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        id1 = new javax.swing.JTextField();
        id2 = new javax.swing.JTextField();
        id3 = new javax.swing.JTextField();
        id4 = new javax.swing.JTextField();
        id5 = new javax.swing.JTextField();
        id6 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        id7 = new javax.swing.JTextField();
        id8 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setText("REMOVE USER");

        mainID.setEditable(false);
        mainID.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        mainID.setForeground(new java.awt.Color(102, 102, 102));
        mainID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mainID.setText("Enter ID");
        mainID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainIDMouseClicked(evt);
            }
        });
        mainID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainIDActionPerformed(evt);
            }
        });
        mainID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mainIDKeyPressed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("REMOVE");
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

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("MULTI REMOVE SELECTION");

        id1.setEditable(false);
        id1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        id1.setForeground(new java.awt.Color(102, 102, 102));
        id1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id1.setText("ID 1");
        id1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id1MouseClicked(evt);
            }
        });
        id1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id1ActionPerformed(evt);
            }
        });

        id2.setEditable(false);
        id2.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        id2.setForeground(new java.awt.Color(102, 102, 102));
        id2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id2.setText("ID 2");
        id2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id2MouseClicked(evt);
            }
        });
        id2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id2ActionPerformed(evt);
            }
        });
        id2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                id2KeyPressed(evt);
            }
        });

        id3.setEditable(false);
        id3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        id3.setForeground(new java.awt.Color(102, 102, 102));
        id3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id3.setText("ID 3");
        id3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id3MouseClicked(evt);
            }
        });
        id3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id3ActionPerformed(evt);
            }
        });
        id3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                id3KeyPressed(evt);
            }
        });

        id4.setEditable(false);
        id4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        id4.setForeground(new java.awt.Color(102, 102, 102));
        id4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id4.setText("ID 4");
        id4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id4MouseClicked(evt);
            }
        });
        id4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id4ActionPerformed(evt);
            }
        });

        id5.setEditable(false);
        id5.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        id5.setForeground(new java.awt.Color(102, 102, 102));
        id5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id5.setText("ID 5");
        id5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id5MouseClicked(evt);
            }
        });
        id5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id5ActionPerformed(evt);
            }
        });

        id6.setEditable(false);
        id6.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        id6.setForeground(new java.awt.Color(102, 102, 102));
        id6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id6.setText("ID 6");
        id6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id6MouseClicked(evt);
            }
        });
        id6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id6ActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("REMOVE");
        jLabel4.setOpaque(true);
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited(evt);
            }
        });

        id7.setEditable(false);
        id7.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        id7.setForeground(new java.awt.Color(102, 102, 102));
        id7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id7.setText("ID 7");
        id7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id7MouseClicked(evt);
            }
        });
        id7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id7ActionPerformed(evt);
            }
        });

        id8.setEditable(false);
        id8.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        id8.setForeground(new java.awt.Color(102, 102, 102));
        id8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id8.setText("ID 8");
        id8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id8MouseClicked(evt);
            }
        });
        id8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id8ActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setForeground(new java.awt.Color(153, 0, 0));
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(156, 156, 156)
                            .addComponent(jLabel1))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addComponent(mainID, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(15, 15, 15)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addComponent(id1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(id2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addComponent(id3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(id4, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(id7, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(id8))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(id5, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(id6, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(1, 1, 1))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(84, 84, 84))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainID, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(id5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(id6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mainIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mainIDActionPerformed

    private void id1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id1ActionPerformed

    private void id2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id2ActionPerformed

    private void id3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id3ActionPerformed

    private void id4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id4ActionPerformed

    private void id6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id6ActionPerformed

    private void id5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id5ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        EraseUser();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        EraseMultipleUser();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseClicked

    private void id7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id7ActionPerformed

    private void id8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id8ActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        id1.setText("ID 1"); 
        id2.setText("ID 2");  
        id3.setText("ID 3");  
        id4.setText("ID 4");  
        id5.setText("ID 5");  
        id6.setText("ID 6");  
        id7.setText("ID 7");  
        id8.setText("ID 8");  // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        jLabel5.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        jLabel5.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        jLabel4.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        jLabel4.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseExited

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseExited

    private void mainIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mainIDKeyPressed
    // TODO add your handling code here:
    }//GEN-LAST:event_mainIDKeyPressed

    private void id1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id1MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id1, "ID 1");        // TODO add your handling code here:
    }//GEN-LAST:event_id1MouseClicked

    private void id2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id2KeyPressed
        animation ani = new animation();

        ani.addPlaceholder(id2, "ID 2");         // TODO add your handling code here:
    }//GEN-LAST:event_id2KeyPressed

    private void id3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id3KeyPressed
        animation ani = new animation();

        ani.addPlaceholder(id3, "ID 3");        // TODO add your handling code here:
    }//GEN-LAST:event_id3KeyPressed

    private void id4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id4MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id4, "ID 4");        // TODO add your handling code here:
    }//GEN-LAST:event_id4MouseClicked

    private void id5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id5MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id5, "ID 5");        // TODO add your handling code here:
    }//GEN-LAST:event_id5MouseClicked

    private void id6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id6MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id6, "ID 6");        // TODO add your handling code here:
    }//GEN-LAST:event_id6MouseClicked

    private void id7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id7MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id7, "ID 7");        // TODO add your handling code here:
    }//GEN-LAST:event_id7MouseClicked

    private void id8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id8MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id8, "ID 8");        // TODO add your handling code here:
    }//GEN-LAST:event_id8MouseClicked

    private void mainIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainIDMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(mainID, "Enter ID");        // TODO add your handling code here:
    }//GEN-LAST:event_mainIDMouseClicked

    private void id2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id2MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id2, "ID 2");// TODO add your handling code here:
    }//GEN-LAST:event_id2MouseClicked

    private void id3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id3MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id3, "ID 3");        // TODO add your handling code here:
    }//GEN-LAST:event_id3MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField id1;
    private javax.swing.JTextField id2;
    private javax.swing.JTextField id3;
    private javax.swing.JTextField id4;
    private javax.swing.JTextField id5;
    private javax.swing.JTextField id6;
    private javax.swing.JTextField id7;
    private javax.swing.JTextField id8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField mainID;
    // End of variables declaration//GEN-END:variables
}
