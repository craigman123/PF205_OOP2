/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Market;

import Profiles.session;
import configuration.config;
import java.awt.Color;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author user
 */
public class Payment extends javax.swing.JInternalFrame {

    /**
     * Creates new form Payment
     */
    private final int ProdId;
    private final JDesktopPane panel;
    private final String Address;
    private final String quan;
    private final String specInfo;
    private final int shipFee;
    private final String nm;
    
    public Payment(int id, JDesktopPane pane, String address, String quantity, String SpecificInfo, int shippingFee, String name) {
        this.ProdId = id;
        this.panel = pane;
        this.Address = address;
        this.quan = quantity;
        this.specInfo = SpecificInfo;
        this.shipFee = shippingFee;
        this.nm = name;
        
        initComponents();
        StyleFrame();
        SwingUtilities.invokeLater(()-> { 
            ToggleAPayBtn();
            CardLogic();
        });
        GroupBtn();
        ShowDue();
        
    }
    
    public void CardLogic(){
        try {
            javax.swing.text.MaskFormatter mask = 
                new javax.swing.text.MaskFormatter("#### #### #### ####");

            mask.setPlaceholderCharacter('_');
            cardNum.setFormatterFactory(
                new javax.swing.text.DefaultFormatterFactory(mask)
            );
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
    }
    
    private int[] values;
    
    public final void ShowDue(){
        StringBuilder message = new StringBuilder("<html>");
        config conf = new config();
        
        int res = 0, promo = 0;
        
        String qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, ProdId); 
        System.out.println(ProdId);
        
        if(!result.isEmpty()){
            java.util.Map<String, Object> prod = result.get(0);
            String pr = prod.get("prod_price").toString();
            String prodName = prod.get("prod_name").toString();
            String cat = prod.get("prod_category").toString();
            String stat = prod.get("prod_status").toString();
            
            int price = Integer.parseInt(pr);
            label.setText(quan);

            message.append("Shipping fee: ").append(shipFee).append("<br>");
            message.append("Product Price: ").append(price).append("<br>");
            message.append("Quantity: ").append(quan).append("<br>");

            res = Integer.parseInt(quan) * price;

            message.append("Price Total: ").append(res).append(".00<br>");
            message.append("Promo: ").append(promo).append("<br>");
            message.append("<br>");
            message.append(prodName).append("<br>");
            message.append(cat).append("<br>");
            message.append(stat).append("<br>");
            message.append("</html>");

             showDue.setText(message.toString());

             int total = shipFee + res + promo;
             System.out.println(total);
             due.setText("P "+ String.valueOf(total) +".00");
             duenow.setText("P "+ String.valueOf(total) +".00");
        
        
            int value = 0;

            if(AP.isSelected()){
                if(one.isSelected()){
                     value = values[0];
                }else if (two.isSelected()){
                     value = values[1];
                }else if(three.isSelected()){
                     value = values[2];
                }else if(four.isSelected()){
                     value = values[3];
                }else if(five.isSelected()){
                     value = values[4];
                }else if(six.isSelected()){
                     value = values[5];
                }else if(OP.isSelected()){
                    value = 0;
                }

                int total2 = shipFee + value + promo;
                System.out.println(total2);
                duenow.setText("P " + String.valueOf(total2) + ".00");
            }
        }
    }
    
