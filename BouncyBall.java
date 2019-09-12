import java.util.ArrayList;

/**
 * Balls that Bounce
 * 
 * @author Andrew Wang
 * @version a fair amount
 */
public class BouncyBall
{
    int diameter;
    int baseDiam = 50;
    double xVel;
    double yVel;
    double xAccel = 0;
    double yAccel = 5;
    int xPos;
    int yPos;

    int screenWidth;
    int screenHeight;

    int pressedTime;    //time when wave was created
    int heldTime;   
    int timePassed;  //time elapsed since wave created

    Integer[] colorArray;
    int color;

    /**
     * Constructor for objects of class BouncyBall
     *         - initializes variables with arbitrary constants, sets color
     */
    public BouncyBall(int x, int y, int xDim, int yDim, int pt, int ht)
    {
        pressedTime = pt;
        heldTime = ht;
        diameter = baseDiam + 50 * heldTime;

        xPos = x - diameter / 2;
        yPos = y - diameter / 2;

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
     * Update! updating the bouncyBall whenever it's called, just deals with changes in acceleration and velocity and position based on mouse impacts, wall impacts, etc
     * 
     * @param      x        x position of the mouse
     * @param      y        y position of the mouse
     * @param  updateTime   current Time of update
     * @return Wave: if a Wave is created as a result of a wall collision, a Wave is created and returned
     */
    public Wave update(int x, int y, int updateTime)    //public Wave update
    {   yVel += yAccel;

        //This part checks the mouse position and does the mouse impact
        double distance = Math.pow(Math.pow((xPos + diameter * 0.5) - x, 2) + Math.pow((yPos + diameter * 0.5) - y, 2), 0.5); /**@TODOOOOOOOOOO*/
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
        }

        //Wall impacts
        Wave newWave = null;
        if(yPos < 0)        //the different ones are to 'make sure' that the waves are being created on center on the very edge of the window where they supposedly collide
        {
            yVel = Math.abs(0.95 * yVel);  //Math.abs means that even if it doesn't exit that bound right away it won't switch from + vel(down) to - and then back to +
            newWave = new Wave(xPos + diameter / 2, 0, diameter, Math.pow((Math.pow(xVel, 2) + Math.pow(yVel, 2)), 0.5), updateTime, color, colorArray);
        }
        if(yPos + diameter > screenHeight)
        {
            yVel = -1 * Math.abs(0.95 * yVel);
            newWave = new Wave(xPos + diameter / 2, screenHeight, diameter, Math.pow((Math.pow(xVel, 2) + Math.pow(yVel, 2)), 0.5), updateTime, color, colorArray);
        }
        if(xPos + diameter > screenWidth)
        {
            xVel = -1 * Math.abs(0.95 * xVel);
            newWave = new Wave(screenWidth, yPos + diameter / 2, diameter, Math.pow((Math.pow(xVel, 2) + Math.pow(yVel, 2)), 0.5), updateTime, color, colorArray);
        }
        if(xPos < 0)
        {
            xVel = Math.abs(0.95 * xVel);
            newWave = new Wave(0, yPos + diameter / 2, diameter, Math.pow((Math.pow(xVel, 2) + Math.pow(yVel, 2)), 0.5), updateTime, color, colorArray);
        }

        xPos += (int)xVel;
        yPos += (int)yVel;
        
        return newWave;
    }
}
