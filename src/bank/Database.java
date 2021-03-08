package bank;

import bank.Account;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;


public class Database {
    private String url;
    private static Connection conn;

    public Database(String name) {

        this.url = "jdbc:sqlite:" + name;
        // url = "jdbc:sqlite:/home/mesn/java/" + name;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                createNewTable();
                conn.close();

            }

        } catch (SQLException e) {
            System.out.println("I'm here");
            System.out.println(e.getMessage());
        }

    }

    public Connection connect() {
        conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card (\n "
                + "  id INTEGER PRIMARY_KEY, \n"
                + "  number VARCHAR(20), \n"
                + "  pin VARCHAR(4), \n"
                + "  balance INTEGER DEFAULT 0\n"
                + ");";
        try (Connection conn = this.connect()) {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException throwables) {

            throwables.printStackTrace();
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
            throwses.printStackTrace();
        }
    }

    public String select(String card,String slct) {
        String sql = "SELECT number, pin, balance FROM card WHERE number = " + card;
        String str = "";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                String numCard = rs.getString("number");
                String pin = rs.getString("pin");
                Integer balance = rs.getInt("balance");

                str = "pin".equals(slct) ? pin : str;
                str = "balance".equals(slct) ? "" + balance : str;
                str = "number".equals(slct) ? "" + numCard : str;
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return str;
    }
}