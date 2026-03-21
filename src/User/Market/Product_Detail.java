/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Market;

import Profiles.session;
import User.Cart.Cart_config;
import User.User_config;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

/**
 *
 * @author user
 */
public final class Product_Detail extends javax.swing.JInternalFrame {

    /**
     * Creates new form Product_Detail
     */
    public Product_Detail() {
        initComponents();
    }
    
    private int ID;
    private JDesktopPane panel;
    
    public Product_Detail(int id, JDesktopPane pane) {
        this.ID = id;
        this.panel = pane;
        
        StyleFrame();
        initComponents();
        SwingUtilities.invokeLater(() -> {
            ShowComponent();
            SetStatusIndicator();
            CheckValidation();
            checkHeart();
            RenderOrderBtn();
            SetOrderButton();
        });
        StyleScrollPane();
    }
    
    public void RenderOrderBtn(){
        session see = new session();
        config conf = new config();
        
        String qry = "SELECT user_ussage FROM users WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, see.GetID()); 
        
        if(!result.isEmpty()){
            java.util.Map<String, Object> user = result.get(0);
            String access = user.get("user_ussage").toString();
            
            if(access.equals("Enable")){
                orderBtn.setBackground(new Color(204,204,204));
                orderBtn.setForeground(Color.BLACK);
            }
        }
    }
    
    public void checkHeart(){
        config conf = new config();
        session see = new session();
        
        String qry = "SELECT * FROM cart WHERE prod_id = ? AND user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, ID, see.GetID()); 
        System.out.println("ID: " + ID + "User ID: " + see.GetID());
        
        if(!result.isEmpty()){
            heartIcon(true, false);
            System.out.println("true");
        }else{
            heartIcon(false, false);
            System.out.println("false");
        }
    }
    
