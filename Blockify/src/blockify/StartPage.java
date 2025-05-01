package blockify;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StartPage extends Panels implements ActionListener{
    
    private JPanel titlePanel, selectionPanel, algorithmPanel,outlinePanel, buttonPanel;
    private JLabel titleLabel, selectLabel, outlineLabel, algorithmLabel, cylinderLabel, lengthLabel, headLabel, directionLabel, errorLabel; 
    public JTextField algorithmField, cylinderField, lengthField, headField;
    public JButton backButton, fcfsButton, sstfButton, scanButton, cscanButton, lookButton, clookButton, allButton, 
            generateButton, resetButton, randomButton, textButton;
    public JComboBox directionBox;
    public boolean hasInvalid = false;
    
    public List<Integer> randomCylinders = new ArrayList<>();
    public List<String> cylinderDetails = new ArrayList<>();
    
    // for storing inputs to simulator class
    public String[] tokens;
    Simulator simulator;
    
    public StartPage(Simulator simulator){
        this.simulator = simulator;
    }
    
    @Override
    public void showComponents(){
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        
        /// title panel ////
        
        titlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 110,0));
        titlePanel.setPreferredSize(new Dimension(1500, 100));
        titlePanel.setOpaque(false);
        
        titleLabel = new JLabel();
        titleLabel.setPreferredSize(new Dimension(500, 115));
        titleLabel.setIcon(smallLogoIcon);
        backButton = createButton(150, 40, "BACK", gray, white,20, this);
        
        titlePanel.add(titleLabel);
        titlePanel.add(backButton);
        
        /// selection panel ///
        
        selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,5));
        selectionPanel.setPreferredSize(new Dimension (900, 580));
        selectionPanel.setBackground(pink);
        selectionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(white, 1), 
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(black, 6), 
                        BorderFactory.createEmptyBorder(10, 10, 10,10))));
        selectLabel = createLabel(822, 40, white, "Select Algorithm", 16f);
        
        /// algorithm panel ///
        
        algorithmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        algorithmPanel.setOpaque(false);
        algorithmPanel.setPreferredSize(new Dimension(870, 130));
        
        fcfsButton = createButton(192, 55, "FCFS", darkpink, white,white, 16, this);
        sstfButton = createButton(192, 55, "SSTF", darkpink, white, white,16, this);
        scanButton = createButton(192, 55, "SCAN", darkpink, white, white, 16, this);
        cscanButton = createButton(192, 55, "C-SCAN", darkpink, white, white, 16, this);
        lookButton = createButton(192, 55, "LOOK",darkpink, white, white, 16, this);
        clookButton = createButton(192, 55, "C-LOOK", darkpink, white, white,16, this);
        allButton = createButton(192, 55, "ALL", darkpink, white, white,16, this);

        algorithmPanel.add(fcfsButton);
        algorithmPanel.add(sstfButton);
        algorithmPanel.add(scanButton);
        algorithmPanel.add(cscanButton);
        algorithmPanel.add(lookButton);
        algorithmPanel.add(clookButton);
        algorithmPanel.add(allButton);
        
        outlineLabel = createLabel(740, 40, white, "Computation Outline", 16f);
        outlineLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        
        outlinePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        outlinePanel.setPreferredSize(new Dimension(700, 240));
        outlinePanel.setBackground(darkpink);
        
        algorithmLabel = createLabel(330, 35, white,  "Algorithm:", 16f);
        algorithmField = createField(330, 35, darkpink, 16f, false);
        
        cylinderLabel = createLabel(330, 35, white,  "Cylinders in the Queue:", 16f);
        cylinderField = createField(330, 35, darkpink, 16f, true);
        
        lengthLabel = createLabel(330, 35, white,  "Length of Queue:", 16f);
        lengthField = createField(330, 35, darkpink, 16f, true);
        lengthField.setEditable(false);
        lengthField.setText("0");
        
        headLabel = createLabel(330, 35, white,  "Head Location:", 16f);
        headField = createField(330, 35, darkpink, 16f, true);
        
        directionLabel = createLabel(330, 35, white,  "Direction of the movement:", 16f);
        directionBox = createComboBox(330, 35, darkpink, 16f, this);
        
        outlinePanel.add(algorithmLabel);
        outlinePanel.add(algorithmField);
        outlinePanel.add(lengthLabel);
        outlinePanel.add(lengthField);
        outlinePanel.add(cylinderLabel);
        outlinePanel.add(cylinderField);
        outlinePanel.add(headLabel);
        outlinePanel.add(headField);
        outlinePanel.add(directionLabel);
        outlinePanel.add(directionBox);
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setPreferredSize(new Dimension(900, 60));
        buttonPanel.setOpaque(false);
        
        generateButton = createButton(190, 40, "Start", darkpink, white, white,16, this);
        resetButton = createButton(190, 40, "Reset", red, white,white, 16, this);
        randomButton = createButton(190, 40, "Random Input", darkpink, white, white,16, this);
        textButton = createButton(190, 40, "Text File Input", darkpink, white, white,16, this);
        
        buttonPanel.add(randomButton);
        buttonPanel.add(textButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(resetButton);
        
        selectionPanel.add(selectLabel);
        selectionPanel.add(algorithmPanel);
        selectionPanel.add(outlineLabel);
        selectionPanel.add(outlinePanel);
        selectionPanel.add(buttonPanel);
        
        errorLabel = createLabel(1000, 20, white, "", 20);
        errorLabel.setHorizontalAlignment(center);
        
        add(titlePanel);
        add(selectionPanel);
        add(errorLabel);
        
        addNumberCountingListener();
    }
    
    private void addNumberCountingListener() {
        cylinderField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                countNumbers(cylinderField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                countNumbers(cylinderField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                countNumbers(cylinderField);
            }
            
        });
    }
    
    private void countNumbers(JTextField textField) {
        String text = textField.getText().trim();
        String[] numbers = text.split(" ");
        
        int count = 0;
        hasInvalid = false;
        
        for (String number : numbers) {
            if (number.matches("\\d+")) {
                int value = Integer.parseInt(number);
                if (value >= 0 && value <= 199) {
                    count++;
                } else {
                    hasInvalid = true;
                }
            } else if(!number.isEmpty()) {
                hasInvalid = true;
            }
        }
        
        lengthField.setText(String.valueOf(count));
        
        if (hasInvalid) {
            errorLabel.setText("Cylinder values must be between 0 and 199");
        } else {
            errorLabel.setText("");
        }
    }

    public void clearInputs(){
        algorithmField.setText("");
        cylinderField.setText("");
        lengthField.setText("0");
        headField.setText("");
        errorLabel.setText("");
        
        // clear inputs in Simulator class
        simulator.clearAlgorithm();
        simulator.clearHeadLocation();
        simulator.clearLength();
        simulator.clearCylinderQueue();
    }
    
    public List<Integer> selectUniqueRandomValues(int n) { 
        int start = 0;
        int end = 199;
        
        for (int i = start; i < end; i++) {
            randomCylinders.add(i);
        }

        Collections.shuffle(randomCylinders);
        return randomCylinders.subList(0, n);
    }
    
    public String convertListToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer number : list) {
            sb.append(number).append(" ");
        }
        return sb.toString().trim(); // Removes the trailing space
    }
    
    private void generateRandomInput(){
        Random r = new Random();
        int randomLength = r.nextInt(10, 40);
        int random = r.nextInt((randomLength));
        
        lengthField.setText(String.valueOf(randomLength));
        String cylinderQueue = convertListToString(selectUniqueRandomValues(randomLength));
        
        cylinderField.setText(cylinderQueue);
        headField.setText(String.valueOf(randomCylinders.get(random)));
    }
    
    private File uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
            return selectedFile;
        } else {
            System.out.println("File selection cancelled.");
            return null;
        }
    }
    
     private void readFile(File file) {
        if (file == null) {
            System.out.println("No file to read.");
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                cylinderDetails.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }  
    }
     
    private void generateTextInput(){
        File selectedFile = uploadFile();
        if (selectedFile != null) {
            readFile(selectedFile);
            
            if (!cylinderDetails.isEmpty()) {
                lengthField.setText(cylinderDetails.get(0));
                cylinderField.setText(cylinderDetails.get(1));
                headField.setText(cylinderDetails.get(2));
            } else {
                System.out.println("No data found in file");
            }
        }
    }
    
    public boolean isNumeric(String string) {
            try {
                Double.valueOf(string);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
    }
    
    public boolean validateInput(){
        String algorithm = algorithmField.getText();
        String cylinders = cylinderField.getText();
        String length = lengthField.getText();
        String head = headField.getText();
        
        // check if head field contains exactly one valid number
        boolean validHead = head.matches("\\d{1,3}") && Integer.parseInt(head) >= 0 && Integer.parseInt(head) <= 199;
        
        if (validHead && !hasInvalid && ((!algorithm.isEmpty() || !algorithm.isBlank())  && (!cylinders.isEmpty() || !cylinders.isBlank())
                && (!length.isEmpty() || !length.isBlank()) && (!head.isEmpty() || !head.isBlank()) 
                && isNumeric(length))
                && (Integer.parseInt(length) <= 40 && Integer.parseInt(length) >= 10) 
                && (Integer.parseInt(length) <= 40 && Integer.parseInt(length) >= 10) ) {
            
            return true;
        }
        else{
            return false;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==fcfsButton){
            errorLabel.setText("");
            algorithmField.setText(fcfsButton.getText());
        }
        else if(e.getSource()==sstfButton){
            errorLabel.setText("");
            algorithmField.setText(sstfButton.getText());
        }
        else if(e.getSource()==scanButton){
            errorLabel.setText("");
            algorithmField.setText(scanButton.getText());
        }
        else if(e.getSource()==cscanButton){
            errorLabel.setText("");
            algorithmField.setText(cscanButton.getText());
        }
        else if(e.getSource()==lookButton){
            errorLabel.setText("");
            algorithmField.setText(lookButton.getText()); 
        }
        else if(e.getSource()==clookButton){
            errorLabel.setText("");
            algorithmField.setText(clookButton.getText());
        }
        else if(e.getSource()==allButton){
            errorLabel.setText("");
            algorithmField.setText(allButton.getText());
        }
        else if(e.getSource()==randomButton){
            errorLabel.setText("");
            generateRandomInput();
        }
        else if(e.getSource()==textButton){
            errorLabel.setText("");
            generateTextInput();
        }
        else if(e.getSource()==generateButton && !validateInput()){
            errorLabel.setText("Invalid. Please check your inputs.");
        }
        else if(e.getSource()==resetButton || e.getSource()==backButton){
            clearInputs();
        }
    }
}
