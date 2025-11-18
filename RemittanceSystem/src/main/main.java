package Main;

import config.dbConnect;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class Main {

    
    public static void viewRemittances() {
        String Query = "SELECT r.remit_id, s.first_name AS sender_name, r.recipient_name, r.amount, r.fee, r.total_charge, DATE_FORMAT(r.remit_date, '%Y-%m-%d') as remit_date, r.status "
                + "FROM tbl_remittances r "
                + "JOIN tbl_senders s ON r.sender_id = s.sender_id";
        
        
        String[] remitHeaders = {"Remit ID", "Sender Name", "Recipient Name", "Amount Sent", "Fee", "Total Charge", "Date", "Status"};
        String[] remitColumns = {"remit_id", "sender_name", "recipient_name", "amount", "fee", "total_charge", "remit_date", "status"};
        dbConnect conf = new dbConnect();
        conf.viewRecords(Query, remitHeaders, remitColumns);
    }

    
    public static void viewSenders() {
        String Query = "SELECT * FROM tbl_senders";
        
       
        String[] senderHeaders = {"Sender ID", "First Name", "Last Name", "Contact", "Email", "Address"};
        String[] senderColumns = {"sender_id", "first_name", "last_name", "contact_num", "email_addr", "address"};
        dbConnect conf = new dbConnect();
        conf.viewRecords(Query, senderHeaders, senderColumns);
    }
    
    // 👥 View Recipients (Mapped from original Pig/Inventory functions)
    public static void viewRecipients() {
        String Query = "SELECT recipient_id, full_name, contact_info, bank_details FROM tbl_recipients";
        
        String[] recipientHeaders = {"Recipient ID", "Full Name", "Contact Info", "Bank Details"};
        String[] recipientColumns = {"recipient_id", "full_name", "contact_info", "bank_details"};
        dbConnect conf = new dbConnect();
        conf.viewRecords(Query, recipientHeaders, recipientColumns);
    }

   
    public static void viewUsers() {
        String Query = "SELECT * FROM tbl_user";
        
        String[] userHeaders = {"ID", "Name", "Email", "Type", "Status"};
        String[] userColumns = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        dbConnect conf = new dbConnect();
        conf.viewRecords(Query, userHeaders, userColumns);
    }

    
    public static void addRecipient(Scanner sc, dbConnect con) {
        System.out.println("\n--- ADD NEW RECIPIENT ---");
        System.out.print("Enter Recipient Full Name: ");
        sc.nextLine(); // Consume newline
        String fullName = sc.nextLine();
        System.out.print("Enter Contact Information: ");
        String contactInfo = sc.next();
        System.out.print("Enter Bank/Account Details: ");
        sc.nextLine(); // Consume newline
        String bankDetails = sc.nextLine();
        
        
        String sql = "INSERT INTO tbl_recipients(full_name, contact_info, bank_details) VALUES (?, ?, ?)";
        con.addRecord(sql, fullName, contactInfo, bankDetails);
        System.out.println("✅ New Recipient added successfully.");
    }
    
 
    public static void removeRecipient(Scanner sc, dbConnect con) {
        System.out.println("\n--- REMOVE RECIPIENT ---");
        viewRecipients();
        System.out.print("Enter Recipient ID to remove (DELETE): ");
        int recipientId = sc.nextInt();
        
        String sql = "DELETE FROM tbl_recipients WHERE recipient_id = ?";
        con.deleteRecord(sql, recipientId);
        System.out.println("❌ Recipient ID " + recipientId + " removed successfully.");
    }

    
    public static void addRemittance(Scanner sc, dbConnect con) {
        System.out.println("\n--- RECORD NEW REMITTANCE TRANSACTION ---");
        
        
        viewSenders();
        System.out.print("Enter Sender ID (or 0 to register new sender): ");
        int senderId = sc.nextInt();
        
        if (senderId == 0) {
            System.out.println("\n--- REGISTER NEW SENDER ---");
            System.out.print("Enter First Name: ");
            String fName = sc.next();
            System.out.print("Enter Last Name: ");
            String lName = sc.next();
            System.out.print("Enter Contact Number: ");
            String contact = sc.next();
            System.out.print("Enter Email Address: ");
            String email = sc.next();
            System.out.print("Enter Address: ");
            sc.nextLine(); // Consume newline
            String address = sc.nextLine();
            
            // SQL INSERT for a new sender
            String insertSenderSql = "INSERT INTO tbl_senders(first_name, last_name, contact_num, email_addr, address) VALUES (?, ?, ?, ?, ?)";
            con.addRecord(insertSenderSql, fName, lName, contact, email, address);
            
            // Re-fetch the newly created sender ID (assuming auto-increment)
            String fetchSenderIdSql = "SELECT sender_id FROM tbl_senders WHERE email_addr = ? ORDER BY sender_id DESC LIMIT 1";
            List<Map<String, Object>> result = con.fetchRecords(fetchSenderIdSql, email);
            if (!result.isEmpty()) {
                senderId = (int) result.get(0).get("sender_id");
            } else {
                System.out.println("Error: Could not retrieve new sender ID. Transaction aborted.");
                return;
            }
        }
        
        
        System.out.print("Enter Recipient Name (Full Name): ");
        sc.nextLine(); // Consume newline
        String recipientName = sc.nextLine();
        System.out.print("Enter Amount to Send: ");
        double amount = sc.nextDouble();
        System.out.print("Enter Transaction Fee: ");
        double fee = sc.nextDouble();
        
        double totalCharge = amount + fee;
        
        System.out.println("Total Amount Charged (Amount + Fee): " + totalCharge);
        System.out.print("Enter Status (e.g., Pending, Completed, Failed): ");
        String status = sc.next();

        
        String saleSql = "INSERT INTO tbl_remittances(sender_id, recipient_name, amount, fee, total_charge, remit_date, status) VALUES (?, ?, ?, ?, ?, CURDATE(), ?)";
        con.addRecord(saleSql, senderId, recipientName, amount, fee, totalCharge, status);
        
        System.out.println("✅ Remittance transaction recorded successfully.");
    }
    
    
    public static void deleteRemittance(Scanner sc, dbConnect con) {
        System.out.println("\n--- DELETE REMITTANCE TRANSACTION ---");
        viewRemittances();
        System.out.print("Enter Remittance ID to delete: ");
        int remitId = sc.nextInt();
        
        String sql = "DELETE FROM tbl_remittances WHERE remit_id = ?";
        con.deleteRecord(sql, remitId);
        System.out.println("❌ Remittance Transaction ID " + remitId + " deleted successfully.");
    }

    public static void main(String[] args) {
        dbConnect con = new dbConnect();
        con.connectDB();
        int choice;
        char cont;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("\n===== REMITTANCE MANAGEMENT SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            
         
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); 
                choice = 0; 
            }

            switch (choice) {
                case 1:
                    
                    System.out.print("Enter email: ");
                    String em = sc.next();
                    System.out.print("Enter Password: ");
                    String pas = sc.next();
                    
                    while (true) {
                        String qry = "SELECT * FROM tbl_user WHERE u_email = ? AND u_pass = ?";
                        List<Map<String, Object>> result = con.fetchRecords(qry, em, pas);
                        
                        if (result.isEmpty()) {
                            System.out.println("INVALID CREDENTIALS");
                            break;
                        } else {
                            Map<String, Object> user = result.get(0);
                            String stat = user.get("u_status").toString();
                            String type = user.get("u_type").toString();
                            
                            if(stat.equals("Pending")){
                                System.out.println("Account is Pending, Contact the Admin!");
                                break;
                            } else {
                                System.out.println("LOGIN SUCCESS!");
                                
                                if(type.equals("Admin")){
                                  
                                    int adminChoice;
                                    do {
                                        System.out.println("\n--- ADMIN DASHBOARD ---");
                                        System.out.println("1. View Remittance Transactions");
                                        System.out.println("2. View Sender Records");
                                        System.out.println("3. View Recipient Records");
                                        System.out.println("4. Approve User Accounts (Management)");
                                        System.out.println("5. Logout");
                                        System.out.print("Enter choice: ");
                                        
                                        if (sc.hasNextInt()) {
                                            adminChoice = sc.nextInt();
                                        } else {
                                            System.out.println("Invalid input. Please enter a number.");
                                            sc.next();
                                            adminChoice = 0;
                                        }
                                        
                                        switch(adminChoice) {
                                            case 1: viewRemittances(); break;
                                            case 2: viewSenders(); break;
                                            case 3: viewRecipients(); break;
                                            case 4:
                                                viewUsers();
                                                System.out.print("Enter ID to Approve: ");
                                                int ids = sc.nextInt();
                                                String sql = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
                                                con.updateRecord(sql, "Approved", ids);
                                                System.out.println("User ID " + ids + " approved.");
                                                break;
                                            case 5:
                                                System.out.println("Logging out...");
                                                break;
                                            default:
                                                System.out.println("Invalid choice.");
                                        }
                                    } while (adminChoice != 5);
                                    
                                } else if(type.equals("Staff")){ 
                                    
                                    int staffChoice;
                                    do {
                                        System.out.println("\n--- STAFF/OPERATOR DASHBOARD ---");
                                        System.out.println("1. Record New Remittance Transaction");
                                        System.out.println("2. View Remittance Transactions");
                                        System.out.println("3. View Sender Records");
                                        System.out.println("4. Add New Recipient");
                                        System.out.println("5. Remove Recipient");
                                        System.out.println("6. Delete Remittance Transaction (Correction)");
                                        System.out.println("7. Logout");
                                        System.out.print("Enter choice: ");
                                        
                                        if (sc.hasNextInt()) {
                                            staffChoice = sc.nextInt();
                                        } else {
                                            System.out.println("Invalid input. Please enter a number.");
                                            sc.next();
                                            staffChoice = 0;
                                        }
                                        
                                        switch(staffChoice) {
                                            case 1: addRemittance(sc, con); break;
                                            case 2: viewRemittances(); break;
                                            case 3: viewSenders(); break;
                                            case 4: addRecipient(sc, con); break;
                                            case 5: removeRecipient(sc, con); break;
                                            case 6: deleteRemittance(sc, con); break;
                                            case 7:
                                                System.out.println("Logging out...");
                                                break;
                                            default:
                                                System.out.println("Invalid choice.");
                                        }
                                    } while (staffChoice != 7);
                                }
                                break;
                            }
                        }
                    }
                    break;

                case 2:
                    
                    System.out.print("Enter user name: ");
                    String name = sc.next();
                    System.out.print("Enter user email: ");
                    String email = sc.next();
                    
                  
                    while (true) {
                        String qry = "SELECT * FROM tbl_user WHERE u_email = ?";
                        List<Map<String, Object>> result = con.fetchRecords(qry, email);

                        if (result.isEmpty()) {
                            break;
                        } else {
                            System.out.print("Email already exists, Enter other Email: ");
                            email = sc.next();
                        }
                    }

                    System.out.print("Enter user Type (1 - Admin/2 -Staff): ");
                    int type = sc.nextInt();
                    while(type > 2 || type < 1){
                        System.out.print("Invalid, choose between 1 & 2 only: ");
                        type = sc.nextInt();
                    }
                    String tp = (type == 1) ? "Admin" : "Staff";
                    
                    System.out.print("Enter Password: ");
                    String pass = sc.next();
                    
                   
                    String sql = "INSERT INTO tbl_user(u_name, u_email, u_type, u_status, u_pass) VALUES (?, ?, ?, ?, ?)";
                    con.addRecord(sql, name, email, tp, "Pending", pass);
                    System.out.println("Registration successful. Your account is pending admin approval.");
                    break;

                case 3:
                    System.out.println("Exiting program...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

            System.out.print("\nDo you want to continue in Main Menu? (Y/N): ");
            cont = sc.next().charAt(0);

        } while (cont == 'Y' || cont == 'y');

        System.out.println("Thank you! Program ended.");
    }
}
