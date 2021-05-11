
package Graph;

import java.util.ArrayList;

public class Vehicle {
    private static int Max_Capacity;
    private int Capacity;
    private static int counter = 1;
    private int Vehicle_ID;    //to indentify the vehicle
    private ArrayList <Node> PathTaken = new ArrayList<>();
    private double Path_Cost;
    private String Description ;

    public double getPath_Cost() {
        return Path_Cost;}
    
    public static void setMax_Capacity(int Max_Capacity) {
        Vehicle.Max_Capacity = Max_Capacity;}
    
    public Vehicle (){ 
         Vehicle_ID = counter;      //Ex: Vehicle 1
         counter ++;}
    
    public boolean addNode(Node customer){ //should always check capacity before passing in
            Capacity += customer.getCapacity();
            if (PathTaken.isEmpty()){  //warehouse
                Path_Cost += 0; 
                Description = "0";
                PathTaken.add(customer);} //first warehouse
            
            else{
            Node previous = PathTaken.get(PathTaken.size()-1);
            Path_Cost += Graph.Euclidean(previous, customer);
            PathTaken.add(customer);
            Description += " -> " + customer.getId();} //end else

            return true;}
    
      public boolean TestNode(Node customer){ //to check whether they are still capacity
          return Capacity + customer.getCapacity() <= Max_Capacity;}
    
    public String toString(){
        String answer = "Vehicle " + Vehicle_ID;
        answer += "\n" + Description;
        answer += "\nCapacity:" + Capacity;
        answer += "\nCost: " + Path_Cost; 
        return answer;}
    
}
