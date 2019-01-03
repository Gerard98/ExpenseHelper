package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class balanceController {

    @FXML
    private Text months,sum,biggestExp,avgExp,monthExp;
    @FXML
    private PieChart pieChartByCategory;
    @FXML
    private BarChart<?,?> barChartByMonth;

    private List<Category> categories = new ArrayList<>();
    private SessionFactory sessionFactory;
    private User user;
    private List<Expense> expenses;

    @FXML
    public void initialize(){
        Errors ses = new Errors();
        sessionFactory = ses.getSessionFactory();
        user = ses.getUser();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String thisYear = year.format(new Date());

        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            // from Expense where id_user = :id_user and TO_CHAR(expenseDate,'MM') = :date
            tx = session.beginTransaction();
            expenses = session.createQuery("from Expense where id_user = " + user.getId_user() + "and TO_CHAR(expensedate,'yyyy') = " + thisYear).list();
            double sumExp,max,avg,balance;
            String month;
            sumExp = Double.valueOf(Objects.toString(session.createSQLQuery("Select SUM(AMOUNT) from Expense where id_user = " + user.getId_user() + "and TO_CHAR(expensedate,'yyyy') = " + thisYear).uniqueResult()));
            max = Double.valueOf(Objects.toString(session.createSQLQuery("Select MAX(AMOUNT) from Expense where id_user = " + user.getId_user() + "and TO_CHAR(expensedate,'yyyy') = " + thisYear).uniqueResult()));
            avg = Double.valueOf(Objects.toString(session.createSQLQuery("Select AVG(AMOUNT) from Expense where id_user = " + user.getId_user() + "and TO_CHAR(expensedate,'yyyy') = " + thisYear).uniqueResult()));
            session.createSQLQuery("SELECT TO_CHAR(EXPENSEDATE, 'MM') FROM EXPENSE e where ID_USER = " + user.getId_user() + " and TO_CHAR(EXPENSEDATE,'YYYY') = " + thisYear + " and (Select Sum(AMOUNT) from expense where TO_CHAR(EXPENSEDATE,'MM') = TO_CHAR(e.expensedate,'MM') and TO_CHAR(ExpenseDATE,'yyyy') = "+thisYear+" and id_user = "+user.getId_user()+") >=ALL (Select SUM(AMOUNT) from expense where TO_CHAR(ExpenseDATE,'yyyy') = "+thisYear+" and id_user = "+user.getId_user()+" group by TO_CHAR(Expensedate, 'MM'))").uniqueResult();
            month = (String) session.createSQLQuery("SELECT TO_CHAR(EXPENSEDATE, 'MM') FROM EXPENSE e where ID_USER = " + user.getId_user() + " and TO_CHAR(EXPENSEDATE,'YYYY') = " + thisYear + " and (Select Sum(AMOUNT) from expense where TO_CHAR(EXPENSEDATE,'MM') = TO_CHAR(e.expensedate,'MM') and TO_CHAR(ExpenseDATE,'yyyy') = "+thisYear+" and id_user = "+user.getId_user()+") >=ALL (Select SUM(AMOUNT) from expense where TO_CHAR(ExpenseDATE,'yyyy') = "+thisYear+" and id_user = "+user.getId_user()+" group by TO_CHAR(Expensedate, 'MM'))").uniqueResult();
            balance = user.getBudget()*12 - sumExp;
            sum.setText("In this year you spend " + sumExp + " so, your balance is: " + balance);
            biggestExp.setText("The biggest cost of expense is: " + max);
            avgExp.setText("Average cost of expeses is: " + avg);
            monthExp.setText("Your expenses were the biggest in " + Month.of(Integer.valueOf(month)));
            tx.commit();

            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

            List<Category> categories = session.createQuery("FROM Category ").list();
            for(Category cat : categories){
                String amount =  session.createSQLQuery("SELECT SUM(AMOUNT) from EXPENSE where ID_USER = " + user.getId_user() + " and TO_CHAR(EXPENSEDATE, 'yyyy') = " + thisYear + " and CATEGORY = '" + cat.getCategory() + "'").uniqueResult().toString();
                data.add(new PieChart.Data(cat.getCategory(), Double.valueOf(amount)));
            }
            pieChartByCategory.setData(data);
            pieChartByCategory.setLabelsVisible(true);
            pieChartByCategory.setTitle("Expenditures by categories");

            //ObservableList<BarChart.Data> dataMonth = FXCollections.observableArrayList();
            XYChart.Series dataMonth = new XYChart.Series();
            dataMonth.setName("Expenses cost");
            for(int i=1 ; i<13 ; i++){
                String amount = session.createSQLQuery("SELECT NVL(SUM(AMOUNT),0) from EXPENSE where ID_USER = " + user.getId_user() + " and TO_CHAR(EXPENSEDATE, 'yyyy') = " + thisYear + " and TO_CHAR(EXPENSEDATE,'MM') = " + i).uniqueResult().toString();
                dataMonth.getData().add(new XYChart.Data<>(Month.of(i).toString(),Double.valueOf(amount)));
            }
            barChartByMonth.getData().addAll(dataMonth);


        }
        catch (HibernateException ex){
            if(tx != null) tx.rollback();
            ex.printStackTrace();
        }




        session.close();
    }

}

