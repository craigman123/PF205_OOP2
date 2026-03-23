/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dispatcher.AllOrders;

import configuration.config;
import dispatcher.connector;
import javax.swing.JDesktopPane;

/**
 *
 * @author user
 */
public class allOrders extends javax.swing.JInternalFrame {

    /**
     * Creates new form allOrders
     */
    private JDesktopPane MainPane;
    private String refNum;
    private String status;
    private int orderID;
    private int ProdID;
    private int exportID;
    
    public allOrders() {
        initComponents();
        StyleTable();
        StyleFrame();
        DisplayParcels();
    }
    
    public final void StyleTable(){
        table.setRowHeight(40);
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void DisplayParcels(){
        config conf = new config();
        
        String qry = "SELECT export_id, reference_number, export_status, export_reciever FROM exports";
        conf.displayData(qry, table);
    }
    
    private void ShowComponent() {
    config conf = new config();
    connector connect = new connector();

    String qry = "SELECT * FROM exports WHERE reference_number = ?";
    java.util.List<java.util.Map<String, Object>> exports = conf.fetchRecords(qry, refNum); 

    if (!exports.isEmpty()) {
        java.util.Map<String, Object> export = exports.get(0);

        // Extract export info
        String address = export.get("export_distination").toString();
        String receiverName = export.get("export_reciever").toString();
        String exportDate = export.get("date_export").toString();
        status = export.get("export_status").toString();
        String reference = export.get("reference_number").toString();
        String dispatcherName = export.get("export_dispatcherName").toString();
        
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
            
            if(status.equals("In Transit")){
                sb.append("\n");
                sb.append("Product not yet Arrived in Hub: ").append("\n");
                sb.append("\n");
            } else {
                sb.append("\n");
                sb.append("Delivered By: ").append(dispatcherName).append("\n");
                sb.append("\n");
            }

            area.setText(sb.toString());
        }
    } else {
        area.setText("No export record found for reference number: " + refNum);
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

        RNfield = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        fresh = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        area = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        RNfield.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        RNfield.setForeground(new java.awt.Color(153, 153, 153));
        RNfield.setText("Enter Reference Number");

        jLabel1.setBackground(new java.awt.Color(202, 202, 202));
        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Get Info");
        jLabel1.setOpaque(true);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        fresh.setBackground(new java.awt.Color(209, 209, 209));
        fresh.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        fresh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fresh.setText("SEARCH");
        fresh.setOpaque(true);
        fresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                freshMouseClicked(evt);
            }
        });

        area.setColumns(20);
        area.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        area.setRows(5);
        jScrollPane3.setViewportView(area);

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
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fresh, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(RNfield, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RNfield, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        String RN = RNfield.getText();

        if(!RN.isEmpty() || RN != null){
            refNum = RN;
        }// TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked

    private void freshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_freshMouseClicked
        DisplayParcels();        // TODO add your handling code here:
    }//GEN-LAST:event_freshMouseClicked

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        int row = table.getSelectedRow();
        Object ref = table.getValueAt(row, 1);
        refNum = (String) ref;
        ShowComponent();// TODO add your handling code here:
    }//GEN-LAST:event_tableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField RNfield;
    private javax.swing.JTextArea area;
    private javax.swing.JLabel fresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
