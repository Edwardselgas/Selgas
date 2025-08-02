
package edwardapp;

import java.util.Scanner;

public class BankAccess {
    
    private final int storedID = 55555;
    private final int storedPasscode = 9876;

    public void authenticate(Scanner scanner) {
        int tries = 0;
        boolean verified = false;

        System.out.println("\n--- Edward's Banking Login ---");

        while (tries < 3 && !verified) {
            System.out.print("Enter your Client ID: ");
            int enteredID = scanner.nextInt();

            System.out.print("Enter your Passcode: ");
            int enteredCode = scanner.nextInt();

            if (enteredID == storedID && enteredCode == storedPasscode) {
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
}