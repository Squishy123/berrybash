import java.util.*;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Container;
import java.nio.file.StandardCopyOption.*;
import java.nio.file.*;
import java.io.FileWriter;
import java.awt.Image;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.event.*;

/**
 * A window used for tile editing. It can be used to choose a tileset, select which tiles to place, and change the layers.
 * 
 * @author Garrett
 * @version Jan 2017
 */
public class TileWindow{
    int offX,offY,posX,posY = 0;
    BufferedImage[][] tiles;
    JButton[][] labels;
    JLabel prelabel;
    BufferedImage set;
    int tileSizeX = 32;
    int tileSizeY = 32;
    JMenu menu;
    TilePlacer placer;
    JList layers;
    DefaultListModel<String> layerList;
    JTextField layerName;
    String currentLayer;

    /**
     * The constructor for a tile window which sets a reference to the tile placer and gives the tile placer a reference to this window.
     */
    public TileWindow(TilePlacer placer){
        this.placer = placer;
        placer.window = this;
    }

    /**
     * Responsible for cutting the tiles and pasting them onto the grid of buttons.
     */
    public BufferedImage[][] getTiles(){
        tiles = new BufferedImage[set.getHeight()/tileSizeY][set.getWidth()/tileSizeX];
        int maxX = set.getWidth();
        int maxY = set.getHeight();
        int rows = set.getHeight()/tileSizeY;
        int columns = set.getWidth()/tileSizeX;
        int count = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(j*tileSizeX + tileSizeX >= maxX+1 || i*tileSizeY + tileSizeY >= maxY+1){
                    break;
                }
                tiles[i][j] = set.getSubimage(j*tileSizeX,i*tileSizeY,tileSizeX,tileSizeY);
                count++;
            }
        }
        return tiles;
    }

    public void updatePositions(){
        for(int i = 0; i < 10; i++){
            if(i >= set.getHeight()/tileSizeY){
                break;
            }
            for(int j = 0; j < 10; j++){
                if(j >= set.getWidth()/tileSizeX || (i+offY >= tiles.length || (tiles.length > 0 && j+offX >= tiles[0].length))){
                   break;
                }
                if(tiles[i+offY][j+offX] != null){
                    int newWidth = Math.max(1,labels[i][j].getWidth());
                    int newHeight = Math.max(1,labels[i][j].getHeight());
                    labels[i][j].setIcon(new ImageIcon(tiles[i+offY][j+offX].getScaledInstance(newWidth,newHeight,Image.SCALE_SMOOTH)));
                }
            }
        }
        try{
            prelabel.setIcon(new ImageIcon(set.getSubimage(posX*tileSizeX,posY*tileSizeY,tileSizeX,tileSizeY)));
        }catch(Exception e){}
    }

    public void updateSize(){
        if(labels == null || labels[0] == null || labels[0][0] == null){return;}

        for(int i = 0; i < 10; i++){
            if(i >= set.getHeight()/tileSizeY){
                break;
            }
            for(int j = 0; j < 10; j++){
                if(j >= set.getWidth()/tileSizeX || (i+offY >= tiles.length || (tiles.length > 0 && j+offX >= tiles[0].length))){
                    break;
                }
                if(tiles[i+offY][j+offX] != null){
                    //FIX NULL
                    int newWidth = Math.max(1,labels[i][j].getWidth());
                    int newHeight = Math.max(1,labels[i][j].getHeight());
                    labels[i][j].setIcon(new ImageIcon(tiles[i+offY][j+offX].getScaledInstance(newWidth,newHeight,Image.SCALE_SMOOTH)));
                }
            }
        }
    }

    /**
     * Manages the horizontal scrollbar.
     */
    class HoriListen implements AdjustmentListener {
        public void adjustmentValueChanged(AdjustmentEvent e) {
            offX = e.getValue();
            updatePositions();
        }
    }

    class VertiListen implements AdjustmentListener {
        public void adjustmentValueChanged(AdjustmentEvent e) {
            offY = e.getValue();
            updatePositions();
        }
    }

    class TileButtonResizer implements ComponentListener{
        public void componentHidden(ComponentEvent e){}

        public void componentMoved(ComponentEvent e){}

        public void componentShown(ComponentEvent e){}

        public void componentResized(ComponentEvent e){
            updateSize();
        }
    }

    private void addLabelTextRows(JLabel[] labels, JSpinner[] textFields, Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    public void newTileset(File set){
        TileStorage.addSet(set);
        placer.updateSet(set.getName());
    }

    /**
     * Makes the window.
     */
    public void win(){
        try{
            set = ImageIO.read(new File("images\\PathAndObjects.png"));
            getTiles();

            int rows = set.getWidth()/tileSizeX;
            int columns = set.getHeight()/tileSizeY;

            JFrame frame = new JFrame("Tile Editor");
            JPanel panel = new JPanel(new GridLayout(10,10,1,1));

            //frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addComponentListener(new TileButtonResizer());
            frame.setMinimumSize(new Dimension(100,100));

            labels = new JButton[10][10];

            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    labels[i][j] = new JButton(new ImageIcon(tiles[i][j]));
                    labels[i][j].setPreferredSize(new Dimension(32,32));
                    labels[i][j].setForeground(Color.BLACK);
                    final int buttonI = i;
                    final int buttonJ = j;
                    labels[i][j].addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e)
                            {
                                posX = buttonJ + offX;
                                posY = buttonI + offY;
                                placer.updateTile(posX,posY);
                                updatePositions();
                            }
                        });
                    panel.add(labels[i][j]);
                }
            }

            panel.setPreferredSize(new Dimension(350,350));
            JPanel spanel = new JPanel(new BorderLayout());

            final JScrollBar hbar=new JScrollBar(JScrollBar.HORIZONTAL, 0, 20, 0, 300);
            final JScrollBar vbar=new JScrollBar(JScrollBar.VERTICAL, 0, 20, 0, 300);

            hbar.setMaximum(20+Math.max(0,columns-10));
            vbar.setMaximum(20+Math.max(0,rows-10));

            hbar.setUnitIncrement(1);
            hbar.setBlockIncrement(1);

            vbar.setUnitIncrement(1);
            vbar.setBlockIncrement(1);

            hbar.addAdjustmentListener(new HoriListen());
            vbar.addAdjustmentListener(new VertiListen());

            spanel.add(hbar, BorderLayout.SOUTH);
            spanel.add(vbar, BorderLayout.EAST);
            spanel.add(panel);

            hbar.setValue(0);
            vbar.setValue(0);

            JMenuBar menuBar = new JMenuBar();

            menu = new JMenu("File");
            menuBar.add(menu);

            JMenuItem menuItemO = new JMenuItem("Open Tileset");
            menuItemO.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
            menu.add(menuItemO);

            menuItemO.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e)
                    {
                        JFileChooser chooser = new JFileChooser();
                        int re = chooser.showOpenDialog(menu);

                        if(re == JFileChooser.APPROVE_OPTION){
                            try{
                                BufferedImage tempset = ImageIO.read(chooser.getSelectedFile());

                                int rows = tempset.getHeight()/tileSizeY;
                                int columns = tempset.getWidth()/tileSizeX;

                                hbar.setMaximum(20+Math.max(0,columns-10));
                                vbar.setMaximum(20+Math.max(0,rows-10));

                                offX = 0;
                                offY = 0;
                                posX = 0;
                                posY = 0;

                                set = tempset;
                                tempset = null;

                                newTileset(chooser.getSelectedFile());

                                getTiles();
                                updatePositions();
                            }catch(IOException ex){}
                            catch(NullPointerException e2){
                                System.out.println("Don't use this");
                            }
                        }
                    }
                });

            JMenuItem menuItem = new JMenuItem("Save TileMap");
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
            menu.add(menuItem);

            menuItem.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JFileChooser chooser = new JFileChooser();
                        int re = chooser.showSaveDialog(menu);

                        if(re == JFileChooser.APPROVE_OPTION){
                            placer.saveMap(chooser.getSelectedFile());
                        }
                    }
                });

            JMenuItem menuItemLoad = new JMenuItem("Load TileMap");
            menuItemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
            menu.add(menuItemLoad);

            menuItemLoad.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JFileChooser chooser = new JFileChooser();
                        int re = chooser.showOpenDialog(menu);

                        if(re == JFileChooser.APPROVE_OPTION){
                            placer.loadMap(chooser.getSelectedFile());
                        }
                    }
                });

            JMenu selectTileMenu = new JMenu("Tiles");
            menuBar.add(selectTileMenu);

            for(String s : TileStorage.getNames()){
                JMenuItem newI = new JMenuItem(s);
                selectTileMenu.add(newI);
                final String actionString = s;
                newI.addActionListener(new ActionListener(){ 
                        public void actionPerformed(ActionEvent e){
                            File file = new File("images\\"+actionString);
                            if(file.exists()){
                                try{
                                    BufferedImage tempset = ImageIO.read(file);

                                    int rows = tempset.getHeight()/tileSizeY;
                                    int columns = tempset.getWidth()/tileSizeX;
                                    
                                    hbar.setMaximum(20+Math.max(0,columns-10));
                                    vbar.setMaximum(20+Math.max(0,rows-10));

                                    offX = 0;
                                    offY = 0;
                                    posX = 0;
                                    posY = 0;

                                    set = tempset;
                                    tempset = null;

                                    newTileset(file);

                                    getTiles();
                                    updatePositions();
                                }catch(IOException ex){}
                                catch(NullPointerException e2){
                                    System.out.println("Don't use this");
                                }
                            }
                        }
                    });
            }

            JPanel lpanel = new JPanel(new BorderLayout());

            GridBagLayout gb = new GridBagLayout();
            JPanel upperPanel = new JPanel(gb);
            JLabel[] labels = new JLabel[6];
            JSpinner[] stuff = new JSpinner[6];
            labels[0] = new JLabel("Tile Width");
            stuff[0] = new JSpinner(new SpinnerNumberModel(tileSizeX, 1, 128, 1));
            labels[1] = new JLabel("Tile Height");
            stuff[1] = new JSpinner(new SpinnerNumberModel(tileSizeY, 1, 128, 1));
            labels[2] = new JLabel("X-Off");
            stuff[2] = new JSpinner(new SpinnerNumberModel(0, 0, 128, 1));
            labels[3] = new JLabel("Y-Off");
            stuff[3] = new JSpinner(new SpinnerNumberModel(0, 0, 128, 1));
            labels[4] = new JLabel("X-Sep");
            stuff[4] = new JSpinner(new SpinnerNumberModel(0, 0, 128, 1));
            labels[5] = new JLabel("Y-Sep");
            stuff[5] = new JSpinner(new SpinnerNumberModel(0, 0, 128, 1));

            JPanel lowerPanel = new JPanel(new BorderLayout());
            BufferedImage pic = ImageIO.read(new File("images\\PathAndObjects.png"));
            ImageIcon preview = new ImageIcon(pic);
            prelabel = new JLabel(preview);
            prelabel.setPreferredSize(new Dimension(40,80));

            lowerPanel.add(prelabel);

            GridBagLayout gb2 = new GridBagLayout();
            JPanel layerPanel = new JPanel(gb2);
            
            currentLayer = "Default Layer";

            layerList = new DefaultListModel<String>();
            layerList.addElement("Actors");
            layerList.addElement("Default Layer");
            layerList.addElement("Solids");

            layers = new JList(layerList);
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
            c.weighty = 1.0;

            layers.addListSelectionListener(new ListSelectionListener(){
                    public void valueChanged(ListSelectionEvent e){
                        int sIndex = layers.getSelectedIndex();
                        if(sIndex >= layerList.size() || sIndex < 0)return;
                        currentLayer = layerList.get(sIndex);
                        System.out.println(currentLayer);
                        placer.setLayer(layerList,currentLayer);
                    }
                });

            JPanel layerSelect = new JPanel(new BorderLayout());
            layerPanel.add(layerSelect,c);

            JScrollPane layerscroll = new JScrollPane();
            layerscroll.setViewportView(layers);
            layerSelect.add(layerscroll,BorderLayout.CENTER);

            JPanel layerBottomMenu = new JPanel(new BorderLayout());
            layerSelect.add(layerBottomMenu,BorderLayout.SOUTH);

            JPanel layerButtons = new JPanel();
            layerBottomMenu.add(layerButtons,BorderLayout.NORTH);

            layerName = new JTextField();
            layerName.setPreferredSize(new Dimension(50,16));
            layerButtons.add(layerName);

            BasicArrowButton layup = new BasicArrowButton(BasicArrowButton.NORTH);
            layerButtons.add(layup);

            layup.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        int sIndex = layers.getSelectedIndex();
                        if(sIndex >= layerList.size())return;
                        String layerName = layerList.get(sIndex);
                        if(sIndex > 0){
                            String prevLayer = layerList.set(sIndex-1,layerName);
                            layerList.set(sIndex,prevLayer);
                            layers.setSelectedIndex(sIndex-1);
                            currentLayer = layerList.get(sIndex-1);
                            placer.setLayer(layerList,currentLayer);
                        }
                    }
                });

            BasicArrowButton laydown = new BasicArrowButton(BasicArrowButton.SOUTH);
            layerButtons.add(laydown);

            laydown.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        int sIndex = layers.getSelectedIndex();
                        if(sIndex >= layerList.size())return;
                        String layerName = layerList.get(sIndex);
                        if(sIndex < layerList.size()-1){
                            String prevLayer = layerList.set(sIndex+1,layerName);
                            layerList.set(sIndex,prevLayer);
                            layers.setSelectedIndex(sIndex+1);
                            currentLayer = layerList.get(sIndex+1);
                            placer.setLayer(layerList,currentLayer);
                        }
                    }
                });

            JButton addLayer = new JButton("New Layer");
            layerBottomMenu.add(addLayer,BorderLayout.WEST);

            addLayer.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        if(layerName.getText().equals("")){return;}
                        layerList.addElement(layerName.getText());
                        placer.setLayer(layerList,currentLayer);
                    }
                });

            JButton setLayer = new JButton("Set Layer");
            layerBottomMenu.add(setLayer,BorderLayout.EAST);

            setLayer.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        if(layerName.getText().equals("")){return;}
                        int sIndex = layers.getSelectedIndex();
                        String prevName = layerList.get(sIndex);
                        layerList.set(sIndex,layerName.getText());
                        currentLayer = layerList.get(sIndex);
                        placer.renameLayer(prevName,currentLayer);
                        placer.setLayer(layerList,currentLayer);
                    }
                });
                
                //This was a really informative presentation and I enjoyed learned more about neutralization reactions. To answer your question on the uses of titration, the food industry can use the data obtained from a titration to find how much a specific chemical is in a food. It is usually to determine the water content, fat content and the concentrations of vitamins. Titration is also used by producers of wine and cheese to test if their product is ready to be consumed. Lastly, titration is used to analyze underwater envirionments to make sure the water is at a certain pH and other properties such as the concentration of ammonia and this information helps us determine if an aquatic enviroment is fit for marine life.
                
                //You have done a good job on this presentation and I really enjoyed reading about the ideal gas law. For the question, the ideal gas law can be used by engineering companies to find the capacity of containers used for storage, and the calculations are also used to determine efficiency of equipment. In a lab, scientists will use the ideal gas law to calculate how much a gas a reaction is going to produce so they can be more accurate when dealing with gases. Finally, it is also used in football where before a game starts, they make sure that the volume of the ball is adjusted based on the temperature of the atmosphere. So, if the temperature was lower that day, they would decrease the volume of the football.

            addLabelTextRows(labels,stuff,upperPanel);

            upperPanel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Tile Options"),
                    BorderFactory.createEmptyBorder(5,5,5,5)));

            layerPanel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Layers"),
                    BorderFactory.createEmptyBorder(5,5,5,5)));

            JToolBar toolbar = new JToolBar();

            Image undoPic = ImageIO.read(new File("images//undo.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            JButton undo = new JButton(new ImageIcon(undoPic));
            toolbar.add(undo);

            Image redoPic = ImageIO.read(new File("images//redo.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            JButton redo = new JButton(new ImageIcon(redoPic));
            toolbar.add(redo);

            lpanel.add(upperPanel,BorderLayout.NORTH);
            lpanel.add(lowerPanel,BorderLayout.SOUTH);
            lpanel.add(layerPanel,BorderLayout.CENTER);
            frame.add(toolbar,BorderLayout.NORTH);

            frame.setAlwaysOnTop(true);
            frame.getContentPane().add(spanel);
            frame.getContentPane().add(lpanel,BorderLayout.WEST);
            frame.setJMenuBar(menuBar);
            frame.setSize(200,200);
            frame.pack();
            frame.setVisible(true);
        }catch(IOException e){}
    }
}