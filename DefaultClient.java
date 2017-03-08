import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
/**
 * Write a description of class DefaultClient here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DefaultClient extends World
{

    /**
     * Constructor for objects of class DefaultClient.
     * 
     */
    public DefaultClient()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        Greenfoot.setWorld(new ClientWorld("defaultClient", "127.0.0.1", 25565));
   
    }
}
