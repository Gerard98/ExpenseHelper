package sample.SettingsWindows;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sample.Errors;
import sample.User;

public class ChangeEmailController {

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

        if(Errors.goodEmail(textField.getText())){
            textField.getStyleClass().add("error");
        }
        else{
            try {
                tx = session.beginTransaction();
                session.createSQLQuery("UPDATE USER1 SET E_MAIL = '" + textField.getText() + "' where LOGIN = '" + user.getLogin() + "'").executeUpdate();
                tx.commit();
            } catch (HibernateException e) {
                if (tx!=null) tx.rollback();
                e.printStackTrace();

            } finally {
                session.close();
                sessionFactory.close();
                ((Stage) textField.getScene().getWindow()).close();
            }
        }

        session.close();
    }
}
