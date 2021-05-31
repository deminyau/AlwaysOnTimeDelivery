package AlwaysOnTime;

import java.io.FileNotFoundException;
import java.util.Scanner;
import Algorithm.*;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
public class NeverOnTimeSdnBhd {

    protected static String file = "";
    public static Scanner group123 = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        Thread MCTSThread = new Thread(new MultiThreading());//Multithreading for MCTS
        System.out.println("*********************************");
        System.out.println("------Never On Time Sdn Bhd------");
        System.out.println("*********************************");
        try {
            System.out.print("Enter file: ");
            String filename = group123.nextLine();
            file = filename;
            MultiThreading.mcts = new MCTS(NeverOnTimeSdnBhd.file);
            MCTSThread.start();
            System.out.println("*********************************");
            message1();
            int choice;
            //System.out.print("Enter choice to be proceed: ");
            do {

                System.out.print("Enter choice to proceed: ");
                choice = group123.nextInt();
                System.out.println("-----------------------------");
                System.out.println("");

                if (choice == 1) {

                    BasicDFS dfs = new BasicDFS(filename);
                    System.out.println("-----------------------------");

                } else if (choice == 2) {

                    Greedy g = new Greedy(filename);
                    g.GreedySimulation();
                    System.out.println("-----------------------------");

                } else if (choice == 3) {
                    if (MultiThreading.answer != null) {//If there is already any mcts result found
                        //Print the result found
                        double total_cost_path = 0;
                        for (int i = 0; i < MultiThreading.answer.size(); i++) {
                            total_cost_path += MultiThreading.answer.get(i).getPath_Cost();
                        }
                        System.out.println("MCTS Simulation");
                        System.out.println("Tour \nTotal Cost: " + total_cost_path);
                        for (int i = 0; i < MultiThreading.answer.size(); i++) {
                            System.out.println(MultiThreading.answer.get(i));
                        }
                        System.out.println("-----------------------------");
                        System.out.println("Would you like to run MCTS all over again? (true/false)");
                        if (group123.nextBoolean()) {
                            System.out.println("Would you like to custom the hyperparameters? (true/false)");
                            if (group123.nextBoolean()) {
                                System.out.println("Enter number of levels: (<=3 levels is strongly recommended,level above 3 will exponentially increase time consumption.)");
                                int level = group123.nextInt();
                                MCTS mcts = new MCTS(filename, level);
                                System.out.println("Enter number of iterations: ");
                                mcts.setIterations(group123.nextInt());
                                System.out.println("Enter value of ALPHA: ");
                                mcts.setALPHA(group123.nextInt());
                                MultiThreading.mcts = mcts;
                            } else {
                                MCTS mcts = new MCTS(filename);
                                MultiThreading.mcts = mcts;
                            }
                            Thread MCTS = new Thread(new MultiThreading());
                            MCTS.start();
                            System.out.println("A new MCTS thread has been created. We will notify you once the simulation is done");

                        }
                    } else {//If no any mcts result yet
                        System.out.println("MCTS Simulation thread is still searching for the result. Come back later, we will notify you once complete.");
                    }
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
        System.out.println("-----------------------------");
    }
}
