
package Graph;


public class Lorry extends Vehicle{
     //max capacity of lorry is +10 of van
      private static int Max_Capacity =  Vehicle.getMax_Capacity() + 10;
    
       
      public String toString(){
           return "Lorry: \n" + super.toString();}
      
       public boolean TestNode(Node customer){ //to check whether they are still capacity
          return super.Capacity + customer.getCapacity() <= Max_Capacity;}
       
}
