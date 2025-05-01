package blockify;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Help extends Panels{
    
    private JPanel header, footer;
    private JLabel titleLabel, helpLabel, logoLabel;
    public JButton backButton;
    public JScrollPane helpScrollPane;
    
    public Help(){
    
    }
    
    @Override
    public void showComponents(){
        header = new JPanel(new FlowLayout(FlowLayout.RIGHT, 70,5));
        header.setPreferredSize(new Dimension(1000, 200));
        header.setOpaque(false);
        
        titleLabel = createLabel(400, 200, white, "Help", 60);
        logoLabel = new JLabel();
        logoLabel.setIcon(smallLogoIcon);
        
        header.add(titleLabel);
        header.add(logoLabel);
        
        String helpString = "<html><h2>Disk Scheduling Simulator – Help & Guide</h2>"
                + "<p>Welcome to the Disk Scheduling Simulator, a visual tool to understand how various disk head scheduling algorithms work. Below are instructions on how to use the simulator and descriptions of each supported algorithm.</p>"
                + "<h3>How to Use the Simulator</h3>"
                + "<ol>"
                + "<li>Input a list of disk cylinder requests (e.g., 75 141 72 62 127 135 12 55 61 77).</li>"
                + "<li>Enter the current disk head position (e.g., 62).</li>"
                + "<li>Select the direction of head movement if applicable (Left or Right).</li>"
                + "<li>Choose a scheduling algorithm to simulate.</li>"
                + "<li>Click 'Start' to view the seek sequence and total seek time.</li>"
                + "<li>You can export the simulation output as PNG or PDF if needed.</li>"
                + "</ol>"
                + "<h3>Supported Algorithms & Descriptions</h3>"
                + "<ul>"
                + "<li><strong>FCFS (First-Come, First-Served):</strong> Services requests in the order they arrive. Simple but can result in high seek time.</li>"
                + "<li><strong>SSTF (Shortest Seek Time First):</strong> Always services the closest request to the current head location. Efficient but can starve distant requests.</li>"
                + "<li><strong>SCAN (Elevator Algorithm):</strong> The head moves in one direction servicing all requests, reaches the end, then reverses. Fair but can result in longer waits.</li>"
                + "<li><strong>CSCAN (Circular SCAN):</strong> The head services requests in one direction, jumps to the start, and continues. Ensures uniform wait times.</li>"
                + "<li><strong>LOOK:</strong> Like SCAN but stops at the last request in the direction, instead of going to the physical disk end.</li>"
                + "<li><strong>CLOOK (Circular LOOK):</strong> Like CSCAN, but jumps between highest and lowest request values only.</li>"
                + "</ul>"
                + "<h3>Output</h3>"
                + "<ul>"
                + "<li><strong>Total Seek Time:</strong> The total distance the disk head traveled to service all requests.</li>"
                + "<li><strong>Graph:</strong> A visual representation of head movement over time.</li>"
                + "</ul>"
                + "<p>Use this simulator to compare and analyze disk scheduling strategies for better understanding and learning.</p>"
                + "</html>";
                
        helpLabel = createLabel(660, 920, white, helpString, archivonarrow, 19f);
        
        helpScrollPane = new JScrollPane(helpLabel);
        helpScrollPane.setPreferredSize(new Dimension(680, 420));
        helpScrollPane.setOpaque(false);
        helpScrollPane.getViewport().setOpaque(false);
        helpScrollPane.setBorder(null);
        helpScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        helpScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void paintThumb(java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle thumbBounds) {
                g.setColor(new java.awt.Color(0xc20071));
                g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
            }

            @Override
            protected void paintTrack(java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle trackBounds) {
                g.setColor(new java.awt.Color(0, 0, 0, 0)); // fully transparent
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                button.setVisible(false);
                return button;
            }
        });

        helpLabel.setOpaque(false);
        
        footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 70,30));
        footer.setPreferredSize(new Dimension(1000, 100));
        footer.setOpaque(false);
        
        backButton = createButton(150, 40, "BACK", gray, white, 20, null);
        footer.add(backButton);
        
        add(header);
        add(helpScrollPane);
        add(footer);
    }
}
