import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


import java.util.*;
/**
 * Displays the current level
 * 
 * @author Christian Wang
 * @version 1.0
 */
public class LevelGUI extends GUI
{
    public LevelGUI(int level) {
        width = 150;
        height = 30;
        GreenfootImage menu = new GreenfootImage(width, height);
        menu.setColor(new Color(145,176,255));
        menu.fillRect(0,0,width,height);
        //menu.setTransparency(220);

        menu.setColor(new Color(255,255,255));
        menu.setFont(new Font("Segoe UI", 30));
        menu.drawString("lv. " + String.valueOf(level), 20, 25);
        setImage(menu);
    }

    public void updateGUI(int level) {
        GreenfootImage menu = new GreenfootImage(width, height);
        menu.setColor(new Color(145,176,255));
        menu.fillRect(0,0,width,height);
        //menu.setTransparency(220);

        menu.setColor(new Color(255,255,255));
        menu.setFont(new Font("Segoe UI", 30));
        menu.drawString("lv. " + String.valueOf(level), 20, 25);
        setImage(menu);
    }
}
