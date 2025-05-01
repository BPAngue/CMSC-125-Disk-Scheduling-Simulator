package blockify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

public class FCFS implements PausableSimulator, Runnable {
    
    private final ArrayList<Integer> diskQueue;
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
    private int currentIndex = 0;
    private Timer timer;
    
    public FCFS(Simulator simulator, CartesianPanel cartesian, ArrayList<Integer> diskQueue, ArrayList<Movement> headMovements, int headLocation, int simulationSpeed, SimulationContext simulation) {
        this.diskQueue = new ArrayList<>(diskQueue);
        this.headMovements = headMovements;
        this.headLocation = headLocation;
        this.simulationSpeed = simulationSpeed;
        this.cartesian = cartesian;
        this.simulation = simulation;
        this.simulator = simulator;
    }
    
    public void startSimulation() {
        // for debugging
        System.out.println("Starting FCFS Simulation...\n");
        
        simulationStartTime = System.nanoTime();
        accumulatedPauseTime = 0;
        isPaused = false;
        
        timer = new Timer(simulationSpeed, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex >= diskQueue.size()) {
                    endSimulation();
                    return;
                }
                
                int nextValue = diskQueue.get(currentIndex);
                int movement = Math.abs(headLocation - nextValue);
                totalSeekTime += movement;
                
                // for debugging
                System.out.printf("Moving head from %d to %d [Seek: %d]%n", headLocation, nextValue, movement);
                
                headMovements.add(new Movement(headLocation, nextValue));
                headLocation = nextValue;
                currentIndex++;
                
                updateSimulationClock();
                displayGraph();
                updateSeekTimeLabel();
            }
        });
        
        timer.start();
    }
    
    private void updateSeekTimeLabel() {
        if (simulator.getAlgorithm().equalsIgnoreCase("ALL")) {
            simulation.updateSeekTimeLabelFcfs(totalSeekTime);
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
            simulation.updateTimerLabelFcfs(minutes, seconds);
        } else {
            simulation.updateTimerLabel(minutes, seconds);
        }
    }
    
    private void endSimulation() {
        timer.stop();
        simulation.markSimulationFinished();
        // for debugging
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
