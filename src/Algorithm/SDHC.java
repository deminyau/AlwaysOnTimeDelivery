package Algorithm;

import AlwaysOnTime.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
public class SDHC extends Graph {

    public SDHC(String name) throws FileNotFoundException {
        Reset();
        Scan(name);
    }

    public boolean addVertex(int x, int y, int c, boolean s) {
        Node temp = head;
        Node newNode = new Node(x, y, c, s); //info and next vertex (null)
        if (head == null) {
            head = newNode;
        } else {
            while (temp.nextVertex != null) {
                temp = temp.nextVertex;
            }
            temp.nextVertex = newNode;
        }
        return true;
    }

    public void Scan(String name) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream(name));
        String temp = sc.nextLine();   //first line of input
        String[] tokens = temp.split(" ");
        super.Number_of_customer = Integer.parseInt(tokens[0]);
        Vehicle.setMax_Capacity(Integer.parseInt(tokens[1]));
        int size = 0; //to see the size added is equal to text file
        while (sc.hasNextLine()) { //input with information of nodes
            size++;
            temp = sc.nextLine();
            tokens = temp.split(" ");
            addVertex(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),
                    Boolean.parseBoolean(tokens[3]));
        }//end while
    }

    public void SDHCSimulation() {
        long start = System.currentTimeMillis();
        LinkedList<Vehicle> Vehicles_List = new LinkedList<>();
        LinkedList<Node> Remaining_Nodes = new LinkedList<>();
        Node temp = head.nextVertex; //dont need to add warehouse (head)

        while (temp != null) { //list of all customer to be visited/ will gradually be removed 
            Remaining_Nodes.add(temp);
            temp = temp.nextVertex;
        }
        Vehicle v;
        //if not site dependent, we will use lorry , else use van
        LinkedList<Node> SD_RemainingNode = getSiteDependent(Remaining_Nodes.toArray());
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
        long end = start + 60 * 1000;//set time limit 
        if (System.currentTimeMillis() < end) {
            System.out.println(timer());
        }
        System.out.println("Greedy simulation added with extra features");
        BasicPrint(Vehicles_List);
        System.out.println("");
    }

    public LinkedList<Node> getSiteDependent(Object[] before_cast) {
        LinkedList<Node> a = new LinkedList<>();
        for (int i = 0; i < before_cast.length; i++) {
            Node temp = (Node) (before_cast[i]);
            if (temp.isSite_Dependent()) {
                a.add(temp);
            }
        }
        return a;
    }
}
