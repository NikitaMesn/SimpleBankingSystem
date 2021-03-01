package bank;

import java.util.*;


public class Main {
    private static HashMap<String, String> CardPin = new HashMap<>();
    private static HashMap<String, Integer> CardBalance = new HashMap<>();

    public static void main(String[] args) {
        mainMenu();

    }
    private static void mainMenu() {
        System.out.println("1. Create an account\n2. Log into account\n0. Exit");
        String answer = userInput();

        switch (answer) {
            case ("1"):
                createAccount();
                mainMenu();
                break;
            case ("2"):
                System.out.println("Enter your card number:");
                String inputCardNum = userInput();

                System.out.println("Enter your PIN:");
                String inputPin = userInput();

                checkAccount(inputCardNum, inputPin);
                break;
            case ("0"):
                System.out.println("Bye!");
                break;
            default:
                System.out.println("Something wrong... Try again");
                mainMenu();
                break;
        }
    }

    private static void accountMenu(String numCard) {
        System.out.println("1. Balance\n2. Log out\n0. Exit");
        String answer = userInput();;
        switch (answer) {
            case ("1"):
                System.out.println("Balance: " + CardBalance.get(numCard) );
                accountMenu(numCard);
                break;
            case ("2"):
                System.out.println("You have successfully logged out!");
                mainMenu();
                break;
            case ("0"):
                System.out.println("Bye!");
                break;
            default:
                System.out.println("Something wrong... Try again");
                mainMenu();
                break;
        }
    }

    private static String generatePin(int num) {
        StringBuilder code = new StringBuilder();
        List<Character> chars = new ArrayList<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        for (int i = 0; i < num; i++ ) {
            Collections.shuffle(chars);
            code.append(chars.get(i));
        }

        return code.toString();
    }
    private static String generateNumCard() {
        StringBuilder code = new StringBuilder("400000");
        code.append(generatePin(10));

        return code.toString();
    }

    private static void createAccount() {
        String card;

        do {
            card = generateNumCard();
        } while (CardBalance.containsKey(card));

        String pin = generatePin(4);

        CardPin.put(card, pin);
        CardBalance.put(card, 0);

        System.out.println("Your card has been created");

        System.out.println("Your card number:\n" + card);
        System.out.println("Your card PIN:\n" + pin + "\n");
    }

    private static void checkAccount(String card, String pin) {
        if (CardBalance.containsKey(card)) {
            if (CardPin.get(card).equals(pin)) {
                System.out.println("You have successfully logged in!");
                accountMenu(card);
            } else {
                System.out.println("Wrong card number or PIN!");
                mainMenu();
            }
        } else {
            System.out.println("Wrong card number or PIN!");
            mainMenu();
        }
    }
    public static String userInput() {
        Scanner scan = new Scanner(System.in);

//        String[] input = scan.nextLine().split(" ");
//        String str = input[input.length - 1];
        String str = scan.nextLine();
        return str;
    }
}
