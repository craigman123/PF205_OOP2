/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Profiles;

import configuration.config;
import java.time.*;

/**
 *
 * @author USER35
 */
public class session {
    private static int ID;
    
    public static void SaveLogIn(int id){
        config conf = new config();
        
        ID = id;
        
        String qry = "INSERT INTO session(login, logout, user_id) VALUES(?,?,?)";
        conf.addRecordAndReturnId(qry, GetDate(), "null", id);
        
    }
    
    public final int GetID(){
        return ID;
    }
    
    public static String GetDate(){
        LocalDate currentDate = LocalDate.now();
        System.out.println("Current Date: " + currentDate);

        LocalTime currentTime = LocalTime.now();
        System.out.println("Current Time: " + currentTime);

        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println("Current Date and Time: " + currentDateTime);
        
        String date = "" + currentDateTime + "";
        
        return date;
    }
    
    
}
