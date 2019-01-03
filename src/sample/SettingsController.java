package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.hibernate.*;

import java.io.IOException;

public class SettingsController {

    @FXML
    private Button changeLogin;

    private static SessionFactory sessionFactory;
    private User user;

    @FXML
    public void initialize(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        try {
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        Controller con = loader.getController();
        sessionFactory = con.getSessionFactory();
        user = con.getUser();
    }

    @FXML
    public void changeLogin(){

        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("SettingsWindows//changeLogin.fxml"));
            stage.setTitle("Change Login");
            stage.setScene(new Scene(root, 200, 200));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void changeBudget(){

        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("SettingsWindows//changeBudget.fxml"));
            stage.setTitle("Change Budget");
            stage.setScene(new Scene(root, 200, 200));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void changePassword(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("SettingsWindows//changePassword.fxml"));
            stage.setTitle("Change Password");
            stage.setScene(new Scene(root, 200, 300));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void changeEmail(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("SettingsWindows//changeEmail.fxml"));
            stage.setTitle("Change E-Mail");
            stage.setScene(new Scene(root, 200, 200));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteExpenses(){

        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createQuery("DELETE Expense Where id_user = " + user.getId_user()).executeUpdate();
            tx.commit();
        }
        catch(HibernateException ex){
            if(tx != null) tx.rollback();
            ex.printStackTrace();
        }
        finally {
            session.close();
            ((Stage) changeLogin.getScene().getWindow()).close();
        }

    }

    @FXML
    public void deleteAccount(){

        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createSQLQuery("DELETE Expense WHERE id_user = " + user.getId_user()).executeUpdate();
            session.createSQLQuery("DELETE User1 WHERE login = '" + user.getLogin() + "'").executeUpdate();
            tx.commit();
        }
        catch(HibernateException ex){
            if(tx != null) tx.rollback();
            ex.printStackTrace();
        }
        finally {
            session.close();
            sessionFactory.close();
            Platform.exit();
        }

    }

}
