/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.ProductControl;

import javax.swing.JDesktopPane;

/**
 *
 * @author user
 */
public class connector {
    private static Object StoreData;
    private static JDesktopPane panel;
    
    public static void datareciever(Object data){
        StoreData = data;
    }
    
    public static Object datasender(){
        return StoreData;   
    }
    
    public void PanelReciever(JDesktopPane pane){
        this.panel = pane;
    }
    
    public static JDesktopPane PanelSender(){
        return panel;
    }

}
