import greenfoot.*;
import java.io.*;

/**
 * Write a description of class test here.
 * @author (your name) @version (a version number or a date)
 */
public class test extends Actor implements Serializable
{
    /* (World, Actor, GreenfootImage, Greenfoot and MouseInfo)*/
    protected int cool = 42;

    /**
     * 
     */
    public test()
    {
        cool = 30;
    }

    /**
     * Act - do whatever the test wants to do. This method is called whenever the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        move(1);
        turn(1);
    }
}
