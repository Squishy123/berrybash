import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;

/**
 * Write a description of class TilePlacer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TilePlacer extends Actor
{
    boolean init = false;
    private GreenfootImage myTile;
    private int tileSizeX,tileSizeY;
    private int posX,posY;
    private boolean clicked = false;
    private boolean selecting = false;
    private int ioTimer = 0;
    private int setTimer = 0;
    private Tiler tiler;
    private String currentSet;
    private DefaultListModel<String> layerData;
    public TileWindow window;

    public TilePlacer(String startingSet, int tileSizeX, int tileSizeY, Tiler tiler){
        currentSet = startingSet;
        this.tileSizeX = tileSizeX;
        this.tileSizeY = tileSizeY;
        this.tiler = tiler;
        myTile = new GreenfootImage(tileSizeX,tileSizeY);
        layerData = new DefaultListModel<String>();
        layerData.addElement("Actors");
        layerData.addElement("DefaultLayer");
        layerData.addElement("Solids");
    }

    public void updateSet(String name){
        currentSet = name;
        updateTile(0,0);
    }
    
    public void renameLayer(String data, String name){
        for(int i = 0; i < layerData.size(); i++){
            if(layerData.get(i).equals(data)){
                layerData.set(i,name);
            }
        }
        tiler.renameLayer(data,name);
    }
    
    public void setLayer(DefaultListModel<String> data, String name){
        layerData = data;
        tiler.setLayer(data,name);
    }

    public void updateTile(int indX,int indY){
        if(TileStorage.setExists(currentSet)){
            myTile.clear();
            posX = indX;
            posY = indY;
            
            indX *= tileSizeX;
            indY *= tileSizeY;

            myTile.drawImage(TileStorage.getSet(currentSet),0-indX,0-indY);

            setImage(myTile);
        }
    }

    public void previewSet(){
        GreenfootImage preview = new GreenfootImage(TileStorage.getSet(currentSet));
        preview.scale(getWorld().getWidth(),getWorld().getHeight());
        setLocation(getWorld().getWidth()/2,getWorld().getHeight()/2);
        setImage(preview);
    }

    public void saveMap(File path){
        ArrayList<TileInfo> tiles = tiler.getTiles();

        try{
            FileWriter writer = new FileWriter(path);
            for(int i = 0; i < layerData.size(); i++){
                writer.write(layerData.get(i) + "|");
            }
            writer.write(System.lineSeparator());
            for(TileInfo t : tiles){
                writer.write(t.indX + "|" + t.indY + "|" + t.x + "|" + t.y + "|" + t.set + "|" + t.layer + System.lineSeparator());
            }
            writer.close();
        }catch(IOException e){}
    }

    public void loadMap(File path){
        ArrayList<TileInfo> tiles = new ArrayList<TileInfo>();

        try{
            Scanner scan = new Scanner(path);
            String[] layerOrder = scan.nextLine().trim().split("[|]");
            DefaultListModel<String> newLayers = new DefaultListModel<String>();
            for(String s : layerOrder){
                newLayers.addElement(s);
            }
            window.layers.setSelectedIndex(0);
            window.layerList.clear();
            for(int i = 0; i < newLayers.size(); i++){
                window.layerList.addElement(newLayers.get(i));
            }
            setLayer(newLayers,layerOrder[0]);
            while(scan.hasNext()){
                String[] nextTile = scan.nextLine().trim().split("[|]");
                TileInfo newTile = new TileInfo();
                newTile.indX = Integer.parseInt(nextTile[0]);
                newTile.indY = Integer.parseInt(nextTile[1]);
                newTile.x = Integer.parseInt(nextTile[2]);
                newTile.y = Integer.parseInt(nextTile[3]);
                newTile.set = nextTile[4];
                newTile.layer = nextTile[5];
                tiles.add(newTile);
            }
        }catch(FileNotFoundException e){}

        tiler.loadTiles(tiles);
    }

    /**
     * Act - do whatever the TilePlacer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(!init){
            init = true;
            new TileWindow(this).win();
            updateTile(0,0);
        }

        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(mouse != null){
            setLocation(mouse.getX()/tileSizeX*tileSizeX+tileSizeX/2,mouse.getY()/tileSizeY*tileSizeY+tileSizeY/2);

            if(mouse.getButton() == 1 && !clicked){
                clicked = true;
                tiler.placeTile(posX,posY,mouse.getX()/tileSizeX*tileSizeX,mouse.getY()/tileSizeY*tileSizeY,currentSet);
            }else{
                clicked = false;
            }

            if(mouse.getButton() == 3){
                tiler.removeTile(mouse.getX()/tileSizeX*tileSizeX,mouse.getY()/tileSizeY*tileSizeY);
            }
        }

        if(Greenfoot.isKeyDown("shift")){
            if(!selecting){
                //selecting = true;
                //previewSet();
            }
        }else{
            if(selecting){
                //selecting = false;
                //updateTile(selX,selY);
            }
        }
    }    
}
