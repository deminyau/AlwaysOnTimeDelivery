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
        Graph G1 = null;
        try {
            timestart = System.currentTimeMillis();
            G1 = new Graph(filename);
            Customer temp = G1.head.nextCustomer;
            while (temp != null) {
                customers.add(temp);
                temp = temp.nextCustomer;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        System.out.println("");
        System.out.println("Number of customers: " + G1.getNumber_of_customer());
        System.out.println("Maximum vehicle capacity: " + Vehicle.getMaxcapacity());
        System.out.println("Warehouse: " + G1.getHead());
        System.out.println("");

        for (int i = 0; i < customers.size(); i++) {
            System.out.println(customers.get(i) + " Capacity: " + customers.get(i).getCapacity());
        }
        System.out.println("");
        /*System.out.println("Maximum tree depth: " + HighestNode(customers, Vehicle.getMaxcapacity()));
        System.out.println("");*/

        TreeNode<Customer>[] RootNodes = ConstructSearchTree(customers, G1.getHead());
        LinkedList<Customer> RootNodesList = new LinkedList();
        for (TreeNode<Customer> treenode : RootNodes) {
            RootNodesList.add(treenode.getData());
            /*System.out.println("Number of different routes if the first node is: " + treenode + " = " + treenode.FindNumOfRoute(treenode));
            treenode.PrintTree(treenode);
            System.out.println("");*///This part is printing the whole tree out, for debugging
        }
        //Generate all possible routes and store in RouteList
        LinkedList<LinkedList<Customer>> RouteList = new LinkedList();
        for (TreeNode<Customer> treenode : RootNodes) {
            LinkedList<LinkedList<Customer>> TempRouteList = treenode.AllPossibleRoute();
            for (LinkedList<Customer> route : TempRouteList) {
                RouteList.add(route);
            }
        }
        System.out.println("");
        BasicSimulation(RouteList, RootNodesList);
        System.out.println("");
        G1.GreedySimulation();
        System.out.println("");
        G1.ExtraAlgo();
        //Show all possible routes
        //This routes are printed for debugging purpose, remove comment if needed
        /*System.out.println("");
        System.out.println("All possible routes: ");
        for (LinkedList<Customer> route : RouteList) {
            System.out.println(route);
        }*/
        long timeend = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (timeend - timestart) + " ms");
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

    //Starting from this part below is for Tree of Routes, Above is for tree of customer

    public static TreeNode<LinkedList<Customer>> ConstructRouteTree(LinkedList<Customer> RootRoute, LinkedList<LinkedList<Customer>> PossibleRoutes, LinkedList<Customer> customers) {
        TreeNode<LinkedList<Customer>> Root = new TreeNode(RootRoute);
        Root.SetHead(RootRoute);
        AddAllChildRoute(Root, PossibleRoutes, customers.size());
        return Root;
    }

    public static void AddAllChildRoute(TreeNode<LinkedList<Customer>> Route, LinkedList<LinkedList<Customer>> PossibleRoutes, int maxdepth) {
        for (int j = 0; j < PossibleRoutes.size(); j++) {
            TreeNode<LinkedList<Customer>> temp = new TreeNode(PossibleRoutes.get(j));
            if (!Repeated(temp, Route)) {
                Route.addChild(temp);
                if (maxdepth > 0) {
                    AddAllChildRoute(Route.getChildren().get(Route.getChildren().size() - 1), PossibleRoutes, maxdepth - 1);
                }
            }
        }
    }

    public static boolean Repeated(TreeNode<LinkedList<Customer>> e, TreeNode<LinkedList<Customer>> node) {
        for (int i = 1; i < e.getData().size()-1; i++) {
            if (node.getData().contains(e.getData().get(i))) {
                return true;
            }
        }
        if (node.getParent() != null) {
                return Repeated(e, node.getParent());
            }
        return false;
    }

    public static double RouteCost(LinkedList<Customer> route) {
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

    public static int SumCapacity(LinkedList<Customer> route) {
        int capacity = 0;
        for (Customer c : route) {
            capacity += c.getCapacity();
        }
        return capacity;
    }

    public static double TourCost(LinkedList<Vehicle> vehicles) {
        double sum = 0;
        int k = 0;
        while (k < vehicles.size()) {
            LinkedList<Customer> route = vehicles.get(k).getPathTaken();
            sum += RouteCost(route);
            k++;
        }
        return sum;
    }

    public static void BasicSimulation(LinkedList<LinkedList<Customer>> List, LinkedList<Customer> customers) {
        for (int i = 0; i < List.size(); i++) {
            LinkedList<Customer> MustUse = List.get(i);
            LinkedList<LinkedList<Customer>> PossibleList = new LinkedList();
            for (int j = 0; j < List.size(); j++) {
                LinkedList<Customer> temp = List.get(j);
                tag:for (int k = 1; k < temp.size() - 1; k++) {
                    if (MustUse.contains(temp.get(k))) {
                        break tag;
                    } else if (k == temp.size() - 2) {
                        PossibleList.add(temp);
                    }
                }
            }
            //At this moment, I have a must use route and all other possible routes that can combine with this route
            TreeNode<LinkedList<Customer>> RootRoute = ConstructRouteTree(MustUse, PossibleList, customers);
            FindMinTour(RootRoute,customers);
        }
        Vehicle.Resetcounter();
        LinkedList<Vehicle> minTourVehicle = new LinkedList();
        for (LinkedList<Customer> route : MinTour) {
            Vehicle v = new Vehicle(route);
            minTourVehicle.add(v);
        }
        System.out.println("Basic Simulation");
        BasicPrint(minTourVehicle);
    }

    public static void BasicPrint(LinkedList<Vehicle> Vehicles_List) {
        System.out.println("Tour \nTotal Cost: " + MinCost);
        //display all vehicles 
        for (int i = 0; i < Vehicles_List.size(); i++) {
            System.out.println("");
            System.out.println(Vehicles_List.get(i));
        }
    }
    private static double MinCost = Double.MAX_VALUE;
    private static LinkedList<LinkedList<Customer>> MinTour = new LinkedList();
    public static void FindMinTour(TreeNode<LinkedList<Customer>> Route, LinkedList<Customer> customers) {
        if (Route.isLeaf()) {
            if (TreeTourCost(Route) < MinCost&&AllCustomersAssigned(Route,customers)) {
                MinCost = TreeTourCost(Route);
                MinTour.clear();
                TreeNode<LinkedList<Customer>> route = Route;
                while (route != null) {
                    MinTour.add(route.getData());
                    route = route.getParent();
                }
            }
        } else {
            List<TreeNode<LinkedList<Customer>>> child = Route.getChildren();
            for (TreeNode<LinkedList<Customer>> children : child) {
                FindMinTour(children,customers);
            }
        }
    }

    public static double TreeTourCost(TreeNode<LinkedList<Customer>> LeafRoute) {
        if (LeafRoute.isRoot()) {
            return RouteCost(LeafRoute.getData());
        } else {
            return RouteCost(LeafRoute.getData()) + TreeTourCost(LeafRoute.getParent());
        }
    }

    public static boolean AllCustomersAssigned(TreeNode<LinkedList<Customer>> LeafRoute, LinkedList<Customer> customers) {
        boolean[] assigned = new boolean[customers.size()];
        TreeNode<LinkedList<Customer>> Route = LeafRoute;
        for (int i = 0; i < customers.size(); i++) {
            assigned[i] = false;
        }
        while (Route != null) {
            for (int i = 0; i < customers.size(); i++) {
                if (Route.getData().contains(customers.get(i))) {
                    assigned[i] = true;
                }
            }
            Route=Route.getParent();
        }
        for (boolean assign : assigned) {
            if (!assign) {
                return false;
            }
        }
        return true;
    }

}
