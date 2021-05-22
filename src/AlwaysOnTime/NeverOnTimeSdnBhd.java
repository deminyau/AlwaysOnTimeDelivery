package AlwaysOnTime;

import java.io.FileNotFoundException;
import java.util.Scanner;
import Algorithm.*;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
public class NeverOnTimeSdnBhd {

    public static Scanner group123 = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
    
        System.out.println("*********************************");
        System.out.println("------Never On Time Sdn Bhd------");
        System.out.println("*********************************");
        try {
            System.out.print("Enter file: ");
            String filename = group123.nextLine();
            System.out.println("*********************************");
            message1();
            int choice;
            //System.out.print("Enter choice to be proceed: ");
            do {

                System.out.print("Enter choice to be proceed: ");
                choice = group123.nextInt();
                System.out.println("-----------------------------");
                System.out.println("");

                if (choice == 1) {

                    BasicBFS bfs = new BasicBFS(filename);
                    System.out.println("-----------------------------");

                } else if (choice == 2) {

                    Greedy g = new Greedy(filename);
                    g.GreedySimulation();
                    System.out.println("-----------------------------");

                } else if (choice == 3) {

                    //MCTS mcts = new MCTS(filename);
                    //mcts.MCTS_Simulation;
                    System.out.println("-----------------------------");

                } else if (choice == 4) {

                    A_Star as = new A_Star(filename);
                    as.AStarSimulation();
                    System.out.println("-----------------------------");

                } else if (choice == 5) {

                    Best_First bf = new Best_First(filename);
                    bf.BFirstSimulation();
                    System.out.println("-----------------------------");

                } else if (choice == 6) {

                    ExtraAlgo ea = new ExtraAlgo(filename);
                    ea.ExtraAlgo();
                    System.out.println("-----------------------------");

                } else if (choice == 7) {

                    SDHC sdhc = new SDHC(filename);
                    sdhc.SDHCSimulation();
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
        System.out.println("6 - Grp123 Simulation");
        System.out.println("7 - Simulation (SiteDependent + Homegenous Capacity)");
        System.out.println("8 - Exit");
        System.out.println("*********************************");
        System.out.println("");
    }
}