    public void heartIcon(boolean change, boolean clone) {
        session see = new session();
        config conf = new config();

        ImageIcon icon = null;
        int w = 0, h = 0;

        int userId = see.GetID();
        java.util.Date now = new java.util.Date();

        try {
            if (change) {
                icon = new ImageIcon(getClass().getResource("/images/cart-green.png"));
                w = heart.getWidth() - 10;
                h = heart.getHeight() - 10;
                
                if(clone){
                    String insertQry = "INSERT INTO cart(user_id, prod_id, cart_timeSaved) VALUES(?, ?, ?)";
                    conf.addRecordAndReturnId(insertQry, userId, ID, now);
                    
                    LocalDateTime nowDate = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = nowDate.format(formatter);
                    String queryNow = "INSERT INTO notification(user_id, n_content, date) VALUES (?, ?, ?)";
                    conf.addRecordAndReturnId(queryNow, see.GetID(), "Product Added to Cart ID: " + ID, formattedDate);
                }

            } else {
                icon = new ImageIcon(getClass().getResource("/images/cart-removebg-preview (1).png"));
                w = heart.getWidth() - 10;
                h = heart.getHeight() - 10;

                String deleteQry = "DELETE FROM cart WHERE user_id = ? AND prod_id = ?";
                conf.deleteRecord(deleteQry, userId, ID);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating favorites: " + ex.getMessage());
        }

        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        heart.setIcon(new ImageIcon(img));
        heart.setHorizontalAlignment(JLabel.CENTER);
        heart.setVerticalAlignment(JLabel.CENTER);
    }
    
    private boolean isFavorited = false;

    public void toggleHeart() {
        isFavorited = !isFavorited;
        heartIcon(isFavorited, true);
    }
    
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
    Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
    Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
    Border grayBorder = BorderFactory.createLineBorder(Color.GRAY, 2);
    Border darkredBorder = BorderFactory.createLineBorder(new Color(120, 0, 0), 2);
    Border lightredBorder = BorderFactory.createLineBorder(new Color(255, 120, 120), 2);
    Border yellowBorder = BorderFactory.createLineBorder(Color.YELLOW, 2);
    Border lightgrayBorder = BorderFactory.createLineBorder(new Color(180, 180, 180), 2);
    
    private boolean stockAllow;
    private boolean ussageAllow;
    private boolean statusAllow;
    
    public void SetOrderButton(){
        config conf = new config();
        session see = new session();
        
        String qry = "SELECT * FROM users WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, see.GetID());
        System.out.println("user: " + see.GetID());
        
        if(!result.isEmpty()){
            java.util.Map<String, Object> user = result.get(0);
            String ussage = user.get("user_ussage").toString();
            System.out.println(ussage);
            
            if(ussage.equals("Enable")){
                badgeLabel.setText("Badge Verified!");
                badgeLabel.setForeground(new Color(0,153,0));
                ussageAllow = true;
            } else if (ussage.equals("Disable")){
                badgeLabel.setText("Badge Unverified!");
                badgeLabel.setForeground(new Color(153,0,0));
                ussageAllow = false;
            }
        }
    }
    
    public String updateStockIndicator() {
        int stockQty = Integer.parseInt(GlobalStock);      
        String text = null;
        
        if (stockQty <= 0) {
            text = "Out of Stock";
            stockindicator.setBackground(Color.RED);
            stockAllow = false;

        } else if (stockQty <= 10 && stockQty >= 1) {
            text = "Very Low Stock: " + stockQty ;
            stockAllow = true;
            stockindicator.setBackground(Color.ORANGE);
            
        }else if(stockQty <= 20 && stockQty >= 11){
            text = "Low Stock: " + stockQty;
            stockAllow = true;
            stockindicator.setBackground(Color.YELLOW);
            
        } else {
            Onstock.setText(String.valueOf(stockQty));
            stockAllow = true;
            stockindicator.setBackground(Color.GREEN);
            
        }
        
        return text;
    }
    
    public void SetStatusIndicator() {
        String stat = status.getText();
        animation ani = new animation();

        switch (stat) {
            case "Active":
                statusindicator.setBackground(new Color(0, 180, 0));
                statusindicator.setBorder(greenBorder);
                statusAllow = true;
                break;

            case "Pending":
                statusindicator.setBackground(Color.YELLOW);
                statusindicator.setBorder(yellowBorder);
                statusAllow = false;
                ani.startBlinking(statusindicator);
                break;

            case "Sold Out":
            case "Out of Stock":
            case "Discontinued":
                statusindicator.setBackground(Color.RED);
                statusindicator.setBorder(redBorder);
                statusAllow = false;
                break;

            case "Suspended":
                statusindicator.setBackground(Color.ORANGE);
                statusindicator.setBorder(orangeBorder);
                statusAllow = false;
                break;

            case "Inactive":
            case "Archived":
                statusindicator.setBackground(Color.GRAY);
                statusindicator.setBorder(grayBorder);
                statusAllow = false;
                break;

            default:
                statusindicator.setBackground(Color.WHITE);
                statusindicator.setBorder(lightgrayBorder);
                statusAllow = false;
        }

        setStatusTooltip(stat);
    }            
    
    private void setStatusTooltip(String stat) {
        switch (stat) {

            case "Active":
                statusindicator.setToolTipText("Product is available for purchase.");
                break;
            case "Pending":
                statusindicator.setToolTipText("Product approval is pending.");
                break;
            case "Sold Out":
                statusindicator.setToolTipText("All items have been sold.");
                break;
            case "Out of Stock":
                statusindicator.setToolTipText("Temporarily unavailable.");
                break;
            case "Discontinued":
                statusindicator.setToolTipText("This product is no longer sold.");
                break;
            case "Suspended":
                statusindicator.setToolTipText("\uD83D\uDED1 This product has been suspended.");                                                
                break;
            case "Inactive":
                statusindicator.setToolTipText("Product is inactive.");
                break;
            case "Archived":
                statusindicator.setToolTipText("Product has been archived.");
                break;
            default:
                statusindicator.setToolTipText(null);
                
        }
}
    
    public void StyleScrollPane(){
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        scrollTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);    
    }
    
    private String GlobalStock;
    
