/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import configuration.animation;
import configuration.config;
import static configuration.config.connectDB;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public final class Overview extends javax.swing.JInternalFrame {

    /**
     * Creates new form Overview
     */
    private JDesktopPane MainPane;
    
    public Overview(JDesktopPane pane) {
        this.MainPane = pane;
        
        initComponents();
        StyleFrame();
        SwingUtilities.invokeLater(() ->{
            try {
                Count();
            } catch (SQLException ex) {
                Logger.getLogger(Overview.class.getName()).log(Level.SEVERE, null, ex);
            }
            String q = "SELECT user_id, user_name, user_badge, user_access FROM users";
            ShowTable(q);
        });
        
        StyleTable();
        displayTopBuyer();
    }
    
    public void displayTopBuyer(){
        String query = 
            "SELECT user_id, order_totalPrice " +
            "FROM userOrders " +
            "ORDER BY order_totalPrice DESC";

            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append("<h2 style='margin-bottom:10px;'>Top Payments</h2>");

            try (Connection conn = connectDB();
                 PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                int rank = 1;

                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    double payment = rs.getDouble("order_totalPrice");

                    sb.append("<div style='margin-bottom:5px;'>")
                      .append("<b>#").append(rank++).append("</b> ")
                      .append("User ID: <b>").append(userId).append("</b>")
                      .append(" — <span style='color:green; font-weight:bold;'>₱")
                      .append(String.format("%.2f", payment))
                      .append("</span>")
                      .append("</div>");
                }

                sb.append("</html>");

                area.setContentType("text/html");
                area.setText(sb.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public final void StyleTable(){
        table.setRowHeight(50);
        
    }
    
    public void Count() throws SQLException{
        config conf = new config();
        String totalusers, totalproducts, totalcancel, totalorders, totalexports, totaldelivery, totalban, highpay;
        
        totalusers = "SELECT COUNT(*) FROM users";   
        totalproducts = "SELECT COUNT(*) FROM products";   
        totalcancel = "SELECT COUNT(*) FROM cancelledOrders";           
        totalorders = "SELECT COUNT(*) FROM userOrders";       
        totalexports = "SELECT COUNT(*) FROM exports";        
        totaldelivery = "SELECT COUNT(*) FROM exports WHERE export_status = 'Delivered'";      
        totalban = "SELECT COUNT(*) FROM users WHERE system_access = 'close'";      
        highpay = "SELECT user_id, order_totalPrice FROM userOrders ORDER BY order_totalPrice DESC LIMIT 1";
        conf.GetHighPay(highpay);
        
        String pay = String.valueOf(conf.GetPay());
        
        int users = conf.getCount(totalusers);
        int products = conf.getCount(totalproducts);
        int cancel = conf.getCount(totalcancel);
        int orders = conf.getCount(totalorders);
        int export = conf.getCount(totalexports);
        int delivery = conf.getCount(totaldelivery);
        int ban = conf.getCount(totalban);
        
        regis.setText(String.valueOf(users));
        prod.setText(String.valueOf(products));
        ord.setText(String.valueOf(orders));
        exports.setText(String.valueOf(export));
        cancels.setText(String.valueOf(cancel));
        deli.setText(String.valueOf(delivery));
        banned.setText(String.valueOf(ban));
        payment.setText(pay);  
    }
    
    public void ShowTable(String query){
        config conf = new config();
        conf.displayData(query, table);
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        prod = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        regis = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        deli = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cancels = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        exports = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        ord = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        banned = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        payment = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        area = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(235, 235, 235));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel2MouseExited(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("TOTAL PRODUCTS");

        prod.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        prod.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        prod.setText("0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(prod, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prod)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 250, 90));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel3MouseExited(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TOTAL USERS");

        regis.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        regis.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        regis.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(regis, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regis)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, 90));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel4MouseExited(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("DELIVERED ORDERS");

        deli.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        deli.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        deli.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(deli, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deli)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, 200, 90));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel5MouseExited(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("CANCELLED ORDERS");

        cancels.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        cancels.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancels.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(cancels, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancels)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 90));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel6MouseExited(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("TOTAL EXPORTS");

        exports.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        exports.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exports.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exports, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exports)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, 190, 90));

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel7MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel7MousePressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("TOTAL ORDERS");

        ord.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        ord.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ord.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(ord, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ord)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 180, 90));

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel8MouseExited(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel13.setText("USERS BANNED");

        banned.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        banned.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        banned.setText("0");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(banned, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(banned)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, 160, 90));

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));
        jPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel9MouseExited(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("HIGHEST PAY");

        payment.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        payment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        payment.setText("0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(payment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(payment)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, 270, 90));

        area.setEditable(false);
        jScrollPane1.setViewportView(area);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 370, 400));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 220, 510, 400));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        String q = "SELECT user_id, user_name, user_badge, user_access FROM users";
        ShowTable(q);       // TODO add your handling code here:
    }//GEN-LAST:event_jPanel3MouseClicked

    private void jPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel3,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );          // TODO add your handling code here:
    }//GEN-LAST:event_jPanel3MouseEntered

    private void jPanel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel3,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );          // TODO add your handling code here:
    }//GEN-LAST:event_jPanel3MouseExited

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel2,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseEntered

    private void jPanel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel2,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );         // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseExited

    private void jPanel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel7,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MouseEntered

    private void jPanel7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MousePressed

    private void jPanel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel7,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );         // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MouseExited

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel6,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel6MouseEntered

    private void jPanel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel6,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel6MouseExited

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        String q = "SELECT cancel_id, order_id, priceLost, prod_category, cancelledBy FROM cancelledOrders";
        ShowTable(q);              // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel5,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );         // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseEntered

    private void jPanel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel5,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );         // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseExited

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel4,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel4,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );         // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseExited

    private void jPanel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel8,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel8MouseEntered

    private void jPanel8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel8,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel8MouseExited

    private void jPanel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel9,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel9MouseEntered

    private void jPanel9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jPanel9,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );         // TODO add your handling code here:
    }//GEN-LAST:event_jPanel9MouseExited

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        String q = "SELECT prod_id, prod_name, prod_status, prod_category FROM products";
        ShowTable(q);     // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
        String q = "SELECT order_id, user_id, order_status, order_paymentMethod FROM userOrders";
        ShowTable(q);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MouseClicked

    private void jPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseClicked
        String q = "SELECT export_id, order_id, export_status, export_reciever, reference_number FROM exports";
        ShowTable(q);           // TODO add your handling code here:
    }//GEN-LAST:event_jPanel6MouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        String q = "SELECT export_id, order_id, export_status, export_dispatcherName, reference_number FROM exports WHERE export_status = 'Delivered'";
        ShowTable(q);         // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
        String q = "SELECT user_id, user_name, user_badge, user_access, system_access FROM users WHERE system_access = 'close'";
        ShowTable(q);          // TODO add your handling code here:
    }//GEN-LAST:event_jPanel8MouseClicked

    private void jPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseClicked
        String q = "SELECT order_id, user_id, order_totalPrice, order_paymentMethod FROM userOrders ORDER BY order_totalPrice DESC";
        ShowTable(q);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel9MouseClicked

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
                // TODO add your handling code here:
    }//GEN-LAST:event_tableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane area;
    private javax.swing.JLabel banned;
    private javax.swing.JLabel cancels;
    private javax.swing.JLabel deli;
    private javax.swing.JLabel exports;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel ord;
    private javax.swing.JLabel payment;
    private javax.swing.JLabel prod;
    private javax.swing.JLabel regis;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
