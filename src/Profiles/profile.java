/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Profiles;

import User.Market.Product_Detail;
import User.User_Details;
import configuration.LogReg_config;
import configuration.animation;
import configuration.config;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import main.LoginRegister;
import java.util.*;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;

/**
 *
 * @author user
 */
public final class profile extends javax.swing.JInternalFrame {

    /**
     * Creates new form InterFrame1
     */
    private final int ID;
    private static String unique_pass;
    private JFrame fr;
    private JDesktopPane MainPane;
    
    public profile(int id, JFrame frame, JDesktopPane pane) {
        this.ID = id;
        this.fr = frame;
        this.MainPane = pane;
        
        initComponents();
        SetTextUser();
        SetTextDetails();
        StyleFrame();
        DisplayNewProducts();
    }
    
    public void DisplayNewProducts() {
    config conf = new config();

    String qry = "SELECT * FROM products";
    List<Map<String, Object>> newProd = conf.fetchRecords(qry);

    if (newProd.isEmpty()) {
        System.out.println("No new products found.");
        NewProductDisplayPanel.setText("No new Products");
        return;
    }

    StringBuilder buildMessage = new StringBuilder(
    "<html><body style='font-family:Trebuchet MS; font-size:18px; font-weight:bold;'>"
    );

    for (Map<String, Object> product : newProd) {
        int prodID = ((Number) product.get("prod_id")).intValue();
        String prodName = product.get("prod_name").toString();
        String cat = product.get("prod_category").toString();
        String status = product.get("prod_status").toString();
        String originalStatus = status;
        BigDecimal price = new BigDecimal(product.get("prod_price").toString());
        String formattedPrice = price.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        String getDate = product.get("date_added").toString();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateAdded = LocalDateTime.parse(getDate, formatter);
        
        LocalDateTime now = LocalDateTime.now();
        long daysDiff = ChronoUnit.DAYS.between(dateAdded, now);

        if (daysDiff > 5) {
            continue;
        }

        if (status.equals("Active")) status = "On Market";

        String color;
        switch (originalStatus) {
            case "Active": color="green"; break;
            case "Inactive": color="red"; break;
            case "Suspended": color="#945B00"; break;
            case "Pending": color="#00876F"; break;
            case "Sold Out": color="#571300"; break;
            case "Out of Stock": color="#5C5C5C"; break;
            case "Archived": color="#002085"; break;
            case "Discountinued": color="#B8B8B8"; break;
            default: color="gray";
        }

        buildMessage.append("New Product Added<br>")
            .append("A ").append(cat).append(" ").append(prodName).append("<br>")
            .append("Vat Price of P").append(formattedPrice).append("<br>")
            .append("Now <span style='color:").append(color).append(";'>")
            .append(status).append("</span>: Reference Number: ")
            .append("<i style='text-decoration:none;'><a href='product://").append(prodID).append("'>")
            .append(prodID).append("</a></i>")
            .append("<br>Added At: ").append(getDate);
    }

    buildMessage.append("</body></html>");

    JEditorPane editor = new JEditorPane("text/html", buildMessage.toString());
    editor.setEditable(false);
    editor.setOpaque(true);
    editor.setBackground(Color.WHITE);
    editor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    editor.addHyperlinkListener(e -> {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            int prodID = Integer.parseInt(e.getDescription().replace("product://",""));
            System.out.println("Clicked Product ID: " + prodID);
            Product_Detail detail = new Product_Detail(prodID, MainPane);
            MainPane.add(detail).setVisible(true);
            this.dispose();
        }
    });

