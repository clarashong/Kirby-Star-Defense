import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player, Kirby, is who the user controls to play the game. Kirby moves up and down and faces left and right with the arrow keys. 
 * Kirby will shoot a projectile when the space bar is pressed, and it shoots in order to protect the rainbow ores from the enemies. 
 */
public class Player extends Actor
{
    //sounds and graphics 
    private GreenfootImage stillPose, stillPoseLeft, shootingPose, shootingPoseLeft; //images
    private GreenfootSound[] shootSounds; 
    private int shootSoundsIndex; 
    
    //Variables
    private static boolean facingRight; 
    private int playerSpeed;
    private int actCounter;
    private int currentAct;
    public Player() {
        //Kirby has two poses, still and mouth open - it opens its mouth when it's shooting 
        stillPose = new GreenfootImage ("KirbyMouthClosed.png");
        shootingPose = new GreenfootImage ("KirbyMouthOpen.png");
        stillPoseLeft = new GreenfootImage ("KirbyMouthClosedLeft.png");
        shootingPoseLeft = new GreenfootImage ("KirbyMouthOpenLeft.png");
        facingRight = true; 
        
        setImage(stillPose);
        
        actCounter = 0; 
        playerSpeed = 6; 
        
        shootSoundsIndex = 0; 
        shootSounds = new GreenfootSound[20]; 
        //Using Mr. Cohen's way of creating an array of sounds so that they can be repeated better. 
        for (int i = 0; i < shootSounds.length; i++) {
            shootSounds[i] = new GreenfootSound("shoot.wav"); //Kirby's shooting sound
            shootSounds[i].setVolume(80); 
        }
    }
    
    //In the act method, the object will check what keys are being pressed, as well as set its iamge based on which direction its facing. 
    public void act() {
        checkKeys();
        //Waits checks the current act a bit so that it doesn't flip its image so fast. 
        if (currentAct + 5 == actCounter ) {
            if (!facingRight) {
                    setImage(stillPoseLeft);
            } else {
                setImage(stillPose);
            }
        }
        actCounter++; 
    }
    
    /** 
     * This method takes what key is pressed in order to decide how Kirby will move. it also includes the keys that activate
     * the cheat codes. For example, "b" will bring the user immediately to the boss battle, and "7" will bring the user to 
     * wave 7. 
     */
    public void checkKeys() {
        String key = Greenfoot.getKey(); //Greenfoot.getKey() is used for shooting, so that there is a gap between shots. 
        GameWorld g = (GameWorld)getWorld(); 
        if (Greenfoot.isKeyDown("down") && getY() < 740) { //boundary of 740 is set so that the player doesn't go off the screen
            //moving down 
            setLocation(getX(), getY() + playerSpeed);
        } else if (Greenfoot.isKeyDown("up") && getY() > 250) { //boundary of 250 to make sure player doesn't move above the grass
            //moving up 
            setLocation(getX(), getY() - playerSpeed);
        } else if (Greenfoot.isKeyDown("left")) {
            if (facingRight) {
                //turns in the left direction if the player is not already in the left position. 
                setImage(stillPoseLeft);
                facingRight = false; //false -> no longer facing right 
            }
        } else if (Greenfoot.isKeyDown("right")) {
            if (!facingRight) {
                //makes the player face right
                setImage(stillPose);
                facingRight = true;
            }
        }
        
        //If there's a key down that's not including the arrow keys (which are already considered for above) 
        if (key != null){
            if  (Greenfoot.isKeyDown("space")){
                //shoot
                if (!facingRight) {
                    //sets the image where Kirby's mouth is open
                    setImage(shootingPoseLeft);
                    shoot(false); //false-> indicates the projctile that the player is facing left, so that it can also move left
                } else {
                    setImage(shootingPose);
                    shoot(true);
                }
                currentAct = actCounter; 
            } else if (Greenfoot.isKeyDown("b")) {
                //skips to boss battle - wave 10 
                g.setWave(10);
                g.setScore(300);
            } else if  (Greenfoot.isKeyDown("7")) {
                //skips to wave 7 
                g.setWave(7); 
                g.setScore(150); 
            }
        }
    }
    
    //shoots based on player direction 
    public void shoot(boolean right) {
        Projectile p = new Projectile(right, "player");
        getWorld().addObject(p, getX() , getY()); //adding a projectile to the world 
        makeShotSound(); 
    }
    
    //method that plays the shooting sound 
    public void makeShotSound() {
        shootSounds[shootSoundsIndex].play(); //plays sound 
        shootSoundsIndex++; 
        //if the index has passed the highest index of the sound array, it will reset and go back to zero. 
        if (shootSoundsIndex > shootSounds.length - 1) {
            shootSoundsIndex = 0; 
        }
    }
}
