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
}
