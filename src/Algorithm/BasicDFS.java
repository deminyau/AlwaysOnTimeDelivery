package Algorithm;

import AlwaysOnTime.*;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
public class BasicDFS extends Graph {

    public BasicDFS(String filename) throws FileNotFoundException {
        //long timestart = 0;
        super(filename);
        LinkedList<Node> customers = new LinkedList();
        Node temp = head.nextVertex;
        while (temp != null) {
            customers.add(temp);
            temp = temp.nextVertex;
        }
        TreeNode<Node>[] RootNodes = ConstructSearchTree(customers, super.getHead());
        LinkedList<Node> RootNodesList = new LinkedList();
        for (TreeNode<Node> treenode : RootNodes) {
            RootNodesList.add(treenode.getData());
            /*System.out.println("Number of different routes if the first node is: " + treenode + " = " + treenode.FindNumOfRoute(treenode));
            treenode.PrintTree(treenode);
            System.out.println("");*///This part is printing the whole tree out, for debugging
        }
        //Generate all possible routes and store in RouteList
        LinkedList<LinkedList<Node>> RouteList = new LinkedList();
        for (TreeNode<Node> treenode : RootNodes) {
            LinkedList<LinkedList<Node>> TempRouteList = treenode.AllPossibleRoute();
            for (LinkedList<Node> route : TempRouteList) {
                RouteList.add(route);
            }
        }
        BasicSimulation(RouteList, RootNodesList);
        System.out.println("");
    }

    public static int HighestNode(LinkedList<Node> customers, int maxcapacity) {
        // highest possible number of node a vehicle can travel based on the maximum capacity
        Collections.sort(customers);
        int maxnode = 0, tempcapacity = 0, cnt = 0;
        for (Node c : customers) {
            if (c.getCapacity() + tempcapacity > maxcapacity) {
                break;
            } else {
                tempcapacity += c.getCapacity();
                maxnode++;
            }
        }
        return maxnode;
    }

