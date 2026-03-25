/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Orders;

import Profiles.session;
import User.Market.Product_Detail;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public final class SeeProductStatus extends javax.swing.JInternalFrame {

    /**
     * Creates new form SeeProductStatus
     */
    private int orderId;
    private int ProdId;
    private JDesktopPane MainPane;
    
    public SeeProductStatus(int orderID, int ProdID, JDesktopPane Pane) {
        this.orderId = orderID;
        this.ProdId = ProdID;
        this.MainPane = Pane;
        
        initComponents();
        ShowComponent();
        StyleFrame();
        CancelButtonShow();
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void ShowComponent(){
        config conf = new config();
        session see = new session();
        
        String qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, ProdId); 
        
        java.util.Map<String, Object> prod = result.get(0);
        String name = prod.get("prod_name").toString();
        String cat = prod.get("prod_category").toString();
        String rare = prod.get("prod_rarity").toString();
        String cash = prod.get("prod_price").toString();
        String descript = prod.get("prod_descript").toString();
        String stock = prod.get("prod_stock").toString();
        String stat = prod.get("prod_status").toString();
        int prodId = ((Number) prod.get("prod_id")).intValue();
        String seller = prod.get("prod_addedBy").toString();
        String finalId = String.valueOf(prodId);

        String statColor;
        switch (stat) {
            case "Active":
                statColor = "#00CC82";
                break;
            case "Inactive":
                statColor = "#CC0041";
                break;
            case "Pending":
                statColor = "#3100B5";
                break;
            case "Suspended":
                statColor = "#69A600";
                break;
            case "Sold Out":
                statColor = "#8F8F8F";
                break;
            case "Out of Stock":
                statColor = "#A82F00";
                break;
            case "Archived":
                statColor = "Archived";
                break;
            case "Discontinued":
                statColor = "black";
                break;
            default:
                statColor = "black";
                break;
        }

        // Build HTML with colored status
        String orderInfoText = "<html>" +
               "<span style='font-size: 22px;'>" + "----" + name + "----" + "</span><br>" +
               "Category: " + cat + "<br>" +
               "Rarity: " + rare + "<br>" +
               "Price: " + cash + "<br>" +
               "Stock: " + stock + "<br>" +
               "ID: " + finalId + "<br>" +
               "Added By: " + seller + "<br>" +
               "Status: <span style='color:" + statColor + "'>" + stat + "</span>" +
               "</html>";

        orderInfo.setText(orderInfoText);
        description.setText(descript);

        try {
            Object imgPath = prod.get("prod_image");
            File file = new File(System.getProperty("user.dir") + File.separator +
                                 "Input_Images" + File.separator + imgPath);

            if (!file.exists()) {
                throw new RuntimeException("Image file not found: " + file.getAbsolutePath());
            }

            ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
            Image img = originalIcon.getImage();

            // Use fixed dimensions for display
            int displayWidth = 375;   // max width in label
            int displayHeight = 300;  // max height in label

            int imgWidth = originalIcon.getIconWidth();
            int imgHeight = originalIcon.getIconHeight();

            double widthRatio = (double) displayWidth / imgWidth;
            double heightRatio = (double) displayHeight / imgHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            int newWidth = (int)(imgWidth * ratio);
            int newHeight = (int)(imgHeight * ratio);

            Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            imageUpload.setIcon(new ImageIcon(scaledImage));
            imageUpload.setText(""); // remove "No Image" text

        } catch (Exception e) {
            e.printStackTrace();
            imageUpload.setIcon(null);
            imageUpload.setText("No Image");
        }
        
        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> prodResult = conf.fetchRecords(query, orderId); 
        
        if(!prodResult.isEmpty()){
            java.util.Map<String, Object> userBought = prodResult.get(0);
            int order_id = orderId;
            int prod_id = ((Number) userBought.get("prod_id")).intValue();
            String orderStatus = userBought.get("order_status").toString();
            int orderQuantity = ((Number) userBought.get("order_quantity")).intValue();
            String orderTotal = userBought.get("order_totalPrice").toString();
            String paymentMethod = userBought.get("order_paymentMethod").toString();
            String orderDate = userBought.get("order_date").toString();
            String extras = userBought.get("order_additionals").toString();
            
            String orderExtras = "None";
            if(extras.equals("[]")){
                orderExtras = "None";
            } else {
                orderExtras = extras;
            }
            
            String orderStatColor;
        switch (stat) {
            case "Pending":
                orderStatColor = "#00855B";
                break;
            case "In Transit":
                orderStatColor = "#0D00CC";
                break;
            case "On Delivery":
                orderStatColor = "#CCA000";
                break;
            case "Completed":
                orderStatColor = "#00C915";
                break;
            default:
                orderStatColor = "black";
                break;
        }
        
        java.time.LocalDate orderLocalDate = java.time.LocalDate.parse(orderDate.substring(0, 10));

        int randomDays = 4 + new java.util.Random().nextInt(4); // 3–7

        // Add days to get estimated delivery
        java.time.LocalDate estimatedDate = orderLocalDate.plusDays(randomDays);

        // Format date nicely
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedEstimate = estimatedDate.format(formatter);
        
        String estimationDays = GetestimatedDate(formattedEstimate);
        
        String textPay = "";
        
        if(paymentMethod.equals("Cash on Delivery")){
            textPay = "Please Prepare a Total Cash of ";
        }else{
            textPay = "Paid: ";
        }
        
            
            String orderEstimationDelivered = "<html>" +
            "<h1 style='font-size:24px;'>== ESTIMATION ==</h1>" +
                    
                  "<br>" + "<span style='font-size: 23px;'>" + estimationDays + "</span><br><br>" +

            "Order ID: " + order_id + "<br>" +
            "Quantity: " + orderQuantity + "<br><br>" +
            "" + textPay + "" + "<i>" + orderTotal + "</i><br>" +
            "Payment: " + paymentMethod + "<br>" +
            "Order Date: " + orderDate + "<br>" +
            "Estimated Delivery: " + formattedEstimate + "<br>" +       
            "Extras: " + orderExtras + "<br>" +
            "Status: <span style='color:" + orderStatColor + ";'>" + orderStatus + "</span>" +

            "</html>";

        orderEstimation.setText(orderEstimationDelivered);
            System.out.println("ProdID: " + prod_id + "\nUser ID: " + see.GetID() + "\nOrder ID: " + order_id);
            
        }
    }
    
    private String GetestimatedDate(String formattedEstimateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        LocalDate estimatedDate = LocalDate.parse(formattedEstimateStr, formatter);

        LocalDate currentDate = LocalDate.now(); 

        long daysLeft = ChronoUnit.DAYS.between(currentDate, estimatedDate);

        String estimationDays;
        if (daysLeft > 0) {
            estimationDays = daysLeft + " day(s) left";
        } else if (daysLeft == 0) {
            estimationDays = "Arriving today!";
        } else {
            estimationDays = "Already arrived";
        }
        
        return estimationDays;
    }
    
    public void CancelOrder() {
    config conf = new config();
    session see = new session();

        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> prodResult = conf.fetchRecords(query, orderId);

        if (!prodResult.isEmpty()) {

            java.util.Map<String, Object> userBought = prodResult.get(0);
            String orderStatus = userBought.get("order_status").toString();
            String price = userBought.get("order_totalPrice").toString();
            String address = userBought.get("order_shippingAddress").toString();
            
            String[] parts = address.split(",");
            String cancelname = parts[0].trim();
           
            query = "SELECT * FROM products WHERE order_id = ?";
            java.util.List<java.util.Map<String, Object>> resu = conf.fetchRecords(query, ProdId);
            
            String cat = "Common";
            
            if(!resu.isEmpty()){
                java.util.Map<String, Object> result = resu.get(0);
                cat = result.get("prod_category").toString();
                
            }

            if (orderStatus.equals("Pending")) {

                int result = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to cancel this order?",
                    "Cancellation",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {

                    String qry = "DELETE FROM userOrders WHERE order_id = ?";
                    conf.deleteRecord(qry, orderId);
                    
                    qry = "INSERT INTO cancelledOrders(order_id, priceLost, prod_category, cancelledBy) VALUES(?,?,?,?)";
                    conf.addRecordAndReturnId(qry, orderId, price, cat, cancelname);
                    
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = now.format(formatter);
                    String queryNow = "INSERT INTO notification(prod_id, user_id, n_content, date, read) VALUES (?,?,?,?,?)";
                    conf.addRecordAndReturnId(queryNow, ProdId, see.GetID(), "Successfully Cancel Order:", formattedDate, false);
                    
                    queryNow = "INSERT INTO logs(prod_id, user_id, dateTime, log_action) VALUES(?,?,?,?)";
                    conf.addRecordAndReturnId(queryNow, ProdId, see.GetID(), formattedDate, "Delete");

                    JOptionPane.showMessageDialog(
                        null,
                        "Order Cancelled!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    this.dispose();
                }

            } else if (orderStatus.equals("Completed")) {

                int deleteres = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this order information?",
                    "Deletion",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (deleteres == JOptionPane.OK_OPTION) {

                    String qry = "UPDATE userOrders SET order_visibility = ? WHERE order_id = ?";
                    conf.updateRecord(qry, "false", orderId);
                    
                    LocalDateTime now = LocalDateTime.now();
                    Timestamp date = Timestamp.valueOf(now);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = now.format(formatter);
                    String queryNow = "INSERT INTO notification(prod_id, user_id, n_content, date, read) VALUES (?,?,?,?,?)";
                    conf.addRecordAndReturnId(queryNow, ProdId, see.GetID(), "Successfully Deleted Order Information:", formattedDate, false);
                    
                    queryNow = "INSERT INTO logs(prod_id, user_id, dateTime, log_action) VALUES(?,?,?,?)";
                    conf.addRecordAndReturnId(queryNow, ProdId, see.GetID(), formattedDate, "Update");

                    JOptionPane.showMessageDialog(
                        null,
                        "Order Information Deleted!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "This order cannot be modified.",
                    "Notice",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } else {
            System.out.println("Order ID not found");
        }
    }
    
    public void CancelButtonShow() {
        config conf = new config();

        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> prodResult = conf.fetchRecords(query, orderId);

        if (!prodResult.isEmpty()) {

            java.util.Map<String, Object> userBought = prodResult.get(0);
            String orderStatus = userBought.get("order_status").toString();

            if (orderStatus.equals("Completed")) {
                cancelOrderBtn.setText("Delete");
            } else if (!orderStatus.equals("Pending")) {
                cancelOrderBtn.setEnabled(false); 
                cancelOrderBtn.setBackground(Color.decode("#404040"));
                cancelOrderBtn.setForeground(Color.decode("#000000"));
            }
            
            if(!orderStatus.equals("Pending")){
                editOrderBtn.setEnabled(false);
                editOrderBtn.setBackground(Color.decode("#404040"));
                editOrderBtn.setForeground(Color.decode("#000000"));
            }

        } else {
            System.out.println("Order ID not found");
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
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        imageUpload = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        orderInfo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        orderEstimation = new javax.swing.JLabel();
        editOrderBtn = new javax.swing.JLabel();
        buyAgain = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        cancelOrderBtn = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel1.setText("ORDER STATUS");

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setText("X");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 320, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(343, 343, 343))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        imageUpload.setText("Image");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageUpload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageUpload, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        orderInfo.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        orderInfo.setText("Product Info");
        orderInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(orderInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(orderInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        orderEstimation.setFont(new java.awt.Font("Trebuchet MS", 1, 22)); // NOI18N
        orderEstimation.setText("Estimation");
        orderEstimation.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel4.add(orderEstimation, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 450));

        editOrderBtn.setBackground(new java.awt.Color(204, 204, 204));
        editOrderBtn.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        editOrderBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        editOrderBtn.setText("Edit Order");
        editOrderBtn.setOpaque(true);
        editOrderBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editOrderBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editOrderBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                editOrderBtnMouseExited(evt);
            }
        });
        jPanel4.add(editOrderBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 110, 50));

        buyAgain.setBackground(new java.awt.Color(204, 204, 204));
        buyAgain.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        buyAgain.setForeground(new java.awt.Color(0, 177, 60));
        buyAgain.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        buyAgain.setText("BUY AGAIN");
        buyAgain.setOpaque(true);
        buyAgain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buyAgainMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buyAgainMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buyAgainMouseExited(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        description.setEditable(false);
        description.setColumns(20);
        description.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        description.setRows(5);
        jScrollPane1.setViewportView(description);

        cancelOrderBtn.setBackground(new java.awt.Color(204, 204, 204));
        cancelOrderBtn.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        cancelOrderBtn.setForeground(new java.awt.Color(255, 0, 0));
        cancelOrderBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancelOrderBtn.setText("CANCEL");
        cancelOrderBtn.setOpaque(true);
        cancelOrderBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelOrderBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelOrderBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelOrderBtnMouseExited(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelOrderBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buyAgain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buyAgain, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buyAgainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buyAgainMouseClicked
        Product_Detail proddet = new Product_Detail(ProdId, MainPane);
        MainPane.add(proddet).setVisible(true);                // TODO add your handling code here:
    }//GEN-LAST:event_buyAgainMouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void cancelOrderBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelOrderBtnMouseClicked
        CancelOrder();        // TODO add your handling code here:
    }//GEN-LAST:event_cancelOrderBtnMouseClicked

    private void buyAgainMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buyAgainMouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            buyAgain,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_buyAgainMouseEntered

    private void buyAgainMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buyAgainMouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            buyAgain,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );          // TODO add your handling code here:
    }//GEN-LAST:event_buyAgainMouseExited

    private void cancelOrderBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelOrderBtnMouseEntered
        animation ani = new animation();
        config conf = new config();
        
        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> prodResult = conf.fetchRecords(query, orderId);

        if (!prodResult.isEmpty()) {
            java.util.Map<String, Object> userBought = prodResult.get(0);
            String orderStatus = userBought.get("order_status").toString();
            
            if(orderStatus.equals("Completed") || orderStatus.equals("Pending")){
                ani.transitionBackground(
                    cancelOrderBtn,
                    Color.decode("#CCCCCC"),
                    Color.decode("#B3B3B3"),
                    200 
                );   
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_cancelOrderBtnMouseEntered

    private void cancelOrderBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelOrderBtnMouseExited
        animation ani = new animation();
        config conf = new config();
        
        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> prodResult = conf.fetchRecords(query, orderId);

        if (!prodResult.isEmpty()) {
            java.util.Map<String, Object> userBought = prodResult.get(0);
            String orderStatus = userBought.get("order_status").toString();
            
            if(orderStatus.equals("Completed") || orderStatus.equals("Pending")){
                ani.transitionBackground(
                    cancelOrderBtn,
                    Color.decode("#B3B3B3"),
                    Color.decode("#CCCCCC"),
                    200
                );    
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_cancelOrderBtnMouseExited

    private void editOrderBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editOrderBtnMouseExited
        animation ani = new animation();
        config conf = new config();
        
        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> prodResult = conf.fetchRecords(query, orderId);

        if (!prodResult.isEmpty()) {
            java.util.Map<String, Object> userBought = prodResult.get(0);
            String orderStatus = userBought.get("order_status").toString();
            
            if(orderStatus.equals("Pending")){
                ani.transitionBackground(
                    editOrderBtn,
                    Color.decode("#B3B3B3"),
                    Color.decode("#CCCCCC"),
                    200
                );    
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_editOrderBtnMouseExited

    private void editOrderBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editOrderBtnMouseEntered
        animation ani = new animation();
        config conf = new config();
        
        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> prodResult = conf.fetchRecords(query, orderId);

        if (!prodResult.isEmpty()) {
            java.util.Map<String, Object> userBought = prodResult.get(0);
            String orderStatus = userBought.get("order_status").toString();
            
            if(orderStatus.equals("Pending")){
                ani.transitionBackground(
                    editOrderBtn,
                    Color.decode("#CCCCCC"),
                    Color.decode("#B3B3B3"),
                    200
                );   
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_editOrderBtnMouseEntered

    private void editOrderBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editOrderBtnMouseClicked
        EditOrderInfo ordinf = new EditOrderInfo(ProdId, orderId);
        config conf = new config();
        
        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> orderRes = conf.fetchRecords(query, orderId);
        
        if(!orderRes.isEmpty()){
            java.util.Map<String, Object> userBought = orderRes.get(0);
            String orderStatus = userBought.get("order_status").toString();
            
            if(orderStatus.equals("Pending")){ MainPane.add(ordinf).setVisible(true); }
        }
    }//GEN-LAST:event_editOrderBtnMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel buyAgain;
    private javax.swing.JLabel cancelOrderBtn;
    private javax.swing.JTextArea description;
    private javax.swing.JLabel editOrderBtn;
    private javax.swing.JLabel imageUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel orderEstimation;
    private javax.swing.JLabel orderInfo;
    // End of variables declaration//GEN-END:variables
}
