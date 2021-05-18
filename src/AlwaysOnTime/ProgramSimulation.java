package AlwaysOnTime;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @Group 123
 * @author Chiew Zhe Wei Hong Zhao Cheng Yau De Min Wong Yu Xuan
 */
public class ProgramSimulation {

    public static void main(String[] args) {
        Scanner group123 = new Scanner(System.in);
        System.out.println("------Never On Time Sdn Bhd------");
        System.out.println("");
        System.out.print("Enter file: ");
        String filename = group123.nextLine();
        try {
            Algorithm al = new Algorithm(filename);
            message1();

            int choice;

            //System.out.print("Enter choice to be proceed: ");
            do {
                System.out.print("Enter choice to be proceed: ");
                choice = group123.nextInt();
                System.out.println("-----------------------------");
                if (choice == 1) {
                    //al.BasicSimulation
                    System.out.println("-----------------------------");
                } else if (choice == 2) {
                    al.GreedySimulation();
                    System.out.println("-----------------------------");
                } else if (choice == 3) {
                    //al.MCTS();
                    System.out.println("-----------------------------");
                } else if (choice == 4) {
                    al.AStarSimulation();
                    System.out.println("-----------------------------");
                } else if (choice == 5) {
                    al.BestFirstSimulation();
                    System.out.println("-----------------------------");
                } else if (choice == 6) {
                    al.CnnditionalSimulation();
                    System.out.println("-----------------------------");
                } else if (choice == 7) {
                    al.SDHCSimulation();
                    System.out.println("-----------------------------");
                } else {
                }
            } while (choice != 8);
            System.out.println("");
            System.out.println("Exit successfully");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

    }

    public static void message1() {
        System.out.println("Choose algorithm to generate best route");
        System.out.println("1 - Basic Simulation");
        System.out.println("2 - Greedy Simulation");
        System.out.println("3 - MCTS Simulation");
        System.out.println("4 - A* Search Simulation");
        System.out.println("5 - Best First Search Simulation");
        System.out.println("6 - Conditional Simulation");
        System.out.println("7 - Simulation with lorry");
        System.out.println("8 - Exit");
    }
}
