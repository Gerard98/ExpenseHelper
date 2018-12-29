package sample;


import java.sql.Date;

public class Expense {
    private int id_expense;
    private double amount;
    private Date expenseDate;
    private String category;
    private String info;
    private int id_user;

    public Expense(){}

    public Expense(int id_expense, double amount, Date expenseDate, String category, String info, int id_user) {
        this.id_expense = id_expense;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.category = category;
        this.info = info;
        this.id_user = id_user;
    }

    //public String toString(){ return Double.toString(amount) + " " + expenseDate.toString() + " " + category;  }

    public String toStringFull() { return id_expense + "," + Double.toString(amount) + ",'" +expenseDate.toString() + "','" + category + "','" + info + "'," + id_user; }


    public int getId_expense() {
        return id_expense;
    }

    public void setId_expense(int id_expense) {
        this.id_expense = id_expense;
    }

    public double getAmount(){ return amount;}

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
