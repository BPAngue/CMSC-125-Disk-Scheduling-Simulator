package blockify;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Simulation extends Panels implements ActionListener{
    
    private JPanel header, leftPanel, centerPanel, rightPanel, timerPanel, mainPanel, footer, speedPanel, infoPanel, 
            orderPanel, totalPanel, bottomLeftPanel, bottomRightPanel, seekTimePanel;
    private JLabel logoLabel, titleLabel, timerLabel,orderLabel, totalLabel, headLocationLabel, seekTimeLabel, cylinderValues, orderPanelTitle; 
    public JButton pdfButton, imgButton, restartButton, plusButton, minusButton, stopButton;
    public JButton backButton;
    private JTextField speedTextField;
    private Simulator simulator;
    
    // for simulation
    CartesianPanel cartesian;
    ArrayList<Integer> diskQueueLabel = new ArrayList<>();
    private Object currentSimulator;
    
    public Simulation(Simulator simulator){
        this.simulator = simulator;
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
        
        header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,25));
        header.setPreferredSize(new Dimension (1480, 180));
        header.setOpaque(false);
        
        logoLabel = new JLabel();
        logoLabel.setIcon(smallLogoIcon);
        
        orderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 1));
        orderPanel.setPreferredSize(new Dimension(590,40));
        orderPanelTitle = new JLabel("Order of Request: ");
        orderPanelTitle.setFont(archivoblack.deriveFont(16f));
        orderPanelTitle.setForeground(white);
        orderPanel.add(orderPanelTitle);
        
        float fontStyle;
        if (simulator.getLength() <= 20) {
            fontStyle = 13f;
        } else {
            fontStyle = 10f;
        }
        
        int count = 0;
        for (int cylinder : simulator.getCylinders()) {
            String cylinderText = "" + cylinder;
            if (count < simulator.getCylinders().size() - 1) {
                cylinderText += ",";
            }
            
            cylinderValues = new JLabel(cylinderText);
            cylinderValues.setFont(archivoblack.deriveFont(fontStyle));
            cylinderValues.setForeground(white);
            orderPanel.add(cylinderValues);
            count++;
        }
        
        //orderLabel = createLabel(590, 40, white, "Order of Requests: " + simulator.getCylinders(), 12);
        orderPanel.setBackground(darkpink);
        orderPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(white, 1),
                BorderFactory.createEmptyBorder(0,10,0,0)));
        // orderPanel.add(orderLabel);
        
        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,13));
        leftPanel.setPreferredSize(new Dimension(590, 200));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        leftPanel.add(orderPanel);
        
        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,0));
        centerPanel.setPreferredSize(new Dimension(300, 200));
        centerPanel.setOpaque(false);
        
        titleLabel = createLabel(820, 80, white, simulator.getAlgorithm(), 20);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        titleLabel.setHorizontalAlignment(center);
        
        pdfButton = createButton(135, 40, "Save as PDF", darkpink, white, white,14, this);
        imgButton = createButton(135, 40, "Save as Image", darkpink, white, white,14, this);
        
        centerPanel.add(titleLabel);
        
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,40));
        rightPanel.setPreferredSize(new Dimension(590, 200));
        rightPanel.setOpaque(false);
        
        backButton = createButton(200, 40, "BACK", gray, white,20, null);
        totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));
        totalPanel.setPreferredSize(new Dimension(435, 40));
        totalPanel.setBackground(darkpink);
        totalPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,0,0,50)
                , BorderFactory.createLineBorder(white, 1)));
        totalLabel = createLabel(280, 40, white, "Total head movements:", 12);
        totalPanel.add(totalLabel);
        
        timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.setPreferredSize(new Dimension(150, 40));
        timerPanel.setBorder(BorderFactory.createLineBorder(white, 1));
        timerPanel.setBackground(darkpink);
        
        timerLabel = createLabel(110, 25, white, "Timer: 00:00", 16f);
        titleLabel.setHorizontalAlignment(center);
        timerPanel.add(timerLabel);
        
        rightPanel.add(backButton);
        rightPanel.add(totalPanel);
        rightPanel.add(timerPanel);
        
        // main simulator panel
        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,0));
        mainPanel.setPreferredSize(new Dimension(1480, 470));
        mainPanel.setBackground(pink);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(white, 1), 
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(black, 6), 
                        BorderFactory.createEmptyBorder(0, 10, 0,10))));
        
        infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        infoPanel.setPreferredSize(new Dimension(1460, 40));
        infoPanel.setBackground(pink);
        
        headLocationLabel = createLabel(610, 40, white, "Head Location: " + simulator.getHeadLocation(), 20);
        headLocationLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(pink, 1)
                ,BorderFactory.createEmptyBorder(0,20,0,0)));
        
        seekTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
        seekTimePanel.setPreferredSize(new Dimension(250, 40));
        seekTimePanel.setBackground(darkpink);
        seekTimeLabel = createLabel(300, 40, white, "Seek Time: ", 20);
        seekTimePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(pink, 1)
                ,BorderFactory.createEmptyBorder(0,20,0,20)));
        seekTimePanel.add(seekTimeLabel);
        
        infoPanel.add(headLocationLabel);
        infoPanel.add(seekTimePanel);
        
        // cartesianPanel
        diskQueueLabel = simulator.getCylinders();
        int headLocation = simulator.getHeadLocation();
        if (!diskQueueLabel.contains(headLocation)) {
            diskQueueLabel.add(headLocation);
        }
        
        cartesian = new CartesianPanel(diskQueueLabel);
        cartesian.setPreferredSize(new Dimension(1460, 410));
        cartesian.setBackground(white);
        
        mainPanel.add(infoPanel);
        mainPanel.add(cartesian);
        
        header.add(leftPanel);
        header.add(centerPanel);
        header.add(rightPanel);
        
        footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        footer.setPreferredSize(new Dimension(1480, 60));
        footer.setOpaque(false);
        
        speedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,10));
        speedPanel.setPreferredSize(new Dimension(370, 60));
        speedPanel.setOpaque(false);
        speedPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,0,50));
        
        minusButton = createButton(60,40, "-", red, white,white, 20,1, this);
        plusButton = createButton(60,40, "+", green, white, white, 20, 1, this);
        speedTextField = createField(200, 40, darkpink, 16f, false);
        speedTextField.setText("1000");
        speedTextField.setHorizontalAlignment(center);
        
        speedPanel.add(minusButton);
        speedPanel.add(speedTextField);
        speedPanel.add(plusButton);
        
        restartButton = createButton(150,40, "Restart", red, white,white, 16,1, this);
        stopButton = createButton(150,40, "Stop", darkpink, white,white, 16,1, this);
        
        bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
        bottomLeftPanel.setPreferredSize(new Dimension(740, 60));
        bottomLeftPanel.setOpaque(false);
        bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 5));
        bottomRightPanel.setPreferredSize(new Dimension(740, 60));
        bottomRightPanel.setOpaque(false);
        
        bottomLeftPanel.add(speedPanel);
        bottomLeftPanel.add(restartButton);
        bottomLeftPanel.add(stopButton);
        bottomRightPanel.add(pdfButton);
        bottomRightPanel.add(imgButton);
        
        footer.add(bottomLeftPanel);
        footer.add(bottomRightPanel);
        
        add(header);
        add(mainPanel);
        add(footer);
    }
   
    public void startSimulation(String algorithm) {
        switch(algorithm) {
            case "FCFS":
                currentSimulator = new FCFS(simulator.getCylinders(), simulator.getHeadLocation());
                ((FCFS) currentSimulator).startSimulation();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       
    }
}
