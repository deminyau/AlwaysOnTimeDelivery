
package Graph;

public class Node {
    private int [] coordinates = new int [2];
    private int capacity;
    private static int counter = 0;
    private int id; 
    protected Node nextVertex ;
    
    public Node (int x, int y, int c){
        id = counter ; 
        counter ++;
        coordinates [0] = x;
        coordinates [1] = y;
        capacity = c;
        nextVertex = null;}
    
    public int getX(){
        return coordinates [0];
    }
    
    public int getY(){
        return coordinates[1];
    }
    
    public int getId(){return id;}
    
    public String toString (){
        String answer = "X : " + getX() + " Y: " + getY();
        answer += "\nCapacity: " + capacity;
        answer += "\nId: " + id;
        return answer;}
}
