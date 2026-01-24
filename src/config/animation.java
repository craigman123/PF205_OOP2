package config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.WindowAdapter;

public class animation {
    
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


}


