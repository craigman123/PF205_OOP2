/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.Market;

import javax.swing.JDesktopPane;

/**
 *
 * @author user
 */
public class connector {
    public static JDesktopPane desktop;
    
    public static void DesktopReciever(JDesktopPane desk){
        desktop = desk;
    }
    
    public static JDesktopPane DesktopSender(){
        return desktop;
    }
    
    
}
