import java.awt.*; 
import javax.swing.*; 
import java.awt.event.*; 

/**
 * DO THE WAVE
 * 
 * @author Andrew Wang
 * @version a yUUuUuUugeee number actually prob not that big
 */
public class Wave
{
    // instance variables - replace the example below with your own
    int xpos;   //xpos of wave epicenter
    int ypos;   //ypos of wave epicenter
    int pressedTime;    //time when wave was created
    int heldTime;   
    Integer[] colorArray;
    int color;
    
    
    int waveSpeed = 10;
        int waveLength = 100;
        int timePassed;  //time elapsed since wave created
        

    /**
     * Constructor for objects of class Wave
     *      - initializes variables, sets color
     */
    public Wave(int x, int y, int pt, int ht)   //ht given in how many 1/20ths of a second have passed
    {
        xpos = x;
        ypos = y;
        pressedTime = pt;
        if(ht < 20)
        {
            heldTime = 1;
        }
        else
        {
            heldTime = ht / 3;  //now heldTime is given in how many fourths of a second have passed
        }
        
        color = (int)(Math.random() * 3);   //determines which color (red green blue) is the main color of the wave
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
     * Constructor for objects of class Wave, used by the BouncyBall class to create Waves as a result of wall-Ball collisions
     *         - initializes variables, sets color
     */
    public Wave(int x, int y, int ballSize, double ballVel, int pt, int c, Integer[] ca)
    {
        xpos = x;
        ypos = y;
        pressedTime = pt;
        heldTime = (int)((ballSize + ballVel) / 200); //determines amplitude
        waveLength = (int)(ballSize + ballVel) / 5;
        waveSpeed = waveLength / 4;
        
        colorArray = new Integer[3];
        for(int counter = 0; counter < ca.length; counter ++)
        {
            if(counter != c - 1)
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
     * waveAdjust: this is pretty much responsible for creating the Wave. The Background/GravField loops through the Rectangles in the grid and this resizes them accordingly to make the Waves
     * 
     * @param      thisRect     - each Rectangle that makes up the grid
     * @param      clockTimer   - the current time, so the Wave knows where it should be
     * @return     the updated Rectangle back to the Background/GravField class
     */
    public Rectangle waveAdjust(Rectangle thisRect, int clockTimer) //clockTimer is current time
    {
        double distance = Math.pow(Math.pow((thisRect.x - thisRect.width * 0.5) - xpos, 2) + Math.pow((thisRect.y - thisRect.height * 0.5) - ypos, 2), 0.5);    //distance of Rectangle from wave epicenter
        
        int waveAmplitude = 10 * heldTime;
        timePassed = clockTimer - pressedTime;  //time elapsed since wave created
        
        if(distance > timePassed * waveSpeed && distance < timePassed * waveSpeed + waveLength)
        {
            int numLocOnWave = (int)(waveLength - (waveLength / 2 - (distance - timePassed * waveSpeed))); //d - tP * wS is the location on wave, (wL/2 - that) is how far it is from the center of the wave
            int fracLocOnWave = numLocOnWave / waveLength;  //gives how far it is from the center of wave (not epicenter) by fraction 
            thisRect.height += Math.abs((int)(fracLocOnWave * waveAmplitude));
            thisRect.width += Math.abs((int)(fracLocOnWave * waveAmplitude));
            //x and y dealt with in paintComponent
        }
        return thisRect;
    }
    
    /**
     * onWave: this is for the purpose of changing the color of Rectangles on the Wave so they're actually visible
     * @param      thisRect     - each Rectangle that makes up the grid
     * @param      clockTimer   - the current time, so the Wave knows which Rectangles are a part of it
     * @return     boolean; whether or not a certain Rectangle is on a Wave
     */
    public boolean onWave(Rectangle thisRect, int clockTimer)
    {
        double distance = Math.pow(Math.pow((thisRect.x - thisRect.width * 0.5) - xpos, 2) + Math.pow((thisRect.y - thisRect.height * 0.5) - ypos, 2), 0.5); 
        if(distance > timePassed * waveSpeed && distance < timePassed * waveSpeed + waveLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
