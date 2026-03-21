/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Orders;


import Profiles.session;
import User.Market.Payment;
import User.User_config;
import configuration.animation;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import configuration.config;
import java.awt.Dimension;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
/**
 *
 * @author USER15
 */
public final class EditOrderInfo extends javax.swing.JInternalFrame {

    /**
     * Creates new form OrderInfo
     */
    private int id;
    private ButtonGroup group;
    private int orderId;
    
    public EditOrderInfo(int ProdID, int orderID) {
        User_config conf = new User_config();
        
        this.id = ProdID;
        this.orderId = orderID;
        
        initComponents();
        StyleFrame();
        StyleObjects();
        conf.loadAllCountries(comboCountry);
        groupBtn();
        ShowDue();
        updateTotal();
        PrefillInputs();
    }
    
    public void PrefillInputs(){
        config conf = new config();
        
        String query = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> orderRes = conf.fetchRecords(query, orderId);
        
        if(!orderRes.isEmpty()){
            java.util.Map<String, Object> foundOrder = orderRes.get(0);
            int orderID = orderId;
            int userID = ((Number) foundOrder.get("user_id")).intValue();
            int productID = ((Number) foundOrder.get("prod_id")).intValue();
            String status = foundOrder.get("order_status").toString();
            String prodName = foundOrder.get("order_prod").toString();
            int quantity = ((Number) foundOrder.get("order_quantity")).intValue();
            String totalPrice = foundOrder.get("order_totalPrice").toString();
            String address = foundOrder.get("order_shippingAddress").toString();
            String additionals = foundOrder.get("order_additionals").toString();
            
            label.setText(String.valueOf(quantity));
            
            String[] parts = address.split(",");
            
            String name = parts[0].trim();
            String b = parts[1].trim();
            String c = parts[2].trim();
            String province = parts[3].trim();
            String reg = parts[4].trim();
            String country = parts[5].trim();
            String zipcode = parts[6].trim();
            String desc = parts[7].trim();
            
            if(desc.isEmpty() || desc.equals("Specific Info . . .")){
                desc = "Specific Info . . .";
            }
            
            Color black = Color.decode("#000000");

            name1.setText(name);
            name1.setForeground(black);

            comboCountry.setSelectedItem(country);
            comboCountry.setForeground(black);

            prov.setText(province);
            prov.setForeground(black);

            city.setText(c);
            city.setForeground(black);

            region.setText(reg);
            region.setForeground(black);

            barangay.setText(b);
            barangay.setForeground(black);

            zipCode.setText(zipcode);
            zipCode.setForeground(black);

            spec.setText(desc);
            spec.setForeground(black);
            
            if (additionals != null && !additionals.trim().isEmpty()) {
                String extras = additionals.replace("[", "").replace("]", "");
                String[] items = extras.split(",");

                for (String item : items) {
                    item = item.trim();

                    if (item.equalsIgnoreCase("Mega")) {
                        mega.setSelected(true);
                    } else if (item.equalsIgnoreCase("Fast")) {
                        fast.setSelected(true);
                    } else if (item.equalsIgnoreCase("Ultra")) {
                        ultra.setSelected(true);
                    } else if (item.equalsIgnoreCase("Normal")) {
                        norma.setSelected(true);
                    } else if (item.equalsIgnoreCase("PPC")) {
                        ppc.setSelected(true);        // checkbox
                    } else if (item.equalsIgnoreCase("Warranty")) {
                        warranty.setSelected(true);  // checkbox
                    }
                    updateTotal(); 
                }
            updateTotal(); 
        }
    }
    }
    
    
    public final void groupBtn(){
        group = new ButtonGroup();
        
        norma.setSelected(true);
        group.add(norma);
        group.add(fast);
        group.add(ultra);
        group.add(mega);
        group.clearSelection();
    }
    
    private final float Shippingfee = 120.00f;
    private final float early = 6.0f;
    private final float late = 7.0f;
    private float totalFee;
    
