/**
 * DisplayGrid.java
 * A Small program for Display a 2D String Array graphically
 * @author Mangat
 */

// Graphics Imports
import javax.swing.*;
import java.awt.*;


class DisplayGrid { 

  // Graphics
  private JFrame frame;
  private int maxX,maxY, GridToScreenRatio;
  private Entity[][] world;
  private int turnNumber;
  
  // Highest fitness stats for entities, updated only when required
  private int bestSheepCount;
  private String bestSheepGender = "None";
  private int bestWolfCount;
  private String bestWolfGender = "None";
  
  // Current toals
  private int grassCount;
  private int sheepCount;
  private int wolfCount;
  
  DisplayGrid(Entity[][] w, int turn) { 
    this.world = w;
    this.turnNumber = turn;
    
    maxX = Toolkit.getDefaultToolkit().getScreenSize().width-100;
    maxY = Toolkit.getDefaultToolkit().getScreenSize().height-100;
    GridToScreenRatio = maxY / (world.length+1);  //ratio to fit in screen as square map
    
    System.out.println("Map size: "+world.length+" by "+world[0].length + "\nScreen size: "+ maxX +"x"+maxY+ " Ratio: " + GridToScreenRatio);
    
    this.frame = new JFrame("Map of World");
    
    GridAreaPanel worldPanel = new GridAreaPanel();
    
    frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    frame.setVisible(true);
  }
  
  /**
   * refresh 
   * Refreshes the frame
   */
  public void refresh() { 
    frame.repaint();
  }
  
  /**
   * addTurn
   * Adds a turn to the display's turn counter
   */
  public void addTurn() {
    this.turnNumber +=1;
  }
  
  /**
   * updateBestSheep 
   * Updates highest fitness sheep with current stats
   * @param entity, The new sheep with highest fitness
   */
  public void updateBestSheep(Entity entity) {
    this.bestSheepCount = ((Sheep)entity).getFitness();
    if (((Sheep)entity).getGender() == 0) {
      this.bestSheepGender = "Male";
    } else {
      this.bestSheepGender = "Female";
    }
  }
  
  
  /**
   * updateBestWolf 
   * Updates highest fitness wolf with current stats
   * @param entity The new wolf with highest fitness
   */
  public void updateBestWolf(Entity entity) {
    this.bestWolfCount = ((Wolf)entity).getFitness();
    if (((Wolf)entity).getGender() == 0) {
      this.bestWolfGender = "Male";
    } else {
      this.bestWolfGender = "Female";
    }
  }
  
  
    
  /**
   * updateAnimalCount 
   * Updates current entity counts
   * @param grass Surrent number of grass
   * @param sheep Current number of sheep
   * @param wolves Current number of Wolves
   */
  public void updateAnimalCount(int grass, int sheep, int wolves) {
    this.grassCount = grass;
    this.sheepCount = sheep;
    this.wolfCount = wolves;
  }
  
    
  class GridAreaPanel extends JPanel {
    public void paintComponent(Graphics g) {        
      
      setDoubleBuffered(true); 
      g.setColor(Color.BLACK);
      
      
      for (int i = 0; i<world.length; i++){ 
        for (int j = 0; j<world[0].length;j=j+1) {
          if (world[i][j] instanceof Grass) {    //This block can be changed to match character-color pairs
            g.setColor(Color.GREEN);
          } else if (world[i][j] instanceof Sheep) {
            g.setColor(Color.WHITE);
          } else if (world[i][j] instanceof Wolf) {
            g.setColor(Color.RED);
          } else {
            g.setColor(Color.LIGHT_GRAY);
          }
          
          
          g.fillRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          g.setColor(Color.BLACK);
          //g.drawRect(j*GridToScreenRatio, i*GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          
          displayInfo(g);
        }
      }
    }
    
    
      
    /**
     * refresh 
     * Displays current health and stats for each entity - experimental only
     * @param g Graphics object
     * @param i Y index of current entity on map
     * @param j X index of current entity on map
     */
    public void paintHealth(Graphics g, int i, int j) {
      if (world[i][j] != null) {
        g.setColor(Color.BLACK);
        String xCoord = Integer.toString(i);
        String yCoord = Integer.toString(j);
        String gender = "";
        String cooldown = "";
        g.drawString(world[i][j].getHealthString(), j*GridToScreenRatio, (i+1)*GridToScreenRatio);
        
        if (world[i][j] instanceof Sheep) {
          if (((Sheep)world[i][j]).getGender() == 0) {
            gender = "male";
          } else if (((Sheep)world[i][j]).getGender() == 1) {
            gender = "female";
          }
          g.drawString(gender, j*GridToScreenRatio, (i+1)*GridToScreenRatio-10);
          cooldown = Integer.toString(((Sheep)world[i][j]).getBreedingCoolDown());
          g.drawString(cooldown, j*GridToScreenRatio, (i+1)*GridToScreenRatio-20);
          
        } else if (world[i][j] instanceof Wolf) {
          if (((Wolf)world[i][j]).getGender() == 0) {
            gender = "male";
          } else if (((Wolf)world[i][j]).getGender() == 1) {
            gender = "female";
          }
          g.drawString(gender, j*GridToScreenRatio, (i+1)*GridToScreenRatio-10);
          cooldown = Integer.toString(((Wolf)world[i][j]).getBreedingCoolDown());
          g.drawString(cooldown, j*GridToScreenRatio, (i+1)*GridToScreenRatio-20);
        }
        
      }
    }
    
    
      
    /**
     * displayInfo 
     * Displays current info to the right of the screen
     * @param g Graphics object
     */
    public void displayInfo(Graphics g) {

      g.setColor(Color.BLACK);
      g.drawString("Number of turns: " + Integer.toString(turnNumber), 700, 100);
      g.drawString("Number of grass: " + Integer.toString(grassCount), 700, 120);
      g.drawString("Number of sheep: " + Integer.toString(sheepCount), 700, 140);
      g.drawString("Number of wolves: " + Integer.toString(wolfCount), 700, 160);
      
      g.drawString("Most fit sheep (number of offspring produced) - " + bestSheepGender + ", total offspring: " + Integer.toString(bestSheepCount), 700, 200);
      g.drawString("Most fit wolf (number of offspring produced) - " + bestWolfGender + ", total offspring: " + Integer.toString(bestWolfCount), 700, 220);
    }
        
      
  }//end of GridAreaPanel
  
} //end of DisplayGrid

