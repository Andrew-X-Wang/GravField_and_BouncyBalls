import java.awt.*; 
import javax.swing.*; 
import java.awt.event.*; 
import java.util.ArrayList;
 
/**
 * 0_O prettyyyy
 * 
 * @author Andrew Wang
 * @version ok yeah this is the yuuuuuuge number
 */
public class Background extends JPanel implements MouseMotionListener, ActionListener, MouseListener
{
    // instance variables - replace the example below with your own
    int dimensionX = 1400;
    int dimensionY = 700;
    int rectSize = 15;
    public Rectangle[][] grid = new Rectangle[dimensionX / rectSize][dimensionY / rectSize];
    
    int size;
    Timer clock;
    int x;
    int y;
    
    JPanel drawing = new JPanel();
    JPanel buttons = new JPanel();
    
    int fluxTimer = 0;
    int reverse;
    
    int clockTimer;
    int pressedTime;
    ArrayList<Wave> waveList = new ArrayList<Wave>();
    
    ArrayList<BouncyBall> ballList = new ArrayList<BouncyBall>();

    /**
     * Constructor for objects of class Background
     *      -sets size of screen, initializes the rectangles that make up the grid, adds the listeners and starts the clock
     */
    public Background() 
    {
        setPreferredSize(new Dimension(dimensionX, dimensionY));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        //add(drawing, BorderLayout.CENTER);
        for(int rows = 0; rows < grid.length; rows ++)
        {
            for(int cols = 0; cols < grid[rows].length; cols ++)
            {
                grid[rows][cols] = new Rectangle(rectSize, rectSize, rectSize * rows, rectSize * cols); //centers of rectangles are rectSize away from each other
            }
        }
        
        addMouseMotionListener(this);
        addMouseListener(this);
        
        clock = new Timer(50, this);
        
        clock.start();
    }
    
    /**
     * PaintComponent: handles a lot of stuff, including drawing and updating the grid, changing colors, bouncyBall movement, Wave deletion for lag clearing, etc
     * 
     * @param  g   Graphics
     * @return none
     */
    public void paintComponent (Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(0));
        
        for(int rows = 0; rows < grid.length; rows ++)
        {
            for(int cols = 0; cols < grid[rows].length; cols ++)
            {
                int recSize = 5; //set to 20 for cool fullscreen color changing //minimum rectangle size I think?
                Rectangle thisRect = grid[rows][cols];
                double distance = Math.pow(Math.pow((thisRect.x + thisRect.width * 0.5) - x, 2) + Math.pow((thisRect.y + thisRect.height * 0.5) - y, 2), 0.5);
                
                /**set colors here so I can choose to change the color of the wave later without impacting the rest of the thing*/
                //Color currentColor = new Color(255, (int)(distance / 1600 * 255), fluxTimer * 10); //red near, yellow far, pink timer
                //Color currentColor = new Color((int)(distance / 1600 * 255), 255, fluxTimer * 10);   //green near, yellow far, lime? or light aqua green????? timer
                //Color currentColor = new Color(255, fluxTimer * 10, (int)(distance / 1600 * 255));   //red near, purple/pink far, light orange timer
                //Color currentColor = new Color((int)(distance / 1600 * 255), fluxTimer * 10, 255);   //blue near, pink far, light sky blue timer
                //Color currentColor = new Color(fluxTimer * 10, (int)(distance / 1600 * 255), 255);   //blue near, torquoise far, purple/pink timer
                //Color currentColor = new Color(fluxTimer * 10, 255, (int)(distance / 1600 * 255));   //light green near, lime/aqua green far, green timer
                //Color currentColor = Color.black;
                
                //g.setColor(currentColor);   //WHY ARE ALL THE COLOR INDICES OUT OF BOUNDS????? BECAUSE UR DISTANCE FORMULA WAS WRONG
                
                //thisRect.height = fluxTimer / 2 + (int)(Math.ceil(recSize / Math.pow((distance / 1000), 0.5)));        //power function to the 0.5
                //thisRect.width = thisRect.height;
                //thisRect.height = fluxTimer / 2 + (int)(Math.ceil(recSize + (1000 - distance) / 100));                 //linear
                //thisRect.width = thisRect.height;
                thisRect.height = fluxTimer / 2 + (int)(Math.ceil(recSize + Math.pow((1000 - distance) / 100, 1.2)));    //power function to the 1.2 
                thisRect.width = thisRect.height;                                                                       
                
                int red = (int)(distance / 1600 * 255);
                int green = 255;
                int blue = fluxTimer * 10;
                
                boolean onWave = false;
                for(int counter = 0; counter < waveList.size(); counter ++)
                {
                    Wave thisWave = waveList.get(counter);
                    thisRect = thisWave.waveAdjust(thisRect, clockTimer);
                    if(clockTimer - thisWave.pressedTime > 600)    //if wave has been in existence for longer than 30 seconds
                    {
                        waveList.remove(counter);
                        counter --; //adjust for removing an index
                    }
                    if(thisWave.timePassed * thisWave.waveSpeed > dimensionX + 50) //if the distance the wave has traveled is greater than the width of the screen + 50
                    {
                        waveList.remove(counter);
                        counter --;
                    }
                    
                    if(thisWave.onWave(thisRect, clockTimer))   //changing colors if it's on the wave
                    {
                        red += thisWave.colorArray[0];
                        if(red > 255)
                        {
                            red -= 255;
                        }
                        green += thisWave.colorArray[1];
                        if(green > 255)
                        {
                            green -= 255;
                        }
                        blue += thisWave.colorArray[2];
                        if(blue > 255)
                        {
                            blue -= 255;
                        }
                    }
                }
                g.setColor(new Color(red, green, blue));
                
                thisRect.x = (int)(rows * rectSize - thisRect.width * 0.5);
                thisRect.y = (int)(cols * rectSize - thisRect.height * 0.5);
                
                if((rows + cols) % 2 == 0)
                {
                    g.drawRect(thisRect.x, thisRect.y, thisRect.width, thisRect.height);
                    //g.drawOval(thisRect.x, thisRect.y, thisRect.width, thisRect.height);
                }
                else
                {
                    g.fillRect(thisRect.x, thisRect.y, thisRect.width, thisRect.height);
                    //g.fillOval(thisRect.x, thisRect.y, thisRect.width, thisRect.height);
                }
            }
        }
        
