/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import configuration.Validations;
import configuration.animation;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.border.Border;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author USER17
 */
public final class User_Details extends javax.swing.JFrame {

    /**
     * Creates new form User_Details
     */
    private static boolean enable;
    private final String username;
    private final int badge;
    private final String hashedPassword;
    
    public User_Details(String username, int badge, String hashedPassword) {
        User_config userconf = new User_config();
        
            this.username = username;
            this.badge = badge;
            this.hashedPassword = hashedPassword;
            
        initComponents();
        Gender();
        userconf.Age(age);
        userconf.Education(educationBox);
        TextInput();
        userconf.loadAllCountries(country);
        setBorder();
        UpdateBar();
        
    }
    
    public void GetComboBoxItem(JComboBox box){
        
    }
    
    private void UpdateBar(){
        animation ani = new animation();
        
        String password = pass.getText();
        int strength = ani.calculatePasswordStrength(password, passStrength);
        ChangeBarLabel(strength);
    }
    
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
    Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
    Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
    
    private void setBorder(){
        
        name.setBorder(redBorder);
        gender.setBorder(redBorder);
        agePane.setBorder(redBorder);
        educationPane.setBorder(redBorder);
        email.setBorder(redBorder);
        number1.setBorder(redBorder);
        BirthPane.setBorder(redBorder);
        number.setBorder(redBorder);
        countryPane.setBorder(redBorder);
        nm.setBorder(greenBorder);
        bdg.setBorder(greenBorder);
        pass.setBorder(greenBorder);
    }
    
    private void ChangeBarLabel(int value){   
        
        passStrength.setOpaque(true);
        passStrength.setBorderPainted(false);
        passStrength.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        passStrength.setBackground(Color.LIGHT_GRAY);
        
        if(value <= 10 && value >= 1){
            PassLabel.setText("Poor");
            PassLabel.setForeground(new Color(204,0,51));
            passStrength.setForeground(new Color(153,0,0));
        }else if(value <= 30 && value >= 11){
            PassLabel.setText("Low");
            PassLabel.setForeground(new Color(255,153,0));
            passStrength.setForeground(new Color(255,153,0));
        }else if(value <= 50 && value >= 31){
            PassLabel.setText("Good");
            PassLabel.setForeground(new Color(255,255,0));
            passStrength.setForeground(new Color(255,255,51));
        }else if(value <= 75 && value >= 51){
            PassLabel.setText("Perfect");
            PassLabel.setForeground(new Color(204,255,204));
            passStrength.setForeground(new Color(0,255,0));
        }else if(value <= 99 && value >= 76){
            PassLabel.setText("Excellent");
            PassLabel.setForeground(new Color(0,204,51));
            passStrength.setForeground(new Color(153,255,204));
        }else if(value == 100){
            PassLabel.setText("Unique");
            PassLabel.setForeground(new Color(102,102,255));
            passStrength.setForeground(new Color(153,153,255));
        }else{
            PassLabel.setText("Vulnerable");
            PassLabel.setForeground(new Color(204,204,204));
            passStrength.setForeground(new Color(204,204,204));
        }
    }


    private Date selectedBirthdate;
    private JButton openDateBtn;
    
    public Date getSelectedBirthdate() {
        return selectedBirthdate;
    }

    private void openDateDialog() {
        
        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        JDialog dialog = new JDialog(frame, "Select Birthdate", true);

        JDateChooser chooser = new JDateChooser();
        chooser.setDateFormatString("yyyy-MM-dd");
        chooser.setMaxSelectableDate(new Date());

        JButton okBtn = new JButton("OK");

        okBtn.addActionListener((ActionEvent e) -> {
            selectedBirthdate = chooser.getDate();
            if (selectedBirthdate != null) {
                String formattedDate = new SimpleDateFormat("dd / MM / yyyy").format(selectedBirthdate);
                BirthField.setText(formattedDate);
            }
            dialog.dispose();
        });

        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.add(chooser);
        dialog.add(okBtn);

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        
        BirthPane.setBorder(greenBorder);
    }
    
    public void TextInput(){
        nm.setText(username);
        bdg.setText(String.valueOf(badge));
        pass.setText(hashedPassword);
    }
    
    public void Gender(){
        ButtonGroup menuGroup = new ButtonGroup();
        
        menuGroup.add(jToggleButton1);
        menuGroup.add(jToggleButton2);
        
        animation.StyleToggleButtons(jToggleButton2); 
        animation.StyleToggleButtons(jToggleButton1); 
        
        if(jToggleButton1.isFocusOwner()){
            gender.setBorder(greenBorder);
        }else if(jToggleButton2.isFocusOwner()){
            gender.setBorder(greenBorder);
        }
        
    }
    
