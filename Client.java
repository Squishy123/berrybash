import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.net.*;
import java.io.*;
/**
 * Client that connects to server
 * 
 * @author Christian Wang
 * @version 1.0
 */
public class Client extends Thread
{
    private int port = 25566;
    private String host = null;
    private Socket serverSocket = null;
    private boolean hasStopped = false;

    private ClientWorld world;

    ClientThread client;

    public Client(String host, int port, ClientWorld world) {
        this.host = host;
        this.port = port;
        this.world = world;
    }

    public void setup() {
        try {
            serverSocket = new Socket(host, port);
            client = new ClientThread(serverSocket, world);
            client.start();
            System.out.println("CONNECTED TO SERVER");
        } catch (UnknownHostException e) {
            System.err.println("Unknown host");
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public void sendObject(Object parsed) {
        client.sendObject(parsed);
    }

    /**
     * Stops the server
     */
    public void shutDown() {
        this.hasStopped = true;
        try {
            this.serverSocket.close();
        } catch(IOException e) {
            throw new RuntimeException("CANNOT CLOSE SERVER", e);
        }
        client.shutDown();
    }

    public void run() {
        setup();
        do {

            //System.out.println("RUNNING");
        } while(!hasStopped);   
        shutDown();
    }
}
