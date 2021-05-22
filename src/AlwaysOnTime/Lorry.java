package AlwaysOnTime;

/**
 *
 * @author @author Hong Zhao Cheng Chiew Zhe Wei Yau De Min Wong Yu Xuan
 */
public class Lorry extends Vehicle {
    //max capacity of lorry is +10 of van

    private static int Max_Capacity = Vehicle.getMax_Capacity() + 10;

    public String toString() {
        return "Lorry: " + super.toString();
    }

    public boolean TestNode(Node customer) { //to check whether they are still capacity
        return super.Capacity + customer.getCapacity() <= Max_Capacity;
    }

}
