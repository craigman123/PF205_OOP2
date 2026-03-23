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
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.*;
import static java.time.LocalDateTime.now;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USER18
 */
public final class Orders extends javax.swing.JInternalFrame {

    /**
     * Creates new form Orders
     */
    private JDesktopPane MainPane;

    public Orders(JDesktopPane Pane) {
        this.MainPane = Pane;
        initComponents();
        loadImages();
        StyleFrame();
        countUserOrdersStatus();
    }

    public final void StyleFrame() {
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui
                = (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
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

    // Clear and setup panel
    orderPanel.removeAll();
    orderPanel.setLayout(new GridLayout(0, 3, 15, 15)); // 3 columns, auto rows
    orderPanel.setBackground(Color.WHITE);
    orderPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // 1️⃣ Get latest orders per product (only one order per product)
    String orderQuery
            = "SELECT o.prod_id, o.order_prod, o.order_date, o.order_status, o.order_visibility "
            + "FROM userOrders o "
            + "INNER JOIN ( "
            + "   SELECT prod_id, MAX(order_date) AS latest_date "
            + "   FROM userOrders "
            + "   WHERE user_id = ? "
            + "   GROUP BY prod_id "
            + ") latest "
            + "ON o.prod_id = latest.prod_id "
            + "AND o.order_date = latest.latest_date "
            + "WHERE o.user_id = ?";

    java.util.List<java.util.Map<String, Object>> orders
            = conf.fetchRecords(orderQuery, see.GetID(), see.GetID());

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Map<Integer, java.sql.Timestamp> latestMap = new HashMap<>();

    for (java.util.Map<String, Object> f : orders) {
        int prodId = ((Number) f.get("prod_id")).intValue();
        String orderDateStr = f.get("order_date").toString();

        try {
            java.util.Date orderDate = sdf.parse(orderDateStr);
            latestMap.put(prodId, new java.sql.Timestamp(orderDate.getTime()));
        } catch (ParseException e) {
            System.out.println("Error parsing date for prod " + prodId + ": " + orderDateStr);
        }
    }

    for (java.util.Map<String, Object> order : orders) {
        int prodId = ((Number) order.get("prod_id")).intValue();
        String status = order.get("order_status").toString();
        Object visibilityObj = order.get("order_visibility");
        boolean visibility = visibilityObj != null && Boolean.parseBoolean(visibilityObj.toString());
        System.out.println(visibility);

        if (visibility) {
            java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords("SELECT * FROM products WHERE prod_id = ?", prodId);
            if (result.isEmpty()) {
                continue;
            }

            java.util.Map<String, Object> prod = result.get(0);
            File file = new File("Input_Images/" + prod.get("prod_image").toString());
            if (!file.exists()) {
                continue;
            }

            java.sql.Timestamp latestTime = latestMap.get(prodId);
            String name = prod.get("prod_name").toString();
            String price = prod.get("prod_price").toString();

            // Build product card
            animation ani = new animation();
            JPanel productCard = ani.linearGrade(Color.WHITE, Color.GRAY);
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
            nameLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));

            JLabel priceLabel = new JLabel("$ " + price + ".00");
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            priceLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));

            String formattedDate = "No Date";
            if (latestTime != null) {
                SimpleDateFormat sdff = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                formattedDate = sdff.format(latestTime);
            }

