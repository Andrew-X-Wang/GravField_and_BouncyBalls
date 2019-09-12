import java.awt.*;
import javax.swing.*;

/**
 * Drives the BackGround. A slightly more experienced driver than GravFieldDriver.
 * 
 * @author Andrew Wang
 * @version 0.5
 */
public class BackgroundDriver extends JFrame
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Try this");
        Background b = new Background();
        frame.add(b);
        frame.pack(); // prepares frame for display
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