    public final void GroupBtn(){
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
    
    public void ToggleAPayBtn(){
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
    
    public final void setAPayLater(){
        config conf = new config();
        
        String qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, ProdId); 
        
        if(!result.isEmpty()){
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
            
            int[] list = {(int)value1, (int)value2, (int)value3, (int)value4, (int)value5, (int)value6};
            
            one.setText(String.format("2 m x %.2f", value1));
            two.setText(String.format("3 m x %.2f", value2));
            three.setText(String.format("4 m x %.2f", value3));
            four.setText(String.format("5 m x %.2f", value4));
            five.setText(String.format("6 m x %.2f", value5));
            six.setText(String.format("7 m x %.2f", value6));
            
            values = list;
        }
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public boolean validateInputs() {
        StringBuilder message = new StringBuilder();
        int finalCode = 0;
        
        String card = cardNum.getText();
        
        if(card.isEmpty() || card.equals("Card Number")){
            message.append("• Please enter a credit card number.\n");
        }

        if (!OP.isSelected() || !AP.isSelected() || !COD.isSelected()) {
            message.append("• Please select a payment method.\n");
        }

        if (OP.isSelected()) {
            if(!landBank.isSelected() || !BDO.isSelected() || !BPI.isSelected() || !Gcash.isSelected() || !UnionBank.isSelected() || !metroBank.isSelected()){
                message.append("• Please select a banking payment.\n");
            }
        }
        
        if(AP.isSelected()){
            if(!landBank.isSelected() || !BDO.isSelected() || !BPI.isSelected() || !Gcash.isSelected() || !UnionBank.isSelected() || !metroBank.isSelected()){
                message.append("• Please select a banking payment.\n");
            }
            
            if(!one.isSelected() || !two.isSelected() || !three.isSelected() || !four.isSelected() || !five.isSelected() || !six.isSelected()){
                message.append("• Please select an Installment.\n");
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
    
    public void GetInputs(){
        OrderInfo info = new OrderInfo();
        config conf = new config();
        session see = new session();
        this.dispose();
        panel.add(info).setVisible(true);
        int value = 0;
        String payMethod = "";
        String APay = null;
        
        if(AP.isSelected()){
            payMethod = "APayLater";
            if(one.isSelected()){
                 APay = "2 Months / Installments";
            }else if (two.isSelected()){
                 APay = "3 Months / Installments";
            }else if(three.isSelected()){
                 APay = "4 Months / Installments";
            }else if(four.isSelected()){
                 APay = "5 Months / Installments";
            }else if(five.isSelected()){
                APay = "6 Months / Installments";
            }else if(six.isSelected()){
                 APay = "7 Months / Installments";
            }else if(OP.isSelected()){
                payMethod = "Online Banking";
            }
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        String query = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(query, ProdId); 
        
        java.util.Map<String, Object> prod = result.get(0);
        String prodName = prod.get("prod_name").toString();
        
        String qry = "INSERT INTO userOrders(user_id, prod_id, order_status, order_prod, order_quantity, order_totalPrice, order_shippingAddress, "
                + "order_paymentMethod, order_date) VALUES(?,?,?,?,?,?,?,?,?)";
        int id = conf.addRecordAndReturnId(qry, see.GetID(), ProdId, "Pending", prodName, quan, due.getText(), Address, payMethod + APay, now);
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
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        due = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        showDue = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        landBank = new javax.swing.JRadioButton();
        Gcash = new javax.swing.JRadioButton();
        metroBank = new javax.swing.JRadioButton();
        BPI = new javax.swing.JRadioButton();
        UnionBank = new javax.swing.JRadioButton();
        BDO = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        cardNum = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        duenow = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        label = new javax.swing.JLabel();
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

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        COD.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        COD.setText("Cash-on-Delivery (COD)");
        COD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CODMouseClicked(evt);
            }
        });
        jPanel1.add(COD, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 350, -1, -1));

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, -1, -1));

        jFormattedTextField2.setEditable(false);
        jFormattedTextField2.setForeground(new java.awt.Color(153, 153, 153));
        jFormattedTextField2.setText("Code");
        jPanel1.add(jFormattedTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 160, 270, 51));

