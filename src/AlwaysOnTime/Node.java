package AlwaysOnTime;

public class Node {

    private int[] coordinates = new int[2];
    private int capacity;
    private static int counter = 0;
    private int id;
    protected Node nextVertex;
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
