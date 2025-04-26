package blockify;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BlockifyMain {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Blockify");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1550, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        frame.setContentPane(cardPanel);
        
        MainMenu mainMenu = new MainMenu(cardLayout, cardPanel);        
        
        frame.setVisible(true);
    }
    
}
