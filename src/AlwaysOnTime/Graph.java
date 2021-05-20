package AlwaysOnTime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
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

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public static int getNumber_of_customer() {
        return Number_of_customer;
    }

    public static void setNumber_of_customer(int Number_of_customer) {
        Graph.Number_of_customer = Number_of_customer;
    }

    public double getTotal_cost_path() {
        return total_cost_path;
    }

    public void setTotal_cost_path(double total_cost_path) {
        this.total_cost_path = total_cost_path;
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
        Node.resetCounter();
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

    //this sorted() is only used by Grp 123Algo (Algo by Group123) simulation 
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

    //this 2 sorted have diff param (overload), so it is ok u keep them same name
    //this sorted() is used by  Greedy, Grp 123 ,SDHC simulation
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

    //
}
