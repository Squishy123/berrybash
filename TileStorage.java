import greenfoot.*;
import java.util.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

/**
 * Write a description of class TileStorage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileStorage  
{
    public static HashMap<String,GreenfootImage> tiles;

    public static void init(){
        tiles = new HashMap<String,GreenfootImage>();
    }
    
    public static void loadTiles(){
        try{
            Scanner scan = new Scanner(new File("Tiles.dat"));
            while(scan.hasNext()){
                String nextSet = scan.nextLine();
                if(!tiles.containsKey(nextSet)){
                    tiles.put(nextSet,new GreenfootImage(nextSet));
                }
            }
        }catch(FileNotFoundException e){}
    }

    public static void addSet(File set){
        try{
            Path setPath = Paths.get(set.getPath());
            String setName = set.getName();
            Files.copy(setPath,Paths.get(System.getProperty("user.dir")+"\\images\\"+setName),StandardCopyOption.REPLACE_EXISTING);
            FileWriter tilesDat = new FileWriter("Tiles.dat");
            tiles.put(setName,new GreenfootImage(setName));
            for(String s : tiles.keySet()){
                tilesDat.write(s + "\n");
            }
            tilesDat.close();
            loadTiles();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static GreenfootImage getSet(String name){
        if(tiles.containsKey(name)){
            return tiles.get(name);
        }else{
            return null;
        }
    }
    
    public static boolean setExists(String name){
        return tiles.containsKey(name);
    }
    
    public static Set<String> getNames(){
        return tiles.keySet();
    }
}
