/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.Sales;

import configuration.animation;
import configuration.config;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 *
 * @author user
 */
public final class Graph extends javax.swing.JInternalFrame {

    /**
     * Creates new form Graph
     */
    public Graph() {
        initComponents();
        StyleFrame();
        BarChart("monthly");
        loadButtons();
    }
    
    public void loadButtons(){
        ButtonGroup menuGroup = new ButtonGroup();
        
        menuGroup.add(year);
        menuGroup.add(quart);
        menuGroup.add(month);
        menuGroup.add(week);
        menuGroup.add(day);
        
        animation.StyleToggleButtons(year); 
        animation.StyleToggleButtons(quart); 
        animation.StyleToggleButtons(month); 
        animation.StyleToggleButtons(week); 
        animation.StyleToggleButtons(day); 
    }
    
    public void BarChart(String period) {
    config conf = new config(); // your DB helper
    Map<String, Double> salesData = new LinkedHashMap<>();
    String[] categories = {"Pistol", "Rifle", "Sniper Rifle", "Shotgun", "Sub Machine Gun", "Machine Gun"};

    Calendar cal = Calendar.getInstance();
    Calendar startCal = (Calendar) cal.clone(); // start of range
    switch (period.toLowerCase()) {
        case "daily":
            startCal.add(Calendar.DAY_OF_YEAR, -1);
            break; // startCal = today
        case "weekly":
            startCal.add(Calendar.DAY_OF_YEAR, -6); // last 7 days
            break;
        case "monthly":
            startCal.add(Calendar.DAY_OF_YEAR, -29); // last 30 days
            break;
        case "quarterly":
            startCal.add(Calendar.DAY_OF_YEAR, -89); // last 90 days
            break;
        case "yearly":
            startCal.add(Calendar.DAY_OF_YEAR, -364); // last 365 days
            break;
        default:
            startCal.add(Calendar.DAY_OF_YEAR, -29); // default 30 days
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String startDate = sdf.format(startCal.getTime());
    String endDate = sdf.format(cal.getTime());

    try {
        for (String cat : categories) {
            String qry = "SELECT COALESCE(SUM(CAST(u.order_totalPrice AS REAL)),0) AS total " +
             "FROM userOrders u " +
             "JOIN products p ON u.prod_id = p.prod_id " +
             "WHERE LOWER(p.prod_category) = LOWER(?) " +
             "AND DATE(u.order_date) BETWEEN ? AND ? " +
             "AND ( " +
             "    (LOWER(u.order_paymentMethod) = 'cash on delivery' AND LOWER(u.order_status) = 'completed') " +
             "    OR " +
             "    (LOWER(u.order_paymentMethod) != 'cash on delivery') " +
             ")";

            List<Object> allParams = new ArrayList<>();
            allParams.add(cat);       // first param = category
            allParams.add(startDate); // start of period
            allParams.add(endDate);   // end of period

            java.util.List<java.util.Map<String,Object>> result = conf.fetchRecords(qry, allParams.toArray());
            double total = 0.0;
            if (!result.isEmpty() && result.get(0).get("total") != null) {
                total = Double.parseDouble(result.get(0).get("total").toString());
            }
            salesData.put(cat, total);
            System.out.println(cat + " -> " + total); // debug
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Create chart panel
    ChartPanel chartPanel = new ChartPanel(salesData);
    MainPane.removeAll();
    MainPane.setLayout(new BorderLayout());
    MainPane.add(chartPanel, BorderLayout.CENTER);
    MainPane.revalidate();
    MainPane.repaint();
}

    // Custom chart panel for drawing bars
    static class ChartPanel extends JPanel {
    private final Map<String, Double> data;
    private final Color barColor = new Color(0, 123, 255);
    private final int padding = 50;
    private final int labelPadding = 40;

    private double animationProgress = 0.0; // 0 → 1
    private Timer animationTimer;

    public ChartPanel(Map<String, Double> data) {
        this.data = data;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        startAnimation();
    }

    private void startAnimation() {
        int duration = 500; // ms, adjust speed
        int fps = 60;
        int delay = 1000 / fps;
        int steps = duration / delay;

        animationTimer = new Timer(delay, null);
        animationTimer.addActionListener(e -> {
            animationProgress += 1.0 / steps;
            if (animationProgress >= 1.0) {
                animationProgress = 1.0;
                animationTimer.stop();
            }
            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        double maxVal = getMaxValue();

        // Draw Y-axis grid lines and labels
        int numYLabels = 5;
        g2.setColor(Color.BLACK);
        Font originalFont = g2.getFont();
        g2.setFont(originalFont.deriveFont(Font.PLAIN, 12f));

        for (int i = 0; i <= numYLabels; i++) {
            int y = height - padding - (int)(i * (height - 2 * padding) / (double)numYLabels);
            double value = i * maxVal / numYLabels;
            g2.drawLine(padding - 5, y, width - padding, y);
            g2.drawString(formatNumber(value), padding - labelPadding, y + 5);
        }

        // Draw bars with animation
        int categoryCount = data.size();
        int barWidth = (width - 2 * padding) / (categoryCount * 2);
        int x = padding;

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            double value = entry.getValue();
            int fullBarHeight = (int)((value / maxVal) * (height - 2 * padding));
            int animatedHeight = (int)(fullBarHeight * animationProgress);

            // Draw bar
            g2.setColor(barColor);
            g2.fillRect(x, height - padding - animatedHeight, barWidth, animatedHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, height - padding - animatedHeight, barWidth, animatedHeight);

            // Draw value above bar once animation is nearly done
            if (animationProgress > 0.9) {
                g2.drawString(formatNumber(value), x + barWidth / 4, height - padding - animatedHeight - 5);
            }

            x += 2 * barWidth;
        }

        // Draw category labels in bold
        g2.setFont(originalFont.deriveFont(Font.BOLD, 14f));
        x = padding;
        for (String category : data.keySet()) {
            g2.drawString(category, x, height - padding + 20);
            x += 2 * barWidth;
        }
    }

    private double getMaxValue() {
        double max = 0;
        for (double v : data.values()) if (v > max) max = v;
        return max == 0 ? 1 : max; // avoid divide by zero
    }

    private String formatNumber(double value) {
        if (value >= 1_000_000_000)
            return String.format("%.1fB", value / 1_000_000_000);
        else if (value >= 1_000_000)
            return String.format("%.1fM", value / 1_000_000);
        else if (value >= 1_000)
            return String.format("%.1fK", value / 1_000);
        else
            return String.format("%.0f", value);
    }
}

    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPane = new javax.swing.JPanel();
        year = new javax.swing.JToggleButton();
        week = new javax.swing.JToggleButton();
        day = new javax.swing.JToggleButton();
        quart = new javax.swing.JToggleButton();
        month = new javax.swing.JToggleButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MainPane.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout MainPaneLayout = new javax.swing.GroupLayout(MainPane);
        MainPane.setLayout(MainPaneLayout);
        MainPaneLayout.setHorizontalGroup(
            MainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 937, Short.MAX_VALUE)
        );
        MainPaneLayout.setVerticalGroup(
            MainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        getContentPane().add(MainPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 65, 937, 440));

        year.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        year.setText("YEARLY");
        year.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                yearMouseClicked(evt);
            }
        });
        getContentPane().add(year, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 43));

        week.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        week.setText("WEEKLY");
        week.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                weekMouseClicked(evt);
            }
        });
        getContentPane().add(week, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, 43));

        day.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        day.setText("DAILY");
        day.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dayMouseClicked(evt);
            }
        });
        getContentPane().add(day, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, 43));

        quart.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        quart.setText("QUARTERLY");
        quart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                quartMouseClicked(evt);
            }
        });
        getContentPane().add(quart, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, 43));

        month.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        month.setText("MONTHLY");
        month.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        getContentPane().add(month, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, 43));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void yearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_yearMouseClicked
        BarChart("yearly");        // TODO add your handling code here:
    }//GEN-LAST:event_yearMouseClicked

    private void dayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dayMouseClicked
        BarChart("daily");        // TODO add your handling code here:
    }//GEN-LAST:event_dayMouseClicked

    private void monthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_monthMouseClicked
        BarChart("monthly");        // TODO add your handling code here:
    }//GEN-LAST:event_monthMouseClicked

    private void weekMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_weekMouseClicked
        BarChart("weekly");        // TODO add your handling code here:
    }//GEN-LAST:event_weekMouseClicked

    private void quartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quartMouseClicked
        BarChart("quarterly");        // TODO add your handling code here:
    }//GEN-LAST:event_quartMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainPane;
    private javax.swing.JToggleButton day;
    private javax.swing.JToggleButton month;
    private javax.swing.JToggleButton quart;
    private javax.swing.JToggleButton week;
    private javax.swing.JToggleButton year;
    // End of variables declaration//GEN-END:variables
}
