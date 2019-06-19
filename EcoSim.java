import java.util.Scanner;
import java.lang.Math;


/**
 * EcoSim.java
 * @version 1.0
 * @author Jesse Liu
 * @since April 24, 2019
 * Master program to simulate ecosystem
 * 
 * CHANGES: 
 * 
 * Added Genders to entities. Sheep breeding, Wolf breeding + attacking is 
 * based genders in addition to health (additional criteria)
 * 
 * Added preferential movement based on health of entity. If health is above threshold,
 * the entity prioritizes movement to breed. If under the threshold, it must move to food.
 * 
 * OPTIMAL NUMBERS:
 * Grid Size: 50
 * Frequency of Grass Spawn: 0.2
 * Initial grass HP: 5
 * Initial Number of Sheep: 100
 * Initial Sheep HP: 25
 * Initial Number of Wolves: 20
 * Initial Wolf HP: 40
 * 
 */

class EcoSim {
  static int plantHealth;
  
  public static void main(String[] args) {
    
    // Scanner
    Scanner input = new Scanner(System.in);
    
    // Get necessary info
    System.out.println("Grid Size: ");
    int mapSize = input.nextInt();
    
    // Spawnrate
    System.out.println("Frequency of grass spawning: ");
    double grassRate = input.nextDouble();
    
    // Grass Health
    System.out.println("Initial health points of grass (integer value):");
    int grassHP = input.nextInt();
    
    // Initial number of sheep
    System.out.println("Initial number of sheep (integer value): ");
    int initialSheep = input.nextInt();
    
    // Sheep Health
    System.out.println("Initial sheep HP (integer value): ");
    int sheepHP = input.nextInt();
    
    // Initial number of Wolves
    System.out.println("Initial number of wolves (integer value): ");
    int initialWolves = input.nextInt();
    
    // Wolf Health
    System.out.println("Initial wolf HP (integer value): ");
    int wolfHP = input.nextInt();
    
    // Create the master map controller, pass all rates and stats into the controller
    EntMaster master = new EntMaster(mapSize, grassRate, grassHP, initialSheep, sheepHP, initialWolves, wolfHP);
    
    //
    System.out.println("_______________________________________________\nSimulation Started.")
    
    // Simulation boolean on
    boolean simOn = true;
    
    // Initialize display, spawn the initial grass in environment
    master.displayMap();
    master.spawnGrass();
    
    // Compensate for an apparent delay on MAC - DELETE ON WINDOWS IF NO DELAY APPARENT
    try{Thread.sleep(3000); }catch(Exception e) {};
    
    while (simOn) {
      master.updateInfo();
      master.spawnGrass();
      master.resetMoveConditions();
      master.moveEntities();
      master.displayMap();
      try { Thread.sleep(100); }catch(Exception e) {};
      
      
      // Check for extinction, turn simulation off
      if (master.checkExtinction() == true) {
        simOn = false;
      }
      
    }
    
    System.out.println("_______________________________________________\nSimulation complete");
    System.out.println("The simulation lasted " + master.getTurn() + " turns.");
    System.out.println(master.getMasterGrass() + " total grass were spawned throughout the simulation.");
    System.out.println(master.getMasterSheep() + " total sheep were spawned throughout the simulation.");
    System.out.println(master.getMasterWolves() + " total wolves were spawned throughout the simulation.");

  }
}