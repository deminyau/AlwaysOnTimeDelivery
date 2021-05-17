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

public class TreeNode<Customer> {

    private Customer data;
    private TreeNode<Customer> parent;
    private List<TreeNode<Customer>> children;
    private int currentcapacity;
    private Customer head;
    private int[] counterdepth;//only used for root nodes

    public void SetHead(Customer head) {
        this.head = head;
    }

    public TreeNode(Customer data, int capacity) {
        this.data = data;
        this.children = new LinkedList<TreeNode<Customer>>();
        currentcapacity = capacity;
    }

    public void setCounterdepth(int maxdepth) {
        counterdepth = new int[maxdepth];
        for (int x : counterdepth) {
            x = 0;
        }
    }

    public TreeNode<Customer> addChild(TreeNode<Customer> node) {
        if (this.currentcapacity + node.getCurrentcapacity() <= Vehicle.getMaxcapacity()) {
            TreeNode<Customer> childNode = new TreeNode<Customer>(node.getData(), node.getCurrentcapacity());
            childNode.parent = this;
            this.children.add(childNode);
            return childNode;
        }
        return null;
    }

    public TreeNode<Customer> addChild(Customer child, int capacity) {
        if (this.currentcapacity + capacity <= Vehicle.getMaxcapacity()) {
            TreeNode<Customer> childNode = new TreeNode<Customer>(child, capacity);
            this.children.add(childNode);
            return childNode;
        }
        return null;
    }

    public List<TreeNode<Customer>> getChildren() {
        return children;
    }

    public int getCurrentcapacity() {
        return currentcapacity;
    }

    public Customer getData() {
        return data;
    }

    public TreeNode<Customer> getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public int getDepth() {
        if (this.isRoot()) {
            return 0;
        } else {
            return parent.getDepth() + 1;
        }
    }

    public int FindNumOfRoute(TreeNode<Customer> node) {
        /*Actually the result is also the number of nodes under that tree including the root node itself
number of different possible routes for a given root node(not using warehouse itself as root node to avoid having route 0>0)
=number of nodes under it+itself*/
        int num = 0;
        if (!node.isLeaf()) {
            num++;
            for (TreeNode<Customer> nodes : node.getChildren()) {
                num += FindNumOfRoute(nodes);
            }
            return num;
        } else {
            return 1;
        }
    }

    public LinkedList<LinkedList<Customer>> AllPossibleRoute() {
        LinkedList<LinkedList<Customer>> List = new LinkedList();
        if (this.isRoot()) {//Use this method for root nodes only
            for (int i = 0; i < FindNumOfRoute((TreeNode<Customer>) this); i++) {
                //Create and initialize all routes with head added first
                LinkedList<Customer> list = new LinkedList();
                list.add(head);
                List.add(list);
            }
            AddNodesToRoutes(List, this);
            for (int i = 0; i < FindNumOfRoute((TreeNode<Customer>) this); i++) {
                //Add warehouse as ending to all possible routes
                if (!List.get(i).get(List.get(i).size() - 1).equals(head)) {
                    List.get(i).add(head);
                }
            }
        }
        return List;
    }
    private int counter = 0;
    private int max = 0;

    private LinkedList<LinkedList<Customer>> AddNodesToRoutes(LinkedList<LinkedList<Customer>> List, TreeNode<Customer> node) {
        //This method is not for independent calling, supporting method for method AllPossibleRoute() only
        if (node.isLeaf()) {
            for (int i = counterdepth[node.getDepth()]; i < counterdepth[node.getDepth()] + 1; i++) {
                List.get(i).add(node.getData());
                //List.get(i+1).add(head);
            }
            counterdepth[node.getDepth()]+=FindNumOfRoute(node.getParent());
        } else {//can try check the last element is parent of x, 
            for (int i = counterdepth[node.getDepth()]; i < counterdepth[node.getDepth()]+ FindNumOfRoute(node); i++) {
                System.out.println("Test FindNumOfRoute(nodes): " + node + " " + FindNumOfRoute(node));
                if (node.getDepth() == List.get(i).size() - 1) {
                    List.get(i).add(node.getData());
                }
            }
            counterdepth[node.getDepth()]+=FindNumOfRoute(node);
            for(int j=node.getDepth()+1;j<counterdepth.length;j++){
                counterdepth[j]++;//Compensate for the route that the node returns to 0
            }
            counterdepth[node.getDepth()+1]++;
            for (TreeNode<Customer> nodes : node.children) {
                AddNodesToRoutes(List, nodes);
            }
        }
        /*for (int j=0;j<node.children.size();j++) {
            TreeNode<Customer> nodes=node.children.get(j);
            if (nodes.isLeaf()) {
                List.get(counter).add(nodes.getData());
                counter++;
            } else {
                for(int i=counter2;i<FindNumOfRoute(nodes);i++){
                    System.out.println("Test FindNumOfRoute(nodes): "+nodes+" "+FindNumOfRoute(nodes));
                    List.get(i).add(nodes.getData());
                }
                counter2+=FindNumOfRoute(nodes);
                counter++;
                AddNodesToRoutes(List, nodes);
            }
        }*/
        return List;
    }

    private int AddToRoutes(LinkedList<LinkedList<Customer>> List, int start, int stop, TreeNode<Customer> node, int howmany) {
        for (int i = start; i < stop && howmany > 0; i++) {
            List.get(i).add(node.getData());
            howmany--;
        }
        if (stop < List.size()) {
            return stop;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "[data null]";
    }
}
