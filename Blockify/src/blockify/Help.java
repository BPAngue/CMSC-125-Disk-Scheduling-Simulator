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
        
        String helpString = "<html><h2>Page Replacement Simulator Instructions</h2>"
                            + "<p>Welcome to the Page Replacement Simulator. This tool helps visualize and simulate various page replacement algorithms. Please follow the steps below to use the simulator:</p>"
                            + "<ul>"
                            + "<li><strong>FIFO (First-In-First-Out):</strong> This algorithm replaces the page that has been in memory the longest. Simply select this option to simulate FIFO behavior.</li>"
                            + "<li><strong>LRU (Least Recently Used):</strong> LRU replaces the page that has not been used for the longest period of time. Select this algorithm to observe LRU's page replacement actions.</li>"
                            + "<li><strong>OPT (Optimal):</strong> The OPT algorithm replaces the page that will not be used for the longest period of time in the future. Choose this option for an optimal replacement strategy.</li>"
                            + "<li><strong>Second Chance Algorithm:</strong> This is a variation of FIFO where each page gets a second chance before being replaced. The page is only replaced if it has been referenced.</li>"
                            + "<li><strong>Enhanced Second Chance Algorithm:</strong> A modification of the Second Chance algorithm that assigns a higher priority to pages that have been accessed recently and provides a more efficient way to decide which pages to replace.</li>"
                            + "<li><strong>LFU (Least Frequently Used):</strong> LFU replaces the page that has been used the least number of times. This algorithm keeps track of the frequency of access to determine which page to remove.</li>"
                            + "<li><strong>MFU (Most Frequently Used):</strong> MFU replaces the page that has been accessed the most times. This algorithm assumes that a frequently used page is likely to be replaced soon, so it removes it.</li>"
                            + "</ul>"
                            + "<p>Steps to use the simulator:</p>"
                            + "<ol>"
                            + "<li>Input the number of pages and frames you want to simulate.</li>"
                            + "<li>Choose the page replacement algorithm you want to test from the list.</li>"
                            + "<li>Provide a page reference string (a sequence of page numbers).</li>"
                            + "<li>Click on the 'Generate' button to run the simulation and observe the page replacements.</li>"
                            + "<li>Analyze the results, including the number of page faults for the selected algorithm.</li>"
                            + "</ol>"
                            + "<p>If you need further assistance, feel free to contact support or refer to the documentation provided.</p></html>";
                
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
