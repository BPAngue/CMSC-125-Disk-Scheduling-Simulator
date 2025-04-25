package blockify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

public class FCFS {
    
    public ArrayList<Integer> diskQueue;
    public int headLocation;
    
    // for simulation
    private static int currentIndex = 0;
    private static int totalSeekTime = 0;
    private static int currentHead;
    private static Timer timer;
    
    public FCFS(ArrayList<Integer> diskQueue, int headLocation) {
        this.diskQueue = diskQueue;
        this.headLocation = headLocation;
        currentHead = headLocation;
        
        for (int num : diskQueue) {
            System.out.println(num);
        }
    }
    
    public void startSimulation() {
        System.out.println("Starting FCFS Simulation...\n");
        
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < diskQueue.size()) {
                    int request = diskQueue.get(currentIndex);
                    int movement = Math.abs(currentHead - request);
                    totalSeekTime += movement;

                    System.out.println("Moving head from " + currentHead + " to " + request + 
                                       " [Seek: " + movement + "]");

                    currentHead = request;
                    currentIndex++;
                } else {
                    timer.stop();
                    System.out.println("\nSimulation complete.");
                    System.out.println("Total Head Movements (Seek Time): " + totalSeekTime);
                }
            }
        });

        timer.start();
    }
}
