package Algorithm;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
import AlwaysOnTime.Graph;
import AlwaysOnTime.Node;
import AlwaysOnTime.Vehicle;
import static AlwaysOnTime.Graph.Euclidean;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class Best_First extends Graph {

    // use distance from warehouse to customer     
    public Best_First(String name) throws FileNotFoundException {
        super(name);
    }

    public void BFirstSimulation() {
        Reset();
        LinkedList<Vehicle> Vehicles_List = new LinkedList<>();
        LinkedList<Node> Remaining_Nodes = new LinkedList<>();
        Node temp = head.nextVertex; //dont need to add warehouse (head)

        while (temp != null) { //list of all customer to be visited/ will gradually be removed 
            Remaining_Nodes.add(temp);
            temp = temp.nextVertex;
        }

        while (!Remaining_Nodes.isEmpty()) {
            Vehicle v = new Vehicle();
            v.addNode(head); //start from warehouse

            Node[] choice = Sorted(Remaining_Nodes.toArray());
            //sort the remaning customer in ascending order of distance from depot
            for (int i = 0; i < choice.length; i++) {
                if (!v.TestNode(choice[i])) {
                    continue;
                }
                v.addNode(choice[i]);//keep adding customer according to straight line distance (capacity enough)
                Remaining_Nodes.remove(choice[i]);
            }

            v.addNode(head); // back to warehouse
            Vehicles_List.add(v);
        } //next vehicle end while

        total_cost_path = CalculateTourCost(Vehicles_List);
        System.out.println("Best First simulation");
        BasicPrint(Vehicles_List);
    }
    // this sorted method overides the 1 param sorted method in parent class (Graph) - sort according to nearest distance from depot

    @Override
    public Node[] Sorted(Object[] before_cast) {
        Node[] a = new Node[before_cast.length];
        for (int i = 0; i < before_cast.length; i++) {
            a[i] = (Node) (before_cast[i]);
        } //cast one by one since cannot cast all at once

        for (int pass = 0; pass < a.length - 1; pass++) { //sort array in ascending order of closest distance to depot
            for (int i = 0; i < a.length - 1 - pass; i++) { //cost from depot to customer = straight line distance
                if (Euclidean(head, a[i]) > Euclidean(head, a[i + 1])) {
                    Node temp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = temp;
                }
            }
        }
        return a;
    }

}