   private void updateTotal() {
        float total = Shippingfee;
        float minDays = early;
        float maxDays = late;
        
        if(norma.isSelected()){
            total = 120;
            minDays = 6;
            maxDays = 7;
        }

        if (fast.isSelected()) {
            total += 20;
            minDays -= 0.5f;
            maxDays -= 1f;
        }

        if (ultra.isSelected()) {
            total += 50;
            minDays -= 1.5f;
            maxDays -= 1.5f;
        }

        if (mega.isSelected()) {
            total += 80;
            minDays -= 2f;
            maxDays -= 1.8f;
        }

        if (ppc.isSelected()) {
            total += 20;
        }
        
        if(warranty.isSelected()){
            total += 100;
        }

        if (minDays < 1) minDays = 1;
        if (maxDays < minDays) maxDays = minDays;

        fee.setText(String.format("SHIPPING FEE: %.2f", total));
        days.setText(String.format("TOTAL DAYS: %.1f - %.1f Days", minDays, maxDays));
        
        totalFee = total;
        ShowDue();
    }
   
    public final void ShowDue(){
        StringBuilder message = new StringBuilder("<html>");
        config conf = new config();
        
        int res = 0, promo = 0;
        int value = Integer.parseInt(label.getText());
        
        String qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, id); 
        System.out.println(id);
        
        if(!result.isEmpty()){
            java.util.Map<String, Object> prod = result.get(0);
            String pr = prod.get("prod_price").toString();
            String prodName = prod.get("prod_name").toString();
            String cat = prod.get("prod_category").toString();
            String stat = prod.get("prod_status").toString();
            
            int price = Integer.parseInt(pr);

            message.append("Shipping fee: ").append(totalFee).append("<br>");
            message.append("Product Price: ").append(price).append("<br>");
            message.append("Quantity: ").append(value).append("<br>");

            res = value * price;

            message.append("Price Total: ").append(res).append(".00<br>");
            message.append("<br>");
            message.append(prodName).append("<br>");
            message.append(cat).append("<br>");
            message.append(stat).append("<br>");
            message.append("</html>");

             ShowDue.setText(message.toString());

             int total = (int) totalFee + res + promo;
             due.setText(String.valueOf(total));
        }
        
        int total = (int) totalFee + res + promo;
        System.out.println(total);
        
