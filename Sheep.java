import java.util.Random;

/*
 * Entity.java
 * @version 1.0
 * @since April 24, 2019
 * Base class for all other entities in simulation
 */

class Sheep extends Entity{
  // Random object for random number generation
  Random rand = new Random();
  // Moved condition (ensure one move per turn)
  private boolean moved;
  // Gender integer - 0 = male, 1 = female
  private final int gender;
  // Breeding cooldown counter
  private int breedingCoolDown = 0;
  // Constant minimum cooldown before breeding
  private final int MINIMUMCOOLDOWN = 3;
  // Number of offspring produced (known as fitness)
  private int fitness = 0;
  
  Sheep(int health, int y, int x) {
    super(health, y, x);
    this.moved = true;
    this.gender = rand.nextInt(2);
  }
  
  /**
   * getGender 
   * Retrieves the gender of the sheep
   * @return int, Gender of the Sheep
   */
  int getGender() {
    return this.gender;
  }
  
  /**
   * getMoveCondition 
   * Retrieves boolean condition for being moved
   * @return boolean, True if moved this turn, false if not
   */
  boolean getMoveCondition() {
    return moved;
  }
  
  
  /**
   * setMoveCondition 
   * Sets new condition for being moved this turn
   * @param condition, New boolean condition for being moved
   */
  void setMoveCondition(boolean condition) {
    this.moved = condition;
  }

  /**
   * getBreedingCoolDown 
   * Retrieves the current breeding cooldown count
   * @return int, Turns since last breeding occured
   */
  int getBreedingCoolDown() {
    return this.breedingCoolDown;
  }
  
  /**
   * addCoolDown() 
   * Adds one to breeding cooldown counter
   */
  void addCoolDown() {
    this.breedingCoolDown +=1;
  }
  
  /**
   * resetCoolDown() 
   * Resets breeding cooldown to zero
   */
  void resetCoolDown() {
    this.breedingCoolDown = 0;
  }
  
  /**
   * addFitness() 
   * Adds one fitness count, used when new offspring produced
   */
  void addFitness() {
    this.fitness +=1;
  }
  
  /**
   * getFitness() 
   * Retrieves current fitness count
   * @return int, Current fitness count
   */
  int getFitness() {
    return this.fitness;
  }
  
  
  /**
   * checkBreedable() 
   * Checks if the entity being tested is breedable to the current sheep
   * @param testSheep, The potential mate
   * @return boolean, True if sheep is breedable, false if not
   */
  private boolean checkBreedable(Entity testSheep) {
    boolean breedable = true;
    
    // Health
    if ((this.getHealth() < 25) || (((Sheep)testSheep).getHealth() < 25)) {
      breedable = false;
    }
    // Gender
    if ((this.gender + (((Sheep)testSheep).getGender())) != 1) {
      breedable = false;
    }
    // Cooldown
    if ((this.breedingCoolDown < MINIMUMCOOLDOWN) || (((Sheep)testSheep).getBreedingCoolDown() < MINIMUMCOOLDOWN)) {
      breedable = false;
    }
    return breedable;
  }
  
