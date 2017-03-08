import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Healthbar
 * 
 * @author Christian Wang 
 * @version 1.0
 */
public class HealthGUI extends GUI
{   
    int scale;
    int maxHealth;
    public HealthGUI(int health) {
        width = 500;
        height = 100;
        scale = (int) ((415 / health));
        maxHealth = health;
        GreenfootImage menu = new GreenfootImage(width, height);
        menu.setColor(new Color(145,176,255));
        menu.fillRect(0,0,width,height);
        //menu.setTransparency(220);

        //menu.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        //menu.drawString("Health : " + String.valueOf(health), 50, 60);
        menu.setColor(new Color(255,255,255));
        menu.fillRect(60, 60, width - 80, 20);
        menu.setFont(new Font("Segoe UI", 30));
        menu.drawString("HP", 10, 80);
        menu.setColor(new Color(172,255,170));
        //menu.fillRect(65, 65, width - 90, 10);
        menu.fillRect(65, 65, scale * health, 10);
        setImage(menu);
    }

    public void updateGUI(int health) {
        GreenfootImage menu = new GreenfootImage(width, height);
        menu.setColor(new Color(145,176,255));
        menu.fillRect(0,0,width,height);
        //menu.setTransparency(220);

        //menu.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        //menu.drawString("Health : " + String.valueOf(health), 50, 60);
        menu.setColor(new Color(255,255,255));
        menu.fillRect(60, 60, width - 80, 20);
        menu.setFont(new Font("Segoe UI", 30));
        menu.drawString("HP", 10, 80);

        if(health/maxHealth < 0.7) {
            menu.setColor(new Color(244,215,66));
            if (health/maxHealth < 0.1) {
                menu.setColor(new Color(244,66,66));
            }
        }else {
            menu.setColor(new Color(172,255,170));
        }
        //menu.fillRect(65, 65, width - 90, 10);
        //menu.fillRect(65, 65, ((int) (scale * health)) - 90, 10);
        menu.fillRect(65, 65, scale * health, 10);
        setImage(menu);
    }
}