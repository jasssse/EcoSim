import java.lang.Math;
import java.util.Random;

/*
 * EntMaster.java
 * @version 1.0
 * @since April 24, 2019
 * Manages all entities and interactions
 */


class EntMaster {
  // Create the random generator (object)
  Random rand = new Random();
  
  // 2D array of entities
  private Entity[][] entMap;
  // Turn number (0)
  private static int turn = 0;
  // Map size
  private int mapSize;
  // Display object
  private DisplayGrid display;
  // Given spawn rate of grass
  private final double GRASSRATE;
  // Given grass health
  private final int GRASSHEALTH;
  // Given sheep health
  private final int SHEEPHEALTH;
  // Given wolf health
  private final int WOLFHEALTH;
  
  // Current entity counts
  private int grassAlive = 0;
  private int sheepAlive = 0;
  private int wolvesAlive = 0;
  
  // Historical entity counts
  private int masterSheepCount = 0;
  private int masterGrassCount = 0;
  private int masterWolfCount = 0;
  
  // Best wolf health
  private int wolfBreedCount = 0;
  // Best sheep health
  private int sheepBreedCount = 0;

  
  
  EntMaster(int size, double grassRate, int grassHP, int initialSheep, int sheepHP, int initialWolves, int wolfHP) {
    this.mapSize = size;
    entMap = new Entity[size][size];
    display = new DisplayGrid(entMap, turn);
    this.GRASSRATE = grassRate;
    this.GRASSHEALTH = grassHP;
    this.SHEEPHEALTH = sheepHP;
    this.WOLFHEALTH = wolfHP;

    this.generateRandomSheep(initialSheep);
    this.generateRandomWolf(initialWolves);
  }
  
  
  /**
   * addGrass
   * Initiates addEnt method with a grass object meant to be created
   * @param y The Y position in the 2D array that holds the object
   * @param x The X position in the 2D array that holds the object
   * @return boolean A boolean to represent the success of creating and adding the grass
   */
  boolean addGrass(int y, int x) {
    return addEnt(new Grass(GRASSHEALTH, y, x));
  }
  
  
  /**
   * addSheep
   * Initiates addEnt method with a sheep object meant to be created
   * @param y The Y position in the 2D array that holds the object
   * @param x The X position in the 2D array that holds the object
   * @return boolean A method (boolean return) to represent the success of creating and adding the sheep
   */
  boolean addSheep(int y, int x) {
    return addEnt(new Sheep(SHEEPHEALTH, y, x));
  }
  
  
  /**
   * addWolf
   * Initiates addEnt method with a wolf object meant to be created
   * @param y The Y position in the 2D array that holds the object
   * @param x The X position in the 2D array that holds the object
   * @return boolean A method (boolean return) to represent the success of creating and adding the sheep
   */
  boolean addWolf(int y, int x) {
    return addEnt(new Wolf(WOLFHEALTH, y, x));
  }
  
  
  /**
   * addEnt
   * Private method that attempts to add an object to the entMap array, should be initiated by specific add entity methods
   * @param entity The entity that is being added to the entMap array
   * @return boolean A boolean to represent addition of the specified object
   */
  private boolean addEnt(Entity entity) {
    int testX = entity.getX();
    int testY = entity.getY();
    
    // Coordinates are reversed for the 2-D array - Y comes before X
    if (entMap[testY][testX] == null) {
      entMap[testY][testX] = entity;
      return true;
    } else {
      return false;
    }
  }
  
  
  /**
   * updateInfo
   * Adds a turn count, counts entities on the map, updates display with current information
   * including the the sheep and wolf with highest fitness
   */
  void updateInfo() {
    turn += 1;
    display.addTurn();
    int currentSheepCount = 0;
    int currentWolfCount = 0;
    int currentGrassCount = 0;
    
    for (int i = 0; i<entMap.length; i++) {
      for (int j = 0; j<entMap[0].length; j++) {
        
        if (entMap[i][j] instanceof Grass) {
          currentGrassCount += 1;
        
        } else if (entMap[i][j] instanceof Sheep) {
          ((Sheep)entMap[i][j]).addCoolDown();
          currentSheepCount += 1;
          
          if (((Sheep)entMap[i][j]).getFitness() > this.sheepBreedCount) {
            this.sheepBreedCount = ((Sheep)entMap[i][j]).getFitness();
            display.updateBestSheep(entMap[i][j]);
          }
          
        } else if (entMap[i][j] instanceof Wolf) {
          ((Wolf)entMap[i][j]).addCoolDown();
          currentWolfCount +=1;
          
          if (((Wolf)entMap[i][j]).getFitness() > this.wolfBreedCount) {
            this.wolfBreedCount = ((Wolf)entMap[i][j]).getFitness();
            display.updateBestWolf(entMap[i][j]);
          }
        }
      }
    }
    
    this.grassAlive = currentGrassCount;
    this.sheepAlive = currentSheepCount;
    this.wolvesAlive = currentWolfCount;
    
    display.updateAnimalCount(currentGrassCount, currentSheepCount, currentWolfCount);
    
  }
  
  
  /**
   * getTurn
   * Retrieves the turn number
   * @return int, The turn number of the current turn
   */
  int getTurn() {
    return turn;
  }
  
  
  /**
   * getMasterGrass
   * Retrieves the historic total of grass
   * @return int, The turn number of total grass spawned throughout the simulation
   */
  int getMasterGrass() {
    return masterGrassCount;
  }
  
  
  /**
   * getMasterSheep
   * Retrieves the historic total of sheep
   * @return int, The turn number of total sheep spawned throughout the simulation
   */
  int getMasterSheep() {
    return masterSheepCount;
  }
  
  
  /**
   * getMasterWolves
   * Retrieves the historic total of wolves
   * @return int, The turn number of total wolves spawned throughout the simulation
   */
  int getMasterWolves() {
    return masterWolfCount;
  }
  
  
  /**
   * checkFull
   * Checks if the entity array is full
   * @return boolean, Condition of being full/not full
   */
  boolean checkFull(){
    boolean full = true;
    for (int i = 0; i < entMap.length; i++) {
      for (int j = 0; j < entMap[0].length; j++) {
        if (entMap[i][j] == null) {
          full = false;
        }
      }
    }
    return full;
  }
      
  
  /**
   * printMap
   * Displays a string-based map in the console
   */
  void printMap() {
    for (int i = 0; i < entMap.length; i++) {
      for (int j = 0; j< entMap[0].length; j++) {
        if (entMap[i][j] instanceof Grass) {
          System.out.print("[G]");
        } else if (entMap[i][j] instanceof Sheep) {
          System.out.print("[S]");
        } else if (entMap[i][j] instanceof Wolf) {
          System.out.print("[W]");
        } else {
          System.out.print("[ ]");
        }
        System.out.print(".");
      }
      System.out.println("");
    }
    System.out.println("");
  }
  
  
  /**
   * displayMap
   * Refreshes the display grid
   */
  void displayMap() {
    display.refresh();
  }
  
  
  /**
   * spawnGrass
   * Spawns grass based on the given grass spawn rate
   * Every null space has a given chance to spawn a grass if a random double generated is below the double GRASSRATE
   */
  void spawnGrass() {
    double spawnDouble;
    for (int i = 0; i < entMap.length; i++) {
      for (int j = 0; j < entMap.length; j++){
        if ((entMap[i][j] == null) && (this.checkFull() == false)) {
          spawnDouble = Math.random();
          //System.out.println(i + " " + j + " " + spawnDouble);
          if (spawnDouble < GRASSRATE) {
            if (addGrass(i, j) == true) {
              this.masterGrassCount += 1;
            }
          }
        }
      }
    }
  }
  
  
  /**
   * eliminateZero
   * Nullifies all entities with zero health
   */
  void eliminateZero() {
    for (int i = 0; i<entMap.length; i++) {
      for (int j = 0; j < entMap[0].length; j++)
        if ((entMap[i][j] != null) && (entMap[i][j].getHealth() <= 0)) {
        entMap[i][j] = null;
      }
    } 
  }
  
  
  /**
   * resetMoveConditions
   * Resets all entity conditions to false
   * Should be used at the start of each turn to allow all entities to be moved again
   * Condition prevents an entity from being moved twice in a turn through 2D array iteration
   */
  void resetMoveConditions() {
    for (int i = 0; i<entMap.length; i++) {
      for (int j = 0; j<entMap[0].length; j++) {
        if (entMap[i][j] instanceof Sheep) {
          ((Sheep)entMap[i][j]).setMoveCondition(false);
        } else if (entMap[i][j] instanceof Wolf) {
          ((Wolf)entMap[i][j]).setMoveCondition(false);
        }
      }
    }
  }
  
  
  /**
   * generateRandomSheep
   * Spawns random sheep based on number of expected spawns
   * @param instances, The number of sheep to initially spawn
   */
  void generateRandomSheep(int instances) {
    int generated = 0;
    int randomX;
    int randomY;
    
    while ((generated < instances) && (this.checkFull() == false)) {
      randomY = rand.nextInt(this.mapSize);
      randomX = rand.nextInt(this.mapSize);
      
      if (entMap[randomY][randomX] == null) {
        if (addSheep(randomY, randomX) == true) {
          generated +=1;
          this.masterSheepCount += 1;
        }
      }
      
    }
  }
  
  
  /**
   * generateRandomWolf
   * Spawns random wolves based on number of expected spawns
   * @param instances, The number of wolves to initially spawn
   */
  void generateRandomWolf(int instances) {
    int generated = 0;
    int randomX;
    int randomY;
    
    while ((generated < instances) && (this.checkFull() == false)) {
      randomY = rand.nextInt(this.mapSize);
      randomX = rand.nextInt(this.mapSize);
      
      if (entMap[randomY][randomX] == null) {
        if (addWolf(randomY, randomX) == true) {
          generated +=1;
          this.masterWolfCount += 1;
          
        }
      }
      
    }
  }
  
  
  
  
  /**
   * replace
   * Replaces new array position with a new entity
   * @param entity, The entity that is being copied
   * @param newY, The new Y coordinate being copied in
   * @param newX, The new X coordinate being copied in
   */
  void replace(Entity entity, int newY, int newX) {
    if ((entity instanceof Sheep)&&(entMap[newY][newX] instanceof Sheep)) {
      System.out.println("ERROR: SHEEP REPLACING SHEEP");
    }
    
    int oldY = entity.getY();
    int oldX = entity.getX();

    entity.setY(newY);
    entity.setX(newX);
    entMap[newY][newX] = entity;
  }
  
  
  /**
   * breedSheep
   * Breeds two sheep, takes away health, resets cooldown count
   * @param firstSheep, First sheep
   * @param secondSheep, The new Y coordinate being copied in
   */
  void breedSheep(Entity firstSheep, Entity secondSheep) {
    ((Sheep)firstSheep).takeDamage(10);
    ((Sheep)firstSheep).resetCoolDown();
    ((Sheep)firstSheep).addFitness();
    ((Sheep)secondSheep).takeDamage(10);
    ((Sheep)secondSheep).resetCoolDown();
    ((Sheep)secondSheep).addFitness();
    generateRandomSheep(1);
  }
  
