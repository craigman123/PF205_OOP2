/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.userOrders;

import com.google.zxing.WriterException;
import companyInfo.CompanyInformation;
import configuration.config;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public final class export extends javax.swing.JInternalFrame {

    /**
     * Creates new form export
     */
    private int orderID;
    private int userID;
    private int prodID;
    private StringBuilder reciept;
    private int width;
    private String RN;
    private String exactLocation;
    
    public export(int IDorder) {
        this.orderID = IDorder;
        
        initComponents();
        StyleFrame();
        showExport();
    }
    
    public final void StyleFrame(){
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.plaf.basic.BasicInternalFrameUI ui =
            (javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void showExport(){
        config conf = new config();
        
        String qry = "SELECT * FROM userOrders WHERE order_id = ?";
        java.util.List<java.util.Map<String, Object>> export = conf.fetchRecords(qry, orderID); 
        System.out.println(orderID);
        
        if(!export.isEmpty()){
            CompanyInformation info = new CompanyInformation();
            String companyName = "Armex Legal Guns Industry Inc.";
            String location = "Armex Industrial Factory Minglanilla Cebu Philippines";
            
            java.util.Map<String, Object> foundExport = export.get(0);
            userID = ((Number) foundExport.get("user_id")).intValue();
            prodID = ((Number) foundExport.get("prod_id")).intValue();
            String address = foundExport.get("order_shippingAddress").toString();
            String quantity = foundExport.get("order_quantity").toString();
            String orderDate = foundExport.get("order_date").toString();
            String totalPrice = foundExport.get("order_totalPrice").toString();
            String payMethod = foundExport.get("order_paymentMethod").toString();
            String status = foundExport.get("order_status").toString();
            exactLocation = foundExport.get("exact_location").toString();
            
            String userqry = "SELECT * FROM details WHERE user_id = ?";
            java.util.List<java.util.Map<String, Object>> userFound = conf.fetchRecords(userqry, userID); 
            
            String prodqry = "SELECT * FROM products WHERE prod_id = ?";
            java.util.List<java.util.Map<String, Object>> prodFound = conf.fetchRecords(prodqry, prodID);
            
            String email = "No detail";
            String phoneNumber = "No detail";
            int validID = 0;

            String prodName = "";
            String prodPrice = "";
            
            if(!userFound.isEmpty()){
                java.util.Map<String, Object> foundExportUser = userFound.get(0);
                email = foundExportUser.get("user_email").toString();
                phoneNumber = foundExportUser.get("user_number").toString();
                validID = ((Number) foundExportUser.get("user_ValidId")).intValue();
            }
            
            if(!prodFound.isEmpty()){
                java.util.Map<String, Object> foundExportProd = prodFound.get(0);
                prodName = foundExportProd.get("prod_name").toString();
                prodPrice = foundExportProd.get("prod_price").toString();
            }
            
            String receiverName = "";
            String customerAddress = "";
            String specificInfo = "";

            if (address != null && !address.isEmpty()) {
                String[] parts = address.split(","); 

                if (parts.length >= 3) {
                    receiverName = parts[0].trim(); 
                    specificInfo = parts[parts.length - 1].trim(); 

                    StringBuilder middle = new StringBuilder();
                    for (int i = 1; i < parts.length - 1; i++) {
                        middle.append(parts[i].trim());
                        if (i < parts.length - 2) {
                            middle.append(", ");
                        }
                    }
                    customerAddress = middle.toString();
                } else {

                    receiverName = address;
                    customerAddress = "";
                    specificInfo = "";
                }
            }
            
            if(specificInfo.equals("Specific Info . . .") || specificInfo.isEmpty()){
                specificInfo = "No Detail";
            }
            
           // === RECEIPT GENERATOR ===
            StringBuilder exportInfo = new StringBuilder();
            int width = 100;

            // Build horizontal border manually
            StringBuilder borderBuilder = new StringBuilder();
            borderBuilder.append("+");
            for (int i = 0; i < width - 2; i++) { // subtract 2 for the '+' corners
                borderBuilder.append("-");
            }
            borderBuilder.append("+\n");
            String border = borderBuilder.toString();

            // ===== HEADER =====
            exportInfo.append(border);
            exportInfo.append(centerText(companyName.toUpperCase(), width)).append("\n");
            appendWrappedNoBorder(exportInfo, "", location, width);
            exportInfo.append(border);

            // ===== ORDER DETAILS =====
            exportInfo.append(centerText("ORDER DETAILS", width)).append("\n");
            exportInfo.append(border);
            appendWrappedNoBorder(exportInfo, "Order Date", orderDate, width);
            appendWrappedNoBorder(exportInfo, "Payment Method", payMethod, width);
            appendWrappedNoBorder(exportInfo, "Total Price", "₱" + totalPrice, width);
            exportInfo.append(border);

            // ===== CUSTOMER INFORMATION =====
            exportInfo.append(centerText("CUSTOMER INFORMATION", width)).append("\n");
            exportInfo.append(border);
            appendWrappedNoBorder(exportInfo, "Name", receiverName, width);
            appendWrappedNoBorder(exportInfo, "Email", email, width);
            appendWrappedNoBorder(exportInfo, "Phone", phoneNumber, width);
            appendWrappedNoBorder(exportInfo, "Address", customerAddress, width);
            appendWrappedNoBorder(exportInfo, "Details", specificInfo, width);
            exportInfo.append(border);

            // ===== PRODUCT =====
            exportInfo.append(centerText("PRODUCT", width)).append("\n");
            exportInfo.append(border);
            appendWrappedNoBorder(exportInfo, "Product", prodName, width);
            appendWrappedNoBorder(exportInfo, "Price", "₱" + prodPrice, width);
            appendWrappedNoBorder(exportInfo, "Quantity", String.valueOf(quantity), width);
            exportInfo.append(border);

            // ===== TOTAL =====
            exportInfo.append(centerText("TOTAL: ₱" + totalPrice, width)).append("\n");
            exportInfo.append(border);

            // ===== REFERENCE =====
            String referenceNumber = generateUniqueReference();
            exportInfo.append(centerText("REF #: " + referenceNumber, width)).append("\n");
            exportInfo.append(border);

            // Set to text area
            information.setText(exportInfo.toString());
            reciept = exportInfo;
            information.setCaretPosition(0);
            RN = referenceNumber;
            
            startLocation = location;
            stat = status;
            endLocation = customerAddress;
            reciName = receiverName;
            AllowExport();
        }
    }
    
    private String startLocation;
    private String stat;
    private String endLocation;
    private String reciName;
    
    public static String fixWidth(String text, int width) {
        if (text.length() > width) {
            return text.substring(0, width);
        }

        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    public static void appendWrappedNoBorder(StringBuilder sb, String label, String value, int width) {
    int labelWidth = 20;                    // reserved for label
    int valueWidth = width - labelWidth - 3; // space for " : "

    if (value == null) value = "";

    int start = 0;
    boolean firstLine = true;

    while (start < value.length()) {
        int end = Math.min(start + valueWidth, value.length());

        // break at last space if not at the end
        if (end < value.length() && value.charAt(end) != ' ') {
            int lastSpace = value.lastIndexOf(' ', end);
            if (lastSpace > start) end = lastSpace;
        }

        String line = value.substring(start, end).trim();

        if (firstLine) {
            sb.append(String.format("%-" + labelWidth + "s", label))
              .append(" : ")
              .append(line)
              .append("\n");
            firstLine = false;
        } else {
            sb.append(String.format("%-" + labelWidth + "s", ""))
              .append("   ")
              .append(line)
              .append("\n");
        }

        start = end;

        while (start < value.length() && value.charAt(start) == ' ') start++;
    }
}
    
    public static String centerText(String text, int width) {
        if (text.length() > width) text = text.substring(0, width);
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) sb.append(" ");
        sb.append(text);
        for (int i = 0; i < (width - text.length() - padding); i++) sb.append(" ");
        return sb.toString();
    }
    
    private String outputPath;
    
   private void recieptView() throws WriterException, IOException {
        String receiptText = reciept.toString();
        String fontPath = "C:\\Windows\\Fonts\\trebuc.ttf";

        File receiptsDir = new File("receipts");
        if (!receiptsDir.exists()) {
            receiptsDir.mkdir();
        }
        
        String[] parts = exactLocation.split(",");

        double latitude = Double.parseDouble(parts[0].trim());
        double longitude = Double.parseDouble(parts[1].trim());

        String fileName = "OrderReceipt_" + orderID + ".pdf";
        outputPath = new File(receiptsDir, fileName).getPath();

        receiptText = receiptText.replace("₱", "PHP "); 

        // 🔥 IMPORTANT: pass coordinates
        PdfReceipt.generateReceiptPdf(
            receiptText, 
            outputPath, 
            fontPath, 
            RN, 
            latitude, 
            longitude
        );

        System.out.println("PDF saved at: " + outputPath);
    }
    
    private static final Random random = new Random();

    private static String generateReferenceNumber() {
        StringBuilder ref = new StringBuilder();
        
        ref.append(random.nextInt(9) + 1);

        for (int i = 0; i < 12; i++) {
            ref.append(random.nextInt(10));
        }

        return ref.toString();
    }
    
    private static String generateUniqueReference() {
        String ref;
        boolean exists;
        config conf = new config();

        do {
            ref = generateReferenceNumber();
            String sql = "SELECT reference_number FROM exports WHERE reference_number = ?";
            List<Map<String, Object>> result = conf.fetchRecords(sql, ref);
            exists = !result.isEmpty(); 
        } while (exists); 

        return ref;
    }
    
    public void openFile(String filePath) {
        try {
            File file = new File(filePath);

            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("File not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean AllowExport(){
        String status = stat;
        boolean allow = false;
        
        if(!status.equals("Pending")){
            exportBtn.setForeground(Color.decode("#9E0000"));
            exportBtn.setBackground(Color.decode("#545454"));
            allow = false;

        } else {
            exportBtn.setForeground(new Color(0,153,0));
            exportBtn.setBackground(new Color(204,204,204));
            allow = true;
        }
        
        return allow;
    }
    
    public static String getDateExported() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
    }
    
    public void ExportOrder(){
        config conf = new config();
        
        String qry = "INSERT INTO exports(order_id, export_startLocation, export_distination, export_reciever, export_status,"
                + "export_dispatcherName, reference_number, reciept_path, date_export) VALUES(?,?,?,?,?,?,?,?,?)";
        conf.addRecordAndReturnId(qry, orderID, startLocation, endLocation, reciName, "In Transit", "", RN, outputPath, getDateExported());
        
        String updateQry = "UPDATE userOrders SET order_status = ? WHERE order_id = ?";
        conf.updateRecord(updateQry, "In Transit", orderID);
        
        JOptionPane.showMessageDialog(
            null,
            "Order is In Transit",
            "Exported Succesfully",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        this.dispose();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        information = new javax.swing.JTextArea();
        exportBtn = new javax.swing.JLabel();

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("RECIEPT VIEW");
        jLabel1.setOpaque(true);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 139, 40));

        information.setEditable(false);
        information.setColumns(20);
        information.setFont(new java.awt.Font("Trebuchet MS", 1, 16)); // NOI18N
        information.setRows(5);
        information.setText("Information");
        jScrollPane1.setViewportView(information);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 16, 410, 440));

        exportBtn.setBackground(new java.awt.Color(204, 204, 204));
        exportBtn.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        exportBtn.setForeground(new java.awt.Color(0, 153, 0));
        exportBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exportBtn.setText("EXPORT");
        exportBtn.setOpaque(true);
        exportBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exportBtnMouseClicked(evt);
            }
        });
        jPanel1.add(exportBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 470, 139, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        try {
            recieptView();
            openFile(outputPath);
        } catch (WriterException ex) {
            Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void exportBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportBtnMouseClicked
        if(AllowExport()){
            try {
                recieptView();
            } catch (WriterException ex) {
                Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(AllowExport()){
            ExportOrder();    
            
        } else {
            JOptionPane.showMessageDialog(
                null,
                "Order is in " + stat,
                "Invalid Action",
                JOptionPane.ERROR_MESSAGE
            );
        }// TODO add your handling code here:
    }//GEN-LAST:event_exportBtnMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel exportBtn;
    private javax.swing.JTextArea information;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
