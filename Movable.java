/**
 * Movable (abstract) - represents properties of movable objects (players and enemies)
 * @author Rupin Mittal
 * @version May 24, 2019
 */

public abstract class Movable
{
    //variables
    private double xPos;    //the horizontal position in pixels
    private double yPos;    //the vertical position in pixels
    private double xVel;    //the horizontal velocity
    private double yVel;    //the vertical velocity

    //methods
    /**
     * Abstract method to move object
     * Will be implemented uniquely in subclasses
     */
    public abstract void move();    //method to move the object, implemented in subclasses

    /**
     * Abstract method to update animation of moving object
     * Will be implemented uniquely in subclasses
     */
    public abstract void updateAnimation();     //method to update animation of moving objects, implemented in subclasses

    /**
     * Method to set the horizontal position of object
     * @param xP double horizontal position in pixels
     */
    public void setXPos(double xP)
    {
        xPos = xP;   //set the horizontal position
    }

    /**
     * Method to set the vertical position of object
     * @param yP double vertical position in pixels
     */
    public void setyPos(double yP)
    {
        yPos = yP;   //set the vertical position
    }

    /**
     * Method to set the horizontal velocity of object
     * @param xV double horizontal velocity
     */
    public void setXVel(double xV)
    {
        xVel = xV;   //set the horizontal velocity
    }

    /**
     * Method to set the vertical velocity of object
     * @param yV double vertical velocity
     */
    public void setYVel(double yV)
    {
        yVel = yV;   //set the vertical velocity
    }

    /**
     * Method to get the horizontal position of object
     * @return xPos double horizontal position in pixels
     */
    public double getXPos()
    {
        return xPos;   //get the horizontal position
    }

    /**
     * Method to get the vertical position of object
     * @return yPos double vertical position in pixels
     */
    public double getYPos()
    {
        return yPos;   //get the horizontal position
    }

    /**
     * Method to get the horizontal velocity of object
     * @return xVel double horizontal velocity
     */
    public double getXVel()
    {
        return xVel;   //get the horizontal velocity
    }

    /**
     * Method to get the vertical velocity of object
     * @return yVel double vertical velocity
     */
    public double getYVel()
    {
        return yVel;   //get the vertical velocity
    }
}