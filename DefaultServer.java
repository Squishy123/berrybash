import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
/**
 * Write a description of class DefaultServer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DefaultServer extends World
{

    /**
     * Constructor for objects of class DefaultServer.
     * 
     */
    public DefaultServer()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 

        Greenfoot.setWorld(new ServerWorld("defaultServer", 25565));
    }
}
