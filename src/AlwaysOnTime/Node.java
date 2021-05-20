package AlwaysOnTime;

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
    protected boolean visited;
    private boolean Site_Dependent;

    public Node(int x, int y, int c) {
        id = counter;
        counter++;
        coordinates[0] = x;
        coordinates[1] = y;
        capacity = c;
        nextVertex = null;
        visited = false;
    }
    
    public static void resetCounter(){
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
        visited = false;
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
}
