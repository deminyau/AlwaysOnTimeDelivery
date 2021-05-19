package SearchTree;

import java.util.*;

public class Vehicle {

    private static int maxcapacity;
    private int Capacity;
    private static int counter = 1;
    private int Vehicle_ID;    //to indentify the vehicle
    private LinkedList<Customer> PathTaken = new LinkedList<>();
    private double Path_Cost;
    private String Description;
    private static int Total_Capacity = 0;

    public static void Resetcounter() {
        counter = 1;
        Total_Capacity = 0;
    }

    public static int getMaxcapacity() {
        return maxcapacity;
    }

    public double getPath_Cost() {
        return Path_Cost;
    }

    public static void setMax_Capacity(int Max_Capacity) {
        Vehicle.maxcapacity = Max_Capacity;
    }

    public Vehicle() {
        Vehicle_ID = counter;      //Ex: Vehicle 1
        counter++;
    }

    public Vehicle(LinkedList<Customer> list) {
        Vehicle_ID = counter;      //Ex: Vehicle 1
        counter++;
        this.addRoute(list);
    }
    public LinkedList<Customer> getPathTaken() {
        return PathTaken;
    }

    public int getVehicle_ID() {
        return Vehicle_ID;
    }

    public static int getTotal_Capacity() {
        return Total_Capacity;
    }

    public static void setMaxcapacity(int maxapacity) {
        Vehicle.maxcapacity = maxapacity;
    }

    public boolean addNode(Customer customer) { //should always check capacity before passing in
        Total_Capacity += customer.getCapacity();
        Capacity += customer.getCapacity();
        if (PathTaken.isEmpty()) {  //warehouse
            Path_Cost += 0;
            Description = "0";
            PathTaken.add(customer);
        } //first warehouse
        else {
            Customer previous = PathTaken.get(PathTaken.size() - 1);
            Path_Cost += Graph.Euclidean(previous, customer);
            PathTaken.add(customer);
            Description += " -> " + customer.getID();
        } //end else

        return true;
    }
    
    public void addRoute(LinkedList<Customer> list){
        for(Customer customer:list){
            addNode(customer);
        }
    }
    public Customer LatestDestination() { //method to return latest customer/node added
        return PathTaken.get(PathTaken.size() - 1);
    }

    public boolean TestNode(Customer customer) { //to check whether they are still capacity
        return Capacity + customer.getCapacity() <= maxcapacity;
    }

    //evaluate whether we can add customer to one of the present vehicle
    public static int PossibleSource(Customer destination, ArrayList<Vehicle> a) {
        int index = -1;
        double min = 10000;

        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).TestNode(destination)) {

                if (Graph.Euclidean(destination, a.get(i).LatestDestination()) < min) {
                    min = Graph.Euclidean(destination, a.get(i).LatestDestination());
                    index = i;
                }
            }
        }
        //none of the vehicle can accomodate this customer
        return index;
    }

    public String toString() {
        String answer = "Vehicle " + Vehicle_ID;
        answer += "\n" + Description;
        answer += "\nCapacity:" + Capacity;
        answer += "\nCost: " + Path_Cost;
        return answer;
    }

}
