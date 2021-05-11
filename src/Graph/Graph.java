
package Graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    private Node head;
    private static int Number_of_customer;
    private double total_cost_path;
    
  
     public boolean addVertex(int x, int y, int c){
        Node temp = head;
        Node newNode = new Node(x,y,c); //info and next vertex (null)
        if (head == null) head = newNode;
        else { 
            while (temp.nextVertex != null) { 
            temp = temp.nextVertex;}
            temp.nextVertex = newNode;}
        return true;}
     
     public Graph (String name) throws FileNotFoundException{
        Scan(name);} //input filename

     public void Scan(String name) throws FileNotFoundException{
         Scanner sc = new Scanner (new FileInputStream(name));
           String temp = sc.nextLine();   //first line of input
           String [] tokens = temp.split(" ");
           Number_of_customer = Integer.parseInt(tokens [0]);
           Vehicle.setMax_Capacity(Integer.parseInt(tokens[1]));
           int size = 0; //to see the size added is equal to text file
           while (sc.hasNextLine()){ //input with information of nodes
           size++;
           temp = sc.nextLine();
           tokens = temp.split(" ");
           addVertex(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]) ,Integer.parseInt(tokens[2]));
         }//end while
    }    
     

     public static double Euclidean(Node a, Node b){
         double eq1 = Math.pow(b.getX() - a.getX() , 2.0);
         double eq2 = Math.pow(b.getY() - a.getY() , 2.0);
         return Math.sqrt(eq1 + eq2);}
     
    public Node getNode(int index){
        Node temp = head;
        for (int i = 0; i< index ; i++) temp = temp.nextVertex;
        return temp;}
     
     public void GreedySimulation(){
         Reset(); 
         ArrayList <Vehicle> Vehicles_List = new ArrayList<>();
         ArrayList <Node> Remaining_Nodes = new ArrayList<>();
         Node temp = head.nextVertex; //dont need to add warehouse (head)
         
         while (temp != null) { //list of all customer to be visited/ will gradually be removed 
         Remaining_Nodes.add(temp);
         temp = temp.nextVertex;}
         
          
          while (!Remaining_Nodes.isEmpty()){
             int current = 0; //store the current node/customer / initially is warehouse
             Vehicle v = new Vehicle();
             v.addNode(head); //start from warehouse
             Node [] choice ;
             //all the available customer to evaluate on that particular node with exception of warehouse and visited customer
             
             while (true){
                 if (Remaining_Nodes.isEmpty()) break;  //no more customers 
                 choice = Sorted(getNode(current), Remaining_Nodes.toArray());
                 //sort all the avaliable destination in ascending order of cost path 
                 //add 0 because closest node
                 if (!v.TestNode(choice[0])) break; //not enough capacity in vehicle
                 v.addNode(choice[0]);              //add customer to vehicle path 
                 Remaining_Nodes.remove(choice[0]); //this customer is serviced
                 current = choice[0].getId();}      //update pointer to latest customer being serviced
             
             
             v.addNode(head); // back to warehouse
             Vehicles_List.add(v);} //next vehicle end while
         
         for (int i = 0; i< Vehicles_List.size() ; i++){
             total_cost_path += (Vehicles_List.get(i)).getPath_Cost();}
         
         System.out.println("Greedy simulation");
         System.out.println("Tour \nTotal Cost: " + total_cost_path);
         //display all vehicles 
         for (int i = 0; i< Vehicles_List.size() ; i++){
             System.out.println(Vehicles_List.get(i));}
     }
     
    
     
     
     public void Reset (){ //reset all visited = false; for diff simulation
         Node temp = head;
         while (temp != null) {
          temp.visited = false;
         temp = temp.nextVertex;}
         total_cost_path = 0;} 
    
     
     public Node [] Sorted (Node source,Object [] before_cast){
             Node [] a = new Node [before_cast.length];
             for (int i = 0; i < before_cast.length;i++) {
             a[i] = (Node) (before_cast[i]);} //cast one by one since cannot cast all at once
         
           for (int pass = 0; pass < a.length - 1; pass++) { //sort array in descending order
            for (int i = 0; i < a.length - 1 - pass; i++) {
                         if (Euclidean(source, a[i]) > Euclidean(source, a[i+1])){
                           Node temp = a[i];
                           a[i] = a[i+1];
                           a[i+1] = temp;}}}
     return a;}

}

        
    

