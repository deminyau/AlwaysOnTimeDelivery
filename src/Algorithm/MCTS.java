package Algorithm;

import AlwaysOnTime.*;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;

public class MCTS extends Graph {

    private int level = 3;
    private int iterations = 100;
    private int ALPHA = 1;
    private int N = Graph.Number_of_customer;
    private double[][][] policy = new double[level][N][N];
    private double[][] globalPolicy = new double[N][N];
    private Node[] allStops = new Node[N];

    public MCTS(String name) throws FileNotFoundException {
        super(name);
        AssignStops();
    }

    public void AssignStops() {
        Node temp = head;
        for (int i = 0; i < allStops.length; i++) {
            allStops[i] = temp;
            temp = temp.nextVertex;
        }
    }

    public void MCTS_Simulation() {
        LinkedList<Vehicle> answer = search(level, iterations);
        System.out.println("MCTS simulation");
        BasicPrint(answer);
    }

    public LinkedList<Vehicle> search(int level, int iterations) {
        int levels=level-1;//I gues level 3 means level 0,1,2 because I do like this also wont get -1 IndexOutOfBound
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
            }
            globalPolicy = policy[levels];
        }
        return besttour;
    }

    public void adapt(LinkedList<Vehicle> a_tour, int level) {//a_tour, level

        for (int i = 0; i < a_tour.size(); i++) {
            Vehicle Route = a_tour.get(i);
            LinkedList<Node> Stops = Route.getPathTaken();
            for (int j = 0; j < Stops.size(); j++) {
                policy[level][j][j + 1] += ALPHA;
                double z = 0.0;
                Node currentStop = Route.getStops(j);
                for (int k = 0; k < allStops.length; k++) {
                    if (currentStop.testNode(allStops[k]) && !allStops[k].visited) {
                        z += Math.exp(globalPolicy[j][k]);
                    }
                }
                for (int k = 0; k < allStops.length; k++) {
                    if (currentStop.testNode(allStops[k]) && !allStops[k].visited) {
                        policy[level][j][k] -= ALPHA * (Math.exp(globalPolicy[j][k]) / z);
                    }
                }
                currentStop.visited = true;
            }
        }
    }

    public LinkedList<Vehicle> rollout() {
        LinkedList<Vehicle> newTour = new LinkedList<>();
        Vehicle v = new Vehicle();
        v.addNode(head);
        newTour.add(v);

        while (true) {
            LinkedList<Node> remaining = Node.getRemaining(); //return all nodes that are unvisited
            Node currentStop = newTour.get(newTour.size() - 1).LatestDestination();
            if (currentStop.Unchecked.isEmpty()) {
                newTour.get(newTour.size() - 1).addNode(head);
                if (remaining.isEmpty()) {
                    break;
                } else {
                    Vehicle v1 = new Vehicle();
                    v1.addNode(head);
                    newTour.add(v1);
                    continue;
                }
            }
            Node nextStop = select_next_move(currentStop, currentStop.Unchecked);
            if (newTour.get(newTour.size() - 1).TestNode(nextStop)) {
                newTour.get(newTour.size() - 1).addNode(nextStop);
                nextStop.visited = true;
            } else {
                currentStop.Unchecked.remove(nextStop);
            }
        }
        return newTour;
    }

    public Node select_next_move(Node currentStop, LinkedList<Node> possible_successor) {
        Double[] probability = new Double[possible_successor.size()];
        double sum = 0;
        for (int i = 0; i < possible_successor.size(); i++) {
            probability[i] = Math.exp(globalPolicy[currentStop.getId()][i]);
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

}