        String value1 = "P " + String.valueOf(total) + ".00";
        due.setText(value1);
    }
    
    public boolean validateInputs() {
        StringBuilder message = new StringBuilder();
        int finalCode = 0;

        String nm = name1.getText().trim();
        String count = (String) comboCountry.getSelectedItem();
        String province = prov.getText().trim();
        String reg = region.getText().trim();
        String code = zipCode.getText().trim();
        String cit = city.getText().trim();
        String barag = barangay.getText().trim();

        if (nm.isEmpty() || nm.equals("Recievers Name")) {
            message.append("• Name is required.\n");
        }

        if (count == null || count.equals("Select Country")) {
            message.append("• Please select a country.\n");
        }

        if (province.isEmpty() || province.equals("Province")) {
            message.append("• Province is required.\n");
        }

        if (reg.isEmpty() || reg.equals("Region")) {
            message.append("• Region is required.\n");
        }

        if (cit.isEmpty() || cit.equals("City")) {
            message.append("• City is required.\n");
        }

        if (barag.isEmpty() || barag.equals("Barangay")) {
            message.append("• Barangay is required.\n");
        }

        if (code.isEmpty() || code.equals("Zip Code")) {
            message.append("• Zip Code is required.\n");
        } else {
            try {
                finalCode = Integer.parseInt(code);
            } catch (NumberFormatException e) {
                message.append("• Zip Code must be numeric.\n");
            }
        }

        if (message.length() > 0) {
            JOptionPane.showMessageDialog(this, message.toString(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
    Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
    Border grayBorder = BorderFactory.createLineBorder(Color.GRAY, 2);
    
    public final void StyleObjects(){
        due.setBorder(grayBorder);
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void UpdateInputs() {
    config conf = new config();
    session see = new session();

    // Collect delivery info
    String desc = spec.getText().trim();
    if (desc.isEmpty() || desc.equals("Specific Info . . .")) {
        desc = "Specific Info . . ."; // default if empty
    }

    String deliveryInfo = name1.getText().trim() + ", "
            + barangay.getText().trim() + ", "
            + city.getText().trim() + ", "
            + prov.getText().trim() + ", "
            + region.getText().trim() + ", "
            + comboCountry.getSelectedItem().toString() + ", "
            + zipCode.getText().trim() + ", "
            + desc; 

    String dueText = due.getText().replaceAll("[^0-9.]", "");
    float Due = 0;
    try {
        Due = Float.parseFloat(dueText);
    } catch (NumberFormatException e) {
        System.out.println("Invalid due value");
    }

    java.util.List<String> additionalsList = new java.util.ArrayList<>();

    if (mega.isSelected()) additionalsList.add("Mega");
    if (fast.isSelected()) additionalsList.add("Fast");
    if (ultra.isSelected()) additionalsList.add("Ultra");

    if (ppc.isSelected()) additionalsList.add("PPC");
    if (warranty.isSelected()) additionalsList.add("Warranty");

    String[] additionals = additionalsList.toArray(new String[0]);

    String query = "SELECT * FROM userOrders WHERE order_id = ?";
    java.util.List<java.util.Map<String, Object>> orderRes = conf.fetchRecords(query, orderId);

    if (!orderRes.isEmpty()) {
        java.util.Map<String, Object> foundOrder = orderRes.get(0);

        String oldDeliveryInfo = foundOrder.get("order_shippingAddress").toString();
        String oldDueText = foundOrder.get("order_totalPrice").toString(); 
        float oldDue = 0;
        try {
            oldDue = Float.parseFloat(oldDueText.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            System.out.println("Invalid old due value");
        }

        String oldAdd = foundOrder.get("order_additionals").toString().replace("[", "").replace("]", "");
        String[] oldAdditionals = oldAdd.isEmpty() ? new String[0] : oldAdd.split(",");
        for (int i = 0; i < oldAdditionals.length; i++) {
            oldAdditionals[i] = oldAdditionals[i].trim();
        }
        
        boolean deliveryUnchanged = deliveryInfo.equals(oldDeliveryInfo);
        boolean dueUnchanged = Due == oldDue;
        boolean additionalsUnchanged = java.util.Arrays.equals(additionals, oldAdditionals);
        System.out.println("booleans: " + deliveryUnchanged + dueUnchanged + additionalsUnchanged);
        System.out.println("Current Due: " + Due + "\nOld Due: " + oldDue);

        if (deliveryUnchanged && dueUnchanged && additionalsUnchanged) {
            JOptionPane.showMessageDialog(
                null, 
                "No changes detected!", 
                "Unchange", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        String additionalsStr = java.util.Arrays.toString(additionals);

        String qry = "UPDATE userOrders SET "
                + "order_shippingAddress = ?, "
                + "order_totalPrice = ?, "
                + "order_additionals = ? "
                + "WHERE order_id = ?";

        conf.updateRecord(qry, deliveryInfo, Due, additionalsStr, orderId);
        
        LocalDateTime now = LocalDateTime.now();
        Timestamp date = Timestamp.valueOf(now);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        String queryNow = "INSERT INTO notification(prod_id, user_id, n_content, date) VALUES (?,?,?,?)";
        conf.addRecordAndReturnId(queryNow, id, see.GetID(), "Successfully Edited Order Information:", formattedDate);

        queryNow = "INSERT INTO logs(prod_id, user_id, dateTime, log_action) VALUES(?,?,?,?)";
        conf.addRecordAndReturnId(queryNow, id, see.GetID(), formattedDate, "Update");
        
        JOptionPane.showMessageDialog(
            null, 
            "Order updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
        this.dispose();
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
        jLabel7 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        label = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        ShowDue = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        due = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        spec = new javax.swing.JTextArea();
        comboCountry = new javax.swing.JComboBox<>();
        zipCode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        fee = new javax.swing.JLabel();
        days = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        name1 = new javax.swing.JTextField();
        barangay = new javax.swing.JTextField();
        prov = new javax.swing.JTextField();
        city = new javax.swing.JTextField();
        region = new javax.swing.JTextField();
        fast = new javax.swing.JRadioButton();
        ultra = new javax.swing.JRadioButton();
        mega = new javax.swing.JRadioButton();
        norma = new javax.swing.JRadioButton();
        ppc = new javax.swing.JCheckBox();
        warranty = new javax.swing.JCheckBox();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel1.setText("EDIT ORDER INFORMATION");

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel7.setText("X");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(245, 245, 245))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 979, -1));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(311, 567, 668, 10));

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(9, 143, 70));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CONFIRM");
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
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 580, 207, 56));

        label.setBackground(new java.awt.Color(204, 204, 204));
        label.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setText("1");
        label.setOpaque(true);
        getContentPane().add(label, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 600, 113, 39));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(311, 85, 11, 473));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(647, 94, 9, 471));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        ShowDue.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        ShowDue.setText("Payment Info");
        ShowDue.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setText("PAYMENT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ShowDue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(jLabel3)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(ShowDue, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(657, 129, 280, 380));

        due.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        due.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        due.setText("DUE");
        getContentPane().add(due, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 520, 210, 43));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setToolTipText("");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        spec.setEditable(false);
        spec.setColumns(20);
        spec.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        spec.setForeground(new java.awt.Color(153, 153, 153));
        spec.setRows(5);
        spec.setText("Specific Info . . .");
        spec.setWrapStyleWord(true);
        spec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                specMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(spec);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 468, 290, 170));

        comboCountry.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCountryActionPerformed(evt);
            }
        });
        getContentPane().add(comboCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 287, 43));

        zipCode.setEditable(false);
        zipCode.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        zipCode.setForeground(new java.awt.Color(153, 153, 153));
        zipCode.setText("Zip Code");
        zipCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zipCodeMouseClicked(evt);
            }
        });
        getContentPane().add(zipCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 290, 40));

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel4.setText("QUANTITY:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 610, -1, -1));

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel5.setText("ESTIMATED PRODUCT ARRIVAL");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 90, -1, -1));

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel6.setText("DELIVERY INFORMATION");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel8.setText("PAYMENT INFORMATION");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 90, -1, -1));

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel9.setText("TRANSIT - 4 - 5 DAY ");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, -1, 30));

        jLabel11.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel11.setText("PRODUCT  PROCESS - 1 DAY (P 100.00)  ");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 130, -1, 30));

        jLabel13.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel13.setText("PRODUCT SHIPPED -  1 DAY ");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, -1, 30));

        jLabel15.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel15.setText("COUNTRY EVENTS (NONE) - 0 DAYS");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 280, -1, 30));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(204, 0, 0));
        jLabel17.setText("DAYS MAY INCREASED DUE TO SOME CIRSCUMSTANCES");
        jLabel17.setAutoscrolls(true);
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 550, 320, 20));

        fee.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        fee.setText("SHIPPING FEE: ");
        getContentPane().add(fee, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 520, 300, -1));

        days.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        days.setText("TOTAL DAYS: ");
        getContentPane().add(days, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 490, 300, -1));

        jLabel20.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel20.setText("TOTAL:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(657, 528, -1, -1));

        jLabel22.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel22.setText("OUT FOR DELIVERY - 1 DAY (P 20.00)");
        jLabel22.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, -1, 30));

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 16)); // NOI18N
        jLabel23.setText("WEATHER CONDITION (GOOD) - 0 DAYS");
        jLabel23.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 250, -1, 30));

        name1.setEditable(false);
        name1.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        name1.setForeground(new java.awt.Color(153, 153, 153));
        name1.setText("Recievers Name");
        name1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                name1MouseClicked(evt);
            }
        });
        name1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                name1KeyTyped(evt);
            }
        });
        getContentPane().add(name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 290, 40));

        barangay.setEditable(false);
        barangay.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        barangay.setForeground(new java.awt.Color(153, 153, 153));
        barangay.setText("Barangay");
        barangay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barangayMouseClicked(evt);
            }
        });
        getContentPane().add(barangay, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 290, 40));

        prov.setEditable(false);
        prov.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        prov.setForeground(new java.awt.Color(153, 153, 153));
        prov.setText("Province");
        prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                provMouseClicked(evt);
            }
        });
        prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                provActionPerformed(evt);
            }
        });
        getContentPane().add(prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 290, 40));

        city.setEditable(false);
        city.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        city.setForeground(new java.awt.Color(153, 153, 153));
        city.setText("City");
        city.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cityMouseClicked(evt);
            }
        });
        city.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cityActionPerformed(evt);
            }
        });
        getContentPane().add(city, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 290, 40));

        region.setEditable(false);
        region.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        region.setForeground(new java.awt.Color(153, 153, 153));
        region.setText("Region");
        region.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regionMouseClicked(evt);
            }
        });
        region.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regionActionPerformed(evt);
            }
        });
        getContentPane().add(region, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 290, 40));

        fast.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        fast.setText("FAST SHIPPING (P 20.00)");
        fast.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fastMouseClicked(evt);
            }
        });
        fast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastActionPerformed(evt);
            }
        });
        getContentPane().add(fast, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 340, -1, -1));

        ultra.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        ultra.setText("ULTRA SHIPPING (P 50.00)");
        ultra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ultraActionPerformed(evt);
            }
        });
        getContentPane().add(ultra, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 370, -1, -1));

        mega.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        mega.setText("MEGA SHIPPING (P 80.00)");
        mega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                megaActionPerformed(evt);
            }
        });
        getContentPane().add(mega, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 400, -1, -1));

        norma.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        norma.setSelected(true);
        norma.setText("NORMAL SHIPPING");
        norma.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                normaMouseClicked(evt);
            }
        });
        getContentPane().add(norma, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, -1, -1));

        ppc.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        ppc.setText("PRODUCT CARE (P 20.00)");
        ppc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ppcMouseClicked(evt);
            }
        });
        ppc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ppcKeyPressed(evt);
            }
        });
        getContentPane().add(ppc, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 430, 280, -1));

        warranty.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        warranty.setText("WARRANTY (P 100.00)");
        warranty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                warrantyMouseClicked(evt);
            }
        });
        getContentPane().add(warranty, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 460, 250, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked

        if(validateInputs() == true){
            UpdateInputs();
        }else{
            return;
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel1.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel1.setBackground(new Color(204,204,204));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseExited

    private void specMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_specMouseClicked
        animation ani = new animation();

        ani.addPlaceholderTextArea(spec, "Specific Info . . .");        // TODO add your handling code here:
    }//GEN-LAST:event_specMouseClicked

    private void comboCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCountryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCountryActionPerformed

    private void zipCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zipCodeMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(zipCode, "Zip Code");        // TODO add your handling code here:
    }//GEN-LAST:event_zipCodeMouseClicked

    private void name1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_name1MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(name1, "Recievers Name");        // TODO add your handling code here:
    }//GEN-LAST:event_name1MouseClicked

    private void name1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_name1KeyTyped
        if(!name1.getText().isEmpty() || !name1.getText().equals("Recievers Name")){
            comboCountry.setEditable(true);
        }       // TODO add your handling code here:
    }//GEN-LAST:event_name1KeyTyped

    private void barangayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barangayMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(barangay, "Barangay");        // TODO add your handling code here:
    }//GEN-LAST:event_barangayMouseClicked

    private void provMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_provMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(prov, "Province");// TODO add your handling code here:
    }//GEN-LAST:event_provMouseClicked

    private void provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_provActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_provActionPerformed

    private void cityMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cityMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(city, "City");        // TODO add your handling code here:
    }//GEN-LAST:event_cityMouseClicked

    private void cityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cityActionPerformed

    private void regionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regionMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(region, "Region");        // TODO add your handling code here:
    }//GEN-LAST:event_regionMouseClicked

    private void regionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_regionActionPerformed

    private void fastMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fastMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_fastMouseClicked

    private void fastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastActionPerformed
        updateTotal();// TODO add your handling code here:
    }//GEN-LAST:event_fastActionPerformed

    private void ultraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ultraActionPerformed
        updateTotal();// TODO add your handling code here:
    }//GEN-LAST:event_ultraActionPerformed

    private void megaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_megaActionPerformed
        updateTotal();     // TODO add your handling code here:
    }//GEN-LAST:event_megaActionPerformed

    private void normaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_normaMouseClicked
        updateTotal();        // TODO add your handling code here:
    }//GEN-LAST:event_normaMouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseClicked

    private void ppcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ppcKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppcKeyPressed

    private void ppcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ppcMouseClicked
        updateTotal();         // TODO add your handling code here:
    }//GEN-LAST:event_ppcMouseClicked

    private void warrantyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_warrantyMouseClicked
        updateTotal();         // TODO add your handling code here:
    }//GEN-LAST:event_warrantyMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ShowDue;
    private javax.swing.JTextField barangay;
    private javax.swing.JTextField city;
    private javax.swing.JComboBox<String> comboCountry;
    private javax.swing.JLabel days;
    private javax.swing.JLabel due;
    private javax.swing.JRadioButton fast;
    private javax.swing.JLabel fee;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel label;
    private javax.swing.JRadioButton mega;
    private javax.swing.JTextField name1;
    private javax.swing.JRadioButton norma;
    private javax.swing.JCheckBox ppc;
    private javax.swing.JTextField prov;
    private javax.swing.JTextField region;
    private javax.swing.JTextArea spec;
    private javax.swing.JRadioButton ultra;
    private javax.swing.JCheckBox warranty;
    private javax.swing.JTextField zipCode;
    // End of variables declaration//GEN-END:variables
}
