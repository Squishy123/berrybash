import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Parent class for GUI objects
 * 
 * @author Christian Wang
 * @version 1.0
 */
public abstract class GUI extends Actor
{   
    int width;
    int height;
    /**
     * Render loop called once every loop 
     * Used for graphical changes
     */
    public void render()
    {
        
    }	
  
    /**
     * Calls render and redraws the GUI every tick
     */
    public void act() 
    {
        render();
    }    
}