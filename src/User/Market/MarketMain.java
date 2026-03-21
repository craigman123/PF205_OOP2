/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Market;

import User.User_config;
import configuration.animation;
import configuration.config;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 *
 * @author user
 */
public class MarketMain extends javax.swing.JInternalFrame {

    /**
     * Creates new form MarketMain
     */
    private JDesktopPane MainPane;
    private String searchText;
    private String status;
    private String categorized;
    
    public MarketMain(JDesktopPane Pane) {
        this.MainPane = Pane;
        User_config userconf = new User_config();
        
        initComponents();
        StyleFrame();
        userconf.DisplayProdStatus(statusBox);
        userconf.DisplayCategory(category);
        loadImages();
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
        
    private boolean isNewProduct(LocalDateTime createdAt) {
        return createdAt.isAfter(LocalDateTime.now().minusDays(1));
    }
    
    public void loadImages() {
    config conf = new config();
    int TotalProducts = 0;
    
    String category = categorized;

    status = (status != null) ? status.trim() : "All";
    category = (category != null) ? category.trim() : "All";
    searchText = (searchText != null) ? searchText.trim() : "";

    mainPanel.removeAll();
    mainPanel.setLayout(new GridLayout(0, 3, 15, 15));
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // 🔥 Prepare search values
    int searchId = -1;
    try {
        searchId = Integer.parseInt(searchText);
    } catch (Exception e) {
        // ignore if not number
    }

    boolean hasSearch = !searchText.isEmpty() &&
                        !searchText.equalsIgnoreCase("Search Products . . .");

    // 🔥 Dynamic scoring query
    String statusCase = status.equals("All")
        ? "0"
        : "CASE WHEN prod_status = ? THEN 1 ELSE 0 END";

    String categoryCase = category.equals("All")
        ? "0"
        : "CASE WHEN prod_category = ? THEN 1 ELSE 0 END";

    StringBuilder qry = new StringBuilder(
        "SELECT *, (" +
        statusCase + " + " +
        categoryCase + " + " +
        (hasSearch ? "CASE WHEN prod_id = ? THEN 2 ELSE 0 END + " : "0 + ") + (" ") +
        (hasSearch ? "CASE WHEN prod_name LIKE ? THEN 1 ELSE 0 END" : "0") +
        ") AS score " +
        "FROM products ORDER BY score DESC, prod_name"
    );

    // 🔥 Parameters
    java.util.List<Object> params = new java.util.ArrayList<>();

    if (!status.equals("All")) params.add(status);
    if (!category.equals("All")) params.add(category);

    if (hasSearch) {
        params.add(searchId);
        params.add("%" + searchText + "%");
    }

    // 🔥 Fetch all products at once
    java.util.List<java.util.Map<String, Object>> results =
        conf.fetchRecords(qry.toString(), params.toArray());

    File folder = new File("Input_Images");

    for (java.util.Map<String, Object> prod : results) {

        String imageName = prod.get("prod_image").toString();
        File file = new File(folder, imageName);
        if (!file.exists()) continue;

        String name = prod.get("prod_name").toString();
        String price = prod.get("prod_price").toString();
        int stock = ((Number) prod.get("prod_stock")).intValue();
        int id = ((Number) prod.get("prod_id")).intValue();
        String prodStatus = prod.get("prod_status").toString();
        String rarity = prod.get("prod_rarity").toString();
        String createdObj = prod.get("date_added").toString();

        // 🔥 Only display ACTIVE (optional: remove if you want all)
        if (!prodStatus.equals("Active")) continue;

        JPanel productCard;
        animation ani = new animation();

        // 🎨 RARITY STYLE (soft tint)
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

        Dimension cardSize = new Dimension(220, 300);
        productCard.setPreferredSize(cardSize);

        // 🖼 Image
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        Image img = icon.getImage().getScaledInstance(200, 180, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        productCard.add(imageLabel);
        productCard.add(Box.createVerticalStrut(8));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdAt = LocalDateTime.parse(createdObj.toString(), formatter);
        
        JLabel newLabel = null;
        if(isNewProduct(createdAt)){
            newLabel = new JLabel("NEW");
            newLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            newLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 24));
            newLabel.setForeground(Color.decode("#B00000"));
            newLabel.setBackground(Color.decode("#2E2E2E"));
            
            productCard.add(Box.createVerticalStrut(10));
            productCard.add(newLabel);
        }

        // 📝 Name
        JLabel nameLabel = new JLabel(name);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 24));