    public void ShowComponent(){
        config conf = new config();
        
        String qry = "SELECT * FROM products WHERE prod_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, ID); 
        
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
            
            prodName.setText(name);
            category.setText(cat);
            rarity.setText(rare);
            price.setText(cash);
            desc.setText(descript);
            GlobalStock = stock;
            Onstock.setText(updateStockIndicator());
            id.setText(finalId);
            addedBy.setText(seller);              
            status.setText(stat);
            
            try {
                Object imgPath = prod.get("prod_image");

                File file = new File(
                    System.getProperty("user.dir") + File.separator +
                    "Input_Images" + File.separator +
                    imgPath
                );

                System.out.println(file.getAbsolutePath());
                System.out.println("Exists: " + file.exists());

                ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
                Image img = originalIcon.getImage();
                
                int imgWidth = originalIcon.getIconWidth();
                int imgHeight = originalIcon.getIconHeight();
                
                int labelWidth = 400;
                int labelHeight = 400;
                
                System.out.println("Width: " + labelWidth + "Height: " + labelHeight);
                System.out.println("Width: " + imgWidth + "Height: " + imgHeight);

                double widthRatio = (double) labelWidth / imgWidth;
                double heightRatio = (double) labelHeight / imgHeight;
                double ratio = Math.min(widthRatio, heightRatio);

                if (ratio > 1.0) {
                    ratio = 1.0;
                }

                int newWidth = (int) (imgWidth * ratio);
                int newHeight = (int) (imgHeight * ratio);

                Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                imageUpload.setIcon(new ImageIcon(scaledImage));
                imageUpload.setText("");

            } catch (Exception e) {
                 e.printStackTrace();
                imageUpload.setIcon(null);
                imageUpload.setText("No Image");
            }
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void OpenBtn(){
        orderBtn.setBackground(new Color(153,153,153));
        orderBtn.setForeground(new Color(102,102,102));
    }
    
    private void CheckValidation(){
        if(statusAllow && stockAllow && ussageAllow){
            OpenBtn();
        }
    }
    
    public void Order(){
        User_config conf = new User_config();

        Order order = new Order(ID, panel);
        this.dispose();
        
        if (panel != null) {
            panel.add(order).setVisible(true);
        } else {
            System.out.println("Desktop pane is null!");
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
        imageUpload = new javax.swing.JLabel();
        prodName = new javax.swing.JLabel();
        category = new javax.swing.JLabel();
        price = new javax.swing.JLabel();
        rarity = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        scrollTextArea = new javax.swing.JScrollPane();
        desc = new javax.swing.JTextArea();
        status = new javax.swing.JLabel();
        addedBy = new javax.swing.JLabel();
        Onstock = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        orderBtn = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        statusindicator = new javax.swing.JLabel();
        stockindicator = new javax.swing.JLabel();
        heart = new javax.swing.JLabel();
        badgeLabel = new javax.swing.JLabel();

        imageUpload.setBackground(new java.awt.Color(153, 153, 153));
        imageUpload.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        imageUpload.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageUpload.setText("IMAGE");
        imageUpload.setOpaque(true);

        prodName.setBackground(new java.awt.Color(204, 204, 204));
        prodName.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        prodName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        prodName.setText("Product Name");
        prodName.setOpaque(true);

        category.setBackground(new java.awt.Color(204, 204, 204));
        category.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        category.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        category.setText("Category");
        category.setOpaque(true);

        price.setBackground(new java.awt.Color(204, 204, 204));
        price.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        price.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price.setText("Price");
        price.setOpaque(true);

        rarity.setBackground(new java.awt.Color(204, 204, 204));
        rarity.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        rarity.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rarity.setText("Rarity");
        rarity.setOpaque(true);

        id.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        id.setText("id");

        desc.setEditable(false);
        desc.setColumns(20);
        desc.setFont(new java.awt.Font("Open Sans Semibold", 1, 18)); // NOI18N
        desc.setRows(5);
        desc.setText("Description . . .");
        desc.setWrapStyleWord(true);
        scrollTextArea.setViewportView(desc);

        status.setBackground(new java.awt.Color(204, 204, 204));
        status.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        status.setText("Status");
        status.setOpaque(true);

        addedBy.setBackground(new java.awt.Color(204, 204, 204));
        addedBy.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        addedBy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addedBy.setText("Seller");
        addedBy.setOpaque(true);

        Onstock.setBackground(new java.awt.Color(204, 204, 204));
        Onstock.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        Onstock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Onstock.setText("Stock");
        Onstock.setOpaque(true);

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setText("Name:");

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel2.setText("Category:");

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setText("Rarity: ");

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel4.setText("Price:");

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel5.setText("Status: ");

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel6.setText("Seller: ");

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel9.setText("Stock:");

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel10.setText("PRODUCT DETAILS");

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel11.setText("X");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(311, 311, 311)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel10)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        orderBtn.setBackground(new java.awt.Color(102, 102, 102));
        orderBtn.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        orderBtn.setForeground(new java.awt.Color(153, 153, 153));
        orderBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        orderBtn.setText("ORDER");
        orderBtn.setOpaque(true);
        orderBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                orderBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                orderBtnMouseExited(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N

        jLabel16.setOpaque(true);

        statusindicator.setBackground(new java.awt.Color(153, 153, 153));
        statusindicator.setOpaque(true);

        stockindicator.setBackground(new java.awt.Color(153, 153, 153));
        stockindicator.setOpaque(true);

        heart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heart.setText("cart");
        heart.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                heartAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        heart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                heartMouseClicked(evt);
            }
        });

