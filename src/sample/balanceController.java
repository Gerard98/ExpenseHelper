package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
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
    private PieChart pieChart;

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
        }
        catch (HibernateException ex){
            if(tx != null) tx.rollback();
            ex.printStackTrace();
        }
        finally {
            session.close();




        }
    }

}

