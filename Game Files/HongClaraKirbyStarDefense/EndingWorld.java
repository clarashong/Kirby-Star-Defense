import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This is the world that shows up at the end of the game. 
 */
public class EndingWorld extends World
{
    private GreenfootImage background; 
    private GreenfootSound music; 
    public EndingWorld(boolean win)
    {    
        // Create a new world with 1200x800 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        //Takes in whether the player has won or not to decide the background
        //Player wins when they beat the boss
        if (win) {
            background = new GreenfootImage("WinningScreen.png"); 
            music = new GreenfootSound("WinMusic.mp3"); 
        } else {
            //player loses when one of their ores is completely destroyed
            background = new GreenfootImage("LosingScreen.png"); 
            music = new GreenfootSound("LoseMusic.mp3"); 
        }
        setBackground (background); 
        music.play(); 
    }
    
    public void act() {
        //When user presses enter, the GameWorld is brought back in and the game restarts
        if (Greenfoot.isKeyDown("enter")){
            Greenfoot.setWorld (new GameWorld());
            music.stop(); 
        }
    }
    
    //Stops the music when the Greenfoot game is stopped
    public void stopped() {
        music.stop();
    }
}
