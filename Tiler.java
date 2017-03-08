import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import java.util.Collections;
import java.util.Comparator;

/**
 * Write a description of class Tiler here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tiler extends Actor
{
    private boolean s = false;
    private GreenfootImage my;
    private int tileSizeX,tileSizeY;
    private ArrayList<TileInfo> tileinfo;
    private String layer;
    private DefaultListModel<String> layerData;
    private ArrayList<TileLayer> activeLayers;

    public Tiler(String startingSet, int tileSizeX, int tileSizeY){
        this.tileSizeX = tileSizeX;
        this.tileSizeY = tileSizeY;
        this.tileinfo = new ArrayList<TileInfo>();
        this.layer = "Default Layer";
        layerData = new DefaultListModel<String>();
        activeLayers = new ArrayList<TileLayer>();
        for(int i = 0; i < 3; i++){
            TileLayer newLay = new TileLayer();
            activeLayers.add(newLay);
        }
        getImage().clear();
    }

    public void redrawTiles(){
        for(TileLayer lay : activeLayers){
            lay.getImage().clear();
            getWorld().removeObject(lay);
        }
        
        for(int i = activeLayers.size()-1; i >= 0; i--){
            getWorld().addObject(activeLayers.get(i),getWorld().getWidth()/2,getWorld().getHeight()/2);
        }

        Collections.sort(tileinfo,new Comparator<TileInfo>(){
                public int compare(TileInfo a,TileInfo b){
                    if(layerData.indexOf(a.layer) < layerData.indexOf(b.layer)){
                        return -1;
                    }else if(layerData.indexOf(a.layer) > layerData.indexOf(b.layer)){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });
        //System.out.println("------------------");
        String currentLayer = "";
        int layerIndex = 0;
        GreenfootImage tempLayer = new GreenfootImage(getWorld().getWidth(),getWorld().getHeight());
        for(TileInfo t: tileinfo){
            //System.out.println(t.layer);
            if(TileStorage.setExists(t.set)){
                if(currentLayer.equals("")){
                    currentLayer = t.layer;
                    //getWorld().addObject(activeLayers.get(0),getWorld().getWidth()/2,getWorld().getHeight()/2);
                }else if(!currentLayer.equals(t.layer)){
                    activeLayers.get(layerIndex).setImage(tempLayer);
                    tempLayer = new GreenfootImage(getWorld().getWidth(),getWorld().getHeight());
                    currentLayer = t.layer;
                    layerIndex++;
                    //getWorld().addObject(activeLayers.get(layerIndex),getWorld().getWidth()/2,getWorld().getHeight()/2);
                }
                GreenfootImage newTile = new GreenfootImage(tileSizeX,tileSizeY);

                int indX = t.indX * tileSizeX;
                int indY = t.indY * tileSizeY;
                String set = t.set;

                newTile.drawImage(TileStorage.getSet(set),0-indX,0-indY);
                tempLayer.drawImage(newTile,t.x,t.y);
            }
        }
        //System.out.println("------------------");
        activeLayers.get(layerIndex).setImage(tempLayer);
    }

    public void placeTile(int indX,int indY, int x, int y, String set){
        TileInfo newTile = new TileInfo();
        newTile.indX = indX;
        newTile.indY = indY;
        newTile.x = x;
        newTile.y = y;
        newTile.set = set;
        newTile.layer = layer;
        for(TileInfo t : tileinfo){
            if(t.indX == newTile.indX && t.indY == newTile.indY && t.x == newTile.x && t.y == newTile.y && t.set == newTile.set && t.layer == this.layer){
                return;
            }
        }
        tileinfo.add(newTile);
        redrawTiles();
    }

    public void removeTile(int x, int y){
        for(int i = 0; i < tileinfo.size(); i++){
            TileInfo info = tileinfo.get(i);
            if(info.x == x && info.y == y && info.layer.equals(layer)){
                tileinfo.remove(i);
                i--;
            }
        }
        redrawTiles();
    }

    public void renameLayer(String prev, String name){
        for(int i = 0; i < tileinfo.size(); i++){
            if(tileinfo.get(i).layer.equals(prev)){
                tileinfo.get(i).layer = name;
            }
        }
    }

    public void setLayer(DefaultListModel<String> data, String name){
        this.layerData = data;
        this.layer = name;
        if(activeLayers.size() != data.size()){
            for(TileLayer lay : activeLayers){
                lay.getImage().clear();
                getWorld().removeObject(lay);
            }
            activeLayers = new ArrayList<TileLayer>();
            for(int i = 0; i < data.size(); i++){
                TileLayer newLay = new TileLayer();
                activeLayers.add(newLay);
            }
        }
    }

    public void loadTiles(ArrayList<TileInfo> tiles){
        tileinfo = tiles;
        redrawTiles();
    }

    public ArrayList<TileInfo> getTiles(){
        return tileinfo;
    }

    /**
     * Act - do whatever the Tiler wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(!s){
            s = true;
            setLocation(getWorld().getWidth()/2,getWorld().getHeight()/2);
            my = new GreenfootImage(getWorld().getWidth(),getWorld().getHeight());
        }
    }
}
