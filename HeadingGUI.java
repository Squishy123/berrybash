import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Headings 
 * 
 * @author Christian Wang 
 * @version 1.0
 */
public class HeadingGUI extends Actor
{
    String text;
    GreenfootImage menu;
    int width;
    int height;
    public HeadingGUI(String text) {
        this.text = text;
        this.width = 100;
        this.height = 100;
        menu = new GreenfootImage(width, height);
        menu.setColor(new Color(255,255,255));
        menu.setFont(new Font("Segoe UI", 30));
        menu.drawString(text, 0, 80);
        
        setImage(menu);
    }
}
