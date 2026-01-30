package configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.Border;

public class animation {
    
    private Map<JButton, List<JCheckBoxMenuItem>> buttonMenuItemsMap = new HashMap<>();
    
    private List<JCheckBoxMenuItem> allItems = new ArrayList<>();
    
    public int addPlaceholder(JTextField field, String placeholder) {
        
    Color placeholderColor = Color.GRAY;
    Color textColor = Color.BLACK;

    final boolean[] showingPlaceholder = {true};

    field.addHierarchyListener(e -> {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && field.isShowing()) {
            field.setText(placeholder);
            field.setForeground(placeholderColor);
            showingPlaceholder[0] = true;
        }
    });
    
        field.setEditable(true);
        field.setFocusable(true);

    field.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            String FieldHolder = field.getText();
            if(!placeholder.equals(FieldHolder)){
                return;
            }
            if (showingPlaceholder[0]) {
                field.setText("");
                field.setForeground(textColor);
                showingPlaceholder[0] = false;
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (field.getText().trim().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(placeholderColor);
                showingPlaceholder[0] = true;
            }
        }
    });
        
        field.setEnabled(true);
        return 0;
}
    
    private static final int GROW_W = 8;
    private static final int GROW_H = 3;
    private static final int TIMER_DELAY = 10;
    private static final int STEP = 10;

    private static final Map<JTextField, Rectangle> originalBounds = new HashMap<>();
    private static final Map<JTextField, Timer> timers = new HashMap<>();

    private static int moveToward(int current, int target) {
        if (current == target) return target;
        int delta = target - current;
        int step = Math.min(Math.abs(delta), STEP);
        return current + Integer.signum(delta) * step;
    }

    private static void animateTextField(JTextField tf, boolean grow) {
        originalBounds.putIfAbsent(tf, tf.getBounds());
        Rectangle base = originalBounds.get(tf);

        int targetW = grow ? base.width + GROW_W : base.width;
        int targetH = grow ? base.height + GROW_H : base.height;
        int targetX = grow ? base.x - GROW_W / 2 : base.x;
        int targetY = grow ? base.y - GROW_H / 2 : base.y;

        if (timers.containsKey(tf) && timers.get(tf).isRunning()) {
            timers.get(tf).stop();
        }

        Timer hoverTimer = new Timer(TIMER_DELAY, null);
        hoverTimer.addActionListener(e -> {
            Rectangle r = tf.getBounds();

            int newWidth = moveToward(r.width, targetW);
            int newHeight = moveToward(r.height, targetH);
            int newX = moveToward(r.x, targetX);
            int newY = moveToward(r.y, targetY);

            tf.setBounds(newX, newY, newWidth, newHeight);
            tf.revalidate();
            tf.repaint();

            if (newWidth == targetW && newHeight == targetH && newX == targetX && newY == targetY) {
                hoverTimer.stop();
            }
        });

        timers.put(tf, hoverTimer);
        hoverTimer.start();
    }

    public static void attachHoverEffect(JTextField tf) {
        tf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateTextField(tf, true);  
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateTextField(tf, false); 
            }
        });
    }
    
    public static void showLoadingAndOpen(JFrame currentFrame, JFrame nextFrame) {
    JFrame loadingFrame = new JFrame();
    loadingFrame.setSize(300, 150);
    loadingFrame.setUndecorated(true);
    loadingFrame.setLocationRelativeTo(null);
    loadingFrame.setLayout(new BorderLayout());

    JLabel iconLabel = new JLabel(new ImageIcon(LogReg_config.class.getResource("/images/loading.gif")));
    iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loadingFrame.add(iconLabel, BorderLayout.CENTER);

    JLabel loadingText = new JLabel("Logging in, please wait...");
    loadingText.setHorizontalAlignment(SwingConstants.CENTER);
    loadingFrame.add(loadingText, BorderLayout.NORTH);

    loadingFrame.setVisible(true);

    new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            Thread.sleep(2000); 
            return null;
        }

        @Override
        protected void done() {
            loadingFrame.dispose();      
            currentFrame.dispose();   
            nextFrame.setLocationRelativeTo(null);
            nextFrame.setVisible(true);  
        }
    }.execute();
}
    
    public static void StyleToggleButtons(JToggleButton button) {
        button.setFocusPainted(false);     
        button.setBorderPainted(false);
        button.setContentAreaFilled(true); 
        button.setOpaque(true);        

        Color defaultBg = new Color(240, 240, 240);
        Color selectedBg = new Color(30, 144, 255);

        button.setBackground(defaultBg);
        button.setForeground(Color.BLACK);

        button.addChangeListener(e -> {
            if (button.isSelected()) {
                button.setBackground(selectedBg);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(defaultBg);
                button.setForeground(Color.BLACK);
            }
        });

        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
    }
    
    public static final void BtnStyle(JButton btn){
        Font customFont = new Font("Arial", Font.BOLD, 14);
        btn.setFont(customFont);
        btn.setForeground(Color.BLACK);
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
    }
    
    private List<JCheckBoxMenuItem> menuItems = new ArrayList<>();

    public void setupCategoryPopup(JButton triggerButton, List<String> categories) {
        JPopupMenu categoryPopup = new JPopupMenu();
        
        

        for (String cat : categories) {
            
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(cat);

            item.setFont(new Font("Arial", Font.PLAIN, 14));
            item.setOpaque(true);
            item.setBackground(Color.WHITE);
            item.setForeground(Color.BLACK);

            item.addActionListener(e -> {
                updateButtonLabel(triggerButton, menuItems);
            });

            categoryPopup.add(item);
            menuItems.add(item);
        }
        
        buttonMenuItemsMap.put(triggerButton, menuItems);

        triggerButton.addActionListener(e -> {
            categoryPopup.show(triggerButton, 0, triggerButton.getHeight());
        });

        BtnStyle(triggerButton);
    }

    private void updateButtonLabel(JButton triggerButton, List<JCheckBoxMenuItem> menuItems) {
        List<String> selected = new ArrayList<>();
        for (JCheckBoxMenuItem item : menuItems) {
            if (item.isSelected()) {
                selected.add(item.getText());
            }
        }

        if (selected.isEmpty()) {
            triggerButton.setText("Select Categories");
        } else {
            triggerButton.setText(String.join(", ", selected));
        }
    }
    
    public void resetAllButtons() {
    for (Map.Entry<JButton, List<JCheckBoxMenuItem>> entry : buttonMenuItemsMap.entrySet()) {
        JButton btn = entry.getKey();
        List<JCheckBoxMenuItem> menuItem = entry.getValue();

        // Uncheck all checkboxes
        for (JCheckBoxMenuItem item : menuItem) {
            item.setSelected(false);
        }

        // Reset button text to original (you can customize this)
        btn.setText(getOriginalLabelForButton(btn));
    }
}
    
    private Map<JButton, String> originalLabels = new HashMap<>();

    public void storeOriginalLabels(JButton... buttons) {
        for (JButton btn : buttons) {
            originalLabels.put(btn, btn.getText());
        }
    }

    private String getOriginalLabelForButton(JButton btn) {
        return originalLabels.getOrDefault(btn, "Select Categories");
    }
    
    public static void BuyTimeLoaddingFrame() {
    JFrame loadingFrame = new JFrame();
    loadingFrame.setSize(300, 150);
    loadingFrame.setUndecorated(true);
    loadingFrame.setLocationRelativeTo(null);
    loadingFrame.setLayout(new BorderLayout());

    JLabel iconLabel = new JLabel(new ImageIcon(LogReg_config.class.getResource("/images/loading.gif")));
    iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loadingFrame.add(iconLabel, BorderLayout.CENTER);

    JLabel loadingText = new JLabel("Loading Inputs...");
    loadingText.setHorizontalAlignment(SwingConstants.CENTER);
    loadingFrame.add(loadingText, BorderLayout.NORTH);

    loadingFrame.setVisible(true);

    new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            Thread.sleep(4000); 
            return null;
        }

        @Override
        protected void done() {
            loadingFrame.dispose();      
          
        }
    }.execute();
}
    
    Border redBorder   = BorderFactory.createLineBorder(Color.RED, 2);
    Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
    Border grayBorder  = BorderFactory.createLineBorder(Color.GRAY, 1);
    Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 1);
    
    public boolean validateRequired(JTextField field) {
        if (field.getText().trim().isEmpty()) {
            field.setBorder(redBorder);
            return false;
        } else {
            field.setBorder(greenBorder);
            return true;
        }
    }
    
    public boolean validateEmail(JTextField field) {
        String email = field.getText().trim();
        if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            field.setBorder(greenBorder);
            return true;
        } else {
            field.setBorder(redBorder);
            return false;
        }
    }
    
    public int calculatePasswordStrength(String password, JProgressBar bar) {
        int strength = 0;

        if (password.length() >= 6) strength += 20; 
        if (password.length() >= 10) strength += 20; 
        if (password.matches(".*[a-z].*")) strength += 20; 
        if (password.matches(".*[A-Z].*")) strength += 20; 
        if (password.matches(".*[0-9].*")) strength += 10; 
        if (password.matches(".*[!@#$%^&*()].*")) strength += 10; 
        
        if (strength > 100) strength = 100;
        if (strength < 0) strength = 0;

        bar.setValue(strength);
        return strength;
    }



}


