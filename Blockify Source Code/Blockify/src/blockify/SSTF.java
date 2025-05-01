package blockify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;

public class SSTF implements PausableSimulator, Runnable {
    
    private final ArrayList<Integer> diskQueue;
    private final ArrayList<Integer> sortedDiskQueue;
    private final ArrayList<Movement> headMovements;
    private int headLocation;
    private int simulationSpeed;
    private final CartesianPanel cartesian;
    private final SimulationContext simulation;
    private final Simulator simulator;
    
    private long simulationStartTime;
    private long accumulatedPauseTime = 0;
    private boolean isPaused = false;
    private long pauseStartTime;
    private int totalSeekTime = 0;
    private Timer timer;
    
    public SSTF(Simulator simulator, CartesianPanel cartesian, ArrayList<Integer> diskQueue, ArrayList<Movement> headMovements, int headLocation, int simulationSpeed, SimulationContext simulation) {
        this.diskQueue = new ArrayList<>(diskQueue);
        this.headMovements = headMovements;
        this.headLocation = headLocation;
        this.simulationSpeed = simulationSpeed;
        this.cartesian = cartesian;
        this.simulation = simulation;
        this.simulator = simulator;
        
        this.sortedDiskQueue = new ArrayList<>(diskQueue);
        //if (!sortedDiskQueue.contains(headLocation)) {
            sortedDiskQueue.add(headLocation);
        //}
        Collections.sort(sortedDiskQueue);
    }
    
    public void startSimulation() {
        // for debugging
        System.out.println("Starting SSTF Simulation...\n");
        
        simulationStartTime = System.nanoTime();
        accumulatedPauseTime = 0;
        isPaused = false;
        
        timer = new Timer(simulationSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sortedDiskQueue.size() <= 1) {
                    endSimulation();
                    return;
                }
                
                int nextHead = findClosestRequest();
                int movement = Math.abs(headLocation - nextHead);
                totalSeekTime += movement;
                
                // for debugging
                System.out.printf("Moving head from %d to %d [Seek: %d]%n", headLocation, nextHead, movement);
                
                headMovements.add(new Movement(headLocation, nextHead));
                sortedDiskQueue.remove(Integer.valueOf(headLocation));
                headLocation = nextHead;
                
                updateSimulationClock();
                displayGraph();
                updateSeekTimeLabel();
            }
        });
        
        timer.start();
    }
    
    private void updateSeekTimeLabel() {
        if (simulator.getAlgorithm().equalsIgnoreCase("ALL")) {
            simulation.updateSeekTimeLabelSstf(totalSeekTime);
        } else {
            simulation.updateSeekTimeLabel(totalSeekTime);
        }
    }
    
    private void updateSimulationClock() {
        long now = System.nanoTime();
        long elapsedNano = now - simulationStartTime - accumulatedPauseTime;
        long elapsedMillis = elapsedNano / 1_000_000;
        
        int virtualSeconds = (int) (elapsedMillis / 1000);
        int minutes = virtualSeconds / 60;
        int seconds = virtualSeconds % 60;
        
        if (simulator.getAlgorithm().equalsIgnoreCase("ALL")) {
            simulation.updateTimerLabelSstf(minutes, seconds);
        } else {
            simulation.updateTimerLabel(minutes, seconds);
        }
    }
    
    private int findClosestRequest() {
        int index = sortedDiskQueue.indexOf(headLocation);
        
        Integer prev = (index > 0) ? sortedDiskQueue.get(index - 1) : null;
        Integer next = (index < sortedDiskQueue.size() - 1) ? sortedDiskQueue.get(index + 1) : null;
        
        if (prev == null) return next;
        if (next == null) return prev;
        
        int distPrev = Math.abs(headLocation - prev);
        int distNext = Math.abs(headLocation - next);
        
        return (distPrev <= distNext) ? prev : next;
    }
    
    private void endSimulation() {
        timer.stop();
        simulation.markSimulationFinished();
        // for debugging;
        System.out.println("\nSimulation complete.");
        System.out.println("Total Head Movements (Seek Time): " + totalSeekTime);
    }
    
    public void stopSimulation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
    
    private void displayGraph() {
        cartesian.repaint();
    }
    
    public Timer getTimer() {
        return timer;
    }
    
    public void setSimulationSpeed(int newSpeed) {
        this.simulationSpeed = newSpeed;
        if (timer != null) {
            timer.setDelay(newSpeed);
        }
    }

    @Override
    public void recordPauseStart() {
        pauseStartTime = System.nanoTime();
        isPaused = true;
    }

    @Override
    public void resumeAfterPause() {
        accumulatedPauseTime += (System.nanoTime() - pauseStartTime);
        isPaused = false;
    }
    
    @Override
    public void run() {
        startSimulation();
    }
}
