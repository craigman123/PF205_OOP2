/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Market;

import Profiles.session;
import User.User_config;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author user
 */
public final class Payment extends javax.swing.JInternalFrame {

    /**
     * Creates new form Payment
     */
    private final int ProdId;
    private final JDesktopPane panel;
    private final String Address;
    private final String quan;
    private final String specInfo;
    private final int shipFee;
    private final float Total;
    private String extra[];
    private GeoPosition location;

    Border grayBorder = BorderFactory.createLineBorder(Color.GRAY, 2);

    public Payment(int id, JDesktopPane pane, String address, String quantity, String SpecificInfo, int shippingFee, float totalPay, String additionals[],
            GeoPosition selectedLocation) {
        
        this.location = selectedLocation;
        this.ProdId = id;
        this.panel = pane;
        this.Address = address;
        this.quan = quantity;
        this.specInfo = SpecificInfo;
        this.shipFee = shippingFee;
        this.Total = totalPay;
        this.extra = additionals;

        initComponents();
        StyleFrame();
        SwingUtilities.invokeLater(() -> {
            ToggleAPayBtn();
        });
        GroupBtn();
        CenterFrame();
        DisplayDueandLocation();
    }

    private float[] values;

    public void DisplayDueandLocation() {

        StringBuilder location = new StringBuilder();
        config conf = new config();

        String qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, ProdId);

