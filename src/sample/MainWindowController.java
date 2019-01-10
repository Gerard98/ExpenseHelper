package sample;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainWindowController {


    @FXML
    private Pane pane;
    @FXML
    private Label monthLabel, spendLabel, keepLabel, userLogin, errorBalance;
    @FXML
    private TableView<Expense> tableView;
    @FXML
    private TableColumn<Expense, Float> amountColumn ;
    @FXML
    private TableColumn<Expense, Date> dateColumn;
    @FXML
    private TableColumn<Expense, String> categoryColumn;
    @FXML
    private ComboBox monthChoice;


    private final String expenseByMonthQuery = "from Expense where id_user = :id_user and TO_CHAR(expenseDate,'MM') = :date";
    private final SimpleDateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private List<Expense> expenses;
    private User user;
    private SessionFactory sessionFactory;


    public void showExpanses(String month){

        Session session = sessionFactory.openSession();

        //  Pobieranie i Wyświetlanie wydatów

        Query query = session.createQuery(expenseByMonthQuery);
        query.setParameter("id_user", user.getId_user());
        query.setParameter("date", month);
        expenses = query.list();

        // Sprawdzanie czy wydatki są z aktualnego roku

        int i=0;
        if(!expenses.isEmpty())
        while(i< expenses.size()){
            if(Errors.goodYear(expenses.get(i).getExpenseDate().toString()))expenses.remove(i);
            else i++;
        }

        // Wyświetlanie wydatków w bieżącym miesiącu

        ObservableList<Expense> expensesObs = FXCollections.observableList(expenses);
        tableView.setItems(expensesObs);
        amountColumn.setCellValueFactory(new PropertyValueFactory("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory("expenseDate"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory("category"));

        // Suma wydatków i ile zostało w bieżącym miesiącu

        double sumExpenses  = 0;
        for(Expense x : expenses){
            sumExpenses += x.getAmount();
        }

        spendLabel.setText(Double.toString(sumExpenses));
        keepLabel.setText(Double.toString(user.getBudget() - sumExpenses));
        if(user.getBudget() -  sumExpenses> 0) keepLabel.setTextFill(Color.GREEN);
        else keepLabel.setTextFill(Color.valueOf("#eb2828"));


        session.close();

    }

    public void showExpanses(){

        String month = (String) monthChoice.getValue();
        ObservableList months = monthChoice.getItems();
        for(int i=0;i<12;i++){
            if(months.get(i).equals(month)){
                i++;
                if(i < 10){
                    showExpanses("0" + String.valueOf(i));
                }
                else showExpanses(String.valueOf(i));
                break;
            }
        }

    }

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
        Session session = sessionFactory.openSession();
        user = con.getUser();


        // ChoiceBox settings

        ObservableList<String> monthObs = FXCollections.observableArrayList("January","February","March","April","May","June","July","August","September","October","November","December");
        monthChoice.setItems(monthObs);
        monthChoice.setValue(monthObs.get(Integer.valueOf(monthFormat.format(new Date())) - 1));

        //  Wyświetlanie aktualnej daty i loginu użytkownika

        monthLabel.setText(onlyDateFormat.format(new Date()));
        userLogin.setText(user.getLogin());
        showExpanses(monthFormat.format(new Date()));

        session.close();

    }

    @FXML
    public void choiceMonth(){
        showExpanses();
    }

    @FXML
    public void refresh(){
        showExpanses();
    }

    @FXML
    public void addExpense(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("add.fxml"));
            stage.setTitle("Add");
            stage.setScene(new Scene(root, 300, 400));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void deleteExpense(){
        Expense expense = tableView.getSelectionModel().getSelectedItem();
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createQuery("delete Expense where id_expense = " + expense.getId_expense()).executeUpdate();
            tx.commit();
        }
        catch(HibernateException e){
            if(tx != null) tx.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
            refresh();
        }
    }

    @FXML
    public void info(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Information of Expanse");
        Expense expense = tableView.getSelectionModel().getSelectedItem();
        alert.setContentText("Id: " + expense.getId_expense() + "\nAmount: " + expense.getAmount() + "\nDate: " +expense.getExpenseDate() + "\nCategory: " + expense.getCategory() + "\nId User: " +expense.getId_user() + "\nInformation: " + expense.getInfo());
        alert.showAndWait();
    }

    @FXML
    public void balance(){

        Session session = sessionFactory.openSession();
        errorBalance.setVisible(false);

        List<Expense> expenses = new ArrayList<>();
        expenses = session.createQuery("from Expense where id_user = " + user.getId_user() + " and TO_CHAR(expenseDate, 'yyyy') = " + yearFormat.format(new Date()).toString()).list();

        if(!expenses.isEmpty()) {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("balance.fxml"));
                stage.setTitle("Balance Sheet");
                stage.setScene(new Scene(root, 800, 600));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            errorBalance.setVisible(true);

        session.close();
    }

    @FXML
    public void settings(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
            stage.setTitle("Settings");
            stage.setScene(new Scene(root, 200, 270));
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

}



