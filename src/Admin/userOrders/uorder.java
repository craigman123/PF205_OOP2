/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.userOrders;

/**
 *
 * @author user
 */
public class uorder {
    private String keepText;
    
    public void GetOrder(String text){
        keepText = text;
    }
    
    public String SendOrder(){
        return keepText;
    }
}