        if (!result.isEmpty()) {

            java.util.Map<String, Object> prod = result.get(0);

            String name = prod.get("prod_name").toString();
            String cat = prod.get("prod_category").toString();
            String cash = prod.get("prod_price").toString();
            String stat = prod.get("prod_status").toString();
            String seller = prod.get("prod_addedBy").toString();

            location.append("<html><div style='font-family:Segoe UI; font-size:14px; line-height:1.6;'>");
            location.append("<h2 style='margin-bottom:8px; font-weight:bold;'>📍 DELIVERY LOCATION</h2>");
            location.append("<hr>");
            location.append("<b style='font-size:13px;'>ADDRESS:</b><br>");
            location.append("<div style='font-weight:bold; color:#222;'>")
                    .append(Address)
                    .append("</div><br>");
            location.append("<b style='font-size:13px;'>SPECIFIC LOCATION:</b><br>");
            location.append("<div style='font-weight:bold; color:#222;'>")
                    .append(specInfo)
                    .append("</div>");
            location.append("</div></html>");
            
            address.setContentType("text/html");
            address.setText(location.toString());

            // ================= PRODUCT INFO =================
            StringBuilder due = new StringBuilder();
            String statusColor = stat.equalsIgnoreCase("Active") ? "#0a8f08" : "#c40000";
            due.append("<html><div style='font-family:Segoe UI; font-size:14px; line-height:1.6;'>");
            due.append("<h2 style='font-weight:bold;'>🛒 PRODUCT SUMMARY</h2><hr>");
            due.append("<b>NAME:</b> <span style='font-weight:bold;'>").append(name).append("</span><br>");
            due.append("<b>CATEGORY:</b> <span style='font-weight:bold;'>").append(cat).append("</span><br>");
            due.append("<b>STATUS:</b> ")
               .append("<span style='font-weight:bold; color:")
               .append(statusColor)
               .append(";'>")
               .append(stat)
               .append("</span><br>");
            due.append("<b>SELLER:</b> <span style='font-weight:bold;'>").append(seller).append("</span><br><br>");
            due.append("<h3 style='font-weight:bold;'>💳 PAYMENT INFORMATION</h3><hr>");
            due.append("<table style='width:100%; font-size:13px; font-weight:bold;'>");
            due.append("<tr><td>PRODUCT PRICE:</td><td style='color:green;'>₱")
               .append(cash).append("</td></tr>");
            due.append("<tr><td>QUANTITY:</td><td>")
               .append(quan).append("</td></tr>");
            due.append("<tr><td>SHIPPING FEE:</td><td style='color:blue;'>₱")
               .append(shipFee).append("</td></tr>");
            due.append("</table>");
            due.append("</div></html>");
            
            payInfo.setContentType("text/html");
            payInfo.setText(due.toString());

            // ================= CALCULATIONS =================
            float installmentPrice = 0;

            if (one.isSelected()) {
                installmentPrice = values[0];
            } else if (two.isSelected()) {
                installmentPrice = values[1];
            } else if (three.isSelected()) {
                installmentPrice = values[2];
            } else if (four.isSelected()) {
                installmentPrice = values[3];
            } else if (five.isSelected()) {
                installmentPrice = values[4];
            } else if (six.isSelected()) {
                installmentPrice = values[5];
            }

            int quantity = Integer.parseInt(quan);

            float installmentTotal = installmentPrice * quantity + shipFee;

            // ================= DISPLAY LOGIC =================
            if (AP.isSelected()) {
                // AP → show Due Now (installment total)
                DueNow.setText("P " + String.format("%.2f", installmentTotal));
            } else if (COD.isSelected() || OP.isSelected()) {
                // COD or OP → show full total
                DueNow.setText("P " + String.format("%.2f", Total));
            }

            // Always show full total below
            FullDue.setText("P " + String.format("%.2f", Total));
        }
    }

    public void CenterFrame() {
        this.pack();
        Dimension desktopSize = panel.getSize();
        Dimension frameSize = this.getSize();

        System.out.println("D: " + desktopSize + ",F: " + frameSize);

        int x = (desktopSize.width - frameSize.width) / 2;
        int y = (desktopSize.height - frameSize.height) / 2;

        this.setLocation(x, y);

        if (this.getParent() == null) {
            panel.add(this);
        }

        this.setVisible(true);
    }

    public void OpenCardField() {

        boolean codSelected = COD.isSelected();
        boolean opSelected = OP.isSelected();
        boolean apSelected = AP.isSelected();

        // ================= COD SELECTED =================
        if (codSelected) {

            // Disable all banks
            landBank.setEnabled(false);
            BDO.setEnabled(false);
            BPI.setEnabled(false);
            Gcash.setEnabled(false);
            UnionBank.setEnabled(false);
            metroBank.setEnabled(false);

            // Disable installment options
            one.setEnabled(false);
            two.setEnabled(false);
            three.setEnabled(false);
            four.setEnabled(false);
            five.setEnabled(false);
            six.setEnabled(false);

            // Disable card number
            cardNum.setEnabled(false);
            cardNum.setText("Card Number");
            cardNum.setForeground(new Color(153, 153, 153));
            
            pin.setEnabled(false);

        } // ================= ONLINE PAYMENT SELECTED =================
        else if (apSelected) {

            // Enable banks
            landBank.setEnabled(true);
            BDO.setEnabled(true);
            BPI.setEnabled(true);
            Gcash.setEnabled(true);
            UnionBank.setEnabled(true);
            metroBank.setEnabled(true);

            // Enable installment options
            one.setEnabled(true);
            two.setEnabled(true);
            three.setEnabled(true);
            four.setEnabled(true);
            five.setEnabled(true);
            six.setEnabled(true);

            // Enable card number
            cardNum.setEnabled(true);
            cardNum.setText("");
            cardNum.setForeground(Color.BLACK);
            
            pin.setEnabled(true);
        } // ================= AP SELECTED =================
        else if (opSelected) {

            // Example: Enable banks but disable installments
            landBank.setEnabled(true);
            BDO.setEnabled(true);
            BPI.setEnabled(true);
            Gcash.setEnabled(true);
            UnionBank.setEnabled(true);
            metroBank.setEnabled(true);

            one.setEnabled(false);
            two.setEnabled(false);
            three.setEnabled(false);
            four.setEnabled(false);
            five.setEnabled(false);
            six.setEnabled(false);

            cardNum.setEnabled(true);
            cardNum.setText("");
            cardNum.setForeground(Color.BLACK);
            
            pin.setEnabled(true);
        }
    }

    public final void GroupBtn() {
        ButtonGroup groupBank = new ButtonGroup();
        ButtonGroup groupAPay = new ButtonGroup();
        ButtonGroup groupPayMethod = new ButtonGroup();

        groupBank.add(landBank);
        groupBank.add(BDO);
        groupBank.add(Gcash);
        groupBank.add(UnionBank);
        groupBank.add(metroBank);
        groupBank.add(BPI);

        groupAPay.add(OP);
        groupAPay.add(AP);
        groupAPay.add(COD);

        groupPayMethod.add(one);
        groupPayMethod.add(two);
        groupPayMethod.add(three);
        groupPayMethod.add(four);
        groupPayMethod.add(five);
        groupPayMethod.add(six);
    }

    public void ToggleAPayBtn() {
        one.setText("2 m x Install");
        two.setText("3 m x Install");
        three.setText("4 m x Install");
        four.setText("5 m x Install");
        five.setText("6 m x Install");
        six.setText("7 m x Install");

        one.setEnabled(false);
        two.setEnabled(false);
        three.setEnabled(false);
        four.setEnabled(false);
        five.setEnabled(false);
        six.setEnabled(false);
    }

    public final void setAPayLater() {
        config conf = new config();

        String qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, ProdId);

        if (!result.isEmpty()) {
            java.util.Map<String, Object> prod = result.get(0);
            String price = prod.get("prod_price").toString();
            System.out.println(price);

            int pr = Integer.parseInt(price);

            float value1 = pr / 2;
            float value2 = pr / 3;
            float value3 = pr / 4;
            float value4 = pr / 5;
            float value5 = pr / 6;
            float value6 = pr / 7;

            this.values = new float[]{value1, value2, value3, value4, value5, value6};

            one.setText(String.format("2 m x %.2f", value1));
            two.setText(String.format("3 m x %.2f", value2));
            three.setText(String.format("4 m x %.2f", value3));
            four.setText(String.format("5 m x %.2f", value4));
            five.setText(String.format("6 m x %.2f", value5));
            six.setText(String.format("7 m x %.2f", value6));
        }
    }

    public final void StyleFrame() {
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui
                = (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }

    public boolean validation() {
        char[] PIN = pin.getPassword();
        String password = new String(PIN);
        
        // Check AP/OP/COD selection
        if (!AP.isSelected() && !OP.isSelected() && !COD.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a payment method.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check installments if AP is selected
        if (AP.isSelected()) {
            if (!one.isSelected() && !two.isSelected() && !three.isSelected()
                    && !four.isSelected() && !five.isSelected() && !six.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please select an installment plan for APay.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // Check bank selection if not COD
        if (!COD.isSelected() && !landBank.isSelected() && !BDO.isSelected()
                && !BPI.isSelected() && !UnionBank.isSelected() && !metroBank.isSelected() && !Gcash.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a bank or digital wallet.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check card number if COD is not selected
        if (!COD.isSelected()) {
            String cardText = cardNum.getText().trim();
            if (cardText.isEmpty() || cardText.equalsIgnoreCase("Card Number")) {
                JOptionPane.showMessageDialog(this, "Please enter your card number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            // Optional: check numeric card number
            try {
                Long.parseLong(cardText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Card number must be numeric.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        if(AP.isSelected() || OP.isSelected()){
            if(password.length() == 0){
                JOptionPane.showMessageDialog(this, "Please Enter your car PIN.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true; // All checks passed
    }

    public void SendInputs() {
        config conf = new config();
        session see = new session();

        String qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, ProdId);

        java.util.Map<String, Object> prod = result.get(0);
        String name = prod.get("prod_name").toString();

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        String paymentMethod = "", card = "";

        if (COD.isSelected()) {
            paymentMethod = "Cash on Delivery";
            card = "none";
        } else if (OP.isSelected()) {
            paymentMethod = "Online Pay";
            card = cardNum.getText();
        } else if (AP.isSelected()) {
            paymentMethod = "Apay-Later";
            card = cardNum.getText();
        }
        
        String locationString = location.getLatitude() + ", " + location.getLongitude();

        qry = "INSERT INTO userOrders(user_id, prod_id, order_status, order_prod, order_quantity, order_totalPrice,"
                + " order_shippingAddress, order_paymentMethod, order_date, order_additionals, order_visibility, exact_location) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        int orderID = conf.addRecordAndReturnId(qry, see.GetID(), ProdId, "Pending", name, quan, Total, Address, paymentMethod, formattedNow,
                Arrays.toString(extra), "true", locationString);

        qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> prodResult = conf.fetchRecords(qry, ProdId);

        if (!prodResult.isEmpty()) {
            java.util.Map<String, Object> user = prodResult.get(0);
            int prodGetStock = ((Number) user.get("prod_stock")).intValue();

            if (Integer.parseInt(quan) == prodGetStock) {
                qry = "UPDATE products SET prod_stock = ?, prod_status = ? WHERE prod_id = ?";
                conf.updateRecord(qry, 0, "Sold Out", ProdId);

            } else {
                int calcStock = prodGetStock - Integer.parseInt(quan);
                qry = "UPDATE products SET prod_stock = ? WHERE prod_id = ?";
                conf.updateRecord(qry, calcStock, ProdId);
            }
        }

        qry = "INSERT INTO logs(user_id, prod_id, dateTime, log_action) VALUES(?,?,?,?) ";
        conf.addRecordAndReturnId(qry, see.GetID(), ProdId, formattedNow, "Bought A Product");

        String queryNow = "INSERT INTO notification(prod_id, user_id, n_content, date, read) VALUES (?,?,?,?,?)";
        conf.addRecordAndReturnId(queryNow, ProdId, see.GetID(), "Purchased Product:", formattedNow, false);

        qry = "INSERT INTO creditCards(user_id, pay_method, card_num) VALUES(?,?,?)";
        conf.addRecordAndReturnId(qry, see.GetID(), paymentMethod, card);

        SuccesMessage mes = new SuccesMessage(orderID, panel);
        panel.add(mes).setVisible(true);
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
        COD = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        DueNow = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        FullDue = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        OP = new javax.swing.JRadioButton();
        AP = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        six = new javax.swing.JRadioButton();
        five = new javax.swing.JRadioButton();
        three = new javax.swing.JRadioButton();
        four = new javax.swing.JRadioButton();
        two = new javax.swing.JRadioButton();
        one = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        landBank = new javax.swing.JRadioButton();
        Gcash = new javax.swing.JRadioButton();
        metroBank = new javax.swing.JRadioButton();
        BDO = new javax.swing.JRadioButton();
        UnionBank = new javax.swing.JRadioButton();
        BPI = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        cardNum = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        pin = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        payInfo = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        address = new javax.swing.JTextPane();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        COD.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        COD.setText("Cash-on-Delivery (COD)");
        COD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CODMouseClicked(evt);
            }
        });
        COD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CODActionPerformed(evt);
            }
        });
        jPanel1.add(COD, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, -1, -1));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel1.setText("PAYMENT SECTION");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(369, 369, 369)
                .addComponent(jLabel1)
                .addContainerGap(381, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(31, 31, 31))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -20, 960, 100));

        DueNow.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        DueNow.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        DueNow.setText("A-Pay Later");
        jPanel1.add(DueNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 500, 170, 43));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setText("TOTAL:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 540, 80, 30));

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel10.setText("PAYMENT METHOD");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, -1, -1));

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CANCEL");
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
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 270, 56));

        jLabel7.setBackground(new java.awt.Color(204, 204, 204));
        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("PLACE ORDER");
        jLabel7.setOpaque(true);
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
        });
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 580, 190, 56));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 90, 10, 550));

        jLabel16.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));
        jPanel1.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 290, 10));

        FullDue.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        FullDue.setText("DUE NOW");
        jPanel1.add(FullDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 540, 200, 30));

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel12.setText("AMOUNT DUE : ");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 510, 130, -1));
        jPanel1.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, 310, 10));

        OP.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        OP.setText("Online Pay");
        OP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OPMouseClicked(evt);
            }
        });
        OP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OPActionPerformed(evt);
            }
        });
        jPanel1.add(OP, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        AP.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        AP.setText("A-payLater");
        AP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                APMouseClicked(evt);
            }
        });
        AP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                APActionPerformed(evt);
            }
        });
        jPanel1.add(AP, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jLabel11.setText("PAYMENT SECURITY - SSL ENCRYPTED");

        six.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        six.setText("7 m x Install");
        six.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sixMouseClicked(evt);
            }
        });

        five.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        five.setText("6 m x Install");
        five.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fiveMouseClicked(evt);
            }
        });

        three.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        three.setText("4 m x Install");
        three.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                threeMouseClicked(evt);
            }
        });

        four.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        four.setText("5 m x Install");
        four.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fourMouseClicked(evt);
            }
        });

        two.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        two.setText("3 m x Install");
        two.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                twoMouseClicked(evt);
            }
        });
        two.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twoActionPerformed(evt);
            }
        });

        one.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        one.setText("2 m x Install");
        one.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                oneMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(one)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                            .addComponent(two))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(three)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(four))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(five)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(six)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(one)
                    .addComponent(two))
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(three)
                    .addComponent(four))
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(five)
                    .addComponent(six))
                .addGap(31, 31, 31)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 270, 180));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        landBank.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        landBank.setText("LandBank");

        Gcash.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        Gcash.setText("GCash");

        metroBank.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        metroBank.setText("Metro Bank");

        BDO.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        BDO.setText("BDO");

        UnionBank.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        UnionBank.setText("Union Bank");

        BPI.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        BPI.setText("BPI");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Gcash)
                    .addComponent(landBank)
                    .addComponent(metroBank))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BPI)
                    .addComponent(BDO)
                    .addComponent(UnionBank))
                .addGap(343, 343, 343))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(landBank)
                    .addComponent(BDO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Gcash)
                    .addComponent(UnionBank))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(metroBank)
                    .addComponent(BPI))
                .addGap(177, 177, 177))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 270, 100));

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel6.setText("PIN:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 230, -1, -1));

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, 10, 510));

        cardNum.setBackground(new java.awt.Color(204, 204, 204));
        cardNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cardNumKeyTyped(evt);
            }
        });
        jPanel1.add(cardNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 160, 280, 50));

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel9.setText("Card Number:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, -1, -1));
        jPanel1.add(pin, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 260, 280, 50));

        payInfo.setEditable(false);
        jScrollPane1.setViewportView(payInfo);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 90, 310, 410));

        address.setEditable(false);
        jScrollPane2.setViewportView(address);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, 300, 310));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void APActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_APActionPerformed
        if (AP.isSelected()) {
            setAPayLater();
            one.setEnabled(true);
            two.setEnabled(true);
            three.setEnabled(true);
            four.setEnabled(true);
            five.setEnabled(true);
            six.setEnabled(true);
        } else {
            ToggleAPayBtn();

        }// TODO add your handling code here:
    }//GEN-LAST:event_APActionPerformed

    private void twoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_twoActionPerformed

    private void OPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OPActionPerformed
        if (OP.isSelected()) {
            one.setEnabled(false);
            two.setEnabled(false);
            three.setEnabled(false);
            four.setEnabled(false);
            five.setEnabled(false);
            six.setEnabled(false);
        }// TODO add your handling code here:
    }//GEN-LAST:event_OPActionPerformed

    private void CODMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CODMouseClicked
        OpenCardField();
        DisplayDueandLocation();// TODO add your handling code here:
    }//GEN-LAST:event_CODMouseClicked

    private void oneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oneMouseClicked
        DisplayDueandLocation();        // TODO add your handling code here:
    }//GEN-LAST:event_oneMouseClicked

    private void twoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_twoMouseClicked
        DisplayDueandLocation();        // TODO add your handling code here:
    }//GEN-LAST:event_twoMouseClicked

    private void threeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_threeMouseClicked
        DisplayDueandLocation();         // TODO add your handling code here:
    }//GEN-LAST:event_threeMouseClicked

    private void fourMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fourMouseClicked
        DisplayDueandLocation();         // TODO add your handling code here:
    }//GEN-LAST:event_fourMouseClicked

    private void fiveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fiveMouseClicked
        DisplayDueandLocation();         // TODO add your handling code here:
    }//GEN-LAST:event_fiveMouseClicked

    private void sixMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sixMouseClicked
        DisplayDueandLocation();         // TODO add your handling code here:
    }//GEN-LAST:event_sixMouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        
        if (validation()) {
            SendInputs();
        }// TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseClicked

    private void CODActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CODActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CODActionPerformed

    private void OPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OPMouseClicked
        OpenCardField();
        DisplayDueandLocation();// TODO add your handling code here:
    }//GEN-LAST:event_OPMouseClicked

    private void APMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_APMouseClicked
        OpenCardField();
        DisplayDueandLocation();// TODO add your handling code here:
    }//GEN-LAST:event_APMouseClicked

    private void cardNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardNumKeyTyped
        char c = evt.getKeyChar();
        String text = cardNum.getText();

        // If not a digit OR length >= 16, ignore the input
        if (!Character.isDigit(c) || text.length() >= 16) {
            evt.consume();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cardNumKeyTyped

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jLabel7,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jLabel7,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );          // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        animation ani = new animation();
        
        ani.transitionBackground(
            jLabel4,
            Color.decode("#CCCCCC"),
            Color.decode("#B3B3B3"),
            200
        );                  // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        animation ani = new animation();
        
        ani.transitionBackground(
            jLabel4,
            Color.decode("#B3B3B3"),
            Color.decode("#CCCCCC"),
            200
        );        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseExited

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        int choice = JOptionPane.showConfirmDialog(
            null,
            "Do you want to cancel the order?",
            "Order Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            System.out.println("Order cancelled");
        } else {
            
            System.out.println("Order retained");
        }       
    }//GEN-LAST:event_jLabel4MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton AP;
    private javax.swing.JRadioButton BDO;
    private javax.swing.JRadioButton BPI;
    private javax.swing.JRadioButton COD;
    private javax.swing.JLabel DueNow;
    private javax.swing.JLabel FullDue;
    private javax.swing.JRadioButton Gcash;
    private javax.swing.JRadioButton OP;
    private javax.swing.JRadioButton UnionBank;
    private javax.swing.JTextPane address;
    private javax.swing.JTextField cardNum;
    private javax.swing.JRadioButton five;
    private javax.swing.JRadioButton four;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JRadioButton landBank;
    private javax.swing.JRadioButton metroBank;
    private javax.swing.JRadioButton one;
    private javax.swing.JTextPane payInfo;
    private javax.swing.JPasswordField pin;
    private javax.swing.JRadioButton six;
    private javax.swing.JRadioButton three;
    private javax.swing.JRadioButton two;
    // End of variables declaration//GEN-END:variables
}
