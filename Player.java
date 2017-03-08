import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
import java.util.*;

/**
 * Snorlax, the pokemon
 * 
 * @author Christian Wang 
 * @version 1.0
 */
public class Player extends GameObject implements Serializable
{   
    private transient SpriteSheet walkFront;
    private transient SpriteSheet walkBack;
    private transient SpriteSheet walkRight;
    private transient SpriteSheet walkLeft;

    States lastState = States.UP;

    States stateX = States.IDLE;
    States lastStateX = States.UP;

    States stateY = States.IDLE;
    States lastStateY = States.UP;

    public String controllerID;

    public int health = 100;

    public int level = 1;

    private int berryC = 0;

    public transient Timer hungry;

    private transient CollectionBar bar;

    transient double velocityY = 0;
    transient double velocityX = 0;
    transient double accelerationY = 0;
    transient double accelerationX = 0;

    transient final double MAX_VELOCITY = 10;

    public Player() {
        super("");
        //this.ID = ((Unique) getWorld()).ID;
        create();
    }

    public Player(String ID) {
        super(ID);
        create();

    }

    public void create() {
        //ANIMATION STUFF
        hungry = new Timer();
        walkFront = new SpriteSheet("images/animals/snorlax/walk_front.png", 27, 26, 2, 3);
        walkBack = new SpriteSheet("images/animals/snorlax/walk_back.png", 27, 26, 2, 3);
        walkRight = new SpriteSheet("images/animals/snorlax/walk_right.png", 21, 27, 2, 3);
        walkLeft = new SpriteSheet("images/animals/snorlax/walk_left.png", 21, 27, 2, 3);
        setImage(walkBack.spriteSheet.get(0));

    }

    public void init() {

        //Checks if there are 2 duplicates with same ID in world and removes one
        ArrayList<Player> players = new ArrayList<Player>(getWorld().getObjects(Player.class));
        HashMap<String, Integer> dup = new HashMap<String, Integer>();

        for(int x = 0; x < players.size(); x++) {
            dup.put(players.get(x).ID, 0);
        }

        for(int i = 0; i < players.size(); i++) {
            dup.put(players.get(i).ID, dup.get(ID)+1);
        }

        if(dup.containsValue(2)) {
            String key = "";
            for(String k : dup.keySet()) {
                if(dup.get(k) == 2) {
                    key = k;
                    break;
                }
            }

            for(int i = 0; i < players.size(); i++) {
                if(players.get(i).ID.equals(key)) {
                    getWorld().removeObject(players.get(i));
                    break;
                }
            }
        }

        //GUI BAR
        if(ID.equals(((Unique) getWorld()).ID)) {
            bar = new CollectionBar(health, level);
            getWorld().addObject(bar, 250, 50);
        }
    }

    /**
     * Hunger ticks and eating fruits
     */
    public void hunger() {
        if(berryC >= 5) {
            level++;
            berryC = 0;
            if(bar != null) {
                bar.updateLevel(level);
            }
        }
        if(hungry.millisElapsed() >= 1000) {
            health-=5;
            hungry.mark();
            if(bar != null) {
                bar.updateHealth(health);
            }
        }

        if(isTouching(Berry.class)) {
            berryC++;
            if(health < 100) {
                health += 5;
                if(bar != null) {
                    bar.updateHealth(health);
                }
            }
            if(getWorld() instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) getWorld();
                world.removeObject(getOneTouchedObject(Berry.class));

            } else if (getWorld() instanceof ClientWorld) {
                ClientWorld world = (ClientWorld) getWorld();
                world.removeObject(getOneTouchedObject(Berry.class));
            }
        }

        if(health <= 0) {
            if(getWorld() instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) getWorld();
                world.removeObject(this);
                Greenfoot.setWorld(new GameOver());

            } else if (getWorld() instanceof ClientWorld) {
                ClientWorld world = (ClientWorld) getWorld();
                world.removeObject(this);
                Greenfoot.setWorld(new GameOver());
            }

        }
    }

    /**
     * Slows down the player automatically if there is
     * no forward motion
     */
    public void friction() {
        switch (currentState) {
            case IDLE:
            switch(lastState) {
                case LEFT:
                if(velocityX > 0) {velocityX-=1; synchrLoc();}
                if(velocityX < 0) {velocityX+=1; synchrLoc();}
                break;
                case RIGHT:
                if(velocityX > 0) {velocityX-=1; synchrLoc();}
                if(velocityX < 0) {velocityX+=1; synchrLoc();}
                break;
            }
        }
        switch (currentState) {
            case IDLE:
            switch(lastState) {
                case UP:
                if(velocityY < 0){velocityY+=1; synchrLoc();}

                break;
                case DOWN: 
                if(velocityY > 0){velocityY-=1; synchrLoc();}
                break;
            }
        }
    }

    /**
     * Movement
     */
    public void control() {
        if(ID.equals(((Unique) getWorld()).ID)) {
            if(Greenfoot.isKeyDown("w")) {
                currentState = States.UP;
                lastState = States.UP;
                stateX = States.UP;
                if(velocityY > -MAX_VELOCITY) velocityY-=2;
                synchrLoc();
            } else if(Greenfoot.isKeyDown("s")) {
                currentState = States.DOWN;
                lastState = States.DOWN;
                stateX = States.DOWN;

                if(velocityY < MAX_VELOCITY) velocityY+=2;
                synchrLoc();
            } else {
                currentState = States.IDLE;
                stateX = States.IDLE;
            }

            if (Greenfoot.isKeyDown("a")) {
                currentState = States.LEFT;
                lastState = States.LEFT;
                stateY = States.LEFT;
                if(velocityX > -MAX_VELOCITY) velocityX-=2;
                synchrLoc();
            } else if (Greenfoot.isKeyDown("d")) {
                currentState = States.RIGHT;
                lastState = States.RIGHT;
                stateY = States.RIGHT;
                setLocation(getX() + 5, getY());
                if(velocityX < MAX_VELOCITY)velocityX+=2;
                synchrLoc();
            } else {
                if(currentState == States.IDLE) {
                    currentState = States.IDLE;
                    stateY = States.IDLE;
                }
            }
            friction();
            setLocation((int) (getX()+velocityX+0.5), (int) (getY() + velocityY + 0.5), GameObject.class, Berry.class);

        }
    }

    /**
     * Synchronizes the location with other clients
     */
    public void synchrLoc() {
        if(getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) getWorld();
            world.moveObject(this, getX(), getY(),currentState);

        } else if (getWorld() instanceof ClientWorld) {
            ClientWorld world = (ClientWorld) getWorld();
            world.moveObject(this, getX(), getY(), currentState);
        }

    }

    /**
     * Controls the animation based on state
     */
    public void controller() {
        switch(currentState) {
            case UP:
            //if(renderTime.millisElapsed() >= animationsPerSecond) {
            setImage(walkFront.animate());
            //}
            break;
            case DOWN:  
            //if(renderTime.millisElapsed() >= animationsPerSecond) {
            setImage(walkBack.animate());
            //}
            break;
            case LEFT:  
            //if(renderTime.millisElapsed() >= animationsPerSecond) {
            setImage(walkLeft.animate());
            //}
            break;
            case RIGHT:  
            //if(renderTime.millisElapsed() >= animationsPerSecond) {
            setImage(walkRight.animate());
            //}
            break;
        }
    }

    /**
     * Overrides super update()
     */
    public void update() {
        //System.out.println(currentState);
        hunger();
        control();
    }

    /**
     * Overrides super render()
     */
    public void render() {
        controller();
    }
}
