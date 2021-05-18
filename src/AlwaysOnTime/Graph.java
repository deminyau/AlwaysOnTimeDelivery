package AlwaysOnTime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {

    protected Node head;
    protected static int Number_of_customer;
    protected double total_cost_path;

    public Graph() {
    }

    public boolean addVertex(int x, int y, int c) {
        Node temp = head;
        Node newNode = new Node(x, y, c); //info and next vertex (null)
        if (head == null) {
            head = newNode;
        } else {
            while (temp.nextVertex != null) {
                temp = temp.nextVertex;
            }
            temp.nextVertex = newNode;
        }
        return true;
    }

    public Graph(String name) throws FileNotFoundException {
        Scan(name);
    } //input filename

    public void Scan(String name) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream(name));
        String temp = sc.nextLine();   //first line of input
        String[] tokens = temp.split(" ");
        Number_of_customer = Integer.parseInt(tokens[0]);
        Vehicle.setMax_Capacity(Integer.parseInt(tokens[1]));
        int size = 0; //to see the size added is equal to text file
        while (sc.hasNextLine()) { //input with information of nodes
            size++;
            temp = sc.nextLine();
            tokens = temp.split(" ");
            addVertex(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
        }//end while
    }

    public static double Euclidean(Node a, Node b) {
        double eq1 = Math.pow(b.getX() - a.getX(), 2.0);
        double eq2 = Math.pow(b.getY() - a.getY(), 2.0);
        return Math.sqrt(eq1 + eq2);
    }

    public Node getNode(int index) {
        Node temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.nextVertex;
        }
        return temp;
    }

    public void Reset() { //reset all visited = false; for diff simulation
        Node temp = head;
        while (temp != null) {
            temp.visited = false;
            temp = temp.nextVertex;
        }
        total_cost_path = 0;
        Vehicle.Resetcounter();
    }

    public int getCapacityNeeded() {
        int total = 0;
        Node temp = head;
        while (temp != null) {
            total += temp.getCapacity();
            temp = temp.nextVertex;
        }
        return total;
    }

    public Node[] Sorted(Object[] before_cast) { //sort descending order Capacity
        Node[] a = new Node[before_cast.length];
        for (int i = 0; i < before_cast.length; i++) {
            a[i] = (Node) (before_cast[i]);
        }
        for (int pass = 0; pass < a.length - 1; pass++) { //sort array in descending order
            for (int i = 0; i < a.length - 1 - pass; i++) {
                if (a[i].getCapacity() < a[i + 1].getCapacity()) {
                    Node temp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = temp;
                }
            }
        }
        return a;
    }

    //ascending order in distance from one node to another
    public Node[] Sorted(Node source, Object[] before_cast) {
        Node[] a = new Node[before_cast.length];
        for (int i = 0; i < before_cast.length; i++) {
            a[i] = (Node) (before_cast[i]);
        } //cast one by one since cannot cast all at once

        for (int pass = 0; pass < a.length - 1; pass++) {
            for (int i = 0; i < a.length - 1 - pass; i++) {
                if (Euclidean(source, a[i]) > Euclidean(source, a[i + 1])) {
                    Node temp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = temp;
                }
            }
        }
        return a;
    }

    public void BasicPrint(ArrayList<Vehicle> Vehicles_List) {
        System.out.println("Tour \nTotal Cost: " + total_cost_path);
        //display all vehicles 
        for (int i = 0; i < Vehicles_List.size(); i++) {
            System.out.println(Vehicles_List.get(i));
        }
    }

    public double CalculateTourCost(ArrayList<Vehicle> Vehicles_List) {
        double tourcost = 0;
        for (int i = 0; i < Vehicles_List.size(); i++) {
            tourcost += (Vehicles_List.get(i)).getPath_Cost();
        }
        return tourcost;
    }

    public ArrayList<Vehicle> PredictedPath(double z) {
        Reset();
        ArrayList<Vehicle> Vehicles_List = new ArrayList<>();
        ArrayList<Node> Remaining_Nodes = new ArrayList<>();
        Node temp = head.nextVertex; //dont need to add warehouse (head)

        while (temp != null) {
            Remaining_Nodes.add(temp);
            temp = temp.nextVertex;
        }
        //loop to create vehicle for each big node
        for (int i = 1; i < Number_of_customer; i++) {
            if (getNode(i).getCapacity() < Vehicle.getMax_Capacity() * z) {
                continue;
            }
            Vehicle v = new Vehicle();  //start from warehouse
            v.addNode(head);
            v.addNode(getNode(i));
            Remaining_Nodes.remove(getNode(i));
            Vehicles_List.add(v);
        }

        //after all big nodes are created with individual vehicle, evaluate leftover small node
        //forecast the extra needed vehicles, create them using closest unserviced customer
        int Number_Extra_Vehicles = (getCapacityNeeded() - (Vehicles_List.size() * Vehicle.getMax_Capacity()))
                / Vehicle.getMax_Capacity();
        int whole_Number = (getCapacityNeeded() - (Vehicles_List.size() * Vehicle.getMax_Capacity()))
                % Vehicle.getMax_Capacity(); //find whehter it is whole number

        if (whole_Number > 0) { //means have decimal places, should round off 
            Number_Extra_Vehicles += 1;
        } //because we will get 2.4 = 2 so round up to 3

        for (int i = 0; i < Number_Extra_Vehicles; i++) {
            Node[] choice = Sorted(head, Remaining_Nodes.toArray());
            Vehicle v = new Vehicle();
            v.addNode(head);
            v.addNode(choice[0]);
            Remaining_Nodes.remove(choice[0]);
            Vehicles_List.add(v);
        }

        while (!Remaining_Nodes.isEmpty()) {
            Node[] choice = Sorted(Remaining_Nodes.toArray()); //sort remaining customer in descending order of capacity
            Node current = choice[0]; //give priority to customer with higher priority count
            int i = Vehicle.PossibleSource(current, Vehicles_List);
            if (i == -1) { //get index of ideal vehicle to add this customer
                Vehicle v = new Vehicle();
                v.addNode(head);
                v.addNode(current);
                Vehicles_List.add(v);
                Remaining_Nodes.remove(current);
                continue;
            }
            Vehicles_List.get(i).addNode(current);
            Remaining_Nodes.remove(current);
        }
        for (int i = 0; i < Vehicles_List.size(); i++) {
            Vehicles_List.get(i).addNode(head);
        }
        return Vehicles_List;
        //everything done i have to add final destination
    }

    public ArrayList<Node> getSiteDependent(Object[] before_cast) {//For shdc simulation
        ArrayList<Node> a = new ArrayList<>();
        for (int i = 0; i < before_cast.length; i++) {
            Node temp = (Node) (before_cast[i]);
            if (temp.isSite_Dependent()) {
                a.add(temp);
            }
        }
        return a;
    }

}
