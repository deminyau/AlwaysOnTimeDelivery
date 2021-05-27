package AlwaysOnTime;

import java.util.LinkedList;

/**
 *
 * @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
public class Node implements Comparable {

    private int[] coordinates = new int[2];
    private int capacity;
    private static int counter = 0;
    private int id;
    public Node nextVertex;
    public boolean checked = false;
    public boolean visited = false;
    private boolean Site_Dependent;
    //public LinkedList <Node> Unchecked=new LinkedList();

    public Node(int x, int y, int c) {
        id = counter;
        counter++;
        coordinates[0] = x;
        coordinates[1] = y;
        capacity = c;
        nextVertex = null;
    }

    public static void resetCounter() {
        counter = 0;
    }

    public int compareTo(Object customers) {
        Node customer = (Node) customers;
        return Integer.compare(capacity, customer.getCapacity());
    }

    public Node(int x, int y, int c, boolean s) {
        id = counter;
        counter++;
        coordinates[0] = x;
        coordinates[1] = y;
        capacity = c;
        nextVertex = null;
        Site_Dependent = s;
    }

    public int getX() {
        return coordinates[0];
    }

    public int getY() {
        return coordinates[1];
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isSite_Dependent() {
        return Site_Dependent;
    }

    public String toString() {
        String answer = "X : " + getX() + " Y: " + getY();
        answer += "\nCapacity: " + capacity;
        answer += "\nId: " + id + "\n";
        return answer;
    }

    public boolean testNode(Node a) { //used in mcts
    // determine whether a node can be added to a vehicle with this node 
        return (this.capacity + a.capacity) <= Vehicle.getMax_Capacity();
    }

    public static LinkedList<Node> getRemaining() {
        //return a list of customer which are not visited
        LinkedList<Node> answer = new LinkedList<>();
        for (int i = 0; i < Graph.allCustomers.size(); i++) {
            Node temp = Graph.allCustomers.get(i);
            if (!temp.visited && temp.getId() != 0) {
                answer.add(temp);
            }
        }
        return answer;
    }

}
