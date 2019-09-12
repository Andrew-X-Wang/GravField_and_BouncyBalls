import java.util.ArrayList;

/**
 * Massive Objects!
 * 
 * @author Andrew Wang
 * @version not as many as some of the other ones
 */
public class MassObject
{
    int diameter;
    int baseDiam = 50;
    double xVel = 0;
    double yVel = 0;
    double xAccel;
    double yAccel;
    int xPos;
    int yPos;

    int screenWidth;
    int screenHeight;

    int pressedTime;    //time when wave was created
    int heldTime;   
    int timePassed;  //time elapsed since wave created

    Integer[] colorArray;
    int color;
    
    double density = 1;
    double mass;
    double fGrav;
    
    boolean collided;
    int collideTime;
    
    
    /**
     * Constructor for objects of class MassObject
     *         - initializes variables, sets color
     */
    public MassObject(int x, int y, int xDim, int yDim, int ht, double vx, double vy)
    {
        //pressedTime = pt;
        heldTime = ht;
        diameter = baseDiam + 50 * heldTime;
        mass = density * Math.pow((diameter / 2), 2); //area of circle is pi*r^2, but pi is a constant

        xPos = x - diameter / 2;        //this one's - instead of + so that the mouse is the center of the created Mass
        yPos = y - diameter / 2;
        
        xVel = vx;
        yVel = vy;

        screenWidth = xDim;
        screenHeight = yDim;
        
        color = (int)(Math.random() * 3);   //determines which color (red green blue) is the dom color of the wave
        colorArray = new Integer[3];
        for(int counter = 0; counter < colorArray.length; counter ++)
        {
            if(counter != color - 1)
            {
                colorArray[counter] = (int)(Math.random() * 50);
            }
            else
            {
                colorArray[counter] = 255 - (int)(Math.random() * 15);
            }
        }
    }

    /**
     * Update! does pretty much the same thing as the Wave update method, except I commented out most things b/c didn't want the grav simulator to be impeded
     *        - also tries to keep in check the unending collisions, although slightly unsuccessfully b/c things still collide really fast and don't "unstick" fast enough
     * 
     * @param      x       x position of the mouse
     * @param      y       y position of the mouse
     * @param  updateTime  time of the Update
     * @return   none
     */
    public void update(int x, int y, int updateTime)        //if you remove all of the Masses the last remaining Mass still has accel
    {
        xVel += xAccel;
        yVel += yAccel;
        
        /**
        //This part checks the mouse position and does the mouse impact
        double distance = Math.pow(Math.pow((xPos - diameter * 0.5) - x, 2) + Math.pow((yPos - diameter * 0.5) - y, 2), 0.5);
        if(distance <= diameter / 2)       //even when this if statement is commented out it still changes direction when I move the mouse...
        {
            int xDistance = (xPos - diameter / 2) - x;
            int yDistance = (yPos - diameter / 2) - y;
            if(xDistance > 0)       //the closer the ball is to the center the greater the added velocity
            {
                xVel += ((xPos - diameter / 2) - xDistance) / 5; // divide by 5 is just some way of dampening the affect of the hit, adjust for impact strength
            }
            else if(xDistance < 0)
            {
                xVel +=  (xDistance - (xPos - diameter / 2)) / 5;
            }
            if(yDistance > 0)
            {
                yVel += ((xPos - diameter / 2) - xDistance) / 5;
            }
            else if(yDistance < 0)
            {
                yVel += (xDistance -(xPos - diameter / 2)) / 5;
            }
        }*/
        //--------
        
        /**
        //Wall impacts
        
        //Wave newWave = null;
        if(yPos < 0)        //the different ones are to 'make sure' that the waves are being created on center on the very edge of the window where they supposedly collide
        {
            yVel = Math.abs(0.95 * yVel);  //Math.abs means that even if it doesn't exit that bound right away it won't switch from + vel(down) to - and then back to +
            //newWave = new Wave(xPos + diameter / 2, 0, diameter, Math.pow((Math.pow(xVel, 2) + Math.pow(yVel, 2)), 0.5), updateTime, color, colorArray);
        }
        if(yPos + diameter > screenHeight)
        {
            yVel = -1 * Math.abs(0.95 * yVel);
            //newWave = new Wave(xPos + diameter / 2, screenHeight, diameter, Math.pow((Math.pow(xVel, 2) + Math.pow(yVel, 2)), 0.5), updateTime, color, colorArray);
        }
        if(xPos + diameter > screenWidth)
        {
            xVel = -1 * Math.abs(0.95 * xVel);
            //newWave = new Wave(screenWidth, yPos + diameter / 2, diameter, Math.pow((Math.pow(xVel, 2) + Math.pow(yVel, 2)), 0.5), updateTime, color, colorArray);
        }
        if(xPos < 0)
        {
            xVel = Math.abs(0.95 * xVel);
            //newWave = new Wave(0, yPos + diameter / 2, diameter, Math.pow((Math.pow(xVel, 2) + Math.pow(yVel, 2)), 0.5), updateTime, color, colorArray);
        }*/
        
        /*
        //Goes to 0 because otherise * (-0.05) will never reach 0
        if(Math.abs(xVel) < 0.1)
        {
            xVel = 0;
        }
        if(Math.abs(yVel) < 0.1)
        {
            xVel = 0;
        }
        */

        xPos += (int)xVel;
        yPos += (int)yVel;
        
        
        if(updateTime > collideTime)  //gets rid of the Mass's "invincibility" status following a collision
        {
            collided = false;
        }
    }
    
    /**
     * collision: does a collision, tries to have the "invincibility" thing after one collision to avoid Masses going into each other and going crazy
     * 
     * @param      m      the Mass it may or may not be colliding with
     * @param      c      weather or not the Mass is in its invincibility stage
     * @param    time     current time
     * @return  collided  whether or not it is not in the invincibility stage following a collision
     */
    public boolean collision(MassObject m, boolean c1, int time) 
    {
        collided = c1;
        
        double distanceX = m.xPos + m.diameter / 2 - (this.xPos + this.diameter / 2);
        double distanceY = m.yPos + m.diameter / 2 - (this.yPos + this.diameter / 2);
        double distance = Math.pow(Math.pow(distanceX, 2) + Math.pow(distanceY, 2), 0.5);
        
        if(!collided)   //"invincibility" status following collision
        {
            if(m.diameter / 2 + this.diameter / 2 >= distance)  //laws of conservation of momentum for elastic collisions
            {
                this.xVel = (this.mass - m.mass) / (this.mass + m.mass) * this.xVel + 2 * m.mass / (this.mass + m.mass) * m.xVel;
                this.yVel = -1 * (this.mass - m.mass) / (this.mass + m.mass) * this.yVel + 2 * m.mass / (this.mass + m.mass) * m.yVel;
                
                m.xVel = (m.mass - this.mass) / (m.mass + this.mass) * m.xVel + 2 * this.mass / (m.mass + this.mass) * this.xVel;
                m.yVel = - 1 * (m.mass - this.mass) / (m.mass + this.mass) * m.yVel + 2 * this.mass / (m.mass + this.mass) * this.yVel;
                            
                collided = true;
                collideTime = time;
                    
                    
                    
            }
        }
        return collided; 
    }
}