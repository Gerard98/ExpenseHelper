package sample;


public class User {
    private int id_user;
    private String login;
    private String password;
    private String e_mail;
    private double budget;

    public User(){}

    public User(int id_user, String login, String password, String e_mail, double budget) {
        this.id_user = id_user;
        this.login = login;
        this.password = password;
        this.e_mail = e_mail;
        this.budget = budget;
    }

    @Override
    public String toString() {
        return id_user + ",'" + login + "','" + password + "','" + e_mail + "'," + budget;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
