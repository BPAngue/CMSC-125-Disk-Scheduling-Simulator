package blockify;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class About extends Panels {
    
    private JPanel header, body;
    private JLabel titleLabel, bodyLabel;
    public JButton backButton;
    
    public About(){
        
    }
    
    @Override
    public void showComponents(){
        header = new JPanel(new FlowLayout(FlowLayout.RIGHT, 70, 40));
        header.setPreferredSize(new Dimension(1000, 250));
        header.setOpaque(false);
        
        backButton = createButton(150, 40, "BACK", gray, white, 20, null);
        
        titleLabel = createLabel(800, 100, white, "About", 60f);
        
        header.add(backButton);
        header.add(titleLabel);
        
        String aboutString = "<html><body style='text-align:right;'>This program was developed by Vall James Luceres, Rosemarie Negros, and Brian Pangue  "
                + "as a group project for CMSC 125. It was developed using Java Language and the GUI was made using Netbeans IDE.</body></html>";
        
        body = new JPanel(new FlowLayout(FlowLayout.CENTER));
        body.setOpaque(false);
 
        bodyLabel = createLabel(750, 200, white, aboutString, archivonarrow, 20f);
        bodyLabel.setIcon(smallLogoIcon);
        bodyLabel.setIconTextGap(20);
        bodyLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        
        body.add(bodyLabel);
                
        add(header);
        add(body);
        
    }
}
