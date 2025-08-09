
package edwardapp;

import java.util.Scanner;

public class BankAccess {
    private int[] storedIDs = new int[10];
    private int[] storedPasscodes = new int[10];
    private int accountCount = 0;

    public void registerAccount(int id, int passcode) {
        storedIDs[accountCount] = id;
        storedPasscodes[accountCount] = passcode;
        accountCount++;
    }

    public boolean checkAccount(int id, int passcode) {
        for (int i = 0; i < accountCount; i++) {
            if (storedIDs[i] == id && storedPasscodes[i] == passcode) {
                return true;
            }
        }
        return false;
    }

    public void viewAccounts() {
        if (accountCount == 0) {
            System.out.println("No accounts registered yet.");
        } else {
            System.out.println("=== Registered Accounts ===");
            for (int i = 0; i < accountCount; i++) {
                System.out.println("Account No: " + storedIDs[i] + " | PIN: " + storedPasscodes[i]);
            }
        }
    }

    public void authenticate(Scanner scanner) {
        int tries = 0;
        boolean verified = false;

        System.out.println("\n--- Edward's Banking Login ---");

        while (tries < 3 && !verified) {
            System.out.print("Enter your Client ID: ");
            int enteredID = scanner.nextInt();

            System.out.print("Enter your Passcode: ");
            int enteredCode = scanner.nextInt();

            if (enteredID == 55555 && enteredCode == 9876) {
                System.out.println("\n[Login Successful] Welcome to Edward’s Banking Services!");
                verified = true;
            } else {
                tries++;
                System.out.println("Access denied. Attempt " + tries + " of 3.");
            }
        }

        if (!verified) {
            System.out.println("\nToo many incorrect attempts. Session ended.");
        }
    }

    public void bankingMenu(Scanner scan) {
        boolean running = true;

        while (running) {
            System.out.println("\n1. Register Account");
            System.out.println("2. Login Account");
            System.out.println("3. View All Accounts");
            System.out.print("Enter selection: ");
            int selection = scan.nextInt();

            switch (selection) {
                case 1:
                    System.out.print("Enter Account No: ");
                    int acc = scan.nextInt();
                    System.out.print("Enter Account Pin: ");
                    int pin = scan.nextInt();
                    registerAccount(acc, pin);
                    System.out.println("Account registered successfully!");
                    break;
                case 2:
                    System.out.print("Enter Account No: ");
                    int loginAcc = scan.nextInt();
                    System.out.print("Enter Account Pin: ");
                    int loginPin = scan.nextInt();
                    if (checkAccount(loginAcc, loginPin)) {
                        System.out.println("LOG-IN SUCCESS");
                    } else {
                        System.out.println("Invalid account");
                    }
                    break;
                case 3:
                    viewAccounts();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            System.out.print("\nDo you want to continue? (1 = Yes / 0 = No): ");
            int cont = scan.nextInt();
            if (cont != 1) {
                running = false;
            }
        }
    }
}
