package sample;

import javafx.fxml.FXMLLoader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public static boolean emptyPassword(String password){
        return password.length() < 3;
    }

    public static boolean samePassword(String password1, String password2){
        if(password1.length() > 3)
            return !password1.equals(password2);
        else return true;
    }

    public static boolean goodYear(String date){

            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            String thisYear = year.format(new Date());
            for (int i = 0; i < 4; i++) {
                if (!(thisYear.charAt(i) == date.charAt(i))) return true;
            }
            return false;
    }

    public static boolean userExist(Session session, String user){

        List<User> logins = session.createQuery("from User ").list();
        if(logins.stream().anyMatch(x -> x.getLogin().equals(user))) return true;
        else return false;

    }

    public SessionFactory getSessionFactory(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml")); // sample/sample.fxml
        try {
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        Controller con = loader.getController();
        return con.getSessionFactory();
    }

    public User getUser(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml")); // sample/sample.fxml
        try {
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        Controller con = loader.getController();
        return con.getUser();
    }


}
