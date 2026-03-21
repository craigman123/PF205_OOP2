/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Cart;

import Profiles.session;
import User.Market.Order;
import User.Market.Product_Detail;
import configuration.animation;
import configuration.config;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

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
        Prefillers();
    }
    
    public void Prefillers(){
        Cart_config cart = new Cart_config();
        
        String getName = cart.NameGetter();
        
        cart.DisplayPreviousAddress(prefillLocation);
        prefillName.setText(getName);
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
    int add = 0;

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
                String rarity = prod.get("prod_rarity").toString();

                if (status.equals("Active")) {
                    cartPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
                    cartPanel.setBackground(Color.WHITE);
                    
                    JPanel productCard;
                    
                    animation ani = new animation();
                    if(rarity.equals("Common")){
                        productCard = ani.linearGrade(
                            Color.decode("#FAFAFA"),
                            Color.decode("#E0E0E0")
                        );

                    }else if(rarity.equals("Uncommon")){
                        productCard = ani.linearGrade(
                            Color.decode("#FAFAFA"),
                            new Color(102, 187, 106, 80)
                        );

                    }else if(rarity.equals("Rare")){
                        productCard = ani.linearGrade(
                            Color.decode("#FAFAFA"),
                            new Color(66, 165, 245, 80)
                        );

                    }else if(rarity.equals("Exquisite")){
                        productCard = ani.linearGrade(
                            Color.decode("#FAFAFA"),
                            new Color(0, 131, 176, 90)
                        );

                    }else if(rarity.equals("Unique")){
                        productCard = ani.linearGrade(
                            Color.decode("#FAFAFA"),
                            new Color(186, 104, 200, 90)
                        );

                    }else if(rarity.equals("Collectors Choice")){
                        productCard = ani.linearGrade(
                            Color.decode("#FAFAFA"),
                            new Color(255, 179, 0, 100)
                        );

                    }else if(rarity.equals("Antique")){
                        productCard = ani.linearGrade(
                            Color.decode("#FAFAFA"),
                            new Color(229, 57, 53, 90)
                        );

                    }else{
                        productCard = ani.linearGrade(Color.WHITE, Color.LIGHT_GRAY);
                    }
                    productCard.setLayout(new BoxLayout(productCard, BoxLayout.Y_AXIS));
                    productCard.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    Dimension cardSize = new Dimension(220, 320);
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
                    add++;
                    manyFav.setText(add + " Item(s): ");

                    productCard.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            DeclareLocationGlobal();
                            
                            Order ord = new Order(prodId, MainPane);
                            MainPane.add(ord).setVisible(true);
                            
                        }
                    });
                }
            }
        }
    }
    
    mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    // 2️⃣ Make scrollbar thin
    mainScroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));

    // 3️⃣ Apply custom UI with colors and hover
    mainScroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
    private final Color thumbDefault = new Color(102,102,102);
    private final Color thumbHover   = new Color(51,51,51);
    private final Color trackClr     = new Color(204,204,204);

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = thumbDefault;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(trackClr); // set custom track color
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
    @Override
    protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }

    private JButton createZeroButton() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0,0));
        btn.setMinimumSize(new Dimension(0,0));
        btn.setMaximumSize(new Dimension(0,0));
        return btn;
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        scrollbar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                thumbColor = thumbHover;
                scrollbar.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                thumbColor = thumbDefault;
                scrollbar.repaint();
            }
        });
    }
});
}
    
    public void DeclareLocationGlobal(){
        Cart_config cart = new Cart_config();
        
        String address =  String.valueOf(prefillLocation.getSelectedItem());
        String name = prefillName.getText();
        cart.sendGlobal(name, address);
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
        manyFav = new javax.swing.JLabel();
        mainScroll = new javax.swing.JScrollPane();
        cartPanel = new javax.swing.JPanel();
        prefillLocation = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        prefillName = new javax.swing.JTextField();

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

        manyFav.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        manyFav.setText("0 Items");
        getContentPane().add(manyFav, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        cartPanel.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cartPanelLayout = new javax.swing.GroupLayout(cartPanel);
        cartPanel.setLayout(cartPanelLayout);
        cartPanelLayout.setHorizontalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 888, Short.MAX_VALUE)
        );
        cartPanelLayout.setVerticalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 478, Short.MAX_VALUE)
        );

        mainScroll.setViewportView(cartPanel);

        getContentPane().add(mainScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 890, 480));

        prefillLocation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(prefillLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(588, 86, 320, 40));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel2.setText("Delivery Area:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 90, -1, 30));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel3.setText("Recievers Name:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, -1, 30));

        prefillName.setText("Name");
        getContentPane().add(prefillName, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 86, 160, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cartPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane mainScroll;
    private javax.swing.JLabel manyFav;
    private javax.swing.JComboBox<String> prefillLocation;
    private javax.swing.JTextField prefillName;
    // End of variables declaration//GEN-END:variables
}
