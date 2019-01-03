package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jdk.nashorn.internal.codegen.types.ArrayType;
import net.bytebuddy.description.method.ParameterList;
import org.hibernate.*;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.ServiceBinding;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;


public class ControllerRegister {
    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField passwordField1;
    @FXML
    private PasswordField passwordField2;
    @FXML
    private TextField  textFieldEmail;
    @FXML
    private TextField textFieldBudget;
    @FXML
    private Label errorLabel;
    @FXML
    private Pane pane;

    private SessionFactory sessionFactory;
    private List<Boolean> error = new ArrayList<>();
    private List<TextField> textFields = new ArrayList<>();

    @FXML
    public void initialize() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        try {
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        Controller con = loader.getController();
        sessionFactory = con.getSessionFactory();

        textFields.add(textFieldLogin);
        textFields.add(passwordField1);
        textFields.add(passwordField2);
        textFields.add(textFieldEmail);
        textFields.add(textFieldBudget);
        textFields.add(textFieldLogin);

    }


    @FXML
    public void actionRegister(){

            Session session = sessionFactory.openSession();
            Transaction tx = null;

            for(int i=0;i<textFields.size();i++){
                textFields.get(i).getStyleClass().remove("error");
            }
            errorLabel.setVisible(false);


            error.clear();

            error.add(Errors.emptyLogin(textFieldLogin.getText()));
            error.add(Errors.emptyPassword(passwordField1.getText()));
            error.add(Errors.samePassword(passwordField1.getText(), passwordField2.getText()));
            error.add(Errors.goodEmail(textFieldEmail.getText()));
            error.add(Errors.goodBudget(textFieldBudget.getText()));
            List<User> logins = session.createQuery("from User ").list();
            if(logins.stream().anyMatch(x -> x.getLogin().equals(textFieldLogin.getText()))){
                error.add(true);
                errorLabel.setVisible(true);
            }

            // DODAWANIE DO BAZY
            if(error.stream().allMatch(x -> x == false)) {
                try {
                tx = session.beginTransaction();
                Object maxID = session.createSQLQuery("SELECT MAX(ID_USER) FROM USER1").uniqueResult();
                int id = Integer.valueOf(Objects.toString(maxID));
                id++;
                User user = new User(id, textFieldLogin.getText(), passwordField1.getText(), textFieldEmail.getText(), Double.valueOf(textFieldBudget.getText()), "NO");
                session.createSQLQuery("INSERT INTO USER1 VALUES(" + user + ")").executeUpdate();
                tx.commit();
                } catch (HibernateException e) {
                    if (tx!=null) tx.rollback();
                    e.printStackTrace();

                } finally {
                    session.close();
                    ((Stage) textFieldLogin.getScene().getWindow()).close();
                }
            }
            else{
                for(int i=0;i<error.size();i++){
                    if(error.get(i)){
                        textFields.get(i).getStyleClass().add("error");
                    }
                }
                session.close();
            }

    }
}
