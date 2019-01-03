package sample.SettingsWindows;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sample.Errors;
import sample.User;

public class ChangePasswordController{
    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword1;
    @FXML
    private PasswordField newPassword2;
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
        oldPassword.getStyleClass().remove("error");
        newPassword1.getStyleClass().remove("error");
        newPassword2.getStyleClass().remove("error");
        Transaction tx = null;
        boolean error = false;
        if(Errors.emptyPassword(oldPassword.getText())){
            oldPassword.getStyleClass().add("error");
            error = true;
        }
        if(Errors.emptyPassword(newPassword1.getText())){
            newPassword1.getStyleClass().add("error");
            error = true;
        }
        if(Errors.samePassword(newPassword1.getText(), newPassword2.getText())){
            newPassword2.getStyleClass().add("error");
            error = true;
        }

        if(!error){
            try {
                tx = session.beginTransaction();
                session.createSQLQuery("UPDATE USER1 SET PASSWORD = '" + newPassword1.getText() + "' where LOGIN = '" + user.getLogin() + "'").executeUpdate();
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