        jLabel15.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel15.setText("Promo Codes");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 130, -1, -1));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel1.setText("PAYMENT SECTION");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(362, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(378, 378, 378))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(31, 31, 31))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -20, 950, 100));

        due.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        due.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        due.setText("DUE");
        jPanel1.add(due, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 530, 190, 43));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setText("TOTAL:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 540, 80, -1));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        showDue.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        showDue.setText("Payment Info");
        showDue.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showDue, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(showDue, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 130, 290, 360));

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel9.setText("PAYMENT INFORMATION");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 90, -1, -1));

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel10.setText("PAYMENT TYPE");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, -1, -1));

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CANCEL");
        jLabel4.setOpaque(true);
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 580, 210, 56));

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel5.setText("QUANTITY:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 600, -1, -1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 92, 10, 480));

        landBank.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        landBank.setText("LandBank");
        jPanel1.add(landBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        Gcash.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        Gcash.setText("GCash");
        jPanel1.add(Gcash, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, -1, -1));

        metroBank.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        metroBank.setText("Metro Bank");
        jPanel1.add(metroBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        BPI.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        BPI.setText("BPI");
        jPanel1.add(BPI, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, -1, -1));

        UnionBank.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        UnionBank.setText("Union Bank");
        jPanel1.add(UnionBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, -1, -1));

        BDO.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        BDO.setText("BDO");
        jPanel1.add(BDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, -1, -1));

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
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 580, 190, 56));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, 10, 440));

        cardNum.setEditable(false);
        cardNum.setForeground(new java.awt.Color(153, 153, 153));
        cardNum.setText("Card Number");
        jPanel1.add(cardNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 270, 51));

        jLabel16.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));
        jPanel1.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 290, 10));

        duenow.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        duenow.setText("DUE NOW");
        jPanel1.add(duenow, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 500, 90, 20));

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel12.setText("AMOUNT DUE NOW: ");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 500, -1, -1));

        label.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        label.setText("qty");
        jPanel1.add(label, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 580, 40, 50));
        jPanel1.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, 310, 10));

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
        jPanel1.add(OP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

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
        jPanel1.add(AP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

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
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
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
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 270, 180));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void APActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_APActionPerformed
        if(AP.isSelected()){
            setAPayLater();
            one.setEnabled(true);
            two.setEnabled(true);
            three.setEnabled(true);
            four.setEnabled(true);
            five.setEnabled(true);
            six.setEnabled(true);
        }else{
            ToggleAPayBtn();
            
        }// TODO add your handling code here:
    }//GEN-LAST:event_APActionPerformed

    private void twoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_twoActionPerformed

    private void OPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OPActionPerformed
        if(OP.isSelected()){
            one.setEnabled(false);
            two.setEnabled(false);
            three.setEnabled(false);
            four.setEnabled(false);
            five.setEnabled(false);
            six.setEnabled(false);
        }// TODO add your handling code here:
    }//GEN-LAST:event_OPActionPerformed

    private void oneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oneMouseClicked
        ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_oneMouseClicked

    private void twoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_twoMouseClicked
         ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_twoMouseClicked

    private void threeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_threeMouseClicked
         ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_threeMouseClicked

    private void fourMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fourMouseClicked
         ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_fourMouseClicked

    private void fiveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fiveMouseClicked
         ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_fiveMouseClicked

    private void sixMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sixMouseClicked
         ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_sixMouseClicked

    private void OPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OPMouseClicked
        ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_OPMouseClicked

    private void APMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_APMouseClicked
        ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_APMouseClicked

    private void CODMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CODMouseClicked
        ShowDue();        // TODO add your handling code here:
    }//GEN-LAST:event_CODMouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        if(validateInputs()){
            GetInputs();   
        }// TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        jLabel7.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        jLabel7.setBackground(new Color(204,204,204));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton AP;
    private javax.swing.JRadioButton BDO;
    private javax.swing.JRadioButton BPI;
    private javax.swing.JRadioButton COD;
    private javax.swing.JRadioButton Gcash;
    private javax.swing.JRadioButton OP;
    private javax.swing.JRadioButton UnionBank;
    private javax.swing.JFormattedTextField cardNum;
    private javax.swing.JLabel due;
    private javax.swing.JLabel duenow;
    private javax.swing.JRadioButton five;
    private javax.swing.JRadioButton four;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel label;
    private javax.swing.JRadioButton landBank;
    private javax.swing.JRadioButton metroBank;
    private javax.swing.JRadioButton one;
    private javax.swing.JLabel showDue;
    private javax.swing.JRadioButton six;
    private javax.swing.JRadioButton three;
    private javax.swing.JRadioButton two;
    // End of variables declaration//GEN-END:variables
}