    JScrollPane scrollPane = new JScrollPane(editor);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(500, 300));

    NewProductDisplayPanel.removeAll();
    NewProductDisplayPanel.setLayout(new BorderLayout());
    NewProductDisplayPanel.add(scrollPane, BorderLayout.CENTER);
    NewProductDisplayPanel.revalidate();
    NewProductDisplayPanel.repaint();
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void SetTextUser(){
        config conf = new config();
        
        String qry = "SELECT * FROM users WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> resultUser = conf.fetchRecords(qry, ID); 
        
        java.util.Map<String, Object> user = resultUser.get(0);
            String accessAccount = user.get("user_access").toString();
            String ussage1 = user.get("user_ussage").toString();
            String pass1 = user.get("user_hashpass").toString();
            int badge1 = ((Number) user.get("user_badge")).intValue();
            String name1 = user.get("user_name").toString();
            int id1 = ((Number) user.get("user_id")).intValue();
            
        userID.setText(String.valueOf(id1));
        Username.setText(name1);
        badge.setText(String.valueOf(badge1));
        unique_pass = pass1; 
        
        if(ussage1.equals("Enable")){
            acess.setText(ussage1);
            acess.setForeground(new Color(0,204,0));
        }else if (ussage1.equals("Disable")){
            acess.setText(ussage1);
            acess.setForeground(new Color(153,0,0));
        }
        
        if(accessAccount.equals("Admin")){
            labeledAs.setText(accessAccount);
            labeledAs.setForeground(Color.decode("#006AA3"));
        }else if(accessAccount.equals("Dispatcher")){
            labeledAs.setText(accessAccount);
            labeledAs.setForeground(Color.decode("#288700"));
        }else if(accessAccount.equals("User")){
            labeledAs.setText(accessAccount);
            labeledAs.setForeground(Color.decode("#BA8C00"));
        }
    }
    
    public void SetTextDetails(){
        config conf = new config();
        
        String query = "SELECT * FROM details WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(query, ID); 
        
        if(!result.isEmpty()){
                java.util.Map<String, Object> details = result.get(0);
                String username1 = details.get("user_name").toString();
                int age1 = ((Number) details.get("user_age")).intValue();
                String gender1 = details.get("user_gender").toString();
                String educ1 = details.get("user_education").toString();
                String country1 = details.get("user_country").toString();
                String birthdate1 = details.get("user_birthdate").toString();
                String email1 = details.get("user_email").toString();
                String phoneNumber1 = details.get("user_number").toString();
                int ValidID1 = ((Number) details.get("user_ValidId")).intValue();

            gender.setText(gender1);
            phonenumber.setText(phoneNumber1);
            ValidID.setText(String.valueOf(ValidID1));
            birthdate.setText(birthdate1);
            country.setText(country1);
            educational.setText(educ1);
            email.setText(email1);
            name.setText(username1);
            age.setText(String.valueOf(age1));
            
        }
    }
    
    public void RefreshFrame(){
        SetTextDetails();
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
        jLabel10 = new javax.swing.JLabel();
        country = new javax.swing.JTextField();
        userID = new javax.swing.JLabel();
        ValidID = new javax.swing.JTextField();
        gender = new javax.swing.JTextField();
        age = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        educational = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pass = new javax.swing.JLabel();
        Username = new javax.swing.JLabel();
        badge = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        phonenumber = new javax.swing.JTextField();
        name = new javax.swing.JTextField();
        birthdate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        scrollNewProducts = new javax.swing.JScrollPane();
        NewProductDisplayPanel = new javax.swing.JTextArea();
        acess = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        labeledAs = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel10.setText("Country:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, -1, -1));

        country.setEditable(false);
        country.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        country.setText("No Detail");
        country.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryActionPerformed(evt);
            }
        });
        jPanel1.add(country, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 250, 50));

        userID.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        userID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userID.setText("UserID");
        jPanel1.add(userID, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 590, 80, 30));

        ValidID.setEditable(false);
        ValidID.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        ValidID.setText("No Detail");
        ValidID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ValidIDActionPerformed(evt);
            }
        });
        jPanel1.add(ValidID, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 250, 50));

        gender.setEditable(false);
        gender.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        gender.setText("No Detail");
        gender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genderActionPerformed(evt);
            }
        });
        jPanel1.add(gender, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 110, 50));

        age.setEditable(false);
        age.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        age.setText("No Detail");
        age.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ageActionPerformed(evt);
            }
        });
        jPanel1.add(age, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, 110, 50));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel13.setText("Gender:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, -1, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel11.setText("Valid ID Number:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 150, -1));

        educational.setEditable(false);
        educational.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        educational.setText("No Detail");
        educational.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                educationalActionPerformed(evt);
            }
        });
        jPanel1.add(educational, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 250, 50));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel9.setText("Educational Attainment:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dyna_image/dafault_image.jpg"))); // NOI18N
        jLabel7.setText("jLabel7");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-50, 0, 186, 400));

        pass.setBackground(new java.awt.Color(255, 255, 255));
        pass.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        pass.setText("****************");
        pass.setOpaque(true);
        jPanel1.add(pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 230, 50));

        Username.setBackground(new java.awt.Color(255, 255, 255));
        Username.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        Username.setText("jLabel1");
        Username.setOpaque(true);
        jPanel1.add(Username, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 230, 50));

        badge.setBackground(new java.awt.Color(255, 255, 255));
        badge.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        badge.setText("jLabel1");
        badge.setOpaque(true);
        jPanel1.add(badge, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 230, 50));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setText("Password:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setText("Username:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, -1, -1));

        jLabel1.setBackground(new java.awt.Color(153, 153, 153));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("View");
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
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 50, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setText("Badge:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, -1, -1));

        email.setEditable(false);
        email.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        email.setText("No Detail");
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        jPanel1.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 250, 50));

        phonenumber.setEditable(false);
        phonenumber.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        phonenumber.setText("No Detail");
        phonenumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phonenumberActionPerformed(evt);
            }
        });
        jPanel1.add(phonenumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 340, 50));

        name.setEditable(false);
        name.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        name.setText("No Detail");
        name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameActionPerformed(evt);
            }
        });
        jPanel1.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 310, 340, 50));

        birthdate.setEditable(false);
        birthdate.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        birthdate.setText("No Detail");
        birthdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                birthdateActionPerformed(evt);
            }
        });
        jPanel1.add(birthdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 390, 340, 50));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Access:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 550, 70, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel12.setText("Age:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, -1, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel15.setText("Phone Number:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 210, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel16.setText("Account Name:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, -1, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel17.setText("Email:");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, -1, -1));

        jLabel18.setBackground(new java.awt.Color(204, 204, 204));
        jLabel18.setOpaque(true);
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 480, 190));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LOG OUT");
        jLabel2.setOpaque(true);
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 590, 120, 30));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("EDIT PROFILE");
        jLabel3.setOpaque(true);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 530, 120, 40));

        scrollNewProducts.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        NewProductDisplayPanel.setEditable(false);
        NewProductDisplayPanel.setColumns(20);
        NewProductDisplayPanel.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        NewProductDisplayPanel.setLineWrap(true);
        NewProductDisplayPanel.setRows(5);
        NewProductDisplayPanel.setText("New Product Display Here");
        scrollNewProducts.setViewportView(NewProductDisplayPanel);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollNewProducts, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollNewProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 290, 500));

        acess.setBackground(new java.awt.Color(204, 204, 204));
        acess.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        acess.setForeground(new java.awt.Color(153, 51, 0));
        acess.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        acess.setText("Access");
        acess.setOpaque(true);
        jPanel1.add(acess, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 480, 160, 50));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel19.setText("Birthdate:");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 370, -1, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Product Acquiration:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 460, 180, -1));

        labeledAs.setBackground(new java.awt.Color(204, 204, 204));
        labeledAs.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        labeledAs.setForeground(new java.awt.Color(153, 153, 153));
        labeledAs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labeledAs.setText("Labeled As:");
        labeledAs.setOpaque(true);
        jPanel1.add(labeledAs, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 570, 160, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 950, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void countryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_countryActionPerformed

    private void ValidIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ValidIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ValidIDActionPerformed

    private void genderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genderActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_genderActionPerformed

    private void ageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ageActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked

        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseClicked

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void phonenumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phonenumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phonenumberActionPerformed

    private void nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameActionPerformed

    private void birthdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_birthdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_birthdateActionPerformed

    private void educationalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_educationalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_educationalActionPerformed

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        jLabel1.setBackground(new Color(102,102,102));
        pass.setText(unique_pass); // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        jLabel1.setBackground(new Color(153,153,153));  
        pass.setText("****************"); // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseExited

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
               // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        config conf = new config();
        session see = new session();
        
        LoginRegister regis = new LoginRegister();
        regis.setVisible(true);
        this.dispose();
        fr.dispose();
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = now.format(formatter);
        
        String qry = "INSERT INTO logs(user_id, dateTime, log_action) VALUES(?,?,?)";
        conf.addRecordAndReturnId(qry, see.GetID(), formatted, "Logged Out");
                // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        int Badge = Integer.parseInt(badge.getText());
        
        User_Details detail = new User_Details(Username.getText(), Badge, "HASHED", true);
        detail.setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea NewProductDisplayPanel;
    private javax.swing.JLabel Username;
    private javax.swing.JTextField ValidID;
    private javax.swing.JLabel acess;
    private javax.swing.JTextField age;
    private javax.swing.JLabel badge;
    private javax.swing.JTextField birthdate;
    private javax.swing.JTextField country;
    private javax.swing.JTextField educational;
    private javax.swing.JTextField email;
    private javax.swing.JTextField gender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labeledAs;
    private javax.swing.JTextField name;
    private javax.swing.JLabel pass;
    private javax.swing.JTextField phonenumber;
    private javax.swing.JScrollPane scrollNewProducts;
    private javax.swing.JLabel userID;
    // End of variables declaration//GEN-END:variables
}
