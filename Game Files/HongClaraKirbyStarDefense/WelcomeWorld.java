import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This is the player's welcome screen
 */
public class WelcomeWorld extends World
{
    private GreenfootImage background; 
    //Class is based off Mr. Cohen's WelcomeWorld from the 40 Minute Shooter
    public WelcomeWorld()
    {    
        //Creating the welcome screen
        super(1200, 800, 1); 
        background = new GreenfootImage("WelcomeScreen.png"); 
        setBackground (background);  
    }
    
    //Loads in the rules if the player presses enter. 
    public void act() {
        if (Greenfoot.isKeyDown("enter")){
            Greenfoot.setWorld (new RulesWorld());
        }
    }
}
