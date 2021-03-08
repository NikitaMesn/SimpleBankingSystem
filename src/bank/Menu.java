package bank;

import bank.Database;
import bank.Account;


import java.util.Scanner;

public class Menu {
    private Database database;
    private Account account;
    private Scanner scanner;

    public Menu(Database database) {
        this.scanner = new Scanner(System.in);
        this.database = database;

    }

    public void mainMenu() {

        System.out.println("\n1. Create an account\n2. Log into account\n0. Exit");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                createAccount();
                mainMenu();
                break;
            case "2":
                login();
                break;
            case "0":
                break;
            default:
                System.out.println("Something wrong... Try again");
                mainMenu();
        }


    }

    private void login() {
        System.out.println("Enter your card number:");
        String inputNum = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String inputPin = scanner.nextLine();

        String sqlPIN = database.select(inputNum, "pin");

        if (sqlPIN.equals(inputPin)) {
            System.out.println("You have successfully logged in!");
            accountMenu(inputPin);
        } else {
            System.out.println("Wrong card number or PIN!");
            mainMenu();
        }
    }

    private void accountMenu(String cardNumInput) {

        System.out.println("\n1. Balance\n2. Log out\n0. Exit");
        String command = scanner.nextLine();
        switch (command) {
            case "1":
                System.out.println("Balance: " + database.select(cardNumInput, "balance"));
                accountMenu(cardNumInput);
                break;
            case "2":
                System.out.println("You have successfully logged out!");
                mainMenu();
            case "0":
                break;
            default:
                System.out.println("Something wrong... Try again");
                accountMenu(cardNumInput);
        }

    }

    private void createAccount() {
        account = new Account();
        System.out.println("Your card number:\n" + account.getNumCard());
        System.out.println("Your card PIN:\n" + account.getPin());
        database.insert(account);
        account.clear();

    }

}