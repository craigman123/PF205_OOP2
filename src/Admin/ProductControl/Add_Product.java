/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.ProductControl;

import Admin.Admin_config;
import Profiles.session;
import configuration.Validations;
import configuration.animation;
import configuration.config;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author user
 */
public class Add_Product extends javax.swing.JInternalFrame {

    /**
     * Creates new form Add_Product
     */
    private static String imagePath;
    private static Image scaledImg;
    
    public Add_Product() {
        Admin_config admin = new Admin_config();
        
        initComponents();
        StyleFrame();
        admin.DisplayCategory(category);
        admin.DisplayRarity(rarity);
        admin.DisplayProdStatus(status);
        setBorder();
        Constructor();
    }
    
    public void Constructor(){
        Descript.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { updateCount(); }
                public void removeUpdate(DocumentEvent e) { updateCount(); }
                public void changedUpdate(DocumentEvent e) { updateCount(); }

                int Datalength;

                private void updateCount() {
                    Datalength = Descript.getText().length();
                        if(!Descript.getText().equals("Description . . .")){
                            Datalength = 0;
                        }
                        
                        if (Datalength > 300) {
                            Descript.setText(Descript.getText().substring(0, 300));
                        }

                        jLabel6.setText(Datalength + "/300");
                    }
            }); 
    }
    
    Border grayBorder = BorderFactory.createLineBorder(Color.GRAY, 2);
    Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
    Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
    
    public void StyleFrame(){
        this.setBorder(null);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
    }
    
    private File selectedImageFile;

    private void FileChooser() {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "jpeg", "webp");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            imagePath = selectedImageFile.getAbsolutePath();

            ImageIcon icon = new ImageIcon(imagePath);
            
            System.out.println("ImagePath: " + imagePath);
            
            Image img = icon.getImage();
            scaledImg = img.getScaledInstance(
                    imageUpload.getWidth(),
                    imageUpload.getHeight(),
                    Image.SCALE_SMOOTH);

            imageUpload.setIcon(new ImageIcon(scaledImg));
            imageUpload.setText("");
            imageUpload.setBorder(greenBorder);
        }
    }

    public void saveImageFileandInputs(File selectedImageFile) throws IOException {
        if (selectedImageFile != null && selectedImageFile.exists()) {
            File destFolder = new File("Input_Images");

            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            File destFile = new File(destFolder, selectedImageFile.getName());
            Files.copy(selectedImageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Image saved to: " + destFile.getAbsolutePath());
        } else {
            System.out.println("No image file selected or file does not exist.");
        }

        String Prname = Prodname.getText();
        String stat = (String) status.getSelectedItem();
        String cat = (String) category.getSelectedItem();
        String rare = (String) rarity.getSelectedItem();
        String cash = price.getText();
        String stock = ProdStock.getText();
        String descrip = Descript.getText();
        String imagePathForDB = (selectedImageFile != null) ? "Input_Images/" + selectedImageFile.getName() : null;
        
        session see = new session();
        config conf = new config();
        
        int id = see.GetID();
        
        String qry = "SELECT user_name FROM users WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, id); 
        
        java.util.Map<String, Object> user = result.get(0);
        String getAddedBy = user.get("user_name").toString();

        String sql = "INSERT INTO products(prod_name, prod_category, prod_price, prod_rarity,"
                + " prod_descript, prod_stock, prod_addedBy, prod_status, prod_image) VALUES(?,?,?,?,?,?,?,?,?)";
        conf.addRecordAndReturnId(sql, Prname, cat, cash, rare, descrip, stock, getAddedBy, stat, imagePathForDB);
        
        ResetInputs();
        
        JOptionPane.showMessageDialog(
            null,
            "Product Successfully Created!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public void ResetInputs(){
        Prodname.setText("Product Name");
        price.setText("Price");
        ProdStock.setText("Stock");
        category.setSelectedIndex(0);
        rarity.setSelectedIndex(0);
        status.setSelectedIndex(0);
        Descript.setText("Description . . .");
        imageUpload.setIcon(null);
        imageUpload.setText("Upload Image");
        
        JComponent[] setBorder = {Prodname, ProdStock, price, imageUpload};

        for (JComponent comp : setBorder) {
            comp.setBorder(grayBorder);
        }
    }
    
    public boolean Validate(){
        StringBuilder error = new StringBuilder();

        String Prname = Prodname.getText();
        String stat = (String) status.getSelectedItem();
        String cat = (String) category.getSelectedItem();
        String rare = (String) rarity.getSelectedItem();
        String cash = price.getText();
        String stock = ProdStock.getText();
        String descrip = Descript.getText();
        

        if (Prname.equals("Product Name") || Prname.isEmpty()) {
            error.append("- Please enter a product name.\n");
        }

        if (stat.equals("Status") || stat.isEmpty()) {
            error.append("- Please select a status.\n");
        }

        if (cat.equals("Category") || cat.isEmpty()) {
            error.append("- Please select a category.\n");
        }

        if (rare.equals("Rarity") || rare.isEmpty()) {
            error.append("- Please select a rarity.\n");
        }

        if (cash.equals("Price") || cash.isEmpty()) {
            error.append("- Please enter a price.\n");
        } else if (!cash.matches("\\d+(\\.\\d{1,2})?")) {
            error.append("- Price must be a valid number.\n");
        }

        if (stock.equals("Stock") || stock.isEmpty()) {
            error.append("- Please enter stock quantity.\n");
        } else if (!stock.matches("\\d+")) { // must be integer
            error.append("- Stock must be a valid integer.\n");
        }

        if (selectedImageFile == null) {
            error.append("- Please select a product image.\n");
        }
        
        if (imageUpload.getIcon() == null) {
            error.append("No Image Attached.\n");
        }


        if (error.length() > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    error.toString(), 
                    "Input Errors", 
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        return true;
    }
    
    public void setBorder(){
        category.setBorder(greenBorder);
        rarity.setBorder(greenBorder);
        status.setBorder(greenBorder);
        imageUpload.setBorder(redBorder);
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
        Prodname = new javax.swing.JTextField();
        category = new javax.swing.JComboBox<>();
        ProdStock = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        price = new javax.swing.JTextField();
        rarity = new javax.swing.JComboBox<>();
        status = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        Descript = new javax.swing.JTextArea();
        imageUpload = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setText("ADD PRODUCTS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(209, 209, 209))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        Prodname.setEditable(false);
        Prodname.setForeground(new java.awt.Color(102, 102, 102));
        Prodname.setText("Product Name");
        Prodname.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProdnameMouseClicked(evt);
            }
        });
        Prodname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProdnameActionPerformed(evt);
            }
        });
        Prodname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ProdnameKeyReleased(evt);
            }
        });

        category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryActionPerformed(evt);
            }
        });

        ProdStock.setForeground(new java.awt.Color(102, 102, 102));
        ProdStock.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ProdStock.setText("Stock");
        ProdStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProdStockMouseClicked(evt);
            }
        });
        ProdStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProdStockActionPerformed(evt);
            }
        });
        ProdStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ProdStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ProdStockKeyTyped(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(153, 153, 153));
        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("-");
        jLabel2.setOpaque(true);
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(153, 153, 153));
        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("+");
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

        price.setEditable(false);
        price.setForeground(new java.awt.Color(102, 102, 102));
        price.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        price.setText("Price");
        price.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                priceMouseClicked(evt);
            }
        });
        price.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceActionPerformed(evt);
            }
        });
        price.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                priceKeyReleased(evt);
            }
        });

        rarity.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        rarity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rarityActionPerformed(evt);
            }
        });
        rarity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rarityKeyReleased(evt);
            }
        });

        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusActionPerformed(evt);
            }
        });

        Descript.setEditable(false);
        Descript.setColumns(20);
        Descript.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        Descript.setForeground(new java.awt.Color(102, 102, 102));
        Descript.setRows(5);
        Descript.setText("Description . . .");
        Descript.setWrapStyleWord(true);
        Descript.setAutoscrolls(false);
        Descript.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DescriptMouseClicked(evt);
            }
        });
        Descript.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DescriptKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(Descript);

        imageUpload.setBackground(new java.awt.Color(204, 204, 204));
        imageUpload.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        imageUpload.setForeground(new java.awt.Color(102, 102, 102));
        imageUpload.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageUpload.setText("Upload Image");
        imageUpload.setOpaque(true);
        imageUpload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageUploadMouseClicked(evt);
            }
        });
        imageUpload.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                imageUploadKeyReleased(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(153, 153, 153));
        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 204, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CONFIRM");
        jLabel4.setOpaque(true);
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(153, 153, 153));
        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("RESET");
        jLabel5.setOpaque(true);
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        jLabel6.setText("300/300");
        jLabel6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel6KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ProdStock, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(Prodname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(price)
                            .addComponent(category, 0, 126, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(rarity, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(status, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(146, 146, 146)
                                .addComponent(jLabel6))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imageUpload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Prodname, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(price, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ProdStock, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rarity, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imageUpload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ProdStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProdStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProdStockActionPerformed

    private void categoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryActionPerformed
   // TODO add your handling code here:
    }//GEN-LAST:event_categoryActionPerformed

    private void priceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceActionPerformed

    private void rarityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rarityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rarityActionPerformed

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusActionPerformed

    private void imageUploadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageUploadMouseClicked
        FileChooser();        // TODO add your handling code here:
    }//GEN-LAST:event_imageUploadMouseClicked

    private void ProdnameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProdnameMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(Prodname, "Product Name");    // TODO add your handling code here:
    }//GEN-LAST:event_ProdnameMouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        String text = ProdStock.getText();
        int number = 0;

        try{
            if (text.equals("Stock") || text.isEmpty()) {
                number = 0;
            } else {
                number = Integer.parseInt(text);
            }
        }catch(NumberFormatException e){
            ProdStock.setText("Stock");
        }

        if(number < 9999){
            number++;  
        }
        
        ProdStock.setText(String.valueOf(number));      // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        String text = ProdStock.getText();
        int number = 0;

        try{
            if (text.equals("Stock") || text.isEmpty()) {
                number = 0;
            } else {
                number = Integer.parseInt(text);
            }
        }catch(NumberFormatException e){
            ProdStock.setText("Stock");
        }

        if (number > 0) {
            number--;
        }

        ProdStock.setText(String.valueOf(number));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void ProdStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProdStockKeyTyped
        char c = evt.getKeyChar();
    
        if (!Character.isDigit(c)) {
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_ProdStockKeyTyped

    private void priceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_priceMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(price, "Price");        // TODO add your handling code here:
    }//GEN-LAST:event_priceMouseClicked

    private void ProdStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProdStockMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(ProdStock, "Stock");// TODO add your handling code here:
    }//GEN-LAST:event_ProdStockMouseClicked

    private void DescriptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DescriptMouseClicked
        animation ani = new animation();

        ani.addPlaceholderTextArea(Descript, "Description . . .");        // TODO add your handling code here:
    }//GEN-LAST:event_DescriptMouseClicked

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        jLabel3.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setBackground(new Color(153,153,153));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setBackground(new Color(153,153,153));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setBackground(new Color(102,102,102));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        jLabel5.setBackground(new Color(102,102,102));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        jLabel5.setBackground(new Color(153,153,153));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        try {
            boolean valid = Validate();

            if(valid){
                saveImageFileandInputs(selectedImageFile);
            }// TODO add your handling code here:
        } catch (IOException ex) {
            Logger.getLogger(Add_Product.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        jLabel4.setBackground(new Color(102,102,102));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        jLabel4.setBackground(new Color(153,153,153));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseExited

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        ResetInputs();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseClicked

    private void ProdnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProdnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProdnameActionPerformed

    private void ProdnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProdnameKeyReleased
        animation ani = new animation();
        Validations validate = new Validations();

        String name = Prodname.getText();
        
        if(!name.equals("Product Name")){
            Prodname.setBorder(greenBorder);
        }else{
            Prodname.setBorder(grayBorder);
        }

     // TODO add your handling code here:
    }//GEN-LAST:event_ProdnameKeyReleased

    private void ProdStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProdStockKeyReleased
        String stockIn = ProdStock.getText();

        if(!stockIn.equals("Stock")){        // TODO add your handling code here:
            ProdStock.setBorder(greenBorder);
        }else{
            ProdStock.setBorder(grayBorder);
        }
    }//GEN-LAST:event_ProdStockKeyReleased

    private void priceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_priceKeyReleased
                // TODO add your handling code here:
    }//GEN-LAST:event_priceKeyReleased

    private void rarityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rarityKeyReleased
    // TODO add your handling code here:
    }//GEN-LAST:event_rarityKeyReleased

    private void imageUploadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imageUploadKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_imageUploadKeyReleased

    private void jLabel6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel6KeyTyped
        

    }//GEN-LAST:event_jLabel6KeyTyped

    private void DescriptKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DescriptKeyTyped
               // TODO add your handling code here:
    }//GEN-LAST:event_DescriptKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Descript;
    private javax.swing.JTextField ProdStock;
    private javax.swing.JTextField Prodname;
    private javax.swing.JComboBox<String> category;
    private javax.swing.JLabel imageUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField price;
    private javax.swing.JComboBox<String> rarity;
    private javax.swing.JComboBox<String> status;
    // End of variables declaration//GEN-END:variables
}
