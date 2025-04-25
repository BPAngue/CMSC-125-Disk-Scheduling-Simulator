package blockify;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

public class CartesianPanel extends JPanel {
    // simulator values
    public ArrayList<Integer> diskQueueLabel;
    
    // x-axis coord constants
    public int X_AXIS_FIRST_X_COORD = 20; // point (X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD) starting point for the x-axis line
    public int X_AXIS_SECOND_X_COORD = 1420; // point (X_AXIST_SECOND_X_COORD, X_AXIS_Y_COORD) ending point for the x-axis line
    public int X_AXIS_Y_COORD = 40; // this pertains to the distance of the x-axis from the top of the panel
    
    // y-axis coord constants
    public int Y_AXIS_FIRST_Y_COORD = 90; 
    public int Y_AXIS_SECOND_Y_COORD = 400; 
    public int Y_AXIS_X_COORD = 20; // this pertains to the distance of the y-axis from the left of the panel
    
    // distance of x-axis numbers from the axis
    public int AXIS_STRING_DISTANCE = 20;
	
    public CartesianPanel(ArrayList<Integer> diskQueueLabel) {
        this.diskQueueLabel = diskQueueLabel;
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
        Graphics2D g2RotatedZero = (Graphics2D) g2.create();
        int zeroX = X_AXIS_FIRST_X_COORD + 5;
        int zeroY = X_AXIS_Y_COORD + 20;
        g2RotatedZero.rotate(-Math.PI / 2, zeroX, zeroY);
        g2RotatedZero.drawString("0", zeroX, zeroY);
        g2RotatedZero.dispose();
        g2.drawLine(X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD - 5, X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD + 5); // line tick above 0
        
        // fraw 199 at the end
        Graphics2D g2Rotated199 = (Graphics2D) g2.create();
        int one99X = X_AXIS_SECOND_X_COORD + 5;
        int one99Y = X_AXIS_Y_COORD - 10; // a bit more space for 3 digits
        g2Rotated199.rotate(-Math.PI / 2, one99X, one99Y);
        g2Rotated199.drawString("199", one99X, one99Y);
        g2Rotated199.dispose();
        g2.drawLine(X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD - 5, X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD + 5); // line tick above 199
        
        if (diskQueueLabel != null && !diskQueueLabel.isEmpty()) {
            ArrayList<Integer> sortedLabels = new ArrayList<>(diskQueueLabel);
            sortedLabels.sort(Integer::compareTo);

            int minValue = sortedLabels.get(0);
            int maxValue = sortedLabels.get(sortedLabels.size() - 1);
            int range = Math.max(1, maxValue - minValue); // prevent division by zero

            int margin = 20;
            int safeStartX = X_AXIS_FIRST_X_COORD + margin;
            int safeEndX = X_AXIS_SECOND_X_COORD - margin;
            double scale = (double)(safeEndX - safeStartX) / range;
            
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

                Graphics2D g2Rotated = (Graphics2D) g2.create();
                g2Rotated.rotate(-Math.PI / 2, labelX, labelY);
                g2Rotated.drawString(label, labelX, labelY);
                g2Rotated.dispose();
            }
        }

        // draw y-axis line
        g2.drawLine(Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD, Y_AXIS_X_COORD, Y_AXIS_SECOND_Y_COORD);
        
        int yCoordLength = diskQueueLabel.size();
        double yLength = (double)(Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD) / (yCoordLength - 1);
        
        // draw y-axis ticks
        for (int i = 0; i < yCoordLength; i++) {
            int y = (int)(Y_AXIS_FIRST_Y_COORD + i * yLength);
            g2.drawLine(Y_AXIS_X_COORD - 5, y, Y_AXIS_X_COORD + 5, y);
        }
    }
}