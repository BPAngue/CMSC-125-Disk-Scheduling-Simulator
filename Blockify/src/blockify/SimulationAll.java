package blockify;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class SimulationAll extends Panels implements SimulationContext, ActionListener{
    
    private JPanel header, leftPanel, centerPanel, rightPanel, timerPanel, mainPanel, footer, speedPanel, infoPanel, 
            orderPanel, totalPanel, bottomLeftPanel, bottomRightPanel, seekTimePanel;
    private JLabel logoLabel, titleLabel, orderLabel, totalLabel, headLocationLabel, cylinderValues, orderPanelTitle; 
    public JButton pdfButton, imgButton, restartButton, plusButton, minusButton, stopButton;
    public JButton backButton;
    public JTabbedPane tabbedPane;
    private JTextField speedTextField;
    private Simulator simulator;
    
    private JPanel fcfsPanel, sstfPanel, scanPanel, cscanPanel, lookPanel, clookPanel;
    
    // for simulation
    private boolean simulationFinished = false;
    private int yLength;
    private CartesianPanel cartesianFcfs;
    private CartesianPanel cartesianSstf;
    private CartesianPanel cartesianScan;
    private CartesianPanel cartesianCscan;
    private CartesianPanel cartesianLook;
    private CartesianPanel cartesianClook;
    
    // same Disk queue label for each
    ArrayList<Integer> diskQueueLabel = new ArrayList<>();
    
    // different head movements instances for each
    public ArrayList<Movement> headMovementsFcfs = new ArrayList<>();
    public ArrayList<Movement> headMovementsSstf = new ArrayList<>();
    public ArrayList<Movement> headMovementsScan = new ArrayList<>();
    public ArrayList<Movement> headMovementsCscan = new ArrayList<>();
    public ArrayList<Movement> headMovementsLook = new ArrayList<>();
    public ArrayList<Movement> headMovementsClook = new ArrayList<>();
    
    // different seek time labels for each
    private JLabel seekTimeLabelFcfs;
    private JLabel seekTimeLabelSstf;
    private JLabel seekTimeLabelScan;
    private JLabel seekTimeLabelCscan;
    private JLabel seekTimeLabelLook;
    private JLabel seekTimeLabelClook;
    
    // different timer labels for each
    private JLabel timerLabelFcfs;
    private JLabel timerLabelSstf;
    private JLabel timerLabelScan;
    private JLabel timerLabelCscan;
    private JLabel timerLabelLook;
    private JLabel timerLabelClook;
    
    // classes
    private FCFS fcfs;
    private SSTF sstf;
    private SCAN scan;
    private CSCAN cscan;
    private LOOK look;
    private CLOOK clook;
    
    // threads
    private Thread fcfsThread;
    private Thread sstfThread;
    private Thread scanThread;
    private Thread cscanThread;
    private Thread lookThread;
    private Thread clookThread;
    
    public SimulationAll(Simulator simulator){
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
        
        titleLabel = createLabel(820, 80, white, "FCFS", 50);
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
        
        titleLabel.setHorizontalAlignment(center);
        timerPanel.add(timerLabelFcfs);
        
        rightPanel.add(backButton);
        rightPanel.add(totalPanel);
        rightPanel.add(timerPanel);
        
        header.add(leftPanel);
        header.add(centerPanel);
        header.add(rightPanel);
        
        // cartesianPanel
        diskQueueLabel = new ArrayList<>(simulator.getCylinders());
        int headLocation = simulator.getHeadLocation();
        if (!diskQueueLabel.contains(headLocation)) {
            diskQueueLabel.add(headLocation);
        }
        
        yLength = simulator.getCylinders().size() + 1;
        
        // seek Time labels
        seekTimeLabelFcfs = createLabel(300, 40, white, "Seek Time: ", 20);
        seekTimeLabelSstf = createLabel(300, 40, white, "Seek Time: ", 20);
        seekTimeLabelScan = createLabel(300, 40, white, "Seek Time: ", 20);
        seekTimeLabelCscan = createLabel(300, 40, white, "Seek Time: ", 20);
        seekTimeLabelLook = createLabel(300, 40, white, "Seek Time: ", 20);
        seekTimeLabelClook = createLabel(300, 40, white, "Seek Time: ", 20);
        
        // cartesian instances
        cartesianFcfs = new CartesianPanel(diskQueueLabel, headMovementsFcfs);
        cartesianSstf = new CartesianPanel(diskQueueLabel, headMovementsSstf);
        cartesianScan = new CartesianPanel(diskQueueLabel, headMovementsScan);
        cartesianCscan = new CartesianPanel(diskQueueLabel, headMovementsCscan);
        cartesianLook = new CartesianPanel(diskQueueLabel, headMovementsLook);
        cartesianClook = new CartesianPanel(diskQueueLabel, headMovementsClook);
        
        // different cartesian panels for each
        fcfsPanel = addMainPanel(cartesianFcfs, seekTimeLabelFcfs);
        sstfPanel = addMainPanel(cartesianSstf, seekTimeLabelSstf);
        scanPanel = addMainPanel(cartesianScan, seekTimeLabelScan);
        cscanPanel = addMainPanel(cartesianCscan, seekTimeLabelCscan);
        lookPanel = addMainPanel(cartesianLook, seekTimeLabelLook);
        clookPanel = addMainPanel(cartesianClook, seekTimeLabelClook);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(1480, 500));
        tabbedPane.setFocusable(false);
        tabbedPane.setBackground(white);
        tabbedPane.setForeground(darkpink);
        tabbedPane.setFont(archivoblack.deriveFont(14f));
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        tabbedPane.add("FCFS", fcfsPanel);
        tabbedPane.add("SSTF", sstfPanel);
        tabbedPane.add("SCAN", scanPanel);
        tabbedPane.add("C-SCAN", cscanPanel);
        tabbedPane.add("LOOK", lookPanel);
        tabbedPane.add("C-LOOK", clookPanel);
        
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String title = tabbedPane.getTitleAt(selectedIndex);
            titleLabel.setText(title);
            
            // remove existing label
            timerPanel.removeAll();
            
            // add correct timer label based on selected tab
            switch(title) {
                case "FCFS" -> timerPanel.add(timerLabelFcfs);
                case "SSTF" -> timerPanel.add(timerLabelSstf);
                case "SCAN" -> timerPanel.add(timerLabelScan);
                case "C-SCAN" -> timerPanel.add(timerLabelCscan);
                case "LOOK" -> timerPanel.add(timerLabelLook);
                case "C-LOOK" -> timerPanel.add(timerLabelClook);
            }
            
            timerPanel.revalidate();
            timerPanel.repaint();
        });
        
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
        add(tabbedPane);
        add(footer);
    }
   
    public void startSimulation() {
        pdfButton.setEnabled(false);
        imgButton.setEnabled(false);
        plusButton.setEnabled(false);
        minusButton.setEnabled(false);
        
        int simulationSpeed = Integer.parseInt(speedTextField.getText());
        speedTextField.setText(String.valueOf(simulationSpeed));
        simulator.speed = simulationSpeed;
        
        // i want to simultaneously start all of the simulation classes here
        // call all of the simulator classes
        cartesianFcfs.yCoordLength = yLength;
        fcfs = new FCFS(simulator, cartesianFcfs, simulator.getCylinders(), headMovementsFcfs, simulator.getHeadLocation(), simulationSpeed, this);
        fcfsThread = new Thread(fcfs);
        
        cartesianSstf.yCoordLength = yLength;
        sstf = new SSTF(simulator, cartesianSstf, simulator.getCylinders(), headMovementsSstf, simulator.getHeadLocation(), simulationSpeed, this);
        sstfThread = new Thread(sstf);
        
        cartesianScan.yCoordLength = yLength + 1;
        scan = new SCAN(simulator, cartesianScan, simulator.getCylinders(), headMovementsScan, simulator.getHeadLocation(), simulator.getDirection(), simulationSpeed, this);
        scanThread = new Thread(scan);
        
        cartesianCscan.yCoordLength = yLength + 2;
        cscan = new CSCAN(simulator, cartesianCscan, simulator.getCylinders(), headMovementsCscan, simulator.getHeadLocation(), simulator.getDirection(), simulationSpeed, this);
        cscanThread = new Thread(cscan);
        
        cartesianLook.yCoordLength = yLength;
        look = new LOOK(simulator, cartesianLook, simulator.getCylinders(), headMovementsLook, simulator.getHeadLocation(), simulator.getDirection(), simulationSpeed, this);
        lookThread = new Thread(look);
        
        cartesianClook.yCoordLength = yLength;
        clook = new CLOOK(simulator, cartesianClook, simulator.getCylinders(), headMovementsClook, simulator.getHeadLocation(), simulator.getDirection(), simulationSpeed, this);
        clookThread = new Thread(clook);
        
        // start threads
        fcfsThread.start();
        sstfThread.start();
        scanThread.start();
        cscanThread.start();
        lookThread.start();
        clookThread.start();
    }
    
    public final JPanel addMainPanel(CartesianPanel cartesian, JLabel seekTimeLabel){
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
        seekTimePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(pink, 1)
                ,BorderFactory.createEmptyBorder(0,20,0,20)));
        seekTimePanel.add(seekTimeLabel);
        
        infoPanel.add(headLocationLabel);
        infoPanel.add(seekTimePanel);
        
        // cartesianPanel
        cartesian.setPreferredSize(new Dimension(1460, 410));
        cartesian.setBackground(white);
        
        mainPanel.add(infoPanel);
        mainPanel.add(cartesian);
        
        return mainPanel;
    }
    
    public void saveTabbedPaneAsPNG() throws Exception {
        if (tabbedPane == null || tabbedPane.getTabCount() == 0) {
            throw new Exception("TabbedPane is empty or null.");
        }

        // Calculate total height required for the combined image
        int totalHeight = 0;
        int width = this.getWidth();

        // Get the height of each tab by capturing its image
        ArrayList<BufferedImage> tabImages = new ArrayList<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setSelectedIndex(i); // Activate the tab
            tabbedPane.revalidate();
            tabbedPane.repaint();

            BufferedImage tabImage = new BufferedImage(width, this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = tabImage.createGraphics();
            this.paint(g2);
            g2.dispose();

            tabImages.add(tabImage);
            totalHeight += tabImage.getHeight(); // Add height of this tab to total height
        }

        // Create a new image with the combined height
        BufferedImage combinedImage = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = combinedImage.createGraphics();

        // Draw each tab's image onto the combined image
        int yOffset = 0;
        for (BufferedImage tabImage : tabImages) {
            g2.drawImage(tabImage, 0, yOffset, null);
            yOffset += tabImage.getHeight(); // Move the yOffset down by the height of the current tab
        }
        g2.dispose();

        // Save the combined image as a PNG
        File dir = new File("screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MMddyy_HHmmss");
        String timestamp = formatter.format(new Date());
        File outputFile = new File(dir, timestamp + "_PG.png");

        ImageIO.write(combinedImage, "png", outputFile);
    }

    public void saveTabbedPaneAsPDF() throws Exception {
        if (tabbedPane == null || tabbedPane.getTabCount() == 0) {
            throw new Exception("JTabbedPane is empty or null!");
        }

        // Create screenshots directory if it doesn't exist
        File dir = new File("screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate filename with timestamp
        SimpleDateFormat formatter = new SimpleDateFormat("MMddyy_HHmmss");
        String timestamp = formatter.format(new Date());
        String filename = timestamp + "_PG.pdf";
        File outputFile = new File(dir, filename);

        // Create PDF document
        Document document = new Document(new com.itextpdf.text.Rectangle(this.getWidth(), this.getHeight()));
        PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        document.open();

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setSelectedIndex(i);

            BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            this.paint(g2);
            g2.dispose();

            Image pdfImage = Image.getInstance(image, null);
            pdfImage.setAbsolutePosition(0, 0);
            pdfImage.scaleToFit(this.getWidth(), this.getHeight());

            document.newPage();
            document.add(pdfImage);
        }

        document.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == imgButton) {
            try {
                saveTabbedPaneAsPNG();
                JOptionPane.showMessageDialog(this, "Files successfully saved to \\Blockify\\screenshots!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        else if (e.getSource() == pdfButton) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String timestamp = sdf.format(new Date());

            try {
                saveTabbedPaneAsPDF();
                JOptionPane.showMessageDialog(this, "PDF successfully saved to \\Blockify\\screenshots!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
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

    @Override
    public void updateSeekTimeLabel(int totalSeekTime) {}
    
    @Override
    public void updateSeekTimeLabelFcfs(int totalSeekTime) {
        seekTimeLabelFcfs.setText("Seek Time: " + totalSeekTime);
    }
    
    @Override
    public void updateSeekTimeLabelSstf(int totalSeekTime) {
        seekTimeLabelSstf.setText("Seek Time: " + totalSeekTime);
    }
    
    @Override
    public void updateSeekTimeLabelScan(int totalSeekTime) {
        seekTimeLabelScan.setText("Seek Time: " + totalSeekTime);
    }
    
    @Override
    public void updateSeekTimeLabelCscan(int totalSeekTime) {
        seekTimeLabelCscan.setText("Seek Time: " + totalSeekTime);
    }
    
    @Override
    public void updateSeekTimeLabelLook(int totalSeekTime) {
        seekTimeLabelLook.setText("Seek Time: " + totalSeekTime);
    }
    
    @Override
    public void updateSeekTimeLabelClook(int totalSeekTime) {
        seekTimeLabelClook.setText("Seek Time: " + totalSeekTime);
    }

    @Override
    public void updateTimerLabel(int minutes, int seconds) {}
    
    @Override
    public void updateTimerLabelFcfs(int minutes, int seconds){
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerLabelFcfs.setText("Timer: " + timeFormatted);
    }
    
    @Override
    public void updateTimerLabelSstf(int minutes, int seconds){
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerLabelSstf.setText("Timer: " + timeFormatted);
    }
    
    @Override
    public void updateTimerLabelScan(int minutes, int seconds){
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerLabelScan.setText("Timer: " + timeFormatted);
    }
    
    @Override
    public void updateTimerLabelCscan(int minutes, int seconds){
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerLabelCscan.setText("Timer: " + timeFormatted);
    }
    
    @Override
    public void updateTimerLabelLook(int minutes, int seconds){
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerLabelLook.setText("Timer: " + timeFormatted);
    }
    
    @Override
    public void updateTimerLabelClook(int minutes, int seconds){
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerLabelClook.setText("Timer: " + timeFormatted);
    }

    @Override
    public void markSimulationFinished() {
        simulationFinished = true;
        stopButton.setEnabled(false);
        pdfButton.setEnabled(true);
        imgButton.setEnabled(true);
        plusButton.setEnabled(true);
        minusButton.setEnabled(true);
    }
}
