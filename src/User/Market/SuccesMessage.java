/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Market;

import User.User_config;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public final class SuccesMessage extends javax.swing.JInternalFrame {

    /**
     * Creates new form SuccesMessage
     */
    private int orderID;
    private JDesktopPane MainPane;
    
    public SuccesMessage(int id, JDesktopPane pane) {
        this.orderID = id;
        this.MainPane = pane;
        
        initComponents();
        StyleFrame();
        SwingUtilities.invokeLater(() -> { Image(); });
        ShowOrderInfo();
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void Image() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/balcj.jpg"));
        System.out.println(getClass().getResource("/images/balcj.jpg"));

        Image img = icon.getImage();

        int labelWidth = image.getWidth();
        int labelHeight = image.getHeight();

        if (labelWidth <= 0 || labelHeight <= 0) return;

        Image scaledImg = img.getScaledInstance(
                labelWidth,
                labelHeight,
                Image.SCALE_SMOOTH
        );

        image.setIcon(new ImageIcon(scaledImg));
        image.setHorizontalAlignment(JLabel.CENTER);
        image.setVerticalAlignment(JLabel.CENTER);
    }
    
    public void ShowOrderInfo(){
    config conf = new config();
    StringBuilder build = new StringBuilder();

    String qry = "SELECT * FROM userOrders WHERE order_id = ?";
    java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, orderID); 

    if(!result.isEmpty()){
        java.util.Map<String, Object> user = result.get(0);

        String status = user.get("order_status").toString();
        String prodName = user.get("order_prod").toString();
        String quant = user.get("order_quantity").toString();
        String total = user.get("order_totalPrice").toString();
        String fullAddress = user.get("order_shippingAddress").toString();
        String paymethod = user.get("order_paymentMethod").toString();
        String date = user.get("order_date").toString();
        String additionals = user.get("order_additionals").toString();

        String name, description, address;

        String[] parts = fullAddress.split(",");

        if (parts.length < 2) {
            area.setText("Invalid address format!");
            return;
        }

        name = parts[0].trim();
        description = parts[parts.length - 1].trim();

        StringBuilder addressBuilder = new StringBuilder();

        for (int i = 1; i < parts.length - 1; i++) {
            addressBuilder.append(parts[i].trim());
            if (i < parts.length - 2) {
                addressBuilder.append(", ");
            }
        }
        
        address = addressBuilder.toString();

        // ================= HTML UI =================
        build.append("<html>");
        build.append("<div style='font-family:Segoe UI; font-size:15px; line-height:1.8; color:#111;'>");

        // HEADER
        build.append("<h1 style='text-align:center; font-weight:bold; margin-bottom:5px;'>🧾 ORDER RECEIPT</h1>");
        build.append("<hr style='border:1px solid #ccc;'>");

        // CUSTOMER INFO
        build.append("<h1><b><span style='font-weieght: bold; size='5'>").append("7 Day(s) Estimation").append("</span></b></h1><br>");
        build.append("<p><b>CUSTOMER NAME:</b> <span style='font-weight:bold;'>").append(name).append("</span></p>");
        build.append("<p><b>ADDRESS:</b> <span style='font-weight:bold;'>").append(address).append("</span></p>");
        build.append("<p><b>DESCRIPTION:</b> <span style='font-weight:bold;'>").append(description).append("</span></p>");

        build.append("<hr>");

        // PRODUCT DETAILS
        build.append("<h2 style='font-weight:bold;'>🛒 PRODUCT DETAILS</h2>");

        build.append("<p><b>PRODUCT:</b> <span style='font-weight:bold;'>").append(prodName).append("</span></p>");
        build.append("<p><b>QUANTITY:</b> <span style='font-weight:bold;'>").append(quant).append("</span></p>");

        build.append("<p><b>TOTAL PRICE:</b> ")
             .append("<span style='color:green; font-weight:bold; font-size:16px;'>₱")
             .append(total)
             .append("</span></p>");

        build.append("<hr>");

        // PAYMENT INFO
        build.append("<h2 style='font-weight:bold;'>💳 PAYMENT INFO</h2>");

        build.append("<p><b>PAYMENT METHOD:</b> <span style='font-weight:bold;'>").append(paymethod).append("</span></p>");
        build.append("<p><b>ORDER DATE:</b> <span style='font-weight:bold;'>").append(date).append("</span></p>");

        // STATUS (ALWAYS GREEN)
        build.append("<p><b>STATUS:</b> ")
             .append("<span style='color:green; font-weight:bold; font-size:15px;'>")
             .append(status.toUpperCase())
             .append("</span></p>");

        if (additionals != null && !additionals.equals("null") && !additionals.equals("[]")) {
            build.append("<p><b>NOTES:</b> <span style='font-weight:bold;'>")
                 .append(additionals)
                 .append("</span></p>");
        }

        build.append("<hr>");

        // FOOTER
        build.append("<h2 style='text-align:center; font-weight:bold;'>THANK YOU FOR YOUR ORDER</h2>");

        build.append("</div></html>");

        area.setContentType("text/html");
        area.setText(build.toString());

    } else {
        area.setText("No order details found.");
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
        image = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        area = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(222, 222, 222));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(197, 197, 197));
        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("COMPLETE");
        jLabel1.setOpaque(true);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 530, 247, 57));

        image.setText("successimage");
        jPanel1.add(image, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 312, 510));

        area.setEditable(false);
        jScrollPane2.setViewportView(area);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 580, 430));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("WAITING TO BE SHIPPED ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 530, 300, 60));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel3.setText("SUCCESFULLY PURCHASED");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        JDesktopPane desktopPane = User_config.GetPane();
        if (desktopPane != null) {
            for (JInternalFrame f : desktopPane.getAllFrames()) {
                if (f.isVisible()) {
                    f.dispose();
                }
            }
        }   
        MarketMain mark = new MarketMain(MainPane);
        MainPane.add(mark).setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jLabel1,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );          // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jLabel1,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );           // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane area;
    private javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