    public double CalDistance(Node a, Node b) {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    public TreeNode<Node>[] ConstructSearchTree(LinkedList<Node> customers, Node head) {
        TreeNode<Node>[] RootNodes = new TreeNode[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            RootNodes[i] = new TreeNode(customers.get(i), customers.get(i).getCapacity());
            RootNodes[i].SetHead(head);
        }
        for (TreeNode<Node> node : RootNodes) {
            AddAllChildNode(customers, node, HighestNode(customers, Vehicle.getMax_Capacity()));
        }
        return RootNodes;
    }

    public void AddAllChildNode(LinkedList<Node> customers, TreeNode<Node> node, int maxdepth) {
        for (int j = 0; j < customers.size(); j++) {
            TreeNode<Node> temp = new TreeNode<>(customers.get(j), customers.get(j).getCapacity());
            if (!isParent(temp, node)
                    && ParentCapacity(node) + customers.get(j).getCapacity() <= Vehicle.getMax_Capacity()) {
                node.addChild(temp);
                if (maxdepth > 0) {
                    AddAllChildNode(customers, node.getChildren().get(node.getChildren().size() - 1), maxdepth - 1);
                }
            }
        }
    }

    public int ParentCapacity(TreeNode<Node> node) {
        if (node.getParent() != null) {
            return ParentCapacity(node.getParent()) + node.getCurrentcapacity();
        }
        return node.getCurrentcapacity();
    }

    public boolean isParent(TreeNode<Node> e, TreeNode<Node> node) {
        if (node.getData().getId() == (e.getData().getId())) {
            return true;
        }
        if (node.getParent() != null) {
            return isParent(e, node.getParent());
        }
        return false;
    }

    //Starting from this part below is for Tree of Routes, Above is for tree of customer
    public TreeNode<LinkedList<Node>> ConstructRouteTree(LinkedList<Node> RootRoute, LinkedList<LinkedList<Node>> PossibleRoutes, LinkedList<Node> customers) {
        TreeNode<LinkedList<Node>> Root = new TreeNode(RootRoute);
        Root.SetHead(RootRoute);
        AddAllChildRoute(Root, PossibleRoutes, customers.size());
        return Root;
    }

    public void AddAllChildRoute(TreeNode<LinkedList<Node>> Route, LinkedList<LinkedList<Node>> PossibleRoutes, int maxdepth) {
        for (int j = 0; j < PossibleRoutes.size(); j++) {
            TreeNode<LinkedList<Node>> temp = new TreeNode(PossibleRoutes.get(j));
            if (!Repeated(temp, Route)) {
                Route.addChild(temp);
                if (maxdepth > 0) {
                    AddAllChildRoute(Route.getChildren().get(Route.getChildren().size() - 1), PossibleRoutes, maxdepth - 1);
                }
            }
        }
    }

    public boolean Repeated(TreeNode<LinkedList<Node>> e, TreeNode<LinkedList<Node>> node) {
        for (int i = 1; i < e.getData().size() - 1; i++) {
            if (node.getData().contains(e.getData().get(i))) {
                return true;
            }
        }
        if (node.getParent() != null) {
            return Repeated(e, node.getParent());
        }
        return false;
    }

    public double RouteCost(LinkedList<Node> route) {
        int i = 0;
        double cost = 0;
        while (true) {
            Node first = route.get(i);
            Node second = null;
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

    public static int SumCapacity(LinkedList<Node> route) {
        int capacity = 0;
        for (Node c : route) {
            capacity += c.getCapacity();
        }
        return capacity;
    }

    public double TourCost(LinkedList<Vehicle> vehicles) {
        double sum = 0;
        int k = 0;
        while (k < vehicles.size()) {
            LinkedList<Node> route = vehicles.get(k).getPathTaken();
            sum += RouteCost(route);
            k++;
        }
        return sum;
    }

    public void BasicSimulation(LinkedList<LinkedList<Node>> List, LinkedList<Node> customers) {
        long start = System.currentTimeMillis();
        long end = start + 50 * 1000; //set time limit for many customers
        outermost:
        for (int i = 0; i < List.size(); i++) {
            LinkedList<Node> MustUse = List.get(i);
            LinkedList<LinkedList<Node>> PossibleList = new LinkedList();
            for (int j = 0; j < List.size(); j++) {
                LinkedList<Node> temp = List.get(j);
                tag:
                for (int k = 1; k < temp.size() - 1; k++) {
                    if (MustUse.contains(temp.get(k))) {
                        break tag;
                    } else if (k == temp.size() - 2) {
                        PossibleList.add(temp);
                    }
                }
            }
            //At this moment, I have a must use route and all other possible routes that can combine with this route
             if (System.currentTimeMillis() > end) {
                break outermost;} //it is possible the 60 sec time limit is insufficient to even finish constructing a tree, 
             //thus will get null, to see final result, remove this if statement and wait patiently for the output
            TreeNode<LinkedList<Node>> RootRoute = ConstructRouteTree(MustUse, PossibleList, customers);
            FindMinTour(RootRoute, customers);
           

        }
        Vehicle.Resetcounter();
        LinkedList<Vehicle> minTourVehicle = new LinkedList();
        for (LinkedList<Node> route : MinTour) {
            Vehicle v = new Vehicle(route);
            minTourVehicle.add(v);
        }
        System.out.println("Basic Simulation");
        BasicPrint(minTourVehicle);
    }

    public void BasicPrint(LinkedList<Vehicle> Vehicles_List) {
        System.out.println("Tour \nTotal Cost: " + MinCost);
        //display all vehicles 
        for (int i = 0; i < Vehicles_List.size(); i++) {
            System.out.println(Vehicles_List.get(i));
        }
    }
    
    private static double MinCost = Double.MAX_VALUE;
    private static LinkedList<LinkedList<Node>> MinTour = new LinkedList();

    public void FindMinTour(TreeNode<LinkedList<Node>> Route, LinkedList<Node> customers) {
        if (Route.isLeaf()) {
            if (TreeTourCost(Route) < MinCost && AllCustomersAssigned(Route, customers)) {
                MinCost = TreeTourCost(Route);
                MinTour.clear();
                TreeNode<LinkedList<Node>> route = Route;
                while (route != null) {
                    MinTour.add(route.getData());
                    route = route.getParent();
                }
            }
        } else {
            List<TreeNode<LinkedList<Node>>> child = Route.getChildren();
            for (TreeNode<LinkedList<Node>> children : child) {
                FindMinTour(children, customers);
            }
        }
    }

    public double TreeTourCost(TreeNode<LinkedList<Node>> LeafRoute) {
        if (LeafRoute.isRoot()) {
            return RouteCost(LeafRoute.getData());
        } else {
            return RouteCost(LeafRoute.getData()) + TreeTourCost(LeafRoute.getParent());
        }
    }

    public boolean AllCustomersAssigned(TreeNode<LinkedList<Node>> LeafRoute, LinkedList<Node> customers) {
        boolean[] assigned = new boolean[customers.size()];
        TreeNode<LinkedList<Node>> Route = LeafRoute;
        for (int i = 0; i < customers.size(); i++) {
            assigned[i] = false;
        }
        while (Route != null) {
            for (int i = 0; i < customers.size(); i++) {
                if (Route.getData().contains(customers.get(i))) {
                    assigned[i] = true;
                }
            }
            Route = Route.getParent();
        }
        for (boolean assign : assigned) {
            if (!assign) {
                return false;
            }
        }
        return true;
    }
}