        // 💰 Price
        JLabel priceLabel = new JLabel("$ " + price + ".00");
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        // 📦 Stock
        JLabel stockLabel = new JLabel("Stock: " + stock);
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        productCard.add(Box.createVerticalStrut(5));
        productCard.add(nameLabel);
        productCard.add(priceLabel);
        productCard.add(stockLabel);
        productCard.add(Box.createVerticalStrut(10));

        addHoverLift(productCard, 10, 200); 
        mainPanel.add(productCard);
        TotalProducts++;

        // 🖱 Click event
        productCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Product_Detail proddet = new Product_Detail(id, MainPane);
                MainPane.add(proddet).setVisible(true);
            }
        });
    }
    
    indicator.setText("Products Available: " + String.valueOf(TotalProducts));
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

    mainPanel.revalidate();
    mainPanel.repaint();
    }

    public void addHoverLift(JPanel panel, int liftPixels, int durationMs) {
        final int fps = 60;
        final int steps = (durationMs * fps) / 1000;
        final javax.swing.Timer timer = new javax.swing.Timer(1000 / fps, null);

        final Border originalBorder = panel.getBorder() != null ? panel.getBorder() : BorderFactory.createEmptyBorder(5,5,5,5);
        final Border hoverBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), 
                originalBorder
        );

        final Insets originalInsets = originalBorder instanceof EmptyBorder ?
                ((EmptyBorder) originalBorder).getBorderInsets() : new Insets(5,5,5,5);

        final int[] step = {0};
        final boolean[] lifting = {true};
        final boolean[] hoverActive = {false};

        timer.addActionListener(e -> {
            double t = (double) step[0] / steps;
            double factor = t < 0.5 ? 4*t*t*t : 1 - Math.pow(-2*t + 2, 3)/2;

            Insets newInsets = lifting[0] ?
                    new Insets(
                            originalInsets.top - (int)(liftPixels * factor),
                            originalInsets.left,
                            originalInsets.bottom + (int)(liftPixels * factor),
                            originalInsets.right
                    ) :
                    new Insets(
                            originalInsets.top - (int)(liftPixels * (1 - factor)),
                            originalInsets.left,
                            originalInsets.bottom + (int)(liftPixels * (1 - factor)),
                            originalInsets.right
                    );

            panel.setBorder(new EmptyBorder(newInsets));
            if(hoverActive[0]) {
                panel.setBorder(BorderFactory.createCompoundBorder(hoverBorder, new EmptyBorder(newInsets)));
            }
            panel.revalidate();
            panel.repaint();

            step[0]++;
            if(step[0] > steps) timer.stop();
        });

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lifting[0] = true;
                step[0] = 0;
                hoverActive[0] = true;
                timer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lifting[0] = false;
                step[0] = 0;
                hoverActive[0] = false;
                timer.start();
            }
        });
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        searchBar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        statusBox = new javax.swing.JComboBox<>();
        mainScroll = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        category = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        indicator = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel1.setText("MARKET");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(393, 393, 393)
                .addComponent(jLabel1)
                .addContainerGap(410, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        searchBar.setEditable(false);
        searchBar.setForeground(new java.awt.Color(153, 153, 153));
        searchBar.setText("Search Market . . .");
        searchBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchBarMouseClicked(evt);
            }
        });
        jPanel1.add(searchBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 98, 521, 34));

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SEARCH");
        jLabel2.setOpaque(true);
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(821, 94, 103, 37));

        statusBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        statusBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusBoxActionPerformed(evt);
            }
        });
        jPanel1.add(statusBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 95, 126, 37));

        mainPanel.setBackground(new java.awt.Color(204, 204, 204));
        mainPanel.setLayout(new java.awt.GridLayout(1, 0));
        mainScroll.setViewportView(mainPanel);

        jPanel1.add(mainScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 208, 909, 428));

        category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryActionPerformed(evt);
            }
        });
        jPanel1.add(category, new org.netbeans.lib.awtextra.AbsoluteConstraints(545, 95, 126, 37));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        indicator.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        indicator.setText("jLabel3");
        jPanel3.add(indicator, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 160, 40));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 138, 180, 60));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void statusBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusBoxActionPerformed

    private void categoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryActionPerformed

    private void searchBarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchBarMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(searchBar, "Search Market . . .");        // TODO add your handling code here:
    }//GEN-LAST:event_searchBarMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        categorized = (String) category.getSelectedItem();
        status = (String) statusBox.getSelectedItem();
        searchText = searchBar.getText();
        
        loadImages();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> category;
    private javax.swing.JLabel indicator;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane mainScroll;
    private javax.swing.JTextField searchBar;
    private javax.swing.JComboBox<String> statusBox;
    // End of variables declaration//GEN-END:variables
}
