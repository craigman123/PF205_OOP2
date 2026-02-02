
package configuration;

import javax.swing.JTextField;

public class Validations {

    public static int BadgeValidate(String bdg){
        config conf = new config();
        int valid = 1;
        
        String qry = "SELECT * FROM users WHERE user_badge = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, bdg);

        int valuebadge = bdg.length();
        
        try{
            int badge = Integer.parseInt(bdg);
        }catch(NumberFormatException e){
            valid = 0;
        }

        if (valuebadge < 4) {
            valid = 0;
        }else if (!result.isEmpty()) {
            valid = 3;
            
        }
        return valid;
    }
    
    public static int ConvertInts(Object bdg){
        int badge;
        
        try{
            badge = Integer.parseInt((String) bdg);
        }catch(NumberFormatException e){
            return 0;
        }
        
        
        return badge;
        
    }
    
    public static int ValidateInteger(String input){
        
        try{
            int FinalInput = Integer.parseInt(input);
        }catch(NumberFormatException e){
            return 0;
        }
        
        return 1;
    }
    
    public static boolean ValidateIntegerBool(String input){
        
        try{
            int FinalInput = Integer.parseInt(input);
        }catch(NumberFormatException e){
            return true;
        }
        
        return false;
    }
    
    public boolean ValidBadge(String value){
        boolean valid1 = false;
        Validations validation = new Validations();
        
        int valid = validation.BadgeValidate(value);
        
        if(valid != 1){
            valid1 = true;
        }
        
        return valid1;
    }
    
    public boolean validateEmailBoolean(JTextField field) {
        String email = field.getText().trim();

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (email.matches(emailRegex)) {
            return true;
        } else {
            return false;
        }
    }
}

