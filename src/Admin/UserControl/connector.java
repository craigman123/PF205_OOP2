/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.UserControl;

import javax.swing.JDesktopPane;

/**
 *
 * @author user
 */
public class connector {
    
    private static JDesktopPane pane;
    
    public static void panelreciever(JDesktopPane panel){
        pane = panel;
    }
    
    public static JDesktopPane panelsender(){
        return pane;
    }   
    
    private static Object StoreData;
    
    public static void DataReciever(Object data){
        StoreData = data;
    }
    
    public static Object DataSender(){
        return StoreData;
    }
}
