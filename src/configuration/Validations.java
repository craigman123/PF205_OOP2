
package configuration;

import java.time.LocalDate;

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
}

