import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GameOver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameOver extends World
{

    /**
     * Constructor for objects of class GameOver.
     * 
     */
    public GameOver()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 600, 1); 
    }
    
    public void act() {
        switch(Greenfoot.ask("Start a new game? Y/N")) {
            case "Y": Greenfoot.setWorld(new Menu());
            break;
            case "N": this.stopped();
            break;
        }
    }
}