  /**
   * checkEatable() 
   * Checks if the space being tested contains an eatable grass
   * @param entityArray, the current map
   * @param testY, Test Y coordinate
   * @param testX, Test X coordinate
   * @return boolean, True if spot contains a grass, false if not
   */
  private boolean checkEatable(Entity[][] entityArray, int testY, int testX) {
    if (entityArray[testY][testX] instanceof Grass) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * checkValidMove() 
   * Checks if the proposed move is valid, used for random generation
   * @param entityArray, the current map
   * @param testY, Test Y coordinate
   * @param testX, Test X coordinate
   * @return boolean, True if spot is a valid move, false if not
   */
  private boolean checkValidMove(Entity[][] entityArray, int testY, int testX) {
    if (entityArray[testY][testX] instanceof Sheep) {
      Entity refEntity = entityArray[testY][testX];
      if (checkBreedable(refEntity) == true) {
        return true;
      } else {
        return false;
      }
    } else if (entityArray[testY][testX] instanceof Wolf){
      return false;
    } else if (entityArray[testY][testX] == null) {
      return true;
    } else {
      return true;
    }
  }
  
  /**
   * generateSmartMove() 
   * Determines move based on current health conditions
   * @param entityArray, the current map
   * @param coords, Array of current coordinates
   * @param maxIndex, Limit index of map
   * @return String, The letter representing a move of either up, down, left, right, or stay (if random generation is reached)
   */
  String generateSmartMove(Entity[][] entityArray, int[] coords, int maxIndex) {
    int direction;
    int currentY = coords[0];
    int currentX = coords[1];
    String possibleMoves;
    
    // Prioritize breeding;
    if (this.getHealth() >= 25) {
      
      if (currentY > 0) {
        if (entityArray[currentY-1][currentX] instanceof Sheep) {
          if (checkBreedable(entityArray[currentY-1][currentX]) == true) {
            return "u";
          }
        }
      }
      if (currentY < maxIndex) {
        if (entityArray[currentY+1][currentX] instanceof Sheep) {
          if (checkBreedable(entityArray[currentY+1][currentX]) == true) {
            return "d";
          }
        }
      }
      if (currentX > 0) {
        if (entityArray[currentY][currentX-1] instanceof Sheep) {
          if (checkBreedable(entityArray[currentY][currentX-1]) == true) {
            return "l";
          }
        }
      }
      if (currentX < maxIndex) {
        if (entityArray[currentY][currentX+1] instanceof Sheep) {
          if (checkBreedable(entityArray[currentY][currentX+1]) == true) {
            return "r";
          }
        }
      }
      
    // Health less than 20 - prioritize eating
    } else if (this.getHealth() < 25) {
      
      if (currentY > 0) {
        if (checkEatable(entityArray, currentY-1, currentX) == true) {
          return "u";
        }
      }
      if (currentY < maxIndex) {
        if (checkEatable(entityArray, currentY+1, currentX) == true) {
          return "d";
        }
      }
      if (currentX > 0) {
        if (checkEatable(entityArray, currentY, currentX-1) == true) {
          return "l";
        }
      }
      if (currentX < maxIndex) {
        if (checkEatable(entityArray, currentY, currentX+1) == true) {
          return "r";
        }
      }
    }
    return generateRandomMove(entityArray, coords, maxIndex);
    
  }
  
  /**
   * generateRandomMove() 
   * Generates a random move based on current coordinates, checks if move is valid before sending,
   * only called when a smarter move cannot be determined (nothing useful around)
   * @param entityArray, the current map
   * @param coords, Array of current coordinates
   * @param maxIndex, Maximum index of the map
   * @return String, A String representing the move
   */
  String generateRandomMove(Entity[][] entityArray, int[] coords, int maxIndex) {
    int direction;
    int currentY = coords[0];
    int currentX = coords[1];
    boolean generateMove = true;
    String move = "";
    
    while (generateMove == true) {
       direction = rand.nextInt(5);
       
       // Up
       if (direction == 0) {
         if (currentY > 0) {
           if (checkValidMove(entityArray, currentY-1, currentX) == true) {
             move = "u";
             generateMove = false;
           }
         }
         
       // Down
       } else if (direction == 1) {
         if (currentY < maxIndex) {
           if (checkValidMove(entityArray, currentY+1, currentX) == true) {
             move = "d";
             generateMove = false;
           }
         }
         
       // Left
       } else if (direction == 2) {
         if (currentX > 0) {
           if (checkValidMove(entityArray, currentY, currentX-1) == true) {
             move = "l";
             generateMove = false;
           }
         }
         
       // Right
       } else if (direction == 3) {
         if (currentX < maxIndex) {
           if (checkValidMove(entityArray, currentY, currentX+1) == true) {
             move = "r";
             generateMove = false;
           }
         }
         
       } else if (direction == 4) {
         move = "s";
         generateMove = false;
       }
    }
    
    return move;
    
    
  }
  
  /**
   * getMove() 
   * Gets a move with other methods, translates into an array of new coordinates
   * @param entityArray, the current map
   * @return coords, Array of new Coordinates
   */
  
  int[] getMove(Entity[][] entityArray) {
    int[] coords = {this.getY(), this.getX()};
    int maxIndex = entityArray.length-1;
    
    String move = generateSmartMove(entityArray, coords, maxIndex);
    
      
    // 0 = up
    // 1 = down
    // 2 = left
    // 3 = right
    // 4 = stay
    
    if (move.equals("u")) {
      coords[0] -= 1;
    } else if (move.equals("d")) {
      coords[0] += 1;
    } else if (move.equals("l")) {
      coords[1] -= 1;
    } else if (move.equals("r")) {
      coords[1] += 1;
    } 
    
    return coords;
  }
    
  
  
  
}