package AlwaysOnTime;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Algorithm extends Graph {

    public Algorithm(String name) throws FileNotFoundException {
        super(name);
    }

    public void AStarSimulation() {//A* search (Extra Feature)
        Reset();
        ArrayList<Vehicle> Vehicles_List = new ArrayList<>();
        ArrayList<Node> Remaining_Nodes = new ArrayList<>();
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

    public void BestFirstSimulation() { //Best First Search (Extra Features)
        Reset();
        ArrayList<Vehicle> Vehicles_List = new ArrayList<>();
        ArrayList<Node> Remaining_Nodes = new ArrayList<>();
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

    public void SDHCSimulation() {
        Reset();
        ArrayList<Vehicle> Vehicles_List = new ArrayList<>();
        ArrayList<Node> Remaining_Nodes = new ArrayList<>();
        Node temp = head.nextVertex; //dont need to add warehouse (head)

        while (temp != null) { //list of all customer to be visited/ will gradually be removed 
            Remaining_Nodes.add(temp);
            temp = temp.nextVertex;
        }
        Vehicle v;
        //if not site dependent, we will use lorry , else use van
        ArrayList<Node> SD_RemainingNode = getSiteDependent(Remaining_Nodes.toArray());
        while (!SD_RemainingNode.isEmpty()) {
            int current = 0;
            v = new Vehicle();
            v.addNode(head);
            while (true) { //add all customers that is site dependent
                Node[] choice = Sorted(getNode(current), SD_RemainingNode.toArray());
                boolean added = false;
                for (int i = 0; i < choice.length; i++) {
                    if (!v.TestNode(choice[i])) {
                        continue;
                    }
                    v.addNode(choice[i]);              //add customer to vehicle path 
                    Remaining_Nodes.remove(choice[i]); //this customer is serviced
                    SD_RemainingNode.remove(choice[i]);
                    current = choice[i].getId();
                    added = true; //added a customer
                    break;
                } //break for loop go next to newly added customer
                if (!added) {
                    break;
                }
            }
            Vehicles_List.add(v);
        }
        //all site dependent is done now we use lorry / present car 

        while (!Remaining_Nodes.isEmpty()) {
            int current = 0;
            Lorry L = new Lorry();
            L.addNode(head);
            Vehicles_List.add(L); //determine where it is ideal to add this customer can be vehicle or lorry
            while (true) {
                if (Remaining_Nodes.isEmpty()) {
                    break;
                }
                Node[] choice = Sorted(getNode(current), Remaining_Nodes.toArray());
                int index = Vehicle.PossibleSource(choice[0], Vehicles_List);
                //only for small demand customer can use vehicle else use lorry

                if (choice[0].getCapacity() > Vehicle.getMax_Capacity() / 2) { //high demand customer
                    if (!L.TestNode(choice[0])) {
                        break;
                    } else {
                        Vehicles_List.get(Vehicles_List.size() - 1).addNode(choice[0]);
                    }
                } else if (index > -1) {
                    Vehicles_List.get(index).addNode(choice[0]);
                } else {
                    break; //lorry capacity not enough 
                }
                Remaining_Nodes.remove(choice[0]);
                current = choice[0].getId();
            }
        }

        for (int i = 0; i < Vehicles_List.size(); i++) {
            Vehicles_List.get(i).addNode(head);
        }
        super.total_cost_path = CalculateTourCost(Vehicles_List);
        System.out.println("Greedy simulation with added extra features");
        BasicPrint(Vehicles_List);
    }

    public void CnnditionalSimulation() { //created using some condition maybe got better route
        ArrayList<Vehicle> mincosttour = PredictedPath((double) 2 / 3); //optimal case
        total_cost_path = CalculateTourCost(mincosttour);

        for (double i = 50; i < 100; i++) {
            double value = i / (double) 100;
            ArrayList<Vehicle> temp = PredictedPath(value);
            total_cost_path = CalculateTourCost(mincosttour);
            if (total_cost_path > CalculateTourCost(temp)) {
                mincosttour = temp;
                total_cost_path = CalculateTourCost(temp);
            }
        }
        System.out.println("Conditonal simulation ");
        BasicPrint(mincosttour);
    }

    public void GreedySimulation() {
        Reset();
        ArrayList<Vehicle> Vehicles_List = new ArrayList<>();
        ArrayList<Node> Remaining_Nodes = new ArrayList<>();
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
        System.out.println("Greedy simulation");
        BasicPrint(Vehicles_List);
    }

}
