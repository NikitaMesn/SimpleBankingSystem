package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
    private String NumCard;
    private String pin;


    public Account() {
        this.NumCard = generateNumCard();
        this.pin = generatePin();

    }
    public void clear() {
        NumCard = "";
        pin = "";
    }


    public String getPin() {
        return pin;
    }

    public String getNumCard() {
        return NumCard;
    }


    static String generateNumCard() {
        StringBuilder numCard = new StringBuilder();
        List<Character> chars = new ArrayList<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

        boolean isLuhn = false;

        while (!isLuhn) {
            numCard = new StringBuilder("400000");
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                Collections.shuffle(chars);
                numCard.append(chars.get(i));
                int x = Integer.parseInt("" + chars.get(i));
                sum += i % 2 == 0 ? (x * 2 < 10 ? x * 2 : (x * 2) - 9) : x;
            }
            if ((sum + 8) % 10 == 0) {
                isLuhn = true;
            }
        }

        return numCard.toString();
    }

    private static String generatePin() {
        StringBuilder code = new StringBuilder();
        List<Character> chars = new ArrayList<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

        for (int i = 0; i < 4; i++) {
            Collections.shuffle(chars);
            code.append(chars.get(i));
        }

        return code.toString();
    }
}
