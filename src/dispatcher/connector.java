/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dispatcher;

/**
 *
 * @author user
 */
public class connector {
    private String ref;
    private int id;
    
    public void RN(String r){
        ref = r;
    }
    
    public void exID(int i){
        id = i;
    }
    
    public String RNGetter(){
        return ref;
    }
    
    public int ExID(){
        return id;
    }
}
