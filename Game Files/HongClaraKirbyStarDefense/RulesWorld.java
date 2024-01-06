import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class RulesWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RulesWorld extends World
{
    private GreenfootImage background; 
    //It is a bit laggy when pressing enter
    //Class is based off Mr. Cohen's WelcomeWorld from the 40 Minute Shooter
    public RulesWorld()
    {    
        // Create a new world with 1200x800 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        background = new GreenfootImage("RulesScreen.png"); 
        setBackground (background); 
    }
    
    //When player presses enter, the GameWorld will be set, and the game will begin
    public void act() {
        if (Greenfoot.isKeyDown("enter")){
            Greenfoot.setWorld (new GameWorld());
        }
    }
}
