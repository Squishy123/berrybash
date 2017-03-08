import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A GUI bar for the player containing a levelgui and healthgui
 * 
 * @author Christian Wang
 * @version 1.0
 */
public class CollectionBar extends GUI
{
    HealthGUI healthBar;
    LevelGUI xpBar;

    private GreenfootImage hp;      
    private GreenfootImage xp;
    
    public CollectionBar(int health, int level) {
        healthBar = new HealthGUI(health);
        xpBar = new LevelGUI(level);

        hp = new GreenfootImage(healthBar.getImage());
        xp = new GreenfootImage(xpBar.getImage());

        GreenfootImage temp = new GreenfootImage(hp.getWidth(), hp.getHeight());
        temp.drawImage(hp, 0, 0);
        temp.drawImage(xp, hp.getWidth()-xp.getWidth(), 20);
        //temp.drawImage(title.getImage(), hp.getWidth() - 300, -37);
        temp.setTransparency(220);

        setImage(temp);


    }

    public void updateHealth(int health) {
        healthBar.updateGUI(health);
        hp = new GreenfootImage(healthBar.getImage());
        xp = new GreenfootImage(xpBar.getImage());

        GreenfootImage temp = new GreenfootImage(hp.getWidth(), hp.getHeight());
        temp.drawImage(hp, 0, 0);
        temp.drawImage(xp, hp.getWidth()-xp.getWidth(), 20);
        //temp.drawImage(title.getImage(), 215, 13);
        temp.setTransparency(220);

        setImage(temp);
    }

    public void updateLevel(int level) {
        xpBar.updateGUI(level);
        hp = new GreenfootImage(healthBar.getImage());
        xp = new GreenfootImage(xpBar.getImage());

        GreenfootImage temp = new GreenfootImage(hp.getWidth(), hp.getHeight());
        temp.drawImage(hp, 0, 0);
        temp.drawImage(xp, hp.getWidth()-xp.getWidth(),20);
        temp.setTransparency(220);

        setImage(temp);
    }

}
