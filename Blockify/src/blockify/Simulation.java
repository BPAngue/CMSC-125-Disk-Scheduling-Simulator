package blockify;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Simulation extends Panels implements SimulationContext, ActionListener{
    
    private JPanel header, leftPanel, centerPanel, rightPanel, timerPanel, mainPanel, footer, speedPanel, infoPanel, 
            orderPanel, totalPanel, bottomLeftPanel, bottomRightPanel, seekTimePanel;
    private JLabel logoLabel, titleLabel, orderLabel, totalLabel, headLocationLabel, cylinderValues, orderPanelTitle; 
    public JButton pdfButton, imgButton, restartButton, plusButton, minusButton, stopButton;
    public JButton backButton;
    private JTextField speedTextField;
    private Simulator simulator;
    
    // seek time label
    private JLabel seekTimeLabelFcfs;
    private JLabel seekTimeLabelSstf;
    private JLabel seekTimeLabelScan;
    private JLabel seekTimeLabelCscan;
    private JLabel seekTimeLabelLook;
    private JLabel seekTimeLabelClook;
    
    // timer label
    private JLabel timerLabelFcfs;
    private JLabel timerLabelSstf;
    private JLabel timerLabelScan;
    private JLabel timerLabelCscan;
    private JLabel timerLabelLook;
    private JLabel timerLabelClook;
    
    // for simulation
    public CartesianPanel cartesian;
    public ArrayList<Integer> diskQueueLabel = new ArrayList<>();
    public ArrayList<Movement> headMovements = new ArrayList<>();
    private int yLength;
    private boolean simulationFinished = false;
    private Object currentSimulator;
    
    public Simulation(Simulator simulator){
        this.simulator = simulator;
        String algorithm = simulator.getAlgorithm();
        
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
        
        orderPanel.setBackground(darkpink);
        orderPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(white, 1),
                BorderFactory.createEmptyBorder(0,10,0,0)));
        
        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,13));
        leftPanel.setPreferredSize(new Dimension(590, 200));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        leftPanel.add(orderPanel);
        
        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,0));
        centerPanel.setPreferredSize(new Dimension(300, 200));
        centerPanel.setOpaque(false);
        
        titleLabel = createLabel(820, 80, white, simulator.getAlgorithm(), 50);
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
        
        timerLabelFcfs = createLabel(110, 25, white, "Timer: 00:00", 16f);
        timerLabelSstf = createLabel(110, 25, white, "Timer: 00:00", 16f);
        timerLabelScan = createLabel(110, 25, white, "Timer: 00:00", 16f);
        timerLabelCscan = createLabel(110, 25, white, "Timer: 00:00", 16f);
        timerLabelLook = createLabel(110, 25, white, "Timer: 00:00", 16f);
        timerLabelClook = createLabel(110, 25, white, "Timer: 00:00", 16f);
        
        timerLabelFcfs.setHorizontalAlignment(center);
        timerLabelSstf.setHorizontalAlignment(center);
        timerLabelScan.setHorizontalAlignment(center);
        timerLabelCscan.setHorizontalAlignment(center);
        timerLabelLook.setHorizontalAlignment(center);
        timerLabelClook.setHorizontalAlignment(center);
        
        switch(algorithm) {
            case "FCFS":
                timerPanel.add(timerLabelFcfs);
                break;
            case "SSTF":
                timerPanel.add(timerLabelSstf);
                break;
            case "SCAN":
                timerPanel.add(timerLabelScan);
                break;
            case "C-SCAN":
                timerPanel.add(timerLabelCscan);
                break;
            case "LOOK":
                timerPanel.add(timerLabelLook);
                break;
            case "C-LOOK":
                timerPanel.add(timerLabelClook);
                break;
        }
        
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
        
        seekTimeLabelFcfs = createLabel(300, 40, white, "Seek Time: 0", 20);
        seekTimeLabelSstf = createLabel(300, 40, white, "Seek Time: 0", 20);
        seekTimeLabelScan = createLabel(300, 40, white, "Seek Time: 0", 20);
        seekTimeLabelCscan = createLabel(300, 40, white, "Seek Time: 0", 20);
        seekTimeLabelLook = createLabel(300, 40, white, "Seek Time: 0", 20);
        seekTimeLabelClook = createLabel(300, 40, white, "Seek Time: 0", 20);
        
        seekTimePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(pink, 1)
                ,BorderFactory.createEmptyBorder(0,20,0,20)));
        
        switch(algorithm) {
            case "FCFS":
                seekTimePanel.add(seekTimeLabelFcfs);
                break;
            case "SSTF":
                seekTimePanel.add(seekTimeLabelSstf);
                break;
            case "SCAN":
                seekTimePanel.add(seekTimeLabelScan);
                break;
            case "C-SCAN":
                seekTimePanel.add(seekTimeLabelCscan);
                break;
            case "LOOK":
                seekTimePanel.add(seekTimeLabelLook);
                break;
            case "C-LOOK":
                seekTimePanel.add(seekTimeLabelClook);
                break;
        }
        
        infoPanel.add(headLocationLabel);
        infoPanel.add(seekTimePanel);
        
        // cartesianPanel
        diskQueueLabel = new ArrayList<>(simulator.getCylinders());
        int headLocation = simulator.getHeadLocation();
        if (!diskQueueLabel.contains(headLocation)) {
            diskQueueLabel.add(headLocation);
        }
        
        // set ylength
        yLength = simulator.getCylinders().size() + 1;
        
        cartesian = new CartesianPanel(diskQueueLabel, headMovements);
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
        stopButton = createButton(150,40, "Pause", darkpink, white,white, 16,1, this);
        
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
        pdfButton.setEnabled(false);
        imgButton.setEnabled(false);
        plusButton.setEnabled(false);
        minusButton.setEnabled(false);
        
        int simulationSpeed = Integer.parseInt(speedTextField.getText());
        speedTextField.setText(String.valueOf(simulationSpeed));
        simulator.speed = simulationSpeed;
        
        switch(algorithm) {
            case "FCFS":
                cartesian.yCoordLength = yLength;
                currentSimulator = new FCFS(simulator, cartesian, simulator.getCylinders(), headMovements, simulator.getHeadLocation(), simulationSpeed, this);
                ((FCFS) currentSimulator).startSimulation();
                break;
            case "SSTF":
                cartesian.yCoordLength = yLength;
                currentSimulator = new SSTF(simulator, cartesian, simulator.getCylinders(), headMovements, simulator.getHeadLocation(), simulationSpeed, this);
                ((SSTF) currentSimulator).startSimulation();
                break;
            case "SCAN":
                cartesian.yCoordLength = yLength + 1;
                currentSimulator = new SCAN(simulator, cartesian, simulator.getCylinders(), headMovements, simulator.getHeadLocation(), simulator.getDirection(), simulationSpeed, this);
                ((SCAN) currentSimulator).startSimulation();
                break;
            case "C-SCAN":
                cartesian.yCoordLength = yLength + 2;
                currentSimulator = new CSCAN(simulator, cartesian, simulator.getCylinders(), headMovements, simulator.getHeadLocation(), simulator.getDirection(), simulationSpeed, this);
                ((CSCAN) currentSimulator).startSimulation();
                break;
            case "LOOK":
                cartesian.yCoordLength = yLength;
                currentSimulator = new LOOK(simulator, cartesian, simulator.getCylinders(), headMovements, simulator.getHeadLocation(), simulator.getDirection(), simulationSpeed, this);
                ((LOOK) currentSimulator).startSimulation();
                break;
            case "C-LOOK":
                cartesian.yCoordLength = yLength;
                currentSimulator = new CLOOK(simulator, cartesian, simulator.getCylinders(), headMovements, simulator.getHeadLocation(), simulator.getDirection(), simulationSpeed, this);
                ((CLOOK) currentSimulator).startSimulation();
                break;
        }
    }
    
    @Override
    public void updateSeekTimeLabel(int totalSeekTime) {
        switch(simulator.getAlgorithm()) {
            case "FCFS":
                seekTimeLabelFcfs.setText("Seek Time: " + totalSeekTime);
                break;
            case "SSTF":
                seekTimeLabelSstf.setText("Seek Time: " + totalSeekTime);
                break;
            case "SCAN":
                seekTimeLabelScan.setText("Seek Time: " + totalSeekTime);
                break;
            case "C-SCAN":
                seekTimeLabelCscan.setText("Seek Time: " + totalSeekTime);
                break;
            case "LOOK":
                seekTimeLabelLook.setText("Seek Time: " + totalSeekTime);
                break;
            case "C-LOOK":
                seekTimeLabelClook.setText("Seek Time: " + totalSeekTime);
                break;
        }
    }
    
    @Override
    public void updateSeekTimeLabelFcfs(int totalSeekTime) {}
    
    @Override
    public void updateSeekTimeLabelSstf(int totalSeekTime) {}
    
    @Override
    public void updateSeekTimeLabelScan(int totalSeekTime) {}
    
    @Override
    public void updateSeekTimeLabelCscan(int totalSeekTime) {}
    
    @Override
    public void updateSeekTimeLabelLook(int totalSeekTime) {}
    
    @Override
    public void updateSeekTimeLabelClook(int totalSeekTime) {}
    
    @Override
    public void updateTimerLabel(int minutes, int seconds) {
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        switch(simulator.getAlgorithm()) {
            case "FCFS":
                timerLabelFcfs.setText("Timer: " + timeFormatted);
                break;
            case "SSTF":
                timerLabelSstf.setText("Timer: " + timeFormatted);
                break;
            case "SCAN":
                timerLabelScan.setText("Timer: " + timeFormatted);
                break;
            case "C-SCAN":
                timerLabelCscan.setText("Timer: " + timeFormatted);
                break;
            case "LOOK":
                timerLabelLook.setText("Timer: " + timeFormatted);
                break;
            case "C-LOOK":
                timerLabelClook.setText("Timer: " + timeFormatted);
                break;
        }
    }
    
    @Override
    public void updateTimerLabelFcfs(int minutes, int seconds){}
    
    @Override
    public void updateTimerLabelSstf(int minutes, int seconds){}
    
    @Override
    public void updateTimerLabelScan(int minutes, int seconds){}
    
    @Override
    public void updateTimerLabelCscan(int minutes, int seconds){}
    
    @Override
    public void updateTimerLabelLook(int minutes, int seconds){}
    
    @Override
    public void updateTimerLabelClook(int minutes, int seconds){}
    
    @Override
    public void markSimulationFinished() {
        simulationFinished = true;
        stopButton.setEnabled(false);
        pdfButton.setEnabled(true);
        imgButton.setEnabled(true);
        plusButton.setEnabled(true);
        minusButton.setEnabled(true);
    }
    
    private File makeScreenshotsDirectory() {
        File dir = new File("screenshots");
        if (!dir.exists()) {
                dir.mkdirs();
        }
        
        return dir;
    }

    private File saveAsPNG(String fileName) throws IOException {
        makeScreenshotsDirectory();
        
        // Capture panel as image
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        this.printAll(g2d);
        g2d.dispose();
    
        // Save the image as a PNG file
        File pngFile = new File("screenshots/" + fileName + ".png");
        ImageIO.write(image, "PNG", pngFile);
        
        return pngFile;
    }
    
    public void savePanelAsPDF(String fileName) throws Exception {
        makeScreenshotsDirectory();
        
        
        // Create a BufferedImage and paint the panel onto it
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        this.printAll(g2d);
        g2d.dispose();

        File pdfFile = new File("screenshots/" + fileName + ".pdf");
        
        // Create PDF document
        Document document = new Document(new com.itextpdf.text.Rectangle(this.getWidth(), this.getHeight()));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        PdfContentByte contentByte = writer.getDirectContent();
        com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(image, null);
        pdfImage.setAbsolutePosition(0, 0);
        contentByte.addImage(pdfImage);

        document.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == imgButton) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String timestamp = sdf.format(new Date());

            String fileName = timestamp + "_PG";
            try {
                File pngFile = saveAsPNG(fileName);
                JOptionPane.showMessageDialog(this, "PNG successfully saved to \\Blockify\\screenshots!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
        
        else if (e.getSource() == pdfButton) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String timestamp = sdf.format(new Date());
            String fileName = timestamp + "_PG";

            try {
                savePanelAsPDF(fileName);
                JOptionPane.showMessageDialog(this, "PDF successfully saved to \\Blockify\\screenshots!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
       
        else if (e.getSource() == stopButton) {
            Timer timer = null;
            
            if (currentSimulator instanceof FCFS fcfs) {
                timer = fcfs.getTimer();
            } else if(currentSimulator instanceof SSTF sstf) {
                timer = sstf.getTimer();
            } else if(currentSimulator instanceof SCAN scan) {
                timer = scan.getTimer();
            } else if(currentSimulator instanceof CSCAN cscan) {
                timer = cscan.getTimer();
            } else if(currentSimulator instanceof LOOK look) {
                timer = look.getTimer();
            } else if(currentSimulator instanceof CLOOK clook) {
                timer = clook.getTimer();
            }
            
            if (timer != null) {
                if (timer.isRunning()) {
                    timer.stop();
                    
                    if (currentSimulator instanceof PausableSimulator ps) {
                        ps.recordPauseStart();
                    }
                    
                    pdfButton.setEnabled(true);
                    imgButton.setEnabled(true);
                    plusButton.setEnabled(true);
                    minusButton.setEnabled(true);
                    stopButton.setText("Play");
                } else {
                    int newSpeed = Integer.parseInt(speedTextField.getText());
                    timer.setDelay(newSpeed);
                    
                    if (currentSimulator instanceof FCFS fcfs) {
                        fcfs.setSimulationSpeed(newSpeed);
                    } else if(currentSimulator instanceof SSTF sstf) {
                        sstf.setSimulationSpeed(newSpeed);
                    } else if(currentSimulator instanceof SCAN scan) {
                        scan.setSimulationSpeed(newSpeed);
                    } else if(currentSimulator instanceof CSCAN cscan) {
                        cscan.setSimulationSpeed(newSpeed);
                    } else if(currentSimulator instanceof LOOK look) {
                        look.setSimulationSpeed(newSpeed);
                    } else if(currentSimulator instanceof CLOOK clook) {
                        clook.setSimulationSpeed(newSpeed);
                    }
                    
                    if (currentSimulator instanceof PausableSimulator ps) {
                        ps.resumeAfterPause();
                    }
                    
                    timer.start();
                    pdfButton.setEnabled(false);
                    imgButton.setEnabled(false);
                    plusButton.setEnabled(false);
                    minusButton.setEnabled(false);
                    stopButton.setText("Pause");
                }
            }
        }
       
        else if (e.getSource() == restartButton) {
            Timer timer = null;
            
            if (currentSimulator instanceof FCFS fcfs) {
                timer = fcfs.getTimer();
            } else if(currentSimulator instanceof SSTF sstf) {
                timer = sstf.getTimer();
            } else if(currentSimulator instanceof SCAN scan) {
                timer = scan.getTimer();
            } else if(currentSimulator instanceof CSCAN cscan) {
                timer = cscan.getTimer();
            } else if(currentSimulator instanceof LOOK look) {
                timer = look.getTimer();
            } else if(currentSimulator instanceof CLOOK clook) {
                timer = clook.getTimer();
            }
            
            if (timer != null) {
                timer.stop();
            }
            
            // Reset GUI Labels
            seekTimeLabelFcfs.setText("Seek Time: 0");
            seekTimeLabelSstf.setText("Seek Time: 0");
            seekTimeLabelScan.setText("Seek Time: 0");
            seekTimeLabelCscan.setText("Seek Time: 0");
            seekTimeLabelLook.setText("Seek Time: 0");
            seekTimeLabelClook.setText("Seek Time: 0");
            
            timerLabelFcfs.setText("Timer: 00:00");
            timerLabelSstf.setText("Timer: 00:00");
            timerLabelScan.setText("Timer: 00:00");
            timerLabelCscan.setText("Timer: 00:00");
            timerLabelLook.setText("Timer: 00:00");
            timerLabelClook.setText("Timer: 00:00");
            
            // Reset Internal states
            simulationFinished = false;
            stopButton.setEnabled(true);
            stopButton.setText("Pause");
            
            // clear previous movements
            headMovements.clear();
            
            // start a fresh simulation
            startSimulation(simulator.getAlgorithm());
            
            // disable speed adjustments and save buttons during simulation
            pdfButton.setEnabled(false);
            imgButton.setEnabled(false);
            plusButton.setEnabled(false);
            minusButton.setEnabled(false);
        }
       
        else if (e.getSource() == plusButton) {
            int speed = Integer.parseInt(speedTextField.getText());
            if (speed < 2000) {
                if (speed == 100) {
                    speed += 150;
                } else if (speed + 250 > 2000) {
                    speed = 2000;
                } else {
                    speed += 250;
                }
                speedTextField.setText(String.valueOf(speed));
            }
        }
       
        else if (e.getSource() == minusButton) {
            int speed = Integer.parseInt(speedTextField.getText());
            if (speed > 100) {
                if (speed == 250) {
                    speed -= 150;
                } else{
                    speed -= 250;
                }
                
                if (speed < 100) {
                    speed = 100;
                }
                speedTextField.setText(String.valueOf(speed));
            }
        }
    }
}
