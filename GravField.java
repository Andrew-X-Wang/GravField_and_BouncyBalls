import java.awt.*; 
import javax.swing.*; 
import java.awt.event.*; 
import java.util.ArrayList;

import java.awt.Stroke;

/**
 * A slightly drunk gravitational field "emulator" (if this passes anything close to the definition of an emulator)
 * 
 * @author Andrew Wang
 * @version: too many probably... just keep adding stuff, finding huge mistakes, changing, rinse and repeat
 */
public class GravField extends JPanel implements MouseMotionListener, ActionListener, MouseListener
{
    int dimensionX = 1400;
    int dimensionY = 700;
    int rectSize = 15;
    public Rectangle[][] grid = new Rectangle[dimensionX / rectSize][dimensionY / rectSize];
    
    int size;
    Timer clock;
    int x;
    int y;
    
    int fluxTimer = 0;
    int reverse;
    
    int clockTimer;
    int pressedTime;
    
    ArrayList<MassObject> massList = new ArrayList<MassObject>();
    
    int initPosX;
    int initPosY;
    
    int red;
    int green;
    int blue;
    
    ArrayList<Wave> waveList = new ArrayList<Wave>();

    /**
     * Constructor for objects of class GravField
     */
    public GravField()
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
        
        clock = new Timer(20, this);
        
