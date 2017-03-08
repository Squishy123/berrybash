import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
/**
 * Parser object holds an actor and coordinate for sending
 * 
 * @author Christian Wang
 * @version 1.0
 */
public class Parser implements Serializable
{
    public Actor object;
    public int x;
    public int y;
    
    /**
     * Constructor for objects of class Parser
     */
    public Parser(Actor object, int x, int y)
    {
        this.object = object;
        this.x = x;
        this.y = y;
    }

}