  /**
   * breedWolf
   * Breeds two wolves, takes away health, resets cooldown count
   * @param firstWolf, First wolf
   * @param secondWolf, Second wolf
   */
  
  void breedWolf(Entity firstWolf, Entity secondWolf) {
    ((Wolf)firstWolf).takeDamage(10);
    ((Wolf)firstWolf).resetCoolDown();
    ((Wolf)firstWolf).addFitness();
    ((Wolf)secondWolf).takeDamage(10);
    ((Wolf)secondWolf).resetCoolDown();
    ((Wolf)secondWolf).addFitness();
    generateRandomWolf(1);
  }
  
  
  /**
   * moveEntities
   * Moves all entities, facilitates all interactions
   */
  void moveEntities() {
    for (int i = 0; i < entMap.length; i++) {
      for (int j = 0; j < entMap.length; j++) {

        // SHEEP
        if ((entMap[i][j] instanceof Sheep) && (((Sheep)entMap[i][j]).getMoveCondition() == false)) {
          entMap[i][j].takeDamage(1);
          
          // Get the next move, give the array to the sheep
          int[] newCoords = ((Sheep)entMap[i][j]).getMove(entMap);
          int newY = newCoords[0];
          int newX = newCoords[1];
          
          // Take away 1 health, guaranteed -1 per turn
          entMap[i][j].takeDamage(1);
          this.eliminateZero();
          
          // If the sheep survives the turn
          if (entMap[i][j] instanceof Sheep) {
            
            // Don't do anything if it stays
            if ((i == newY) && (j == newX)) {
              ((Sheep)entMap[i][j]).setMoveCondition(true);

            // Interaction with Grass
            } else if (entMap[newY][newX] instanceof Grass) {
              entMap[i][j].gainHealth(entMap[newY][newX].getHealth());
              entMap[newY][newX] = null;
              this.replace(entMap[i][j], newY, newX);
              ((Sheep)entMap[newY][newX]).setMoveCondition(true);
              entMap[i][j] = null;
            
            // Interaction with Sheep
            } else if (entMap[newY][newX] instanceof Sheep) {
              this.breedSheep(entMap[i][j], entMap[newY][newX]);
              //System.out.println(i + " " + " " + j + " Breeding with " + newY + " " + newX);
              ((Sheep)entMap[i][j]).setMoveCondition(true);
              
              
            // Move to blank spot
            } else {
              this.replace(entMap[i][j], newY, newX);
              ((Sheep)entMap[newY][newX]).setMoveCondition(true);
              entMap[i][j] = null;
            }
          }
          
          
        // WOLF
        } else if ((entMap[i][j] instanceof Wolf) && (((Wolf)entMap[i][j]).getMoveCondition() == false)) {
          
          // Take away 1 health, guaranteed -1 per turn
          entMap[i][j].takeDamage(1);
      
          // Get the next move, give the array to the wolf
          int[] newCoords = ((Wolf)entMap[i][j]).getMove(entMap);
          //int[] newCoords = ((Sheep)entMap[i][j]).getMove(entMap);
          int newY = newCoords[0];
          int newX = newCoords[1];
          
          // Eliminate entity if health is equal to 0
          this.eliminateZero();
          
          // If the wolf survives the turn
          if (entMap[i][j] instanceof Wolf) {
            
            // Don't do anything if it stays
            if ((i == newY) && (j == newX)) {
              ((Wolf)entMap[i][j]).setMoveCondition(true);
              
            // Interaction with Sheep
            } else if (entMap[newY][newX] instanceof Sheep) {
              entMap[i][j].gainHealth(entMap[newY][newX].getHealth()/4);
              entMap[newY][newX] = null;
              this.replace(entMap[i][j], newY, newX);
              ((Wolf)entMap[newY][newX]).setMoveCondition(true);
              entMap[i][j] = null;
            
            // Interaction with Wolf
            } else if (entMap[newY][newX] instanceof Wolf) {
              if (((Wolf)entMap[newY][newX]).getGender() == ((Wolf)entMap[i][j]).getGender()) {
                //System.out.println("\nA " + ((Wolf)entMap[i][j]).getGender() + " is attacking a " + ((Wolf)entMap[newY][newX]).getGender());
                //System.out.println("The first wolf sees a differential of " + ((Wolf)entMap[i][j]).compareTo(entMap[newY][newX]));
                entMap[newY][newX].takeDamage(10);
              } else {
                this.breedWolf(entMap[i][j], entMap[newY][newX]);
              }
              //System.out.println(i + " " + " " + j + " Breeding with " + newY + " " + newX);
              ((Wolf)entMap[i][j]).setMoveCondition(true);
              
              
            // Move into new spot if null or instance of grass (trample)
            } else {
              this.replace(entMap[i][j], newY, newX);
              ((Wolf)entMap[newY][newX]).setMoveCondition(true);
              entMap[i][j] = null;
            }
          }
        }
      }
    }
  }
  
  
  /**
   * checkExtinction
   * Checks for extinction of one animal
   * @return boolean, Condition of extinction (either sheep or wolf)
   */
  
  boolean checkExtinction() {
    if ((this.sheepAlive <= 0) || (this.wolvesAlive <= 0)) {
      return true;
    } else {
      return false;
    }
    
  }
  
  
}