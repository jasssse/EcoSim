/*
 * Entity.java
 * @version 1.0
 * @since April 24, 2019
 * Base class for all other entities in simulation
 */


abstract class Entity {
  
  // Health points
  private int health;
  // X Position on map
  private int xPos;
  // Y Position on Map
  private int yPos;
  // Moved condition
  private boolean moved;
  
  Entity(int health, int y, int x) {
    this.moved = true;
    this.health = health;
    this.yPos = y;
    this.xPos = x;
  }
  
    
  /**
   * getHealth 
   * Retrieves entity's health
   * @return int, true if the operation was a success, false otherwise.
   */
  int getHealth() {
    return health;
  }
  
    
  /**
   * takeDamage 
   * Decreases entity's health
   * @param points, Amount of health taken off
   */
  void takeDamage(int points) {
    this.health -= points;
  }
  
    
  /**
   * gainHealth 
   * Increases entity's health
   * @param points, Amount of health added
   */
  void gainHealth(int points) {
    this.health += points;
  }
  
  /**
   * getX 
   * Retrieves the X position
   * @return int, X position of entity
   */
  int getX() {
    return xPos;
  }
  
  
  /**
   * getY 
   * Retrieves the Y position
   * @return int, Y position of entity
   */
  int getY() {
    return yPos;
  }
  
  /**
   * setX
   * Updates X position of entity
   * @param newX, new X position
   */
  void setX(int newX) {
    this.xPos = newX;
  }
  
  /**
   * setY
   * Updates Y position of entity
   * @param newY, new Y position
   */
  void setY(int newY) {
    this.yPos = newY;
  }
  
  
  /**
   * getHealthString
   * Retrieves the health of the entity as a string to be used on the display
   * @return String, Health points of the entity
   */
  String getHealthString() {
    return Integer.toString(health);
  }
  

  
  
  
}