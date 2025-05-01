package blockify;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;

public class CartesianPanel extends JPanel {
    // simulator values
    public ArrayList<Integer> diskQueueLabel; // for x-axis
    public ArrayList<Movement> headMovements; // from -> to
    
    // x-axis coord constants
    public int X_AXIS_FIRST_X_COORD = 20; // point (X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD) starting point for the x-axis line
    public int X_AXIS_SECOND_X_COORD = 1420; // point (X_AXIST_SECOND_X_COORD, X_AXIS_Y_COORD) ending point for the x-axis line
    public int X_AXIS_Y_COORD = 40; // this pertains to the distance of the x-axis from the top of the panel
    
    // y-axis coord constants
    public int Y_AXIS_FIRST_Y_COORD = 90; 
    public int Y_AXIS_SECOND_Y_COORD = 400; 
    public int Y_AXIS_X_COORD = 20; // this pertains to the distance of the y-axis from the left of the panel
    
    public int yCoordLength;
    
    // distance of x-axis numbers from the axis
    public int AXIS_STRING_DISTANCE = 20;
	
    public CartesianPanel(ArrayList<Integer> diskQueueLabel, ArrayList<Movement> headMovements) {
        this.diskQueueLabel = diskQueueLabel;
        this.headMovements = headMovements;
    }
	
    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 15f));
        g2.setStroke(new java.awt.BasicStroke(2f)); // 2.0 pixel line thickness

	// draw x-axis line
	g2.drawLine(X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD, X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);
        
        // draw 0 at the start
        drawVerticalLabel(g2, "0", X_AXIS_FIRST_X_COORD + 5, X_AXIS_Y_COORD + 20);
        g2.drawLine(X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD - 5, X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD + 5); // line tick above 0
        
        // draw 199 at the end
        drawVerticalLabel(g2, "199", X_AXIS_SECOND_X_COORD + 5, X_AXIS_Y_COORD - 10);
        g2.drawLine(X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD - 5, X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD + 5); // line tick above 199
        
        // populate x-axis
        if (diskQueueLabel != null && !diskQueueLabel.isEmpty()) {
            ArrayList<Integer> sortedLabels = new ArrayList<>(diskQueueLabel);
            Collections.sort(sortedLabels);

            int minValue = sortedLabels.get(0);
            int maxValue = sortedLabels.get(sortedLabels.size() - 1);
            int range = Math.max(1, maxValue - minValue); // prevent division by zero

            int margin = 20;
            int safeStartX = X_AXIS_FIRST_X_COORD + margin;
            int safeEndX = X_AXIS_SECOND_X_COORD - margin;
            double scale = (double)(safeEndX - safeStartX) / range;
            
            double yLength = (double)(Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD) / (yCoordLength - 1);
            
            // draw x-axis ticks and rotated labels
            for (int value : sortedLabels) {
                if (value == 0 || value == 199) continue;
                
                int xPosition = (int)(safeStartX + (value - minValue) * scale);

                // Draw tick
                g2.drawLine(xPosition, X_AXIS_Y_COORD - 5, xPosition, X_AXIS_Y_COORD + 5);

                // Draw label
                String label = Integer.toString(value);
                boolean drawAbove = (value % 2 == 0);
                int digits = label.length();
                int labelX = xPosition + 5;
                int labelY = drawAbove
                    ? switch (digits) {
                        case 1 -> X_AXIS_Y_COORD + 20;
                        case 2 -> X_AXIS_Y_COORD + 30;
                        case 3 -> X_AXIS_Y_COORD + 38;
                        default -> X_AXIS_Y_COORD + 15;
                    }
                    : X_AXIS_Y_COORD - 10;

                drawVerticalLabel(g2, label, labelX, labelY);
            }
            
            // draw head movement lines 
            if (headMovements != null && !headMovements.isEmpty()) {
                int index = 0;
                g2.setColor(java.awt.Color.RED);
                g2.setStroke(new java.awt.BasicStroke(2.5f));
                
                for (int i = 0; i < headMovements.size(); i++) {
                    Movement move = headMovements.get(i);
                    int from = move.from;
                    int to = move.to;
                    
                    int x1; 
                    int x2;
                    
                    x1 = (int) (switch (from) {
                        case 0 -> X_AXIS_FIRST_X_COORD;
                        case 199 -> X_AXIS_SECOND_X_COORD;
                        default -> (int) (safeStartX + (from - minValue) * scale);
                    });
                    
                    x2 = (int) (switch (to) {
                        case 0 -> X_AXIS_FIRST_X_COORD;
                        case 199 -> X_AXIS_SECOND_X_COORD;
                        default -> (int) (safeStartX + (to - minValue) * scale);
                    });
                    
                    int y1 = (int)(Y_AXIS_FIRST_Y_COORD + i * yLength);
                    int y2 = (int)(Y_AXIS_FIRST_Y_COORD + (i + 1) * yLength);
                    
                    g2.drawLine(x1, y1, x2, y2);
                    int circleRadius = 5;
                    int diameter = circleRadius * 2;
                    g2.fillOval(x2 - circleRadius, y2 - circleRadius, diameter, diameter);
                }
            }
        }
    }
    
    public void drawVerticalLabel(Graphics2D g2, String text, int x, int y) {
        Graphics2D g2Rotated = (Graphics2D) g2.create();
        g2Rotated.rotate(-Math.PI / 2, x, y);
        g2Rotated.drawString(text, x, y);
        g2Rotated.dispose();
    }
}