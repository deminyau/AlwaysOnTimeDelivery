package SearchTree;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zhaoc
 */
import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        long timestart = 0;
        LinkedList<Customer> customers = new LinkedList();
        Scanner s = new Scanner(System.in);
        System.out.println("Enter txt file name: ");
        String filename = s.nextLine();
        Graph G1=null;
        try {
            timestart = System.currentTimeMillis();
            G1 = new Graph(filename);
            Customer temp=G1.head.nextCustomer;
            while(temp!=null){
                customers.add(temp);
                temp=temp.nextCustomer;
            }
            G1.ExtraAlgo();
            System.out.println("");
            G1.GreedySimulation();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        
        System.out.println("");
        System.out.println("Number of customers: " + G1.getNumber_of_customer());
        System.out.println("Maximum vehicle capacity: " + Vehicle.getMaxcapacity());
        System.out.println("Warehouse:" + G1.getHead());
        System.out.println("");
        
        for (int i = 0; i < customers.size(); i++) {
            System.out.println(customers.get(i) + " Capacity: " + customers.get(i).getCapacity());
        }
        System.out.println("");
        System.out.println("Maximum tree depth: " + HighestNode(customers, Vehicle.getMaxcapacity()));
        System.out.println("");
        
        TreeNode<Customer>[] RootNodes = ConstructSearchTree(customers,G1.getHead());
        for (TreeNode<Customer> treenode : RootNodes) {
            treenode.setCounterdepth(HighestNode(customers, Vehicle.getMaxcapacity()));
            System.out.println("Number of different routes if the first node is: " + treenode + " = " + treenode.FindNumOfRoute(treenode));
            PrintTree(treenode);
            System.out.println("");
        }
        //Generate all possible routes and store in RouteList
        LinkedList<LinkedList<Customer>> RouteList= new LinkedList();
        for (TreeNode<Customer> treenode : RootNodes) {
            LinkedList<LinkedList<Customer>> TempRouteList= treenode.AllPossibleRoute();
            for(LinkedList<Customer> route: TempRouteList){
                RouteList.add(route);
            }
        }
        //Show all possible routes
        System.out.println("All possible routes: ");
        for(LinkedList<Customer> route: RouteList){
            System.out.println(route);
        }
        
        long timeend = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (timeend - timestart) + " ms");
    }

    public static void PrintTree(TreeNode<Customer> rootnode) {
        System.out.println("Depth: " + rootnode.getDepth() + " " + rootnode);
        if (!rootnode.isLeaf()) {
            List<TreeNode<Customer>> nodes = rootnode.getChildren();
            for (TreeNode<Customer> node : nodes) {
                PrintTree(node);
            }
        }
    }

    public static int HighestNode(LinkedList<Customer> customers, int maxcapacity) {
// highest possible number of node a vehicle can travel based on the maximum capacity
        Collections.sort(customers);
        int maxnode = 0, tempcapacity = 0, cnt = 0;
        for (Customer c : customers) {
            if (c.getCapacity() + tempcapacity > maxcapacity) {
                break;
            } else {
                tempcapacity += c.getCapacity();
                maxnode++;
            }
        }
        return maxnode;
    }

    public static double CalDistance(Customer a, Customer b) {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    public static double RouteCost(ArrayList<Customer> route) {
        int i = 0;
        double cost = 0;
        while (true) {
            Customer first = route.get(i);
            Customer second = null;
            if (i < route.size() - 1) {
                second = route.get(i + 1);
                cost += CalDistance(first, second);
                i++;
            } else {
                //the sum of this route is already finished calculated
                break;
            }
        }
        return cost;
    }

    public static double TourCost(ArrayList<Vehicle> vehicles) {
        double sum = 0;
        int k = 0;
        while (k < vehicles.size()) {
            ArrayList<Customer> route = vehicles.get(k).getPathTaken();
            sum += RouteCost(route);
            k++;
        }
        return sum;
    }

    public static TreeNode<Customer>[] ConstructSearchTree(LinkedList<Customer> customers, Customer head) {
        TreeNode<Customer>[] RootNodes = new TreeNode[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            RootNodes[i] = new TreeNode(customers.get(i), customers.get(i).getCapacity());
            RootNodes[i].SetHead(head);
        }
        for (TreeNode<Customer> node : RootNodes) {
            AddAllChildNode(customers, node, HighestNode(customers, Vehicle.getMaxcapacity()));
        }
        return RootNodes;
    }

    public static void AddAllChildNode(LinkedList<Customer> customers, TreeNode<Customer> node, int maxdepth) {
        for (int j = 0; j < customers.size(); j++) {
            TreeNode<Customer> temp = new TreeNode<>(customers.get(j), customers.get(j).getCapacity());
            if (!isParent(temp, node)
                    && ParentCapacity(node) + customers.get(j).getCapacity() <= Vehicle.getMaxcapacity()) {
                node.addChild(temp);
                if (maxdepth > 0) {
                    AddAllChildNode(customers, node.getChildren().get(node.getChildren().size() - 1), maxdepth - 1);
                }
            }
        }
    }

    public static int ParentCapacity(TreeNode<Customer> node) {
        if (node.getParent() != null) {
            return ParentCapacity(node.getParent()) + node.getCurrentcapacity();
        }
        return node.getCurrentcapacity();
    }

    public static boolean isParent(TreeNode<Customer> e, TreeNode<Customer> node) {
        if (node.getData().getID() == (e.getData().getID())) {
            return true;
        }
        if (node.getParent() != null) {
            return isParent(e, node.getParent());
        }
        return false;
    }
    
    
}
/*public static ArrayList<Vehicle> BFSroute(ArrayList<Customer> customers, Depot depot) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        ArrayList<Vehicle> besttour = null;
        LinkedList<Customer> route = new LinkedList<>();
        double mincost = Double.MAX_VALUE;
        route.add(depot);
        int k = 0;
        while (!customers.isEmpty()) {
            int currentcapacity = 0;
            vehicles.add(new Vehicle(customers.size()));
            for (int i = 0; i < customers.size(); i++) {
                vehicles.get(k).addPossiblenext(i, customers.get(i));

            }
            if (currentcapacity >= Vehicle.getMaxcapacity()) {
                break;
            } else {

            }
            vehicles.get(k).setRoute(route);
            if (TourCost(vehicles) < mincost) {
                besttour = vehicles;
            }
            k++;
        }
        
        Arrays.sort(customers);
        while (!customers.isEmpty()) {
            Customer c=customers.get(0);
            
            for(Customer x:customers){
                distance.add(CalDistance(c,x));
                
            }
            customers.remove(0);
        }
        return besttour;
    }*/