        badgeLabel.setForeground(new java.awt.Color(153, 0, 0));
        badgeLabel.setText("Badge Unverified!");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel14)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollTextArea, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                    .addComponent(imageUpload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 41, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addGap(360, 360, 360)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(Onstock, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(stockindicator, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(statusindicator, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(orderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(heart, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(370, 370, 370))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel5))
                                .addGap(36, 36, 36)
                                .addComponent(addedBy, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(196, 196, 196)
                                .addComponent(badgeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(121, 121, 121)
                                .addComponent(jLabel3)
                                .addGap(113, 113, 113)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(rarity, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(prodName, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(prodName, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rarity, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addedBy, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Onstock, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(stockindicator, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(badgeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(orderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(statusindicator, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(heart, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(5, 5, 5))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(imageUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(scrollTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel11MouseClicked

    private void heartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_heartMouseClicked
            
    }//GEN-LAST:event_heartMouseClicked

    private void heartAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_heartAncestorAdded
        heart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isFavorited = !isFavorited; 

                if (isFavorited) {
                    heartIcon(true, true);
                    heart.setToolTipText("Click to unfavorite");

                } else {
                    heartIcon(false, false);
                    heart.setToolTipText("Click to favorite");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                heart.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });        // TODO add your handling code here:
    }//GEN-LAST:event_heartAncestorAdded

    private void orderBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderBtnMouseClicked
        if (statusAllow && stockAllow && ussageAllow) {
            orderBtn.setToolTipText(null);
            
            Order();    
            return;
        }// TODO add your handling code here:
    }//GEN-LAST:event_orderBtnMouseClicked

    private String originalText;
    private Timer hoverTimer;
    private int messageIndex = 0;
    
    private void orderBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderBtnMouseEntered
//        animatio ani = new animation();
        
        originalText = orderBtn.getText();
        
        List<String> messages = new ArrayList<>();
        
        if (!statusAllow) messages.add("INACTIVE");
        if (!stockAllow) messages.add("SOLD OUT");
        if (!ussageAllow) messages.add("BADGE UNVERIFIED");
        messages.add("ORDER");
        
        if (messages.equals(1)) {
            orderBtn.setBackground(new Color(102,102,102));
            orderBtn.setForeground(new Color(153,153,153));
            return;
        }

        String[] hoverMessages = messages.toArray(new String[0]);

            messageIndex = 0;

        hoverTimer = new Timer(1500, e -> {
            orderBtn.setText(hoverMessages[messageIndex]);
            messageIndex++;

            if (messageIndex >= hoverMessages.length) {
                messageIndex = 0;
            }
        });
        hoverTimer.start();
    }//GEN-LAST:event_orderBtnMouseEntered

    private void orderBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderBtnMouseExited
        if (hoverTimer != null) hoverTimer.stop();
            orderBtn.setText(originalText);
            
        if(statusAllow && stockAllow && ussageAllow){
            orderBtn.setBackground(new Color(204,204,204));
            orderBtn.setForeground(new Color(0,0,0));
        }
    }//GEN-LAST:event_orderBtnMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Onstock;
    private javax.swing.JLabel addedBy;
    private javax.swing.JLabel badgeLabel;
    private javax.swing.JLabel category;
    private javax.swing.JTextArea desc;
    private javax.swing.JLabel heart;
    private javax.swing.JLabel id;
    private javax.swing.JLabel imageUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel orderBtn;
    private javax.swing.JLabel price;
    private javax.swing.JLabel prodName;
    private javax.swing.JLabel rarity;
    private javax.swing.JScrollPane scrollTextArea;
    private javax.swing.JLabel status;
    private javax.swing.JLabel statusindicator;
    private javax.swing.JLabel stockindicator;
    // End of variables declaration//GEN-END:variables
}
