import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Starting screen for game
 * 
 * @author Christian Wang
 * @version (a version number or a date)
 */
public class Menu extends World
{

    /**
     * Constructor for objects of class Menu.
     * 
     */
    public Menu()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 600, 1); 

    }
    public void act() {
        String hosting = Greenfoot.ask("DO YOU WANT TO HOST A GAME? Y/N");
        switch(hosting.toUpperCase()) {
            case "Y" : Greenfoot.setWorld(new ServerWorld("defaultServer", 25565));
            break;
            case "N" : Greenfoot.setWorld(new ClientWorld("defaultClient", Greenfoot.ask("ENTER IP"), 25565));
            break;
        }
    }
}
