
package configuration;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

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
    
    public static int ConvertInts(String input) {
        
        if(!input.equals("Valid ID Number") && !input.equals("Badge") && !input.isEmpty() && input != null){
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Input should be an integer!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return 0;
            }
        }
        return 0;
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
    
    Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
    Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
    Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
    Border grayBorder = BorderFactory.createLineBorder(Color.GRAY, 2);
    
    public boolean ValidBadgeUpdate(String Input, int sessionBadge, JTextField bdg) {
        config conf = new config();
        boolean valid = false;
        int input = 0;

        try {
            input = Integer.parseInt(Input);

            String qry = "SELECT * FROM users WHERE user_badge = ?";
            java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, input);

            if (input == sessionBadge) { 
                valid = true;
                bdg.setBorder(greenBorder);
            } else if (result.isEmpty()) { 
                valid = true;
                bdg.setBorder(greenBorder);
            } else { 
                valid = false;
                bdg.setBorder(orangeBorder);
            }

        } catch (NumberFormatException e) {
            bdg.setBorder(redBorder); 
            valid = false;
        }

        return valid;
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

