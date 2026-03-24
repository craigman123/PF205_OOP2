/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

/**
 *
 * @author user
 */
public class Agreement extends javax.swing.JFrame {

    /**
     * Creates new form Agreement
     */
    public Agreement() {
        initComponents();
        ShowAgreement();
    }
    
    public void ShowAgreement() {
    
    
    area.setText(
        "SECURITY AND PRIVACY AGREEMENT\n\n" +

        "This Security and Privacy Agreement outlines the terms, conditions, and legal " +
        "obligations governing the access, use, and interaction with this system. By " +
        "proceeding to use this platform, you explicitly acknowledge that you have read, " +
        "understood, and agreed to be bound by all provisions stated herein.\n\n" +

        "1. AUTHORIZED ACCESS\n" +
        "Access to this system is strictly limited to individuals who possess valid " +
        "authorization and, where required, appropriate licensing. Users must ensure that " +
        "all credentials provided during registration and usage are accurate, legitimate, " +
        "and verifiable. Any attempt to falsify identity, credentials, or authorization " +
        "status constitutes a direct violation of this agreement.\n\n" +

        "2. REGULATED PRODUCTS AND COMPLIANCE\n" +
        "This system facilitates the management and processing of regulated products, " +
        "including but not limited to firearms and related materials. All users are " +
        "required to comply with national and local laws governing the possession, " +
        "purchase, transport, and use of such items. Strict adherence to legal standards " +
        "is mandatory at all times.\n\n" +

        "3. LEGAL FRAMEWORK\n" +
        "In accordance with Republic Act No. 10591, also known as the Comprehensive " +
        "Firearms and Ammunition Regulation Act, unauthorized acquisition, possession, " +
        "or handling of firearms and ammunition is considered a criminal offense. Any " +
        "user found to be in violation of these laws may be subjected to severe legal " +
        "penalties, including arrest, prosecution, fines, and imprisonment as determined " +
        "by the appropriate authorities.\n\n" +

        "4. ACCOUNT RESPONSIBILITY\n" +
        "Users are solely responsible for maintaining the confidentiality and security " +
        "of their accounts. Sharing of login credentials, unauthorized access attempts, " +
        "or negligence resulting in compromised accounts will be treated as a violation " +
        "of this agreement. The system administrators reserve the right to suspend or " +
        "terminate accounts involved in such incidents.\n\n" +

        "5. MONITORING AND ENFORCEMENT\n" +
        "To ensure compliance and maintain system integrity, all activities within the " +
        "platform may be monitored, recorded, and reviewed. Any suspicious, unauthorized, " +
        "or illegal activity may be reported to law enforcement agencies without prior " +
        "notice to the user. The system enforces a zero-tolerance policy for violations.\n\n" +

        "6. PENALTIES AND SANCTIONS\n" +
        "Violation of any terms stated in this agreement will result in immediate " +
        "administrative action, which may include temporary suspension or permanent " +
        "revocation of system access. In severe cases, offending users may be permanently " +
        "blacklisted and subjected to legal proceedings under applicable laws.\n\n" +

        "7. PRIVACY AND DATA HANDLING\n" +
        "All personal data collected by the system will be handled in accordance with " +
        "applicable data protection policies. However, users acknowledge that relevant " +
        "information may be disclosed to authorities when required for legal compliance, " +
        "investigations, or enforcement actions.\n\n" +

        "8. USER DECLARATION\n" +
        "By continuing to access and use this system, you declare that you are a legally " +
        "authorized individual, fully compliant with licensing requirements, and aware " +
        "of the responsibilities associated with handling regulated products. You further " +
        "acknowledge that misuse of this system or violation of applicable laws will " +
        "result in serious consequences.\n\n" +

        "FINAL NOTICE\n" +
        "This agreement serves as a legally binding document between the user and the " +
        "system administrators. If you do not agree with any part of these terms, you " +
        "must immediately discontinue use of the system. Continued use signifies full " +
        "acceptance of all conditions stated above.\n\n" +

        "Proceed with full responsibility and in compliance with the law." + 
                
        "By: ARMEX INCOORPORATION"
    );
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
        jScrollPane1 = new javax.swing.JScrollPane();
        area = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        area.setEditable(false);
        area.setColumns(20);
        area.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        area.setLineWrap(true);
        area.setRows(5);
        area.setWrapStyleWord(true);
        jScrollPane1.setViewportView(area);

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("BACK");
        jLabel1.setOpaque(true);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 16, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked

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
            java.util.logging.Logger.getLogger(Agreement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Agreement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Agreement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Agreement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Agreement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea area;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
