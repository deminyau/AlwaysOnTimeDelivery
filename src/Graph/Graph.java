
package Graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
     

     public double Euclidean(Node a, Node b){
         double eq1 = Math.pow(b.getX() - a.getX() , 2.0);
         double eq2 = Math.pow(b.getY() - a.getY() , 2.0);
         return Math.sqrt(eq1 + eq2);}
     
     public void Display (){
         Node temp = head.nextVertex;
         while (temp != null){
             System.out.print("Distance between " + head.getId() + " " + temp.getId() + " : ") ;
             System.out.print(Euclidean(head, temp));
             System.out.println("");
         temp = temp.nextVertex;}}
     }

        
    

