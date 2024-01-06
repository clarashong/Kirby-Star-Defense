import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Enemies: The player has to destroy them. 
 * They try to destroy the rocks. 
 * Enemy Guide: Waddle Dees -> orange monkeys, need two shots to kill, walk towards rocks to destroy them. 
 * Awoofies -> Dog creatures, need one shot to kill, dashes towards rocks to destroy them, but they do less damage than waddle dees. 
 * Scarfies -> Tiny orange blobs, they need one shot to kill, they lie along the ends of the lanes and shoot the walls. 
 */
public class Enemy extends Actor
{
    //graphics and sound
    private GreenfootImage[] images;
    private GreenfootImage image; 
    private GreenfootSound pop; 
    
    private boolean right; 
    
    private int actCounter = 0; 

    private int speed, damage, hp; //stats 
    private int randomEnemy; //how many enemy types are available, and the type of enemy (1, 2, or 3)
    
    public Enemy(int enemyTypes) {
        randomEnemy = Greenfoot.getRandomNumber(enemyTypes); 
        
        //index 0 in the array is for waddle dees, index 1 is for awoofy's, and 2 is for scarfy's
        int[] speeds = {1, 3, 1};
        int [] damages = {2,1, 0}; 
        int[] maxHps = {200, 100, 100}; //different types of enemies have hp
        speed = speeds[randomEnemy];
        damage = damages[randomEnemy];
        hp = maxHps[randomEnemy];
        
        //setting graphics and sound 
        images = new GreenfootImage[3];
        images[0] = new GreenfootImage("WaddleDee.png");
        images[1] = new GreenfootImage("Awoofy.png");
        images[2] = new GreenfootImage("scarfy.png"); 
        image = images[randomEnemy]; //picks between the possible enemy types
        pop = new GreenfootSound("pop.wav");  
        pop.setVolume(70); 
        setImage(image);
    }
    
    public void act()
    {
        Boss b = (Boss)getOneIntersectingObject(Boss.class); 
        if (b != null) {
            getWorld().removeObject(this); //removing all enemies who are touching the boss. This is to avoid weird overlapping. 
            return;
        }
        
        Wall w; 
        if (right) {
            if(randomEnemy == 0) {
                //randomEnemy == 0 -> waddle dees 
                //They have a separate condition just for more precise collision detection based on their image. 
                w = (Wall)getOneObjectAtOffset(-45, 0, Wall.class);
            } else {
                //This is mostly for awoofies because scarfies don't move. 
                w = (Wall)getOneObjectAtOffset(-30, 0, Wall.class);
            }
        } else {
            if(randomEnemy == 0) {
                w = (Wall)getOneObjectAtOffset(45, 0, Wall.class);
            } else {
                w = (Wall)getOneObjectAtOffset(30, 0, Wall.class);
            }
        }
        if (w == null) { 
            //enemy keeps moving until it's touching a rock/wall 
            if (right) {
                move (-speed);
                shootWall(false); //method is called for scarfies 
            } else {
                move (speed);
                shootWall(true);
            }
        } else {
            //Is touching wall -> starts damaging it
            w.damageMe(damage); 
        }
        actCounter++; 
    }
    
    //The addedToWorld method here used to decide which lane the enemy will spawn in. 
    public void addedToWorld( World w) {
        int randomX = Greenfoot.getRandomNumber(2); //Generates 0 or 1, will decide if enemy comes in on the right or the left
        int randomY = Greenfoot.getRandomNumber(5); //Generates number from 0-4, decides the lane. 
        int x, y; //final x and y
        int[] yArray = {200, 320, 440, 560, 680}; //array of the y position of all the lanes
        
        if (randomX == 0) {
            image.mirrorHorizontally(); //fixes the enemy image to be looking in the right direction. 
            x = -60; //spawns a bit off-screen 
            y = yArray[randomY] + 60; // the start of every lane is in the array, but 60 is added so that the enemy is centred in the lane
            right = false;
        } else {
            x = 1260; //spawns off-screen
            y = yArray[randomY] + 60;
            right = true;
        }
        setLocation (x, y);
    }
    
    /**
     * Updates the enemy's hp based on damage caused by a projectile. If the hp is at 0 or less than 0, the enemy will "die" and be removed and
     * a pop sound will be played. 
     */
    public void damageMe(int projectileDamage) {
        hp -= projectileDamage; //taking away damage from the overall hp 
        if (hp <= 0) {
            GameWorld g = (GameWorld)getWorld();
            pop.play(); //playing the pop sound 
            g.removeObject(this);
        }
    }
    
    //only for scarfies
    /**
     * This method makes scarfies shoot a projectile at the wall. It only works for scarfies and if they are in the right position for shooting. 
     * The only parameter is "right" which dictates which way the enemy is facing which is passed onto the projectiles properties, so that the 
     * projectile is shot the right way. 
     */
    public void shootWall(boolean right) {
        Projectile p = new Projectile(right, "enemy");
        if (randomEnemy == 2) { //2 is the represetative number for scarfies 
            if (getX() == 60 || getX() == 1140) {
                speed = 0; //makes sure the scarfies don't move past the desired x-position 
                if (actCounter % 60 == 0) { //shoots every 60 acts 
                    getWorld().addObject(p, getX() , getY());
                } 
            }
        }
    }
}
