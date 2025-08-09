
package edwardapp;

import java.util.Scanner;

public class EdwardSystem {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to Edward’s Banking Services!");
        System.out.println("What do you feel doing today?");
        System.out.println("1. Banking");
        System.out.println("2. Doctor Appointment");
        System.out.println("3. Shopping");

        System.out.print("Enter choice: ");
        int userOption = scan.nextInt();

        switch (userOption) {
            case 1:
                BankAccess loginPortal = new BankAccess();
                loginPortal.bankingMenu(scan);
                break;
            case 2:
                System.out.println("\n[Doctor Appointment] Not available at the moment.");
                break;
            case 3:
                System.out.println("\n[Shopping] Feature coming soon!");
                break;
            default:
                System.out.println("\nInvalid input. Restart the system and try again.");
        }

        scan.close();
    }
}