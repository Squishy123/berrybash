import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class gfi here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileEditRoom extends World
{
    /**
     * Constructor for objects of class gfi.
     * 
     */
    public TileEditRoom()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 400, 1);
        
        TileStorage.init();
        TileStorage.loadTiles();
        Tiler tileset = new Tiler("PathAndObjects.png",32,32);
        addObject(tileset,0,0);
        TilePlacer ty = new TilePlacer("PathAndObjects.png",32,32,tileset);
        addObject(ty,0,0);

        setPaintOrder(TilePlacer.class,Tiler.class);
        System.gc();
    }
}
