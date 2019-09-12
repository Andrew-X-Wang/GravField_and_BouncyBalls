import java.awt.*;
import javax.swing.*;

/**
 * Drives a Gravity Field. Could use some more lessons from Christo's though
 * 
 * @author Andrew Wang
 * @version 0.0
 */
public class GravFieldDriver extends JFrame
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Gravity!");
        GravField gf = new GravField();
        frame.add(gf);
        frame.pack(); // prepares frame for display
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

