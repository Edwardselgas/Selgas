
package selgas.edward;

import java.util.Scanner;

public class ACT3 {
   
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a number: ");
        int num = sc.nextInt();

        if (num == 0) {
            System.out.println("The number is zero.");
        } else if (num > 0) {
            System.out.println("The number is positive.");
        } else {
            System.out.println("The number is negative.");
        }

        sc.close();
    }
}