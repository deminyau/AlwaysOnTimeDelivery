package AlwaysOnTime;

import Algorithm.MCTS;
import java.util.LinkedList;

/**
 *
 * @author Hong Zhao Cheng (123)
 */
public class MultiThreading implements Runnable {

    protected static LinkedList<Vehicle> answer = null;
    protected static MCTS mcts = null;

    @Override
    public void run() {
        answer = mcts.MCTS_Simulation2();
    }

}
