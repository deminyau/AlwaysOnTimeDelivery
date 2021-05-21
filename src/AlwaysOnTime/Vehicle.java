package AlwaysOnTime;

/**
 *
 * @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
import java.util.LinkedList;

public class Vehicle {

    private static int Max_Capacity;
    protected int Capacity;
    private static int counter = 1;
    private int Vehicle_ID;    //to indentify the vehicle
    private LinkedList<Node> PathTaken = new LinkedList<>();
    private double Path_Cost;
    private String Description;
    private static int Total_Capacity = 0;

    public static void Resetcounter() {
        counter = 1;
        Total_Capacity = 0;
    }

    public Vehicle(LinkedList<Node> list) {
        Vehicle_ID = counter;      //Ex: Vehicle 1
        counter++;
        this.addRoute(list);
    }

    public void addRoute(LinkedList<Node> list) {
        for (Node customer : list) {
            addNode(customer);
        }
    }

    public static int getMax_Capacity() {
        return Max_Capacity;
    }

    public double getPath_Cost() {
        return Path_Cost;
    }

    public static void setMax_Capacity(int Max_Capacity) {
        Vehicle.Max_Capacity = Max_Capacity;
    }

    public Vehicle() {
        Vehicle_ID = counter;      //Ex: Vehicle 1
        counter++;
    }

    public int getVehicle_ID() {
        return Vehicle_ID;
    }

    public static int getTotal_Capacity() {
        return Total_Capacity;
    }

    public LinkedList<Node> getPathTaken() {
        return PathTaken;
    }

    public void setPathTaken(LinkedList<Node> PathTaken) {
        this.PathTaken = PathTaken;
    }

    public boolean addNode(Node customer) { //should always check capacity before passing in
        Total_Capacity += customer.getCapacity();
        Capacity += customer.getCapacity();
        if (PathTaken.isEmpty()) {  //warehouse
            Path_Cost += 0;
            Description = "0";
            PathTaken.add(customer);
        } //first warehouse
        else {
            Node previous = PathTaken.get(PathTaken.size() - 1);
            Path_Cost += Graph.Euclidean(previous, customer);
            PathTaken.add(customer);
            Description += " -> " + customer.getId();
        } //end else

        return true;
    }

    public Node LatestDestination() { //method to return latest customer/node added
        return PathTaken.get(PathTaken.size() - 1);
    }

    public boolean TestNode(Node customer) { //to check whether they are still capacity
        return Capacity + customer.getCapacity() <= getMax_Capacity();
    }

    //evaluate whether we can add customer to one of the present vehicle
    public static int PossibleSource(Node destination, LinkedList<Vehicle> a) {
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
    
    
    public Node getStops (int i ){
        return PathTaken.get(i);}


    public String toString() {
        String answer = "Vehicle " + Vehicle_ID;
        answer += "\n" + Description;
        answer += "\nCapacity:" + Capacity;
        answer += "\nCost: " + Path_Cost;
        return answer;}
    
   

}
