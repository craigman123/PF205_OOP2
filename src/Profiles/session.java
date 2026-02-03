/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Profiles;

/**
 *
 * @author USER35
 */
public class session {
    private static int ID;
    
    public static void SaveLogIn(int id){
        
        ID = id;
        
    }
    
    public final int GetID(){
        return ID;
    }
    
    
}
