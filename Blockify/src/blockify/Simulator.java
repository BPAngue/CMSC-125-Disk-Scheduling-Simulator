package blockify;

import java.util.ArrayList;


public class Simulator {
    
    private final ArrayList<Integer> cylinders = new ArrayList<>();
    private String algorithm, direction;
    private int length, head;
    public int speed;
    
    public Simulator(){
    
    }
    
    public void addCylinder(String cylinder){
        cylinders.add(Integer.valueOf(cylinder));
    }
    
    public ArrayList<Integer> getCylinders(){
        return cylinders;
    }
    
    public void setAlgorithm(String algorithm){
        this.algorithm = algorithm;
    }
    
    public String getAlgorithm(){
        return algorithm;
    }
    
    public void setHeadLocation(String head){
        this.head = Integer.parseInt(head);
    }
    
    public int getHeadLocation(){
        return head;
    }
    
    public void setLength(String length){
        this.length = Integer.parseInt(length);
    }
    
    public int getLength(){
        return length;
    }
    
    // clear inputs
    public void clearCylinderQueue() {
        cylinders.clear();
    }
    
    public void clearHeadLocation() {
        head = 0;
    }
    
    public void clearLength() {
        length = 0;
    }
    
    public void clearAlgorithm() {
        algorithm = "";
    }
    
     public void setDirection(String direction){
        this.direction = direction;
    }
    
    public String getDirection(){
        return direction;
    }
}
