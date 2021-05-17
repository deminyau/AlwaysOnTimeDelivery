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
public class Customer implements Comparable {

    private int capacity, ID;
    private int xaxis, yaxis;
    private static int counter = 0;
    protected boolean visited = false;
    protected Customer nextCustomer;

    public Customer() {
    }

    public int compareTo(Object customers) {
        Customer customer = (Customer) customers;
        return Integer.compare(capacity, customer.getCapacity());
    }

    public Customer(int xaxis, int yaxis, int capacity) {
        this.capacity = capacity;
        this.ID = counter;
        counter++;
        nextCustomer = null;
        this.xaxis = xaxis;
        this.yaxis = yaxis;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getX() {
        return xaxis;
    }

    public int getY() {
        return yaxis;
    }

    @Override
    public String toString() {
        return "ID: " + ID;
    }

}
