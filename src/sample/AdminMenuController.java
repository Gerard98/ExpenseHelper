package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;


public class AdminMenuController {

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn idColumn, loginColumn, passwordColumn, emailColumn;
    private static SessionFactory sessionFactory;
    private User user;

    public void showUsers(){
        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("from User").list();
        for(User user : users) {
            if (user.getId_user() == this.user.getId_user()) {
                users.remove(user);
                break;
            }
        }
        ObservableList<User> usersObs = FXCollections.observableList(users);
        userTableView.setItems(usersObs);
        idColumn.setCellValueFactory(new PropertyValueFactory("id_user"));
        loginColumn.setCellValueFactory(new PropertyValueFactory("login"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory("password"));
        emailColumn.setCellValueFactory(new PropertyValueFactory("e_mail"));
        session.close();
    }

    @FXML
    public void initialize(){
        Errors error = new Errors();
        sessionFactory = error.getSessionFactory();
        user = error.getUser();
        showUsers();
    }

    @FXML
    public void refresh(){
        showUsers();
    }

    @FXML
    public void addCategory(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AdminWindows//AddCategory.fxml"));
            stage.setTitle("Add Category");
            stage.setScene(new Scene(root, 250, 140));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void addNewAdmin(){

        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("AdminWindows//AddNewAdmin.fxml"));
            stage.setTitle("Add New Admin");
            stage.setScene(new Scene(root, 300, 320));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void delete(){
        User user = userTableView.getSelectionModel().getSelectedItem();
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createSQLQuery("DELETE Expense WHERE id_user = " + user.getId_user()).executeUpdate();
            session.createQuery("delete User where id_user = " + user.getId_user()).executeUpdate();
            tx.commit();
        }
        catch(HibernateException e){
            if(tx != null) tx.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
            showUsers();
        }
    }

}
