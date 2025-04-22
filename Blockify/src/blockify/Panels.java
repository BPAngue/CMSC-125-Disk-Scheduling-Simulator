package blockify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Panels extends JPanel{
    public Font font;
    public final Font archivoblack = getFont("archivoblack"); 
    public final Font archivonarrow = getFont("archivonarrow"); 
    
    public BufferedImage img;
    public final BufferedImage bg = getImg("/img/bg.jpg");;
    public final BufferedImage logo = getImg("/img/logo.png");
    public final BufferedImage smallLogo = getImg("/img/logo_small.png");
    
    public final Icon logoIcon = new ImageIcon(logo);
    public final Icon smallLogoIcon = new ImageIcon(smallLogo);
    
    public final Color gray = new Color(118, 130, 138);
    public final Color transparent = new Color(0,0,0,0);
    public final Color white = Color.WHITE;
    public final Color black = Color.BLACK;
    public final Color green = new Color(24,121,76);
    public final Color darkgreen = new Color(35, 102, 46);
    public final Color red = new Color(188,24,35);
    public final Color darkpink = new Color(0xc20071);
    public final Color pink = new Color(0xff039e);
    
    public final String[] directions = {"Left", "Right"};
    
    
    public final int center = SwingConstants.CENTER;
    
    public Panels(){
        showComponents();
    }
    
    public void showComponents(){
        
    }
    
    public JButton createButton(String text, ActionListener l) {
        JButton button = new JButton(text);
        button.setBackground(new Color(118,130,138));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(320, 60));
        button.setFont(archivoblack.deriveFont(Font.BOLD, 30));
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.addActionListener(l);
        return button;
    }
    
    public JButton createButton(int width, int height, String text, Color background, Color foreground, int size, ActionListener l) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(archivoblack.deriveFont(Font.BOLD, size));
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.addActionListener(l);
        return button;
    }
    
    public JButton createButton(int width, int height, String text, Color background, Color foreground, Color border, float size, ActionListener l) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(archivoblack.deriveFont(size));
        button.setFocusable(false);
        button.setBorder(BorderFactory.createLineBorder(foreground, 2));
        button.addActionListener(l);
        return button;
    }
    
    public JButton createButton(int width, int height, String text, Color background, Color foreground, Color border, float size, int borderwidth, ActionListener l) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(archivoblack.deriveFont(size));
        button.setFocusable(false);
        button.setBorder(BorderFactory.createLineBorder(foreground, borderwidth));
        button.addActionListener(l);
        return button;
    }
    
    public Font getFont(String filepath){
        try {
            InputStream i = getClass().getResourceAsStream("/font/" + filepath + ".ttf");
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, i);
            } catch (FontFormatException ex) {
                System.out.println("no font");
            }
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        return font;
    }
    
    
    public BufferedImage getImg(String filepath){
        try{
            img = ImageIO.read(getClass().getResourceAsStream(filepath));
        }catch(Exception e){
            //e.printStackTrace();
        }
        return img;
    }
    
    public JLabel createLabel(int width, int height, Color foreground, String text, float size){
        JLabel label = new JLabel();
        label.setText(text);
        label.setForeground(foreground);
        label.setFont(archivoblack.deriveFont(size));
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }
    
    public JLabel createLabel(int width, int height, Color foreground, String text, Font font, float size){
        JLabel label = new JLabel();
        label.setText(text);
        label.setForeground(foreground);
        label.setFont(font.deriveFont(size));
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }
    
    public JTextField createField(int width, int height, Color foreground, float size, boolean editable){
        JTextField textfield = new JTextField();
        textfield.setPreferredSize(new Dimension(width, height));
        textfield.setForeground(foreground);
        textfield.setFont(archivoblack.deriveFont(size));
        textfield.setBackground(white);
        textfield.setEditable(editable);
        
        return textfield;
    }
    
    public JComboBox createComboBox(int width, int height, Color foreground, float size, ActionListener l){
        JComboBox comboBox = new JComboBox(directions);
        comboBox.setFocusable(false);
        comboBox.setFont(archivoblack.deriveFont(size));
        comboBox.setPreferredSize(new Dimension(width, height));
        comboBox.setBackground(white);
        comboBox.setForeground(foreground);
        comboBox.setBorder(null);
        comboBox.addActionListener(l);
        
        return comboBox;
    }
            
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, 1550, 800, null); 
    }
}
