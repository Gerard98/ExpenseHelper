package sample.SettingsWindows;

import javafx.scene.control.Label;
import sample.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sample.Controller;
import sample.Errors;
import sample.User;

import java.io.IOException;

public class ChangeLoginController {

    @FXML
    private Label error;
    @FXML
    private TextField textField;
    private static SessionFactory sessionFactory;
    private User user;

    @FXML
    public void initialize(){
        Errors error = new Errors();
        sessionFactory = error.getSessionFactory();
        user = error.getUser();
    }


    @FXML
    public void change(){
        Session session = sessionFactory.openSession();
        textField.getStyleClass().remove("error");
        Transaction tx = null;

        String newLogin = textField.getText();
        if(Errors.emptyLogin(newLogin) || Errors.userExist(session, newLogin)){
            textField.getStyleClass().add("error");
            error.setVisible(true);
        }
        else{
            try {
                tx = session.beginTransaction();
                session.createSQLQuery("UPDATE USER1 SET LOGIN = '" + newLogin + "' where LOGIN = '" + user.getLogin() + "'").executeUpdate();
                tx.commit();
            } catch (HibernateException e) {
                if (tx!=null) tx.rollback();
                e.printStackTrace();

            } finally {
                session.close();
                sessionFactory.close();
                Platform.exit();
            }
        }

        session.close();
    }
}
