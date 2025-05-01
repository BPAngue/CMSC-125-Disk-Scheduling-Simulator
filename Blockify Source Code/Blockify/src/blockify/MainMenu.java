package blockify;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel implements ActionListener{
    
    public CardLayout cardLayout;
    public JPanel cardPanel, outerPanel, titlePanel, buttonPanel;
    public JButton startButton, helpButton, aboutButton, exitButton;
    public JLabel titleLabel;
    public String token[];
    
    Panels panels = new Panels();
    Help helpPanel = new Help();
    About aboutPanel = new About();
    Simulator simulator = new Simulator();
    StartPage startPanel = new StartPage(simulator);
    
    Simulation simulationPanel;
    SimulationAll simulationAllPanel;
    
    public MainMenu (CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        showComponents();
    }
    
    public void showComponents(){
        outerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        outerPanel.setOpaque(false);
        outerPanel.setPreferredSize(new Dimension(1000, 800));
        
        titlePanel = new JPanel(new GridBagLayout());
        
        titleLabel = new JLabel();
        titleLabel.setIcon(panels.logoIcon);
        
        GridBagConstraints tgbc = new GridBagConstraints();
        tgbc.gridx = 0;
        tgbc.gridy = 0;
        tgbc.insets = new Insets(80, 0, 0, 0);

        titlePanel.add(titleLabel, tgbc);
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension (1000, 250));
       
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(1000, 500));
        
        startButton = panels.createButton("START", this);
        helpButton = panels.createButton("HELP", this);
        aboutButton = panels.createButton("ABOUT", this);
        exitButton = panels.createButton("EXIT", this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 25, 0);
        buttonPanel.add(startButton, gbc);
        
        gbc.gridy++;
        buttonPanel.add(helpButton, gbc);
        
        gbc.gridy++;
        buttonPanel.add(aboutButton, gbc);
        
        gbc.gridy++;
        buttonPanel.add(exitButton, gbc);
        
        outerPanel.add(titlePanel);
        outerPanel.add(buttonPanel);
        
        add(outerPanel);
        cardPanel.add(this, "MAIN_MENU");
        cardPanel.add(helpPanel, "HELP");
        cardPanel.add(aboutPanel, "ABOUT");
        cardPanel.add(startPanel, "START");
        
        // Action listeners (safe to add here)
        startPanel.backButton.addActionListener(this);
        startPanel.generateButton.addActionListener(this);
        startPanel.allButton.addActionListener(this);
        helpPanel.backButton.addActionListener(this);
        aboutPanel.backButton.addActionListener(this);
    }   
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(panels.bg, 0, 0, 1550, 800, null); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==exitButton){
            System.exit(0);
        }
        else if (e.getSource()==helpButton){
            cardLayout.show(cardPanel, "HELP");
        }
        else if (e.getSource()==aboutButton){
            cardLayout.show(cardPanel, "ABOUT");
        }
        else if (startButton == e.getSource()
                || (simulationPanel != null && simulationPanel.backButton == e.getSource())
                || (simulationAllPanel != null && simulationAllPanel.backButton == e.getSource())) {
            simulator.clearAlgorithm();
            simulator.clearHeadLocation();
            simulator.clearLength();
            simulator.clearCylinderQueue();
            startPanel.clearInputs();
            cardLayout.show(cardPanel, "START");
        }
        else if (e.getSource()==startPanel.backButton || e.getSource()==helpPanel.backButton
                || e.getSource()==aboutPanel.backButton){
            cardLayout.show(cardPanel, "MAIN_MENU");
        }
        else if (e.getSource()==startPanel.generateButton && startPanel.validateInput()){
            // add values to simulator class
            simulator.setAlgorithm(startPanel.algorithmField.getText());
            simulator.setHeadLocation(startPanel.headField.getText());
            simulator.setLength(startPanel.lengthField.getText());
            simulator.setDirection((String) startPanel.directionBox.getSelectedItem());
            token = startPanel.cylinderField.getText().split(" ");
            for (String cylinder : token) {
                simulator.addCylinder(cylinder);
            }
            
            if (simulator.getAlgorithm().equals("ALL")) {
                // System.out.println("ALL"); // for debugging
                // simulate all
                if (simulationAllPanel != null) cardPanel.remove(simulationAllPanel);
                simulationAllPanel = new SimulationAll(simulator);
                simulationAllPanel.backButton.addActionListener(this);
                cardPanel.add(simulationAllPanel, "ALL");
                simulationAllPanel.startSimulation();
                cardLayout.show(cardPanel, "ALL");
            } else {
                // System.out.println("Other algorithms"); // for debugging
                // normal page simulator panel
                if (simulationPanel != null) cardPanel.remove(simulationPanel);
                simulationPanel = new Simulation(simulator);
                simulationPanel.backButton.addActionListener(this);
                cardPanel.add(simulationPanel, "SIMULATOR");
                simulationPanel.startSimulation(simulator.getAlgorithm());
                cardLayout.show(cardPanel, "SIMULATOR");
            }  
            
            // for debugging
            System.out.println("Algorithm: " + simulator.getAlgorithm());
            System.out.println("Cylinders in the queue: " + simulator.getCylinders());
            System.out.println("Length of queue: " + simulator.getLength());
            System.out.println("Head Location: " + simulator.getHeadLocation());
            System.out.println("Direction: " + simulator.getDirection());
        }
    }
    
}
