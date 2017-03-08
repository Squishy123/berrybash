import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
import java.util.*;
/**
 * Super class for game objects
 * Has a bunch of helper methods for gameobjects
 * 
 * @author Christian Wang
 * @version 1.0
 */
public abstract class GameObject extends Actor implements Serializable
{
    int initCount = 0;

    public Timer renderTime;
    public Timer updateTime;

    public int animationsPerSecond = 60;
    public int updatesPerSecond = 60;

    public String ID;

    public States currentState = States.IDLE;

    /**
     * Create a new gameobject
     */
    public GameObject(String ID) {
        this.ID = ID;
        renderTime = new Timer();
        updateTime = new Timer();
    }

    /**
     * First called when actor is added to the world
     * ONLY CALLED ONCE
     */
    public void init() {

    }

    /**
     * Called every tick
     */
    public void update() {

    }

    /**
     * Called every so many ticks - based on counter
     */
    public void fixedUpdate() {

    }

    /**
     * Used for rendering object
     */
    public void render() {

    }

    /**
     * Uses alpha of images to detect collision
     */
    public final Actor getOneVisualizingObject(Class clss){
        ArrayList<Actor>cactor = new ArrayList<Actor>(super.getIntersectingObjects(clss));
        for(Actor c : cactor){
            for(int y = getY()-getImage().getHeight()/2; y < getY()+getImage().getHeight()/2; y++){
                for(int x = getX()-getImage().getWidth()/2; x < getX()+getImage().getWidth()/2; x++){
                    for(int cy = c.getY()-c.getImage().getHeight()/2; cy < c.getY()+c.getImage().getHeight()/2; cy++){
                        for(int cx = c.getX()-c.getImage().getWidth()/2; cx < c.getX()+c.getImage().getWidth()/2; cx++){
                            if(x==cx && y==cy){
                                GreenfootImage a = new GreenfootImage(getImage()), 
                                b = new GreenfootImage(c.getImage());
                                a.rotate(getRotation());
                                b.rotate(c.getRotation());
                                int
                                ix = x-(getX()-getImage().getWidth()/2),
                                iy = y-(getY()-getImage().getHeight()/2),
                                icx = cx-(c.getX()-c.getImage().getWidth()/2),
                                icy = cy-(c.getY()-c.getImage().getHeight()/2);
                                if(a.getColorAt(ix,iy).getAlpha()>50 &&
                                b.getColorAt(icx, icy).getAlpha()>50)
                                    return c;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Checks for one touching object and returns it
     */
    public Actor getOneTouchedObject(Class clss)
    {
        List<Actor> list = getIntersectingObjects(clss);
        List<Actor> list2 = getIntersectingObjects(clss);
        list2.clear();
        GreenfootImage i=new GreenfootImage(getImage());
        i.rotate(getRotation());
        int w=i.getWidth(),h=i.getHeight(),x=getX(),y=getY();
        for(Actor A : list)
        {
            GreenfootImage Ai = new GreenfootImage(A.getImage()), i2 = new GreenfootImage(w,h);
            Ai.rotate(A.getRotation());
            int Aw=Ai.getWidth(),Ah=Ai.getHeight(),Ax=A.getX(),Ay=A.getY();
            i2.drawImage(Ai,Ax-x-(Aw/2-w/2),Ay-y-(Ah/2-h/2));
            for(int yi = 0; yi<h; yi++)
                for(int xi = 0; xi<w; xi++)
                    if(i2.getColorAt(xi,yi).getAlpha()>0 && i.getColorAt(xi,yi).getAlpha()>0)
                        return A;
        }
        return null;
    }

    /**
     * Set the location using exact coordinates.
     */
    public void setLocation(int x, int y, Class cls, Class exception) 
    {   
        int tempX = getX();
        int tempY = getY();

        int differenceX = tempX - x;
        int differenceY = tempY - y;
        super.setLocation(x, y);
        // System.out.println(differenceX + " " + differenceY);
        Actor temp = getOneTouchedObject(cls);
        if(temp != null) {
            if(temp.getClass() != exception) {
                //Logs the collision with object and the coordinates
                super.setLocation(tempX + differenceX, tempY+ differenceY);
            }
        }
    }

    /**
     * Act - do whatever the GameObject wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(initCount < 1) init();
        initCount++;

        if(updateTime.millisElapsed() > this.updatesPerSecond) {
            update();
            updateTime.mark();
        }

        if(renderTime.millisElapsed() > this.animationsPerSecond) {
            render();
            renderTime.mark();
        }
    }    
}
