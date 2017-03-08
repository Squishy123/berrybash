import java.awt.image.*;
import java.util.*;
import greenfoot.*;
/**
 * Spritesheet loader
 * 
 * @author Christian 
 * @version 1.0
 */
public class SpriteSheet 
{
    int spriteWidth;
    int spriteHeight;
    int numberOfSprites;
    int scaleFactor;

    //Sprite Counter for animation
    int spriteCounter = 0;

    //State associated with the spritesheet
    States currentObjState;
    GreenfootImage spriteSheetImage;

    ArrayList<GreenfootImage> spriteSheet = new ArrayList<GreenfootImage>();

    boolean showHitBoxes = false;

    /**
     * Constructor for objects of class SpriteSheet
     */
    public SpriteSheet(String fileName, int spriteWidth, int spriteHeight, int numberOfSprites, int scaleFactor)
    {
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numberOfSprites = numberOfSprites;
        this.scaleFactor = scaleFactor;

        spriteSheetImage = new GreenfootImage(fileName);
        create();        
    }

    /**
     * Constructor for objects of class SpriteSheet
     */
    public SpriteSheet(String fileName, int spriteWidth, int spriteHeight, int numberOfSprites, int scaleFactor, boolean showHitBoxes)
    {
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.numberOfSprites = numberOfSprites;
        this.scaleFactor = scaleFactor;
        this.showHitBoxes = showHitBoxes;

        spriteSheetImage = new GreenfootImage(fileName);
        create();        
    }

    private GreenfootImage crop(int startX, int startY) {
        GreenfootImage cropped = new GreenfootImage(spriteWidth, spriteHeight);
        cropped.drawImage(spriteSheetImage, startX, startY);

        cropped.scale(spriteWidth * scaleFactor, spriteHeight * scaleFactor);
        if(showHitBoxes) {
            cropped.drawRect(0,0,cropped.getWidth()-1, cropped.getHeight()-1);

        }return cropped;
    }

    public GreenfootImage get(int spriteNum) {
        return spriteSheet.get(spriteNum);
    }

    /**
     * Switches to the next sprite in the spritesheet
     */
    public GreenfootImage animate() {
        if(spriteCounter >= numberOfSprites) spriteCounter = 0;
        GreenfootImage temp = new GreenfootImage(spriteSheet.get(spriteCounter));
        spriteCounter++;
        return temp;
    }

    private void create() {
        for(int i = 0; i < numberOfSprites; i++) {
            spriteSheet.add(crop(i*spriteWidth*-1, 0));
        }
    }

}