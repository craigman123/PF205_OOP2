/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dispatcher;

import Profiles.session;
import configuration.animation;
import configuration.config;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public final class pendingParcelPanel extends javax.swing.JInternalFrame {

    /**
     * Creates new form pendingParcelPanel
     */
    private String ref;
    
    public pendingParcelPanel(String r) {
        this.ref = r;
        
        initComponents();
        StyleFrame();
        ShowComponent();
        LoadSmt();
    }
    
    public void LoadSmt(){
        
    }
    
    private String status;
    private int orderID;
    private int exportID;
    
    private void ShowComponent() {
    config conf = new config();
    connector connect = new connector();

    String qry = "SELECT * FROM exports WHERE reference_number = ?";
    java.util.List<java.util.Map<String, Object>> exports = conf.fetchRecords(qry, ref); 

    if (!exports.isEmpty()) {
        java.util.Map<String, Object> export = exports.get(0);

        // Extract export info
        String address = export.get("export_distination").toString();
        String receiverName = export.get("export_reciever").toString();
        String exportDate = export.get("date_export").toString();
        status = export.get("export_status").toString();
        String reference = export.get("reference_number").toString();
        
        exportID = ((Number) export.get("export_id")).intValue();
        orderID = ((Number) export.get("order_id")).intValue(); // Corrected, you used date_export before

        qry = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> orders = conf.fetchRecords(qry, orderID); 

        if (!orders.isEmpty()) {
            java.util.Map<String, Object> order = orders.get(0);

            String productName = order.get("order_prod").toString();
            String totalPrice = order.get("order_totalPrice").toString();
            String quantity = order.get("order_quantity").toString();
            int prodID = ((Number) order.get("prod_id")).intValue();

            qry = "SELECT * FROM products WHERE prod_id = ?";
            java.util.List<java.util.Map<String, Object>> products = conf.fetchRecords(qry, prodID); 

            String priceEach = "";
            if (!products.isEmpty()) {
                java.util.Map<String, Object> product = products.get(0);
                priceEach = product.get("prod_price").toString();
            }

            // Build professional info string
            StringBuilder sb = new StringBuilder();
            sb.append("+------------------------------------------------+\n");
            sb.append("              PARCEL DELIVERY DETAILS           \n");
            sb.append("+------------------------------------------------+\n");
            sb.append("Receiver Name  : ").append(receiverName).append("\n");
            sb.append("Delivery Addr  : ").append(address).append("\n");
            sb.append("Export Date    : ").append(exportDate).append("\n");
            sb.append("Order Status   : ").append(status).append("\n");
            sb.append("\n");
            sb.append("Product Name   : ").append(productName).append("\n");
            sb.append("Quantity       : ").append(quantity).append("\n");
            sb.append("Price Each     : ₱").append(priceEach).append("\n");
            sb.append("Total Price    : ₱").append(totalPrice).append("\n");
            sb.append("+------------------------------------------------+\n");
            sb.append("Reference Number: ").append(reference).append("\n");
            sb.append("+------------------------------------------------+\n");
            sb.append("+------------------------------------------------+\n");

            if ("Delivered".equalsIgnoreCase(status)) {
                sb.append("✅ This parcel has been delivered successfully.\n");
            } else if ("Pending".equalsIgnoreCase(status)) {
                sb.append("⌛ The parcel is still to be Delivered.\n");
            } else {
                sb.append("⚠️ Current status: ").append(status).append("\n");
            }

            area.setText(sb.toString());
        }
    } else {
        area.setText("No export record found for reference number: " + ref);
    }
}
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void ParcelDelivered() {
        config conf = new config();
        session see = new session();
        
        String qry = "SELECT * FROM users WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> user = conf.fetchRecords(qry, see.GetID()); 

        String userName = "";
        if(!user.isEmpty()){
            java.util.Map<String, Object> exports = user.get(0);
            userName = exports.get("user_name").toString();
            
        }
        
        try {
            String qryOrder = "UPDATE userOrders SET order_status = ? WHERE order_id = ?";
            conf.updateRecord(qryOrder, "Completed", orderID);

            String qryExport = "UPDATE exports SET export_status = ?, export_dispatcherName = ? WHERE reference_number = ?";
            conf.updateRecord(qryExport, "Delivered", userName, ref);

            System.out.println("✅ Parcel marked as delivered successfully.");
            JOptionPane.showMessageDialog(
                null, 
                "✅ Parcel marked as delivered successfully!", 
                "Parcel Delivered", 
                JOptionPane.INFORMATION_MESSAGE
            );
            
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to update parcel status.");
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
        MainScroll = new javax.swing.JScrollPane();
        area = new javax.swing.JTextArea();

        jPanel1.setBackground(new java.awt.Color(220, 220, 220));

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Parcel Delivered");
        jLabel1.setOpaque(true);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        MainScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        area.setEditable(false);
        area.setColumns(20);
        area.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        area.setRows(5);
        MainScroll.setViewportView(area);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(278, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(MainScroll)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(MainScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        ParcelDelivered();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane MainScroll;
    private javax.swing.JTextArea area;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
