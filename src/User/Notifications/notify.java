/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Notifications;

import Profiles.session;
import User.UserDashboard;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author user
 */
public class notify extends javax.swing.JInternalFrame {

    /**
     * Creates new form notify
     */
    private JDesktopPane MainPane;
    
    public notify(JDesktopPane pane) {
        this.MainPane = pane;
        session see = new session();
        
        initComponents();
        StyleFrame();
        loadNotifications(NotifyScroll, see.GetID());
    }
    
    public void loadNotifications(JScrollPane NotifyScroll, int userId) {
        config conf = new config();
        int unread = 0;

        JPanel notifyPanel = new JPanel();
        notifyPanel.setLayout(new BoxLayout(notifyPanel, BoxLayout.Y_AXIS));

        try {
            String qry = "SELECT * FROM notification WHERE user_id = ? ORDER BY date DESC";
            java.util.List<java.util.Map<String, Object>> notifications = conf.fetchRecords(qry, userId);

            notifyPanel.removeAll();

            for (java.util.Map<String, Object> notif : notifications) {

                String content = notif.get("n_content").toString();
                String date = notif.get("date").toString();
                String read = notif.get("read").toString();
                int notifId = ((Number) notif.get("n_id")).intValue();
                
                if(!read.equals("2")){
                    JLabel label = new JLabel(
                        "<html>"
                        + "<div style='font-size:14px; font-weight:400;'>"
                        + content
                        + "<br><span style='font-size:10px; color:gray;'>"
                        + date
                        + "</span></div></html>"
                    );

                    // Style
                    label.setOpaque(true);
                    label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    label.setAlignmentX(Component.LEFT_ALIGNMENT);

                    if (read.equals("0")) {
                        label.setBackground(new Color(220, 240, 255)); 

                        unread++;
                        notifyIndicator.setText(unread + " Unread Notifications");
                    } else {
                        label.setBackground(Color.WHITE);
                    }

                    // Click event (mark as read)
                    label.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            try {
                                String updateQry = "UPDATE notification SET read = 1 WHERE n_id = ?";
                                conf.updateRecord(updateQry, notifId);
                                
                                loadNotifications(NotifyScroll, userId);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    // Add spacing + label
                    notifyPanel.add(label);
                    notifyPanel.add(Box.createVerticalStrut(5));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set panel to scroll
        NotifyScroll.setViewportView(notifyPanel);

        // Refresh UI
        notifyPanel.revalidate();
        notifyPanel.repaint();
    }
    
    public void markAllAsRead(int userId) {
        config conf = new config();

        try {
            String qry = "UPDATE notification SET read = 1 WHERE user_id = ? AND read = 0";
            conf.updateRecord(qry, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void deleteReadLevel2(int userId) {
        config conf = new config();

        try {
            String qry = "UPDATE notification SET read = 2 WHERE user_id = ?";
            conf.updateRecord(qry, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void refresh(){
        this.dispose();
        UserDashboard board = new UserDashboard();
        notify notif = new notify(MainPane);
        MainPane.add(notif).setVisible(true);
        board.Toggle_Buttons();
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
        jLabel2 = new javax.swing.JLabel();
        clearnotif = new javax.swing.JLabel();
        allread = new javax.swing.JLabel();
        NotifyScroll = new javax.swing.JScrollPane();
        notifyIndicator = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel1.setText("NOTIFICATION");

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel2.setText("X");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 351, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(342, 342, 342))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, -1));

        clearnotif.setBackground(new java.awt.Color(204, 204, 204));
        clearnotif.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        clearnotif.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clearnotif.setText("CLEAR NOTIFICATION");
        clearnotif.setOpaque(true);
        clearnotif.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearnotifMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clearnotifMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clearnotifMouseExited(evt);
            }
        });
        getContentPane().add(clearnotif, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 106, 182, 44));

        allread.setBackground(new java.awt.Color(204, 204, 204));
        allread.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        allread.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        allread.setText("ALL READ");
        allread.setOpaque(true);
        allread.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                allreadMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                allreadMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                allreadMouseExited(evt);
            }
        });
        getContentPane().add(allread, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 106, 103, 44));
        getContentPane().add(NotifyScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 159, 920, 463));

        notifyIndicator.setBackground(new java.awt.Color(204, 204, 204));
        notifyIndicator.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        notifyIndicator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        notifyIndicator.setText("0 Unread Notifcation(s):");
        notifyIndicator.setOpaque(true);
        getContentPane().add(notifyIndicator, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 110, 210, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clearnotifMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearnotifMouseClicked
        session see = new session();
        deleteReadLevel2(see.GetID()); 
        refresh(); // TODO add your handling code here:
    }//GEN-LAST:event_clearnotifMouseClicked

    private void allreadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allreadMouseClicked
        session see = new session();
        markAllAsRead(see.GetID());
        refresh();        // TODO add your handling code here:
    }//GEN-LAST:event_allreadMouseClicked

    private void clearnotifMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearnotifMouseEntered
        animation ani = new animation();
        ani.transitionBackground(
            clearnotif,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200 
        );           // TODO add your handling code here:
    }//GEN-LAST:event_clearnotifMouseEntered

    private void clearnotifMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearnotifMouseExited
        animation ani = new animation();
        ani.transitionBackground(
            clearnotif,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200 
        );          // TODO add your handling code here:
    }//GEN-LAST:event_clearnotifMouseExited

    private void allreadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allreadMouseEntered
        animation ani = new animation();
        ani.transitionBackground(
            allread,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200 
        );          // TODO add your handling code here:
    }//GEN-LAST:event_allreadMouseEntered

    private void allreadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allreadMouseExited
        animation ani = new animation();
        ani.transitionBackground(
            allread,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200 
        );           // TODO add your handling code here:
    }//GEN-LAST:event_allreadMouseExited

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane NotifyScroll;
    private javax.swing.JLabel allread;
    private javax.swing.JLabel clearnotif;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel notifyIndicator;
    // End of variables declaration//GEN-END:variables
}