            JLabel dateLabel = new JLabel(formattedDate);
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dateLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));

            JLabel label = new JLabel("Buy Again");
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
            label.setForeground(Color.GREEN);

            productCard.add(Box.createVerticalStrut(10));
            productCard.add(imageLabel);
            productCard.add(Box.createVerticalStrut(8));
            productCard.add(nameLabel);
            productCard.add(priceLabel);
            productCard.add(dateLabel);
            productCard.add(label);
            productCard.add(Box.createVerticalStrut(10));

            orderPanel.add(productCard);

            productCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    Product_Detail proddet = new Product_Detail(prodId, MainPane);
                    MainPane.add(proddet).setVisible(true);
                }
            });
        }
    }

    // ✅ Layout & scrolling fix
    mainScroll.setViewportView(orderPanel);
    mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    // Optional: thin scrollbar
    mainScroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));

    // Optional: force preferred size so scrolling works with many rows
    int totalCards = orderPanel.getComponentCount();
    int rows = (int) Math.ceil(totalCards / 3.0);
    int cardHeight = 320; // approximate card height
    orderPanel.setPreferredSize(new Dimension(orderPanel.getWidth(), rows * cardHeight));

    orderPanel.revalidate();
    orderPanel.repaint();
}

    public void countUserOrdersStatus() {
        session see = new session();
        config conf = new config();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date now = new java.util.Date();

        int totalOrders = 0;
        int pendingCount = 0;
        int inTransitCount = 0;
        int deliveryInProgressCount = 0;
        int completedCount = 0;

        String query = "SELECT prod_id, order_date, order_status, order_visibility FROM userOrders WHERE user_id = ?";
        List<Map<String, Object>> orders = conf.fetchRecords(query, see.GetID());

        for (Map<String, Object> order : orders) {

            String currentStatus = order.get("order_status").toString();
            String orderVisibility = order.get("order_visibility").toString();
            String orderDateStr = order.get("order_date").toString();
            java.util.Date orderDate;
            try {
                orderDate = sdf.parse(orderDateStr);
            } catch (Exception e) {
                System.out.println("Error parsing date for prod " + order.get("prod_id") + ": " + orderDateStr);
                continue;
            }

            long diffDays = (now.getTime() - orderDate.getTime()) / (1000 * 60 * 60 * 24);
            String newStatus = currentStatus;

            if (orderVisibility.equals("true")) {
                totalOrders++;
                switch (currentStatus) {
                    case "Pending":
                        pendingCount++;
                        break;

                    case "In Transit":
                        if (diffDays >= 3) {
                            newStatus = "On Delivery";
                            deliveryInProgressCount++;
                        } else {
                            inTransitCount++;
                        }
                        break;

                    case "On Delivery":
                        if (diffDays >= 4) {
                            
                        } else {
                            deliveryInProgressCount++;
                        }
                        break;

                    case "Completed":
                        completedCount++;
                        break;

                    default:
                        pendingCount++;
                        break;
                }

                // Update only if the status progressed
                if (!newStatus.equals(currentStatus)) {
                    String updateQry = "UPDATE userOrders SET order_status = ? WHERE prod_id = ? AND user_id = ?";
                    conf.updateRecord(updateQry, newStatus, order.get("prod_id"), see.GetID());
                }
            }
        }

        // Update GUI labels
        O.setText(String.valueOf(totalOrders));
        P.setText(String.valueOf(pendingCount));
        IT.setText(String.valueOf(inTransitCount));
        ID.setText(String.valueOf(deliveryInProgressCount));
        C.setText(String.valueOf(completedCount));
    }

    private void loadOrdersByStatus(String statusFilter) {
    session see = new session();
    config conf = new config();
    File folder = new File("Input_Images");

    if (!folder.exists() || !folder.isDirectory()) {
        return;
    }

    // Clear and setup panel
    orderPanel.removeAll();
    orderPanel.setLayout(new GridLayout(0, 3, 15, 15)); // 3 columns, auto rows
    orderPanel.setBackground(Color.WHITE);
    orderPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Fetch all orders for this user filtered by status
    String orderQuery
            = "SELECT * FROM userOrders u "
            + "JOIN products p ON u.prod_id = p.prod_id "
            + "WHERE u.user_id = ? AND LOWER(u.order_status) = LOWER(?) "
            + "ORDER BY u.order_date DESC";

    java.util.List<java.util.Map<String, Object>> orders
            = conf.fetchRecords(orderQuery, see.GetID(), statusFilter);

    SimpleDateFormat sdff = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

    for (java.util.Map<String, Object> order : orders) {
        int prodId = ((Number) order.get("prod_id")).intValue();

        Object visibilityObj = order.get("order_visibility");
        boolean visibility = visibilityObj != null && Boolean.parseBoolean(visibilityObj.toString());

        if (visibility) {
            java.util.List<java.util.Map<String, Object>> result
                    = conf.fetchRecords("SELECT * FROM products WHERE prod_id = ?", prodId);

            if (result.isEmpty()) continue;
            java.util.Map<String, Object> prod = result.get(0);

            File file = new File("Input_Images/" + prod.get("prod_image").toString());
            if (!file.exists()) continue;

            String name = prod.get("prod_name").toString();
            String price = prod.get("prod_price").toString();
            String orderDateStr = order.get("order_date").toString();

            java.sql.Timestamp latestTime = null;
            try {
                latestTime = java.sql.Timestamp.valueOf(orderDateStr); // parse to Timestamp
            } catch (IllegalArgumentException e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    java.util.Date parsedDate = sdf.parse(orderDateStr);
                    latestTime = new java.sql.Timestamp(parsedDate.getTime());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }

            // Build product card
            animation ani = new animation();
            JPanel productCard = ani.linearGrade(Color.WHITE, Color.GRAY);
            productCard.setLayout(new BoxLayout(productCard, BoxLayout.Y_AXIS));
            productCard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            Dimension cardSize = new Dimension(220, 320);
            productCard.setPreferredSize(cardSize);

            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(200, 180, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel nameLabel = new JLabel(name);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));

            JLabel priceLabel = new JLabel("$ " + price + ".00");
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            priceLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));

            String formattedDate = latestTime != null ? sdff.format(latestTime) : "No Date";
            JLabel dateLabel = new JLabel(formattedDate);
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dateLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));

            JLabel stat = new JLabel("Status: " + statusFilter);
            stat.setAlignmentX(Component.CENTER_ALIGNMENT);
            stat.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
            if (statusFilter.equalsIgnoreCase("Pending")) {
                stat.setForeground(Color.decode("#00D18B"));
            } else if (statusFilter.equalsIgnoreCase("In Transit")) {
                stat.setForeground(Color.decode("#0015FF"));
            } else if (statusFilter.equalsIgnoreCase("On Delivery")) {
                stat.setForeground(Color.decode("#A37900"));
            } else if (statusFilter.equalsIgnoreCase("Completed")) {
                stat.setForeground(Color.decode("#00A310"));
            } else {
                stat.setForeground(Color.decode("#787878"));
            }

            JLabel label = new JLabel("View Order");
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
            label.setForeground(Color.decode("#006094"));

            productCard.add(Box.createVerticalStrut(10));
            productCard.add(imageLabel);
            productCard.add(Box.createVerticalStrut(8));
            productCard.add(nameLabel);
            productCard.add(priceLabel);
            productCard.add(stat);
            productCard.add(dateLabel);
            productCard.add(label);
            productCard.add(Box.createVerticalStrut(10));

            addHoverLift(productCard, 10, 200);
            orderPanel.add(productCard);

            int orderID = ((Number) order.get("order_id")).intValue();
            productCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    SeeProductStatus prodStat = new SeeProductStatus(orderID, prodId, MainPane);
                    MainPane.add(prodStat).setVisible(true);
                }
            });
        }
    }

    // ✅ Layout fix for scroll & grid
    mainScroll.setViewportView(orderPanel);
    mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    mainScroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));

    int totalCards = orderPanel.getComponentCount();
    int rows = (int) Math.ceil(totalCards / 3.0);
    int cardHeight = 320; // card height including spacing
    orderPanel.setPreferredSize(new Dimension(orderPanel.getWidth(), rows * cardHeight));

    orderPanel.revalidate();
    orderPanel.repaint();
}

    public void addHoverLift(JPanel panel, int liftPixels, int durationMs) {
        final int fps = 60;
        final int steps = (durationMs * fps) / 1000;
        final javax.swing.Timer timer = new javax.swing.Timer(1000 / fps, null);

        final Border originalBorder = panel.getBorder() != null ? panel.getBorder() : BorderFactory.createEmptyBorder(5, 5, 5, 5);
        final Border hoverBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                originalBorder
        );

        final Insets originalInsets = originalBorder instanceof EmptyBorder
                ? ((EmptyBorder) originalBorder).getBorderInsets() : new Insets(5, 5, 5, 5);

        final int[] step = {0};
        final boolean[] lifting = {true};
        final boolean[] hoverActive = {false};

        timer.addActionListener(e -> {
            double t = (double) step[0] / steps;
            double factor = t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;

            Insets newInsets = lifting[0]
                    ? new Insets(
                            originalInsets.top - (int) (liftPixels * factor),
                            originalInsets.left,
                            originalInsets.bottom + (int) (liftPixels * factor),
                            originalInsets.right
                    )
                    : new Insets(
                            originalInsets.top - (int) (liftPixels * (1 - factor)),
                            originalInsets.left,
                            originalInsets.bottom + (int) (liftPixels * (1 - factor)),
                            originalInsets.right
                    );

            panel.setBorder(new EmptyBorder(newInsets));
            if (hoverActive[0]) {
                panel.setBorder(BorderFactory.createCompoundBorder(hoverBorder, new EmptyBorder(newInsets)));
            }
            panel.revalidate();
            panel.repaint();

            step[0]++;
            if (step[0] > steps) {
                timer.stop();
            }
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
        jLabel1 = new javax.swing.JLabel();
        mainScroll = new javax.swing.JScrollPane();
        orderPanel = new javax.swing.JPanel();
        CountPanel = new javax.swing.JPanel();
        label = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        O = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        P = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        IT = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        ID = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        comp3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        C = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        comp1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        comp4 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        comp5 = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel1.setText("ORDERS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(403, 403, 403)
                .addComponent(jLabel1)
                .addContainerGap(464, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        orderPanel.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 898, Short.MAX_VALUE)
        );
        orderPanelLayout.setVerticalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );

        mainScroll.setViewportView(orderPanel);

        getContentPane().add(mainScroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 890, 410));

        label.setBackground(new java.awt.Color(204, 204, 204));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelMouseEntered(evt);
            }
        });
        label.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel4.setText("ORDERS");
        label.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        O.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        O.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        O.setText("0");
        label.add(O, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 60, -1));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4MouseEntered(evt);
            }
        });
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel5.setText("PENDING");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        P.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        P.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P.setText("0");
        jPanel4.add(P, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 60, -1));

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));
        jPanel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel11MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel11MouseEntered(evt);
            }
        });
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel11.setText("IN TRANSIT");
        jPanel11.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        IT.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        IT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IT.setText("0");
        jPanel11.add(IT, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 60, -1));

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel7MouseEntered(evt);
            }
        });
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel7.setText("IN DELIVERY");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 110, 40));

        ID.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        ID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ID.setText("0");
        jPanel7.add(ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 60, -1));

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel8.setText("COMPLETED");
        jPanel8.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        comp3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        comp3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        comp3.setText("0");
        jPanel8.add(comp3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 60, -1));

        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 150, 80));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
        });
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setText("COMPLETED");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        C.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        C.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        C.setText("0");
        jPanel5.add(C, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 60, -1));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel6.setText("COMPLETED");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        comp1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        comp1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        comp1.setText("0");
        jPanel6.add(comp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 60, -1));

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 150, 80));

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel9.setText("COMPLETED");
        jPanel9.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        comp4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        comp4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        comp4.setText("0");
        jPanel9.add(comp4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 60, -1));

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel10.setText("COMPLETED");
        jPanel10.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        comp5.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        comp5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        comp5.setText("0");
        jPanel10.add(comp5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 60, -1));

        jPanel9.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 150, 80));

        jPanel5.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 150, 80));

        javax.swing.GroupLayout CountPanelLayout = new javax.swing.GroupLayout(CountPanel);
        CountPanel.setLayout(CountPanelLayout);
        CountPanelLayout.setHorizontalGroup(
            CountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CountPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CountPanelLayout.setVerticalGroup(
            CountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CountPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(CountPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void labelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMouseClicked
        loadImages();        // TODO add your handling code here:
    }//GEN-LAST:event_labelMouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        loadOrdersByStatus("Pending");        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jPanel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel11MouseClicked
        loadOrdersByStatus("In Transit");        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel11MouseClicked

    private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
        loadOrdersByStatus("On Delivery");        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MouseClicked

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        loadOrdersByStatus("Completed");        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

    private void labelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMouseEntered
        addHoverLift(label, 10, 200);        // TODO add your handling code here:
    }//GEN-LAST:event_labelMouseEntered

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        addHoverLift(jPanel4, 10, 200);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel11MouseEntered
        addHoverLift(jPanel11, 10, 200);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel11MouseEntered

    private void jPanel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseEntered
        addHoverLift(jPanel7, 10, 200);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MouseEntered

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
        addHoverLift(jPanel5, 10, 200);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel C;
    private javax.swing.JPanel CountPanel;
    private javax.swing.JLabel ID;
    private javax.swing.JLabel IT;
    private javax.swing.JLabel O;
    private javax.swing.JLabel P;
    private javax.swing.JLabel comp1;
    private javax.swing.JLabel comp3;
    private javax.swing.JLabel comp4;
    private javax.swing.JLabel comp5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel label;
    private javax.swing.JScrollPane mainScroll;
    private javax.swing.JPanel orderPanel;
    // End of variables declaration//GEN-END:variables
}
