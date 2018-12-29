package sample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Errors {


    public static boolean emptyLogin(String word){
        return word.length() < 3;
    }

    public static boolean goodEmail(String word){
        boolean error = true;
        if(word != "") {
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == '@') {
                    error = false;
                }
            }
        }
        return error;
    }

    public static boolean goodBudget(String budget){
        boolean error = true;
        if(budget != "") {
            int numberOfDot = 0;
            for (int i = 0; i < budget.length(); i++) {
                if (budget.charAt(i) > 47 && budget.charAt(i) < 58) error = false;
                if (budget.charAt(i) == 46) numberOfDot++;
            }
            if (numberOfDot > 1) error = true;
        }
        return error;
    }

    public static boolean samePassword(String password1, String password2){
        if(password1.length() > 3) {
            return !password1.equals(password2);
        }
        return true;
    }

    public static boolean goodYear(String date){
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String thisYear = year.format(new Date());
        for(int i=0;i<4;i++){
            if(!(thisYear.charAt(i) == date.charAt(i))) return true;
        }
        return false;
    }


}
