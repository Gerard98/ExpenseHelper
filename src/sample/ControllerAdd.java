package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.sql.Date;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public class ControllerAdd {

    @FXML
    private ComboBox category;
    @FXML
    private TextField amount;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea info;

    private SessionFactory sessionFactory;
    private User user;

    @FXML
    public void initialize(){
        // Pobieranie sessionFactory i usera z Controllera "Controller"
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        try {
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        Controller con = loader.getController();
        sessionFactory = con.getSessionFactory();
        user = con.getUser();
        Session session = sessionFactory.openSession();

        List<Category> elo = session.createQuery("from Category ").list();
        ObservableList<String> categoryList = FXCollections.observableArrayList();
        for(Category x : elo) categoryList.add(x.getCategory());
        category.setItems(categoryList);
        session.close();
    }



    @FXML
    public void add(){
        // end
        boolean error = false;
        amount.getStyleClass().remove("error");
        category.getStyleClass().remove("error");
        date.getStyleClass().remove("error");
        if(Errors.goodBudget(amount.getText())) {
            amount.getStyleClass().add("error");
            error = true;
        }
        if(category.getValue() == "") {
            category.getStyleClass().add("error");
            error = true;
        }
        if(Errors.goodYear(date.getValue().toString())) {
            date.getStyleClass().add("error");
            error = true;
        }

        if(error == false){

            Session session = sessionFactory.openSession();
            Transaction tx = null;

            try {

            tx = session.beginTransaction();
            int id =Integer.valueOf(Objects.toString( session.createSQLQuery("Select MAX(ID_EXPENSE) from EXPENSE").uniqueResult()));
            Expense expense = new Expense(++id, Double.valueOf(amount.getText()), Date.valueOf(date.getValue()), (String) category.getValue(), info.getText(), user.getId_user());
            session.createSQLQuery("insert into Expense Values( " + expense.toStringFull() + ")" ).executeUpdate();
            tx.commit();
            }
            catch (HibernateException e){
                if (tx!=null) tx.rollback();
                e.printStackTrace();
            }
            finally {
                 session.close();
                ((Stage) amount.getScene().getWindow()).close();

            }



        }




    }

}
