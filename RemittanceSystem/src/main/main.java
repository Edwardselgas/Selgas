package main;

import config.config;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.UUID; // Used for a unique transaction ID

public class main {

    // NOTE: For simplicity and to match the existing 'config' methods, 
    // we assume the 'config' class can handle new table structures (tbl_senders, tbl_recipients)
    // and correctly map object types (String, Double, Integer) to the database parameters.

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        // NOTE: The main Scanner is closed at the very end to prevent issues in recursive calls
        Scanner sc = new Scanner(System.in);
        config con = new config();
        main app = new main();
        String resp = null;

        do {
            System.out.println("\n===== REMITTANCE MAIN MENU =====");
            System.out.println("1. Login (Sender/Admin)");
            System.out.println("2. Register New Sender");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = -1;
            
            // Input validation for choice
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // consume the invalid input
                continue;
            }
            sc.nextLine(); // consume newline after nextInt

            switch (choice) {
                case 1: // LOGIN
                    System.out.print("Enter email: ");
                    String email = sc.nextLine().trim();
                    System.out.print("Enter Password: ");
                    String pass = sc.nextLine().trim();

                    // 1. Try to log in as ADMIN (using the old tbl_users structure)
                    String qryAdmin = "SELECT * FROM tbl_users WHERE u_email = ? AND u_pass = ? AND u_type = 'Admin'";
                    List<Map<String, Object>> resultAdmin = con.fetchRecords(qryAdmin, email, pass);
                    
                    if (!resultAdmin.isEmpty()) {
                        Map<String, Object> user = resultAdmin.get(0);
                        String name = user.get("u_name").toString();
                        System.out.println("✅ ADMIN LOGIN SUCCESS! Welcome, " + name);
                        app.adminDashboard();
                        break;
                    }

                    // 2. Try to log in as SENDER (using the new tbl_senders structure)
                    String qrySender = "SELECT * FROM tbl_senders WHERE Email = ? AND Password = ?";
                    List<Map<String, Object>> resultSender = con.fetchRecords(qrySender, email, pass);
                    
                    if (!resultSender.isEmpty()) {
                        Map<String, Object> sender = resultSender.get(0);
                        String firstName = sender.get("FirstName").toString();
                        String senderId = sender.get("SenderID").toString(); 

                        System.out.println("✅ SENDER LOGIN SUCCESS! Welcome, " + firstName);
                        app.senderDashboard(senderId);
                    } else {
                        System.out.println("❌ INVALID CREDENTIALS");
                    }
                    break;

                case 2: // REGISTER NEW SENDER
                    app.registerNewSender(sc, con);
                    break;

                case 3: // EXIT
                    System.out.println("Exiting program... Goodbye! 👋");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }

            System.out.print("\nDo you want to return to the MAIN MENU? (yes/no): ");
            resp = sc.nextLine();

        } while (resp.equalsIgnoreCase("yes"));

        System.out.println("Thank you for using the Remittance Service. Program ended. 💰");
        sc.close();
    }
    
    // -------------------------------------------------------------
    // --- HELPER REGISTRATION FUNCTION (SENDER) ---
    // -------------------------------------------------------------
    public void registerNewSender(Scanner sc, config con) {
        System.out.println("\n--- NEW SENDER REGISTRATION ---");
        
        System.out.print("Enter First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();
        System.out.print("Enter Country: ");
        String country = sc.nextLine();
        
        String newEmail;
        // VALIDATION: check if email exists in tbl_senders
        while (true) {
            System.out.print("Enter user email: ");
            newEmail = sc.nextLine().trim();
            String checkQry = "SELECT * FROM tbl_senders WHERE Email = ?";
            List<Map<String, Object>> exists = con.fetchRecords(checkQry, newEmail);
            if (exists.isEmpty()) {
                break;
            } else {
                System.out.println("Email already exists. Enter a different email.");
            }
        }
        
        System.out.print("Enter Contact Phone: ");
        String phone = sc.nextLine();
        System.out.print("Enter desired Password: ");
        String newPass = sc.nextLine(); 
        
        String kycStatus = "Pending"; // Default Status
        
        // Conceptual SQL: Insert into tbl_senders
        String sql = "INSERT INTO tbl_senders (FirstName, LastName, Address, Country, Email, Phone, KYCStatus, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Pass 8 parameters, excluding the auto-generated SenderID:
        con.addRecord(sql, firstName, lastName, address, country, newEmail, phone, kycStatus, newPass); 

        System.out.println("✅ Registration successful! Please log in.");
    }

    // -------------------------------------------------------------
    // --- ADMIN DASHBOARD ---
    // -------------------------------------------------------------
    public void adminDashboard() {
        Scanner sc = new Scanner(System.in);
        int choice = 0; 
        do {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. Manage Senders");
            System.out.println("2. View All Remittance Transactions");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Enter choice: ");
            
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();  
                continue;
            }
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    manageSenders();
                    break;
                case 2:
                    viewAllTransactions();
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (choice != 3);
    }
    
    // -------------------------------------------------------------
    // --- SENDER MANAGEMENT (Replacing generic User Management for Admin) ---
    // -------------------------------------------------------------
    public void manageSenders() {
        Scanner sc = new Scanner(System.in);
        int userChoice = 0; 

        do {
            System.out.println("\n===== SENDER MANAGEMENT MENU =====");
            System.out.println("1. View All Senders");
            System.out.println("2. Update Sender KYC Status");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            
            if (sc.hasNextInt()) {
                userChoice = sc.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
                continue;
            }
            sc.nextLine(); 

            switch (userChoice) {
                case 1:
                    viewSenders();
                    break;
                case 2:
                    viewSenders(); // Show list before asking for ID
                    updateSenderKyc();
                    break;
                case 3:
                    System.out.println("Going back to Admin Dashboard...");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (userChoice != 3);
    }

    public void viewSenders() {
        System.out.println("\n--- CURRENT SYSTEM SENDERS ---");
        // Query matching the conceptual tbl_senders fields
        String qry = "SELECT SenderID, FirstName, LastName, Country, Email, Phone, KYCStatus FROM tbl_senders";
        
        String[] hdrs = {"ID", "First Name", "Last Name", "Country", "Email", "Phone", "KYC Status"};
        String[] clms = {"SenderID", "FirstName", "LastName", "Country", "Email", "Phone", "KYCStatus"}; 

        config conf = new config();
        conf.viewRecords(qry, hdrs, clms);
    }

    public void updateSenderKyc() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Sender ID to Update KYC Status: ");
        int id;
        if (sc.hasNextInt()) {
            id = sc.nextInt(); 
        } else {
            System.out.println("Invalid ID format. Update cancelled.");
            sc.next();
            return;
        }
        sc.nextLine(); 

        System.out.print("New KYC Status (e.g., Verified, Pending, Rejected): ");
        String kycStatus = sc.nextLine();

        String qry = "UPDATE tbl_senders SET KYCStatus = ? WHERE SenderID = ?";
        conf.updateRecord(qry, kycStatus, id); 
        System.out.println("✅ Sender ID " + id + "'s KYC Status updated to " + kycStatus + ".");
    }
    
    // -------------------------------------------------------------
    // --- SENDER DASHBOARD ---
    // -------------------------------------------------------------
    public void senderDashboard(String senderId) {
        Scanner sc = new Scanner(System.in);
        int choice = 0; 
        do {
            System.out.println("\n===== SENDER DASHBOARD =====");
            System.out.println("1. Send New Remittance 💸");
            System.out.println("2. View My Transactions");
            System.out.println("3. Log out");
            System.out.print("Enter choice: ");
            
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();  
                continue;
            }
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    sendRemittance(senderId); // Functionality for sending money
                    break;
                case 2:
                    viewMyTransactions(senderId); // Functionality for viewing own history
                    break;
                case 3:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (choice != 3);
    }

    // -------------------------------------------------------------
    // --- REMITTANCE TRANSACTION FUNCTIONS ---
    // -------------------------------------------------------------

    public void sendRemittance(String senderId) {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.println("\n--- NEW REMITTANCE ---");

        // --- RECIPIENT DETAILS COLLECTION ---
        System.out.print("Enter Recipient First Name: ");
        String r_firstName = sc.nextLine();
        System.out.print("Enter Recipient Last Name: ");
        String r_lastName = sc.nextLine();
        System.out.print("Enter Recipient Bank Name: ");
        String r_bankName = sc.nextLine();
        System.out.print("Enter Recipient Account Number: ");
        String r_accountNumber = sc.nextLine();
        System.out.print("Enter Recipient Country: ");
        String r_country = sc.nextLine();
        System.out.print("Enter Recipient Pickup Method (e.g., Cash, Bank Transfer, Mobile Wallet): ");
        String r_pickupMethod = sc.nextLine();
        
        // --- FIX: Use a less restrictive check and handle ID retrieval better ---
        // Check if Recipient exists by Account Number (since Name could be changed later)
        String checkRecipientQry = "SELECT RecipientID FROM tbl_recipients WHERE AccountNumber = ?";
        List<Map<String, Object>> existingRecipient = conf.fetchRecords(checkRecipientQry, r_accountNumber);
        
        String recipientId;
        if (existingRecipient.isEmpty()) {
            // Recipient does not exist, so insert new record
            System.out.println("Recipient not found. Registering new recipient...");
            
            String insertRecipientSql = "INSERT INTO tbl_recipients (FirstName, LastName, BankName, AccountNumber, Country, PickupMethod) VALUES (?, ?, ?, ?, ?, ?)";
            conf.addRecord(insertRecipientSql, r_firstName, r_lastName, r_bankName, r_accountNumber, r_country, r_pickupMethod);
            
            // Re-query to get the new RecipientID. This handles the IndexOutOfBoundsException.
            List<Map<String, Object>> newRecipient = conf.fetchRecords(checkRecipientQry, r_accountNumber);

            if (newRecipient.isEmpty()) {
                 // This usually means the INSERT failed, or the re-query is too fast for the DB commit.
                 System.out.println("❌ CRITICAL ERROR: Failed to retrieve new Recipient ID after insert. Transaction cancelled.");
                 return;
            }
            
            // Assuming RecipientID is the correct column name in the DB now.
            recipientId = newRecipient.get(0).get("RecipientID").toString();
            System.out.println("New Recipient Registered. ID: " + recipientId);

        } else {
            // Recipient Found.
            recipientId = existingRecipient.get(0).get("RecipientID").toString();
            System.out.println("Recipient Found. ID: " + recipientId);
        }

        // --- TRANSACTION DETAILS COLLECTION ---
        System.out.print("Enter Amount to Send: ");
        double amountSent = 0.0;
        if (sc.hasNextDouble()) {
            amountSent = sc.nextDouble();
        } else {
            System.out.println("Invalid amount entered. Transaction cancelled.");
            sc.nextLine(); 
            return;
        }
        sc.nextLine(); 

        System.out.print("Enter Currency Sent (e.g., USD): ");
        String currencySent = sc.nextLine().toUpperCase();
        System.out.print("Enter Expected Amount Received (after fees/exchange): ");
        double amountReceived = 0.0;
        if (sc.hasNextDouble()) {
            amountReceived = sc.nextDouble();
        } else {
            System.out.println("Invalid received amount entered. Transaction cancelled.");
            sc.nextLine(); 
            return;
        }
        sc.nextLine(); 
        System.out.print("Enter Currency Received (e.g., PHP): ");
        String currencyReceived = sc.nextLine().toUpperCase();
        
        // Simplified calculation for demo
        double exchangeRate = amountReceived / amountSent;
        double feeCharged = amountSent - (amountReceived / exchangeRate); 
        
        System.out.print("Enter Payment Method (e.g., Card, Bank Debit): ");
        String paymentMethod = sc.nextLine();
        
        // --- LOGGING TRANSACTION ---
        String transactionId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();  
        String transactionStatus = "Pending";
        String transferDate = new java.util.Date().toString(); 

        // Conceptual SQL for 'tbl_transactions'
        String sql = "INSERT INTO tbl_transactions (TransactionID, SenderID, RecipientID, AmountSent, CurrencySent, AmountReceived, CurrencyReceived, ExchangeRate, FeeCharged, TransferDate, Status, PaymentMethod) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Pass 12 parameters (assuming config.addRecord can handle this many)
        conf.addRecord(sql, 
            transactionId, senderId, recipientId, amountSent, currencySent, amountReceived, 
            currencyReceived, exchangeRate, feeCharged, transferDate, transactionStatus, paymentMethod);
        
        System.out.println("✅ Remittance of " + String.format("%.2f %s", amountSent, currencySent) + " sent successfully!");
        System.out.println("Expected Recipient Amount: " + String.format("%.2f %s", amountReceived, currencyReceived));
        System.out.println("Your Transaction ID is: " + transactionId);
    }

    public void viewMyTransactions(String senderId) {
        System.out.println("\n--- MY REMITTANCE HISTORY ---");
        // Joining tbl_transactions with tbl_recipients to show Recipient details
        String qry = "SELECT t.TransactionID, r.FirstName, r.LastName, t.AmountSent, t.CurrencySent, t.AmountReceived, t.CurrencyReceived, t.Status, t.TransferDate FROM tbl_transactions t JOIN tbl_recipients r ON t.RecipientID = r.RecipientID WHERE t.SenderID = '" + senderId + "'";
        
        String[] hdrs = {"ID", "R_First Name", "R_Last Name", "Sent Amt", "Sent Curr", "Rec'd Amt", "Rec'd Curr", "Status", "Date"};
        String[] clms = {"TransactionID", "FirstName", "LastName", "AmountSent", "CurrencySent", "AmountReceived", "CurrencyReceived", "Status", "TransferDate"};

        config conf = new config();
        conf.viewRecords(qry, hdrs, clms); 
    }
    
    public void viewAllTransactions() {
        System.out.println("\n--- ALL REMITTANCE TRANSACTIONS ---");
        // Conceptual JOIN query to get Sender Name and Recipient Name
        String qry = "SELECT t.TransactionID, s.FirstName as SenderName, r.FirstName || ' ' || r.LastName as RecipientName, t.AmountSent, t.CurrencySent, t.Status FROM tbl_transactions t JOIN tbl_senders s ON t.SenderID = s.SenderID JOIN tbl_recipients r ON t.RecipientID = r.RecipientID";
        
        String[] hdrs = {"ID", "Sender", "Recipient", "Amount Sent", "Currency", "Status"};
        String[] clms = {"TransactionID", "SenderName", "RecipientName", "AmountSent", "CurrencySent", "Status"};

        config conf = new config();
        conf.viewRecords(qry, hdrs, clms);  
    }
}