
package edwardapp;

import java.util.Scanner;

public class EdwardSystem {
        public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("=== WELCOME TO EDWARD'S SERVICE PORTAL ===");
        System.out.println("Select a service to begin:");
        System.out.println("1. Banking Access");
        System.out.println("2. Book Doctor Appointment");
        System.out.println("3. Start Shopping");

        System.out.print("Your option: ");
        int userOption = scan.nextInt();

        switch (userOption) {
            case 1:
                BankAccess loginPortal = new BankAccess();
                loginPortal.authenticate(scan);
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