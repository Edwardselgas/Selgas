package selgas.edward;

import java.util.Scanner;

    public class SelgasEdward {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        
        System.out.print("Enter Science marks: ");
        int sci = sc.nextInt();
        System.out.print("Enter History marks: ");
        int his = sc.nextInt();
        System.out.print("Enter Math marks: ");
        int math = sc.nextInt();
        System.out.print("Enter Soc marks: ");
        int soc = sc.nextInt();
        System.out.print("Enter Arts marks: ");
        int arts = sc.nextInt();

        
        int total = sci + his + math + soc + arts;
        double percent = total / 5.0;

       
        String remarks = "";

        if (percent < 70) {
            remarks = "Fail";
        } else if (percent <= 75) {
            remarks = "Poor";
        } else if (percent <= 80) {
            remarks = "Fair";
        } else if (percent <= 85) {
            remarks = "Good";
        } else if (percent <= 90) {
            remarks = "Very Good";
        } else {
            remarks = "Excellent";
        }

        System.out.println("Total: " + total);
        System.out.println("Percentage: " + percent);
        System.out.println("Remarks: " + remarks);

        
        if (percent < 70) {
            System.out.println(name + ", you failed. Try harder next time.");
        } else {
            System.out.println("Well done " + name + " \nYou passed.");
        }

        sc.close();
    }
}