        //setting the colors of the Balls, adds waves whenever needed for Ball interaction with walls
        for(int counter = 0; counter < ballList.size(); counter ++)
        {
            BouncyBall thisBall = ballList.get(counter);
            
            Wave newWave = thisBall.update(x, y, clockTimer);
            if(newWave != null)
            {
                waveList.add(newWave);
            }
            
            int red = thisBall.colorArray[0];
            if(red > 255)
            {
                red -= 255;
            }
            int green = thisBall.colorArray[1];
            if(green > 255)
            {
                green -= 255;
            }
            int blue = thisBall.colorArray[2];
            if(blue > 255)
            {
                blue -= 255;
            }
            g.setColor(new Color(red, green, blue));
            
            g.fillOval(thisBall.xPos, thisBall.yPos, thisBall.diameter, thisBall.diameter);
        }
    }
   
    /**
     * mouseMoved: updates the x and y position identified in the class for things like where to create waves, when to have the BouncyBalls interact with the mouse, etc
     * 
     * @param  e   MouseEvent
     * @return none
     */
    public void mouseMoved(MouseEvent e)
    {
        x = e.getX();
        y = e.getY();
        //repaint();
    }
    
    /**
     * mouseDragged: It's just mouseMoved
     * 
     * @param  e   MouseEvent
     * @return none
     */
    public void mouseDragged(MouseEvent e)
    {
        x = e.getX();
        y = e.getY();
        //repaint();
    }
    
    /**
     * mousePressed: sets pressedTime as the current clockTimer, which is used by the Wave to determine where it is/ draw it
     *                  -also contributes towards how large the created Wave and BouncyBalls are
     * 
     * @param  e   MouseEvent
     * @return none
     */
    public void mousePressed(MouseEvent e)
    {
        pressedTime = clockTimer;    //for the purpose of finding how long the mouse was held for
            //it's okay for pressedTime to be defined in BackGround class because it doesn't matter anymore as soon as 
            //heldTime is defined when the mouse is released... it's not possible for two waves to be created such that 
            //the pressedTime will be overwritten before the first wave is finished being created
    }
    
    /**
     * mouseReleased: calculates the heldTime which is used to calculate how large the created Waves and bouncyBalls are
     *                     -also officially creates the Waves and bouncyBalls as a result of the mouse clicking
     * 
     * @param  e   MouseEvent
     * @return none
     */
    public void mouseReleased(MouseEvent e)
    {
        int heldTime = clockTimer - pressedTime;
        waveList.add(new Wave(x, y, clockTimer, heldTime));   //clockTimer (current time) is time the wave was created
        
        ballList.add(new BouncyBall(x, y, dimensionX, dimensionY, clockTimer, heldTime));
        
        repaint();
    }
    
    public void mouseEntered(MouseEvent e)
    {
    
    }
    
    public void mouseExited(MouseEvent e)
    {
    
    }
    
    public void mouseClicked(MouseEvent e)
    {
    
    }
    
    /**
     * actionPerformed: updates 20 times a second on the firing of the clock. Updates the screen and gets the fluxTimer going which makes things look fancy :3 oscillating colors :D
     *                          -also helps clear lag
     * 
     * @param  e   MouseEvent
     * @return none
     */
    public void actionPerformed(ActionEvent e)  //20 times a second
    {
        if(e.getSource() == clock)
        {
            if(fluxTimer > 20)
            {
                reverse = -1;
            }
            if(fluxTimer < 1)
            {
                reverse = 1;
            }
            fluxTimer += reverse;
            
            
            clockTimer ++;
            
            
            if(waveList.size() > 45)
            {
                for(int counter = 0; counter < waveList.size() - 30; counter ++)
                {
                    waveList.remove(0); //lag clear
                    counter --;
                }
            }
            
            repaint();
        }
    }
}
