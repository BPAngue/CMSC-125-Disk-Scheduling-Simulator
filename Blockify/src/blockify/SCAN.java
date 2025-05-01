/* need editing: make sure that headLocation is always added into diskQueue
*/

package blockify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;

public class SCAN implements PausableSimulator, Runnable {
    
    private final ArrayList<Integer> diskQueue;
    private final ArrayList<Integer> sortedDiskQueue;
    private final ArrayList<Movement> headMovements;
    private int headLocation;
    private int simulationSpeed;
    private String currentDirection;
    private final CartesianPanel cartesian;
    private final SimulationContext simulation;
    private final Simulator simulator;
    
    private long simulationStartTime;
    private long accumulatedPauseTime = 0;
    private boolean isPaused = false;
    private long pauseStartTime;
    private int totalSeekTime = 0;
    private int iteration = 0;
    private int pivotIndex;
    private Timer timer;
    
    public SCAN(Simulator simulator, CartesianPanel cartesian, ArrayList<Integer> diskQueue, ArrayList<Movement> headMovements, int headLocation, String directionOfMovement, int simulationSpeed, SimulationContext simulation) {
        this.diskQueue = new ArrayList<>(diskQueue);
        this.headMovements = headMovements;
        this.headLocation = headLocation;
        this.simulationSpeed = simulationSpeed;
        this.cartesian = cartesian;
        this.simulation = simulation;
        this.currentDirection = directionOfMovement;
        this.simulator = simulator;
        
        this.sortedDiskQueue = new ArrayList<>(diskQueue);
        if (!sortedDiskQueue.contains(headLocation)) {
            sortedDiskQueue.add(headLocation);
        } 
        sortedDiskQueue.add(directionOfMovement.equalsIgnoreCase("Left") ? 0 : 199);
        Collections.sort(sortedDiskQueue);
        
        this.pivotIndex = sortedDiskQueue.indexOf(headLocation);
    }
    
    public void startSimulation() {
        // for debugging
        System.out.println("Starting SCAN Simulation...\n");
        
        simulationStartTime = System.nanoTime();
        accumulatedPauseTime = 0;
        isPaused = false;
        
        timer = new Timer(simulationSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iteration >= sortedDiskQueue.size() - 1) {
                    endSimulation();
                    return;
                }
                
                int nextValue = getNextHeadPosition();
                int movement = Math.abs(headLocation - nextValue);
                totalSeekTime += movement;
                
                // for debugging
                System.out.printf("Moving head from %d to %d [Seek: %d]%n", headLocation, nextValue, movement);
                
                headMovements.add(new Movement(headLocation, nextValue));
                headLocation = nextValue;
                iteration++;
                
                updateSimulationClock();
                displayGraph();
                updateSeekTimeLabel();
            }
        });
        
        timer.start();
    }
    
    private void updateSeekTimeLabel() {
        if (simulator.getAlgorithm().equalsIgnoreCase("ALL")) {
            simulation.updateSeekTimeLabelScan(totalSeekTime);
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
            simulation.updateTimerLabelScan(minutes, seconds);
        } else {
            simulation.updateTimerLabel(minutes, seconds);
        }
    }
    
    private int getNextHeadPosition() {
        int currentIndex = sortedDiskQueue.indexOf(headLocation);
        
        if (currentDirection.equalsIgnoreCase("Right")) {
            if (currentIndex < sortedDiskQueue.size() - 1 && currentIndex >= pivotIndex) {
                return sortedDiskQueue.get(currentIndex + 1);
            } else {
                currentDirection = "Left";
                pivotIndex--;
                return sortedDiskQueue.get(pivotIndex);
            }
        } else { // "Left"
            if (currentIndex > 0 && currentIndex <= pivotIndex) {
                return sortedDiskQueue.get(currentIndex - 1);
            } else {
                currentDirection = "Right";
                pivotIndex++;
                return sortedDiskQueue.get(pivotIndex);
            }
        }
    }
    
    private void endSimulation() {
        timer.stop();
        simulation.markSimulationFinished();
        // for debugging
        System.out.println("\nSimulation complete.");
        System.out.println("Total Head Movements (Seek Time): " + totalSeekTime);
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
