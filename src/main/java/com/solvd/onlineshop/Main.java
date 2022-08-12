package com.solvd.onlineshop;

import com.solvd.onlineshop.exceptions.InvalidChoiceException;
import com.solvd.onlineshop.processes.SignUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class Main {
    private final static Logger MAIN_LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Integer choice;
        do {
            Scanner scanner = new Scanner(System.in);

            MAIN_LOGGER.info("Hi! Choose your next step:" + '\n' +
                    "1) Sign up a new account" + '\n' +
                    "2) Buy products" + '\n' +
                    "3) Open my shopping orders" + '\n' +
                    "4) Contact the shop" + '\n' +
                    "0) Exit" + '\n' + '\n' +
                    "Make your choice:");
            choice = scanner.nextInt();

            try {
                if (choice == 1) {
                    SignUp.registration();
                } else if (choice == 2) {
                    MAIN_LOGGER.info("You choose 2");
                } else if (choice == 3) {
                    MAIN_LOGGER.info("You choose 3");
                } else if (choice == 4) {
                    MAIN_LOGGER.info("You choose 4");
                } else if (choice == 0) {
                    MAIN_LOGGER.info("Exit from the online shop. Have a nice day!");
                    break;
                } else {
                    throw new InvalidChoiceException("Invalid entering data. Please enter one of the provided numbers.");
                }
            } catch (InvalidChoiceException e) {
                MAIN_LOGGER.error(e);
            }
        } while (choice != 0);
    }
}


