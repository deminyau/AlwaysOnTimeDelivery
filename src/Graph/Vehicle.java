
package Graph;

import java.util.ArrayList;

public class Vehicle {
    private static int Max_Capacity;
    private int Capacity;
    private static int counter = 1;
    private int Vehicle_ID;    //to indentify the vehicle
    private ArrayList <Node> PathTaken = new ArrayList<>();

    public static void setMax_Capacity(int Max_Capacity) {
        Vehicle.Max_Capacity = Max_Capacity;}
    
    public Vehicle (){ 
         Vehicle_ID = counter;      //Ex: Vehicle 1
         counter ++;}
    
    public boolean addCustomer(Node customer){
        
        return false;
    }
    
    
    
}
