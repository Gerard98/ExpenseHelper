package sample;

import com.sun.media.jfxmediaimpl.platform.PlatformManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Controller {

    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;

    private static User user;

    private static final SessionFactory sessionFactory;

    static{
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    @FXML
    public void initialize(){
        errorLabel.setVisible(false);
    }

    public void endSessionFactory(){
        sessionFactory.close();
    }

    public SessionFactory getSessionFactory(){ return sessionFactory; }

    public User getUser() {
        return user;
    }

    @FXML
    public void signUp(){

        Session session = sessionFactory.openSession();
        Transaction tx = null;

        user =(User) session.createQuery("from User where login = '" + login.getText() + "' and password = '" + password.getText() + "'").uniqueResult();

        if(user != null && user.getAdmin().equals("NO")) {
            try {

                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                stage.setTitle("Hello");
                stage.setScene(new Scene(root, 700, 600));
                stage.show();
                stage.setOnCloseRequest(event -> {
                    endSessionFactory();
                });

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                session.close();
                ((Stage) login.getScene().getWindow()).close();
            }
        }
        else if(user != null && user.getAdmin().equals("YES")){
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("adminMenu.fxml"));
                stage.setTitle("Hello");
                stage.setScene(new Scene(root, 600, 500));
                stage.show();
                stage.setOnCloseRequest(event -> {
                    endSessionFactory();
                });

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                session.close();
                ((Stage) login.getScene().getWindow()).close();
            }
        }
        else{
            errorLabel.setVisible(true);
        }



    }

    @FXML
    public void registerClicked(){
        try {

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            stage.setTitle("Registration");
            stage.setScene(new Scene(root, 300, 400));
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
