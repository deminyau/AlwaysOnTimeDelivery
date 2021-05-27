package Algorithm;

import AlwaysOnTime.*;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */

public class MCTS extends Graph {
    //configuration set up-----------
    private int level = 3;
    private int iterations = 100;// must > 0
    private int ALPHA = 1;
    //--------------------------------
    private int N = Graph.Number_of_customer;
    private double[][][] policy = new double[level][N][N];
    private double[][] globalPolicy = new double[N][N];
    private Node[] allStops = new Node[N];

    public MCTS(String name) throws FileNotFoundException {
        super(name);
        AssignStops();
    }

    public void AssignStops() {//Store all customers
        Node temp = head;
        for (int i = 0; i < allStops.length; i++) {
            allStops[i] = temp;
            temp = temp.nextVertex;
        }
    }

    public void MCTS_Simulation() throws InterruptedException {
        Reset();
        if (Number_of_customer >= 15) {
            stopper();
            System.out.println("");
            LinkedList<Vehicle> answer = search(level, iterations);
            System.out.println("MCTS simulation");
            BasicPrint(answer);
            System.out.println("");
        } else {
            System.out.println(timer());
            LinkedList<Vehicle> answer = search(level, iterations);
            System.out.println("MCTS simulation");
            BasicPrint(answer);
            System.out.println("");
        } 
    }

    public LinkedList<Vehicle> search(int level, int iterations) {
        long start = System.currentTimeMillis();
        int levels = level - 1;
        double minTourCost = Double.MAX_VALUE;
        LinkedList<Vehicle> besttour = null;
        if (level == 0) {
            return rollout();
        } else {
            policy[levels] = globalPolicy;
            for (int i = 0; i < iterations; i++) {
                LinkedList<Vehicle> newtour = search(level - 1, iterations);
                if (CalculateTourCost(newtour) < minTourCost) {
                    minTourCost = CalculateTourCost(newtour);
                    besttour = newtour;
                    adapt(besttour, levels);
                }
                long end = start + 30 * 1000;//set time limit 
                if (System.currentTimeMillis() > end) {
                    return besttour;
                } 
            }
            globalPolicy = policy[levels];
        }
        total_cost_path = minTourCost;
        return besttour;
    }

    public void adapt(LinkedList<Vehicle> a_tour, int level) {//a_tour, level

        for (int i = 0; i < a_tour.size(); i++) {
            Vehicle Route = a_tour.get(i);
            LinkedList<Node> Stops = Route.getPathTaken();
            for (int j = 0; j < Stops.size() - 1; j++) {
                policy[level][Stops.get(j).getId()][Stops.get(j + 1).getId()] += ALPHA;
                double z = 0.0;
                Node currentStop = Route.getStops(j);
                for (int k = 0; k < allStops.length; k++) {
                    if (currentStop.testNode(allStops[k]) && !allStops[k].visited) {
                        z += Math.exp(globalPolicy[currentStop.getId()][allStops[k].getId()]);
                    }
                }
                for (int k = 0; k < allStops.length; k++) {
                    if (currentStop.testNode(allStops[k]) && !allStops[k].visited) {
                        policy[level][currentStop.getId()][allStops[k].getId()]
                                -= ALPHA * (Math.exp(globalPolicy[currentStop.getId()][allStops[k].getId()]) / z);
                    }
                }
                currentStop.visited = true;
            }
        }
    }

    public LinkedList<Vehicle> rollout() {
        Vehicle.Resetcounter();
        LinkedList<Vehicle> newTour = new LinkedList<>();
        Vehicle v = new Vehicle();
        v.addNode(head); //initialize new_tour with first route with first stop at 0
        newTour.add(v);
        for (Node node : Graph.allCustomers) {
            node.visited = false;
        }
        while (true) {
            Node currentStop = newTour.get(newTour.size() - 1).LatestDestination();
            LinkedList<Node> Possible = new LinkedList<>();
            Vehicle currentV = newTour.get(newTour.size() - 1);
            //find every possible successors that is not yet checked for the currentStop
            for (int i = 0; i < Graph.getNumber_of_customer(); i++) {
                Node temp = Graph.allCustomers.get(i);
                if (temp.getId() == 0) {
                    continue;
                }
                if (!RepeatedNode(temp, newTour) && currentV.TestNode(temp) && temp.checked == false) {
                    Possible.add(temp);
                }
            }
            if (Possible.isEmpty()) {
                newTour.get(newTour.size() - 1).addNode(head);
                if (AllStopsVisited())//all stops visited
                {
                    break;
                }
                Vehicle vehicle = new Vehicle();
                vehicle.addNode(head);
                newTour.add(vehicle);
                for (Node node : Graph.allCustomers) {
                    if (node.getId() == 0) {
                        continue;
                    }
                    node.checked = false;
                    if (!RepeatedNode(node, newTour)) {
                        node.visited = false;
                    }
                }
                continue;
            }
            Node nextStop = select_next_move(currentStop, Possible);
            if (!RepeatedNode(nextStop, newTour) && newTour.get(newTour.size() - 1).TestNode(nextStop)) {
                newTour.get(newTour.size() - 1).addNode(nextStop);
                nextStop.visited = true;
            } else {
                nextStop.checked = true;
            }
        }
        return newTour;
    }

    public Node select_next_move(Node currentStop, LinkedList<Node> possible_successor) {
        Double[] probability = new Double[possible_successor.size()];
        double sum = 0;
        for (int i = 0; i < possible_successor.size(); i++) {
            probability[i] = Math.exp(globalPolicy[currentStop.getId()][possible_successor.get(i).getId()]);
            sum += probability[i];
        }
        double mrand = new Random().nextDouble() * sum;
        int j = 0;
        sum = probability[0];
        while (sum < mrand) {
            sum += probability[++j];
        }

        return possible_successor.get(j);
    }

    public boolean AllStopsVisited() {//assign the node(customer) as visited and will not be explored anymore
        for (Node node : Graph.allCustomers) {
            if (node.getId() == 0) {
                continue;
            } else {
                if (!node.visited) {
                    return false;
                }
            }
        }
        return true;
    }

    public void stopper() throws InterruptedException {
        String formatter = "\r%s%6ds";
        StringBuilder timeline = new StringBuilder();
        timeline.append("Time Elapsed: |");
        int i = 0;
        while (i < 50) {
            i++;
            Thread.sleep(500);
            if (i != 50) {
                timeline.append("=");
            } else {
                timeline.append("=| ");
                timeline.append(" (Simulation stop!) Max time : >");
            }
            System.out.printf(formatter, timeline, i);
        }
        
    }

    public boolean RepeatedNode(Node node, LinkedList<Vehicle> newTour) {//check whether there no repeated customer in the same tour
        for (Vehicle v : newTour) {
            LinkedList<Node> route = v.getPathTaken();
            if (route.contains(node)) {
                return true;
            }
        }
        return false;
    }
}
