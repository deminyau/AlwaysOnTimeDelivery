package AlwaysOnTime;

/**
 *
 * @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
import java.util.LinkedList;
import java.util.List;

public class TreeNode<E> {

    private E data;
    private TreeNode<E> parent;//Parent of a treenode
    private List<TreeNode<E>> children;//children of a treenode
    private int currentcapacity;
    private E head;

    public void SetHead(E head) {
        this.head = head;
    }

    public TreeNode(E data, int capacity) {
        this.data = data;
        this.children = new LinkedList<TreeNode<E>>();
        currentcapacity = capacity;
    }

    public TreeNode(E data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<E>>();
    }

    public TreeNode<E> addChild(TreeNode<E> node) {
        //make sure only possible child node is added
        if (this.currentcapacity + node.getCurrentcapacity() <= Vehicle.getMax_Capacity()) {
            TreeNode<E> childNode = new TreeNode<E>(node.getData(), node.getCurrentcapacity());
            childNode.parent = this;
            this.children.add(childNode);
            return childNode;
        }
        return null;
    }

    public TreeNode<E> addChild(E child, int capacity) {
        if (this.currentcapacity + capacity <= Vehicle.getMax_Capacity()) {
            TreeNode<E> childNode = new TreeNode<E>(child, capacity);
            this.children.add(childNode);
            return childNode;
        }
        return null;
    }

    public List<TreeNode<E>> getChildren() {
        return children;
    }

    public int getCurrentcapacity() {
        return currentcapacity;
    }

    public E getData() {
        return data;
    }

    public TreeNode<E> getParent() {
        return parent;
    }

    //Check if a node is the root node
    public boolean isRoot() {
        return parent == null;
    }

    //Check if a node is the leaf node
    public boolean isLeaf() {
        return children.size() == 0;
    }

    //Get the depth a treenode is located at
    public int getDepth() {
        if (this.isRoot()) {
            return 0;
        } else {
            return parent.getDepth() + 1;
        }
    }

    
    public int FindNumOfRoute(TreeNode<E> node) {
        /*Actually the result is also the number of nodes under that tree including the root node itself
number of different possible routes for a given root node(not using warehouse itself as root node to avoid having route 0>0)
=number of nodes under it+itself*/
        int num = 0;
        if (!node.isLeaf()) {
            num++;
            for (TreeNode<E> nodes : node.getChildren()) {
                num += FindNumOfRoute(nodes);
            }
            return num;
        } else {
            return 1;
        }
    }

    //Find all possible routes(Use this method for root nodes only)
    public LinkedList<LinkedList<E>> AllPossibleRoute() {
        LinkedList<LinkedList<E>> List = new LinkedList();
        if (this.isRoot()) {//Use this method for root nodes only
            for (int i = 0; i < FindNumOfRoute((TreeNode<E>) this); i++) {
                //Create and initialize all routes with head added first
                LinkedList<E> list = new LinkedList();
                list.add(head);
                List.add(list);
            }
            AddNodesToRoutes(List, this);
            for (int i = 0; i < FindNumOfRoute((TreeNode<E>) this); i++) {
                //Add warehouse as ending to all possible routes
                if (!List.get(i).get(List.get(i).size() - 1).equals(head)) {
                    List.get(i).add(head);
                }
            }
        }
        return List;
    }
    private int counter = 0;

    private void RecursionAdd(LinkedList<LinkedList<E>> List, TreeNode<E> node) {
        // This method is supporting method for AddNodesToRoutes
        //Create each possible route as a LinkedList<Customer> one by one, save into List
        if (!node.isLeaf()) {
            List.get(counter).add(node.getData());
            if (node.isRoot()) {
                counter++;
            } else {
                RecursionAdd(List, node.getParent());
            }
        } else {
            List.get(counter).add(node.getData());
            if (!node.isRoot()) {
                RecursionAdd(List, node.getParent());
            }
        }
    }

    private LinkedList<LinkedList<E>> AddNodesToRoutes(LinkedList<LinkedList<E>> List, TreeNode<E> node) {
        //This method is not for independent calling, supporting method for method AllPossibleRoute() only
        //Add the correct nodes to the routes
        RecursionAdd(List, node);
        if (!node.isLeaf()) {
            List<TreeNode<E>> nodes = node.getChildren();
            for (TreeNode<E> Node : nodes) {
                AddNodesToRoutes(List, Node);
            }
        }
        return List;
    }

    //Print the whole tree in DFS manner
    public void PrintTree(TreeNode<E> rootnode) {
        System.out.println("Depth: " + rootnode.getDepth() + " " + rootnode);
        if (!rootnode.isLeaf()) {
            List<TreeNode<E>> nodes = rootnode.getChildren();
            for (TreeNode<E> node : nodes) {
                PrintTree(node);
            }
        }
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "[data null]";
    }
}
