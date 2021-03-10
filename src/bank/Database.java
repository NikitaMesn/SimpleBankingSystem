package bank;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;


public class Database {
    private final String url;

    public Database(String name) {

        this.url = "jdbc:sqlite:" + name;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                createNewTable();
                conn.close();
            }
        } catch (SQLException e) {
            //System.out.println("I'm here");
            System.out.println(e.getMessage());
        }
    }

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
            //System.out.println(throwables.printStackTrace());
        }

        return conn;
    }

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card (\n "
                + "  id INTEGER PRIMARY KEY ASC, \n"
                + "  number VARCHAR(20), \n"
                + "  pin VARCHAR(4), \n"
                + "  balance INTEGER DEFAULT 0\n"
                + ");";
        try (Connection conn = this.connect()) {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
            //System.out.println(throwables.printStackTrace());
        }
    }

    public void insert(Account account) {
        String numCard = account.getNumCard();
        String pin = account.getPin();
        String sql = "INSERT INTO card (number, pin) VALUES (?, ?);";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numCard);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
        } catch (SQLException throwses) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
            //System.out.println(throwses.printStackTrace());
        }
    }

    public String select(String card,String slct) {
        String sql = "SELECT id, number, pin, balance FROM card WHERE number = " + card + ";";
        String str = "";

        try (Connection conn = this.connect()) {

            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)){

                while (rs.next()) {
                    String numCard = rs.getString("number");
                    String pin = rs.getString("pin");
                    int balance = rs.getInt("balance");
                    int id = rs.getInt("id");

                    str = "id".equals(slct) ? "" + id : str;
                    str = "pin".equals(slct) ? pin : str;
                    str = "balance".equals(slct) ? "" + balance : str;
                    str = "number".equals(slct) ? numCard : str;
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
            //System.out.println(e.printStackTrace());
        }
        return str;



    }

    public String removeAccount(String card, String id) {
        int cardId = Integer.parseInt(id);

        String sqlDEL = "DELETE FROM card WHERE number = ?";
        String sqlID = "UPDATE card SET id = id - 1 WHERE id > ?";

        try (Connection conn = this.connect()) {

            conn.setAutoCommit(false);

            try (PreparedStatement delAcc = conn.prepareStatement(sqlDEL);
                 PreparedStatement updId = conn.prepareStatement(sqlID)) {

                delAcc.setString(1, card);
                delAcc.executeUpdate();

                updId.setInt(1, cardId);
                updId.executeUpdate();

                conn.commit();
            }
        } catch (SQLException e) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
            //e.printStackTrace();
            return "An error has occurred";
        }

        return "The account has been closed!";
    }

    public String addIncome(String num, int sum) {

        String sqlInc = "UPDATE card SET balance = balance + ? WHERE number =  ?";

        try (Connection conn = this.connect()) {

            conn.setAutoCommit(false);

            try (PreparedStatement updInc = conn.prepareStatement(sqlInc)) {

                updInc.setInt(1, sum);
                updInc.setString(2, num);
                updInc.executeUpdate();

                conn.commit();
            }
        } catch (SQLException e) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
            //e.printStackTrace();
            return "An error has occurred";
        }

        return "Income was added!";
    }

    public String doTransfer(String userNumCard, String transNumCard, int money) {

        String sqlIn = "UPDATE card SET balance = balance + ? WHERE number =  ?";
        String sqlOut = "UPDATE card SET balance = balance - ? WHERE number =  ?";

        if (Integer.parseInt(this.select(userNumCard, "balance")) < money) {
            return "Not enough money!";
        }

        try (Connection conn = this.connect()) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement sqIn = conn.prepareStatement(sqlIn);
                    PreparedStatement sqOut = conn.prepareStatement(sqlOut)){

                sqOut.setInt(1, money);
                sqOut.setString(2, userNumCard);
                sqOut.executeUpdate();

                sqIn.setInt(1, money);
                sqIn.setString(2, transNumCard);
                sqIn.executeUpdate();

                conn.commit();
            }
        } catch (SQLException e) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
            //System.out.println(e.printStackTrace());
        }
        return "Success!";
    }
}