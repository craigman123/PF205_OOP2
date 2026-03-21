/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Cart;

import Profiles.session;
import configuration.config;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javax.swing.JComboBox;

/**
 *
 * @author user
 */
public class Cart_config {
    public static void SaveCart(int prodID){
        config conf = new config();
        session see = new session();
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = now.format(formatter);
        
        String qry = "INSERT INTO cart(prod_id, user_id, cart_timeSaved) VALUES(?,?,?)";
        conf.addRecordAndReturnId(qry, prodID, see.GetID(), formatted);
    }
    
    private static String name;

    public static void DisplayPreviousAddress(JComboBox<String> actionCombo){
        config conf = new config();
        session see = new session();

        String qry = "SELECT * FROM userOrders WHERE user_id = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, see.GetID()); 

        if(!result.isEmpty()){

            java.util.Set<String> uniqueAddresses = new java.util.LinkedHashSet<>();

            actionCombo.removeAllItems();

            for (java.util.Map<String, Object> previousOrders : result) {

                String rawAddress = previousOrders.get("order_shippingAddress").toString();

                String[] parts = rawAddress.split(",");

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                if (name == null) {
                    name = parts[0];
                }

                String address = parts[1] + ", " +
                                 parts[2] + ", " +
                                 parts[3] + ", " +
                                 parts[4] + ", " +
                                 parts[5] + ", " +
                                 parts[6] + ", " +
                                 parts[7] + ", ";

                uniqueAddresses.add(address);
            }

            for (String addr : uniqueAddresses) {
                actionCombo.addItem(addr);
            }

        } else {
            System.out.println("No previous Order Details");
        }
    }
    
    public String NameGetter(){
        return name;
    }
    
    private static String Finalname;
    private static String city;
    private static String province;
    private static String country;
    private static String region;
    private static String brg;
    private static int zip;
    private static String spec;

    public static void sendGlobal(String name, String address) {
        Finalname = name;

        String[] parts = address.split(",");

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length > 0) brg = parts[0];
        if (parts.length > 1) city = parts[1];
        if (parts.length > 2) province = parts[2];
        if (parts.length > 3) region = parts[3];
        if (parts.length > 4) country = parts[4];
        if (parts.length > 5) zip = Integer.parseInt(parts[5]);
        if (parts.length > 6) spec = parts[6];
    }

    public static String getFinalname() { return Finalname; }
    public static String getCity() { return city; }
    public static String getProvince() { return province; }
    public static String getCountry() { return country; }
    public static String getRegion() { return region; }
    public static String getBrg() { return brg; }
    public static int getZip() { return zip; }
    public static String getSpecificInf() { return spec; }
    
    
}
