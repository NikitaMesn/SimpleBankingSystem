package bank;

import bank.Database;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String baseName = "";

        if (args.length != 0){
            baseName = args[args.length - 1];
        } else {
            baseName = "/home/mesn/card.s3db";
        }

        Database db = new Database(baseName);
        Menu menu = new Menu(db);
        menu.mainMenu();
    }
}

