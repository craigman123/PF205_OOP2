/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.ProductControl;

import Admin.Admin_config;
import configuration.animation;
import configuration.config;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author user
 */
public final class Refactor_Product extends javax.swing.JInternalFrame {

    /**
     * Creates new form Refactor_Product
     */
    private int ID;
    
    public Refactor_Product() {
        Admin_config adminconf = new Admin_config();
        
        initComponents();
        StyleFrame();
        adminconf.DisplayCategory(category);
        adminconf.DisplayRarity(rarity);
        adminconf.DisplayProdStatus(status);
        Renderer();
        int ProdID = (int) connector.datasender();
        
        this.ID = ProdID;
        AutoGet();
    }
    
    public void AutoGet(){
        id.setText(String.valueOf(ID));
        SwingUtilities.invokeLater(() -> {
            GetInfo();
        });
        
    }
    
    public void Renderer(){
        status.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                Component c = super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (value != null) {
                    String text = value.toString();
                    switch (text) {
                        case "Active": c.setForeground(Color.GREEN); break;
                        case "Inactive": c.setForeground(Color.GRAY); break;
                        case "Pending": c.setForeground(Color.ORANGE); break;
                        case "Suspended": c.setForeground(Color.RED); break;
                        case "Sold Out": c.setForeground(Color.MAGENTA); break;
                        case "Archived": c.setForeground(Color.BLUE); break;
                        default: c.setForeground(Color.BLACK);
                    }
                }

                return c;
            }
        });
        
        category.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                Component c = super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (value != null) {
                    String text = value.toString();
                    switch (text) {
                        case "Pistol": c.setForeground(Color.GREEN); break;
                        case "Rifle": c.setForeground(new Color(102,102,0)); break;
                        case "Sniper Rifle": c.setForeground(Color.ORANGE); break;
                        case "Shotgun": c.setForeground(Color.RED); break;
                        case "Sub Machine Gun": c.setForeground(Color.MAGENTA); break;
                        case "Machine Gun": c.setForeground(Color.BLUE); break;
                        default: c.setForeground(Color.BLACK);
                    }
                }

                return c;
            }
        });
        
        rarity.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                Component c = super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (value != null) {
                    String text = value.toString();
                    switch (text) {
                        case "Common": c.setForeground(Color.GRAY); break;
                        case "Uncommon": c.setForeground(Color.GREEN); break;
                        case "Rare": c.setForeground(Color.BLUE); break;
                        case "Exquisite": c.setForeground(Color.CYAN); break;
                        case "Unique": c.setForeground(Color.decode("#E6E6FA")); break;
                        case "Collectors Choice": c.setForeground(Color.decode("#FFD700")); break;
                        case "Antique": c.setForeground(Color.RED); break;
                        default: c.setForeground(Color.BLACK);
                    }
                }

                return c;
            }
        });

    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    private static String imgPath;
    
    public void GetInfo(){
        config conf = new config();
        
        int FinalId = 0;
        String Prodid = id.getText();
        
        try{
            FinalId = Integer.parseInt(Prodid);
            
            String qry = "SELECT * FROM products WHERE prod_id = ?";
            java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, FinalId); 
            
            if(!result.isEmpty()){
                java.util.Map<String, Object> prod = result.get(0);
                String name = prod.get("prod_name").toString();
                String cat = prod.get("prod_category").toString();
                String rare = prod.get("prod_rarity").toString();
                String cash = prod.get("prod_price").toString();
                String desc = prod.get("prod_descript").toString();
                String stock = prod.get("prod_stock").toString();
                String stat = prod.get("prod_status").toString();
                String DataBaseimagePath = "";
                
                try {
                DataBaseimagePath = prod.get("prod_image").toString();

                File file = new File(
                    System.getProperty("user.dir") + File.separator +
                    "Input_Images" + File.separator +
                    DataBaseimagePath
                );

                System.out.println(file.getAbsolutePath());
                System.out.println("Exists: " + file.exists());

                ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
                Image img = originalIcon.getImage();
                
                int imgWidth = originalIcon.getIconWidth();
                int imgHeight = originalIcon.getIconHeight();
                
                int labelWidth = imageUpload.getSize().width;
                int labelHeight = imageUpload.getSize().height;
                
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
                imgPath = DataBaseimagePath;
                currentImagePathFromDatabase = imgPath;
                imageChanged = false;
                selectedImageFile = null;
                
                prodName.setText(name);
                ProdStock.setText(stock);
                price.setText(cash);
                rarity.setSelectedItem(rare);
                category.setSelectedItem(cat);
                status.setSelectedItem(stat);
                Descript.setText(desc);
                
                
            }else{
                JOptionPane.showMessageDialog(
                    null,
                    "ID does not exist!",
                    "ID not found",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }catch(NumberFormatException e){
            if(Prodid.equals("Enter ID . . .") || Prodid.isEmpty()){
                JOptionPane.showMessageDialog(
                    null,
                    "Enter Valid Product ID",
                    "Empty Fields",
                    JOptionPane.ERROR_MESSAGE
                );
            }else{
                JOptionPane.showMessageDialog(
                    null,
                    "ID should be Integer!",
                    "Input error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
        
    public void ResetInputs() {

        id.setText("Enter ID . . .");
        prodName.setText("Product Name");
        ProdStock.setText("Stock");
        price.setText("Price");
        Descript.setText("Description . . .");

        if (category.getItemCount() > 0) {
            category.setSelectedIndex(0);
        }

        if (rarity.getItemCount() > 0) {
            rarity.setSelectedIndex(0);
        }

        if (status.getItemCount() > 0) {
            status.setSelectedIndex(0);
        }

        imageUpload.setIcon(null);
        imageUpload.setText("No Image");
        imageUpload.setBorder(grayBorder);
    }
    
    private boolean validateInputs() {
        StringBuilder error = new StringBuilder();
        config conf = new config();

        String idText = id.getText().trim();
        String name = prodName.getText().trim();
        String priceText = price.getText().trim();
        String stockText = ProdStock.getText().trim();
        String desc = Descript.getText().trim();

        if (idText.isEmpty() || idText.equals("Enter ID . . .")) {
            error.append("• Product ID is required.\n");
        } else {
            try {
                int Userid = Integer.parseInt(idText);
                
                String qry = "SELECT * FROM products WHERE prod_id = ?";
                java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, Userid); 
                
                if(result.isEmpty()){
                   error.append("User ID not found!\n"); 
                }
                
            } catch (NumberFormatException e) {
                error.append("• Product ID must be a valid integer.\n");
            }
        }

        if (name.isEmpty()) {
            error.append("• Product Name cannot be empty.\n");
        }

        if (priceText.isEmpty()) {
            error.append("• Price cannot be empty.\n");
        } else {
            try {
                double priceValue = Double.parseDouble(priceText);
                if (priceValue < 0) {
                    error.append("• Price cannot be negative.\n");
                }
            } catch (NumberFormatException e) {
                error.append("• Price must be a valid number.\n");
            }
        }

        if (stockText.isEmpty()) {
            error.append("• Stock cannot be empty.\n");
        } else {
            try {
                int stockValue = Integer.parseInt(stockText);
                if (stockValue < 0) {
                    error.append("• Stock cannot be negative.\n");
                }
            } catch (NumberFormatException e) {
                error.append("• Stock must be a valid integer.\n");
            }
        }

        if (desc.isEmpty()) {
            error.append("• Description cannot be empty.\n");
        }

        if (error.length() > 0) {
            JOptionPane.showMessageDialog(
                    null,
                    error.toString(),
                    "Validation Errors",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }
    
    private File selectedImageFile = null;
    private boolean imageChanged = false;
    private String currentImagePathFromDatabase = "";
    
    public void saveChanges() throws Exception {
    config conf = new config();

    String prodId = id.getText().trim();
    String name = prodName.getText().trim();
    String cat = category.getSelectedItem().toString();
    String rare = rarity.getSelectedItem().toString();
    String cash = price.getText().trim();
    String desc = Descript.getText().trim();
    String stock = ProdStock.getText().trim();
    String stat = status.getSelectedItem().toString();
    String finalFileName = currentImagePathFromDatabase;

    // Handle image change
    if (imageChanged && selectedImageFile != null) {
        String extension = selectedImageFile.getName()
                             .substring(selectedImageFile.getName().lastIndexOf("."))
                             .toLowerCase();
        if (!extension.equals(".jpg") && !extension.equals(".jpeg") &&
            !extension.equals(".png") && !extension.equals(".webp")) {
            JOptionPane.showMessageDialog(null, "Invalid image type!");
            return;
        }
        String uniqueName = conf.generateHashedName();
        finalFileName = uniqueName + extension;
        saveImageToFolder(selectedImageFile, uniqueName, extension);
        System.out.println("Image changed: " + finalFileName);
    }

    // Validate image path
    if (finalFileName == null || !finalFileName.contains(".")) {
        JOptionPane.showMessageDialog(null, "Invalid image path!");
        return;
    }

    // Fetch current product data from DB
    List<Map<String, Object>> records = conf.fetchRecords(
        "SELECT * FROM products WHERE prod_id = ?", prodId
    );

    if (records.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Product not found!");
        return;
    }

    Map<String, Object> currentProd = records.get(0);

    // Check if any field actually changed
    boolean changed = false;
    if (!name.equals(currentProd.get("prod_name"))) changed = true;
    if (!cat.equals(currentProd.get("prod_category"))) changed = true;
    if (!cash.equals(currentProd.get("prod_price").toString())) changed = true;
    if (!rare.equals(currentProd.get("prod_rarity"))) changed = true;
    if (!desc.equals(currentProd.get("prod_descript"))) changed = true;
    if (!stock.equals(currentProd.get("prod_stock").toString())) changed = true;
    if (!stat.equals(currentProd.get("prod_status"))) changed = true;
    if (!finalFileName.equals(currentProd.get("prod_image"))) changed = true;

    if (!changed) {
        JOptionPane.showMessageDialog(null, "No changes detected.");
        return;
    }
    
    if(!stock.equals("0")) {
        stat = "Active";
    }

    // Update the product
    String qry = "UPDATE products SET prod_name = ?, prod_category = ?, prod_price = ?, "
               + "prod_rarity = ?, prod_descript = ?, prod_stock = ?, prod_status = ?, "
               + "prod_image = ? WHERE prod_id = ?";

    int rowsAffected = conf.updateRecord(qry, name, cat, cash, rare, desc, stock, stat, finalFileName, prodId);
    
    LocalDateTime now = LocalDateTime.now();
    Timestamp date = Timestamp.valueOf(now);
    String queryNow = "INSERT INTO logs(prod_id, user_id, dateTime, log_action) VALUES(?,?,?,?)";
    conf.addRecordAndReturnId(queryNow, prodId, ID, date, "Update");

    if (rowsAffected > 0) {
        JOptionPane.showMessageDialog(null, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        ResetInputs();
    } else {
        JOptionPane.showMessageDialog(null, "Update failed!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    public String saveImageToFolder(File sourceFile, String uniqueName, String extension) {
        try {
            String projectPath = System.getProperty("user.dir");
            File folder = new File(projectPath + "/Input_Images");

            if (!folder.exists()) {
                folder.mkdir();
            }

            String newFileName = uniqueName + extension;
            File destinationFile = new File(folder, newFileName);

            Files.copy(sourceFile.toPath(),
                       destinationFile.toPath(),
                       StandardCopyOption.REPLACE_EXISTING);
        
            return newFileName;

        } catch (Exception e) {
            e.printStackTrace();
            return currentImagePathFromDatabase; 
        }
    }
    
    Border grayBorder = BorderFactory.createLineBorder(Color.GRAY, 2);
    Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
    Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
    
    private static String imagePath;
    private static Image scaledImg;
    
    private void FileChooser() {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "jpeg", "webp");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            imagePath = selectedImageFile.getAbsolutePath();
            imageChanged = true;

            ImageIcon icon = new ImageIcon(imagePath);
            
            System.out.println("ImagePath: " + imagePath);
            
            Image img = icon.getImage();
            scaledImg = img.getScaledInstance(
                    imageUpload.getWidth(),
                    imageUpload.getHeight(),
                    Image.SCALE_SMOOTH);

            imageUpload.setIcon(new ImageIcon(scaledImg));
            imageUpload.setText("");
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        prodName = new javax.swing.JTextField();
        rarity = new javax.swing.JComboBox<>();
        category = new javax.swing.JComboBox<>();
        status = new javax.swing.JComboBox<>();
        ProdStock = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        price = new javax.swing.JTextField();
        imageUpload = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Descript = new javax.swing.JTextArea();

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setText("REFACTOR PRODUCT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        id.setEditable(false);
        id.setForeground(new java.awt.Color(102, 102, 102));
        id.setText("Enter ID . . .");
        id.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                idMouseClicked(evt);
            }
        });
        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(153, 153, 153));
        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Get Info");
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

        prodName.setEditable(false);
        prodName.setForeground(new java.awt.Color(153, 153, 153));
        prodName.setText("Product Name");
        prodName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prodNameMouseClicked(evt);
            }
        });
        prodName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodNameActionPerformed(evt);
            }
        });

        rarity.setBackground(new java.awt.Color(204, 204, 204));
        rarity.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        rarity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rarityActionPerformed(evt);
            }
        });

        category.setBackground(new java.awt.Color(153, 153, 153));
        category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryActionPerformed(evt);
            }
        });

        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusActionPerformed(evt);
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

        jLabel3.setBackground(new java.awt.Color(153, 153, 153));
        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("-");
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

        jLabel4.setBackground(new java.awt.Color(153, 153, 153));
        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("+");
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

        price.setForeground(new java.awt.Color(153, 153, 153));
        price.setText("Price");
        price.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                priceMouseClicked(evt);
            }
        });

        imageUpload.setBackground(new java.awt.Color(153, 153, 153));
        imageUpload.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        imageUpload.setForeground(new java.awt.Color(102, 102, 102));
        imageUpload.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageUpload.setText("No Image");
        imageUpload.setOpaque(true);
        imageUpload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageUploadMouseClicked(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(153, 153, 153));
        jLabel6.setForeground(new java.awt.Color(0, 255, 102));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("CONFIRM");
        jLabel6.setOpaque(true);
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(153, 153, 153));
        jLabel7.setForeground(new java.awt.Color(255, 51, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("RESET");
        jLabel7.setOpaque(true);
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        Descript.setColumns(20);
        Descript.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        Descript.setForeground(new java.awt.Color(153, 153, 153));
        Descript.setRows(5);
        Descript.setText("Description . . .");
        jScrollPane1.setViewportView(Descript);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(id)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(prodName, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rarity, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(category, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(price)
                            .addComponent(imageUpload, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 10, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProdStock, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(status, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(8, 8, 8))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(id, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prodName, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rarity, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProdStock, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageUpload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prodNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prodNameActionPerformed

    private void rarityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rarityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rarityActionPerformed

    private void categoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryActionPerformed

    private void ProdStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProdStockMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(ProdStock, "Stock");// TODO add your handling code here:
    }//GEN-LAST:event_ProdStockMouseClicked

    private void ProdStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProdStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProdStockActionPerformed

    private void ProdStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProdStockKeyReleased

    }//GEN-LAST:event_ProdStockKeyReleased

    private void ProdStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProdStockKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_ProdStockKeyTyped

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

        if (number > 0) {
            number--;
        }

        ProdStock.setText(String.valueOf(number));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        jLabel2.setBackground(new Color(102,102,102));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel2.setBackground(new Color(153,153,153));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
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
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        jLabel3.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        jLabel3.setBackground(new Color(153,153,153));         // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseExited

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        GetInfo();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void prodNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prodNameMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(prodName, "Product Name");      // TODO add your handling code here:
    }//GEN-LAST:event_prodNameMouseClicked

    private void priceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_priceMouseClicked
        animation ani =  new animation();

        ani.addPlaceholder(price, "Price");        // TODO add your handling code here:
    }//GEN-LAST:event_priceMouseClicked

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setBackground(new Color(102,102,102));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setBackground(new Color(153,153,153));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        ResetInputs();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseClicked

    private void imageUploadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageUploadMouseClicked
        FileChooser();        // TODO add your handling code here:
    }//GEN-LAST:event_imageUploadMouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        boolean valid = validateInputs();

        if(valid == true){        
            try {
                saveChanges();
            } catch (Exception ex) {
                Logger.getLogger(Refactor_Product.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void idMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idMouseClicked
        animation ani = new animation();

        ani.addPlaceholder(id, "Enter ID . . .");        // TODO add your handling code here:
    }//GEN-LAST:event_idMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Descript;
    private javax.swing.JTextField ProdStock;
    private javax.swing.JComboBox<String> category;
    private javax.swing.JTextField id;
    private javax.swing.JLabel imageUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField price;
    private javax.swing.JTextField prodName;
    private javax.swing.JComboBox<String> rarity;
    private javax.swing.JComboBox<String> status;
    // End of variables declaration//GEN-END:variables
}
