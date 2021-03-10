package bank;


import java.util.Scanner;

public class Menu {
    private final Database database;
    private final Scanner scanner;

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

    private void createAccount() {
        Account account = new Account();
        System.out.println("Your card number:\n" + account.getNumCard());
        System.out.println("Your card PIN:\n" + account.getPin());
        database.insert(account);
        account.clear();

    }

    private void login() {
        System.out.println("Enter your card number:");
        String inputNum = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String inputPin = scanner.nextLine();

        String sqlPIN = database.select(inputNum, "pin");

        if (sqlPIN.equals(inputPin)) {
            System.out.println("You have successfully logged in!");
            accountMenu(inputNum);
        } else {
            System.out.println("Wrong card number or PIN!");
            mainMenu();
        }
    }



    private void accountMenu(String cardNumInput) {

        System.out.println("\n1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out\n0. Exit");
        String command = scanner.nextLine();
        switch (command) {
            case "1":
                System.out.println("Balance: " + database.select(cardNumInput, "balance"));
                accountMenu(cardNumInput);
                break;
            case "2":
                addInc(cardNumInput);
                break;
            case "3":
                transfer(cardNumInput);
                break;
            case "4":
                System.out.println(database.removeAccount(cardNumInput, database.select(cardNumInput, "id")));
                mainMenu();
                break;
            case "5":
                System.out.println("You have successfully logged out!");
                mainMenu();
                break;
            case "0":
                break;
            default:
                System.out.println("Something wrong... Try again");
                accountMenu(cardNumInput);
        }
    }



    private void addInc(String numCard) {
        System.out.println("Enter income:");
        String input = scanner.nextLine();

        try  {
            int sum = Integer.parseInt(input);
            System.out.println(database.addIncome(numCard, sum));
            accountMenu(numCard);
        } catch (NumberFormatException e) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());

            System.out.println("Wrong input!\nTry again?(y/n)");
            String answer = scanner.nextLine();

            if ("y".equalsIgnoreCase(answer) || "yes".equalsIgnoreCase(answer)) {
                addInc(numCard);
            }else{
                accountMenu(numCard);
            }
        }
    }

    private void transfer(String numCard) {
        System.out.println("Enter card number:");
        String inputCard = scanner.nextLine();

        if (inputCard.equals(numCard)) {
            System.out.println("You can't transfer money to the same account!");
            accountMenu(numCard);
        }

        if (isCorrectNum(inputCard)) {
            if (inputCard.equals(database.select(inputCard, "number"))) {
                System.out.println("Enter how much money you want to transfer:");
                String inputMoney = scanner.nextLine();

                try {
                    int money = Integer.parseInt(inputMoney);
                    System.out.println(database.doTransfer(numCard, inputCard, money));

                    accountMenu(numCard);
                } catch (NumberFormatException e) {
                    System.out.println("Wrong input!");
                    accountMenu(numCard);
                }

            }else{
                System.out.println("Such a card does not exist.");
                accountMenu(numCard);
            }

        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            accountMenu(numCard);
        }
    }

    private boolean isCorrectNum(String num) {
        int sum = 0;

        if (num.length() != 16) {
            return false;
        }

        for(int i = 0; i < 16; i++) {
            try{
                int x = Integer.parseInt("" + num.charAt(i));
                sum += i % 2 == 0 ? (x * 2 < 10 ? x * 2 : (x * 2) - 9) : x;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }
        return (sum % 10 == 0);
    }
}