    private void updateBirthPaneBorder() {
        String selected = BirthField.getText().trim();

        if (selected.isEmpty() || selected.equals("Select Birthdate")) {

            BirthPane.setBorder(redBorder);
        } else {

            String[] parts = selected.split("\\s*/\\s*"); 

            if (parts.length != 3 || parts[0].isEmpty() || parts[1].isEmpty() || parts[2].isEmpty()) {
                BirthPane.setBorder(orangeBorder);
            } else {
                try {
                    int month = Integer.parseInt(parts[0]);
                    int day = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);


                    if (month < 1 || month > 12 || day < 1 || day > 31 || year < 1900 || year > 2100) {
                        BirthPane.setBorder(orangeBorder);
                    } else {

                        BirthPane.setBorder(greenBorder);
                    }
                } catch (NumberFormatException e) {

                    BirthPane.setBorder(orangeBorder);
                }
            }
        }
    }
    
    private boolean CheckAllInputs() {
        
        Validations validation = new Validations();

    // Username / Badge / Password
        if (nm.getText() == null || nm.getText().trim().isEmpty() || nm.getText().equals("Username"))
            return false;

        if (bdg.getText() == null || bdg.getText().trim().isEmpty() || bdg.getText().equals("badge"))
            return false;

        if (pass.getText() == null || pass.getText().trim().isEmpty() || pass.getText().equals("Password"))
            return false;

        // Personal info
        if (name.getText().trim().isEmpty())
            return false;

        if (number1.getText().equals("Valid ID Number") || number1.getText().trim().isEmpty())
            return false;

        if (number.getText().equals("Phone Number") || number.getText().trim().isEmpty())
            return false;

        if (email.getText().equals("Email") || email.getText().trim().isEmpty() || validation.validateEmailBoolean(email))
            return false;

        if (BirthField.getText().equals("Select Birthdate"))
            return false;

        // ComboBoxes
        if (age.getSelectedItem().equals("Select Age"))
            return false;

        if (country.getSelectedItem().equals("Select Country"))
            return false;

        if (educationBox.getSelectedItem().equals("Select Educational Attainment"))
            return false;

        // Age logic (only AFTER safe checks)
        int selectedAge = Integer.parseInt(age.getSelectedItem().toString());
        if (selectedAge < 18)
            return false;

        // Gender
        if (!jToggleButton1.isSelected() && !jToggleButton2.isSelected())
            return false;

        return true; // ✅ ALL INPUTS VALID
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
        jPanel3 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nm = new javax.swing.JTextField();
        bdg = new javax.swing.JTextField();
        pass = new javax.swing.JTextField();
        number = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        number1 = new javax.swing.JTextField();
        BirthPane = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        BirthField = new javax.swing.JTextField();
        passStrength = new javax.swing.JProgressBar();
        jLabel5 = new javax.swing.JLabel();
        PassLabel = new javax.swing.JLabel();
        gender = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        educationPane = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        educationBox = new javax.swing.JComboBox<>();
        countryPane = new javax.swing.JPanel();
        country = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        agePane = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        age = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        jLabel1.setText("REGISTRATION DETAILS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(388, 388, 388)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        name.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        name.setForeground(new java.awt.Color(153, 153, 153));
        name.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        name.setText("Name");
        name.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nameMouseClicked(evt);
            }
        });
        name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameActionPerformed(evt);
            }
        });
        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameKeyReleased(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Log In Info");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        nm.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        nm.setForeground(new java.awt.Color(153, 153, 153));
        nm.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nm.setText("Username");
        nm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nmActionPerformed(evt);
            }
        });
        nm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nmKeyReleased(evt);
            }
        });

        bdg.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        bdg.setForeground(new java.awt.Color(153, 153, 153));
        bdg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        bdg.setText("Badge");
        bdg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bdgActionPerformed(evt);
            }
        });
        bdg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bdgKeyReleased(evt);
            }
        });

        pass.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        pass.setForeground(new java.awt.Color(153, 153, 153));
        pass.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pass.setText("Password");
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        pass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nm)
                    .addComponent(bdg, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pass, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(nm, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bdg, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        number.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        number.setForeground(new java.awt.Color(153, 153, 153));
        number.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        number.setText("Phone Number");
        number.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                numberMouseClicked(evt);
            }
        });
        number.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberActionPerformed(evt);
            }
        });
        number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                numberKeyReleased(evt);
            }
        });

        email.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        email.setForeground(new java.awt.Color(153, 153, 153));
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("Email");
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailMouseClicked(evt);
            }
        });
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailKeyReleased(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(153, 153, 153));
        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("COMPLETE");
        jLabel3.setOpaque(true);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });

        number1.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        number1.setForeground(new java.awt.Color(153, 153, 153));
        number1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        number1.setText("Valid ID Number");
        number1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                number1MouseClicked(evt);
            }
        });
        number1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                number1ActionPerformed(evt);
            }
        });
        number1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                number1KeyReleased(evt);
            }
        });

        BirthPane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel4.setText("Birthdate");
        BirthPane.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, -1));

        jLabel10.setBackground(new java.awt.Color(153, 153, 153));
        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Select");
        jLabel10.setOpaque(true);
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
        });
        BirthPane.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 70, 60));

        BirthField.setEditable(false);
        BirthField.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        BirthField.setText("Select Birthdate");
        BirthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BirthFieldActionPerformed(evt);
            }
        });
        BirthField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BirthFieldKeyTyped(evt);
            }
        });
        BirthPane.add(BirthField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 260, 50));

        passStrength.setForeground(new java.awt.Color(204, 204, 204));
        passStrength.setOpaque(true);

        jLabel5.setText("Account password strength: ");

        PassLabel.setBackground(new java.awt.Color(153, 153, 153));
        PassLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        PassLabel.setForeground(new java.awt.Color(204, 204, 204));
        PassLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PassLabel.setText("Null");
        PassLabel.setOpaque(true);

        gender.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jToggleButton1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jToggleButton1.setText("Female");
        jToggleButton1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jToggleButton1FocusGained(evt);
            }
        });
        gender.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 187, 44));

        jToggleButton2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jToggleButton2.setText("Male");
        jToggleButton2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jToggleButton2FocusGained(evt);
            }
        });
        gender.add(jToggleButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 183, 44));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel6.setText("Educational Attainment");

        educationBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        educationBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                educationBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout educationPaneLayout = new javax.swing.GroupLayout(educationPane);
        educationPane.setLayout(educationPaneLayout);
        educationPaneLayout.setHorizontalGroup(
            educationPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, educationPaneLayout.createSequentialGroup()
                .addContainerGap(110, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(113, 113, 113))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, educationPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(educationBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        educationPaneLayout.setVerticalGroup(
            educationPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, educationPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(educationBox, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        country.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        country.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel7.setText("Country");

        javax.swing.GroupLayout countryPaneLayout = new javax.swing.GroupLayout(countryPane);
        countryPane.setLayout(countryPaneLayout);
        countryPaneLayout.setHorizontalGroup(
            countryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(countryPaneLayout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, countryPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(country, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        countryPaneLayout.setVerticalGroup(
            countryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, countryPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(country, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel8.setText("Age");

        age.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        age.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout agePaneLayout = new javax.swing.GroupLayout(agePane);
        agePane.setLayout(agePaneLayout);
        agePaneLayout.setHorizontalGroup(
            agePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(agePaneLayout.createSequentialGroup()
                .addGap(176, 176, 176)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(agePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(age, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        agePaneLayout.setVerticalGroup(
            agePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, agePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(age, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(number)
                                .addComponent(number1)
                                .addComponent(educationPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(gender, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(countryPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(agePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BirthPane, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(passStrength, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(115, 115, 115))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(gender, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(educationPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(number, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(number1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(agePane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(142, 142, 142)
                                .addComponent(BirthPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(countryPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passStrength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(PassLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameActionPerformed

    private void nmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nmActionPerformed

    private void bdgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bdgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bdgActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passActionPerformed

    private void nameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(name, "Name");// TODO add your handling code here:
    }//GEN-LAST:event_nameMouseClicked

    private void numberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_numberMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(number, "Phone Number");        // TODO add your handling code here:
    }//GEN-LAST:event_numberMouseClicked

    private void numberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numberActionPerformed

    private void emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(email, "Email");         // TODO add your handling code here:
    }//GEN-LAST:event_emailMouseClicked

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked

        if (CheckAllInputs() == true) {
            
            
            new UserDashboard().setVisible(true);
            this.dispose();
            
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Some fields are invalid or Empty!. Please fix highlighted inputs.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered

            jLabel3.setBackground(new Color(102,102,102));   // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited

            jLabel3.setBackground(new Color(153,153,153)); // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseExited

    private void number1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_number1MouseClicked
        animation ani = new animation();

        ani.addPlaceholder(number1, "Valid ID Number");// TODO add your handling code here:
    }//GEN-LAST:event_number1MouseClicked

    private void number1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_number1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_number1ActionPerformed

    private void nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameKeyReleased
        animation ani = new animation();
        ani.validateRequired(name);        // TODO add your handling code here:
    }//GEN-LAST:event_nameKeyReleased

    private void numberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numberKeyReleased
        animation ani = new animation();
        ani.validateRequired(number);          // TODO add your handling code here:
    }//GEN-LAST:event_numberKeyReleased

    private void emailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailKeyReleased
        animation ani = new animation();
        ani.validateEmail(email);          // TODO add your handling code here:
    }//GEN-LAST:event_emailKeyReleased

    private void number1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_number1KeyReleased
        animation ani = new animation();
        ani.validateRequired(number1);         // TODO add your handling code here:
    }//GEN-LAST:event_number1KeyReleased

    private void passKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passKeyReleased
        animation ani = new animation();
        ani.validateRequired(pass); 
        
        pass.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }

            private void updateStrength() {
                String password = pass.getText();
                int strength = ani.calculatePasswordStrength(password, passStrength);
                ChangeBarLabel(strength);
            }
        });      // TODO add your handling code here:
    }//GEN-LAST:event_passKeyReleased

    private void nmKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nmKeyReleased
        animation ani = new animation();
        ani.validateRequired(nm);        // TODO add your handling code here:
    }//GEN-LAST:event_nmKeyReleased

    private void bdgKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bdgKeyReleased
        animation ani = new animation();
        Validations validate = new Validations();
        
        String badge = bdg.getText();
        
        int valid = validate.BadgeValidate(badge);
        
        switch (valid) {
            case 1:
                ani.validateRequired(bdg);
                break;
            case 0:
                bdg.setBorder(redBorder);
                break;
            case 3:
                bdg.setBorder(orangeBorder);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_bdgKeyReleased

    private void countryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryActionPerformed
        Object selected = country.getSelectedItem();

        if (selected == null || selected.toString().equals("Select Country")) {
            countryPane.setBorder(redBorder);
        } else {
            countryPane.setBorder(greenBorder);
        }       // TODO add your handling code here:
    }//GEN-LAST:event_countryActionPerformed

    private void educationBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_educationBoxActionPerformed
        Object selected = educationBox.getSelectedItem();

        if (selected == null || selected.toString().equals("Select Educational Attainment")) {
            educationPane.setBorder(redBorder);
        } else {
            educationPane.setBorder(greenBorder);
        }
   // TODO add your handling code here:
    }//GEN-LAST:event_educationBoxActionPerformed

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        jLabel10.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
        jLabel10.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseExited

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        openDateDialog();   // TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseClicked

    private void BirthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BirthFieldActionPerformed
        String selected = BirthField.getText();

        if (selected.equals("Select Birthdate")) {
            BirthPane.setBorder(redBorder);
        } else {
            BirthPane.setBorder(greenBorder);
        }       // TODO add your handling code here:
    }//GEN-LAST:event_BirthFieldActionPerformed

    private void jToggleButton1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jToggleButton1FocusGained
        gender.setBorder(greenBorder);// TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1FocusGained

    private void jToggleButton2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jToggleButton2FocusGained
        gender.setBorder(greenBorder);        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton2FocusGained

    private void ageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ageActionPerformed
        Object selected = age.getSelectedItem();

        if (selected == null) {
            return;
        }

        String selectedAge = selected.toString();
        
        if("Select Age".equals(selectedAge)){
            return;
        }
        
        int ageItem;
        try {
            ageItem = Integer.parseInt(selectedAge);
        } catch (NumberFormatException e) {
            return;
        }

        if (ageItem >= 18) {
            agePane.setBorder(greenBorder);
        } else {
            agePane.setBorder(orangeBorder);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_ageActionPerformed

    private void BirthFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BirthFieldKeyTyped
        BirthField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateBirthPaneBorder(); }
            public void removeUpdate(DocumentEvent e) { updateBirthPaneBorder(); }
            public void changedUpdate(DocumentEvent e) { updateBirthPaneBorder(); }
    });

         // TODO add your handling code here:
    }//GEN-LAST:event_BirthFieldKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(User_Details.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User_Details.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User_Details.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User_Details.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new User_Details().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BirthField;
    private javax.swing.JPanel BirthPane;
    private javax.swing.JLabel PassLabel;
    private javax.swing.JComboBox<String> age;
    private javax.swing.JPanel agePane;
    private javax.swing.JTextField bdg;
    private javax.swing.JComboBox<String> country;
    private javax.swing.JPanel countryPane;
    private javax.swing.JComboBox<String> educationBox;
    private javax.swing.JPanel educationPane;
    private javax.swing.JTextField email;
    private javax.swing.JPanel gender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JTextField name;
    private javax.swing.JTextField nm;
    private javax.swing.JTextField number;
    private javax.swing.JTextField number1;
    private javax.swing.JTextField pass;
    private javax.swing.JProgressBar passStrength;
    // End of variables declaration//GEN-END:variables
}
