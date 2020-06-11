package Main;

import java.util.Scanner;
import java.text.DecimalFormat;

//add enter pi
public class Main {

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#.##########");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Bisection and Five point formula calculator :)");

        System.out.print("Enter the Function (the variable x , + , - , * , / , ^ ,sin , cos , tan , log , ln , pi , e , and spaces are only allowed)"
                + "\nF(x) = ");

        String function = scanner.nextLine();

//        Function temp = new Function("log (x + 3 + cos pi) ", 0);      
//        System.out.println(temp.at(1));
        //enter and check if the operation is equal to 1 or 2
        int operation;

        do {
            try {
                scanner = new Scanner(System.in);
                System.out.println("\n#Enter 1 for bisection calculator."
                        + "\n#Enter 2 for five point formula calculator.");
                operation = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Wrong Input :(");
                operation = 0;
            }
        } while (operation != 1 && operation != 2);

        switch (operation) {
            case 1:
                System.out.println("---Bisection method---");
                

                double a;
                while (true)
                    try {
                        System.out.print("Enter the intial point : ");
                        scanner = new Scanner(System.in);
                        a = scanner.nextDouble();
                        break;
                    } catch (Exception e) {
                        System.out.println("Wrong Input :(");
                    }

                double b;
                while (true)
                    try {
                        System.out.print("Enter the terminal point : ");
                        scanner = new Scanner(System.in);
                        b = scanner.nextDouble();
                        break;
                    } catch (Exception e) {
                        System.out.println("Wrong Input :(");
                    }

                double errorTolerance;
                while (true)
                    try {
                        System.out.print("Enter the errorTolerance(enter 0 to get the max precision): ");
                        scanner = new Scanner(System.in);
                        errorTolerance = scanner.nextDouble();
                        break;
                    } catch (Exception e) {
                        System.out.println("Wrong Input :(");
                    }

                Bisection bisection = new Bisection(function, a, b);
                System.out.println("Root = " + df.format(bisection.findRoot(errorTolerance)));
                break;

            case 2:
                System.out.println("---Five point formula---");

                double x;
                while (true)
                    try {
                        System.out.print("Enter the the point that you want to find derivative at : ");
                        scanner = new Scanner(System.in);
                        x = scanner.nextDouble();
                        break;
                    } catch (Exception e) {
                        System.out.println("Wrong Input :(");
                    }

                double h = 0.00001;
                while (true)
                    try {
                        System.out.print("Enter h = ");
                        scanner = new Scanner(System.in);
                        h = scanner.nextDouble();
                        break;
                    } catch (Exception e) {
                        System.out.println("Wrong Input :(");
                    }

                System.out.println("F`("+x+") = " + df.format(FivePointFormula.findDerivative(function, x, h)));

        }

    }

}