        clock.start();
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(0));
        
        for(int rows = 0; rows < grid.length; rows ++)
        {
            for(int cols = 0; cols < grid[rows].length; cols ++)
            {
                int recSize = 3; //set to 20 for cool fullscreen color changing //minimum rectangle size I think?
                Rectangle thisRect = grid[rows][cols];
                double distance = Math.pow(Math.pow((thisRect.x + thisRect.width * 0.5) - x, 2) + Math.pow((thisRect.y + thisRect.height * 0.5) - y, 2), 0.5);
                
                /**Set/Reset the original position here: rectangles farther away are smaller, closer are larger*/ //the divide by 10 is to dampen the effect of the fluxTimer, otherwise too fast
                //thisRect.height = fluxTimer / 10 + (int)(Math.ceil(recSize / Math.pow((distance / 1000), 0.5)));           //power function to the 0.5
                //thisRect.width = thisRect.height;
                thisRect.height = fluxTimer / 10 + (int)(Math.ceil(recSize + (1565 - distance) / 100));                      //linear
                thisRect.width = thisRect.height;
                //thisRect.height = fluxTimer / 10 + (int)(Math.ceil(recSize + Math.pow((1565 - distance) / 100, 1.2)));     //power function to the 1.2 
                //thisRect.width = thisRect.height;                                                                       
                
                thisRect.x = (int)(rows * rectSize - thisRect.width * 0.5);
                thisRect.y = (int)(cols * rectSize - thisRect.height * 0.5);
                
                /**set colors here so I can choose to change the color of the wave/ whatever else later without impacting the rest of the thing*/
                //Color currentColor = new Color(255, (int)(distance / 862 * 255), fluxTimer * 10); //red near, yellow far, pink timer
                //Color currentColor = new Color((int)(distance / 862 * 255), 255, fluxTimer * 10);   //green near, yellow far, lime? or light aqua green????? timer
                //Color currentColor = new Color(255, fluxTimer * 10, (int)(distance / 862 * 255));   //red near, purple/pink far, light orange timer
                //Color currentColor = new Color((int)(distance / 862 * 255), fluxTimer * 10, 255);   //blue near, pink far, light sky blue timer
                //Color currentColor = new Color(fluxTimer * 10, (int)(distance / 862 * 255), 255);   //blue near, torquoise far, purple/pink timer
                //Color currentColor = new Color(fluxTimer * 10, 255, (int)(distance / 862 * 255));   //light green near, lime/aqua green far, green timer
                //Color currentColor = Color.black;
                
                double other = (int)distance;
                if(distance > 1565)     //1565 is the maximum distance that can be achieved within the grid? so I guess problems arise when it leaves the grid...
                {
                    other = 1565;
                }
                
                red = (int)(other / 1565 * 255);
                green = 255;
                blue = fluxTimer * 5;     //5 is an arbitrary value to get the blue to cycle from 0 to 250
                
                g.setColor(new Color(red, green, blue));
                
                
                //This part warps the grid (maybe a little too much) around Massive objects
                for(int counter = 0; counter < massList.size(); counter ++) 
                {
                    MassObject thisMass = massList.get(counter);
                    double massXDist = Math.abs(thisMass.xPos + thisMass.diameter / 2 - (thisRect.x + thisRect.height / 2));
                    double massYDist = Math.abs(thisMass.yPos + thisMass.diameter / 2 - (thisRect.y + thisRect.height / 2));
                    double massDist = Math.pow(Math.pow(massXDist, 2) + Math.pow(massYDist, 2), 0.5);   //always pos value
                    
                    double fGrav = 30 * thisMass.mass / (Math.pow(massDist, 2));
                    if(thisMass.xPos + thisMass.diameter / 2 > thisRect.x)      //Ok this is probably as close as I'm gonna get to a gravitational field thingy grid, so lets just say the random lines are solar flares or something
                    {
                        thisRect.x +=  fGrav * massXDist / massDist;        
                            //thisRect.x += -1 * massDist / fGrav / massXDist; /**only leaving this here cause it looks cool*/
                    }
                    else
                    {
                        thisRect.x += -1 * fGrav * massXDist / massDist;
                            //thisRect.x += massDist / fGrav / massXDist;
                    }
                    if(thisMass.yPos + thisMass.diameter / 2 > thisRect.y)
                    {
                        thisRect.y += fGrav * massYDist / massDist;
                            //thisRect.y += -1 * massDist / massYDist / fGrav;
                    }
                    else
                    {
                        thisRect.y += -1 * fGrav * massYDist / massDist;
                            //thisRect.y += massDist / massYDist / fGrav;
                    }
                    
                }
                
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
                
                //Drawing lines between rectangles to form grid
                Rectangle rightRect;
                Rectangle downRect;
                if(cols + 1 < grid[rows].length)
                {
                    rightRect = grid[rows][cols + 1];
                }
                else{rightRect = thisRect;}
                if(rows + 1 < grid.length)
                {
                    downRect = grid[rows + 1][cols];
                }
                else{downRect = thisRect;}
                
                //making the lines of the grid skinnier so it looks like a grid not a solid screen
                Graphics2D g3 = (Graphics2D)g;
                Stroke dfltStroke = g3.getStroke();
                g3.setStroke(new BasicStroke(1));
                g.drawLine(thisRect.x + thisRect.height / 2, thisRect.y + thisRect.height / 2, rightRect.x + rightRect.height / 2, rightRect.y + rightRect.height / 2);
                g.drawLine(thisRect.x + thisRect.height / 2, thisRect.y + thisRect.height / 2, downRect.x + downRect.height / 2, downRect.y + downRect.height / 2);
                g2.setStroke(dfltStroke);
                
                //Updates the Waves
                for(int counter = 0; counter < waveList.size(); counter ++)
                {
                    waveList.get(counter).waveAdjust(thisRect, clockTimer);
                }
            }
        }
        
        
        //Gravitational effect of every single other mass added
        for(int counter = 0; counter < massList.size(); counter ++)
        {
            massList.get(counter).xAccel = 0;
            massList.get(counter).yAccel = 0;
        }
        for(int counter = 0; counter < massList.size(); counter ++) /**Doesn't seem right... drawn more to cluster of small Masses than the huge Mass*/
        {
            MassObject mass1 = massList.get(counter);
            for(int counter2 = counter + 1; counter2 < massList.size(); counter2 ++)
            {
                MassObject mass2 = massList.get(counter2);
                double xDist = Math.abs((mass1.xPos + mass1.diameter / 2) - (mass2.xPos + mass2.diameter / 2));
                    double yDist = Math.abs((mass1.yPos + mass1.diameter / 2) - (mass2.yPos + mass2.diameter / 2));
                    double dist = Math.pow(Math.pow(xDist, 2) + Math.pow(yDist, 2), 0.5);
                
                double fGrav = mass1.mass * mass2.mass / (Math.pow(dist, 2));
                double fGravX = fGrav * xDist / dist;
                double fGravY = fGrav * yDist / dist;
                
                
                if(mass1.xPos + mass1.diameter > mass2.xPos + mass2.diameter) //@TODO AHHH IT DOESNT BOUNCE AT THE RIGHT ANGLES
                {                           //sign of acceleration depends on where it is in relation to the other mass
                    mass2.xAccel += fGravX / mass2.mass;
                    mass1.xAccel += -1 * fGravX / mass1.mass;
                }
                else
                {
                    mass2.xAccel += -1 * fGravX / mass2.mass;
                    mass1.xAccel += fGravX / mass1.mass;
                }
                if(mass1.yPos + mass1.diameter  > mass2.yPos + mass2.diameter)
                {
                    mass2.yAccel += fGravY / mass2.mass;
                    mass1.yAccel += -1 * fGravY / mass1.mass;
                }else
                {
                    mass2.yAccel += -1 * fGravY / mass2.mass;
                    mass1.yAccel += fGravY / mass1.mass;
                }
                
                
                if(mass1.collision(mass2, mass1.collided, clockTimer))
                {
                    
                    mass2.collideTime = clockTimer; //because the last one won't be set? but no? doesn't work as well without this line IDK why yet
                    
                    double momentum1 = mass1.mass * Math.pow(Math.pow(mass1.xVel, 2) + Math.pow(mass1.yVel, 2), 0.5);
                    double momentum2 = mass2.mass * Math.pow(Math.pow(mass2.xVel, 2) + Math.pow(mass2.yVel, 2), 0.5);
                    
                    
                    
                    int xContact = 0;
                    int yContact = 0;
                    
                    if(mass1.xPos + mass1.diameter / 2 > mass2.xPos + mass2.diameter / 2)       //sets the point of contact (where the wave will originate from
                    {
                        xContact = (int)(mass1.xPos + mass1.diameter / 2 - (xDist - mass1.diameter * 0.5 / dist));  //the center of the wave
                    }
                    else
                    {
                        xContact = (int)(mass1.xPos + mass1.diameter / 2 + (xDist - mass1.diameter * 0.5 / dist));
                    }
                    if(mass1.yPos + mass1.diameter / 2 > mass2.yPos + mass2.diameter / 2)
                    {
                        yContact = (int)(mass1.yPos + mass1.diameter / 2 - (yDist - mass1.diameter * 0.5 / dist));
                    }   
                    else
                    {
                        yContact = (int)(mass1.yPos + mass1.diameter / 2 + (yDist - mass1.diameter * 0.5 / dist));
                    }
                    
                    Wave newWave = new Wave(xContact, yContact, clockTimer, (int)(momentum1 + momentum2));              //THESE ARE THE GRAVITATIONAL WAVES YOU HEARD ABOUT ON THE NEWS LOL NO
                    waveList.add(newWave);
                    
                    g.setColor(Color.BLACK);
                    g.drawString("COLLISION", dimensionX - 300, 30);        //tells you when your collisions are working (and when they're not)
                    g.setColor(new Color(red, green, blue));
                }
                
            }
            
            mass1.update(x, y, clockTimer); //don't need to update mass2; each mass updates when it's finished adding all of the accels
            
            g.setColor(Color.BLACK);
            
            g.fillOval(mass1.xPos, mass1.yPos, mass1.diameter, mass1.diameter);             //draws the Mass Object
            g.drawString(mass1.xAccel + ", " + mass1.yAccel, mass1.xPos, mass1.yPos);       //displays the Accels of the Object in x and y directions
            g.drawString(mass1.xPos + ", " + mass1.yPos, 10, 20 + 30 * counter);            //displays the positions of the Masses in top left corner going down
            g.setColor(new Color(red, green, blue));
        }
    }
    
    public void mouseMoved(MouseEvent e)
    {
        x = e.getX();
        y = e.getY();
        //repaint();
    }
    
    public void mouseDragged(MouseEvent e)
    {
        x = e.getX();
        y = e.getY();
        //repaint();
    }
    
    public void mousePressed(MouseEvent e)
    {
        pressedTime = clockTimer;    //for the purpose of finding how long the mouse was held for
            //it's okay for pressedTime to be defined in GravField class because it doesn't matter anymore as soon as 
            //heldTime is defined when the mouse is released... it's not possible for two Masses to be created such that 
            //the pressedTime will be overwritten before the first Mass is finished being created
        
        initPosX = e.getX();
        initPosY = e.getY();
    }
    
    public void mouseReleased(MouseEvent e)
    {
        int heldTime = clockTimer - pressedTime;
        
        double initVelX = .07 * (e.getX() - initPosX);                                  //0.7 is an arbitrary value to make the initial velocities have a reasonable magnitude
        double initVelY = .07 * (e.getY() - initPosY);
        double initVel = Math.pow(Math.pow(initVelX, 2) + Math.pow(initVelY, 2), 0.5);
        
        int sizeMod = 5 * (int)(initVel);
        if(5 * initVel > heldTime)
        {
            sizeMod = heldTime;
        }
        
        massList.add(new MassObject(x, y, dimensionX, dimensionY, heldTime - sizeMod, initVelX, initVelY));     //having the sizeMod makes it so that you're not creating a huge Mass by holding and dragging
                                                                                                                //to give an initial velocity to the ball. You can still choose to have a massive object
        repaint();                                                                                              //with a lower initial velocity because the velocity's only dependent on the distance dragged
    }
    
    public void mouseEntered(MouseEvent e)
    {
    
    }
    
    public void mouseExited(MouseEvent e)
    {
    
    }
    
    public void mouseClicked(MouseEvent e)
    {
        if(SwingUtilities.isRightMouseButton(e))            //if I right click the Mass object is deleted
        {
            for(int counter = 0; counter < massList.size(); counter ++)
            {
                MassObject thisMass = massList.get(counter);
                double mouseDist = Math.pow(Math.pow((thisMass.xPos + thisMass.diameter * 0.5) - x, 2) + Math.pow((thisMass.yPos + thisMass.diameter * 0.5) - y, 2), 0.5);
                if(mouseDist <= thisMass.diameter / 2)
                {
                    massList.remove(counter);
                    counter --;
                }
            }
        }
    }
    
    public void actionPerformed(ActionEvent e)  //20 times a second
    {
        if(e.getSource() == clock)
        {
            if(fluxTimer > 50)      //reverses every 2.5 seconds
            {
                reverse = -1;
            }
            if(fluxTimer < 1)
            {
                reverse = 1;
            }
            fluxTimer += reverse;         //this thing makes the grid fluctuate on a timer in both color and size of the rectangles
                                          //a bit more jerky than I'd like it to be though...
            clockTimer ++;
            
            if(massList.size() > 45)
            {
                for(int counter = 0; counter < massList.size() - 30; counter ++)
                {
                    massList.remove(0); //lag clear, not really necessary unless you're actually going crazy with the clicking
                    counter --;
                }
            }
            
            if(waveList.size() > 5)
            {
                waveList.remove(0); //lag clear: this one's a bit more necessary... the Waves require more calculations than the Masses I think
            }
            
            repaint();
        }
    }
    
}






