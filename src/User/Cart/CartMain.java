/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Cart;

import Profiles.session;
import User.Market.Product_Detail;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author user
 */
public class CartMain extends javax.swing.JInternalFrame {

    /**
     * Creates new form CartMain
     */
    private JDesktopPane MainPane;
    
    public CartMain(JDesktopPane Pane) {
        this.MainPane = Pane;
        initComponents();
        StyleFrame();
        loadImages();
    }

    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    private void loadImages() {
    session see = new session();
    config conf = new config();
    File folder = new File("Input_Images");

    if (!folder.exists() || !folder.isDirectory()) {
        System.out.println("Folder not found!");
        return;
    }

    cartPanel.removeAll();
    cartPanel.setLayout(new GridLayout(0, 3, 15, 15));
    cartPanel.setBackground(Color.WHITE);
    cartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    String favQuery = "SELECT prod_id FROM cart WHERE user_id = ?";
    java.util.List<java.util.Map<String, Object>> favoriteOnly = conf.fetchRecords(favQuery, see.GetID());

    java.util.Set<Integer> favoriteSet = new java.util.HashSet<>();
    for (java.util.Map<String, Object> f : favoriteOnly) {
        favoriteSet.add(((Number) f.get("prod_id")).intValue());
    }

    File[] files = folder.listFiles((dir, name) -> name.toLowerCase().matches(".*\\.(jpg|png|jpeg)$"));

    if (files != null) {
        for (File file : files) {
            String qry = "SELECT * FROM products WHERE prod_image = ?";
            java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, file.getName());

            if (!result.isEmpty()) {
                java.util.Map<String, Object> prod = result.get(0);
                int prodId = ((Number) prod.get("prod_id")).intValue();

                if (!favoriteSet.contains(prodId)) continue;

                String name = prod.get("prod_name").toString();
                String price = prod.get("prod_price").toString();
                int stock = ((Number) prod.get("prod_stock")).intValue();
                String status = prod.get("prod_status").toString();

                if (status.equals("Active")) {
                    cartPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
                    cartPanel.setBackground(Color.WHITE);

                    animation ani = new animation();
                    JPanel productCard = ani.linearGrade(Color.WHITE, Color.GRAY);
                    productCard.setLayout(new BoxLayout(productCard, BoxLayout.Y_AXIS));
                    productCard.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    Dimension cardSize = new Dimension(220, 300);
                    productCard.setPreferredSize(cardSize);
                    productCard.setMinimumSize(cardSize);
                    productCard.setMaximumSize(cardSize);

                    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(200, 180, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(img));
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    JLabel nameLabel = new JLabel(name);
                    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    nameLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 24));

                    JLabel priceLabel = new JLabel("$ " + price + ".00");
                    priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    priceLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

                    JLabel stockLabel = new JLabel("Stock: " + stock);
                    stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    stockLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

                    productCard.add(Box.createVerticalStrut(10));
                    productCard.add(imageLabel);
                    productCard.add(Box.createVerticalStrut(8));
                    productCard.add(nameLabel);
                    productCard.add(priceLabel);
                    productCard.add(stockLabel);
                    productCard.add(Box.createVerticalStrut(10));

                    cartPanel.add(productCard);

                    productCard.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            Product_Detail proddet = new Product_Detail(prodId, MainPane);
                            MainPane.add(proddet).setVisible(true);
                        }
                    });
                }
            }
        }
    }

    cartPanel.revalidate();
    cartPanel.repaint();
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
        jLabel2 = new javax.swing.JLabel();
        cartPanel = new javax.swing.JPanel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel1.setText("CART");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(439, 439, 439)
                .addComponent(jLabel1)
                .addContainerGap(414, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 80));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel2.setText("Favorited Items");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        cartPanel.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cartPanelLayout = new javax.swing.GroupLayout(cartPanel);
        cartPanel.setLayout(cartPanelLayout);
        cartPanelLayout.setHorizontalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        cartPanelLayout.setVerticalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );

        getContentPane().add(cartPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 900, 490));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cartPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
