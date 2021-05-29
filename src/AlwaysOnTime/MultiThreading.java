/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlwaysOnTime;

import Algorithm.MCTS;
import java.util.LinkedList;

/**
 *
 * @author zhaoc
 */
public class MultiThreading implements Runnable {

    protected static LinkedList<Vehicle> answer = null;
    protected static MCTS mcts = null;

    @Override
    public void run() {
        answer = mcts.MCTS_Simulation2();
    }

}
