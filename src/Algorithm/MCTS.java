package Algorithm;

import AlwaysOnTime.Graph;
import AlwaysOnTime.Node;
import AlwaysOnTime.TreeNode;
import AlwaysOnTime.Vehicle;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MCTS extends Graph {

    private int level = 3;
    private int iteration  = 100;

    public MCTS(String name) throws FileNotFoundException {
        super(name);
        
    }
    
        
    public void search(LinkedList<LinkedList<Node>> List, LinkedList<Node> customers,int level, int iterations) {
        //initialize best_tour with positive infinity cost
        if (this.level == 0) {
            rollout();
        }
    }
    
    public void adapt(){//a_tour, level
    }
    
    public void rollout(){
        
    }
}
