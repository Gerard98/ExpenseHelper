package sample.AdminWindows;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sample.Category;
import sample.Errors;

import java.util.List;
import java.util.Objects;

public class AddCategoryController {

    @FXML
    private TextField newName;
    private static SessionFactory sessionFactory;

    @FXML
    public void initialize(){
        Errors errors = new Errors();
        sessionFactory = errors.getSessionFactory();
    }

    @FXML
    public void confirm(){
        Session session = sessionFactory.openSession();
        boolean error = false;
        newName.getStyleClass().remove("error");

        List<Category> categories = session.createQuery("from Category ").list();
        for(Category category : categories){
            if(category.getCategory().equals(newName.getText())) error = true;
        }

        if(error == false) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.createSQLQuery("insert into Category Values('" + newName.getText() + "')").executeUpdate();
                tx.commit();
            } catch (HibernateException ex) {
                if (tx != null) tx.rollback();
                ex.printStackTrace();
            }
            finally {
                ((Stage) newName.getScene().getWindow()).close();
            }
        }
        else newName.getStyleClass().add("error");

    }

}
