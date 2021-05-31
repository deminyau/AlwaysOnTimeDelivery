package Algorithm;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
import AlwaysOnTime.*;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class ExtraAlgo extends Graph {

    public ExtraAlgo(String name) throws FileNotFoundException {
        super(name);
    }

    public void ExtraAlgo() {
        long start = System.currentTimeMillis();
        LinkedList<Vehicle> mincosttour = PredictedPath((double) 2 / 3); //optimal case
        total_cost_path = CalculateTourCost(mincosttour);
        //parameterize the ratio of classifying big and small node
        for (double i = 50; i < 100; i++) {
            double value = i / (double) 100;
            LinkedList<Vehicle> temp = PredictedPath(value);
            total_cost_path = CalculateTourCost(mincosttour);
            //tour cost varies for different ratio choosen to classify big and small customer
            if (total_cost_path > CalculateTourCost(temp)) {
                mincosttour = temp;
                total_cost_path = CalculateTourCost(temp);
            }
        }
        long end = start + 60 * 1000;//set time limit 
        if (System.currentTimeMillis() < end) {
            System.out.println(timer());
        }
        System.out.println("Grp123 Simulation (Extra Feature) ");
        BasicPrint(mincosttour);
        System.out.println("");
    }

    public LinkedList<Vehicle> PredictedPath(double z) {
        Reset();
        LinkedList<Vehicle> Vehicles_List = new LinkedList<>();
        LinkedList<Node> Remaining_Nodes = new LinkedList<>();
        Node temp = head.nextVertex; //dont need to add warehouse (head)

        while (temp != null) {
            Remaining_Nodes.add(temp);
            temp = temp.nextVertex;
        }
        //loop to create vehicle for each big node
        for (int i = 1; i < Number_of_customer; i++) {
            if (getNode(i).getCapacity() < Vehicle.getMax_Capacity() * z) {
                continue; //z is parameterize ratio to classify big nodes from small nodes
            }
            Vehicle v = new Vehicle();  //start from warehouse
            v.addNode(head);
            v.addNode(getNode(i)); //pre defined several vehicles using different high demand customer as a first destination
            Remaining_Nodes.remove(getNode(i));
            Vehicles_List.add(v);
        }

        //after all big nodes are created with individual vehicle, evaluate leftover small node
        //forecast the extra needed vehicles, create them using closest unserviced customer
        int Number_Extra_Vehicles = (getCapacityNeeded() - (Vehicles_List.size() * Vehicle.getMax_Capacity()))
                / Vehicle.getMax_Capacity();
        int whole_Number = (getCapacityNeeded() - (Vehicles_List.size() * Vehicle.getMax_Capacity()))
                % Vehicle.getMax_Capacity(); //find whether it is whole number

        if (whole_Number > 0) { //means have decimal places, should round off 
            Number_Extra_Vehicles += 1;
        } //because we will get 2.4 = 2 so round up to 3

        //predicted how many extra vehicles needed
        //we use closest unserviced customer as first destination by the newly assigned vehicles
        for (int i = 0; i < Number_Extra_Vehicles; i++) {
            Node[] choice = Sorted(head, Remaining_Nodes.toArray());
            Vehicle v = new Vehicle();
            v.addNode(head);    //pre-defined additional vehicles with first destination
            v.addNode(choice[0]); //used the closet unserviced customer to depot as first destination
            Remaining_Nodes.remove(choice[0]);
            Vehicles_List.add(v);
        }
        //Evaluate the remaning unserviced customer, they can be assigned to any present vehicles accodding to nearest
        while (!Remaining_Nodes.isEmpty()) {
            Node[] choice = Sorted(Remaining_Nodes.toArray()); //sort remaining customer in descending
            Node current = choice[0]; //give priority to customer with higher demands first
            int i = Vehicle.PossibleSource(current, Vehicles_List);
            //get index of ideal vehicle to add this customer (cheapest)
            if (i == -1) { //case where no vehicles can accomodate this customer
                Vehicle v = new Vehicle();
                v.addNode(head); //create new vehicle with this customer as first destination
                v.addNode(current);
                Vehicles_List.add(v);
                Remaining_Nodes.remove(current);
                continue;
            } //else add this customer to ideal vehicle
            Vehicles_List.get(i).addNode(current);
            Remaining_Nodes.remove(current);
        }
        for (int i = 0; i < Vehicles_List.size(); i++) {
            Vehicles_List.get(i).addNode(head);
        }
        return Vehicles_List;
        //return current vehicle list to be evaluated later by recursions
    }

}
