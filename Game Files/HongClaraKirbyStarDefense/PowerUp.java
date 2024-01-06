import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Power-ups are what aid the player in surviving as well as defeating the enemies. 
 * A guide to what each power-up does is included in the multi-block comment in the GameWorld. 
 * The player must shoot the power-ups in order to use their abilities. 
 */
public class PowerUp extends Actor
{
    //graphics and sound 
    private GreenfootImage image; 
    private GreenfootImage[] images; 
    private GreenfootSound explosion; 
    private GreenfootSound heal; 
    
    private String currentPowerUp; //which power up is this object: "bomb", "hammer", or "red star" 
    private int actCounter; 
    private static boolean hammerUsed;
    
    //Constructor
    public PowerUp (int powerUpTypes) {
        int random = Greenfoot.getRandomNumber(powerUpTypes); //decides a number that will decide the power up 
        currentPowerUp = findCurrentPowerUp(random); //represents the powerup as a string based on the randomly generated value. 
        
        explosion = new GreenfootSound("explosion.wav"); 
        heal = new GreenfootSound("magic.wav"); 
        heal.setVolume(80); 
        explosion.setVolume(80); 
        
        //initiating graphics 
        images = new GreenfootImage[3];
        images[0] = new GreenfootImage("bomb.png"); 
        images[1] = new GreenfootImage("RedStarPowerUp.png");
        images[2] = new GreenfootImage("Hammer.png");
        image = images[random]; 
        setImage(image); 
        
        actCounter = 0; 
    }
    
    public void act() {
        Boss b = (Boss)getOneIntersectingObject(Boss.class); 
        if (b != null){
            //removes the object if it's touching the boss to avoid weird overlapping of the images. 
            getWorld().removeObject(this); 
            return; 
        }
        
        //every 3 acts, the image gets a bit more transparent 
        if (actCounter % 3 == 0) {
            image.setTransparency(image.getTransparency() - 1); //power up fades 
        }
        //The power-up will be removed when it is completely transparent - it can't be used by the player anymore 
        if (image.getTransparency() == 0) {
            getWorld().removeObject(this); //removes object once completely transparent
        }
        actCounter++; 
    }
    
    public void addedToWorld(World w) {
        int randomY = Greenfoot.getRandomNumber(5);
        int randomX = Greenfoot.getRandomNumber(2);
        int xDistanceFromCenter = Greenfoot.getRandomNumber(300) + 250;
        int[] yArray = {260, 380, 500, 620, 740}; //y coordinates where powerup can be spawned
        int y = yArray[randomY]; 
        int x; 
        //left side 
        if (randomX == 0) {
            x = 600 - xDistanceFromCenter; 
        } else {
            x = 600 + xDistanceFromCenter;
        }
        setLocation (x, y);
    }
    
    //There are 3 types of power ups, the powerup is decided based on a generated number above (the parameter).  
    //This method assigns the object to a String. 
    public String findCurrentPowerUp(int imageNum) {
        if (imageNum == 0) {
            return "bomb"; //destroys all enemy units on the lawn 
        } else if (imageNum == 1) {
            return "red star"; //heals all rocks 
        } else {
            return "hammer"; //Instantly kills all the enemies in its lane
        }
    }
    
    //getter method that can be used by other classes
    public String getCurrentPowerUp() {
        return currentPowerUp;
    }
    
    //This method activate each power-up's ability. 
    public void usePower(){
        GameWorld g = (GameWorld)getWorld();
        Projectile p = (Projectile)getOneIntersectingObject(Projectile.class);
        if (currentPowerUp.equals("bomb")) {
            explosion.play(); //makes explosion sound when used
            g.removeObjects(getWorld().getObjects(Enemy.class)); //removes all enemy class objects
            g.addScore(5); //gains 5 points when bomb is used
            g.removeObject(this); //the power up is plways removed once it is used. 
        } else if (currentPowerUp.equals("red star")){
            p.setIsRed(true);
            p.setRedsLeft(10); //Gives the projectile class 10 red star ammo. 
            g.removeObject(this);
        } else if (currentPowerUp.equals("hammer")) {
            heal.play(); //makes "healing sound when used. 
            for (Wall w : getWorld().getObjects(Wall.class)){
                w.setMax(); //for every Wall object in the GameWorld, their hp will be set to the max
            }
            g.removeObject(this);
        }
    }
}
