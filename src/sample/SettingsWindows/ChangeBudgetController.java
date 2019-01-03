package sample.SettingsWindows;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sample.Errors;
import sample.User;



public class ChangeBudgetController {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField budgetField;
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
        budgetField.getStyleClass().remove("error");
        errorLabel.setVisible(false);
        Transaction tx = null;

        if(Errors.goodBudget(budgetField.getText())){
            budgetField.getStyleClass().add("error");
            errorLabel.setVisible(true);
        }
        else{
            try {
                tx = session.beginTransaction();
                session.createSQLQuery("UPDATE USER1 SET BUDGET = " + Double.valueOf(budgetField.getText()) + " where LOGIN = '" + user.getLogin() + "'").executeUpdate();
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
