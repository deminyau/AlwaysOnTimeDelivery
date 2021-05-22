package Algorithm;

import AlwaysOnTime.*;
import java.io.FileNotFoundException;
import java.util.LinkedList;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
public class A_Star extends Graph {
    //use distance between warehouse and customer + distance between 2 customers    

    public A_Star(String name) throws FileNotFoundException {
        super(name);
    }

    public void AStarSimulation() {
        LinkedList<Vehicle> Vehicles_List = new LinkedList<>();
        LinkedList<Node> Remaining_Nodes = new LinkedList<>();
        Node temp = head.nextVertex; //dont need to add warehouse (head)

        while (temp != null) { //list of all customer to be visited/ will gradually be removed 
            Remaining_Nodes.add(temp);
            temp = temp.nextVertex;
        }

        while (!Remaining_Nodes.isEmpty()) {
            int current = 0; //store the current node/customer / initially is warehouse
            Vehicle v = new Vehicle();
            v.addNode(head); //start from warehouse
            Node[] choice;
            //all the available customer to evaluate on that particular node with exception of warehouse and visited customer

            while (true) {
                if (Remaining_Nodes.isEmpty()) {
                    break;  //no more customers 
                }
                choice = Sorted(getNode(current), Remaining_Nodes.toArray());
                //sort all the avaliable destination in ascending order of cost path 
                //Evaluate all potential customer sorted by distance and add
                boolean added = false;
                for (int i = 0; i < choice.length; i++) {
                    if (!v.TestNode(choice[i])) {
                        continue;
                    }
                    v.addNode(choice[i]);              //add customer to vehicle path 
                    Remaining_Nodes.remove(choice[i]); //this customer is serviced
                    current = choice[i].getId();
                    added = true; //added a customer
                    break;
                } //break for loop go next to newly added customer
                if (!added) {
                    break;
                }
            } //break while loop because nothing is added to current path anymore

            v.addNode(head); // back to warehouse
            Vehicles_List.add(v);
        } //next vehicle end while

        total_cost_path = CalculateTourCost(Vehicles_List);
        System.out.println("A* simulation");
        BasicPrint(Vehicles_List);
    }
    //this sorted() is used uniquely by A* simulation because it takes into account the distance from current node to goal (warehouse)

    @Override
    public Node[] Sorted(Node source, Object[] before_cast) {
        Node[] a = new Node[before_cast.length];
        for (int i = 0; i < before_cast.length; i++) {
            a[i] = (Node) (before_cast[i]);
        } //cast one by one since cannot cast all at once

        for (int pass = 0; pass < a.length - 1; pass++) { //sort array in ascending order of closest distance
            for (int i = 0; i < a.length - 1 - pass; i++) { //cost from one node to another + cost from the node from depot (cost so far)
                if (Euclidean(source, a[i]) + Euclidean(head, a[i])
                        > Euclidean(source, a[i + 1]) + Euclidean(head, a[i + 1])) {
                    Node temp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = temp;
                }
            }
        }
        return a;
    }

}
