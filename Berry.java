import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Berry here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Berry extends GameObject
{
    Timer life;
    private static int berryCount = 0;
    
    public Berry() {
        super("berry : "+String.valueOf(berryCount));
        berryCount++;
        GreenfootImage berry = new GreenfootImage("images/food/berry.png");
        berry.scale(30,30);
        setImage(berry);

        life = new Timer();
    }

    /**
     * Act - do whatever the Berry wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(life.millisElapsed() >= 5000) {
            GreenfootImage berry = new GreenfootImage("images/food/berry.png");
            berry.scale(30,30);
            berry.setTransparency(200);
            setImage(berry);
        }

        if(life.millisElapsed() >= 10000) {
            if(getWorld() instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) getWorld();
                world.removeObject(this);

            } else if (getWorld() instanceof ClientWorld) {
                ClientWorld world = (ClientWorld) getWorld();
                world.removeObject(this);
            }

        }
    }    
}
